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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.example.data.DataInfo;
import com.example.feiyu.R;
import com.example.feiyumain.EditMSActivity;
import com.example.feiyumain.EnterPhoneActivity;
import com.example.feiyumain.ForseeMSActivity;
import com.example.feiyumain.MSLibraryActivity;
import com.example.feiyumain.SlideActivity;

public class FailCheckActivity extends Activity {

	private Button editBt;
	private TextView title_txt;
	private ImageButton returnBt;

	private Button resend_BT;
	public ListView listView;
	public SimpleAdapter adapter;
	public LayoutInflater inflater;
	HashMap<Object, Object> map = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_failcheck);

		editBt = (Button) findViewById(R.id.editBt);
		title_txt = (TextView) findViewById(R.id.title_txt);
		returnBt = (ImageButton) findViewById(R.id.returnBt);

		editBt.setVisibility(View.INVISIBLE);
		title_txt.setText("重发失败记录");
		returnBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});

		listView = (ListView) findViewById(R.id.failchecklistView);
		initListView();

		resend_BT = (Button) findViewById(R.id.resend_BT);

		resend_BT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				/*
				 * 补充全部重发！！！！
				 */
				// Toast.makeText(FailCheckActivity.this, "正在重发中.....",
				// Toast.LENGTH_SHORT).show();
				DataInfo.listItem.clear();
				ArrayList<Map<String, String>> faillist = getData();
				for (int i = 0; i < faillist.size(); i++) {
					Map<String, String> map = faillist.get(i);

					HashMap<String, Object> m = new HashMap<String, Object>();
					m.put("Phone", map.get("phone"));
					m.put("num", map.get("number"));
					DataInfo.listItem.add(m);
					DataInfo.RESEND_FLAG = true;
					DataInfo.RESEND_ID.add(map.get("id"));
				}
				
				Intent in = new Intent(FailCheckActivity.this,
						SlideActivity.class);
				in.putExtra("index", "1");
				finish();
				MSLibraryActivity.addFunction.finish();
				startActivity(in);

			}
		});

	}

	public void initListView() {

		adapter = new SimpleAdapter(this, getData(), R.layout.sendcheck_item,
				new String[] { "number", "phone", "time", "result" },
				new int[] { R.id.quhao, R.id.phone_sendcheck,
						R.id.time_sendcheck, R.id.ifsend });
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			private Intent in;
			private int position;
			private String message;
			private Map<String, String> map;
			private int choic;

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				this.position = position;

				map = (Map<String, String>) FailCheckActivity.this.adapter
						.getItem(position);

				AlertDialog.Builder builder = new AlertDialog.Builder(
						FailCheckActivity.this);
				builder.setTitle("重发");
				builder.setMessage("确认重发手机号为：" + map.get("phone") + "的信息吗？");
				// final String[] choics = { "使用", "删除" };

				// 设置一个单项选择下拉框
				/**
				 * 第一个参数指定我们要显示的一组下拉单选框的数据集合 第二个参数代表索引，指定默认哪一个单选框被勾选上，0表示默认'使用'
				 * 会被勾选上 第三个参数给每一个单选项绑定一个监听器
				 */
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// DataInfo.DBHelper.delete(Integer.parseInt(map
								// .get("id")));
								// adapter.notifyDataSetChanged();
								// initListView();
								/**
								 * 这个需要补充 重发！！！
								 */
								DataInfo.listItem.clear();

								HashMap<String, Object> m = new HashMap<String, Object>();
								m.put("Phone", map.get("phone"));
								m.put("num", map.get("number"));
								DataInfo.listItem.add(m);
								Intent in = new Intent(FailCheckActivity.this,
										SlideActivity.class);
								in.putExtra("index", "1");
								DataInfo.RESEND_FLAG = true;
								DataInfo.RESEND_ID.add(map.get("id"));
								finish();
								MSLibraryActivity.addFunction.finish();
								startActivity(in);

							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				builder.show();

			}
		});

	}

	// 获取数据
	public ArrayList<Map<String, String>> getData() {

		return DataInfo.DBHelper.sendFailed_searchAll();

	}
	
	protected void onRestart() {
		super.onRestart();
		// 刷新列表!!!!!!!!!
		initListView();
	}

}
