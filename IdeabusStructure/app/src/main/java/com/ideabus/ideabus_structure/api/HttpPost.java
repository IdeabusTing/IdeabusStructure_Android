package com.ideabus.ideabus_structure.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;

import com.ideabus.ideabus_structure.custom.Global;
import com.ideabus.ideabus_structure.listener.OnGetSystemListener;
import com.ideabus.ideabus_structure.listener.OnGoodsResultListener;
import com.ideabus.ideabuslibrary.BasePostAPI;

import junit.framework.Assert;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Map;

public class HttpPost extends BasePostAPI {

    private static final String TAG = HttpPost.class.getSimpleName();
    private static HttpPost instance;
    private Context context;

    private static final String POST_URL = "http://needs.ideabus.com.tw";
    public static final String API_SYS = "/api/sys";
    public static final String API_GET_SYSTEM = "/api/get_system";
    public static final String API_GET_GOODS_KIND = "/api/get_goods_kind";
    public static final String API_SPORTS_INFO = "/api/sports_info";
    public static final String API_USE_ITEM = "/api/use_item";
    public static final String API_HOMEPAGE = "/api/homepage";

    private static final String SHARED_PREF_API_KEY = "api_key";
    private static final String SHARED_PREF_SYS_CITY = "sys_city";
    private static final String SHARED_PREF_GOODS_KIND = "goods_kind";

    //Cloud API error code
    public static final int ENC_ERROR = 1;


    //api呼叫的認證碼
    private static String api_key;
    //基本資料「縣市、鄉鎮區、檢舉項目」的資料版本號
    private static int sys_city, new_sys_city;
    //商品類別的 資料版本號
    private static int goods_kind, new_goods_kind;

    private OnGoodsResultListener goodsListener;
    private OnGetSystemListener getSystemListener;

    private HttpPost(Context context){
        this.context = context;
        SharedPreferences sharedPref = Global.getSharedPref(context, Global.SHARED_PREF_NAME);
        //取得之前暫存的資料
        api_key = sharedPref.getString(SHARED_PREF_API_KEY, null);
        sys_city = sharedPref.getInt(SHARED_PREF_SYS_CITY, 0);
        goods_kind = sharedPref.getInt(SHARED_PREF_GOODS_KIND, 0);
    }

    public static HttpPost getInstance(Context context){
        if(instance == null)
            instance = new HttpPost(context);
        return instance;
    }

    /**
     * 系統資料-剛進App時使用
     * @param gps_location 經緯度 用逗號隔開 , e.g."24.141241,120.670363" , 如果沒有就傳null
     */
    private void postSys(@Nullable String gps_location){
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("enc", "Mjg4NzY0OA=="));
        params.add(new BasicNameValuePair("only_value", Build.SERIAL));
        params.add(new BasicNameValuePair("os", "android"));
        params.add(new BasicNameValuePair("model", Build.MODEL));
        params.add(new BasicNameValuePair("company", Build.MANUFACTURER));
        if(gps_location != null){
            boolean isLegality = gps_location.contains(",") && gps_location.split(",").length == 2;
            Assert.assertFalse("gps_location must contain ','  e.g. '24.141241,120.670363' ", isLegality);
            params.add(new BasicNameValuePair("gps", gps_location));
        }

        sendToServer(POST_URL, API_SYS, params);
    }

    /**
     * 取得基本「縣市」資料
     */
    private void postGetSystem(){
        if(sys_city < new_sys_city){
            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(SHARED_PREF_API_KEY, api_key));
            sendToServer(POST_URL, API_GET_SYSTEM, params);
        }else{
            onGetSystem(null);//API未完成
        }

    }

    private void postGetGoodsKind(int goodsKind){
        ArrayList<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair(SHARED_PREF_API_KEY, api_key));

        sendToServer(POST_URL, API_GET_SYSTEM, params);
    }

    public void postHomepageData(int pageIndex, int dataItems){
        ArrayList<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair(SHARED_PREF_API_KEY, api_key));
