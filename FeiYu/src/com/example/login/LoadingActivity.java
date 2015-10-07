package com.example.login;



import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.widget.Toast;

import com.example.feiyu.R;
import com.example.feiyumain.MainActivity;
import com.example.net.HttpRequest;

public class LoadingActivity extends Activity {
	
	 public static LoadingActivity loadingactivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.loading);
		loadingactivity = this;
	//这里Handler的postDelayed方法，等待10000毫秒在执行run方法。
	//在Activity中我们经常需要使用Handler方法更新UI或者执行一些耗时事件，
	//并且Handler中post方法既可以执行耗时事件也可以做一些UI更新的事情，比较好用，推荐使用
		
		
	
	
		
   }
	
	
}