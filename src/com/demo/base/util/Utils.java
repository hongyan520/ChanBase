package com.demo.base.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class Utils {
	private static final String LOG_TAG = "Utils";
	
	/**
	 * 获取窗口属性
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		// 取得窗口属性
		wm.getDefaultDisplay().getMetrics(dm);
		return dm;
	}
	public static boolean isNetWorkExist(Context ctx) {
		try {
			ConnectivityManager conManager = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (conManager.getActiveNetworkInfo() == null
					|| !conManager.getActiveNetworkInfo().isAvailable()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	public static int getVersionCode(Context ctx) {
		int versionCode = 101;

		try {

			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi;
			pi = pm.getPackageInfo(ctx.getPackageName(), 0);
			if (pi != null) {
				versionCode = pi.versionCode;
			} else {
			}

		} catch (NameNotFoundException e) {
		}

		return versionCode;
	}

	public static String getVersionName(Context ctx) {
		String versionName = "";

		try {

			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi;
			pi = pm.getPackageInfo(ctx.getPackageName(), 0);
			if (pi != null) {
				versionName = pi.versionName;
			} else {
			}

		} catch (NameNotFoundException e) {
		}

		return versionName;
	}

	public static void killProcess(Context mAct) {
		String packageName = mAct.getPackageName();
		String processId = "";
		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec("ps");
			BufferedReader br = new BufferedReader(new InputStreamReader(p
					.getInputStream()));
			String inline;
			while ((inline = br.readLine()) != null) {
				if (inline.contains(packageName)) {
					break;
				}
			}
			br.close();
			StringTokenizer processInfoTokenizer = new StringTokenizer(inline);
			int count = 0;
			while (processInfoTokenizer.hasMoreTokens()) {
				count++;
				processId = processInfoTokenizer.nextToken();
				if (count == 2) {
					break;
				}
			}
			r.exec("kill -15 " + processId);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void showKeypad(Context ctx, View view) {
		InputMethodManager imm = (InputMethodManager) ctx
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void hideKeypad(Context ctx, View view) {
		InputMethodManager imm = (InputMethodManager) ctx
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
	}

	/**
	 * 
	 * @param mContext
	 * @param assetsPath
	 *            :assets下的文件路径
	 * @param dataPath
	 *            ：data/data下的文件路径
	 * @param saveName
	 *            ：保存成的名字
	 */
	public static void moveFile(Context mContext, String assetsPath,
			String dataPath, String saveName) {
		AssetManager assetManager = mContext.getAssets();
		InputStream myInput;
		try {
			myInput = assetManager.open(assetsPath);
			String outFileName = dataPath + saveName;
			OutputStream myOutput = new FileOutputStream(outFileName);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void showQuitDialog(final Activity ctx, String title, String ok, String cancel) {
		Dialog alertDialog = new AlertDialog.Builder(ctx)
				.setMessage(title)
				.setPositiveButton(ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								ctx.finish();
							}
						})
				.setNegativeButton(cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).create();

		alertDialog.show();
	}

	public static final String encodeUrl(String url) {
		try {
			URL url1;
			url1 = new URL(url);

			URI uri = new URI(url1.getProtocol(), url1.getUserInfo(),
					url1.getHost(), url1.getPort(), url1.getPath(),
					url1.getQuery(), url1.getRef());

			URL u = uri.toURL();
			return u.toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return url;

	}

	/** Method to check whether external media available and writable. */

	public static boolean checkExternalMedia() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		boolean stat;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// Can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
			stat = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// Can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
			stat = false;
		} else {
			// Can't read or write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
			stat = false;
		}

		return stat;
	}

	public static String getMetaData(Context ctx, String key) {
		String result = "";

		try {

			ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(
					ctx.getPackageName(), PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			result = bundle.getString(key);

		} catch (NameNotFoundException e) {
			Log.e(LOG_TAG,
					"Failed to load meta-data, NameNotFound: " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e(LOG_TAG,
					"Failed to load meta-data, NullPointer: " + e.getMessage());

		}

		return result;

	}

	

	public static String buildUrl(HashMap<String, Object> parameters)
			throws UnsupportedEncodingException {
		StringBuilder builder = new StringBuilder();
		Set<String> keys = parameters.keySet();
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = iterator.next();

			builder.append(key)
					.append("=")
					.append(URLEncoder.encode(parameters.get(key).toString(),
							"UTF-8"));
			if (iterator.hasNext()) {
				builder.append("&");
			}
		}
		return builder.toString();
	}

	public static String getLocaleTime(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		SimpleDateFormat tempDate = new SimpleDateFormat("MM/dd HH:mm:ss");
		return tempDate.format(calendar.getTime());
	}

	public static String checkSdcard(String path) {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String result = "";
		String state = Environment.getExternalStorageState();

		File fs = new File(path);

		try {
			if (!fs.exists()) {
				fs.mkdirs();
			}
		} catch (Exception e) {
			if (!fs.canWrite()) {
				result = "您的SD存储卡不可写入数据";
			} else {

			}
		}

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
			result = "您的SD存储卡不可写入数据"
					+ mExternalStorageAvailable;
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
			result = "请插入SD存储卡来保存您的数据"
					+ mExternalStorageAvailable + mExternalStorageWriteable;
		}

		return result;
	}

	public static String saveToSDCard(byte[] data, String path, String name) {
		String sdcardStatus = checkSdcard(path);
		if ("".equals(sdcardStatus)) {
			return sdcardStatus;
		}

		saveFile(data, path, name);

		return "";
	}

	private static boolean saveFile(byte[] data, String path, String name) {
		String filename = name;
		File sdImageMainDirectory = new File(path);

		if (!sdImageMainDirectory.exists()) {
			sdImageMainDirectory.mkdirs();
		}

		File outputFile = new File(sdImageMainDirectory, filename);

		try {
			FileOutputStream fos2 = new FileOutputStream(outputFile);
			fos2.write(data);
			fos2.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean createDirIfNotExist(String path) {
		File sdImageMainDirectory = new File(path);

		if (!sdImageMainDirectory.exists()) {
			sdImageMainDirectory.mkdirs();
		}
		return false;
	}

	public static String getSafeString() {

		return "citybustour";
	}

	/**
	 ** 获取SdCard路径
	 * 
	 * 　
	 */
	public static String getExternalStoragePath() {
		// 获取SdCard状态
		String state = android.os.Environment.getExternalStorageState();
		// 判断SdCard是否存在并且是可用的
		if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
			if (android.os.Environment.getExternalStorageDirectory().canWrite()) {
				return android.os.Environment.getExternalStorageDirectory()
						.getPath();
			}
		}
		return null;
	}

	/**
	 * 
	 * 获取存储卡的剩余容量，单位为字节
	 * 
	 * @param filePath
	 * 
	 * @return availableSpare
	 */
	public static long getAvailableStore(String filePath) {
		// 取得sdcard文件路径
		StatFs statFs = new StatFs(filePath);
		// 获取block的SIZE
		long blocSize = statFs.getBlockSize();
		// 获取BLOCK数量
		// long totalBlocks = statFs.getBlockCount();
		// 可使用的Block的数量
		long availaBlock = statFs.getAvailableBlocks();
		// long total = totalBlocks * blocSize;
		long availableSpare = availaBlock * blocSize;
		return availableSpare;
	}

	public static boolean isServiceRunning(Context context, String className) {

		boolean isRunning = false;

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(Integer.MAX_VALUE);

		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {

				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	public static void deleteFileFromPath(String dir, String fileName) {
		new File(dir, fileName).delete();
	}
}
