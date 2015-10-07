package com.example.feiyumain;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.data.DataInfo;
import com.example.data.DatabaseHelper;
import com.example.feiyu.R;

//这是滑动的主要工具页面，它可以
public class SlideActivity extends Activity {

	private LocalActivityManager manager = null;
	private ViewPager pager = null;
	private LinearLayout EditMessLayout, InputNumLayout, MessLibLayout,
			MineLayout;
	private ImageView EditMessImg, InputNumImg, MessLibImg, MineImg;
	private TextView EditMessTV, InputNumTV, MessLibTV, MineTV;
	public int currIndex = 1;// 当前页卡编号
	private View EditMessView, InputNumView, MessLibView, MineView;
	private ImageButton lookupmess;

	// private TextView treeCount,forest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pagermain);

		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);

		pager = (ViewPager) findViewById(R.id.id_viewPager);
		lookupmess = (ImageButton) findViewById(R.id.lookuPmessBT);
		
		Intent in = getIntent();
		this.currIndex = Integer.parseInt(in.getStringExtra("index"));
		
		System.out.println("index"+currIndex);
		
		initview();
		initPagerViewer();
		changetextview(currIndex);// 改变图标
	}

	private void initview() {
		EditMessLayout = (LinearLayout) findViewById(R.id.editMessLayout);
		InputNumLayout = (LinearLayout) findViewById(R.id.inputNumLayout);
		MessLibLayout = (LinearLayout) findViewById(R.id.messLibLayout);
		MineLayout = (LinearLayout) findViewById(R.id.mineLayout);

		// ImageView
		EditMessImg = (ImageView) findViewById(R.id.editMessImg);
		InputNumImg = (ImageView) findViewById(R.id.inputNumImg);
		MessLibImg = (ImageView) findViewById(R.id.messLibImg);
		MineImg = (ImageView) findViewById(R.id.mineImg);

		EditMessTV = (TextView) findViewById(R.id.editMessTV);
		InputNumTV = (TextView) findViewById(R.id.inputNumTV);
		MessLibTV = (TextView) findViewById(R.id.messLibTV);
		MineTV = (TextView) findViewById(R.id.mineTV);

		EditMessLayout.setOnClickListener(new MyOnClickListener(0));
		InputNumLayout.setOnClickListener(new MyOnClickListener(1));
		MessLibLayout.setOnClickListener(new MyOnClickListener(2));
		MineLayout.setOnClickListener(new MyOnClickListener(3));
		
		lookupmess.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*			try {
				//获取信息
				getUserInfo();
			} catch (JSONException e) {
				e.printStackTrace();
			}   */
//			String msgcount = DataInfo.DBHelper.query_msgcount("");
				String account = DataInfo.DBHelper.first_account();
				String msgcount = DataInfo.DBHelper.query("msgcount", "name", account);
				Toast.makeText(getApplicationContext(),
						"您的短信剩余：" + msgcount + "条", 5000).show();
				
			}
		});
			
						

	}

	private void initPagerViewer() {
		final ArrayList<View> list = new ArrayList<View>();
		Intent intent = new Intent(this, EditMSActivity.class);
		EditMessView = getView("EditMSActivity", intent);
		list.add(EditMessView);

		Intent intent2 = new Intent(this, EnterPhoneActivity.class);
		InputNumView = getView("EnterPhoneActivity", intent2);
		list.add(InputNumView);

		Intent intent3 = new Intent(this, MSLibraryActivity.class);
		MessLibView = getView("MSLibraryActivity", intent3);
		list.add(MessLibView);

		Intent intent4 = new Intent(this, UserInfoActivity.class);
		MineView = getView("UserInfoActivity", intent4);
		list.add(MineView);

		pager.setAdapter(new MyPagerAdapter(list));
		pager.setCurrentItem(currIndex);
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	public class MyPagerAdapter extends PagerAdapter {
		List<View> list = new ArrayList<View>();

		public MyPagerAdapter(ArrayList<View> list) {
			this.list = list;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ViewPager pViewPager = ((ViewPager) container);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {

			ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.addView(list.get(arg1));
			// changetextview(arg1);//改变图标取消改变~~~~~
			return list.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			currIndex = arg0;
			changetextview(arg0);// 改变图标
			System.out.println("pageID" + arg0);
			setProgress();
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

	}

	protected void onRestart() {
		System.out.println("onRestart====");
		super.onRestart();
		setProgress();
	}

	protected void onStart() {
		System.out.println("onStart====");
		super.onStart();
		setProgress();
	}

	public void setProgress() {
		// 实例化一个对象
		if (DataInfo.DBHelper == null) {
			DataInfo.DBHelper = new DatabaseHelper(getApplicationContext());
			// DataShare.DBHelper.createTable();
			// AddPlanActivity.dbHelper.onUpgrade(AddPlanActivity.dbHelper.getWritableDatabase(),
			// 1, 2);
		}
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			pager.setCurrentItem(index);
			changetextview(index);// 改变图标

		}
	};

	@SuppressLint({ "NewApi", "ResourceAsColor" })
	private void changetextview(int index) {
		resetimgtext();
		switch (index) {
		case 0:
			EditMessImg.setImageDrawable(getResources().getDrawable(
					R.drawable.small_editmess_pressed));
			EditMessTV.setTextColor(R.color.focuschangeTvcolor);
			EditMessLayout
					.setBackgroundResource(R.color.white);
			break;
		case 1:
			InputNumImg.setImageDrawable(getResources().getDrawable(
					R.drawable.small_inputnumber_pressed));
			InputNumTV.setTextColor(R.color.focuschangeTvcolor);
			InputNumLayout
					.setBackgroundResource(R.color.white);
			break;
		case 2:

			MessLibImg.setImageDrawable(getResources().getDrawable(
					R.drawable.small_messlib_pressed));
			MessLibTV.setTextColor(R.color.focuschangeTvcolor);
			MessLibLayout.setBackgroundResource(R.color.white);
			break;
		case 3:
			MineImg.setImageDrawable(getResources().getDrawable(
					R.drawable.small_person_pressed));
			MineTV.setTextColor(R.color.focuschangeTvcolor);
			MineLayout.setBackgroundResource(R.color.white);
			break;
		default:
			break;
		}
	}

//	@SuppressLint("ResourceAsColor")
	private void resetimgtext() {
		EditMessImg.setImageDrawable(getResources().getDrawable(
				R.drawable.small_editmess_normal));
		EditMessTV.setTextColor(Color.parseColor("#ffffff"));
		EditMessLayout.setBackgroundResource(R.color.backgroundcolor);
		InputNumImg.setImageDrawable(getResources().getDrawable(
				R.drawable.small_inputnumber_normal));
		InputNumTV.setTextColor(Color.parseColor("#ffffff"));
		InputNumLayout.setBackgroundResource(R.color.backgroundcolor);
		MessLibImg.setImageDrawable(getResources().getDrawable(
				R.drawable.small_messlib_normal));
		MessLibTV.setTextColor(Color.parseColor("#ffffff"));
		MessLibLayout.setBackgroundResource(R.color.backgroundcolor);
		MineImg.setImageDrawable(getResources().getDrawable(
				R.drawable.small_person_normal));
		MineTV.setTextColor(Color.parseColor("#ffffff"));
		MineLayout.setBackgroundResource(R.color.backgroundcolor);

	}


}
