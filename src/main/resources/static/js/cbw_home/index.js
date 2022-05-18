// When the user clicks the button, open the modal 
var mainStationClick = function (element) {
    //var span_head = "<span class='btn' id='closeSpan' onclick='clickSpan(this);'>&times;</span>";
    //console.log(element);
    var span_head = "<span class='btn' id='closeSpan' onclick='clickSpan(this);'><img width='40' height='40' src='/csprscbw/image/close.png' /></span>";
    var table_head = "<table class='table table-bordered table-sm'><thead>" +
        "<tr class='table-info'><th scope='col'>測站名稱</th>" +
        "<th scope='col'>觀測時間</th><th scope='col'>溫度(攝氏)</th>" +
        "<th scope='col'>天氣</th><th scope='col'>風向</th>" +
        "<th scope='col'>風力(級)</th><th scope='col'>陣風(級)</th>" +
        "<th scope='col'>能見度(公里)</th><th scope='col'>相對溼度(%)</th>" +
        "<th scope='col'>海平面氣壓(百帕)</th><th scope='col'>當日累積雨量(毫米)</th>" +
        "<th scope='col'>日照時數</th></tr></thead><tbody>";
    var total = span_head + table_head;
    var station = element.id;
    $.ajax({
        url: "/csprscbw/cwb/stationGetData",
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        data: station,
        // dataType:"json",
        dataType: "text",
        success: function (msg) {
            var modal_content = document.getElementById('modal-content');
            var callback = JSON.parse(msg.replaceAll("'", "\"").replaceAll(">", "大於"));
            for (var i = 0; i < callback.length; i++) {
                var bean = callback[i];
                var html;
                if (i == callback.length - 1) {
                    html = "<tr><td scope='row'>" + bean['station'] + "</td><td>" + bean['curr_time'] + "</td><td>" + bean['temperature'] + "</td>" +
                        "<td>" + bean['weather'] + "</td><td>" + bean['w_direction'] + "</td><td>" + bean['w_power'] + "</td><td>" + bean['w_level'] + "</td>" +
                        "<td>" + bean['visibility'] + "</td><td>" + bean['humidity'] + "</td><td>" + bean['sea_pressure'] + "</td><td>" + bean['rainfall_day'] + "</td>" +
                        "<td>" + bean['sun_hour'] + "</td></tr></tbody></table>";
                } else {
                    html = "<tr><td scope='row'>" + bean['station'] + "</td><td>" + bean['curr_time'] + "</td><td>" + bean['temperature'] + "</td>" +
                        "<td>" + bean['weather'] + "</td><td>" + bean['w_direction'] + "</td><td>" + bean['w_power'] + "</td><td>" + bean['w_level'] + "</td>" +
                        "<td>" + bean['visibility'] + "</td><td>" + bean['humidity'] + "</td><td>" + bean['sea_pressure'] + "</td><td>" + bean['rainfall_day'] + "</td>" +
                        "<td>" + bean['sun_hour'] + "</td></tr>";
                }
                total += html;
            }
            modal_content.innerHTML = total;
            var modal = document.getElementById("myModal");
            modal.style.display = "block";

        }
    })
}
// ************************
var mainStationClick = function (element) {
    //var span_head = "<span class='btn' id='closeSpan' onclick='clickSpan(this);'>&times;</span>";
    //console.log(element);
    var span_head = "<span class='btn' id='closeSpan' onclick='clickSpan(this);'><img width='40' height='40' src='/csprscbw/image/close.png' /></span>";
    var table_head = "<table class='table table-bordered table-sm'><thead>" +
        "<tr class='table-info'><th scope='col'>測站<br>名稱</th>" +
        "<th scope='col' class='align-middle'>觀測<br>時間</th><th scope='col' class='align-middle'>溫度<br>(攝氏)</th>" +
        "<th scope='col' class='align-middle'>天氣</th><th scope='col' class='align-middle'>風向</th>" +
        "<th scope='col' class='align-middle'>風力<br>(級)</th><th scope='col' class='align-middle'>陣風<br>(級)</th>" +
        "<th scope='col' class='align-middle'>能見度<br>(公里)</th><th scope='col' class='align-middle'>相對<br>溼度(%)</th>" +
        "<th scope='col' class='align-middle'>海平面<br>氣壓(百帕)</th><th scope='col' class='align-middle'>當日累積雨量<br>(毫米)</th>" +
        "<th scope='col' class='align-middle'>日照<br>時數</th></tr></thead><tbody>";
    var total = span_head + table_head;
    var station = element.id;
    $.ajax({
        url: "/csprscbw/cwb/stationGetData",
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        data: station,
        // dataType:"json",
        dataType: "text",
        success: function (msg) {
            var modal_content = document.getElementById('modal-content');
            var callback = JSON.parse(msg.replaceAll("'", "\"").replaceAll(">", "大於"));
            for (var i = 0; i < callback.length; i++) {
                var bean = callback[i];
                var html;
                if (i == callback.length - 1) {
                    html = "<tr><td scope='row'>" + bean['station'] + "</td><td>" + bean['curr_time'] + "</td><td>" + bean['temperature'] + "</td>" +
                        "<td>" + bean['weather'] + "</td><td>" + bean['w_direction'] + "</td><td>" + bean['w_power'] + "</td><td>" + bean['w_level'] + "</td>" +
                        "<td>" + bean['visibility'] + "</td><td>" + bean['humidity'] + "</td><td>" + bean['sea_pressure'] + "</td><td>" + bean['rainfall_day'] + "</td>" +
                        "<td>" + bean['sun_hour'] + "</td></tr></tbody></table>";
                } else {
                    html = "<tr><td scope='row'>" + bean['station'] + "</td><td>" + bean['curr_time'] + "</td><td>" + bean['temperature'] + "</td>" +
                        "<td>" + bean['weather'] + "</td><td>" + bean['w_direction'] + "</td><td>" + bean['w_power'] + "</td><td>" + bean['w_level'] + "</td>" +
                        "<td>" + bean['visibility'] + "</td><td>" + bean['humidity'] + "</td><td>" + bean['sea_pressure'] + "</td><td>" + bean['rainfall_day'] + "</td>" +
                        "<td>" + bean['sun_hour'] + "</td></tr>";
                }
                total += html;
            }
            modal_content.innerHTML = total;
            var modal = document.getElementById("myModal");
            modal.style.display = "block";

        }
    })
}

