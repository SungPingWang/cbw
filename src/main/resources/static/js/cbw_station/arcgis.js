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

	view.ui.add(document.getElementById("panelItem"), "bottom-left");
	document.getElementById("bmapId").addEventListener('click', function (event) {
        if (basemapStartPosition <= 2) {
            basemapStartPosition++;
        } else {
            basemapStartPosition = 0;
        }
        console.log(basemapList[basemapStartPosition]);
        map.basemap = basemapList[basemapStartPosition];
    });

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
    
	var els = document.getElementsByClassName("stationDisplay");

	Array.prototype.forEach.call(els, function(el) {
		el.addEventListener('click', function (event) {
			var longitude = el.dataset.longitude;
			var latitude = el.dataset.latitude;
			var station = el.dataset.station;
			var city = el.dataset.city;
			const point = { //Create a point
	            type: "point",
	            longitude: parseFloat(longitude),
	            latitude: parseFloat(latitude)
	        };
			let symbol = {
	            type: "picture-marker",  // autocasts as new PictureMarkerSymbol()
	            url: "/csprscbw/image/google-map.png",
	            width: "30px",
	            height: "30px"
	        };
	        const pointGraphic = new Graphic({
	            geometry: point,
	            symbol: symbol
	        });
			graphicsLayer.add(pointGraphic);
			var station_html = "<table class='data'>" +
            "<tbody><tr><td scope='row'>經度</td><td scope='col'>" + longitude + "</td></tr>" +
            "<tr><td scope='row'>緯度</td><td scope='col'>" + latitude + "</td></tr>" +
            "</tbody></table>";
			var template = new PopupTemplate();
        	template.title = "測站點位";
        	template.content = station_html;
			pointGraphic.popupTemplate = template;
			map.add(graphicsLayer, 0);
			var options = {
				duration: 1000
			};
	        view.goTo({
				center: [parseFloat(longitude), parseFloat(latitude)],
				zoom: 15,
		        minZoom: 7
		    }, options);
			displayOnMap(station, city);
			arcgisInit();
		});
	});
	
});