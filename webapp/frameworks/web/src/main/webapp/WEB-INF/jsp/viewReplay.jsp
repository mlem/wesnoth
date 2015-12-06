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
    <script type="text/javascript">
        var stompClient = null;


        function connect() {
            var socket = new SockJS('/replays/hello');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/greetings', function (greeting) {
                    showGreeting(greeting.body);
                });
                sendName();
            });
        }

        function disconnect() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
        }

        function sendName() {
            stompClient.send("/app/hello", {}, "${viewReplayDto.downloadUri}");
        }

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
            var svg = document.getElementById('svg-map2');
            var w = window.innerWidth;
            var h = window.innerHeight;
            //svg.setAttribute('width', '' + (map.length * 54 + 54));
            //svg.setAttribute('height', '' + (map.length * 72 + 72));
            function displayWidth() {
                return w - 150;
            }

            function displayHeight() {
                return h - 100;
            }
            function mapWidth() {
                return (map.length * 54 + 54);
            }

            function mapHeight() {
                return (map.length * 72 + 72);
            }

            svg.setAttribute('width', '' + mapWidth() );
            svg.setAttribute('height', '' + mapHeight() );

            // svg.setAttribute('viewBox', '0 0 '+displayWidth()+' '+ displayHeight())
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
                //appendAsHtml(map, p);


            } else {
                p.appendChild(document.createTextNode(json.payload));
            }
            response.appendChild(p);
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
<body id="body" onload="connect()">
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
    enabled. Please enable
    Javascript and reload this page!</h2></noscript>


<div>

    <svg id="svg-map2" xmlns="http://www.w3.org/2000/svg" version="1.1">
    </svg>
    <div id="conversationDiv">
        <p id="response"></p>
    </div>
</div>
</body>
</html>