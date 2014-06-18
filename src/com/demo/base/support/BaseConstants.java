package com.demo.base.support;

import android.os.Environment;

public class BaseConstants {

	/**
	 *  请求方式 postByteData
	 */
	public final static String POST_BYTE_DATA = "postByteData";
	
	/**
	 *  请求方式 postKeyValueData
	 */
	public final static String POST_KEYVALUE_DATA = "postKeyValueData";
	
	/**
	 *  请求方式 getHttpData
	 */
	public final static String GET_HTTP_DATA = "getHttpData";
	
	/**
	 *  请求方式 postFilesData
	 */
	public final static String POST_FILES_DATA = "postFilesData";
	
	/**
	 *  请求失败
	 */
	public final static String HTTP_REQUEST_FAIL = "HTTP_REQUEST_FAIL";
	
	/**
	 *  是否使用GZIP配置，默认为FALSE.
	 */
	public final static String HTTP_GZIP_ISUSEED = "FALSE";
	
	/**
	 *  编码统一设置Charset
	 */
	public final static String CHARSET = "UTF-8";
	
	/**
	 * 设置是否使用缓存 ，项目中设置可在项目string.xml中设置is_use_cache值即可
	 */
	public static boolean ISUSECACHE = true;
	
	/**
	 * 缓存根目录 项目中设置可在项目Constant中设置值即可
	 */
	public static String BASE_CACHE_PATH = Environment.getExternalStorageDirectory().toString() + "/gixue/";
	
	/**
	 * 静态缓存目录 项目中设置可在项目Constant中设置值即可
	 */
	public static String STATIC_PATH = "static/";
	
	/**
	 * API动态缓存目录 项目中设置可在项目Constant中设置值即可
	 */
	public static String API_PATH = "Api/";
	
	/**
	 * 后缀 API动态缓存目录 项目中设置可在项目Constant中设置值即可
	 */
	public static String API_FILE_SUFFIX=".gixue";
	
}
