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
    "esri/geometry/Point"
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
    watchUtils, Format, Point) {

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
        //template.title = "title";
        template.content = station_html;
        // close popup 
        // https://community.esri.com/t5/arcgis-api-for-javascript/what-is-the-function-to-close-a-popup-window/td-p/11379
        pointGraphic.popupTemplate = template;
        map.add(graphicsLayer, 0);
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

});