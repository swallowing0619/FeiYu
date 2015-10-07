package com.example.addfunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.data.DataInfo;
import com.example.feiyu.R;

public class SendCheckActivity extends Activity {

	private Button editBt;
	private TextView title_txt;
	private ImageButton returnBt;
	private EditText queryNum_edit;
	private Button query;
	private boolean query_flag = true;
	public ListView listView;
	public SimpleAdapter adapter;
	public LayoutInflater inflater;
	HashMap<Object, Object> map = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendcheck);

		editBt = (Button) findViewById(R.id.editBt);
		title_txt = (TextView) findViewById(R.id.title_txt);
		returnBt = (ImageButton) findViewById(R.id.returnBt);
		listView = (ListView) findViewById(R.id.sendchecklistView);
		queryNum_edit = (EditText) findViewById(R.id.queryNumEdit);
		query = (Button) findViewById(R.id.query);
		
		editBt.setText("清空");
		title_txt.setText("发送记录");
		returnBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
		editBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.out.println("清空");
				DataInfo.DBHelper.sendRecord_deleteALL();
				adapter.notifyDataSetChanged();
				initListView();
			}
		});
		query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(query_flag){
				String phone=queryNum_edit.getText().toString();
				System.out.println("查询号码"+phone);
				initListView_query( phone);
				adapter.notifyDataSetChanged();
				query.setText("取消");
				query_flag = false;
				}else{
					initListView();
					adapter.notifyDataSetChanged();
					query.setText("查询");
					queryNum_edit.setText("");
					query_flag = true;
				}
			}
		});

		initListView();
	}

	public void initListView() {

		adapter = new SimpleAdapter(this, getData(), R.layout.sendcheck_item,
				new String[] { "number", "phone", "time", "result" },
				new int[] { R.id.quhao, R.id.phone_sendcheck,
						R.id.time_sendcheck, R.id.ifsend });
		listView.setAdapter(adapter);

	}
	
	public void initListView_query(String phone) {

		adapter = new SimpleAdapter(this, getQueryData(phone), R.layout.sendcheck_item,
				new String[] { "number", "phone", "time", "result" },
				new int[] { R.id.quhao, R.id.phone_sendcheck,
						R.id.time_sendcheck, R.id.ifsend });
		listView.setAdapter(adapter);

	}

	// 获取数据
	public ArrayList<Map<String, String>> getData() {

		return DataInfo.DBHelper.sendRecord_searchAll();

	}
	//获取查询数据
	public ArrayList<Map<String, String>> getQueryData(String phone) {

		return  DataInfo.DBHelper.query_number(phone);

	}


	protected void onRestart() {
		super.onRestart();
		// 刷新列表!!!!!!!!!
		initListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
