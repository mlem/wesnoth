/*
   Copyright (C) 2003 - 2015 by David White <dave@whitevine.net>
   Part of the Battle for Wesnoth Project http://www.wesnoth.org/

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY.

   See the COPYING file for more details.
*/

/**
 *  @file
 *  Video-testprogram, standalone
 */

#include "global.hpp"

#include "font.hpp"
#include "floating_label.hpp"
#include "image.hpp"
#include "log.hpp"
#include "preferences.hpp"
#include "preferences_display.hpp"
#include "sdl/utils.hpp"
#include "sdl/rect.hpp"
#include "sdl/window.hpp"
#include "video.hpp"
#include "sdl/gpu.hpp"

#include <boost/foreach.hpp>

#include <vector>
#include <map>
#include <algorithm>

static lg::log_domain log_display("display");
#define LOG_DP LOG_STREAM(info, log_display)
#define ERR_DP LOG_STREAM(err, log_display)

namespace {
	bool fullScreen = false;
	int disallow_resize = 0;
	GPU_Target *render_target_;
}
void resize_monitor::process(events::pump_info &info) {
	if(info.resize_dimensions.first >= preferences::min_allowed_width()
	&& info.resize_dimensions.second >= preferences::min_allowed_height()
	&& disallow_resize == 0) {
		preferences::set_resolution(info.resize_dimensions);
	}
}

resize_lock::resize_lock()
{
	++disallow_resize;
}

resize_lock::~resize_lock()
{
	--disallow_resize;
}

static unsigned int get_flags(unsigned int flags)
{
	/* The wanted flags for the render need to be evaluated for SDL2. */
#ifdef SDL_GPU
	flags |= SDL_OPENGLBLIT;
#endif
#if !SDL_VERSION_ATLEAST(2, 0, 0)
	// SDL under Windows doesn't seem to like hardware surfaces
	// for some reason.
#if !(defined(_WIN32) || defined(__APPLE__))
		flags |= SDL_HWSURFACE;
#endif
#endif


	if((flags&SDL_FULLSCREEN) == 0)
#if SDL_VERSION_ATLEAST(2, 0, 0)
		flags |= SDL_WINDOW_RESIZABLE;
#else
		flags |= SDL_RESIZABLE;
#endif

	return flags;
}

