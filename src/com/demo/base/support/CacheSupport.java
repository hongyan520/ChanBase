package com.demo.base.support;

import java.util.Map;
import java.util.Set;

import com.demo.base.util.FileUtils;

public abstract class CacheSupport {


	/**
	 * 静态资源 服务器路径转化为本地缓存路径
	 * 逻辑：将UUID的目录部分作为此文件的文件名
	 * @param serverUrl
	 * @return
	 */
	public static String staticServerUrlConvertToCachePath(String serverUrl) {
		if("".equals(serverUrl)){
			return "";
		}
		String localUrl = "";
		int j = serverUrl.lastIndexOf("/")-1;
		int i = serverUrl.lastIndexOf("/",j);
		localUrl = serverUrl.substring(i+1,j)+serverUrl.substring(serverUrl.lastIndexOf("."));
		return BaseConstants.BASE_CACHE_PATH+BaseConstants.STATIC_PATH+localUrl;
	}
	
	/**
	 * 获取动态资源url转化为规范的文件名缓存路径
	 * 逻辑：将所有特殊字符替换成“-”
	 * @param apiStr
	 * @return
	 */
	public static String getApiStrConvertToCachePath(String apiStr) {
		if("".equals(apiStr)){
			return "";
		}
		return BaseConstants.BASE_CACHE_PATH+ BaseConstants.API_PATH + apiStr.replaceAll("http://", "").replaceAll("[?,=,&,.]", "-")+BaseConstants.API_FILE_SUFFIX;
	}
	
	/**
	 * 根据url和参数获取本地缓存路径
	 * @param urlString
	 * @param param
	 * @return
	 */
	public static String getApiCachePathByUrlAndParam(String urlString,Map<String, String> param){
		//1，根据urlString和param组装请求url
		String url = urlString;
		Set<String> keyset = param.keySet();
		for(String key : keyset){
			url += "&"+key+"="+param.get(key);
		}
		//2，根据组装url返回转化后的本地缓存路径 GixueUtils.getApiStrConvertToCachePath()
		return  getApiStrConvertToCachePath(url);
	}
	
	/**
	 * 根据传入url和参数获取缓存内容
	 * @param urlString 请求api的url
	 * @param param 请求的参数封装Map（keyValue）
	 * @return
	 */
	public static String getApiCacheContentByUrlAndParam(String urlString,Map<String, String> param){
		// 根据本地缓存路径查找本地缓存内容FileUtils.readText()
		String cacheContent = FileUtils.readText(getApiCachePathByUrlAndParam(urlString, param));
		return cacheContent;
	}
}
