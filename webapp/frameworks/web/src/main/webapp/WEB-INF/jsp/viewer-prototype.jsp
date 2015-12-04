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
                    // definitions are preloaded onload with ajax (see javacsript below)
                    overlayPolygon.setAttributeNS("http://www.w3.org/1999/xlink",'xlink:href', '#' + tileClasses[k]);
                    // overlayPolygon.setAttribute('transform', 'translate(' + j * 54 + ',' + (j & 1) * 36 + ')');
                    overlayPolygon.setAttribute('x', j * 54);
                    overlayPolygon.setAttribute('y', (j & 1) * (-36));
                    //overlayPolygon.setAttribute('fill', 'url(#' + tileClasses[k] + ')');
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

        var ajax = new XMLHttpRequest();
        ajax.open("GET", "/test-map.svg", true);
        ajax.send();
        ajax.onload = function(e) {
            var div = document.createElement("div");
            div.innerHTML = ajax.responseText;
            document.body.insertBefore(div, document.body.childNodes[0]);
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
        <use class="hex hex-in" xlink:href="#Wog" x="0" y="0"></use>
        <use class="hex hex-in" xlink:href="#Wog" x="54" y="36"></use>
        <use class="hex hex-in" xlink:href="#Dd" x="108" y="0"></use>
        <use class="hex hex-in" xlink:href="#Wog" x="162" y="36"></use>
        <use class="hex hex-in" xlink:href="#_off" x="108" y="0"></use>
        <use class="hex hex-in" xlink:href="#Dd" x="540" y="0"></use>
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