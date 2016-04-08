package com.ideabus.ideabus_structure.api;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.ideabus.ideabus_structure.custom.Global;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class HttpDownload extends BasePostAPI {

	private static final String TAG = HttpDownload.class.getSimpleName();
    private static HttpDownload instance;

    private OnDownloadCompletedListener listener;

    public interface OnDownloadCompletedListener{
		void OnDownloadCompleted(Bitmap bitmap);
	}
	public void setOnDownloadCompletedListener(OnDownloadCompletedListener l){
        listener = l;
	}
	private void OnDownloadCompleted(Bitmap bitmap){
		if(listener != null)
            listener.OnDownloadCompleted(bitmap);
	}

    public static HttpDownload getInstance(){
        if(instance == null)
            instance = new HttpDownload();
        return instance;
    }

    public void downloadDataFromUrl(String url){
        new HttpDownloadTask().execute(url);
    }

    private class HttpDownloadTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            OnDownloadCompleted(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String downloadUrl = params[0];
            Global.printLog("d", TAG, "downloadDataFromUrl  downloadUrl = " + downloadUrl);
            InputStream is = null;
            ByteArrayOutputStream baos = null;
            Bitmap bitmap = null;

            try {
                URLConnection connection = new URL(downloadUrl).openConnection();

                is = connection.getInputStream();
                baos = new ByteArrayOutputStream();

                int len;
                byte[] bt = new byte[1024];
                while ((len = is.read(bt)) != -1) {
                    baos.write(bt, 0, len);
                }
                byte[] archives = baos.toByteArray();
                bitmap = Global.decodeBitmapFromByteArray(archives, 1);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(baos != null){
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return bitmap;
        }
    }

}
