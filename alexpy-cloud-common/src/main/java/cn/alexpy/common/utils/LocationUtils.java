package cn.alexpy.common.utils;

/**
 * @author alexpy
 */
public class LocationUtils {

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        if(lng1 == 0 || lat1 == 0 || lng2 == 0 || lat2 == 0){
            return 0;
        }
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        double earthRadius = 6378.137;
        s = s * earthRadius;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }

//    public static void main(String[] args) {
//        double s = getDistance(108.9423, 34.1845, 108.94220733642578, 34.18467712402344);
//        System.out.println(s);
//    }
}