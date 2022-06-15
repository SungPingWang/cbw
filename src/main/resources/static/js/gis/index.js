require([
  "esri/Map",
  "esri/views/MapView",
  "esri/views/ui/UI",
  "esri/Graphic",
  "esri/geometry/Point",
  "esri/PopupTemplate",
  "esri/core/watchUtils",
  "esri/widgets/CoordinateConversion",
  "esri/widgets/CoordinateConversion/support/Conversion",
  "esri/widgets/CoordinateConversion/support/Format",
  "esri/layers/GeoJSONLayer",
  "esri/layers/GraphicsLayer",
  "esri/layers/TileLayer",
  "esri/layers/WMTSLayer",
  "esri/widgets/Measurement",
  "esri/widgets/Search",
  "esri/core/Accessor"

], function (
  Map, MapView, UI,
  Graphic, Point, PopupTemplate, watchUtils,
  CoordinateConversion, Conversion, Format,
  GeoJSONLayer, GraphicsLayer, TileLayer, WMTSLayer,
  Measurement, Search, Accessor
) {
  setblockUI();
  // ****************************************************************************
  // ****************************************************************************
  // 初始化
  // 給上傳的圖層們一些不同的顏色
  var uploadIdCnt = 0;
  // 8種色
  var uploadFeatureColorCycle = [[222, 49, 99, 0.5],
  [100, 149, 237, 0.5], [223, 255, 0, 0.5],
  [255, 191, 0, 0.5], [159, 226, 191, 0.5],
  [64, 224, 208, 0.5], [204, 204, 255, 0.5]]
  // 初始化值
  var basemapStartPosition = 0;
  var basemapList = ["satellite", "topo-vector", "hybrid", "osm", "dark-gray-vector"];
  var WMTS_MAP_ARR = ["正射影像圖(通用版)", "正射影像圖(通用版)", "電子地圖(通用版)", "混合地圖(通用版)",
    "段籍圖(通用版)", "1/5000圖幅框(通用版)", "OpenStreetMapLayer", "Google地圖街道", "Google衛星", "Google混和地圖"]
  // 正射影像圖(通用版)
  var mapObj = {
    "noWMap": ["noWMap", "正射影像圖(通用版)"],
    "WMap4": ["https://wmts.nlsc.gov.tw/wmts/正射影像圖", "正射影像圖(通用版)"],
    "WMap5": ["https://wmts.nlsc.gov.tw/wmts/電子地圖", "電子地圖(通用版)"],
    "WMap6": ["https://wmts.nlsc.gov.tw/wmts/混合地圖", "混合地圖(通用版)"],
    "WMap7": ["https://wmts.nlsc.gov.tw/wmts/段籍圖", "段籍圖(通用版)"],
    "WMap8": ["https://wmts.nlsc.gov.tw/wmts/5000圖幅框", "1/5000圖幅框(通用版)"]
    //"WMap9": ["osm", "OpenStreetMapLayer"],
    //"WMap10": ["streets", "Google地圖街道"],
    //"WMap11": ["satellite", "Google衛星"],
    //"WMap12": ["hybrid", "Google混和地圖"]
  }
  var tempTileLayer = "https://fa.idv.tw/mapweb/rest/services/FA_AIR_3857/MapServer";
  var WMTS_urlBase = "https://wmts.nlsc.gov.tw/wmts";
  var featureLayerArr = [];
  // ****************************************************************************
  // ****************************************************************************
  // Map View的兩個宣告
  var map = new Map({
    basemap: "satellite" // Basemap layer service
  });

  const view = new MapView({
    map: map,
    center: [121.301, 23.4499], // Longitude, latitude
    zoom: 9, // Zoom level
    container: "viewDiv" // Div element
  });
  // ****************************************************************************
  // ****************************************************************************
  // Map圖層的更元件加上
  // 1. 獲取轉換，僅獲取對XY的轉換，並將他丟右上角並隱藏，我需要他之後的轉換計算就好
  const ccWidget = new CoordinateConversion({
    view: view,
  });
  watchUtils.whenOnce(ccWidget, "formats.length", () => {
    ccWidget.formats = ccWidget.formats.filter(f => {
      return f.name === "xy" //|| f.name === "basemap"
    });
  });
  ccWidget.visibleElements = {
    settingsButton: false,
    captureButton: false,
    editButton: false,
    expandButton: false
  };
  // 2. 將一些我要的元件放到Map的位置上
  view.ui.add(document.getElementById("arcgisBtRight"), "bottom-right");
  view.ui.add(document.getElementById("panelItem"), "top-left");
  view.ui.add(document.getElementById("layerChosenComboboxItem"), "top-right");
  view.ui.add(document.getElementById("upload_info"), "bottom-left");
  view.ui.add(ccWidget, "top-right");
  // ****************************************************************************
  // ****************************************************************************
  // GraphicsLayer建立假資料
  // 1. 宣告一個新的GraphicsLayer
  const graphicsLayer = new GraphicsLayer();
  map.add(graphicsLayer);
  // 2. 宣告一個方形polygon
  const polygon = {
    type: "polygon",
    rings: [[121.2, 23.5], [121.3, 23.5], [121.3, 23.4], [121.2, 23.4], [121.2, 23.5]]
  };
  // 3. 方形polygon的symbology
  const simpleFillSymbol = {
    type: "simple-fill",
    color: [255, 255, 255, 1.0],
    style: "diagonal-cross",
    outline: {
      color: [255, 255, 255, 1.0],
      width: 3
    }
  };
  // 4. 方形polygon的popup，會按照下面的attributes進行對照
  const popupTemplate = {
    title: "{Name}",
    content: "{Description}"
  }
  // 5. 方形polygon的attribute
  const attributes = {
    Name: "Graphic",
    Description: "<table><thead><tr><th colspan='2'>The header</th></tr></thead><tbody><tr><td>The table body</td><td>with two columns</td></tr></tbody></table>"
  }
  // 6. 把方形polygon裝進一個Graphic中
  const polygonGraphic = new Graphic({
    geometry: polygon,
    symbol: simpleFillSymbol,
    attributes: attributes,
    popupTemplate: popupTemplate
  });
  // 7. Graphic再裝進graphicsLayer內
  graphicsLayer.add(polygonGraphic);
  // ****************************************************************************
  // ****************************************************************************
  // 點擊切換basemap按鈕的事件
  document.getElementById("bmapId").addEventListener('click', function (event) {
    updateLastStep("切換basemap");
    // 1. basemapStartPosition位置，不斷的循環
    if (basemapStartPosition <= 3) {
      basemapStartPosition++;
    } else {
      basemapStartPosition = 0;
    }
    map.basemap = basemapList[basemapStartPosition];
    // 2. 更改basemapStage，圖層的顯示文字，如果是該圖層的話就反藍並加粗
    var basemapStages = document.getElementsByClassName("basemapStage");
    for (let i = 0; i < basemapStages.length; i++) {
      if (i == basemapStartPosition) {
        basemapStages[i].style.color = "blue";
        basemapStages[i].style.fontWeight = "bold";
      } else {
        basemapStages[i].style.color = "black";
        basemapStages[i].style.fontWeight = "normal";
      }
    }
    // 3. 更改basemap的話我們要把另外一個的WMTS的底圖給關掉以防萬一
    for (let lyr of map.layers) {
      if (lyr.url == tempTileLayer) {
        map.layers.remove(lyr);
      }
      if (lyr.id == "webTileLayer") {
        map.layers.remove(lyr);
      }
      map.basemap = basemapList[basemapStartPosition];
      document.getElementById("dropdownMenu1").value = "noWMap";
      document.getElementById("noWMap").innerHTML = "選擇圖資服務";
    }
  });
  // ****************************************************************************
  // ****************************************************************************
  // 上傳圖資
  var uploadOpt = document.getElementById("uploadIt");
  uploadOpt.onclick = function () {
    // 利用id獲取輸入的文件們
    let files = document.getElementById("file").files;
    if (files.length == 0) {
      return; //do nothing if no file given yet
    } else {
      // 如果我有使用multiple file的話這樣比較保險，但因為這只使用一個文件所以也可以files[0]
      var file = files[files.length - 1];
      if (file.size < 1024 * 100) { // 如果文件大小超過100KB就抱錯，沒有就繼續
        var reader = new FileReader();
        reader.onload = function () {
          // 感覺不用寫error等等的錯誤在這上面，寫在其他地方
          // if (reader.readyState != 2 || reader.error){
          // 輸入reader.result，透過shp.js轉化為geojson
          shp(reader.result).then(function (geojson) {
            // 選個顏色吧
            let choice_color = uploadFeatureColorCycle[(uploadIdCnt) % uploadFeatureColorCycle.length];
            // 將geojson格式轉化為文字
            let geojsonStr = JSON.stringify(geojson);
            // 建立新的blob格式並帶入文字geojsonStr
            let blob1 = new Blob([geojsonStr], { type: "application/json" });
            // 將blob內的資訊輸出到隨機網址中(可以console網址來看)
            let url = URL.createObjectURL(blob1);
            let layer = new GeoJSONLayer({
              id: 'uploadLayer_' + uploadIdCnt, // id setting
              url: url,
              outFields: ["*"],
              renderer: {
                type: "simple",
                symbol: {
                  type: "simple-fill",
                  color: choice_color,
                  outline: { color: "white", width: 1 }
                }
              }
              // spatialReference: {wkid: 3857}
            });
            // add layer in map
            map.layers.push(layer);
            // 什麼東西都加入了之後，就可以把uploadIdCnt++了並且開始做監聽事件function
            // 代表如果layer啟動完畢後，就會實施
            layer.when(() => {
              // 新增對於上傳文件的相關欄位與按鈕
              // 新增upload-content內部的innerHTML
              document.getElementById('upload-content').innerHTML +=
                "<div class='row' style='margin-top: 5px'>" +
                "<div class='col-2'>" + file.name + "</div>" + // 顯示文件名稱
                "<div class='col-2'>" + (file.size / 1024).toFixed(2) + "KB</div>" + // 顯示文件容量
                "<div class='col-5'><input type='range' class='custom-range uploadLayerOpacity' min='1' max='10' step='1' id='customRange3_" + uploadIdCnt + "'></div>" +
                "<div class='col-3'><div class='btn-group' role='group' aria-label='Basic outlined example'>" + 
                "<button type='button' class='btn btn-outline-success btn-sm uploadLayerView' id='viewTo_" + uploadIdCnt + "'>移至</button>" + // goto按鍵
                "<button type='button' class='btn btn-outline-danger btn-sm uploadLayerDelete' id='uploadLayer_" + uploadIdCnt + "'>刪除</button>" + // 刪除按鍵
                "</div></div>" +
                //"<div class='col-2'><button class='badge badge-success uploadLayerView' id='viewTo_" + uploadIdCnt + "' style='color:black'>移至</button></div>" + // goto按鍵
                //"<div class='col-2'><button class='badge badge-danger uploadLayerDelete' id='uploadLayer_" + uploadIdCnt + "' style='color:black'>刪除</button></div>" + // 刪除按鍵
                "</div>";

              // 新增popupTemplate
              let arr = layer.fields;
              // 建立每個field name跟label的info稍後加入template
              let fieldInfos = arr.map(function (field) {
                return { "fieldName": field.name, "label": field.alias, "visible": true }
              });
              let template = new PopupTemplate({
                title: "上傳圖層",
                outFields: ["*"],
                content: [{ type: "fields", fieldInfos: fieldInfos }] // fieldInfos加入fieldInfos
              });
              // setting popup template
              layer.popupTemplate = template;
              // 新增當下layer的事件
              // 新增透明度事件
              let UploadOpacityOpt = document.getElementsByClassName("uploadLayerOpacity");
              opacityUploadShp(map, UploadOpacityOpt, uploadFeatureColorCycle);
              let UploadViewOpt = document.getElementsByClassName("uploadLayerView");
              // 新增定位事件
              moveToUploadShp(map, UploadViewOpt, view);
              // 新增刪除圖資事件
              deleteUploadShp(map, UploadViewOpt);
              uploadIdCnt++;
            });
          });
        }
        reader.readAsArrayBuffer(file);
        updateLastStep("上傳檔案圖資");
      } else {
        alert("上傳檔案不得大於100KB");
      }
    }
  }
  // ****************************************************************************
  // ****************************************************************************
  // 選擇添加的底圖(Raster)
  /*var FA_AIR_3857_Layer = new TileLayer({
    id: "tempTileLayer",
    url: tempTileLayer
  });*/
  // ****************************************************************************
  // ****************************************************************************
  // 選一個好用但是繁瑣的好了，PS這裡是地圖服務，不是切換baseMap底圖的(這個另外有其他的按鈕喔)
  // 選擇TileLayer地圖服務
  var WMapFunc = function (url) {
    let url_split;
    let LayerType;
    let layerss = map.layers;
    // 1 判斷是不是取消地圖服務
    if (url == "noWMap") {
      for (let lyr of layerss) {
	  	// 1.1 首先刪除所有的tileLayer與webTileLayer(.png為尾的)
        if (lyr.url == tempTileLayer || lyr.id == "webTileLayer") {
          map.layers.remove(lyr);
        }
		// 1.2 因為是取消圖層，所以要重新添加baseMap服務
        map.basemap = basemapList[basemapStartPosition];
      }
    // 2 如果是其他的地圖服務
	} else {
      url_split = url.split("/");
      LayerType = url_split[url_split.length - 1];
      // 如果以後要設定basemap一律去掉的話: map.basemap = null;
      // 2.1 刪除所有的tileLayer與webTileLayer(.png為尾的)
      for (let lyr of layerss) {
        if (lyr.url == tempTileLayer || lyr.id == "webTileLayer") {
          map.layers.remove(lyr);
        }
      }
      if (LayerType == "MapServer") { // 目前好像還沒有這個東西
        let tileLayer = new TileLayer({ url: url });
        map.add(tileLayer, 0);
        tempTileLayer = url;
      } else if (LayerType == "正射影像圖") {
        url = WMTS_urlBase;
        let wmtsLayer = new WMTSLayer({
          url: url, // 不管了寫死了
          serviceMode: "KVP",
          activeLayer: {
            id: "PHOTO2"
          },
          layerInfo: { identifier: "PHOTO2", format: "jpeg" }
        });
        map.add(wmtsLayer, 0);
        tempTileLayer = url;
      } else if (LayerType == "電子地圖") {
        url = WMTS_urlBase;
        var wmtsLayer = new WMTSLayer({
          url: url, // 不管了寫死了
          serviceMode: "KVP",
          activeLayer: {
            id: "EMAP"
          },
          layerInfo: { identifier: "EMAP", format: "jpeg" }
        });
        map.add(wmtsLayer, 0);
        tempTileLayer = url;
      } else if (LayerType == "混合地圖") {
        url = WMTS_urlBase;
        var wmtsLayer = new WMTSLayer({
          url: url, // 不管了寫死了
          serviceMode: "KVP",
          activeLayer: {
            id: "PHOTO_MIX"
          },
          layerInfo: { identifier: "PHOTO_MIX", format: "jpeg" }
        });
        map.add(wmtsLayer, 0);
        tempTileLayer = url;
      } else if (LayerType == "段籍圖") {
        url = WMTS_urlBase;
        var wmtsLayer = new WMTSLayer({
          url: url, // 不管了寫死了
          serviceMode: "KVP",
          activeLayer: {
            id: "LANDSECT"
          },
          layerInfo: { identifier: "LANDSECT", format: "png" }
        });
        map.add(wmtsLayer, 0);
        tempTileLayer = url;
      } else if (LayerType == "5000圖幅框") {
        url = WMTS_urlBase;
        var wmtsLayer = new WMTSLayer({
          url: url, // 不管了寫死了
          serviceMode: "KVP",
          activeLayer: {
            id: "MB5000"
          },
          layerInfo: { identifier: "MB5000", format: "png" }
        });
        map.add(wmtsLayer, 0);
        tempTileLayer = url;
      } else if (url.endsWith('.png')) { // 目前好像也還沒有這個
        var webTileLayer = new WebTileLayer({
          id: "webTileLayer",
          urlTemplate: url
        });
        map.add(webTileLayer, 0);
        tempTileLayer = url;
        view.zoom = 8;
      }
    }
  }
  // 建立圖資的點擊事件(其中要添加取消服務的功能)
  var dropdownMenu1 = document.getElementById("dropdownMenu1");
  dropdownMenu1.addEventListener("change", function () {
    var opt = dropdownMenu1.value;
    var optGet = mapObj[opt];
    WMapFunc(optGet[0]);
    document.getElementById("noWMap").innerHTML = "取消地圖服務";
    updateLastStep("切換地圖服務");
  })
  // ****************************************************************************
  // ****************************************************************************
  // 定位
  let locationButton = document.getElementById('location');
  function geoFindMe() {
    function success(position) {
      let latitude = position.coords.latitude;
      let longitude = position.coords.longitude;
      let greeting = "前往位置：" + longitude + ", " + latitude;
      alert(greeting);
      view.goTo({
        center: [longitude, latitude],
        zoom: 20
      })
      let point = { //Create a point
        type: "point",
        longitude: longitude,
        latitude: latitude
      };
      let simpleMarkerSymbol = {
        type: "simple-marker",
        color: [226, 119, 40],  // Orange
        outline: {
          color: [255, 255, 255], // White
          width: 1
        }
      };
      let graphicsLayer = new GraphicsLayer();
      let pointGraphic = new Graphic({
        geometry: point,
        symbol: simpleMarkerSymbol
      });
      graphicsLayer.add(pointGraphic);
      map.add(graphicsLayer);
      updateLastStep("定位");
    }
    function error() {
      //status.textContent = 'Unable to retrieve your location';
	  alert("無法獲取您的確切位置時發生錯誤，請重試。");
    }
    if (!navigator.geolocation) {
      alert("無法獲取您的確切位置，請檢察您是否有開啟位置設定。");
    } else {
      //status.textContent = 'Locating…';
      navigator.geolocation.getCurrentPosition(success, error);
    }
  }
  locationButton.addEventListener('click', geoFindMe);
  // ****************************************************************************
  // ****************************************************************************
  // 測量
  let distanceButton = document.getElementById('distance');
  let areaButton = document.getElementById('area');
  let clearButton = document.getElementById('clear');
  // Create new instance of the Measurement widget
  let measurement = new Measurement();
  // function of distance measurement 
  function distanceMeasurement() {
    let type = view.type;
    measurement.activeTool = type.toUpperCase() === "2D" ? "distance" : "direct-line";
    distanceButton.classList.add("active");
    view.ui.add(measurement, "top-right");
    measurement.view = view;
    areaButton.classList.remove("active");
    updateLastStep("距離測量");
  }
  // Call the appropriate AreaMeasurement2D or AreaMeasurement3D
  function areaMeasurement() {
    //const type = view.type;
    measurement.activeTool = "area";
    distanceButton.classList.remove("active");
    view.ui.add(measurement, "top-right");
    measurement.view = view;
    areaButton.classList.add("active");
    updateLastStep("面積測量");
  }
  // Clears all measurements
  function clearMeasurements() {
    distanceButton.classList.remove("active");
    areaButton.classList.remove("active");
    measurement.clear();
    updateLastStep("清除測量");
  }

  distanceButton.addEventListener("click", function () {
    distanceMeasurement();
  });
  areaButton.addEventListener("click", function () {
    areaMeasurement();
  });
  clearButton.addEventListener("click", function () {
    clearMeasurements();
  });
  // ****************************************************************************
  // ****************************************************************************
  // layer 圖資服務
  let layerChosenComboboxItem = document.getElementById("layerChosenComboboxItem");
  layerChosenComboboxItem.addEventListener("calciteComboboxChange", function (event) {
	let selectedItems = event.detail.selectedItems;
    let addFeatureLayer = "";
    let subFeatureLayer = "";
    let cpFeatureLayerArr = [];
    if(selectedItems.length > 0){
      selectedItems.forEach(ele => {
        cpFeatureLayerArr.push(ele.value);
        if(!featureLayerArr.includes(ele.value)){
          addFeatureLayer = ele.value;
        }
      })
      if(addFeatureLayer == ""){
        featureLayerArr.forEach(ele => {
          let isSub = cpFeatureLayerArr.includes(ele) ? true : false;
          if(!isSub){
            subFeatureLayer = ele;
          }
        })
      }
    }else{
      subFeatureLayer = featureLayerArr[0];
    }
    featureLayerArr = cpFeatureLayerArr;
	
	if(addFeatureLayer != ""){
		if(addFeatureLayer == "villageLyr" || addFeatureLayer == "cbwLyr"){
			let mockDataPath = MockLyrData[addFeatureLayer + "Mock"]["path"];
			let mockDataRender = MockLyrData[addFeatureLayer + "Mock"]["renderer"];
			let mockDataTemplate = MockLyrData[addFeatureLayer + "Mock"]["template"];
			let geoJson = getGeoJson(mockDataPath);
			// 添加taiwanCounty geojson圖層
			var geoJsonBlob = new Blob([geoJson], {
		    	type: "application/json"
		  	});
		  	var geoJsonBlobUrl = URL.createObjectURL(geoJsonBlob);
		  	var geoJsonLayer = new GeoJSONLayer({ 
				id: addFeatureLayer,
				url: geoJsonBlobUrl,
				renderer: mockDataRender,
				popupEnabled: true,
				visible: true,
				outfields: ["*"]
			});
			geoJsonLayer.popupTemplate = mockDataTemplate;
			map.add(geoJsonLayer, 0);
			let html = "<div id='" + addFeatureLayer + "-ctl' class='row' style='height: 30px;'>";
	     	html += "<div class='col-5'><p class='float-start'>";
	      	html += "。" + addFeatureLayer + "</p></div>";
	      	html += "<div class='col-5'><input id='" + addFeatureLayer + "-opScroll' type='range' min='1' max='10' step='1' style='width:100px;' class='scrollLayerOpacity'></div>";
	      	html += "<div class='col-2'><p class='float-end'>";
	      	html += "<button id='" + addFeatureLayer + "-mvBtn' type='button' class='btn btn-success btn-sm moveLayerBtn'>移動</button>";
	      	html += "</p></div></div>";  
	      	document.getElementById("layerShow").innerHTML += html;
		} else {
	      	let tmpGraphicsLayer = new GraphicsLayer({ id: addFeatureLayer });
	      	let getMockData = MockLyrData[addFeatureLayer + "Mock"];
	      	let tempGraphic = new Graphic({
	        	geometry: getMockData.polygon,
	        	symbol: getMockData.symbol,
	        	attributes: getMockData.attributes,
	        	popupTemplate: getMockData.popupTemplate
	      	});
	      	tmpGraphicsLayer.add(tempGraphic);
	      	map.add(tmpGraphicsLayer);
	      	let html = "<div id='" + addFeatureLayer + "-ctl' class='row' style='height: 30px;'>";
	      	html += "<div class='col-5'><p class='float-start'>";
	      	html += "。" + addFeatureLayer + "</p></div>";
	      	html += "<div class='col-5'><input id='" + addFeatureLayer + "-opScroll' type='range' min='1' max='10' step='1' style='width:100px;' class='scrollLayerOpacity'></div>";
	      	html += "<div class='col-2'><p class='float-end'>";
	      	html += "<button id='" + addFeatureLayer + "-mvBtn' type='button' class='btn btn-success btn-sm moveLayerBtn'>移動</button>";
	      	html += "</p></div></div>";  
	      	document.getElementById("layerShow").innerHTML += html;
    	}
	}
	
    if(subFeatureLayer != ""){
	  setblockUI();
      for (let lyr of map.layers){
        if (lyr.id == subFeatureLayer) {
          map.layers.remove(lyr);
          var elem = document.getElementById(subFeatureLayer + "-ctl");
          elem.parentNode.removeChild(elem);
        }
      }
    }
    // 更新所有
    let moveLayerBtns = document.getElementsByClassName("moveLayerBtn");
    for (let i = 0; i < moveLayerBtns.length; i++) {
      let layerBtn = moveLayerBtns[i];
      let layerId = layerBtn.id; 
      layerBtn.addEventListener("click", function(){
        for (let lyr of map.layers){
          if(layerId == lyr.id + "-mvBtn"){
			let tmpExtent;
			if(lyr.graphics != null){
				tmpExtent = lyr.graphics.items[0].geometry;
			}else{
				tmpExtent = lyr.fullExtent;
			}
			view.goTo({
	        	geometry: tmpExtent
	        });
          }
        }
      });
    }
    let scrollLayerOpacity = document.getElementsByClassName("scrollLayerOpacity");
    for (let i = 0; i < scrollLayerOpacity.length; i++) {
      let layerSlider = scrollLayerOpacity[i];
      let layerId = layerSlider.id; 
      layerSlider.addEventListener("change", function(){
        for (let lyr of map.layers){
          if(layerId == lyr.id + "-opScroll"){ 
            let scale = layerSlider.value;
            lyr.opacity = scale * 0.1;
          }
        }
      });
    }
    updateLastStep("Layer設定")
	setUnblockUI();
  })
  // ****************************************************************************
  // ****************************************************************************
  // 圖層按鈕顯示
  document.getElementById("layers").addEventListener("click", function(){
    let layerOpenState = document.getElementById("layerShow");
    if(layerOpenState.style.display == "none"){
      layerOpenState.style.display = "inline-block";
    }else{
      layerOpenState.style.display = "none";
    }
  })
  // ****************************************************************************
  // ****************************************************************************
  // 定位搜尋功能
  let searchWidget = new Search({
	label: "查詢結果",
	returnGeometry: true,
	view: view
  });
  view.ui.add(searchWidget, {
	position: "top-right"
  });
  searchWidget.visible = false; // 關閉不要被看到
  // 點擊定位搜尋會開啟dialog
  document.getElementById("srhLocationDialog").addEventListener("click", function(){
	var title = "查詢位置";
	$("#searchLocationDialog").dialog("option", "title", title);
	$("#searchLocationDialog").dialog("open");
  });
  // 點擊dialog中的查詢按鈕就可進行查詢search功能
  document.getElementById("searchLocation").addEventListener("click", function(){
	let district = document.getElementById("districtInput").value;
	let city = document.getElementById("cityInput").value;
	let road = document.getElementById("roadInput").value;
	let detail = document.getElementById("detailInput").value;
	$("#searchLocationDialog").dialog("close");
	searchWidget.search(city+district+road+detail);
	searchWidget.on("search-complete", function(searchcomplete){
	  if(searchcomplete.numResults == 0){
		alert("查無此相關條件：", searchcomplete.searchTerm);
	  }
	});
  })  
  // ****************************************************************************
  // ****************************************************************************
});