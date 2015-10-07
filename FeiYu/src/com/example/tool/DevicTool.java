package com.example.tool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.util.DisplayMetrics;




/*
 * 获取手机屏幕信息以及对资源图片进行大小适配的工具类
 */
public class DevicTool {
	// 存储当前手机设备的屏幕宽度
		public static int deviceWidth;
		// 存储当前手机设备的屏幕高度
		public static int deviceHeight;

		/**
		 * 将给定的图片的大小进行重置
		 * @param bitmap
		 * @return
		 */
		/*public static Bitmap resizedBitmap(Bitmap bitmap) {
			if (bitmap != null) {
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				Matrix matrix = new Matrix();
				matrix.postScale(Config.scaleWidth, Config.scaleHeight);
				Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
						height, matrix, true);
				return resizedBitmap;
			} else {
				throw new NullPointerException();
			}
		}*/

		/**
		 * 将给定的图片进行大小重置
		 * @param bitmap 给定的图片
		 * @param newWidth 要重置的宽度
		 * @param newHeight 要重置的高度
		 * @return 返回得到的图片
		 */
		public static Bitmap DevicTool(Bitmap bitmap, int newWidth,
				int newHeight) {
			if (bitmap != null) {
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				float scaleWidth = ((float) newWidth) / width;
				float scaleHeight = ((float) newHeight) / height;
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
				return resizedBitmap;
			} else {
				throw new NullPointerException();
			}
		}

		/**
		 * 获取当前手机屏幕的信息（手机的宽度和高度）
		 * @param context 上下文对象
		 */
		public static void getScreenInfo(Context context) {
			if (deviceHeight == 0 && deviceWidth == 0) {
				DisplayMetrics metrics = new DisplayMetrics();
				((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
				deviceHeight = metrics.heightPixels;
				deviceWidth = metrics.widthPixels;
			}
		}

}
