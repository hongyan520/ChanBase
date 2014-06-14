package com.demo.base.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class BitmapUtils
{
	


	public static Drawable scaleBitmap(Bitmap bitmapOrg,Context context)
	{

		// / 加载需要操作的图片，这里是eoeAndroid的logo图片
		// Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),
		// R.drawable.eoe_android);

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		// 取得窗口属性
		wm.getDefaultDisplay().getMetrics(dm);
		// 窗口的宽度
		int screenWidth = dm.widthPixels;
		// 窗口高度
		int screenHeight = dm.heightPixels;
		// 屏幕高宽比例
		float screenRatio = ((float) screenHeight) / ((float) screenWidth);

		// 图片的原始高宽比例
		int height = bitmapOrg.getHeight();
		int width = bitmapOrg.getWidth();
		float picRatio = ((float) height) / ((float) width);

		float splashHeight = 0;
		float splashWidth = 0;
		if (screenRatio > picRatio) {
			splashHeight = screenHeight;
			splashWidth = screenHeight / picRatio;
		} else {
			splashWidth = screenWidth;
			splashHeight = screenWidth * picRatio;
		}

		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = splashWidth / width;
		float scaleHeight = splashHeight / height;

		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();

		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);

		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);

		// 将上面创建的Bitmap转换成Drawable对象，使得其可以使用在ImageView, ImageButton中
		BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
		return bmd;
	}

	public static Drawable scaleBitmap(Bitmap bitmapOrg, int setwh, int setht)
	{

		// / 加载需要操作的图片，这里是eoeAndroid的logo图片
		// Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),
		// R.drawable.eoe_android);

		// 获取这个图片的宽和高
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();

		// 定义预转换成的图片的宽度和高度
		int newWidth = setwh;
		int newHeight = setht;

		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();

		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);

		// 旋转图片 动作
		// matrix.postRotate(45);

		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);

		// 将上面创建的Bitmap转换成Drawable对象，使得其可以使用在ImageView, ImageButton中
		BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);

		return bmd;
	}

	/********************* Drawable转Bitmap ************************/
	public static Bitmap drawabletoBitmap(Drawable drawable)
	{

		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicWidth();

		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);

		drawable.draw(canvas);

		return bitmap;
	}

}
