package com.zy.android.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class NetworkHelper {
	private static final int RETRIES = 50;
	
	public static Bitmap fetchImage(String fileUrl) {
		Bitmap img = null;
		boolean downSampleFlag = false;
		for (int i = 0; i < RETRIES; i++) {
	        try {
	        	if (downSampleFlag) {
	        		img = getImageDownSample(fileUrl);
	        	} else {
	        		img = getImage(fileUrl);
	        	}
	        } catch (IOException e) {
	            e.printStackTrace();
	            long delay = (long) (Math.random() * (Math.pow(4, i) * 100L));
				 try {
					 Thread.sleep(delay);
				 } catch (InterruptedException iex){
//					 e.printStackTrace();
				 }
	            continue;
	        } catch (OutOfMemoryError e) {
	        	downSampleFlag = true;
	        	continue;
	        }
	        return img;
		}    
		
		return null;
    }
	
	private static Bitmap getImage(String url) throws IOException {
        URL myFileUrl = new URL(url);
        HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        Bitmap image = BitmapFactory.decodeStream(is);
        is.close();
        return image;
	}
	
	private static Bitmap getImageDownSample(String url) throws IOException {
        URL myFileUrl = new URL(url);
        HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap image = BitmapFactory.decodeStream(is,null,options);
        is.close();
        return image;
	}
	
	
}
