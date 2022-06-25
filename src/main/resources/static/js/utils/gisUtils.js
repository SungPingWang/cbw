/* 四捨五入(經緯度用) */
var roundDecimal = function (val, precision) {
    return Math.round(Math.round(val * Math.pow(10, (precision || 0) + 1)) / 10) / Math.pow(10, (precision || 0));
}

/* wgs84轉twd97 */
var lonlat2twd97 = function (lon, lat) {
    var a = 6378137.0;
    var b = 6356752.3142451;
    var lon0 = 121 * Math.PI / 180;
    var k0 = 0.9999;
    var dx = 250000;
    var dy = 0;
    var e = 1 - Math.pow(b, 2) / Math.pow(a, 2);
    var e2 = (1 - Math.pow(b, 2) / Math.pow(a, 2)) / (Math.pow(b, 2) / Math.pow(a, 2));


    var TWD97 = "";

    lon = (lon - Math.floor((lon + 180) / 360) * 360) * Math.PI / 180;
    lat = lat * Math.PI / 180;

    var V = a / Math.sqrt(1 - e * Math.pow(Math.sin(lat), 2));
    var T = Math.pow(Math.tan(lat), 2);
    var C = e2 * Math.pow(Math.cos(lat), 2);
    var A = Math.cos(lat) * (lon - lon0);
    var M = a * ((1.0 - e / 4.0 - 3.0 * Math.pow(e, 2) / 64.0 - 5.0 * Math.pow(e, 3) / 256.0) * lat -
        (3.0 * e / 8.0 + 3.0 * Math.pow(e, 2) / 32.0 + 45.0 * Math.pow(e, 3) / 1024.0) *
        Math.sin(2.0 * lat) + (15.0 * Math.pow(e, 2) / 256.0 + 45.0 * Math.pow(e, 3) / 1024.0) *
        Math.sin(4.0 * lat) - (35.0 * Math.pow(e, 3) / 3072.0) * Math.sin(6.0 * lat));
    // x
    var x = dx + k0 * V * (A + (1 - T + C) * Math.pow(A, 3) / 6 + (5 - 18 * T + Math.pow(T, 2) + 72 * C - 58 * e2) * Math.pow(A, 5) / 120);
    // y
    var y = dy + k0 * (M + V * Math.tan(lat) * (Math.pow(A, 2) / 2 + (5 - T + 9 * C + 4 * Math.pow(C, 2)) * Math.pow(A, 4) / 24 + (61 - 58 * T + Math.pow(T, 2) + 600 * C - 330 * e2) * Math.pow(A, 6) / 720));

    TWD97 = roundDecimal(x, 5).toString() + ", " + roundDecimal(y, 5).toString();
    return TWD97;
}

