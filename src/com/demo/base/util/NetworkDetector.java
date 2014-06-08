package com.demo.base.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * <p> Title: </p>
 * <p> Description: 检测当前网络状态是否可用  </p>
 * <p> Copyright:Copyright (c) 2012  </p>
 * <p> Company:湖南科创  </p> 
 * @author xianlu.lu
 * @version 1.0
 * @date 2012-5-5
 */

public class NetworkDetector {  
   
    public static boolean isNetWork(Context act) {  
        
       ConnectivityManager manager = (ConnectivityManager) act  
              .getApplicationContext().getSystemService( Context.CONNECTIVITY_SERVICE);  
        
       if (manager == null) {  
           return false;  
       }  
       
       /** 获取可用网络信息 */
       NetworkInfo networkinfo = manager.getActiveNetworkInfo();  
        
       if (networkinfo == null) {  
           return false;  
       }  
   
       return true;  
    }  
}  