namespace {
struct event {
	int x, y, w, h;
	bool in;
	event(const SDL_Rect& rect, bool i) : x(i ? rect.x : rect.x + rect.w), y(rect.y), w(rect.w), h(rect.h), in(i) { }
};
#if !SDL_GPU && !SDL_VERSION_ATLEAST(2, 0, 0)
bool operator<(const event& a, const event& b) {
	if (a.x != b.x) return a.x < b.x;
	if (a.in != b.in) return a.in;
	if (a.y != b.y) return a.y < b.y;
	if (a.h != b.h) return a.h < b.h;
	if (a.w != b.w) return a.w < b.w;
	return false;
}
bool operator==(const event& a, const event& b) {
	return a.x == b.x && a.y == b.y && a.w == b.w && a.h == b.h && a.in == b.in;
}
#endif

struct segment {
	int x, count;
	segment() : x(0), count(0) { }
	segment(int x, int count) : x(x), count(count) { }
};


std::vector<SDL_Rect> update_rects;
std::vector<event> events;
std::map<int, segment> segments;

#if !SDL_GPU && !SDL_VERSION_ATLEAST(2, 0, 0)
static void calc_rects()
{
	events.clear();

	BOOST_FOREACH(SDL_Rect const &rect, update_rects) {
		events.push_back(event(rect, true));
		events.push_back(event(rect, false));
	}

	std::sort(events.begin(), events.end());
	std::vector<event>::iterator events_end = std::unique(events.begin(), events.end());

	segments.clear();
	update_rects.clear();

	for (std::vector<event>::iterator iter = events.begin(); iter != events_end; ++iter) {
		std::map<int, segment>::iterator lower = segments.find(iter->y);
		if (lower == segments.end()) {
			lower = segments.insert(std::make_pair(iter->y, segment())).first;
			if (lower != segments.begin()) {
				std::map<int, segment>::iterator prev = lower;
				--prev;
				lower->second = prev->second;
			}
		}

		if (lower->second.count == 0) {
			lower->second.x = iter->x;
		}

		std::map<int, segment>::iterator upper = segments.find(iter->y + iter->h);
		if (upper == segments.end()) {
			upper = segments.insert(std::make_pair(iter->y + iter->h, segment())).first;
			std::map<int, segment>::iterator prev = upper;
			--prev;
			upper->second = prev->second;
		}

		if (iter->in) {
			while (lower != upper) {
				++lower->second.count;
				++lower;
			}
		} else {
			while (lower != upper) {
				lower->second.count--;
				if (lower->second.count == 0) {
					std::map<int, segment>::iterator next = lower;
					++next;

					int x = lower->second.x, y = lower->first;
					unsigned w = iter->x - x;
					unsigned h = next->first - y;
					SDL_Rect a = sdl::create_rect(x, y, w, h);

					if (update_rects.empty()) {
						update_rects.push_back(a);
					} else {
						SDL_Rect& p = update_rects.back(), n;
						int pa = p.w * p.h, aa = w * h, s = pa + aa;
						int thresh = 51;

						n.w = std::max<int>(x + w, p.x + p.w);
						n.x = std::min<int>(p.x, x);
						n.w -= n.x;
						n.h = std::max<int>(y + h, p.y + p.h);
						n.y = std::min<int>(p.y, y);
						n.h -= n.y;

						if (s * 100 < thresh * n.w * n.h) {
							update_rects.push_back(a);
						} else {
							p = n;
						}
					}

					if (lower == segments.begin()) {
						segments.erase(lower);
					} else {
						std::map<int, segment>::iterator prev = lower;
						--prev;
						if (prev->second.count == 0) segments.erase(lower);
					}

					lower = next;
				} else {
					++lower;
				}
			}
		}
	}
}
#endif


bool update_all = false;
}
#if !SDL_GPU && !SDL_VERSION_ATLEAST(2, 0, 0)
static void clear_updates()
{
	update_all = false;
	update_rects.clear();
}
#endif

namespace {

surface frameBuffer = NULL;
bool fake_interactive = false;

#if SDL_VERSION_ATLEAST(2, 0, 0)
sdl::twindow* window = NULL;
#endif
}

bool non_interactive()
{
	if (fake_interactive)
		return false;
#if SDL_VERSION_ATLEAST(2, 0, 0)
	return false;
#else
	return SDL_GetVideoSurface() == NULL;
#endif
}



GPU_Target *get_render_target()
{
	return render_target_;
}

surface display_format_alpha(surface surf)
{
#if !SDL_VERSION_ATLEAST(2, 0, 0)
	if(SDL_GetVideoSurface() != NULL)
		return SDL_DisplayFormatAlpha(surf);
	else if(frameBuffer != NULL)
		return SDL_ConvertSurface(surf,frameBuffer->format,0);
	else
#else
		UNUSED(surf);
#endif
		return NULL;
}

surface get_video_surface()
{
	return frameBuffer;
}

SDL_Rect screen_area()
{
	return sdl::create_rect(0, 0, frameBuffer->w, frameBuffer->h);
}

void update_rect(size_t x, size_t y, size_t w, size_t h)
{
	update_rect(sdl::create_rect(x, y, w, h));
}

void update_rect(const SDL_Rect& rect_value)
{
	if(update_all)
		return;

	SDL_Rect rect = rect_value;

#if SDL_VERSION_ATLEAST(2, 0, 0)
	surface const fb = NULL;
#else
	surface const fb = SDL_GetVideoSurface();
#endif
	if(fb != NULL) {
		if(rect.x < 0) {
			if(rect.x*-1 >= int(rect.w))
				return;

			rect.w += rect.x;
			rect.x = 0;
		}

		if(rect.y < 0) {
			if(rect.y*-1 >= int(rect.h))
				return;

			rect.h += rect.y;
			rect.y = 0;
		}

		if(rect.x + rect.w > fb->w) {
			rect.w = fb->w - rect.x;
		}

		if(rect.y + rect.h > fb->h) {
			rect.h = fb->h - rect.y;
		}

		if(rect.x >= fb->w) {
			return;
		}

		if(rect.y >= fb->h) {
			return;
		}
	}

	update_rects.push_back(rect);
}

