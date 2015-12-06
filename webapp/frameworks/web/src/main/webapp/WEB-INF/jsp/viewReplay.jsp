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
    <script src="/wesnoth.js"></script>
    <link href="/wesnoth.css" type="text/css" rel="stylesheet" />
    <script>

        replayDownloadUrl = "${viewReplayDto.downloadUri}";
        ajaxSvg("/test-map.svg");
        ajaxSvg("/wesnoth-definitions.svg");
    </script>
</head>
<body id="body" onload="connect()">
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
    enabled. Please enable
    Javascript and reload this page!</h2></noscript>


<div>

    <svg id="svg-map2" xmlns="http://www.w3.org/2000/svg" version="1.1">
        <rect width="100%" height="100%" fill="url('#background')">
        </rect>
    </svg>
    <div id="conversationDiv">
        <p id="response"></p>
    </div>
</div>
</body>
</html>