/* 圖資檔案上傳的相關步驟 */
var deleteUploadShp = function (map, UploadViewOpt) {
    // 新增刪除圖資事件
    var UploadDeleteOpt = document.getElementsByClassName("uploadLayerDelete");
    for (var i = 0; i < UploadDeleteOpt.length; i++) {
        (function (index) {
            UploadDeleteOpt[index].addEventListener("click", function () {
                var layerss = map.layers;
                for (var lyr of layerss) {
                    var id_cnt = UploadViewOpt[index].id.split('_')[1];
                    if (lyr.id == 'uploadLayer_' + id_cnt) {
                        //map.layers.remove(lyr);
                        lyr.visible = false; // 我們只把它隱藏起來就好了，不然會很麻煩 
                        var uploadElement = document.getElementById('uploadLayer_' + id_cnt)
                        uploadElement.parentElement.parentElement.parentElement.hidden = true;
                        document.getElementById("file").value = "";
                        // range回歸正常
                        var rangeInfo = document.getElementById("uploadOptInfo");
                        rangeInfo.innerHTML = "<label for='customRange3'>透明度</label><span id='valBox'></span><br>" +
                            "<input type='range' class='custom-range' min='1' max='10' step='1' id='customRange3' disabled>";
                        updateLastStep("刪除上傳圖資");
                        document.getElementById("uploadIt").disabled = true;
                    }
                }
            })
        })(i);
    }
}
var moveToUploadShp = function (map, UploadViewOpt, view) {
    // 新增定位事件
    for (var i = 0; i < UploadViewOpt.length; i++) {
        (function (index) {
            UploadViewOpt[index].addEventListener("click", function () {
                var layerss = map.layers;
                var isok = 0; // 設置isok參數判斷錯誤
                for (var lyr of layerss) {
                    var id_cnt = UploadViewOpt[index].id.split('_')[1];
                    if (lyr.id == 'uploadLayer_' + id_cnt) {
                        // layer goto
                        lyr.queryExtent().then((response) => {
                            view.goTo(response.extent)
                                .catch((error) => {
                                    console.error(error);
                                });
                        });
                        isok++;
                        updateLastStep("移動至上傳圖資");
                    }
                }
                if (isok == 0) { // 如果有錯誤 → isok==0，就重新整理他
                    alert("檔案文件發生問題，將在提醒後刪除");
                    try {
                        map.layers.remove(lyr);
                    } catch (e) {
                        alert("刪除檔案發生錯誤");
                    }
                    UploadViewOpt[index].parentElement.parentElement.remove();
                    document.getElementById("file").value = "";
                    location.reload();
                }
            })
        })(i);
    }
}
var opacityUploadShp = function (map, UploadOpacityOpt, uploadFeatureColorCycle) {
    // 新增透明度事件
    for (var i = 0; i < UploadOpacityOpt.length; i++) {
        (function (index) {
            UploadOpacityOpt[index].addEventListener("change", function () {
                var current_range = UploadOpacityOpt[index]; // 當前的range事件獲取條
                var id_cnt = current_range.id.split('_')[1]; // 獲取假設opacity_0切分後的"0"index
                var layerss = map.layers;
                for (var lyr of layerss) { // 便利所有layer
                    if (lyr.id == 'uploadLayer_' + id_cnt) { // ex: uploadLayer_0
                        // 將range指數/10四捨五入到小數第一位則為透明度
                        var set_opacity = (current_range.value / 10).toFixed(1);
                        // 透過我們的index id來判斷說是屬於哪種顏色的，id / uploadFeatureColorCycle陣列長取餘數
                        var temp_color = uploadFeatureColorCycle[(id_cnt) % uploadFeatureColorCycle.length];
                        var set_color = [temp_color[0], temp_color[1], temp_color[2], set_opacity];
                        // 設置它的顏色
                        lyr.renderer = {
                            type: "simple",
                            symbol: {
                                type: "simple-fill",
                                color: set_color,
                                outline: { color: "white", width: 1 }
                            }
                        };
                        updateLastStep("更新上傳圖資透明度");
                    }
                }
            })
        })(i);
    }
}

/* 讀取goejson檔案 */
var getGeoJson = function (path) {
    setblockUI();
    let mockDataPath = path;
    let geoJson = "";
    let rawFile = new XMLHttpRequest();
    rawFile.open("GET", mockDataPath, false);
    rawFile.onreadystatechange = function () {
        if (rawFile.readyState === 4) {
            if (rawFile.status === 200 || rawFile.status == 0) {
                let allText = rawFile.responseText;
                geoJson = allText;
            }
        }
    }
    rawFile.send(null);
    return geoJson;
}

var arcgisInit = function () {
    /**/
    var orgCoordinateDisplayNode = document.getElementsByClassName('esri-coordinate-conversion__display')[0];
    orgCoordinateDisplayNode.addEventListener('DOMNodeInserted',
        function () {
            var pntSplit = orgCoordinateDisplayNode.innerHTML.split(",")
            var html = "WGS84: " + orgCoordinateDisplayNode.innerHTML + "<br>";
            var twd97Array = lonlat2twd97(
                pntSplit[0].trim().substring(0, pntSplit[0].trim().length - 2),
                pntSplit[1].trim().substring(0, pntSplit[1].trim().length - 2));
            html += "TWD97: " + twd97Array;
            document.getElementById("attrId").innerHTML = html;
        }, false);
    document.getElementsByClassName("esri-attribution__powered-by")[0].innerHTML = "&copy;中華民國航空測量及遙感探測學會 2021-2022";

    if (document.getElementsByClassName("esri-icon-plus").length != 0) {
        // 修改zoom in out圖片
        var plusIcon = document.getElementsByClassName("esri-icon-plus")[0];
        plusIcon.classList.add("esri-icon-zoom-in-magnifying-glass");
        plusIcon.classList.remove("esri-icon-plus");
        var minusIcon = document.getElementsByClassName("esri-icon-minus")[0];
        minusIcon.classList.add("esri-icon-zoom-out-magnifying-glass");
        minusIcon.classList.remove("esri-icon-minus");
    }

}

