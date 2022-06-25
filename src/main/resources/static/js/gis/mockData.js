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
        },
		"canQueryField": []
    },
    "cbwLyrMock": {
        "path": "/csprscbw/cbwStation.geojson",
		// https://developers.arcgis.com/javascript/latest/api-reference/esri-renderers-SimpleRenderer.html
		"renderer": {
		  	type: "simple",  // autocasts as new SimpleRenderer()
		  	symbol: {
		    	type: "simple-marker",  // autocasts as new SimpleMarkerSymbol()
		    	size: 6,
		    	color: [255, 255, 0, 0.8],
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
		},
		"canQueryField": []
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
        },
		"canQueryField": []
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
		},
		"canQueryField": []
	},
	"countyLyrMock": {
		"path": "/csprscbw/taiwanCounty.geojson",
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
			"title": "縣市名稱",
			"outFields": ["*"],
			"content": "<table class='data'><tr><td scope='row'>縣市代碼</td><td scope='col'>{COUNTY_ID}</td></tr><tr><td scope='row'>縣市名稱</td><td scope='col'>{COUNTY}</td></tr></table>"
		},
		"canQueryField": []
	},
	"fishPool2020LyrMock": {
		"path": "/csprscbw/fishPool2020.geojson",
		"renderer": {
			type: "simple", 
			symbol: { 
			type: "simple-fill",
				color: "rgba(153, 50, 204, 0.2)",
		  		outline: {
		 			color: "rgba(220, 20, 60, 0.8)"
				}
			}
		},
		"template": {
			"title": "魚塭編號：{魚塭編}",
			"outFields": ["*"],
			"content": "<table class='data'><tr><td scope='row'>魚種</td><td scope='col'>{GFISH}</td></tr><tr><td scope='row'>魚塭<br>面積</td><td scope='col'>{LAKE_AREA}</td></tr><tr><td scope='row'>縣市<br>鄉鎮</td><td scope='col'>{縣市別}{鄉鎮別}</td></tr><tr><td scope='row'>住址</td><td scope='col'>{ADDRESS_AL}</td></tr><tr><td scope='row'>姓名</td><td scope='col'>{CH_NAME}</td></tr><tr><td scope='row'>魚溫<br>狀態</td><td scope='col'>{魚種}</td></tr><tr><td scope='row'>登記證</td><td scope='col'>{登記證}</td></tr><tr><td scope='row'>水種</td><td scope='col'>{WATERTYPE}</td></tr><tr><td scope='row'>KEYSTR</td><td scope='col'>{KEYSTR}</td></tr></table>"
		},
		"labelClass": {
		  	labelExpressionInfo: { expression: "$feature.魚塭編" },
		  	symbol: {
		    	type: "text",  // autocasts as new TextSymbol()
		    	color: "black",
		    	haloSize: 1,
		    	haloColor: "white"
		  	}
		},
		"canQueryField": ["魚塭編", "縣市別", "LAKE_NO"]
	}
}