var test = "2werfwe";
var alterBean = function (msg) {
    var th_tag = msg.parentElement.parentElement.getElementsByTagName("th");
    var td_tag = msg.parentElement.parentElement.getElementsByTagName("td");


    var id = th_tag[0].innerHTML;
    var name = td_tag[0].innerHTML;
    var password = td_tag[1].innerHTML;
    var description = td_tag[2].innerHTML;
    var role = td_tag[3].innerHTML;

    console.log(role);
    var role_split = role.split(",");
    if (role_split.includes("ADMIN")) {
        document.getElementById("roleCheck1").checked = true;
    } else {
        document.getElementById("roleCheck1").checked = false;
    }
    if (role_split.includes("MANAGER")) {
        document.getElementById("roleCheck2").checked = true;
    } else {
        document.getElementById("roleCheck2").checked = false;
    }
    if (role_split.includes("USER")) {
        document.getElementById("roleCheck3").checked = true;
    } else {
        document.getElementById("roleCheck3").checked = false;
    }

    document.getElementById("inputId").value = id;
    document.getElementById("inputName").value = name;
    document.getElementById("inputPassword").value = password;
    document.getElementById("inputDescription").value = description;
}

var alterAccount = function () {
    var select_id = $('#inputId').val();
    var select_name = $('#inputName').val();
    var select_pwd = $('#inputPassword').val();
    var select_desp = $('#inputDescription').val();
    var select_chk1 = document.getElementById("roleCheck1");
    var select_chk2 = document.getElementById("roleCheck2");
    var select_chk3 = document.getElementById("roleCheck3");
    var role_cnt = 0;
    if (select_chk1.checked) {
        role_cnt += 1;
    }
    if (select_chk2.checked) {
        role_cnt += 10;
    }
    if (select_chk3.checked) {
        role_cnt += 100;
    }
    var role_cnt_str = role_cnt.toString();
    // 可能要考慮不用逗號分隔，因為description有可能就會有逗號
    var ajaxInput = select_id + ";" + select_name + ";" + select_pwd + ";" + select_desp + ";" + role_cnt_str;
    var sure = confirm("are you sure to add new account or update?");
    if (sure && role_cnt != 0 && select_name != "" && select_name != "" && select_pwd != "" && select_desp != "") {
        $.ajax({
            url: "/csprsApp/profile/addUpdate",
            type: "POST",
            contentType: 'application/json; charset=utf-8',
            data: ajaxInput,
            // dataType:"json",
            dataType: "text",
            success: function (msg) {
                if (msg == "success") {
                    alert("Operation Success，重新整理檢查更新");
                } else {
                    alert(msg);
                }
                location.href = "/csprsApp/profile/account?page=1";
            }
        });
    } else {
        alert("you have to write down all the form");
    }
};
var getStationData = function () {
    var selected = $('#select1').find(":selected").text();
    location.href = "/csprscbw/cbw_station?city=" + selected;
}

var displayOnMap = function(station, city) {
	var title = "測站位置：" + city + station;
	console.log(title);
	$( "#viewDiv" ).dialog( "option", "title", title );
	$( "#viewDiv" ).dialog( "open" );
	
}