package com.ideabus.ideabuslibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Ting on 15/11/13.
 */
public class BaseGlobal {

    private static boolean isPrintLog = true;
    public static float density = 0;
    public static int screenWidth = 0;
    public static int screenHeight = 0;
    public static float screenScale = 0;
    public static boolean isTabletScaleSize;

    /**
     * String轉成int, 如果失敗則回傳0
     * @param str string
     * @return
     */
    public static int stringToInt(String str) {
        int i = 0;
        try{
            i = Integer.parseInt(str);
        }catch(Exception e){
            e.printStackTrace();
        }
        return i;
    }

    public static SharedPreferences getSharedPref(Context context, String sharedPrefName){
        return context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getSharePrefEditor(Context context, String sharedPrefName){
        return context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).edit();
    }

    /**
     * 判斷是否有網路環境
     * @param context c
     * @return true=有網路
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()){
            return false;
        }else{
            if(!activeNetworkInfo.isAvailable()){
                return false;
            }else{
                return true;
            }
        }
    }

    /**
     * i前面捕length個fillStr
     * @param i int
     * @param length 要補的數量
     * @param fillStr 要補的String
     * @return 補完fillStr的String
     */
    public static String getFillString(int i, int length, String fillStr){
        StringBuilder str = new StringBuilder(String.valueOf(i));
        while (str.length() < length){
            str.insert(0, fillStr);
        }
        return str.toString();
    }

    /**
     * 設定value四捨五入到scale位數
     * @param value float
     * @param scale 四捨五入到小數點後幾數
     * @return 四捨五入後的String
     */
    public static String getScaleToString(float value, int scale){
        return getScaleDecimal(value, scale).toString();
    }

    /**
     * 設定value四捨五入到scale位數
     * @param value float
     * @param scale 四捨五入到小數點後幾數
     * @return 四捨五入後的String
     */
    public static float getScaleToFloat(float value, int scale){
        return getScaleDecimal(value, scale).floatValue();
    }

    private static BigDecimal getScaleDecimal(float value, int scale){
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * value除以divideValue四捨五入後取到小數點後scale位數
     * @param value 除數
     * @param divideValue 被除數
     * @param scale 四捨五入到小數點後幾數
     * @return 除後四捨五入後的float
     */
    public static float divide(float value, float divideValue, int scale){
        BigDecimal bigDecimal = new BigDecimal(value).divide(new BigDecimal(divideValue), scale, RoundingMode.HALF_UP);
        return bigDecimal.floatValue();
    }

    /**
     * 數值前面補0
     * @param num 要補0的參數
     * @param place 要補幾個0
     * @return 補完0後的String
     */
    public static String getStringPlace(String num, int place){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false); //取消逗號
        nf.setMinimumIntegerDigits(place);
        return nf.format(num);
    }

    /**
     * 數值前面補0
     * @param num 要補0的參數
     * @param place 要補幾個0
     * @return 補完0後的String
     */
    public static String getStringPlace(int num, int place){
        return getStringPlace(String.valueOf(num), place);
    }

    /**
     * 取得系統日期
     * @return 2014/12/12 12:12:12
     */
    public static String getDateTime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        String date = year + "/" + getStringPlace(month, 2) + "/" + getStringPlace(day, 2) + " " +
                getStringPlace(hour, 2) + ":" + getStringPlace(minute, 2) + ":" + getStringPlace(second, 2);

