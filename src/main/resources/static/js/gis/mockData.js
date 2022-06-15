var MockLyrData = {
    "roadKillLyrMock": {
        "polygon": {
            type: "polygon",
            rings: [[119.2, 22.5], [121.3, 22.5], [121.3, 22.4], [119.2, 22.4], [119.2, 22.5]]
        },
        "symbol": {
            type: "simple-fill",
            color: [124, 252, 0, 0.8],
            style: "diagonal-cross",
            outline: {
                color: [255, 255, 0, 0.5],
                width: 5
            }
        },
        "popupTemplate": {
            title: "{Name}",
            content: "{Description}"
        },
        "attributes": {
            Name: "路殺資料範圍",
            Description: "<table><thead><tr><th colspan='2'>The header</th></tr></thead><tbody><tr><td>The table body</td><td>with two columns</td></tr></tbody></table>"
        }
    },
    "cbwLyrMock": {
        "path": "/csprscbw/cbwStation.geojson",
		// https://developers.arcgis.com/javascript/latest/api-reference/esri-renderers-SimpleRenderer.html
		"renderer": {
		  	type: "simple",  // autocasts as new SimpleRenderer()
		  	symbol: {
		    	type: "simple-marker",  // autocasts as new SimpleMarkerSymbol()
		    	size: 6,
		    	color: "black",
		    	outline: {  // autocasts as new SimpleLineSymbol()
		      		width: 0.5,
		      		color: "white"
		   	 	}
		  	}
		},
		"template": {
			"title": "{city}{name}",
			"outFields": ["*"],
			"content": "<table class='data'><tr><td scope='row'>經度</td><td scope='col'>{longitude}</td></tr><tr><td scope='row'>緯度</td><td scope='col'>{latitude}</td></tr></table>"
		}
    },
    "roadSectionLyrMock": {
        "polygon": {
            type: "polygon",
            rings: [[120.2, 24.5], [121.0, 24.5], [121.0, 23.9], [120.2, 23.9], [120.2, 24.5]]
        },
        "symbol": {
            type: "simple-fill",
            color: [255, 0, 255, 0.8],
            style: "diagonal-cross",
            outline: {
                color: [255, 255, 0, 0.5],
                width: 5
            }
        },
        "popupTemplate": {
            title: "{Name}",
            content: "{Description}"
        },
        "attributes": {
            Name: "路段資料範圍",
            Description: "<table><thead><tr><th colspan='2'>The header</th></tr></thead><tbody><tr><td>The table body</td><td>with two columns</td></tr></tbody></table>"
        }
    },
	"villageLyrMock": {
		"path": "/csprscbw/taiwanVillage.geojson",
		"renderer": {
			type: "simple", 
			symbol: { 
			type: "simple-fill",
				color: "rgba(60, 179, 113, 0.2)",
		  		outline: {
		 			color: "white"
				}
			}
		},
		"template": {
			"title": "{COUNTY}{TOWN}",
			"outFields": ["*"],
			"content": "<table class='data'><tr><td scope='row'>村里名稱</td><td scope='col'>{VILLAGE}</td></tr></table>"
		}
	}
}