package com.demo.base.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

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
				URL url = new URL(serverUrl);
				conn = (HttpURLConnection) url
						.openConnection();
				input = conn.getInputStream();
				String dir = localUrl.substring(0,
						localUrl.lastIndexOf("/") + 1);
				new File(dir).mkdir();// 新建文件夹
				file.createNewFile();// 新建文件
				output = new FileOutputStream(file);
				// 读取大文件
				byte[] buffer = new byte[4 * 1024];
				while (input.read(buffer) != -1) {
					output.write(buffer);
				}
				output.flush();
			}
			Log.i(tag, localUrl + "download success");
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(tag, localUrl + "download fail");
			return false;
		} finally {
			try {
				if(output != null)
					output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(input != null){
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(conn != null){
				conn.disconnect();
			}
		}
		return true;
	}
	
}
