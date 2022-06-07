var arcgisInit = function() {
	/**/
	var orgCoordinateDisplayNode = document.getElementsByClassName('esri-coordinate-conversion__display')[0];
	orgCoordinateDisplayNode.addEventListener('DOMNodeInserted',
		function() {
			var pntSplit = orgCoordinateDisplayNode.innerHTML.split(",")
			var html = "WGS84: " + orgCoordinateDisplayNode.innerHTML + "<br>";
			var twd97Array = lonlat2twd97(
				pntSplit[0].trim().substring(0, pntSplit[0].trim().length - 2),
				pntSplit[1].trim().substring(0, pntSplit[1].trim().length - 2));
			html += "twd97: " + twd97Array;
			document.getElementById("attrId").innerHTML = html;
		}, false);
	document.getElementsByClassName("esri-attribution__powered-by")[0].innerHTML = "&copy;中華民國航空測量及遙感探測學會 2021-2022";
	
	if(document.getElementsByClassName("esri-icon-plus").length != 0){
		// 修改zoom in out圖片
		var plusIcon = document.getElementsByClassName("esri-icon-plus")[0];
		plusIcon.classList.add("esri-icon-zoom-in-magnifying-glass");
		plusIcon.classList.remove("esri-icon-plus");
		var minusIcon = document.getElementsByClassName("esri-icon-minus")[0];
		minusIcon.classList.add("esri-icon-zoom-out-magnifying-glass");
		minusIcon.classList.remove("esri-icon-minus");
	}

}