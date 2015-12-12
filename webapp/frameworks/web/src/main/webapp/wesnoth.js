var stompClient = null;
var replayDownloadUrl = null;


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
    stompClient.send("/app/hello", {}, replayDownloadUrl);
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
            overlayPolygon.setAttributeNS("http://www.w3.org/1999/xlink", 'xlink:href', '#' + tileClasses[k]);
            overlayPolygon.setAttribute('x', j * 54);
            overlayPolygon.setAttribute('y', (j + 1 & 1) * 36);
            rowDiv.appendChild(overlayPolygon);
        }
    }
    return rowDiv;

}
function appendAsSvg(map) {
    var svg = document.getElementById('svg-map2');
    var w = window.innerWidth;
    var h = window.innerHeight;

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

    svg.setAttribute('width', '' + mapWidth());
    svg.setAttribute('height', '' + mapHeight());

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
function ajaxSvg(svgUrl) {
    var ajax = new XMLHttpRequest();
    ajax.open("GET", svgUrl, true);
    ajax.send();
    ajax.onload = function (e) {
        if (ajax.readyState === 4) {
            if (ajax.status === 200) {
                var div = document.createElement("div");
                div.innerHTML = ajax.responseText;
                document.body.insertBefore(div, document.body.childNodes[0]);
            } else {
                console.log("Error", ajax.statusText);
                var div = document.createElement("div");
                div.innerHTML = ajax.responseText;
                document.body.insertBefore(div, document.body.childNodes[0]);
            }
        }
    };
    ajax.onerror = function (e) {
        console.error(xhr.statusText);
    };

}