<%-- see  http://www.tutorialspoint.com/jsp/jsp_standard_tag_library.htm --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Replays</title>
    <meta charset="utf-8">

    <script src="/sockjs-0.3.4.js"></script>
    <script src="/stomp.js"></script>
    <link type="text/css" href="/test.css" rel="stylesheet"/>
    <script type="text/javascript">


        function createHtmlRow(row) {
            var rowDiv = document.createElement('div');
            rowDiv.classList.add("tile-row");
            for (var j = 0; j < row.length; j++) {
                var tile = row[j]['tileString'].replace(/ /g, '^').replace(/\//g, '-ne-sw');
                var div = document.createElement('div');

                div.classList.add("hex");
                var tileClasses = tile.split("^");
                var lastDiv = div;
                for (var k = 0; k < tileClasses.length; k++) {
                    var subdiv = document.createElement('div');
                    subdiv.classList.add("hex-in");
                    subdiv.classList.add(tileClasses[k]);
                    lastDiv.appendChild(subdiv);
                    lastDiv = subdiv;

                }
                var overlay = document.createElement('span');
                overlay.classList.add("overlay");
                lastDiv.appendChild(overlay);
                div.appendChild(document.createTextNode(tile));
                rowDiv.appendChild(div);
            }
            return rowDiv;
        }
        function appendAsHtml(map, p) {
            for (var i = 0; i < map.length; i++) {
                var row = map[i];
                var rowDiv = createHtmlRow(row);
                p.appendChild(rowDiv);
            }
        }
        function createSvgRow(row, rowNumber, svg) {
            var g = document.createElementNS("http://www.w3.org/2000/svg", "g");
            svg.appendChild(g);
            g.setAttribute('transform', 'translate(0,' + rowNumber * 72 + ')');

            var rowDiv = g;
            for (var j = 0; j < row.length; j++) {
                var tile = row[j]['tileString'].replace(/ /g, '^').replace(/\//g, '-ne-sw');
                var tileClasses = tile.split("^");
                for (var k = 0; k < tileClasses.length; k++) {
                    var overlayPolygon = document.createElementNS("http://www.w3.org/2000/svg", "use");
                    overlayPolygon.classList.add('hex');
                    overlayPolygon.classList.add('hex-in');
                    overlayPolygon.setAttribute('xlink:href', '#' + tileClasses[k]);
                    // overlayPolygon.setAttribute('transform', 'translate(' + j * 54 + ',' + (j & 1) * 36 + ')');
                    overlayPolygon.setAttribute('x', j * 54);
                    overlayPolygon.setAttribute('y', (j & 1) * 36);
                    //overlayPolygon.setAttribute('fill', 'url(#' + tileClasses[k] + ')');
                    // <use x="100" y="100" xlink:href="path/to/animals.svg#bird" />
                    rowDiv.appendChild(overlayPolygon);
                }
            }
            return rowDiv;

        }
        function appendAsSvg(map) {
            var svg = document.getElementById('svg-map');
            svg.setAttribute('width', '' + (map.length * 54 + 54));
            svg.setAttribute('height', '' + (map.length * 72 + 72));
            document.getElementById('body').appendChild(svg);
            for (var i = 0; i < map.length; i++) {
                var row = map[i];
                var rowDiv = createSvgRow(row, i, svg);
                svg.appendChild(rowDiv);
            }
        }

        function showGreeting(message) {
            var response = document.getElementById('response');
            var json = JSON.parse(message);

            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            if (json.payload.map !== undefined) {
                var map = json.payload.map;
                appendAsSvg(map);
               // appendAsHtml(map, p);


            } else {
                p.appendChild(document.createTextNode(json.payload));
            }
            response.appendChild(p);
        }

        function loadTestMap() {
            loadJSON('/test-map.json', function (data) {
                showGreeting(data);
            });


        }

        function loadJSON(url, callback) {

            var xobj = new XMLHttpRequest();
            xobj.overrideMimeType("application/json");
            xobj.open('GET', url, true); // Replace 'my_data' with the path to your file
            xobj.onreadystatechange = function () {
                if (xobj.readyState == 4 && xobj.status == "200") {
                    // Required use of an anonymous callback as .open will NOT return a value but simply returns undefined in asynchronous mode
                    callback(xobj.responseText);
                }
            };
            xobj.send(null);
        }

    </script>
    <style>
        div.tile-row {
            clear: both;
            float: left;
            display: block;
            position: relative;
        }

        div.hex {
            float: left;
        }

        div.hex:nth-child(2n) {
            margin-top: -36px;
        }

        div.hex:nth-child(n+1) {
            margin-left: -18px;
        }

        .hex:hover {
            stroke: #fff20f;
            stroke-width: 2;
        }
    </style>
</head>
<body id="body" onload="loadTestMap();">
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
    enabled. Please enable
    Javascript and reload this page!</h2></noscript>

<div>
    Just for test:

    <svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="100%" height="300"
         xmlns:xlink="http://www.w3.org/1999/xlink">

        <defs>

            <pattern id="pattern-Wog" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/water/ocean-grey-tile.png"></image>
            </pattern>
            <polygon id="Wog" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Wog)"></polygon>

        </defs>
        <use class="hex hex-in" xlink:href="#Wog" x="0" y="0"></use>
        <use class="hex hex-in" xlink:href="#Wog" x="54" y="36"></use>
        <use class="hex hex-in" xlink:href="#Wog" x="108" y="0"></use>
        <use class="hex hex-in" xlink:href="#Wog" x="162" y="36"></use>
    </svg>
</div>

<div>
    <div>
        <button id="connect" onclick="connect();">Connect</button>
        <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
    </div>
    <%--
    TODO: make this visible. currently there are so many elements, but it still doesn't work.
    --%>
    <svg id="svg-map" xmlns="http://www.w3.org/2000/svg" version="1.1" xmlns:xlink="http://www.w3.org/1999/xlink">
        <defs>

            <pattern id="pattern-Wog" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/water/ocean-grey-tile.png"></image>
            </pattern>
            <polygon id="Wog" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Wog)"></polygon>


            <pattern id="pattern-Wo" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/water/ocean-tile.png"></image>
            </pattern>
            <polygon id="Wo" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Wo)"></polygon>


            <pattern id="pattern-Wot" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/water/ocean-tropical-tile.png"></image>
            </pattern>
            <polygon id="Wot" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Wot)"></polygon>


            <pattern id="pattern-Wwg" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/water/coast-grey-tile.png"></image>
            </pattern>
            <polygon id="Wwg" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Wwg)"></polygon>


            <pattern id="pattern-Ww" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/water/coast-tile.png"></image>
            </pattern>
            <polygon id="Ww" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ww)"></polygon>


            <pattern id="pattern-Wwt" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/water/coast-tropical-tile.png"></image>
            </pattern>
            <polygon id="Wwt" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Wwt)"></polygon>


            <pattern id="pattern-Wwf" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/water/ford-tile.png"></image>
            </pattern>
            <polygon id="Wwf" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Wwf)"></polygon>


            <pattern id="pattern-Wwrg" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/water/reef-gray-tile.png"></image>
            </pattern>
            <polygon id="Wwrg" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Wwrg)"></polygon>


            <pattern id="pattern-Wwr" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/water/reef-tile.png"></image>
            </pattern>
            <polygon id="Wwr" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Wwr)"></polygon>


            <pattern id="pattern-Wwrt" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/water/reef-tropical-tile.png"></image>
            </pattern>
            <polygon id="Wwrt" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Wwrt)"></polygon>


            <pattern id="pattern-Ss" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/swamp/water-tile.png"></image>
            </pattern>
            <polygon id="Ss" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ss)"></polygon>


            <pattern id="pattern-Sm" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/swamp/mud-tile.png"></image>
            </pattern>
            <polygon id="Sm" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Sm)"></polygon>


            <pattern id="pattern-Gg" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/grass/green-symbol.png"></image>
            </pattern>
            <polygon id="Gg" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Gg)"></polygon>


            <pattern id="pattern-Gs" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/grass/semi-dry.png"></image>
            </pattern>
            <polygon id="Gs" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Gs)"></polygon>


            <pattern id="pattern-Gd" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/grass/dry-symbol.png"></image>
            </pattern>
            <polygon id="Gd" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Gd)"></polygon>


            <pattern id="pattern-Gll" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/grass/leaf-litter.png"></image>
            </pattern>
            <polygon id="Gll" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Gll)"></polygon>


            <pattern id="pattern-Rb" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/flat/dirt-dark.png"></image>
            </pattern>
            <polygon id="Rb" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Rb)"></polygon>


            <pattern id="pattern-Re" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/flat/dirt.png"></image>
            </pattern>
            <polygon id="Re" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Re)"></polygon>


            <pattern id="pattern-Rd" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/flat/desert-road.png"></image>
            </pattern>
            <polygon id="Rd" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Rd)"></polygon>


            <pattern id="pattern-Rr" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/flat/road.png"></image>
            </pattern>
            <polygon id="Rr" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Rr)"></polygon>


            <pattern id="pattern-Rrc" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/flat/road-clean.png"></image>
            </pattern>
            <polygon id="Rrc" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Rrc)"></polygon>


            <pattern id="pattern-Rp" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/flat/stone-path.png"></image>
            </pattern>
            <polygon id="Rp" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Rp)"></polygon>


            <pattern id="pattern-Ai" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/frozen/ice.png"></image>
            </pattern>
            <polygon id="Ai" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ai)"></polygon>


            <pattern id="pattern-Aa" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/frozen/snow.png"></image>
            </pattern>
            <polygon id="Aa" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Aa)"></polygon>


            <pattern id="pattern-Dd" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/sand/desert.png"></image>
            </pattern>
            <polygon id="Dd" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Dd)"></polygon>


            <pattern id="pattern-Ds" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/sand/beach.png"></image>
            </pattern>
            <polygon id="Ds" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ds)"></polygon>


            <pattern id="pattern-Do" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/sand/desert-oasis.png"></image>
            </pattern>
            <polygon id="Do" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Do)"></polygon>


            <pattern id="pattern-Dr" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/misc/rubble-tile.png"></image>
            </pattern>
            <polygon id="Dr" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Dr)"></polygon>


            <pattern id="pattern-DdDc" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/sand/crater.png"></image>
            </pattern>
            <polygon id="DdDc" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-DdDc)"></polygon>


            <pattern id="pattern-Efm" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/embellishments/flowers-mixed.png"></image>
            </pattern>
            <polygon id="Efm" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Efm)"></polygon>


            <pattern id="pattern-Gvs" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/embellishments/farm-veg-spring-icon.png"></image>
            </pattern>
            <polygon id="Gvs" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Gvs)"></polygon>


            <pattern id="pattern-Es" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/embellishments/stones-small7.png"></image>
            </pattern>
            <polygon id="Es" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Es)"></polygon>


            <pattern id="pattern-Em" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/embellishments/mushroom.png"></image>
            </pattern>
            <polygon id="Em" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Em)"></polygon>


            <pattern id="pattern-Emf" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/embellishments/mushroom-farm-small.png"></image>
            </pattern>
            <polygon id="Emf" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Emf)"></polygon>


            <pattern id="pattern-Edp" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/embellishments/desert-plant5.png"></image>
            </pattern>
            <polygon id="Edp" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Edp)"></polygon>


            <pattern id="pattern-Edpp" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/embellishments/desert-plant.png"></image>
            </pattern>
            <polygon id="Edpp" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Edpp)"></polygon>


            <pattern id="pattern-Wm" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/misc/windmill-embellishment-tile.png"></image>
            </pattern>
            <polygon id="Wm" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Wm)"></polygon>


            <pattern id="pattern-Ecf" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/misc/fire-A01.png"></image>
            </pattern>
            <polygon id="Ecf" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ecf)"></polygon>


            <pattern id="pattern-Eb" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/misc/brazier-embellishment.png"></image>
            </pattern>
            <polygon id="Eb" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Eb)"></polygon>


            <pattern id="pattern-Ebn" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/misc/brazier-A01.png"></image>
            </pattern>
            <polygon id="Ebn" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ebn)"></polygon>


            <pattern id="pattern-Eff" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/embellishments/fence-se-nw-01.png"></image>
            </pattern>
            <polygon id="Eff" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Eff)"></polygon>


            <pattern id="pattern-Esd" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/embellishments/rocks.png"></image>
            </pattern>
            <polygon id="Esd" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Esd)"></polygon>


            <pattern id="pattern-Ewl" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/embellishments/water-lilies-tile.png"></image>
            </pattern>
            <polygon id="Ewl" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ewl)"></polygon>


            <pattern id="pattern-Ewf" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/embellishments/water-lilies-flower-tile.png"></image>
            </pattern>
            <polygon id="Ewf" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ewf)"></polygon>


            <pattern id="pattern-Edt" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/misc/detritus/trashC-1.png"></image>
            </pattern>
            <polygon id="Edt" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Edt)"></polygon>


            <pattern id="pattern-Edb" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/misc/detritus/detritusC-1.png"></image>
            </pattern>
            <polygon id="Edb" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Edb)"></polygon>


            <pattern id="pattern-Fet" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/forest/great-tree-tile.png"></image>
            </pattern>
            <polygon id="Fet" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Fet)"></polygon>


            <pattern id="pattern-Fetd" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/great-tree-dead-tile.png"></image>
            </pattern>
            <polygon id="Fetd" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Fetd)"></polygon>


            <pattern id="pattern-Ft" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/tropical/jungle-tile.png"></image>
            </pattern>
            <polygon id="Ft" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ft)"></polygon>


            <pattern id="pattern-Ftr" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/tropical/rainforest-tile.png"></image>
            </pattern>
            <polygon id="Ftr" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ftr)"></polygon>


            <pattern id="pattern-Ftd" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/tropical/palm-desert-tile.png"></image>
            </pattern>
            <polygon id="Ftd" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ftd)"></polygon>


            <pattern id="pattern-Ftp" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/tropical/palms-tile.png"></image>
            </pattern>
            <polygon id="Ftp" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ftp)"></polygon>


            <pattern id="pattern-Fts" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/tropical/savanna-tile.png"></image>
            </pattern>
            <polygon id="Fts" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Fts)"></polygon>


            <pattern id="pattern-Fp" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/forest/pine-tile.png"></image>
            </pattern>
            <polygon id="Fp" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Fp)"></polygon>


            <pattern id="pattern-Fpa" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/snow-forest-tile.png"></image>
            </pattern>
            <polygon id="Fpa" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Fpa)"></polygon>


            <pattern id="pattern-Fds" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/deciduous-summer-tile.png"></image>
            </pattern>
            <polygon id="Fds" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Fds)"></polygon>


            <pattern id="pattern-Fdf" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/deciduous-fall-tile.png"></image>
            </pattern>
            <polygon id="Fdf" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Fdf)"></polygon>


            <pattern id="pattern-Fdw" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/deciduous-winter-tile.png"></image>
            </pattern>
            <polygon id="Fdw" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Fdw)"></polygon>


            <pattern id="pattern-Fda" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/deciduous-winter-snow-tile.png"></image>
            </pattern>
            <polygon id="Fda" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Fda)"></polygon>


            <pattern id="pattern-Fms" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/mixed-summer-tile.png"></image>
            </pattern>
            <polygon id="Fms" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Fms)"></polygon>


            <pattern id="pattern-Fmf" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/forest/mixed-fall-tile.png"></image>
            </pattern>
            <polygon id="Fmf" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Fmf)"></polygon>


            <pattern id="pattern-Fmw" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/mixed-winter-tile.png"></image>
            </pattern>
            <polygon id="Fmw" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Fmw)"></polygon>


            <pattern id="pattern-Fma" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/mixed-winter-snow-tile.png"></image>
            </pattern>
            <polygon id="Fma" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Fma)"></polygon>


            <pattern id="pattern-Hh" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/hills/regular.png"></image>
            </pattern>
            <polygon id="Hh" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Hh)"></polygon>


            <pattern id="pattern-Hhd" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/hills/dry.png"></image>
            </pattern>
            <polygon id="Hhd" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Hhd)"></polygon>


            <pattern id="pattern-Hd" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/hills/desert.png"></image>
            </pattern>
            <polygon id="Hd" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Hd)"></polygon>


            <pattern id="pattern-Ha" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/hills/snow.png"></image>
            </pattern>
            <polygon id="Ha" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ha)"></polygon>


            <pattern id="pattern-Mm" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/mountains/basic-tile.png"></image>
            </pattern>
            <polygon id="Mm" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Mm)"></polygon>


            <pattern id="pattern-Md" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/mountains/dry-tile.png"></image>
            </pattern>
            <polygon id="Md" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Md)"></polygon>


            <pattern id="pattern-Ms" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/mountains/snow-tile.png"></image>
            </pattern>
            <polygon id="Ms" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ms)"></polygon>


            <pattern id="pattern-Iwr" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/interior/wood-regular.png"></image>
            </pattern>
            <polygon id="Iwr" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Iwr)"></polygon>


            <pattern id="pattern-Ii" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/cave/beam-tile.png"></image>
            </pattern>
            <polygon id="Ii" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ii)"></polygon>


            <pattern id="pattern-Uu" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/cave/floor6.png"></image>
            </pattern>
            <polygon id="Uu" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Uu)"></polygon>


            <pattern id="pattern-Uue" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/cave/earthy-floor3.png"></image>
            </pattern>
            <polygon id="Uue" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Uue)"></polygon>


            <pattern id="pattern-Urb" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/cave/flagstones-dark.png"></image>
            </pattern>
            <polygon id="Urb" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Urb)"></polygon>


            <pattern id="pattern-Ur" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/cave/path.png"></image>
            </pattern>
            <polygon id="Ur" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ur)"></polygon>


            <pattern id="pattern-Uf" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/forest/mushrooms-tile.png"></image>
            </pattern>
            <polygon id="Uf" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Uf)"></polygon>


            <pattern id="pattern-Ufi" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/forest/mushrooms-beam-tile.png"></image>
            </pattern>
            <polygon id="Ufi" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ufi)"></polygon>


            <pattern id="pattern-Uh" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/cave/hills-variation.png"></image>
            </pattern>
            <polygon id="Uh" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Uh)"></polygon>


            <pattern id="pattern-Br|" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/misc/rails-n-s.png"></image>
            </pattern>
            <polygon id="Br|" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Br|)"></polygon>


            <pattern id="pattern-Br-ne-sw" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/misc/rails-ne-sw.png"></image>
            </pattern>
            <polygon id="Br-ne-sw" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Br-ne-sw)"></polygon>


            <pattern id="pattern-Br\" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/misc/rails-se-nw.png"></image>
            </pattern>
            <polygon id="Br\" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Br\)"></polygon>


            <pattern id="pattern-Qxu" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/chasm/depths.png"></image>
            </pattern>
            <polygon id="Qxu" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Qxu)"></polygon>


            <pattern id="pattern-Qxe" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/chasm/depths.png"></image>
            </pattern>
            <polygon id="Qxe" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Qxe)"></polygon>


            <pattern id="pattern-Qxua" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/chasm/abyss.png"></image>
            </pattern>
            <polygon id="Qxua" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Qxua)"></polygon>


            <pattern id="pattern-Ql" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/unwalkable/lava.png"></image>
            </pattern>
            <polygon id="Ql" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ql)"></polygon>


            <pattern id="pattern-Qlf" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/unwalkable/lava.png"></image>
            </pattern>
            <polygon id="Qlf" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Qlf)"></polygon>


            <pattern id="pattern-Mv" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/mountains/volcano-tile.png"></image>
            </pattern>
            <polygon id="Mv" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Mv)"></polygon>


            <pattern id="pattern-MmXm" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/mountains/cloud-tile.png"></image>
            </pattern>
            <polygon id="MmXm" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-MmXm)"></polygon>


            <pattern id="pattern-MdXm" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/mountains/cloud-desert-tile.png"></image>
            </pattern>
            <polygon id="MdXm" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-MdXm)"></polygon>


            <pattern id="pattern-MsXm" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/mountains/cloud-snow-tile.png"></image>
            </pattern>
            <polygon id="MsXm" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-MsXm)"></polygon>


            <pattern id="pattern-Xu" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/void/void.png"></image>
            </pattern>
            <polygon id="Xu" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Xu)"></polygon>


            <pattern id="pattern-Xuc" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/void/void.png"></image>
            </pattern>
            <polygon id="Xuc" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Xuc)"></polygon>


            <pattern id="pattern-Xue" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/void/void.png"></image>
            </pattern>
            <polygon id="Xue" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Xue)"></polygon>


            <pattern id="pattern-Xuce" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/void/void.png"></image>
            </pattern>
            <polygon id="Xuce" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Xuce)"></polygon>


            <pattern id="pattern-Xos" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/void/void.png"></image>
            </pattern>
            <polygon id="Xos" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Xos)"></polygon>


            <pattern id="pattern-Xol" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/void/void.png"></image>
            </pattern>
            <polygon id="Xol" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Xol)"></polygon>


            <pattern id="pattern-Xv" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/void/void.png"></image>
            </pattern>
            <polygon id="Xv" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Xv)"></polygon>


            <pattern id="pattern-Vda" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/desert-tile.png"></image>
            </pattern>
            <polygon id="Vda" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vda)"></polygon>


            <pattern id="pattern-Vdt" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/village/desert-camp-tile.png"></image>
            </pattern>
            <polygon id="Vdt" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vdt)"></polygon>


            <pattern id="pattern-Vct" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/camp-tile.png"></image>
            </pattern>
            <polygon id="Vct" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vct)"></polygon>


            <pattern id="pattern-Vo" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/orc-tile.png"></image>
            </pattern>
            <polygon id="Vo" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vo)"></polygon>


            <pattern id="pattern-Voa" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/orc-snow-tile.png"></image>
            </pattern>
            <polygon id="Voa" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Voa)"></polygon>


            <pattern id="pattern-Vea" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/village/elven-snow-tile.png"></image>
            </pattern>
            <polygon id="Vea" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vea)"></polygon>


            <pattern id="pattern-Ve" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/elven-tile.png"></image>
            </pattern>
            <polygon id="Ve" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ve)"></polygon>


            <pattern id="pattern-Vh" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/human-tile.png"></image>
            </pattern>
            <polygon id="Vh" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vh)"></polygon>


            <pattern id="pattern-Vha" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/snow-tile.png"></image>
            </pattern>
            <polygon id="Vha" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vha)"></polygon>


            <pattern id="pattern-Vhr" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/village/human-cottage-ruin-tile.png"></image>
            </pattern>
            <polygon id="Vhr" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vhr)"></polygon>


            <pattern id="pattern-Vhc" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/village/human-city-tile.png"></image>
            </pattern>
            <polygon id="Vhc" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vhc)"></polygon>


            <pattern id="pattern-Vwm" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/misc/windmill-tile.png"></image>
            </pattern>
            <polygon id="Vwm" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vwm)"></polygon>


            <pattern id="pattern-Vhca" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/village/human-city-snow-tile.png"></image>
            </pattern>
            <polygon id="Vhca" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Vhca)"></polygon>


            <pattern id="pattern-Vhcr" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/village/human-city-ruin-tile.png"></image>
            </pattern>
            <polygon id="Vhcr" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Vhcr)"></polygon>


            <pattern id="pattern-Vhh" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/village/human-hills-tile.png"></image>
            </pattern>
            <polygon id="Vhh" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vhh)"></polygon>


            <pattern id="pattern-Vhha" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/village/human-snow-hills-tile.png"></image>
            </pattern>
            <polygon id="Vhha" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Vhha)"></polygon>


            <pattern id="pattern-Vhhr" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/village/human-hills-ruin-tile.png"></image>
            </pattern>
            <polygon id="Vhhr" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Vhhr)"></polygon>


            <pattern id="pattern-Vht" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/tropical-tile.png"></image>
            </pattern>
            <polygon id="Vht" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vht)"></polygon>


            <pattern id="pattern-Vd" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/drake-tile.png"></image>
            </pattern>
            <polygon id="Vd" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vd)"></polygon>


            <pattern id="pattern-Vu" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/cave-tile.png"></image>
            </pattern>
            <polygon id="Vu" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vu)"></polygon>


            <pattern id="pattern-Vud" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/dwarven-tile.png"></image>
            </pattern>
            <polygon id="Vud" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vud)"></polygon>


            <pattern id="pattern-Vc" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/hut-tile.png"></image>
            </pattern>
            <polygon id="Vc" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vc)"></polygon>


            <pattern id="pattern-Vca" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/hut-snow-tile.png"></image>
            </pattern>
            <polygon id="Vca" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vca)"></polygon>


            <pattern id="pattern-Vl" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/log-cabin-tile.png"></image>
            </pattern>
            <polygon id="Vl" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vl)"></polygon>


            <pattern id="pattern-Vla" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/village/log-cabin-snow-tile.png"></image>
            </pattern>
            <polygon id="Vla" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vla)"></polygon>


            <pattern id="pattern-Vaa" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/igloo-tile.png"></image>
            </pattern>
            <polygon id="Vaa" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vaa)"></polygon>


            <pattern id="pattern-Vhs" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/village/swampwater-tile.png"></image>
            </pattern>
            <polygon id="Vhs" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vhs)"></polygon>


            <pattern id="pattern-Vm" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/village/coast-tile.png"></image>
            </pattern>
            <polygon id="Vm" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vm)"></polygon>


            <pattern id="pattern-Vov" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/fog/fog1.png"></image>
            </pattern>
            <polygon id="Vov" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Vov)"></polygon>


            <pattern id="pattern-Ce" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/encampment/regular-tile.png"></image>
            </pattern>
            <polygon id="Ce" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ce)"></polygon>


            <pattern id="pattern-Cea" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/encampment/snow-tile.png"></image>
            </pattern>
            <polygon id="Cea" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Cea)"></polygon>


            <pattern id="pattern-Co" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/castle/orcish/tile.png"></image>
            </pattern>
            <polygon id="Co" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Co)"></polygon>


            <pattern id="pattern-Coa" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/winter-orcish/tile.png"></image>
            </pattern>
            <polygon id="Coa" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Coa)"></polygon>


            <pattern id="pattern-Ch" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/castle/castle-tile.png"></image>
            </pattern>
            <polygon id="Ch" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ch)"></polygon>


            <pattern id="pattern-Cha" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/snowy/castle-tile.png"></image>
            </pattern>
            <polygon id="Cha" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Cha)"></polygon>


            <pattern id="pattern-Cv" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/castle/elven/tile.png"></image>
            </pattern>
            <polygon id="Cv" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Cv)"></polygon>


            <pattern id="pattern-Cud" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/dwarven-castle-tile.png"></image>
            </pattern>
            <polygon id="Cud" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Cud)"></polygon>


            <pattern id="pattern-Chr" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/castle/ruin-tile.png"></image>
            </pattern>
            <polygon id="Chr" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Chr)"></polygon>


            <pattern id="pattern-Chw" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/sunken-ruin-tile.png"></image>
            </pattern>
            <polygon id="Chw" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Chw)"></polygon>


            <pattern id="pattern-Chs" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/castle/swamp-ruin-tile.png"></image>
            </pattern>
            <polygon id="Chs" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Chs)"></polygon>


            <pattern id="pattern-Cd" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/castle/sand/tile.png"></image>
            </pattern>
            <polygon id="Cd" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Cd)"></polygon>


            <pattern id="pattern-Cdr" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/castle/sand/ruin-tile.png"></image>
            </pattern>
            <polygon id="Cdr" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Cdr)"></polygon>


            <pattern id="pattern-Ke" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/encampment/regular-keep-tile.png"></image>
            </pattern>
            <polygon id="Ke" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ke)"></polygon>


            <pattern id="pattern-Ket" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/encampment/tall-keep-tile.png"></image>
            </pattern>
            <polygon id="Ket" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ket)"></polygon>


            <pattern id="pattern-Kea" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/encampment/snow-keep-tile.png"></image>
            </pattern>
            <polygon id="Kea" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Kea)"></polygon>


            <pattern id="pattern-Ko" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/orcish/keep-tile.png"></image>
            </pattern>
            <polygon id="Ko" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Ko)"></polygon>


            <pattern id="pattern-Koa" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/winter-orcish/keep-tile.png"></image>
            </pattern>
            <polygon id="Koa" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Koa)"></polygon>


            <pattern id="pattern-Kh" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/castle/keep-tile.png"></image>
            </pattern>
            <polygon id="Kh" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Kh)"></polygon>


            <pattern id="pattern-Kha" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/castle/snowy/keep-tile.png"></image>
            </pattern>
            <polygon id="Kha" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Kha)"></polygon>


            <pattern id="pattern-Kv" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/castle/elven/keep-tile.png"></image>
            </pattern>
            <polygon id="Kv" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Kv)"></polygon>


            <pattern id="pattern-Kud" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/dwarven-keep-tile.png"></image>
            </pattern>
            <polygon id="Kud" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Kud)"></polygon>


            <pattern id="pattern-Khr" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/ruined-keep-tile.png"></image>
            </pattern>
            <polygon id="Khr" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Khr)"></polygon>


            <pattern id="pattern-Khw" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/sunken-keep-tile.png"></image>
            </pattern>
            <polygon id="Khw" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Khw)"></polygon>


            <pattern id="pattern-Khs" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/castle/swamp-keep-tile.png"></image>
            </pattern>
            <polygon id="Khs" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Khs)"></polygon>


            <pattern id="pattern-Kd" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/castle/sand/keep-tile.png"></image>
            </pattern>
            <polygon id="Kd" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Kd)"></polygon>


            <pattern id="pattern-Kdr" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/castle/sand/ruin-keep-tile.png"></image>
            </pattern>
            <polygon id="Kdr" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Kdr)"></polygon>


            <pattern id="pattern-Cov" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/fog/fog1.png"></image>
            </pattern>
            <polygon id="Cov" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Cov)"></polygon>


            <pattern id="pattern-Kov" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/fog/fog1.png"></image>
            </pattern>
            <polygon id="Kov" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Kov)"></polygon>


            <pattern id="pattern-Bw|" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/bridge/wood-n-s.png"></image>
            </pattern>
            <polygon id="Bw|" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Bw|)"></polygon>


            <pattern id="pattern-Bw-ne-sw" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/bridge/wood-ne-sw.png"></image>
            </pattern>
            <polygon id="Bw-ne-sw" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Bw-ne-sw)"></polygon>


            <pattern id="pattern-Bw\" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/bridge/wood-se-nw.png"></image>
            </pattern>
            <polygon id="Bw\" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Bw\)"></polygon>


            <pattern id="pattern-Bw|r" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/bridge/wood-rotting-n-s.png"></image>
            </pattern>
            <polygon id="Bw|r" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Bw|r)"></polygon>


            <pattern id="pattern-Bw-ne-swr" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/bridge/wood-rotting-ne-sw.png"></image>
            </pattern>
            <polygon id="Bw-ne-swr" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Bw-ne-swr)"></polygon>


            <pattern id="pattern-Bw\r" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/bridge/wood-rotting-se-nw.png"></image>
            </pattern>
            <polygon id="Bw\r" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Bw\r)"></polygon>


            <pattern id="pattern-Bsb|" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/bridge/stonebridge-n-s-tile.png"></image>
            </pattern>
            <polygon id="Bsb|" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Bsb|)"></polygon>


            <pattern id="pattern-Bsb\" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/bridge/stonebridge-se-nw-tile.png"></image>
            </pattern>
            <polygon id="Bsb\" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Bsb\)"></polygon>


            <pattern id="pattern-Bsb-ne-sw" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/bridge/stonebridge-ne-sw-tile.png"></image>
            </pattern>
            <polygon id="Bsb-ne-sw" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Bsb-ne-sw)"></polygon>


            <pattern id="pattern-Bs|" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/cave/chasm-stone-bridge-s-n-tile.png"></image>
            </pattern>
            <polygon id="Bs|" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Bs|)"></polygon>


            <pattern id="pattern-Bs-ne-sw" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/cave/chasm-stone-bridge-sw-ne-tile.png"></image>
            </pattern>
            <polygon id="Bs-ne-sw" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Bs-ne-sw)"></polygon>


            <pattern id="pattern-Bs\" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/cave/chasm-stone-bridge-se-nw-tile.png"></image>
            </pattern>
            <polygon id="Bs\" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Bs\)"></polygon>


            <pattern id="pattern-Bh\" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/bridge/hanging-se-nw-tile.png"></image>
            </pattern>
            <polygon id="Bh\" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Bh\)"></polygon>


            <pattern id="pattern-Bh-ne-sw" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/bridge/hanging-sw-ne-tile.png"></image>
            </pattern>
            <polygon id="Bh-ne-sw" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Bh-ne-sw)"></polygon>


            <pattern id="pattern-Bh|" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/bridge/hanging-s-n-tile.png"></image>
            </pattern>
            <polygon id="Bh|" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Bh|)"></polygon>


            <pattern id="pattern-Bcx\" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/bridge/chasm-se-nw-tile.png"></image>
            </pattern>
            <polygon id="Bcx\" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Bcx\)"></polygon>


            <pattern id="pattern-Bcx-ne-sw" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/bridge/chasm-sw-ne-tile.png"></image>
            </pattern>
            <polygon id="Bcx-ne-sw" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Bcx-ne-sw)"></polygon>


            <pattern id="pattern-Bcx|" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/bridge/chasm-s-n-tile.png"></image>
            </pattern>
            <polygon id="Bcx|" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Bcx|)"></polygon>


            <pattern id="pattern-Bp\" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/bridge/planks-se-nw-tile.png"></image>
            </pattern>
            <polygon id="Bp\" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Bp\)"></polygon>


            <pattern id="pattern-Bp-ne-sw" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72"
                       xlink:href="/data/core/images/terrain/bridge/planks-sw-ne-tile.png"></image>
            </pattern>
            <polygon id="Bp-ne-sw" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"
                     fill="url(#pattern-Bp-ne-sw)"></polygon>


            <pattern id="pattern-Bp|" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
                <image height="72" width="72" xlink:href="/data/core/images/terrain/bridge/planks-s-n.png"></image>
            </pattern>
            <polygon id="Bp|" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Bp|)"></polygon>

        </defs>
    </svg>
    <div id="conversationDiv">
        <p id="response"></p>
    </div>
</div>
<br>
<br>

<style>


</style>
</body>
</html>