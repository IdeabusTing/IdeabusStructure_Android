package com.ideabus.ideabuslibrary;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ERROR_CODE_NO_INTERNET = 1
 * ERROR_CODE_SERVER_ERROR = 2
 * ERROR_CODE_UNKNOWN_ERROR = 3
 */
public abstract class BasePostAPI {

	private static final String TAG = BasePostAPI.class.getSimpleName();

	private static final int ERROR_CODE_NO_INTERNET = 1;
	private static final int ERROR_CODE_SERVER_ERROR = 2;
	private static final int ERROR_CODE_UNKNOWN_ERROR = 3;

	/**
	 * Post成功
	 * @param apiUrl {@link BasePostAPI#sendToServer}的apiUrl
	 * @param result Json data
	 */
	protected abstract void apiPostSucceeded(String apiUrl, String result);
	/**
	 * Post失敗
	 * @param apiUrl {@link BasePostAPI#sendToServer}的apiUrl
	 * @param errorCode
	 *          {@link BasePostAPI#ERROR_CODE_NO_INTERNET}, {@link BasePostAPI#ERROR_CODE_SERVER_ERROR}, {@link BasePostAPI#ERROR_CODE_UNKNOWN_ERROR}<br>
	 *
	 * @param errorMessage 錯誤訊息
	 */
	protected abstract void apiPostFailed(String apiUrl, int errorCode, String errorMessage);

	/**
	 * 發送資料至Server
	 * @param url Post完整網址
	 * @param apiUrl API Url
	 * @param params Post參數
	 */
	protected void sendToServer(String url, String apiUrl, ArrayList<NameValuePair> params){
		final String api = apiUrl;
		final String postUrl = url + api;
		final ArrayList<NameValuePair> postParams = params;

		new Thread(){
			public void run(){
				HttpURLConnection urlConnection = null;
				OutputStream outputStream = null;
				ByteArrayOutputStream byteArrayOutputStream = null;
				String result;
				try {
					byte[] data = getRequestData(postParams, "UTF-8").toString().getBytes();
					urlConnection = (HttpURLConnection) new URL(postUrl).openConnection();
					urlConnection.setRequestMethod("POST");
					urlConnection.setConnectTimeout(10000);
					urlConnection.setDoInput(true);
					urlConnection.setDoOutput(true);
					urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					urlConnection.setRequestProperty("Content-Length", String.valueOf(data.length));

					outputStream = urlConnection.getOutputStream();
					outputStream.write(data);
					outputStream.flush();

					int statusCode = urlConnection.getResponseCode();

					if(statusCode == HttpURLConnection.HTTP_OK) {
						InputStream inputStream = urlConnection.getInputStream();

						byteArrayOutputStream = new ByteArrayOutputStream();
						byte[] bytes = new byte[1024];
						int len;
						while((len = inputStream.read(bytes)) != -1) {
							byteArrayOutputStream.write(bytes, 0, len);
						}
						result = new String(byteArrayOutputStream.toByteArray());
						apiPostSucceeded(api, result);
					}else
						apiPostFailed(api, ERROR_CODE_SERVER_ERROR, "status code:" + statusCode);

				} catch (UnknownHostException e) {
					e.printStackTrace();
					apiPostFailed(api, ERROR_CODE_NO_INTERNET, "No internet");
				} catch (Exception e) {
					e.printStackTrace();
					apiPostFailed(api, ERROR_CODE_UNKNOWN_ERROR, e.toString());
				} finally {
					if(urlConnection != null){
						urlConnection.disconnect();
					}
					if(outputStream != null){
						try {
							outputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(byteArrayOutputStream != null){
						try {
							byteArrayOutputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();

	}

	private StringBuffer getRequestData(List<NameValuePair> params, String encode) {
		StringBuffer stringBuffer = new StringBuffer();
		try {
			for(NameValuePair param : params) {
				stringBuffer.append(param.getName())
						.append("=")
						.append(URLEncoder.encode(param.getValue(), encode))
						.append("&");
			}
			//刪除最後一個"&"
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer;
	}

	/**
	 * 解析JSon字串
	 * @param jsonString 要解析的JSon完整字串
	 * @param fieldNames 巢狀JSon的欄位Name...
	 * @return
	 */
	@Nullable
	protected ArrayList<Map<String, String>> parseJson(@NonNull String jsonString, String... fieldNames) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		ArrayList<Map<String, String>> dataList = new ArrayList<>();

		if(fieldNames != null){
			for(String fieldName : fieldNames){
				JSONArray jsonList = null;

				//如果Json最外層有包起來
				Map<String, String> outsideMap = new HashMap<>();
				if(fieldName != null && !fieldName.equals("")){
					try{
						//取出來的data暫存在Map<>
						outsideMap.putAll(addDecodingJson(jsonObject));
						jsonList = jsonObject.getJSONArray(fieldName);
					}catch(JSONException e){
						e.printStackTrace();
					}
				}

				//如果Json最外層有包起來，且正確取出資料
				if(jsonList != null){
					try{
						for(int i = 0 ; i < jsonList.length() ; i ++){
							jsonObject = new JSONObject(jsonList.getString(i));

							Map<String, String> insideMap = addDecodingJson(jsonObject);
							//與之前暫存在Map<>的data合併
							insideMap.putAll(outsideMap);
							dataList.add(insideMap);
						}
					}catch(JSONException e){
						e.printStackTrace();
					}
				}else {
					dataList.add(addDecodingJson(jsonObject));
				}
			}
		}else{
			dataList.add(addDecodingJson(jsonObject));
		}

		return dataList.size() == 0 ? null : dataList;
	}

	private Map<String, String> addDecodingJson(JSONObject jsonObject){
		String key;
		String value;

		Iterator<String> keyIter = jsonObject.keys();
		Map<String, String> map = new HashMap<>();
		try{
			while (keyIter.hasNext()) {
				key = keyIter.next();
				value = jsonObject.get(key).toString();
				map.put(key, value);
			}
		}catch(JSONException e){
			e.printStackTrace();
		}

		return map;
	}

}
