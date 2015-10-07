package com.example.userui;

import java.io.File;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.data.DataInfo;
import com.example.feiyu.R;
import com.example.net.HttpRequest;

public class PersonalEditActivity extends Activity {

	private Button editBt;
	private TextView title_txt;
	private ImageButton returnBt;
//	private ImageButton user_picture_edit_BT;
	private EditText name_EditT;
	private EditText address_edit;
	private EditText sendoutarea_EditT;
	private EditText sendcompany_edit;
	private TextView account;

	private String picname;
	private String address;
	private String area;
	private String company;
	
	private static final int PHOTO_REQUEST = 1;
	private static final int CAMERA_REQUEST = 2;
	private static final int PHOTO_CLIP = 3;

	private String account_name = DataInfo.DBHelper.first_account();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_edit);
		initview();

	}

	public void initview() {
		editBt = (Button) findViewById(R.id.editBt);
		title_txt = (TextView) findViewById(R.id.title_txt);
		returnBt = (ImageButton) findViewById(R.id.returnBt);
//		user_picture_edit_BT = (ImageButton) findViewById(R.id.user_picture_edit_BT);

		account = (TextView) findViewById(R.id.account_name_TV);
		name_EditT = (EditText) findViewById(R.id.name_Edit);
		address_edit = (EditText) findViewById(R.id.address_edit);
		sendoutarea_EditT = (EditText) findViewById(R.id.sendoutarea_Edit);
		sendcompany_edit = (EditText) findViewById(R.id.sendcompany_edit);

		editBt.setText("����");
		title_txt.setText("�༭��Ϣ");
		// ��ʼ��
		init();

		editBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//�������������߳�
				new Thread(runnable).start();
//				updatepersoninfo();
//				finish();
			}
		});

		returnBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
		
//	user_picture_edit_BT.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(Intent.ACTION_PICK, null);
//				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//						"image/*");
//				startActivityForResult(intent, PHOTO_REQUEST);
//			}
//		});

	}

	// handler������Ϣ�ص�����
	private Callback callback = new Callback() {
		public boolean handleMessage(Message msg) {
			// ˢ��
			if (msg.what == 0) {
				// �����û���Ϣ
				Toast.makeText(PersonalEditActivity.this, "����ɹ�",
						Toast.LENGTH_SHORT).show();
				updatepersoninfo();
				finish();
				

			} else if (msg.what == 1) {
				Toast.makeText(getApplicationContext(), "��������������",
						Toast.LENGTH_SHORT).show();

			}
			return false;
		}
	};
	// �̷߳���������м����
	private Handler handler = new Handler(callback);

	private Runnable runnable = new Runnable() {

		public void run() {
			while (true) {
				// ���ܴ��̷߳��ʽ����ϵ����
				try {
					String picname_new = name_EditT.getText().toString();
					String address_new = address_edit.getText().toString();
					String area_new = sendoutarea_EditT.getText().toString();
					String company_new = sendcompany_edit.getText().toString();
					HttpRequest httprequest = new HttpRequest();
					boolean flag = httprequest.chgUserInfo(picname_new, address_new,
							area_new, company_new);
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
	
	private void init() {
		
		account.setText(account_name);
		

		picname = DataInfo.DBHelper.query("nicheng", "name", account_name);
		name_EditT.setText(picname);

		address = DataInfo.DBHelper.query("address", "name", account_name);
		address_edit.setText(address);

		area = DataInfo.DBHelper.query("area", "name", account_name);
		sendoutarea_EditT.setText(area);

		company = DataInfo.DBHelper.query("company", "name", account_name);
		sendcompany_edit.setText(company);

	}

	/**
	 * �������� �û��ĸ�����Ϣ�����Ҵ��ص� ��һ���û�����
	 */

	public void updatepersoninfo() {
		// �������ݿ�
		String picname_new = name_EditT.getText().toString();
		String address_new = address_edit.getText().toString();
		String area_new = sendoutarea_EditT.getText().toString();
		String company_new = sendcompany_edit.getText().toString();

		DataInfo.DBHelper.updateUserInfo(account_name, "nicheng",
				picname_new);
		DataInfo.DBHelper.updateUserInfo(account_name, "address",
				address_new);
		DataInfo.DBHelper.updateUserInfo(account_name, "area", area_new);
		DataInfo.DBHelper.updateUserInfo(account_name, "company",
				company_new);
	}
	
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		switch (requestCode) {
//		case CAMERA_REQUEST:
//			switch (resultCode) {
//			case -1://-1��ʾ���ճɹ�
//				File file = new File(Environment.getExternalStorageDirectory()
//						+ "/test.jpg");
//				if (file.exists()) {
//					photoClip(Uri.fromFile(file));
//				}
//				break;
//			default:
//				break;
//			}
//			break;
//		case PHOTO_REQUEST:
//			if (data != null) {
//				photoClip(data.getData());
//			}
//			break;
//		case PHOTO_CLIP:
//			if (data != null) {
//				Bundle extras = data.getExtras();
//				if (extras != null) {
//					Log.w("test", "data");
//					Bitmap photo = extras.getParcelable("data");
//					user_picture_edit_BT.setImageBitmap(photo);
//					DataInfo.photo = photo;
//				}
//			}
//			break;
//		default:
//			break;
//		}
//
//	}
//	
//	private void photoClip(Uri uri) {
//		// ����ϵͳ���Դ���ͼƬ����
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
//		// �������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�
//		intent.putExtra("crop", "true");
//		// aspectX aspectY �ǿ�ߵı���
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//		// outputX outputY �ǲü�ͼƬ���
//		intent.putExtra("outputX", 150);
//		intent.putExtra("outputY", 150);
//		intent.putExtra("return-data", true);
//		startActivityForResult(intent, PHOTO_CLIP);
//	}


}
