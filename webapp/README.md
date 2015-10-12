Major Plan for this project
===========================
1. Create a replay viewer.
    This should be the easiest task. You'll load a file, parse it and display it step by step.
    The "advanced" part is to step backwards and forwards by userinteraction

2. Create an online realtime viewer.
    Watch current games LIVE! You'll should be able to chat with them too... at least like any observer does.

3. Create an online wesnoth webapp game.
    At first you should be just able to join default_era games.
    Later on you should be able to host default_era games by yourself.

4. Implement Support for add-ons
    In replay viewer, realtime viewer and online wesnoth.

5. Somewhere inbetween I will need to parse the wesnoth help to some wiki and link it in those online games
    e.g.: if you want a unit description, it should be enough to open a new window with the wiki
    displaying the right chapter. It would be non-blocking (in wesnoth, if you'll watch the wiki, the game stops for
    you only)



Current Status
==============

Currently you can browse (just one) the directory of replays and have the possibility to download or watch it.
If you click on watch, you'll get the map displayed. (WIP here: I'm displaying currently just the map-tiles text)
