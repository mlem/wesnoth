package org.wesnoth.gateway.replays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wesnoth.ReplayMeta;
import org.wesnoth.UserName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HtmlToListOfReplaysParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlToListOfReplaysParser.class);
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
    private static final Pattern PATTERN = Pattern.compile("<tr><td valign=\"top\"><img src=\"\\/icons\\/unknown.gif\" alt=\"\\[\\s*]\"><\\/td><td><a href=\\\"([^\"]*)\">([^<]*)<\\/a><\\/td><td align=\"right\">([^<]*)<\\/td><td align=\"right\">([^<]*)<\\/td><td><i>title<\\/i>: <b>([^<]*)<\\/b>\\s<i>era<\\/i>: <b>([^<]*)<\\/b> <i>players<\\/i>:([^<]*)");
    private static final Pattern ID_AND_ZIP_PATTERN = Pattern.compile(".*\\(([^)]+)\\)\\.(gz|bz)");
    private static final Pattern SIZE_PATTERN = Pattern.compile("([0-9\\.]*)([MK])");


    public List<ReplayMeta> parseStreamToListOfReplays(InputStream inputStream, String currentUrl) throws
            IOException, URISyntaxException {
        List<ReplayMeta> replayMetas = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String readLine = br.readLine();
        while (readLine != null) {
            Matcher matcher = PATTERN.matcher(readLine);
            if (matcher.find()) {
                ReplayMeta replayMeta = convertToReplayInfo(matcher, currentUrl);
                replayMetas.add(replayMeta);

            }
            readLine = br.readLine();
        }
        return replayMetas;
    }

    private ReplayMeta convertToReplayInfo(Matcher matcher, String currentUrl) throws URISyntaxException {
        String downloadLink = currentUrl + matcher.group(1);
        String filename = matcher.group(2);

        int turnIndex = filename.indexOf("Turn");
        int scenarioIndex = filename.indexOf("^");
        if (scenarioIndex != -1) {
            turnIndex = turnIndex < scenarioIndex ? turnIndex : scenarioIndex;
        }
        String mapName = filename.substring(0, turnIndex - 1).replace("_", " ").trim();
        Matcher idAndZipMatcher = ID_AND_ZIP_PATTERN.matcher(filename);
        Integer replayId = null;
        Compression compression = null;
        if (idAndZipMatcher.find()) {
            replayId = Integer.parseInt(idAndZipMatcher.group(1));
            compression = Compression.findBy(idAndZipMatcher.group(2));
        }


        String date = matcher.group(3);
        Date recordedDate = null;
        try {
            recordedDate = SIMPLE_DATE_FORMAT.parse(date.trim());
        } catch (ParseException e) {
            LOGGER.warn("Couldn't parse date: " + date + " to format " + SIMPLE_DATE_FORMAT.toPattern());
        }
        String size = matcher.group(4);

        Matcher sizeMatcher = SIZE_PATTERN.matcher(size);
        int replaySize = 0;
        if (sizeMatcher.find()) {
            String foundSize = sizeMatcher.group(1);
            int sizeAsInt;
            if (!foundSize.contains(".")) {
                sizeAsInt = Integer.parseInt(foundSize) * 10;
            } else {
                String without = foundSize.replace(".", "");
                sizeAsInt = Integer.parseInt(without);
            }
            String foundUnit = sizeMatcher.group(2);
            if ("K".equals(foundUnit)) {
                replaySize = (int) (sizeAsInt * 102.4);
            } else if ("M".equals(foundUnit)) {
                replaySize = (int) (sizeAsInt * 102.4) * 1024;
            }


        }
        String gameName = matcher.group(5);
        String era = matcher.group(6);
        String players = matcher.group(7);
        String[] splittedPlayers = players.split(",");
        List<UserName> playersOfReplay = Stream.of(splittedPlayers).map(String::trim).map(UserName::new).collect(Collectors.toList());
        return new ReplayMeta(new URI(downloadLink), filename, recordedDate, replaySize, gameName, era, playersOfReplay, mapName, replayId, compression);
    }

}