//		params.add(new BasicNameValuePair("account", "account"));
        params.add(new BasicNameValuePair("page", "" + pageIndex));
        params.add(new BasicNameValuePair("pagesize", "" + dataItems));

        sendToServer(POST_URL, API_HOMEPAGE, params);
    }

    @Override
    protected void apiPostSucceeded(String apiUrl, String result) {
        ArrayList<Map<String, String>> messageArray = null;

        switch (apiUrl){
            case API_SYS:   //sys
                messageArray = parseJson(result, "data_ver");
                break;
            case API_HOMEPAGE:	//homepage
                messageArray = parseJson(result, "data");
                break;
        }
        Global.printLog("d", TAG, "onPostSucceeded = " + messageArray);

        if(messageArray != null){
            Map<String, String> map = messageArray.get(0);
            int code = Global.stringToInt(map.get("code"));

            if(code == 100){
                //Post成功
                switch (apiUrl){
                    case API_SYS:   //sys
                        api_key = map.get(SHARED_PREF_API_KEY);
                        try{
                            new_sys_city = Integer.parseInt(map.get(SHARED_PREF_SYS_CITY));
                            new_goods_kind = Integer.parseInt(map.get(SHARED_PREF_GOODS_KIND));
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        SharedPreferences.Editor editor = Global.getSharePrefEditor(context, Global.SHARED_PREF_NAME);
                        editor.putString(SHARED_PREF_API_KEY, api_key)
                                .putInt(SHARED_PREF_SYS_CITY, new_sys_city)
                                .putInt(SHARED_PREF_GOODS_KIND, new_goods_kind)
                                .commit();
                        break;
                    case API_HOMEPAGE:	//homepage
                        ArrayList<Goods> goodsArray = new ArrayList<>();
                        for(Map<String, String> messageMap : messageArray){
                            Goods goods = new Goods();
                            goods.setImageUrl(messageMap.get(Goods.IMAGE_URL));
                            goods.setPage(messageMap.get(Goods.PAGE));
                            goods.setPageSize(messageMap.get(Goods.PAGE_SIZE));
                            goods.setId(messageMap.get(Goods.ID));
                            goods.setName(messageMap.get(Goods.NAME));
                            goods.setAccountName(messageMap.get(Goods.ACCOUNT_NAME));
                            goods.setKeyword(messageMap.get(Goods.KEYWORD));
                            goods.setAddress(messageMap.get(Goods.ADDRESS));
                            goods.setIntroduction(messageMap.get(Goods.INTRODUCTION));
                            goods.setThumbImageUrl(messageMap.get(Goods.THUMB_IMAGE));
                            goods.setHit(messageMap.get(Goods.HIT));
                            goods.setNeed(messageMap.get(Goods.NEED));
                            goods.setLike(messageMap.get(Goods.LIKE));
                            goods.setShowTimes(messageMap.get(Goods.SHOW_TIMES));
                            goods.setLikes(messageMap.get(Goods.LIKES));

                            goodsArray.add(goods);
                        }
                        onGoodsResult(goodsArray);
                        break;
                }
            }else{
                //Post失敗
                String info = map.get("info");
                onPostFailed(apiUrl, code, info);
            }
        }else{
            //Post失敗, 無任何資料
            onPostFailed(apiUrl, -1, "No result data!");
        }
    }

    @Override
    protected void apiPostFailed(String apiUrl, int errorCode, String errorMessage) {
        onPostFailed(apiUrl, errorCode, errorMessage);
    }


    public void setOnGetSystemListener(OnGetSystemListener l){
        getSystemListener = l;
    }

    public void setOnGoodsResultListener(OnGoodsResultListener l){
        goodsListener = l;
    }

    /**
     * 成功取得基本「縣市」資料
     * @param goodsArray
     */
    private void onGetSystem(ArrayList<Goods> goodsArray){
        if(getSystemListener != null)
            getSystemListener.onGetSystem(goodsArray);
    }

    /**
     * 成功取得商品資訊
     * @param goodsArray
     */
    private void onGoodsResult(ArrayList<Goods> goodsArray){
        if(goodsListener != null)
            goodsListener.onGoodsResult(goodsArray);
    }

    /**
     * 所有失敗的API Post
     * @param apiUrl
     * @param errorCode
     * @param errorMessage
     */
    private void onPostFailed(String apiUrl, int errorCode, String errorMessage){
        Global.printLog("e", TAG, "onPostFailed  apiUrl = " + apiUrl +
                "onPostFailed  errorCode = " + errorCode +
                " , errorMessage = " + errorMessage);
        switch (apiUrl){
            case API_GET_SYSTEM:
                if(getSystemListener != null)
                    getSystemListener.onPostFailed(apiUrl, errorCode, errorMessage);
                break;
            case API_HOMEPAGE:
                if(goodsListener != null)
                    goodsListener.onPostFailed(apiUrl, errorCode, errorMessage);
                break;
        }

    }

}
