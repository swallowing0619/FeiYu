package com.example.userui;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.data.DataInfo;
import com.example.feiyu.R;
import com.example.net.HttpRequest;

public class PersonalActivity extends Activity {

	private Button editBt;
	private ImageButton returnBt;
	private TextView account_TV;
	private TextView nicheng_TV;
	private TextView address_TV;
	private TextView area_TV;
	private TextView company_TV;
	private ImageView user_picture;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);

		editBt = (Button) findViewById(R.id.editBt);
		returnBt = (ImageButton) findViewById(R.id.returnBt);
		user_picture  = (ImageView) findViewById(R.id.user_picture);
		account_TV = (TextView) findViewById (R.id.personal_name_TV);
		nicheng_TV  = (TextView) findViewById (R.id.personal_nicheng_TV);
		address_TV = (TextView) findViewById (R.id.personal_address_TV);
		area_TV = (TextView) findViewById (R.id.personal_sendoutarea_TV);
		company_TV = (TextView) findViewById (R.id.personal_sendcompany_TV);
		
		//初始化值
		init();
		// 获取用户信息
		// 启动线程
		new Thread(runnable).start();
		
		editBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(PersonalActivity.this,
						PersonalEditActivity.class);
				startActivity(intent);
			}
		});

		returnBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
	}
	
	private void init(){
		String account = DataInfo.DBHelper.first_account();
		account_TV .setText(account); 
		
		String picname = DataInfo.DBHelper.query("nicheng", "name",account);
		nicheng_TV.setText(picname);
		String address =  DataInfo.DBHelper.query("address", "name", account);
		address_TV.setText(address);
		String area =  DataInfo.DBHelper.query("area", "name",account);
		area_TV.setText(area);
		String company =  DataInfo.DBHelper.query("company", "name", account);
		company_TV.setText(company);
	}
	
	private Callback callback = new Callback() {
		public boolean handleMessage(Message msg) {
			// 刷新
			if (msg.what == 0) {
				// 更新用户信息
				init();
			} else if (msg.what == 1) {
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
					boolean flag = httprequest.getAllUserInfo();
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
	
	protected void onRestart() {
		super.onRestart();
		init();
	}
}