        return date;
    }

    public static float dpToPixel(float dp){
        float px = dp * density;
        return px;
    }

    public static float pixelToDp(float pixel){
        float dp = pixel / density;
        return dp;
    }

    /**
     * 是否透過printLog() Print Log
     * @param b
     */
    public static void setIsPrintLog(boolean b){
        isPrintLog = b;
    }

    public static void printLog(String type, String tag, String msg) {
        byte priority = 0;
        if(type.equals("i")) {
            priority = 4;
        } else if(type.equals("d")) {
            priority = 3;
        } else if(type.equals("e")) {
            priority = 6;
        }
        if(isPrintLog) {
            Log.println(priority, tag, msg);
        }
    }

    /**
     * 設定APP語言,設定完需重啟Activity
     * @param resources
     * @param locale
     */
    public static void setAppLocale(Resources resources, Locale locale) {
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        config.setLocale(locale);
        resources.updateConfiguration(config, dm);
    }

    /**
     * 平板版面會因為4:3 or 16:9不同而會有空隙太大的問題,所以要依比例放大某些Size
     * @param size 要放大的Size
     * @param scale 預設可設為1, 放大倍率可以此調整
     * @return 放大後的Size
     */
    public static int getTabletScalSize(int size, float scale){
        if(isTabletScaleSize){
            float scaleMultiple = 2.9f - screenScale;
            return (int) (size * scale * scaleMultiple);
        }else{
            return size;
        }
    }

    /**
     * 載入R file drawable
     * @param resources
     * @param rid
     * @param inSampleSize
     * @return
     */
    public static Bitmap decodeBitmapFromRid(Resources resources, int rid, int inSampleSize){
        if(rid == 0)
            return null;
        BitmapFactory.Options options = getBitmapOptions(inSampleSize);
        InputStream is = resources.openRawResource(rid);
        //new BitmapDrawable(resources, BitmapFactory.decodeStream(is, null, options));
        return BitmapFactory.decodeStream(is, null, options);
    }

    public static Bitmap decodeBitmapFromByteArray(byte[] byteArray, int inSampleSize){
        if(byteArray == null)
            return null;
        BitmapFactory.Options options = getBitmapOptions(inSampleSize);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }

    private static BitmapFactory.Options getBitmapOptions(int inSampleSize){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    /**
     * byte[] 轉 Hex String
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * byte[] 轉 十進制 String
     * @param src
     * @return
     */
    public static String bytesToString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i];
            String hv = Integer.toString(v);
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * byte[] 轉 ACSII
     * @param src
     * @return
     */
    public static String bytesToAscii(byte[] src){
        String asciiStr = "";
        try {
            asciiStr = new String(src, "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return asciiStr;
    }

    public static byte[] hexToByteArray(String hexString) {
        char[] hex = hexString.toCharArray();
        // 轉rawData長度減半
        int length = hex.length / 2;
        byte[] rawData = new byte[length];
        for (int i = 0; i < length; i++) {
            // 先將hex資料轉10進位數值
            int high = Character.digit(hex[i * 2], 16);
            int low = Character.digit(hex[i * 2 + 1], 16);
            // 將第一個值的二進位值左平移4位,ex: 00001000 => 10000000 (8=>128)
            // 然後與第二個值的二進位值作聯集ex: 10000000 | 00001100 => 10001100 (137)
            int value = (high << 4) | low;
            // 與FFFFFFFF作補集
            if (value > 127)
                value -= 256;
            // 最後轉回byte就OK
            rawData[i] = (byte) value;
        }
        return rawData;
    }

    /**
     * 十進制轉十六進制
     * @param decimal
     * @return
     */
    public static String decimalToHex(int decimal){
        StringBuilder hexStr = new StringBuilder(Integer.toHexString(decimal));
        while(hexStr.length() < 2)
            hexStr.insert(0, "0");
        return hexStr.toString().toUpperCase();
    }

    /**
     * 十六進制轉二進制
     * @param hex
     * @param bitCount
     * @return
     */
    public static String hexToBinary(String hex, int bitCount) {
        StringBuilder binaryStr = new StringBuilder(new BigInteger(hex, 16).toString(2));
        while(binaryStr.length() < bitCount)
            binaryStr.insert(0, "0");
        return binaryStr.toString();
    }

    /**
     * 二進制轉十六進制
     * @param binary
     * @return
     */
    public static String binaryToHex(String binary) {
        StringBuilder hexStr = new StringBuilder(new BigInteger(binary, 2).toString(16));
        while(hexStr.length() < 2)
            hexStr.insert(0, "0");
        return hexStr.toString().toUpperCase();
    }

    /**
     * 二進制轉十進制
     * @param binary
     * @return
     */
    public static int binaryToDecimal(String binary) {
        return Integer.parseInt(binary, 2);
    }

    /**
     * 十進制轉二進制
     * @param decimal
     * @param bitCount
     * @return
     */
    public static String decimalToBinary(int decimal, int bitCount) {
        StringBuilder binaryStr = new StringBuilder(Integer.toBinaryString(decimal));
        while(binaryStr.length() < bitCount)
            binaryStr.insert(0, "0");
        return binaryStr.toString();
    }

}
