package com.example.wait;


import com.example.data.DataInfo;

import android.content.Context;
import android.graphics.Canvas;
import android.provider.ContactsContract.Contacts.Data;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class MysurfaceView extends SurfaceView implements Callback, Runnable{
	private SurfaceHolder holder;
	private boolean runFlag = true;
	private SurfaceView view = (SurfaceView) this;
	private Thread t;
	private Thread move;
	
	
	public MysurfaceView(Context context) {
		this(context, null);
	}

	public MysurfaceView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MysurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		holder = this.getHolder();
		// ��ӻص��ӿ�
		holder.addCallback(this);

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// �������
	//	add();
		// �����ƶ��߳�
	//	move = new MoveControl();
	//	move.start();
		// ������ͼ�߳�
		t = new Thread(this);
		t.start();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		runFlag = false;
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (runFlag) {
//			System.out.println("");
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				DataInfo.canvas = canvas;
				
				// �������ƶ�����
				DataInfo.fish.draw(canvas);
				
				
//				if(GameData.BACKPRESS){
//					return ;
//				}
				

			} catch (Exception ex) {
				ex.printStackTrace();

			} finally {
				if (canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}
		}
	}

}
