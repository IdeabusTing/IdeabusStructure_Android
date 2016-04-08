package com.ideabus.ideabus_structure.custom;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import java.util.List;

/**
 * Created by Ting on 2015/7/14.
 */
public class GpsLocation {

    private static final String TAG = GpsLocation.class.getSimpleName();
    private static GpsLocation instance;
    private Context context;

    private LocationManager locationManager = null;
    private boolean isRegisterGPS = false;

    public interface OnLocationChangedListener {
        void onLocationChanged(Location location);
    }
    public OnLocationChangedListener listener;
    public void setOnLocationChangedListener(OnLocationChangedListener l){
        listener = l;
    }

    public GpsLocation(Context context){
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    private void locationChanged(Location location){
        if(listener != null)
            listener.onLocationChanged(location);
    }

    public static GpsLocation getInstance(Context context){
        if(instance == null)
            instance = new GpsLocation(context);
        return instance;
    }

    /**
     * 取得GPS的類型
     * @return LocationManager.GPS_PROVIDER or LocationManager.NETWORK_PROVIDER or null
     */
    @Nullable
    private String getProvider(){
//        Criteria criteria = new Criteria();     // 构建位置查询条件
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);// 查询精度：中
//        criteria.setAltitudeRequired(false);     // 是否查询海拨：否
//        criteria.setBearingRequired(false);      // 是否查询方位角 : 否
//        criteria.setCostAllowed(false);     // 是否允许付费：否
//        criteria.setPowerRequirement(Criteria.POWER_HIGH);       // 电量要求：高
//
//        // 返回最合适的符合条件的 provider ，第 2 个参数为 true 说明 , 如果只有一个 provider 是有效的 , 则返回当前 provider
//        String provider = locationManager.getBestProvider(criteria, true);
//        FunctionUtils.myLog("e", "provider-----------------" + provider);
//        return provider;
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(gps)
            return LocationManager.GPS_PROVIDER;
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(network)
            return LocationManager.NETWORK_PROVIDER;
        else
            return null;
    }

    /**
     * 開啟GPS
     */
    public void goEnableGps(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 判斷GPS是否開啟
     * @return true=已開啟
     */
    public boolean isEnableGps(){
        return getProvider() != null;
    }

    /**
     * Register GPS listener
     */
    public Location registerGpsListener() {
        if(isRegisterGPS){
            Global.printLog("e", TAG, "Already register gps listener!");
            return null;
        }

        Location location = null;
        String provider = getProvider();
        Global.printLog("d", TAG, "registerGpsListener  provider = " + provider);

        if(provider != null){
            isRegisterGPS = true;
            locationManager.requestLocationUpdates(provider, 1000, 1, mLocationListener);

            try{
                location = locationManager.getLastKnownLocation(provider);
                if(location != null){
                    Global.printLog("d", TAG, "getLastKnownLocation Lat = " + location.getLatitude() +
                            " , Long = " + location.getLongitude());
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        return location;
    }

    public void unregisterGpsListener() {
        isRegisterGPS = false;
        try {
            LocationManager locationManager
                    = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            locationManager.removeUpdates(mLocationListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            locationChanged(location);
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }
        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
            locationChanged(null);
        }
    };


    private void test(Context context){
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String operator = manager.getNetworkOperator();
        /**通过operator获取 MCC 和MNC */
        int mcc = Integer.parseInt(operator.substring(0, 3));
        int mnc = Integer.parseInt(operator.substring(3));
        Global.printLog("d", TAG, "TelephonyManager---- 國家代碼 = " + mcc);
        Global.printLog("d", TAG, "TelephonyManager---- 網路號碼 = " + mnc);

        GsmCellLocation location = (GsmCellLocation) manager.getCellLocation();

        /**通过GsmCellLocation获取中国移动和联通 LAC 和cellID */
        int lac = location.getLac();
        int cellid = location.getCid();
        Global.printLog("d", TAG, "GsmCellLocation---- lac = " + lac);
        Global.printLog("d", TAG, "GsmCellLocation---- cellid = " + cellid);

        int strength = 0;
        /**通过getNeighboringCellInfo获取BSSS */
        List<NeighboringCellInfo> infoLists = manager.getNeighboringCellInfo();
        System.out.println("infoLists:" + infoLists + "     size:" + infoLists.size());
        for (NeighboringCellInfo info : infoLists) {
            strength+=(-133+2*info.getRssi());// 获取邻区基站信号强度
            //info.getLac();// 取出当前邻区的LAC
            //info.getCid();// 取出当前邻区的CID
            Global.printLog("d", TAG, "NeighboringCellInfo---- rssi = "+info.getRssi() + " , strength = "+strength);
            System.out.println();
        }
    }

}