void update_whole_screen()
{
	update_all = true;
}
CVideo::CVideo(FAKE_TYPES type) :
#ifdef SDL_GPU
	shader_(),
#endif
	mode_changed_(false),
	bpp_(0),
	fake_screen_(false),
	help_string_(0),
	updatesLocked_(0)
{
	initSDL();
	switch(type)
	{
		case NO_FAKE:
			break;
		case FAKE:
			make_fake();
			break;
		case FAKE_TEST:
			make_test_fake();
			break;
	}
}

void CVideo::initSDL()
{
#ifdef SDL_GPU
	//800x600 is a dummy value, the actual resolution is set in setMode
	render_target_ = GPU_Init(800, 600, GPU_DEFAULT_INIT_FLAGS);

	if(render_target_ == NULL) {
		ERR_DP << "Could not initialize window: " << SDL_GetError() << std::endl;
		throw CVideo::error();
	}

	const std::string vertex_src = game_config::path + "/data/shaders/default.vert";
	const std::string frag_src = game_config::path + "/data/shaders/default.frag";
	shader_ = sdl::shader_program(vertex_src, frag_src);
	shader_.activate();
#else
	const int res = SDL_InitSubSystem(SDL_INIT_VIDEO | SDL_INIT_NOPARACHUTE);

	if(res < 0) {
		ERR_DP << "Could not initialize SDL_video: " << SDL_GetError() << std::endl;
		throw CVideo::error();
	}
#endif
}

#ifdef SDL_GPU
void CVideo::update_overlay(SDL_Rect *rect)
{
	sdl::timage img(overlay_);
	shader_.set_overlay(img);

	// Re-render the appropriate screen area so that overlay change is visible
	static sdl::timage empty(image::get_texture("images/misc/blank.png"));
	SDL_Rect whole = sdl::create_rect(0, 0, overlay_->w, overlay_->h);
	SDL_Rect *r = rect == NULL ? &whole : rect;
	empty.set_scale(float(r->w) / empty.base_width(), float(r->h) / empty.base_height());
	draw_texture(empty, r->x, r->y);
}
#endif

CVideo::~CVideo()
{
	LOG_DP << "calling SDL_Quit()\n";
	SDL_Quit();
#if SDL_VERSION_ATLEAST(2, 0, 0)
	delete window;
#endif
	LOG_DP << "called SDL_Quit()\n";
}

void CVideo::blit_surface(int x, int y, surface surf, SDL_Rect* srcrect, SDL_Rect* clip_rect)
{
	surface target(getSurface());
	SDL_Rect dst = sdl::create_rect(x, y, 0, 0);

	const clip_rect_setter clip_setter(target, clip_rect, clip_rect != NULL);
	sdl_blit(surf,srcrect,target,&dst);
}

#ifdef SDL_GPU
GPU_Target *CVideo::render_target() const
{
	return render_target_;
}

void CVideo::draw_texture(sdl::timage &texture, int x, int y)
{
	texture.draw(*this, x, y);
}

void CVideo::set_texture_color_modulation(int r, int g, int b, int a)
{
	shader_.set_color_mod(r, g, b, a);
}

void CVideo::set_texture_submerge(float f)
{
	shader_.set_submerge(f);
}

void CVideo::set_texture_effects(int effects)
{
	shader_.set_effects(effects);
}

void CVideo::blit_to_overlay(surface surf, int x, int y)
{
	if (x < 0 || y < 0 || x > overlay_->w || y > overlay_->h) {
		return;
	}
	SDL_Rect r = sdl::create_rect(x, y, surf->w, surf->h);
	SDL_BlitSurface(surf, NULL, overlay_, &r);
	update_overlay(&r);
}

