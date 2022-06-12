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
