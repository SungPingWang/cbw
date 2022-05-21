(function () {
    const second = 1000,
        minute = second * 60,
        hour = minute * 60,
        day = hour * 24;

    // let birthday = "Sep 30, 2021 00:00:00",
    current = new Date().getTime(),
        x = setInterval(function () {

            let now = new Date().getTime(),
                distance = now - current;

            //document.getElementById("days").innerText = Math.floor(distance / (day)),
            try {
                document.getElementById("hours").innerText = Math.floor((distance % (day)) / (hour));
            } catch (error) {
            }

            document.getElementById("minutes").innerText = Math.floor((distance % (hour)) / (minute));
            document.getElementById("seconds").innerText = Math.floor((distance % (minute)) / second);

        }, 0)
}());

// ----------------------------

// 滾到最上面
$(document).ready(function () {
    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('#scroll').fadeIn();
        } else {
            $('#scroll').fadeOut();
        }
    });
    $('#scroll').click(function () {
        $("html, body").animate({ scrollTop: 0 }, 600);
        return false;
    });
});

// ----------------------------
// station20Cbwbeans Json文字，weatherJson 天氣JSON - PNG，weatherType 需要的欄位名稱
var weatherStation20Process = function (station20Cbwbeans, weatherJson, weatherType) {
    var station20CbwJson = JSON.parse(station20Cbwbeans.replaceAll("'", "\"").replaceAll(">", "大於"));
    var cardDeckHtml1 = "<div class='row'>";
    var cardDeckHtml2 = "<div class='row'>";
    var cardDeckHtml3 = "<div class='row'>";
    var cardDeckHtml4 = "<div class='row'>";
    var cardDeckHtml5 = "<div class='row'>";

    for (var i = 0; i < station20CbwJson.length; i++) {
        var temp_json = station20CbwJson[i];
        var weatherTypeText = "-";
        var html = "";
        //var weatherPng = "/image/weather/rainwater-catchment.png";
        if (temp_json[weatherType] in weatherJson) {
            weatherPng = weatherJson[temp_json[weatherType]];
            weatherTypeText = temp_json[weatherType];
        } else if (weatherType == "humidity" || weatherType == "rainfall_day") {
            weatherPng = weatherJson['other'];
            weatherTypeText = temp_json[weatherType];
        } else {
            weatherPng = weatherJson['other'];
        }

        if (i == 0 || i == 1 || i == 2 || i == 3) {
            html = "<div class='col-sm-3'><div class='card border-info'>" +
                "<button id='" + temp_json['station'] + "' class='card-header mainStation btn' onclick='mainStationClick(this);'>" +
                temp_json['station'] + "&nbsp <img width='40' height='40' src='" + weatherPng + "'><br>" + weatherTypeText +
                "</button></div></div>";
            cardDeckHtml1 += html;
        } else if (i == 4 || i == 5 || i == 6 || i == 7) {
            html = "<div class='col-sm-3'><div class='card border-info'>" +
                "<button id='" + temp_json['station'] + "' class='card-header mainStation btn' onclick='mainStationClick(this);'>" +
                temp_json['station'] + "&nbsp <img width='40' height='40' src='" + weatherPng + "'><br>" + weatherTypeText +
                "</button></div></div>";
            cardDeckHtml2 += html;
        } else if (i == 8 || i == 9 || i == 10 || i == 11) {
            html = "<div class='col-sm-3'><div class='card border-info'>" +
                "<button id='" + temp_json['station'] + "' class='card-header mainStation btn' onclick='mainStationClick(this);'>" +
                temp_json['station'] + "&nbsp <img width='40' height='40' src='" + weatherPng + "'><br>" + weatherTypeText +
                "</button></div></div>";
            cardDeckHtml3 += html;
        } else if (i == 12 || i == 13 || i == 14 || i == 15) {
            html = "<div class='col-sm-3'><div class='card border-info'>" +
                "<button id='" + temp_json['station'] + "' class='card-header mainStation btn' onclick='mainStationClick(this);'>" +
                temp_json['station'] + "&nbsp <img width='40' height='40' src='" + weatherPng + "'><br>" + weatherTypeText +
                "</button></div></div>";
            cardDeckHtml4 += html;
        } else {
            html = "<div class='col-sm-3'><div class='card border-info'>" +
                "<button id='" + temp_json['station'] + "' class='card-header mainStation btn' onclick='mainStationClick(this);'>" +
                temp_json['station'] + "&nbsp <img width='40' height='40' src='" + weatherPng + "'><br>" + weatherTypeText +
                "</button></div></div>";
            cardDeckHtml5 += html;
        }
    }

    cardDeckHtml1 += "</div><br>";
    cardDeckHtml2 += "</div><br>";
    cardDeckHtml3 += "</div><br>";
    cardDeckHtml4 += "</div><br>";
    cardDeckHtml5 += "</div></div>";

    var tatal_html = cardDeckHtml1 + cardDeckHtml2 + cardDeckHtml3 + cardDeckHtml4 + cardDeckHtml5;
    document.getElementById("station20s").innerHTML = tatal_html;
}
// ----------------------------
var createStationBoardStatusHtml = function (station20Cbwbeans) {
    var weatherJson = {
        "晴": "/csprscbw/image/weather/summer.png",
        "多雲": "/csprscbw/image/weather/partly-cloudy-day.png",
        "陰": "/csprscbw/image/weather/rain.png",
        "晴有靄": "/csprscbw/image/weather/partly-cloudy-day.png",
        "陰有靄": "/csprscbw/image/weather/partly-cloudy-day.png",
        "多雲": "/csprscbw/image/weather/partly-cloudy-day.png",
        "other": "/csprscbw/image/weather/sun.png"
    }
    weatherStation20Process(station20Cbwbeans, weatherJson, 'weather');
}

var createStationBoardHumidityHtml = function (station20Cbwbeans) {
    var weatherJson = {
        "other": "/csprscbw/image/weather/humidity1.png"
    }
    weatherStation20Process(station20Cbwbeans, weatherJson, 'humidity');
}

var createStationBoardRainFallHtml = function (station20Cbwbeans) {
    var weatherJson = {
        "other": "/csprscbw/image/weather/rainfall1.png"
    }
    weatherStation20Process(station20Cbwbeans, weatherJson, 'rainfall_day');
}


