package com.demo.base.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class BitmapUtils
{
	
	public static Bitmap toRoundBitmap(Bitmap bitmap)
	{
		if(bitmap == null){
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height)
		{
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		}
		else
		{
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}


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
	
	public static boolean saveBitmap2file(Bitmap bmp,String filepath, String filename){
		CompressFormat format= Bitmap.CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		try {
		stream = new FileOutputStream(filepath + filename);
		} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}

		return bmp.compress(format, quality, stream);
	}

}
