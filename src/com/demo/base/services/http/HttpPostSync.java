package com.demo.base.services.http;

import java.io.File;
import java.util.Map;

import com.demo.base.support.BaseConstants;
import com.demo.base.support.HttpSubmitMethodExt;

import android.content.Context;

/**
 *<p>Title:HttpPostSync.java</p>
 *<p>Description:http同步请求</p>
 *@author yin.liu
 *@version 1.0
 *2014-06-05
 */
public class HttpPostSync {

	private Context context;

	public HttpPostSync(Context context) {
		this.context = context;
	}

	private String tag = "HttpPostSync";

	public Object executePost(Object... param) {
		if (BaseConstants.POST_KEYVALUE_DATA.equals(param[0])) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) param[2];
			return HttpSubmitMethodExt.postKeyValueData(param[1].toString(), map);
		} else if(BaseConstants.POST_BYTE_DATA.equals(param[0])){
			return HttpSubmitMethodExt.postByteData(param[1].toString(), (byte[]) param[2]);
		} else if(BaseConstants.GET_HTTP_DATA.equals(param[0])){
			return HttpSubmitMethodExt.getHttpData(param[1].toString());
		} else if(BaseConstants.POST_FILES_DATA.equals(param[0])){
			Map<String, String> map = (Map<String, String>) param[2];
			Map<String, File> files = (Map<String, File>)param[3];
			return HttpSubmitMethodExt.post(param[1].toString(), map, files);
		} else{
			return BaseConstants.HTTP_REQUEST_FAIL;
		}
	}

}
