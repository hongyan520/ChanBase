package com.demo.base.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;

public class HttpUtils {

	private static final String tag = "HttpUtils";

	/**
	 * 下载服务器文件
	 * @param serverUrl 服务器地址
	 * @param localUrl 本地存储地址
	 * @return
	 */
	public static boolean downloadFile(String serverUrl, String localUrl) {
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection conn = null;
		try {

			File file = new File(localUrl);
			if (file.exists()) {
				Log.i(tag, localUrl + "is exist");
				return true;
			} else {
				
				//httpGet连接对象  
		        HttpGet httpRequest = new HttpGet(serverUrl);
		      //取得HttpClient 对象  
		        HttpClient httpclient = new DefaultHttpClient();  
		      //请求httpClient ，取得HttpRestponse  
	            HttpResponse httpResponse = httpclient.execute(httpRequest);  
	            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
	                //取得相关信息 取得HttpEntiy  
	                HttpEntity httpEntity = httpResponse.getEntity();  
	                //获得一个输入流  
	                input = httpEntity.getContent();  
	                String dir = localUrl.substring(0,
							localUrl.lastIndexOf("/") + 1);
					new File(dir).mkdirs();// 新建文件夹
					file.createNewFile();// 新建文件
					output = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
	                int len = 0;
	                while ((len = input.read(buffer)) != -1) {
	                	output.write(buffer, 0, len);
	                }
					
					// 读取大文件
//					byte[] buffer = new byte[4 * 1024];
//					while (input.read(buffer) != -1) {
//						output.write(buffer);
//					}
					output.flush();
	            }
				
//				URL url = new URL(serverUrl);
//				conn = (HttpURLConnection) url
//						.openConnection();
//				input = conn.getInputStream();
				
			}
			Log.i(tag, localUrl + "download success");
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(tag, localUrl + "download fail");
			return false;
		} finally {
			if(input != null){
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				if(output != null)
					output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(conn != null){
				conn.disconnect();
			}
		}
		return true;
	}
	
	
}
