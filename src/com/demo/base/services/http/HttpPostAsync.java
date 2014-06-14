package com.demo.base.services.http;

import java.io.File;
import java.util.Map;

import com.demo.base.services.ICallBack;
import com.demo.base.support.BaseConstants;
import com.demo.base.support.HttpSubmitMethodExt;

import android.content.Context;
import android.os.AsyncTask;


/**
 *<p>Title:HttpPostAsync.java</p>
 *<p>Description:http异步请求</p>
 *@author yin.liu
 *@version 1.0
 *2014-06-05
 */
public class HttpPostAsync extends AsyncTask<Object, Object, Object> implements ICallBack {
	
	private String tag = "HttpPostAsync";
	
	private Context context;
	
	public HttpPostAsync(Context context) {
		this.context = context;
	}
	

	/**
	  * <b>Summary: </b>
	  *     复写方法 doInBackground
	  * @param param
	  * @return 
	  * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Object doInBackground(Object... param) {
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

	/**
	  * <b>Summary: </b>
	  *     复写方法 onPostExecute
	  * @param result 
	  * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Object result) {
		backResult(result);
	}

	/**
	  * <b>Summary: </b>
	  *     复写方法 backResult
	  * @param result
	  * @return 
	  * @see com.chinacreator.framework.services.ICallBack#backResult(java.lang.Object)
	 */
	public Object backResult(Object result) {
		return result;
	}
}
