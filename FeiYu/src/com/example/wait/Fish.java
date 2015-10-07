package com.example.wait;

import com.example.data.DataInfo;
import com.example.tool.DevicTool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Fish {

	private Bitmap[] icons;
	private Paint paint = new Paint();
	private int count;
	private int x, y;
	private int height;//高度

	public Fish() {
		x = DataInfo.SCREEN_WIDTH/2;
		y = DataInfo.SCREEN_HEIGHT/2;
		
		icons = new Bitmap[3];
		for (int i = 1; i < 3; i++)
			icons[i - 1] = DevicTool.DevicTool(DataInfo.getBitmap("person"+i),
					DataInfo.width,DataInfo.height);//重置图片大小
//		icons[3] = DevicTool.DevicTool(DataInfo.getBitmap("person_down"),
//				DataInfo.PERSON_Width-25,DataInfo.PERSON_Height-30);
//		height = GameData.PERSON_Height;
	}

	public void draw(Canvas c) {
		c.drawBitmap(icons[count], x, y, paint);
		// 图片变化
		count += 1;

	}

}