void CVideo::clear_overlay_area(SDL_Rect area)
{
	const Uint32 color = SDL_MapRGBA(overlay_->format, 0, 0, 0, 0);
	Uint32 *pixels = static_cast<Uint32*>(overlay_->pixels);
	for (int x = area.x; x<area.x + area.w; ++x) {
		for (int y = area.y; y<area.y +area.h; ++y) {
			const int index = y * (area.w + overlay_->pitch) + x;
			pixels[index] = color;
		}
	}
	update_overlay(&area);
}

void CVideo::clear_overlay()
{
	overlay_ = create_compatible_surface(overlay_, getx(), gety());
	update_overlay();
}
#endif

void CVideo::make_fake()
{
	fake_screen_ = true;
	frameBuffer = SDL_CreateRGBSurface(SDL_SWSURFACE,16,16,24,0xFF0000,0xFF00,0xFF,0);
	image::set_pixel_format(frameBuffer->format);
}

void CVideo::make_test_fake(const unsigned width,
			const unsigned height, const unsigned bpp)
{
	frameBuffer = SDL_CreateRGBSurface(SDL_SWSURFACE,
			width, height, bpp, 0xFF0000, 0xFF00, 0xFF, 0);
	image::set_pixel_format(frameBuffer->format);

	fake_interactive = true;

}

int CVideo::bppForMode( int x, int y, int flags)
{
	int test_values[3] = {getBpp(), 32, 16};
	BOOST_FOREACH(int &bpp, test_values) {
		if(modePossible(x, y, bpp, flags) > 0) {
			return bpp;
		}
	}

	return 0;
}

int CVideo::modePossible( int x, int y, int bits_per_pixel, int flags, bool current_screen_optimal )
{
#if SDL_VERSION_ATLEAST(2, 0, 0)
	UNUSED(x);
	UNUSED(y);
	UNUSED(flags);
	UNUSED(current_screen_optimal);

	return bits_per_pixel;
#else
	int bpp = SDL_VideoModeOK( x, y, bits_per_pixel, get_flags(flags) );
	if(current_screen_optimal)
	{
		const SDL_VideoInfo* const video_info = SDL_GetVideoInfo();
		/* if current video_info is smaller than the mode checking and the checked mode is supported
		(meaning that probably the video card supports higher resolutions than the monitor)
		that means that we just need to adjust the resolution and the bpp is ok
		*/
		if(bpp==0 && video_info->current_h<y && video_info->current_w<x){
			return bits_per_pixel;
		}
	}
	return bpp;
#endif
}

#if SDL_VERSION_ATLEAST(2, 0, 0)
int CVideo::setMode( int x, int y, int bits_per_pixel, int flags )
{
	update_rects.clear();
	if (fake_screen_) return 0;
	mode_changed_ = true;

	flags = get_flags(flags);

	fullScreen = (flags & FULL_SCREEN) != 0;

	if(!window) {
		window = new sdl::twindow("", 0, 0, x, y, flags, SDL_RENDERER_SOFTWARE);
	} else {
		if(fullScreen) {
			window->full_screen();
		} else {
			window->set_size(x, y);
		}
	}

	frameBuffer = SDL_GetWindowSurface(*window);

	if(frameBuffer != NULL) {
		image::set_pixel_format(frameBuffer->format);
		return bits_per_pixel;
	} else	{
		return 0;
	}
}
#else
int CVideo::setMode( int x, int y, int bits_per_pixel, int flags )
{
	update_rects.clear();
	if (fake_screen_) return 0;
	mode_changed_ = true;

	flags = get_flags(flags);
	const int res = SDL_VideoModeOK( x, y, bits_per_pixel, flags );
#ifdef SDL_GPU
	const bool toggle_fullscreen = ((flags & FULL_SCREEN) != 0) != fullScreen;
#endif

	if( res == 0 )
		return 0;

	fullScreen = (flags & FULL_SCREEN) != 0;
#ifdef SDL_GPU
	//NOTE: this surface is in fact unused now. Can be removed when possible.
	frameBuffer = SDL_CreateRGBSurface(SDL_SWSURFACE, x, y, 32,
									 0x00ff0000,
									 0x0000ff00,
									 0x000000ff,
									 0xff000000);
	overlay_ = SDL_CreateRGBSurface(SDL_SWSURFACE, x, y, 32,
									0x00ff0000,
									0x0000ff00,
									0x000000ff,
									0xff000000);
	GPU_SetWindowResolution(x, y);
	if (toggle_fullscreen) {
		GPU_ToggleFullscreen(1);
	}
#else
	frameBuffer = SDL_SetVideoMode( x, y, bits_per_pixel, flags );
#endif

	if( frameBuffer != NULL ) {
		image::set_pixel_format(frameBuffer->format);
		return bits_per_pixel;
	} else	return 0;
}
#endif

