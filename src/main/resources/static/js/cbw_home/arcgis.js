require([
    "esri/config",
    "esri/Map",
    "esri/views/MapView",
    "esri/layers/FeatureLayer",
    "esri/Graphic",
    "esri/layers/GraphicsLayer",
    "esri/symbols/PictureMarkerSymbol",
    "esri/widgets/Popup",
    "esri/PopupTemplate",
    "esri/geometry/Extent",
    "esri/views/ui/UI",
    "esri/widgets/CoordinateConversion",
    "esri/widgets/CoordinateConversion/support/Conversion",
    "esri/core/watchUtils",
    "esri/widgets/CoordinateConversion/support/Format",
    "esri/geometry/Point",
	"esri/layers/GeoJSONLayer",
	"esri/renderers/Renderer",
	"esri/core/reactiveUtils",
	"esri/rest/support/Query"
], function (
    esriConfig,
    Map, 
    MapView,
    FeatureLayer,
    Graphic, GraphicsLayer,
    PictureMarkerSymbol,
    Popup, PopupTemplate,
    Extent, UI,
    CoordinateConversion, Conversion,
    watchUtils, Format, Point, GeoJSONLayer,
	Renderer, reactive, Query) {

    var basemapStartPosition = 0;
    var basemapList = ["satellite", "topo-vector", "hybrid", "osm"];

    const map = new Map({
        basemap: "satellite"
    });

    const view = new MapView({
        container: "viewDiv",
        map: map,
        center: [119.641720, 23.947938],
        zoom: 7,
        collapseEnabled: false,
        popup: {
            spinnerEnabled: false,
            dockEnabled: true,
            dockOptions: {
                // Disables the dock button from the popup
                buttonEnabled: false,
                // Ignore the default sizes that trigger responsive docking
                breakpoint: true,
                position: "bottom-left"
            }
        },
        //featureMenuOpen: false,
        //featureNavigationEnabled: false
    });
    view.popup.actions = [];
    view.popup.visibleElements = {
        featureNavigation: false,
        closeButton: false
    };
    //view.constraints.minZoom = 7;
    view.constraints = {
        geometry: {
            type: "extent",
            xmin: 119.0,
            ymin: 21.0,
            xmax: 122.5,
            ymax: 26.5
        },
        minZoom: 7,
        rotationEnabled: false // Disables map rotation
    };

    view.ui.add(document.getElementsByClassName("attrClass")[0], "bottom-right");
	view.ui.add(document.getElementById("panelItem"), "bottom-left");
	
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
    view.ui.add(ccWidget, "top-right");

    // **************************************************
    const graphicsLayer = new GraphicsLayer();
    // **************************************************
    for (var i = 0; i < showingStation.length; i++) {
        var instance_point = showingStation[i];
        var instance_city = instance_point["city"];
        var instance_station = instance_point["station"];
        var instance_X = instance_point["coord_X"];
        var instance_Y = instance_point["coord_Y"];
        const point = { //Create a point
            type: "point",
            longitude: parseFloat(instance_X),
            latitude: parseFloat(instance_Y)
        };
        let symbol = {
            type: "picture-marker",  // autocasts as new PictureMarkerSymbol()
            url: "/csprscbw/image/google-map.png",
            width: "25px",
            height: "25px"
        };
        const pointGraphic = new Graphic({
            geometry: point,
            symbol: symbol
        });
        graphicsLayer.add(pointGraphic);
        // table-bordered table-sm table
        var station_html = "<br><table class='data'>" +
            "<tbody><tr><td scope='row'>縣市</td><td scope='col'>" + instance_city + "</td></tr>" +
            "<tr><td scope='row'>測站</td><td scope='col'>" + instance_station + "</td></tr>" +
            "<tr><td scope='row'>經度</td><td scope='col'>" + instance_X + "</td></tr>" +
            "<tr><td scope='row'>緯度</td><td scope='col'>" + instance_Y + "</td></tr>" +
            "</tbody></table>";
        var template = new PopupTemplate();
        template.title = "測站點位";
        template.content = station_html;
        // close popup 
        // https://community.esri.com/t5/arcgis-api-for-javascript/what-is-the-function-to-close-a-popup-window/td-p/11379
        pointGraphic.popupTemplate = template;
        map.add(graphicsLayer, 1);
    }

    document.getElementById("bmapId").addEventListener('click', function (event) {
        if (basemapStartPosition <= 2) {
            basemapStartPosition++;
        } else {
            basemapStartPosition = 0;
        }
        console.log(basemapList[basemapStartPosition]);
        map.basemap = basemapList[basemapStartPosition];
    });

	// **************************************************
	// 添加taiwanCounty geojson renderer
	let taiwanCountyRenderer = {
	  	type: "simple", 
	  	symbol: { 
			type: "simple-fill",
			color: "rgba(60, 179, 113, 0.2)",
            outline: {
              color: "white"
            }
		},  
	};
	var taiwanCountyHtml = "<table class='data'><tr><td scope='row'>縣市代碼</td><td scope='col'>{COUNTY_ID}</td></tr>";
	taiwanCountyHtml += "<tr><td scope='row'>縣市名稱</td><td scope='col'>{COUNTY}</td></tr>";
	taiwanCountyHtml += "</table>";
	var taiwanCountyTemplate = {
		title: "縣市",
		content: taiwanCountyHtml,
		outFields: ["*"]
	}
	// 添加taiwanCounty geojson圖層
	var taiwanCountyBlob = new Blob([taiwanCountyGeoJson], {
    	type: "application/json"
  	});
  	var taiwanCountyBlobUrl = URL.createObjectURL(taiwanCountyBlob);
  	var taiwanCountyLayer = new GeoJSONLayer({ 
		id: "taiwanCountyLayer",
		url: taiwanCountyBlobUrl,
		renderer: taiwanCountyRenderer,
		popupEnabled: true,
		visible: true,
		outfields: ["*"]
	});
	taiwanCountyLayer.popupTemplate = taiwanCountyTemplate;
	map.add(taiwanCountyLayer, 0);
	// **************************************************
	view.on("click", function(event){
		let query = taiwanCountyLayer.createQuery();
		query.geometry = view.toMap(event);  // the point location of the pointer
		query.distance = 2;
		query.units = "miles";
		query.spatialRelationship = "intersects";  // this is the default
		query.returnGeometry = true;
		query.outFields = [ "*" ];
		
		var options = {
			duration: 1000
		};
		taiwanCountyLayer.queryFeatures(query)
			.then(function(response){
				if(response.features[0]){
					view.goTo({
						geometry: response.features[0].geometry,
					    zoom: 9
					}, options);
				}
			});
	});
	
	document.getElementById("orgExtentId").addEventListener('click', function (event) {
		var options = {
			duration: 1000
		};
        view.goTo({
	        geometry: {
	            type: "extent",
	            xmin: 119.0, ymin: 21.0,
	            xmax: 122.5, ymax: 26.5
	        },
			center: [119.641720, 23.947938],
			zoom: 7,
	        minZoom: 7
	    }, options);
		view.popup.close();
    });
});