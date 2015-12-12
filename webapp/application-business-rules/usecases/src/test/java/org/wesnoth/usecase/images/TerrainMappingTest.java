package org.wesnoth.usecase.images;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class TerrainMappingTest {

    @Test
    public void test() throws MalformedURLException, URISyntaxException {
        String terrain = "#\n" +
                "\n" +
                "[terrain_type]\n" +
                "    symbol_image=water/ocean-grey-tile\n" +
                "    id=deep_water_gray\n" +
                "    name= _ \"Deep Water\"\n" +
                "    editor_name= _ \"Gray Deep Water\"\n" +
                "    string=Wog\n" +
                "    aliasof=Wdt\n" +
                "    submerge=0.5\n" +
                "    editor_group=water\n" +
                "[/terrain_type]";
        ImageMappingUsecase.Request request = new ImageMappingUsecase.Request(new ByteArrayInputStream(terrain.getBytes()));


        ImageMappingUsecase.Response response = new ImageMappingUsecase.Response();
        new ImageMappingUsecase().execute(request, response);

        assertThat(response.imageMapping(), is(not(nullValue())));
        assertThat(response.imageMapping(), is(instanceOf(ImageMappings.class)));
        assertThat(response.imageMapping().of(TagType.TERRAIN_TYPE, "Wog").isPresent(), is(true));
        assertThat(response.imageMapping().of(TagType.TERRAIN_TYPE, "Wog").get(), is(instanceOf(ImageMapping.class)));
        assertThat(response.imageMapping().of(TagType.TERRAIN_TYPE, "Wog").get().id(), is("deep_water_gray"));
        assertThat(response.imageMapping().of(TagType.TERRAIN_TYPE, "Wog").get().uri(), is(new URI("/data/core/images/terrain/water/ocean-grey-tile.png")));

    }

}