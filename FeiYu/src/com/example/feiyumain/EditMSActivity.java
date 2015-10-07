package com.example.feiyumain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.data.DataInfo;
import com.example.feiyu.R;

public class EditMSActivity extends Activity implements OnClickListener {
	private Button saveBt;
	private Button useBt;
	private EditText newMSM_txt;
	private TextView textCount;
	public ListView listView;
	public static SimpleAdapter adapter;
	public LayoutInflater inflater;
	ArrayList<HashMap<Object, Object>> list = new ArrayList<HashMap<Object, Object>>();
	HashMap<Object, Object> map = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editms);
		initViews();
		// ��ʼ��listview
		initListView();
		initEvents();

	}

	private void initViews() {
		saveBt = (Button) this.findViewById(R.id.save);
		newMSM_txt = (EditText) this.findViewById(R.id.newms_text);
		listView = (ListView) findViewById(R.id.model_lv);
		inflater = this.getLayoutInflater();
		if (!(DataInfo.MESSAGE.equals(""))) {
			newMSM_txt.setText(DataInfo.MESSAGE);
		}
		useBt = (Button) this.findViewById(R.id.use);
		textCount = (TextView) this.findViewById(R.id.textCount);

		newMSM_txt.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				int sum = s.length();
				textCount.setHint(sum + "/70");
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 70) {
					Toast.makeText(getApplicationContext(), "�����Ѵ�70",
							Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void afterTextChanged(Editable s) {
				int sum2 = s.toString().length();
				textCount.setHint(sum2 + "/70");
			}

		});
	}

	/*
	 * ���ü�����
	 */
	private void initEvents() {
		saveBt.setOnClickListener(this);
		useBt.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			private Intent in;
			private int position;
			private String message;
			private Map<String, String> map;
			private int choic;

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				this.position = position;

				map = (Map<String, String>) EditMSActivity.adapter
						.getItem(position);

				message = map.get("message");
				newMSM_txt.setText(message);
			}
		});

	}

	@Override
	public void onClick(View v) {
		// �����ɺ����ݲ������ݿ�
		if (v.equals(saveBt)) {
			System.out.println("save");
			// �ж����ݿ��ģ���Ƿ��ظ�
			boolean flag = DataInfo.DBHelper.query_message(newMSM_txt.getText()
					.toString());
			if (!flag) {
				DataInfo.DBHelper.insert(newMSM_txt.getText().toString());
				EditMSActivity.adapter.notifyDataSetChanged();
				initEvents();
				initListView();
			} else {
				Toast.makeText(getApplicationContext(), "���ſ��Ѵ��ڣ�����洢",
						Toast.LENGTH_SHORT).show();
			}

		} else if (v.equals(useBt)) {
			// ���ʹ��
			// �ж϶���ģ���Ƿ�������
			boolean havingflag = DataInfo.isHave(newMSM_txt.getText()
					.toString(), "x");
			if (!havingflag) {
				Intent in = new Intent(EditMSActivity.this, SlideActivity.class);
				in.putExtra("index", "1");
				DataInfo.MESSAGE = newMSM_txt.getText().toString();
				System.out.println("message" + DataInfo.MESSAGE);
				this.finish();
				this.startActivity(in);
			}else{
				Toast.makeText(getApplicationContext(), "��������δ���ƣ������޸�",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	public void initListView() {
		adapter = new SimpleAdapter(this, getData(), R.layout.mslib_item,
				new String[] { "message" }, new int[] { R.id.model_text });
		listView.setAdapter(adapter);

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			private Intent in;
			private int position;
			private String message;
			private Map<String, String> map;
			private int choic;

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				this.position = position;

				map = (Map<String, String>) EditMSActivity.this.adapter
						.getItem(position);

				message = map.get("message");

				AlertDialog.Builder builder = new AlertDialog.Builder(
						EditMSActivity.this);
				builder.setTitle("ɾ��");
				builder.setMessage("ȷ��ɾ����");
				// final String[] choics = { "ʹ��", "ɾ��" };

				// ����һ������ѡ��������
				/**
				 * ��һ������ָ������Ҫ��ʾ��һ��������ѡ������ݼ��� �ڶ�����������������ָ��Ĭ����һ����ѡ�򱻹�ѡ�ϣ�0��ʾĬ��'ʹ��'
				 * �ᱻ��ѡ�� ������������ÿһ����ѡ���һ��������
				 */
				// builder.setSingleChoiceItems(choics, 0,
				// new DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog,
				// int which) {
				// choic = which;
				//
				// }
				// });
				builder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// if (choic == 0) {//ʹ��
								//
								// // if(DataInfo.listItem.size()>0){
								// // in = new Intent(MSLibraryActivity.this,
								// // ForseeMSActivity.class);
								// // DataInfo.MESSAGE = message;
								// // MSLibraryActivity.this.finish();
								// // MSLibraryActivity.this.startActivity(in);
								// // }else{
								// Intent in = new Intent(EditMSActivity.this,
								// SlideActivity.class);
								// in.putExtra("index", "1");
								// DataInfo.MESSAGE = message;
								// System.out.println("message"+DataInfo.MESSAGE);
								// EditMSActivity.this.finish();
								// EditMSActivity.this.startActivity(in);
								// // }
								//
								//
								//
								// } else if (choic == 1) {
								DataInfo.DBHelper.delete(Integer.parseInt(map
										.get("id")));
								adapter.notifyDataSetChanged();
								initListView();
								Toast.makeText(EditMSActivity.this, "��ɾ��",
										Toast.LENGTH_SHORT).show();

								// }
								// else if (choic == 2) {
								// Intent intent = new Intent();
								// intent.setClass(EditMSActivity.this,
								// SlideActivity.class);
								// intent.putExtra("index", "0");
								// startActivity(intent);
								//
								// }

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
				return false;

			}
		});
	}

	// ��ȡ����
	public ArrayList<Map<String, String>> getData() {

		return DataInfo.DBHelper.searchAll();

	}

	private class ModelAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {

			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.mslib_item, null);
				View view = convertView;

			}
			notifyDataSetChanged();// ˢ��
			return convertView;
		}
	}

	protected void onRestart() {
		super.onRestart();
		// ˢ���б�!!!!!!!!!
		initListView();
	}

}
