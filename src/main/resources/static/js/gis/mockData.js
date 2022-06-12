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
        "polygon": {
            type: "polygon",
            rings: [[122.2, 24.5], [123.3, 24.5], [123.3, 24.4], [122.2, 24.4], [122.2, 24.5]]
        },
        "symbol": {
            type: "simple-fill",
            color: [0, 255, 255, 0.8],
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
            Name: "天氣資料範圍",
            Description: "<table><thead><tr><th colspan='2'>The header</th></tr></thead><tbody><tr><td>The table body</td><td>with two columns</td></tr></tbody></table>"
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
		"path": "/csprscbw/taiwanVillage.geojson"
	}
}