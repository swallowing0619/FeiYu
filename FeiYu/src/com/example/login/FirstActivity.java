package com.example.login;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.data.DataInfo;
import com.example.feiyu.R;
import com.example.feiyumain.MainActivity;
import com.example.net.HttpRequest;
import com.example.tool.DevicTool;

public class FirstActivity extends Activity {
	private ImageView fist_background;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		fist_background = new ImageView();
		setContentView(R.layout.activity_first);
		// 给TOOL传参
		DevicTool.getScreenInfo(this);
		//界面跳转

		creatDatabase();
		new Handler().postDelayed(runnable, 1800);

	}

	Runnable runnable = new Runnable() {
		public void run() {
			//下一步登录界面
			int account_sum = DataInfo.DBHelper.query_sum();
			if(account_sum==0){
				Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
				startActivity(intent);
				FirstActivity.this.finish();
			}else{
//				String account = DataInfo.DBHelper.first_account();
//				System.out.println(account);
//				String picname = DataInfo.DBHelper.query("nicheng", "name",account);
//				System.out.println(picname);
//				String address =  DataInfo.DBHelper.query("address", "name", account);
//				System.out.println(address);
//				String area =  DataInfo.DBHelper.query("area", "name",account);
//				System.out.println(area);
//				String company =  DataInfo.DBHelper.query("company", "name", account);
//				System.out.println(company);
				//若有用户信息直接 进入主界面
				Intent intent = new Intent(FirstActivity.this,
						MainActivity.class);
				finish();
				FirstActivity.this.startActivity(intent);
			}
			
		}
	};
	private void creatDatabase() {
		if (DataInfo.DBHelper == null) {
			DataInfo.DBHelper = new com.example.data.DatabaseHelper(
					getApplicationContext());
		}
		if (DataInfo.listItem == null)
			DataInfo.listItem = new ArrayList<HashMap<String, Object>>();
	}

	protected void onDestroy() {
		System.out.println("destory");
		super.onDestroy();
	}


}
