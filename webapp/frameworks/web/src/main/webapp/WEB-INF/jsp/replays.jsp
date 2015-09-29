<%-- see  http://www.tutorialspoint.com/jsp/jsp_standard_tag_library.htm --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="org.wesnoth.i18n.replay"/>
<!DOCTYPE html>
<html lang="${language}">
<head>
    <title>Replays</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <style>
        .wrapword {
            white-space: -moz-pre-wrap !important; /* Mozilla, since 1999 */
            white-space: -o-pre-wrap; /* Opera 7 */
            white-space: pre-wrap; /* css-3 */
            word-wrap: break-word; /* Internet Explorer 5.5+ */
            word-break: break-all;
        }

        .glyphicon-tower {
            font-size: x-large;
        }

        .glyphicon-download-alt, .glyphicon-play-circle {
            font-size: xx-large;
        }
    </style>

    <script>
        $(function () {
            $('[data-toggle="popover"]').popover()
        })
    </script>
</head>
<body>

<br>
<br>

<table class="table">
    <tr>
        <th><fmt:message key="replay.table.heading.mapname"/></th>
        <th><fmt:message key="replay.table.heading.recorddate"/></th>
        <th><fmt:message key="replay.table.heading.size"/></th>
        <th><fmt:message key="replay.table.heading.gamename"/></th>
        <th><fmt:message key="replay.table.heading.era"/></th>
        <th><fmt:message key="replay.table.heading.players"/></th>
        <th><fmt:message key="replay.table.heading.actions"/></th>
    </tr>
    <c:forEach items="${replayInfos}" var="replayInfo">
        <tr>
            <td class="wrapword">${replayInfo.mapName}</td>
            <td><fmt:formatDate type="both" value="${replayInfo.recordedDate}" dateStyle="short"
                                timeStyle="short"/></td>
            <td>${replayInfo.replaySize}</td>
            <c:choose>
                <c:when test="${fn:length(replayInfo.gameName) > 20}">
                    <c:set var="shortGameName" value="${fn:substring(replayInfo.gameName, 0, 20)}"/>
                    <td><abbr title="${replayInfo.gameName}">${shortGameName}</abbr></td>
                </c:when>
                <c:otherwise>
                    <td>${replayInfo.gameName}</td>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${fn:length(replayInfo.era) >= 20}">
                    <c:set var="shortEra" value="${fn:substring(replayInfo.era, 0, 20)}"/>
                    <td><abbr title="${replayInfo.era}">${shortEra}</abbr></td>
                </c:when>
                <c:otherwise>
                    <td>${replayInfo.era}</td>
                </c:otherwise>
            </c:choose>

            <td>
                <style>
                    <c:forEach items="${replayInfo.players}" var="player" varStatus="loopStatus">
                    <c:set var="playerCssClass" value="${fn:replace(player.name, ' ' ,'_' )}"/>
                    <%-- TODO: refactor this - we shouldn't create a css for the color all over again, if it was already defined --%>
                    .${playerCssClass} {
                        color: ${'#' += player.colorCode};
                    }

                    </c:forEach>
                </style>
                <c:forEach items="${replayInfo.players}" var="player">
                    <c:set var="playerCssClass" value="${fn:replace(player.name, ' ' ,'_' )}"/>

                    <span class="glyphicon glyphicon-tower ${playerCssClass}" aria-hidden="true"
                          data-toggle="popover" title="" data-content="${player.name}"
                          data-placement="top" data-trigger="hover"></span>
                    <span class="sr-only">${player.name}</span>
                </c:forEach>
            </td>
                <%-- TODO: refactor this - we shouldn't write http:// in front of the url, the url should contain it already... --%>
            <td><a href="${replayInfo.downloadUri}" class="btn btn-default" title="download"><span
                    class="glyphicon glyphicon-download-alt"></span> </a>

                <form action="replay/view/${replayInfo.replayId}.html" method="post">
                    <input type="hidden" name="downloadUri" value="${replayInfo.downloadUri}">
                    <button type="submit" class="btn btn-default"><span
                            class="glyphicon glyphicon-play-circle"></span></button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>