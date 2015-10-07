package com.example.feiyumain;

import java.util.HashMap;

import android.widget.LinearLayout.LayoutParams;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.data.DataInfo;
import com.example.feiyu.R;
import com.example.tool.ArrayWheelAdapter;
import com.example.tool.OnWheelChangedListener;
import com.example.tool.WheelView;

public class EnterPhoneActivity extends Activity implements OnClickListener {

	public HashMap<Object, Object> map = null;
	public PhoneListAdapter adapter;
	public ListView phoneList;
	private Button completeBt, setBt;
	private EditText phoneNum;
	public static EnterPhoneActivity enterphone_activity;
	private int flag = 1;
//	private int order = 1;
	public String[] category_str1 = new String[27];
	public String[] category_str2 =new String[2];

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enterphone);
		enterphone_activity = this;
		
		
		initViews();
		initEvents();

	}

	private void initViews() {
		phoneList = (ListView) findViewById(R.id.phonelistView);
		completeBt = (Button) findViewById(R.id.sure);
		setBt = (Button) this.findViewById(R.id.set);
		phoneNum = (EditText) this.findViewById(R.id.enterNumEdit);

		/**
		 * 增加！！！！！！！！！！！！！！！
		 */
		char m = 65;
		for (int i = 0; i < 26; i++) {
			category_str1[i] = new String("   " + m + "   ");
			m++;
			System.out.print(m);
		}
		category_str1[26] = "无";
		category_str2[0]="固定";
		category_str2[1]="顺序";
	}

	private void initEvents() {
		completeBt.setOnClickListener(this);
		setBt.setOnClickListener(this);

		adapter = new PhoneListAdapter(flag, getApplicationContext());// 实例化适配器
		phoneList.setAdapter(adapter);// 绑定适配器

		if (!quhao_set) {
			phoneNum.setFocusable(false);
		}

		phoneNum.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 10) {
					System.out.println("jiesu");
					int length = DataInfo.listItem.size();
					System.out.println("l=" + length);
					HashMap<String, Object> m = new HashMap<String, Object>();
					m.put("Phone", s.toString());
					if (DataInfo.ORDER == 1) // 顺序排序！ +1
					{
						m.put("num", DataInfo.QuHao + (length + 1 + ""));

					} else if (DataInfo.ORDER == 0) // 固定顺序为1
					{
						m.put("num", DataInfo.QuHao + (""));

					}
					DataInfo.listItem.add(m);
					adapter.notifyDataSetChanged();
					phoneNum.setText("");
					phoneNum.setHint("请输入号码");
				}

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});
	}

	public static Boolean quhao_set = false;

	// 按钮监听事件
	@SuppressWarnings("deprecation")
	public void onClick(View v) {

		if (v.getId() == R.id.sure) {

			if (!quhao_set) {
				Toast.makeText(getApplicationContext(), "请先点击设置按钮",
						Toast.LENGTH_SHORT).show();
			} else {

				 if(DataInfo.MESSAGE.length()<=4){
					Toast.makeText(getApplicationContext(), "请先完善设置要发送的短信",
							Toast.LENGTH_SHORT).show();
					Intent in = new Intent(EnterPhoneActivity.this,
							SlideActivity.class);
					in.putExtra("index", "0");
					this.finish();
					this.startActivity(in);
				}else if (DataInfo.listItem.size()==0) {
					Toast.makeText(getApplicationContext(), "请先输入手机号码",
							Toast.LENGTH_SHORT).show();
				} else{
					Intent in = new Intent(EnterPhoneActivity.this,
							ForseeMSActivity.class);
					this.startActivity(in);
				}

			}

		} else if (v.getId() == R.id.set) {
//			if(quhao_set){
//				
//			}

			quhao_set = true;

			final AlertDialog dialog = new AlertDialog.Builder(
					EnterPhoneActivity.this).create();
			dialog.setTitle("请选择区号+区内顺序");

			// 创建布局
			final LinearLayout ll = new LinearLayout(EnterPhoneActivity.this);
			// 设置布局方式：水平
			ll.setOrientation(LinearLayout.HORIZONTAL);

			final WheelView category1 = new WheelView(EnterPhoneActivity.this);
			category1.setVisibleItems(3);
			category1.setCyclic(true);
			category1.setAdapter(new ArrayWheelAdapter<String>(category_str1));

			final WheelView category2 = new WheelView(EnterPhoneActivity.this);
			category2.setVisibleItems(3);
			category2.setCyclic(true);
			category2.setAdapter(new ArrayWheelAdapter<String>(category_str2));
			category2.setCurrentItem(1);
			// 创建参数
			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp1.gravity = Gravity.LEFT;
			// lp1.weight = (float) 0.4;

			LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp2.weight = (float) 0.6;
			lp2.gravity = Gravity.RIGHT;
			lp2.leftMargin = 5;
			lp2.topMargin = 20;
			ll.addView(category1, lp1);
			ll.addView(category2, lp2);
			// 为会话创建确定按钮
			dialog.setButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialog.setButton2("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String cat1 = category_str1[category1.getCurrentItem()];
					String cat2 = category_str2[category2.getCurrentItem()];
					Toast.makeText(EnterPhoneActivity.this,
							"您选择的区号是：" + cat1.charAt(3) + " 顺序方式： " + cat2,
							Toast.LENGTH_SHORT).show();
					DataInfo.QuHao = String.valueOf(cat1.charAt(3));
					if (cat2.equals("固定")) {
						DataInfo.ORDER = 0;
					} else if (cat2.equals("顺序")) {
						DataInfo.ORDER = 1;
					}
					dialog.dismiss();
					// 输入框重新获取焦点
					phoneNum.setFocusable(true);
					phoneNum.setFocusableInTouchMode(true);
					phoneNum.requestFocus();
					phoneNum.requestFocusFromTouch();
					phoneNum.setHint("请输入号码");
				}
			});
			dialog.setView(ll);
			dialog.show();
		}

	}
}
