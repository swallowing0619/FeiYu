package com.example.tool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.util.DisplayMetrics;




/*
 * ��ȡ�ֻ���Ļ��Ϣ�Լ�����ԴͼƬ���д�С����Ĺ�����
 */
public class DevicTool {
	// �洢��ǰ�ֻ��豸����Ļ���
		public static int deviceWidth;
		// �洢��ǰ�ֻ��豸����Ļ�߶�
		public static int deviceHeight;

		/**
		 * ��������ͼƬ�Ĵ�С��������
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
		 * ��������ͼƬ���д�С����
		 * @param bitmap ������ͼƬ
		 * @param newWidth Ҫ���õĿ��
		 * @param newHeight Ҫ���õĸ߶�
		 * @return ���صõ���ͼƬ
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
		 * ��ȡ��ǰ�ֻ���Ļ����Ϣ���ֻ��Ŀ�Ⱥ͸߶ȣ�
		 * @param context �����Ķ���
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