bool CVideo::modeChanged()
{
	bool ret = mode_changed_;
	mode_changed_ = false;
	return ret;
}

int CVideo::getx() const
{
#ifdef SDL_GPU
	return GPU_GetContextTarget()->w;
#else
	return frameBuffer->w;
#endif
}

int CVideo::gety() const
{
#ifdef SDL_GPU
	return GPU_GetContextTarget()->h;
#else
	return frameBuffer->h;
#endif
}

void CVideo::flip()
{
	if(fake_screen_)
		return;
#ifdef SDL_GPU
	assert(render_target_);
	GPU_Flip(render_target_);
#else
#if !SDL_VERSION_ATLEAST(2, 0, 0)
	if(update_all) {
		::SDL_Flip(frameBuffer);
	} else if(update_rects.empty() == false) {
		calc_rects();
		if(!update_rects.empty()) {
			SDL_UpdateRects(frameBuffer, update_rects.size(), &update_rects[0]);
		}
	}

	clear_updates();
#else
	assert(window);
	window->render();
#endif
#endif
}

void CVideo::lock_updates(bool value)
{
	if(value == true)
		++updatesLocked_;
	else
		--updatesLocked_;
}

bool CVideo::update_locked() const
{
	return updatesLocked_ > 0;
}

#if SDL_VERSION_ATLEAST(2, 0, 0)
Uint8
CVideo::window_state()
{
	Uint8 state = 0;
	Uint32 flags = 0;

	if(!window) {
		return state;
	}

	flags = SDL_GetWindowFlags(*window);
	if ((flags & SDL_WINDOW_SHOWN) && !(flags & SDL_WINDOW_MINIMIZED)) {
		state |= SDL_APPACTIVE;
	}
	if (flags & SDL_WINDOW_INPUT_FOCUS) {
		state |= SDL_APPINPUTFOCUS;
	}
	if (flags & SDL_WINDOW_MOUSE_FOCUS) {
		state |= SDL_APPMOUSEFOCUS;
	}
	return state;
}

void CVideo::set_window_title(const std::string& title)
{
	assert(window);
	window->set_title(title);
}

void CVideo::set_window_icon(surface& icon)
{
	assert(window);
	window->set_icon(icon);
}

sdl::twindow *CVideo::get_window()
{
	return window;
}

#endif

#if SDL_VERSION_ATLEAST(2, 0, 0)
static int sdl_display_index()
{
	if(window) {
		return SDL_GetWindowDisplayIndex(*window);
	}
	return 0;
}
#endif

