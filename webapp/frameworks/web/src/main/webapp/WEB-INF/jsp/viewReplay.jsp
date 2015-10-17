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

        function setConnected(connected) {
            document.getElementById('connect').disabled = connected;
            document.getElementById('disconnect').disabled = !connected;
            document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
            document.getElementById('response').innerHTML = '';
        }

        function connect() {
            var socket = new SockJS('/replays/hello');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                setConnected(true);
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

        function showGreeting(message) {
            var response = document.getElementById('response');

            var json = JSON.parse(message);

            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            if (json.payload.map !== undefined) {
                var map = json.payload.map;
                for (var i = 0; i < map.length; i++) {
                    var row = map[i];
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
                    p.appendChild(rowDiv);
                }
            } else {
                p.appendChild(document.createTextNode(json.payload));
            }
            response.appendChild(p);
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

        .hex, .hex-in {
            width: 72px;
            height: 72px;
            display: inline-block;
            background: linear-gradient(
                    rgba(0, 0, 0, 0.7),
                    rgba(0, 0, 0, 0.7)
            );

            -webkit-mask: url(/hexagon.svg) no-repeat 50% 50%;
            mask: url(/hexagon.svg) no-repeat 50% 50%;
            -webkit-mask-size: cover;
            mask-size: cover;
        }

        .overlay {
            width: 100%;
            height: 100%;
            position: absolute;
            background-color: #000;
            opacity: 0;
            border-radius: 30px;
        }

        div:hover > .overlay {
            opacity: 0.5;
        }

        .hex:hover {
            stroke: #fff20f;
            stroke-width: 2;

        }

    </style>
</head>
<body onload="connect()">
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
    enabled. Please enable
    Javascript and reload this page!</h2></noscript>

<div>
    Just for test:
    <svg id="image-fill" xmlns="http://www.w3.org/2000/svg" version="1.1" width="100%" height="300"
         xmlns:xlink="http://www.w3.org/1999/xlink">

        <defs>
            <pattern id="Wo" x="0" y="0" height="72" width="72" patternUnits="userSpaceOnUse">
                <image width="72" height="72" xlink:href="/data/core/images/terrain/water/ocean-tile.png"></image>
            </pattern>
            <polygon id="hexagon" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36"></polygon>
        </defs>
        <use x="0" y="0" xlink:href="#hexagon" fill="url('#Wo')" class="hex"></use>
        <use x="54" y="36" xlink:href="#hexagon" fill="url('#Wo')" class="hex"></use>
        <use x="108" y="0" xlink:href="#hexagon" fill="url('#Wo')" class="hex"></use>
        <use x="162" y="36" xlink:href="#hexagon" fill="url('#Wo')" class="hex"></use>

        <use x="0" y="72" xlink:href="#hexagon" fill="url('#Wo')" class="hex"></use>
        <use x="54" y="108" xlink:href="#hexagon" fill="url('#Wo')" class="hex"></use>
        <use x="108" y="72" xlink:href="#hexagon" fill="url('#Wo')" class="hex"></use>
        <use x="162" y="108" xlink:href="#hexagon" fill="url('#Wo')" class="hex"></use>


    </svg>
    <svg id="hex-image" xmlns="http://www.w3.org/2000/svg" version="1.1" width="72px" height="72px"
         xmlns:xlink="http://www.w3.org/1999/xlink">
        <polygon points="18,0 54,0 72,36 54,72 18,72 0,36"></polygon>
    </svg>
    <img src="/data/core/images/terrain/water/ocean-tile.png">

    <div class="hex Dd"></div>
</div>

<div>
    <div>
        <button id="connect" onclick="connect();">Connect</button>
        <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
    </div>
    <div id="conversationDiv">
        <p id="response"></p>
    </div>
</div>
<br>
<br>
<style>
    <c:forEach items="${viewReplayDto.images}" var="image" varStatus="loopStatus">
    <%-- TODO: refactor this - we shouldn't create a css for the color all over again, if it was already defined --%>
    <c:set var="imageCssClass" value="${fn:replace(image.name, ' ' ,'_' )}"/>
    .${imageCssClass} {
        background: url(${image.uri});
    }

    </c:forEach>


</style>
</body>
</html>