var alterBean = function (msg) {
    var th_tag = msg.parentElement.parentElement.getElementsByTagName("th");
    var td_tag = msg.parentElement.parentElement.getElementsByTagName("td");

    var id = th_tag[0].innerHTML;
    var name = replaceInput(td_tag[0].innerHTML);
    var password = replaceInput(td_tag[1].innerHTML);
	var mail = replaceInput(td_tag[2].innerHTML);
    var description = replaceInput(td_tag[3].innerHTML);
    var role = td_tag[4].getElementsByTagName("p")[0].innerHTML;
	var role_split = role.split(",");
	
	if(document.getElementById("roleCheck1") != null){
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
	}
    
    document.getElementById("inputId").value = id;
    document.getElementById("inputName").value = name;
    document.getElementById("inputPassword").value = password;
	document.getElementById("inputMail").value = mail;
    document.getElementById("inputDescription").value = description;
}
var alterAccount = function () {
    var select_id = $('#inputId').val();
    var select_name = replaceInput($('#inputName').val());
    var select_pwd = replaceInput($('#inputPassword').val());
	var select_mail = replaceInput($('#inputMail').val());
    var select_desp = replaceInput($('#inputDescription').val());
    var select_chk1 = document.getElementById("roleCheck1");
    var select_chk2 = document.getElementById("roleCheck2");
    var select_chk3 = document.getElementById("roleCheck3");

	// 驗證階段
	if(!select_mail.includes("@")){
		alert("信箱錯誤!!");
		return false;
	}

	var ajaxInput = "";
	if(select_chk1 != null && select_chk2 != null && select_chk3 != null){
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
	    ajaxInput = select_id + ";" + select_name + ";" + select_pwd + ";" + select_desp + ";" + role_cnt_str + ";" + select_mail;
	}else{
		ajaxInput = select_id + ";" + select_name + ";" + select_pwd + ";" + select_desp + ";" + "999" + ";" + select_mail;
	}
    var sure = confirm("are you sure to add new account or update?");
    if (sure && role_cnt != 0 && select_name != "" && select_name != "" && select_pwd != "" && select_desp != "" && select_mail != "") {
        $.ajax({
            url: "/csprscbw/account/addUpdate",
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
                location.href = "/csprscbw/account?page=1";
            }
        });
    } else {
        alert("you have to write down all the form");
    }
};

var replaceInput = function(element) {
	return element.replaceAll(';', ',');
}
