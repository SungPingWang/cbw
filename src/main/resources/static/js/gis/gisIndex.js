// 更新顯示上個步驟的div
var updateLastStep = function (talk) {
  document.getElementById("lastStep").innerHTML = talk;
}
// 圖資服務讀取(和constants.js是綁定的)
var readAndSetMapServiceWidget = function () {
  let mapService = Constants.mapService;
  let mapServiceHtml = "";
  Object.keys(mapService).forEach(key => {
    let keyArray = mapService[key];
    mapServiceHtml += "<optgroup label='" + key + "'>";
    keyArray.forEach(value => {
      let txt = "<option id='" + value["id"] + "' value='" + value["id"] + "'>" + value["name"] + "</option>";
      mapServiceHtml += txt;
    });
    mapServiceHtml += "</optgroup>";
  });
  document.getElementById("dropdownMenu1").innerHTML += mapServiceHtml;
}
// 圖層服務讀取(和constants.js是綁定的)
var readAndSetLayerServiceWidget = function () {
  let layerService = Constants.layerService;
  let layerServiceHtml = "";
  Object.keys(layerService).forEach(key => {
    let keyArray = layerService[key];
    layerServiceHtml += "";
    keyArray.forEach(value => {
      let txt = "<calcite-combobox-item class='mapServiceCombobox' value='";
      if (key == "normal") {
        txt += value["name"] + "' text-label='" + value["label"];
      } else {
        txt += key + "-" + value["name"] + "' text-label='" + value["label"];
      }
      if (value["status"] == 1) {
        txt += "'></calcite-combobox-item>";
      } else {
        txt += "' disabled></calcite-combobox-item>";
      }
      layerServiceHtml += txt;
    });
  });
  document.getElementById("layerChosenComboboxItem").innerHTML = layerServiceHtml;
}

// 建立查詢定位事件
var buildSearchLocationWidget = function(jsonObj){
	// city select option
	let cities = Object.keys(jsonObj);
	let html = "";
	for(let i=0; i<cities.length; i++){
		html += "<option";
		if(i == 0){
			html += " selected value='" +cities[i]+ "' >" + cities[i];
		}else{
			html += " value='" +cities[i]+ "' >" + cities[i];
		}
		html += "</option>";
	}
	document.getElementById("cityInput").innerHTML = html;
	// city select change event
	document.getElementById("cityInput").addEventListener("change", function(event){
		let city = document.getElementById("cityInput").value;
		let muiltiDistricts = jsonObj[city];
		let result = {};
		muiltiDistricts.forEach(function(item) {
			let site = item["site_id"]
			result[site] = result[site] ? result[site] + 1 : 1;
		});
		// let districts build select option
		let districts = Object.keys(result);
		let firstDistricts = districts[0];
		let html = "";
		for(let i=0; i<districts.length; i++){
			let district = districts[i].replaceAll(city, "");
			html += "<option";
			if(i == 0){
				html += " selected value='" +district+ "' >" + district;
			}else{
				html += " value='" +district+ "' >" + district;
			}
				html += "</option>";
		}
		document.getElementById("districtInput").innerHTML = html;
		// let roads build select option (find by first distrcit)
		let filters = muiltiDistricts.filter(item => item["site_id"] == firstDistricts);
		let rdhtml ="";
		for(let i=0; i<filters.length; i++){
			let road = filters[i]["road"];
			rdhtml += "<option";
			if(i == 0){
				rdhtml += " selected value='" +road+ "' >" + road;
			}else{
				rdhtml += " value='" +road+ "' >" + road;
			}
			rdhtml += "</option>";
		}
		document.getElementById("roadInput").innerHTML = rdhtml;		
	});
	// road select change event
	document.getElementById("districtInput").addEventListener("change", function(event){
		let district = document.getElementById("districtInput").value;
		let city = document.getElementById("cityInput").value;
		let muiltiDistricts = jsonObj[city];
		let filters = muiltiDistricts.filter(item => item["site_id"] == city+district);
		let html ="";
		for(let i=0; i<filters.length; i++){
			let road = filters[i]["road"];
			html += "<option";
			if(i == 0){
				html += " selected value='" +road+ "' >" + road;
			}else{
				html += " value='" +road+ "' >" + road;
			}
			html += "</option>";
		}
		document.getElementById("roadInput").innerHTML = html;
	});
}