// 設定系統/上傳圖資的查詢設定
var queryLayerByAttribute = function (map, view, zoomScale, UploadSearchViewOpt, type) {
	let lyrQueryFieldInput = document.getElementById(type + "QueryFieldInput");
	let lyrQueryFieldSelect = document.getElementById(type + "QueryFieldSelect");
	let lyrQueryFieldSearch = document.getElementById(type + "QueryFieldSearch");
	let lyrQueryFieldCheck = document.getElementById(type + "QueryFieldCheck");
    
    for (let i = 0; i < UploadSearchViewOpt.length; i++) {
        let layerBtn = UploadSearchViewOpt[i];
        layerBtn.addEventListener("click", function (e) {
			let lyrId = e.target.id;
			if(type == "uploadLyr"){
				lyrId = (e.target.id).replaceAll("searchTo_", "uploadLayer_");
			}
			console.log(type == "uploadLyr");
			console.log(type);
            for (let lyr of map.layers) {
                if (lyrId == lyr.id || lyrId == lyr.id + "-qyBtn") {
                    let mockDataCanQuery = [];
                    for (let f = 0; f < lyr.fields.length; f++) {
                        mockDataCanQuery.push(lyr.fields[f].name);
                    }
                    $("#"+type+"QueryFieldSelectDialog").dialog("open");
                    let lyrQueryFieldSelectHtml = "<option selected value='0'>請選擇欄位</option>";
                    for (let i = 0; i < mockDataCanQuery.length; i++) {
                        lyrQueryFieldSelectHtml += "<option value='" + mockDataCanQuery[i] + "'>" + mockDataCanQuery[i] + "</option>"
                    }
                    lyrQueryFieldSelect.innerHTML = lyrQueryFieldSelectHtml;
                    // 輸入查詢欄位觸發後
                    lyrQueryFieldInput.addEventListener("keyup", function () {
                        if (lyrQueryFieldInput.value.trim() != "") {
                            lyrQueryFieldSelect.disabled = false;
                        } else {
                            lyrQueryFieldSelect.disabled = true;
                        }
                    });
                    // 選擇欄位觸發事件 test
                    lyrQueryFieldSelect.addEventListener("change", function () {
                        let query = lyr.createQuery();
                        query.where = lyrQueryFieldSelect.value + " = '" + lyrQueryFieldInput.value + "'";
                        query.outFields = ["*"];
                        lyr.queryFeatures(query).then(function (response) {
							console.log(response);
                            if (response.features.length > 0) {
                                lyrQueryFieldSelect.disabled = false;
                                lyrQueryFieldSearch.disabled = false;
                                let headName = mockDataCanQuery[0];
                                let lyrQueryFieldCheckHtml = "";
                                for (let i = 0; i < response.features.length; i++) {
                                    let x = response.features[i].geometry.extent.center.longitude;
                                    let y = response.features[i].geometry.extent.center.latitude;
                                    let show = response.features[i].attributes[headName];
									let attr = alertOneStackObject(response.features[i].attributes);
                                    lyrQueryFieldCheckHtml += "<div class='form-check'>";
                                    if (i == 0) {
                                        lyrQueryFieldCheckHtml += "<input class='form-check-input' type='radio' name='"+type+"QueryFieldCheckRadio' id='"+type+"QueryFieldCheckRadio" + i + "' value='" + JSON.stringify([x, y]) + "' checked>";
                                    } else {
                                        lyrQueryFieldCheckHtml += "<input class='form-check-input' type='radio' name='"+type+"QueryFieldCheckRadio' id='"+type+"QueryFieldCheckRadio" + i + "' value='" + JSON.stringify([x, y]) + "' >";
                                    }
                                    lyrQueryFieldCheckHtml += "<label class='form-check-label' for='"+type+"QueryFieldCheckRadio" + i + "'>";
									lyrQueryFieldCheckHtml += "<button type='button' class='btn btn-outline-dark btn-sm "+type+"QueryFieldCheckBtn' data-content='"+attr+"' >" + show + "</button>";
                                    lyrQueryFieldCheckHtml += "</label>";
                                    lyrQueryFieldCheckHtml += "</div>";
                                }
                                lyrQueryFieldCheck.innerHTML = lyrQueryFieldCheckHtml;
                                let lyrQueryFieldCheckBtn = document.getElementsByClassName(type + "QueryFieldCheckBtn");
                                for (let i = 0; i < lyrQueryFieldCheckBtn.length; i++) {
                                    lyrQueryFieldCheckBtn[i].addEventListener("click", function () {
										document.getElementById(type + "QueryFieldDescription").value = lyrQueryFieldCheckBtn[i].dataset.content;
                                    })
                                }
                                lyrQueryFieldSearch.addEventListener("click", function () {
                                    view.goTo({
                                        center: JSON.parse($("input[name="+type+"QueryFieldCheckRadio]:checked").val()),
                                        zoom: zoomScale
                                    })
                                    lyrQueryFieldInput.value = "";
                                    lyrQueryFieldSelect.disabled = true;
                                    lyrQueryFieldSearch.disabled = true;
                                    lyrQueryFieldCheck.innerHTML = "";
                                })
                            } else {
                                lyrQueryFieldCheck.innerHTML = "";
                                lyrQueryFieldSearch.disabled = true;
                            }
                        });
                    });
                    break;
                }
            }
        });
    }
}













