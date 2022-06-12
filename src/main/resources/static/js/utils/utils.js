var setblockUI = function() {
	$.blockUI({
		fadeIn: 0 ,
		message: '<img src="/csprscbw/image/loading.gif" width="100px" height="100px" />',
	    css: { border: 'none', color: '#fff', backgroundColor: 'rgba(0, 0, 0, 0)'   }
	});
}
var setUnblockUI = function() {
	// 關閉等待畫面
	setTimeout(function () {
		$.unblockUI();
	}, 2000);
}
