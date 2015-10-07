package com.example.feiyumain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.data.DataInfo;
import com.example.feiyu.R;
import com.example.login.LoginActivity;
import com.example.net.HttpRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ImageButton editMS;
	private ImageButton MSLibrary;
	private ImageButton enterPhone;
	private ImageButton userInfo;
	private ImageButton lookupmess;
	public static MainActivity main_activity;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		creatDatabase();// 创建数据库
		// 获取控件
		accessControl();
		// 添加监听
		addListener();
		main_activity = this;

	}

	private void creatDatabase() {
		if (DataInfo.DBHelper == null) {
			DataInfo.DBHelper = new com.example.data.DatabaseHelper(
					getApplicationContext());
		}
		if (DataInfo.listItem == null)
			DataInfo.listItem = new ArrayList<HashMap<String, Object>>();
	}

	// 获取控件
	private void accessControl() {
		editMS = (ImageButton) findViewById(R.id.editmessBtn);
		MSLibrary = (ImageButton) findViewById(R.id.messlibBtn);
		enterPhone = (ImageButton) findViewById(R.id.enternumBtn);
		userInfo = (ImageButton) findViewById(R.id.userinfoBtn);
		lookupmess = (ImageButton) findViewById(R.id.lookuPmessBT);
	}

	// 添加监听
	private void addListener() {
		MyOnClickListener ml = new MyOnClickListener();
		editMS.setOnClickListener(ml);
		MSLibrary.setOnClickListener(ml);
		enterPhone.setOnClickListener(ml);
		userInfo.setOnClickListener(ml);
		lookupmess.setOnClickListener(ml);
	}

	// handler接受消息回调方法
	private Callback callback = new Callback() {
		public boolean handleMessage(Message msg) {
			// 刷新
			if (msg.what == 0) {
				String account = DataInfo.DBHelper.first_account();
				String msgcount = DataInfo.DBHelper.query("msgcount", "name",
						account);
				Toast.makeText(getApplicationContext(),
						"您的短信剩余：" + msgcount + "条", 5000).show();
			}
			if (msg.what == 1) {
				Toast.makeText(getApplicationContext(), "网络连接有问题",
						Toast.LENGTH_SHORT).show();

			}
			return false;
		}
	};
	// 线程访问组件的中间代理
	private Handler handler = new Handler(callback);

	private Runnable runnable = new Runnable() {

		public void run() {
			while (true) {
				// 不能从线程访问界面上的组件
				try {
					HttpRequest httprequest = new HttpRequest();
					boolean flag = httprequest.getUserInfo();
					Thread.sleep(100);
					if (flag) {
						System.out.println("OK");
						handler.sendEmptyMessage(0);
						break;
					} else {
						System.out.println("fail");
						if (DataInfo.Request_diable) {
							handler.sendEmptyMessage(1);
						}
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

	};

	private class MyOnClickListener implements OnClickListener {

		public void onClick(View v) {
			Intent intent = new Intent();

			if (v.getId() == R.id.editmessBtn) {
				intent.setClass(MainActivity.this, SlideActivity.class);
				intent.putExtra("index", "0");
				startActivity(intent);

			} else if (v.getId() == R.id.messlibBtn) {
				intent.setClass(MainActivity.this, SlideActivity.class);
				intent.putExtra("index", "2");
				startActivity(intent);

			} else if (v.getId() == R.id.enternumBtn) {
				intent.setClass(MainActivity.this, SlideActivity.class);
				intent.putExtra("index", "1");
				startActivity(intent);

			} else if (v.getId() == R.id.userinfoBtn) {
				intent.setClass(MainActivity.this, SlideActivity.class);
				intent.putExtra("index", "3");
				startActivity(intent);
			} else if (v.getId() == R.id.lookuPmessBT) {
				Toast.makeText(getApplicationContext(), "用户信息查询中",
						Toast.LENGTH_SHORT).show();
				// 启动线程
				new Thread(runnable).start();

			}
		}
	}

	private long exitTime = 0;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {

				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
