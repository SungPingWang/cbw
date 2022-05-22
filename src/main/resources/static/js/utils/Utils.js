var roundDecimal = function (val, precision) {
  return Math.round(Math.round(val * Math.pow(10, (precision || 0) + 1)) / 10) / Math.pow(10, (precision || 0));
}

var lonlat2twd97 = function(lon, lat) {
    var a = 6378137.0;
    var b = 6356752.3142451;
    var lon0 = 121 * Math.PI / 180;
    var k0 = 0.9999;
    var dx = 250000;
    var dy = 0;
    var e = 1 - Math.pow(b, 2) / Math.pow(a, 2);
    var e2 = (1 - Math.pow(b, 2) / Math.pow(a, 2)) / (Math.pow(b, 2) / Math.pow(a, 2));


    var TWD97 = "";

    lon = (lon - Math.floor((lon + 180) / 360) * 360) * Math.PI / 180;
    lat = lat * Math.PI / 180;

    var V = a / Math.sqrt(1 - e * Math.pow(Math.sin(lat), 2));
    var T = Math.pow(Math.tan(lat), 2);
    var C = e2 * Math.pow(Math.cos(lat), 2);
    var A = Math.cos(lat) * (lon - lon0);
    var M = a *((1.0 - e / 4.0 - 3.0 * Math.pow(e, 2) / 64.0 - 5.0 * Math.pow(e, 3) / 256.0) * lat -
          (3.0 * e / 8.0 + 3.0 * Math.pow(e, 2) / 32.0 + 45.0 * Math.pow(e, 3) / 1024.0) *
          Math.sin(2.0 * lat) + (15.0 * Math.pow(e, 2) / 256.0 + 45.0 * Math.pow(e, 3) / 1024.0) *
          Math.sin(4.0 * lat) - (35.0 * Math.pow(e, 3) / 3072.0) * Math.sin(6.0 * lat));
    // x
    var x = dx + k0 * V * (A + (1 - T + C) * Math.pow(A, 3) / 6 + (5 - 18 * T + Math.pow(T, 2) + 72 * C - 58 * e2) * Math.pow(A, 5) / 120);
    // y
    var y = dy + k0 * (M + V * Math.tan(lat) * (Math.pow(A, 2) / 2 + (5 - T + 9 * C + 4 * Math.pow(C, 2)) * Math.pow(A, 4) / 24 + ( 61 - 58 * T + Math.pow(T, 2) + 600 * C - 330 * e2) * Math.pow(A, 6) / 720));
    
    TWD97 = roundDecimal(x, 5).toString() + ", " + roundDecimal(y, 5).toString();
    return TWD97;
}