#if SDL_VERSION_ATLEAST(2, 0, 0)
std::vector<std::pair<int, int> > CVideo::get_available_resolutions()
{
	std::vector<std::pair<int, int> > result;

	const int modes = SDL_GetNumDisplayModes(sdl_display_index());
	if(modes <= 0) {
		std::cerr << "No modes supported\n";
		return result;
	}

	const std::pair<int,int> min_res = std::make_pair(preferences::min_allowed_width(),preferences::min_allowed_height());

	SDL_DisplayMode mode;
	for(int i = 0; i < modes; ++i) {
		if(SDL_GetDisplayMode(0, i, &mode) == 0) {
			if (mode.w >= min_res.first && mode.h >= min_res.second)
				result.push_back(std::make_pair(mode.w, mode.h));
		}
	}
	if(std::find(result.begin(), result.end(), min_res) == result.end())
		result.push_back(min_res);

	std::sort(result.begin(), result.end());
	result.erase(std::unique(result.begin(), result.end()), result.end());

	return result;
}
#else
std::vector<std::pair<int, int> > CVideo::get_available_resolutions()
{
	std::vector<std::pair<int, int> > result;
	const SDL_Rect* const * modes;
	if (const surface& surf = getSurface()) {
		SDL_PixelFormat format = *surf->format;
		format.BitsPerPixel = getBpp();
		modes = SDL_ListModes(&format, FULL_SCREEN);
	} else
		modes = SDL_ListModes(NULL, FULL_SCREEN);

	// The SDL documentation says that a return value of -1
	// means that all dimensions are supported/possible.
	if(modes == reinterpret_cast<SDL_Rect**>(-1)) {
		// SDL says that all modes are possible, so it's OK to use a
		// hardcoded list here. Include tiny and small gui since they
		// will be filtered out later if not needed.
		result.push_back(std::make_pair(800, 480));	// EeePC resolution
		result.push_back(std::make_pair(800, 600));
		result.push_back(std::make_pair(1024, 600)); // used on many netbooks
		result.push_back(std::make_pair(1024, 768));
		result.push_back(std::make_pair(1280, 960));
		result.push_back(std::make_pair(1280, 1024));
		result.push_back(std::make_pair(1366, 768)); // 16:9 notebooks
		result.push_back(std::make_pair(1440, 900));
		result.push_back(std::make_pair(1440, 1200));
		result.push_back(std::make_pair(1600, 1200));
		result.push_back(std::make_pair(1680, 1050));
		result.push_back(std::make_pair(1920, 1080));
		result.push_back(std::make_pair(1920, 1200));
		result.push_back(std::make_pair(2560, 1600));

		return result;
	} else if(modes == NULL) {
		std::cerr << "No modes supported\n";
		return result;
	}

	const std::pair<int,int> min_res = std::make_pair(preferences::min_allowed_width(),preferences::min_allowed_height());

	if (getSurface() && getSurface()->w >= min_res.first && getSurface()->h >= min_res.second)
		result.push_back(std::make_pair(getSurface()->w, getSurface()->h));

	for(int i = 0; modes[i] != NULL; ++i) {
		if (modes[i]->w >= min_res.first && modes[i]->h >= min_res.second)
			result.push_back(std::make_pair(modes[i]->w,modes[i]->h));
	}

	std::sort(result.begin(), result.end());
	result.erase(std::unique(result.begin(), result.end()), result.end());

	return result;
}
#endif

surface& CVideo::getSurface()
{
	return frameBuffer;
}

bool CVideo::isFullScreen() const { return fullScreen; }

void CVideo::setBpp( int bpp )
{
	bpp_ = bpp;
}

int CVideo::getBpp()
{
	return bpp_;
}

int CVideo::set_help_string(const std::string& str)
{
	font::remove_floating_label(help_string_);

	const SDL_Color color = { 0, 0, 0, 0xbb };

	int size = font::SIZE_LARGE;

	while(size > 0) {
		if(font::line_width(str, size) > getx()) {
			size--;
		} else {
			break;
		}
	}

	const int border = 5;

	font::floating_label flabel(str);
	flabel.set_font_size(size);
	flabel.set_position(getx()/2, gety());
	flabel.set_bg_color(color);
	flabel.set_border_size(border);

	help_string_ = font::add_floating_label(flabel);

	const SDL_Rect& rect = font::get_floating_label_rect(help_string_);
	font::move_floating_label(help_string_,0.0,-double(rect.h));
	return help_string_;
}

void CVideo::clear_help_string(int handle)
{
	if(handle == help_string_) {
		font::remove_floating_label(handle);
		help_string_ = 0;
	}
}

void CVideo::clear_all_help_strings()
{
	clear_help_string(help_string_);
}

