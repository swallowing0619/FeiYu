package com.example.feiyumain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.addfunction.WaitSendActivity;
import com.example.addfunction.FailCheckActivity;
import com.example.addfunction.SendCheckActivity;
import com.example.addfunction.SendPersentActivity;
import com.example.data.DataInfo;
import com.example.feiyu.R;
import com.example.userui.AboutFishActivity;
import com.example.userui.FunctionIntroActivity;

public class MSLibraryActivity extends Activity implements OnClickListener{
//	private TextView title;
	public static MSLibraryActivity addFunction;
	private RelativeLayout checksend_layout,waitsend_layout,sendfailure_layout,sendpersent_layout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mslib);
		checksend_layout = (RelativeLayout) findViewById(R.id.checksend_layout);
		waitsend_layout = (RelativeLayout) findViewById(R.id.waitsend_layout);
		sendfailure_layout = (RelativeLayout) findViewById(R.id.sendfailure_layout);
		sendpersent_layout = (RelativeLayout) findViewById(R.id.sendpersent_layout);
		
		addFunction =  this;
		
		checksend_layout.setOnClickListener(this);
		waitsend_layout.setOnClickListener(this);
		sendfailure_layout.setOnClickListener(this);
		sendpersent_layout.setOnClickListener(this);
	
	}

	@Override
	public void onClick(View arg0) {
		
		switch (arg0.getId()) {
		case R.id.checksend_layout:
			Intent intent = new Intent();
			intent.setClass(MSLibraryActivity.this, SendCheckActivity.class);
//			finish();
			startActivity(intent);
			break;
		case R.id.waitsend_layout:
			Intent intent1 = new Intent();
			intent1.setClass(MSLibraryActivity.this, WaitSendActivity.class);
//			finish();
			startActivity(intent1);
			break;
		case R.id.sendfailure_layout:
			Intent intent2 = new Intent();
			intent2.setClass(MSLibraryActivity.this, FailCheckActivity.class);
//			finish();
			startActivity(intent2);
			break;
		case R.id.sendpersent_layout:
			Intent intent3 = new Intent(MSLibraryActivity.this, SendPersentActivity.class);
//			finish();
			this.startActivity(intent3);
			break;
		
		}
		
	}

	


}