// When the user clicks on <span> (x), close the modal
var clickSpan = function (element) {
    var modal = document.getElementById("myModal");
    modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
    if (event.target == modal) {
        var modal = document.getElementById("myModal");
        modal.style.display = "none";
    }
}

var selectCity = function (sel) {
    $.ajax({
        url: "/csprscbw/cwb/cityGetData",
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        data: sel.value,
        // dataType:"json",
        dataType: "text",
        success: function (msg) {
            var callback = JSON.parse(msg.replaceAll("'", "\""));
            var tbody = document.getElementById('selectCityCallBackTable');
            var html = "";
            //console.log(callback);
            for (var i = 0; i < callback.length; i++) {
                var bean = callback[i];
                html += "<tr><td scope='row'>" + bean['station'] + "</td><td>" + bean['curr_time'] + "</td><td>" + bean['temperature'] + "</td>" +
                    "<td>" + bean['weather'] + "</td><td>" + bean['w_direction'] + "</td><td>" + bean['w_power'] + "</td><td>" + bean['w_level'] + "</td>" +
                    "<td>" + bean['visibility'] + "</td><td>" + bean['humidity'] + "</td><td>" + bean['sea_pressure'] + "</td><td>" + bean['rainfall_day'] + "</td>" +
                    "<td>" + bean['sun_hour'] + "</td></tr>";
            }
            tbody.innerHTML = html;

            document.getElementById('downloadCsv').classList.remove("disabled");
            document.getElementById('downloadCsv').removeAttribute("hidden");

            document.getElementById('viewSuperset').classList.remove("disabled");
            document.getElementById('viewSuperset').removeAttribute("hidden");
        }
    })

}

