package com.demo.base.support;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.demo.base.util.StringUtil;

public class HttpSubmitMethodExt {

	private static final String tag = "HttpSubmitMethodExt";
	
	/**
	*<b>Summary: </b>
	* postByteData(发送http请求)
	* @param urlString	请求url地址
	* @param data	byte[]类型的参数
	* @return
	 */
	public static synchronized Object postByteData(String urlString, byte[] data) {
		DataOutputStream ds = null;
		InputStream is = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(urlString);
			con = (HttpURLConnection) url.openConnection();
			 
			con.setRequestMethod("POST");//设置传送的method=POST
			con.setDoInput(true);// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
			con.setDoOutput(true);//设置是否从httpUrlConnection读入，默认情况下是true;
			con.setUseCaches(false);// Post 请求不能使用缓存
			
			// 设置头信息 
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", BaseConstants.CHARSET);
			con.setRequestProperty("content-type", "text/html");
			con.setRequestProperty("postMethod", BaseConstants.POST_BYTE_DATA);//设置http请求的方式
			
			if("TRUE".equalsIgnoreCase(BaseConstants.HTTP_GZIP_ISUSEED)){
				// 使用GZIP
				con.setRequestProperty("Accept-Encoding", "gzip");//设置http请求使用gzip
			}
			
			//设置请求超时时间为 3s
			con.setReadTimeout(3000);
			//设置输入流 
			ds = new DataOutputStream(con.getOutputStream());
			ds.write(data);
			ds.flush();
			
			is = con.getInputStream();//发送请求获取输出流
			
			if(con.getContentEncoding()!=null && con.getContentEncoding().contains("gzip")){//使用Gzip
				is = new GZIPInputStream(is);
			}
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, BaseConstants.CHARSET));
			
			StringBuilder sb = new StringBuilder();
			Object line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			Log.e(tag, StringUtil.deNull(e==null?"":e.getMessage()));
		} finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(ds != null){
				try {
					ds.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(con != null){
				con.disconnect();
			}
		}
		return BaseConstants.HTTP_REQUEST_FAIL;
	}

	/**
	*<b>Summary: </b>
	* postKeyValueData(发送http请求)
	* @param urlString	请求url地址
	* @param param	Map键值对
	* @return
	 */
	public static synchronized Object postKeyValueData(String urlString,Map<String, String> param) {
		InputStream is= null;
		try {
			// 是否使用缓存
			if(BaseConstants.ISUSECACHE){
				// 使用缓存
				
				//1，根据urlString和param组装请求url
				String url = urlString;
				Set<String> keyset = param.keySet();
				for(String key : keyset){
					url += "&"+key+"="+param.get(key);
				}
				//2，根据组装url返回转化后的本地缓存路径 GixueUtils.getApiStrConvertToCachePath()
				String cachePath = CacheSupport.getApiStrConvertToCachePath(url);
				//3，根据本地缓存路径查找本地缓存内容FileUtils.readText()，并将存在的记录的digest获取后添加到url参数，再继续http请求
				
				
				// 无网络，直接返回缓存内容
				
				// 有网络：
				// 如果返回为空，直接请求
				
				// 如果有值找出digest参数添加到请求url中去  （最后如果返回值空，则直接将本地缓存值返回）
			}
			
			HttpPost httpPost = new HttpPost(urlString);
			
			//在http请求头中添加请求方法类型
			httpPost.setHeader("postMethod", BaseConstants.POST_KEYVALUE_DATA);
			
			if("TRUE".equalsIgnoreCase(BaseConstants.HTTP_GZIP_ISUSEED)){
				// 使用GZIP
				httpPost.setHeader("Accept-Encoding", "gzip");//设置http请求使用gzip
			}
			
			// Post传送参数数必须用NameValuePair[]阵列储存
			// 传参数服务端获取的方法为request.getParameter("name")
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			Set<String> keyset = param.keySet();
			for(String key : keyset){
				params.add(new BasicNameValuePair(key,param.get(key)));
			}
			// 发出HTTP请求
			httpPost.setEntity(new UrlEncodedFormEntity(params,BaseConstants.CHARSET));
			DefaultHttpClient httpClient = new DefaultHttpClient();
			//设置请求超时时间为 3s
			httpClient.getParams().setIntParameter("http.socket.timeout",3000);  
			
			// 取得HTTP response
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {//正常响应
				HttpEntity httpEntity = httpResponse.getEntity();
				if(httpEntity!=null && httpEntity.getContentEncoding()!=null && httpEntity.getContentEncoding().getValue() != null && httpEntity.getContentEncoding().getValue().contains("gzip")){
					Log.i(tag, "使用gzip解析数据");
					// 使用Gzip
					is = httpEntity.getContent();
					is= new GZIPInputStream(is);
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, BaseConstants.CHARSET));
					
					StringBuilder sb = new StringBuilder();
					Object line;
					while ((line = bufferedReader.readLine()) != null) {
						sb.append(line);
					}
					return sb.toString();
				}else{
					// 返回响应字串
					return EntityUtils.toString(httpEntity);	
				}
				
			} else {
				throw new Exception("http请求响应不正确，响应值为"+httpResponse.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			Log.e(urlString, StringUtil.deNull(e==null?"":e.getMessage()));
		} finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return BaseConstants.HTTP_REQUEST_FAIL;
	}
	
	/**
	*<b>Summary: </b>
	* getHttpData(发送http请求)
	* @param urlString	请求url地址
	* @return
	 */
	public static synchronized Object getHttpData(String urlString) {
		InputStream is= null;
		try {
			
			HttpGet httpGet = new HttpGet(urlString);
			
			//在http请求头中添加请求方法类型
			httpGet.setHeader("postMethod", BaseConstants.GET_HTTP_DATA);
			if("TRUE".equalsIgnoreCase(BaseConstants.HTTP_GZIP_ISUSEED)){
				// 使用GZIP
				httpGet.setHeader("Accept-Encoding", "gzip");//设置http请求使用gzip
			}
			// 发出HTTP请求
			DefaultHttpClient httpClient = new DefaultHttpClient();
			//设置请求超时时间为 3s
			httpClient.getParams().setIntParameter("http.socket.timeout",3000);  
			
			// 取得HTTP response
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {//正常响应
				HttpEntity httpEntity = httpResponse.getEntity();
				if(httpEntity!=null && httpEntity.getContentEncoding()!=null && httpEntity.getContentEncoding().getValue() != null && httpEntity.getContentEncoding().getValue().contains("gzip")){
					Log.i(tag, "使用gzip解析数据");
					// 使用Gzip
					is = httpEntity.getContent();
					is= new GZIPInputStream(is);
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, BaseConstants.CHARSET));
					
					StringBuilder sb = new StringBuilder();
					Object line;
					while ((line = bufferedReader.readLine()) != null) {
						sb.append(line);
					}
					return sb.toString();
				}else{
					return EntityUtils.toString(httpResponse.getEntity());
				}
			} else {
				throw new Exception("http请求响应不正确，响应值为"+httpResponse.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			Log.e(urlString, StringUtil.deNull(e==null?"":e.getMessage()));
		} finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return BaseConstants.HTTP_REQUEST_FAIL;
	}
	
	/**
	 * POST上传文本与文件
	 * @param actionUrl
	 * @param params 文本参数对象 <参数名、参数值>
	 * @param files 文件MAP对象 <文件名、文件对象>
	 * @return
	 * @throws IOException
	 */
	public static String post(String actionUrl, Map<String, String> params,
			Map<String, File> files) {
		InputStream in = null;
		DataOutputStream outStream = null;
		HttpURLConnection conn = null;
		try{
			String BOUNDARY = java.util.UUID.randomUUID().toString();
			String PREFIX = "--", LINEND = "\r\n";
			String MULTIPART_FROM_DATA = "multipart/form-data";
			URL uri = new URL(actionUrl);
			conn = (HttpURLConnection) uri.openConnection();
			conn.setReadTimeout(3 * 1000);
			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false);
			conn.setRequestMethod("POST"); // Post方式
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charsert", BaseConstants.CHARSET);
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
					+ ";boundary=" + BOUNDARY);
			
			if("TRUE".equalsIgnoreCase(BaseConstants.HTTP_GZIP_ISUSEED)){
				// 使用GZIP
				conn.setRequestProperty("Accept-Encoding", "gzip");//设置http请求使用gzip
			}
	
			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + BaseConstants.CHARSET + LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}
	
			outStream = new DataOutputStream(conn
					.getOutputStream());
			outStream.write(sb.toString().getBytes());
	
			// 发送文件数据
			if (files != null)
				for (Map.Entry<String, File> file : files.entrySet()) {
					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
									+ file.getKey() + "\"" + LINEND);
					sb1.append("Content-Type: application/octet-stream; charset="
							+ BaseConstants.CHARSET + LINEND);
					sb1.append(LINEND);
					outStream.write(sb1.toString().getBytes());
					InputStream is = new FileInputStream(file.getValue());
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
	
					is.close();
					outStream.write(LINEND.getBytes());
				}
	
			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
	
			// 得到响应码
			int res = conn.getResponseCode();
			in = conn.getInputStream();
		//	InputStreamReader isReader = new InputStreamReader(in);
		//	BufferedReader bufReader = new BufferedReader(isReader);
		//	String line = null;
			//String data = "OK";
	
	//		while ((line = bufReader.readLine()) == null)
	//			data += line;
	
			if(conn.getContentEncoding() !=null && conn.getContentEncoding().contains("gzip")){//使用Gzip
				in = new GZIPInputStream(in);
			}
			
			if (res == 200) {
				int ch;
				StringBuilder sb2 = new StringBuilder();
				while ((ch = in.read()) != -1) {
					sb2.append((char) ch);
				}
			} else {
				throw new Exception("http请求响应不正确，响应值为"+res);
			}
			
			return in.toString();
		}catch (Exception e) {
			Log.e(actionUrl, StringUtil.deNull(e==null?"":e.getMessage()));
		} finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(outStream != null){
				try {
					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(conn != null){
				conn.disconnect();
			}
		}
		return BaseConstants.HTTP_REQUEST_FAIL;
	}
}
