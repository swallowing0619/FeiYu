package com.example.addfunction;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.data.DataInfo;
import com.example.feiyu.R;
import com.example.feiyumain.MSLibraryActivity;
import com.example.feiyumain.SlideActivity;

public class WaitSendActivity extends Activity{

	private Button editBt;
	private TextView title_txt;
	private ImageButton returnBt;
	private ListView listView;
	public SimpleAdapter adapter;
	public LayoutInflater inflater;
	HashMap<Object, Object> map = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waitsend);
		
		editBt = (Button) findViewById(R.id.editBt);
		title_txt = (TextView) findViewById(R.id.title_txt);
		returnBt=(ImageButton) findViewById(R.id.returnBt);
		
		editBt.setVisibility(View.INVISIBLE);
		title_txt.setText("�����ͼ�¼");
		returnBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
		
		listView = (ListView) findViewById(R.id.waitsendlistView);
		initListView();
		
	}
	
	public void initListView() {

		adapter = new SimpleAdapter(this, getData(), R.layout.sendcheck_item,
				new String[] { "number", "phone", "time", "result" },
				new int[] { R.id.quhao, R.id.phone_sendcheck,
						R.id.time_sendcheck, R.id.ifsend });
		listView.setAdapter(adapter);
/*
		listView.setOnItemClickListener(new OnItemClickListener() {
			private Intent in;
			private int position;
			private String message;
			private Map<String, String> map;
			private int choic;

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				this.position = position;

				map = (Map<String, String>) adapter
						.getItem(position);

				AlertDialog.Builder builder = new AlertDialog.Builder(
						WaitSendActivity.this);
				builder.setTitle("ȡ������");
				builder.setMessage("ȷ��ȡ�������ֻ��ţ�" + map.get("phone") + "����Ϣ��");
				// final String[] choics = { "ʹ��", "ɾ��" };

				// ����һ������ѡ��������
				
				builder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// DataInfo.DBHelper.delete(Integer.parseInt(map
								// .get("id")));
								// adapter.notifyDataSetChanged();
								// initListView();
								//ɾ����ʱ�ļ�¼
								 
								DataInfo.DBHelper.timerSend_delete(map.get("id"));
								adapter.notifyDataSetChanged();
								initListView();
							}
						});
				builder.setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				builder.show();
				
			}
		});*/

	}

	// ��ȡ����
	public ArrayList<Map<String, String>> getData() {

		return DataInfo.DBHelper.timerSend_searchAll();

	}
	
	protected void onRestart() {
		super.onRestart();
		// ˢ���б�!!!!!!!!!
		initListView();
	}
}

