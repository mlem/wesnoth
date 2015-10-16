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

                        div.classList.add("tile");
                        var tileClasses = tile.split("^");
                        var lastDiv = div;
                        for (var k = 0; k < tileClasses.length; k++) {
                            var subdiv = document.createElement('div');
                            subdiv.classList.add("tile");
                            subdiv.style.margin = "-6px";
                            subdiv.classList.add(tileClasses[k]);
                            lastDiv.appendChild(subdiv);
                            lastDiv = subdiv;

                        }
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

        div.tile {
            padding: 6px;
            width: 60px;
            height: 60px;
            top: -6px;
            left: -6px;
            float: left;
        }

        div.tile:nth-child(2n) {
            margin-top: -36px;
        }

        div.tile:nth-child(n+1) {
            margin-left: -18px;
        }

    </style>
</head>
<body onload="connect()">
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
    enabled. Please enable
    Javascript and reload this page!</h2></noscript>

<div>
    Just for test:
    <img src="/data/core/images/terrain/water/ocean-tile.png">
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
    div.${imageCssClass} {
        background: url(${image.uri});
    }

    </c:forEach>


</style>
</body>
</html>