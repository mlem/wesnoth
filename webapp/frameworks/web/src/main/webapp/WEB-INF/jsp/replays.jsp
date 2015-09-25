<%-- see  http://www.tutorialspoint.com/jsp/jsp_standard_tag_library.htm --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Replays</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>
<body>

<br>
<br>

<table class="table">
    <tr>
        <th>filename</th>
        <th>record date</th>
        <th>replay size</th>
        <th>game name</th>
        <th>era</th>
        <th>players</th>
        <th><fmt:message key="person.form.lastName"/></th>
    </tr>
    <c:forEach items="${replayInfos}" var="replayInfo">
        <tr>
            <td>${replayInfo.filename}</td>
            <td><fmt:formatDate type="both" value="${replayInfo.recordedDate}" dateStyle="short"
                                timeStyle="short"/></td>
            <td>${replayInfo.replaySize}</td>
            <td>${replayInfo.gameName}</td>
            <td>${replayInfo.era}</td>
            <td>${replayInfo.players}</td>
            <td><a href="http://${replayInfo.downloadUri}" role="button">download</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>