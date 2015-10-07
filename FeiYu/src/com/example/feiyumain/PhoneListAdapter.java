package com.example.feiyumain;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.data.DataInfo;
import com.example.feiyu.R;

public class PhoneListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private HashMap<String, Object> m;
	public ViewHolder holder;
	public int index = 1;
	public Context context;
	public String newPhoneNum;
	private Activity activity;

	// private int flag;

	public PhoneListAdapter(int flag, Context context) {
		// this.flag = flag;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);

	}

	public int getCount() {
		// TODO Auto-generated method stub
		return DataInfo.listItem.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return DataInfo.listItem.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		holder = new ViewHolder();
		if (convertView == null) {
			// �󶨲���
			convertView = mInflater.inflate(R.layout.phone_item, null);
			/** �õ������ؼ��Ķ��� */
			holder.xuhao = (TextView) convertView.findViewById(R.id.xuhao1);
			holder.phone = (EditText) convertView.findViewById(R.id.phone1);
			holder.delete = (ImageButton) convertView
					.findViewById(R.id.item_delete);

			// ��ViewHolder����
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		/** ����TextView��ʾ�����ݣ������Ǵ���ڶ�̬�����е����� */
		holder.phone.setText(DataInfo.listItem
				.get(DataInfo.listItem.size() - 1 - position).get("Phone")
				.toString());
		holder.xuhao.setText(DataInfo.listItem
				.get(DataInfo.listItem.size() - 1 - position).get("num")
				.toString());

		// ����ǰΪEnterPhoneActivity
		// if (flag == 1) {

		// �޸ĵĵ������
		holder.phone.setOnClickListener(new MyClickListener(holder.phone,
				holder.xuhao, position));

		// ɾ���ĵ������
		holder.delete.setOnClickListener(new MyClickListener(holder.phone,
				holder.xuhao, position));

		// ���ŵĵ������
		holder.xuhao.setOnClickListener(new MyClickListener(holder.phone,
				holder.xuhao, position));
		// }

		return convertView;
	}

	public class MyClickListener implements View.OnClickListener {
		private EditText phone;
		private TextView xuhao;
		private String xh;
		private String phoneNum;
		private int position;

		public MyClickListener(EditText phone, TextView xuhao, int position) {
			this.phone = phone;
			this.xuhao = xuhao;
			this.position = position;
		}

		public void onClick(View v) {
			if (v.getId() == R.id.item_delete) {
				System.out.println("Builder");
				AlertDialog.Builder builder = new Builder(
						EnterPhoneActivity.enterphone_activity);
				initBuild(builder);
			} else {

				xh = xuhao.getText().toString();
				phoneNum = phone.getText().toString();
				if (phoneNum.length() > 10) {
					// System.out.println("jiesu");
					System.out.println("position= " + position + " " + phoneNum
							+ "set");
					m = DataInfo.listItem.get(DataInfo.listItem.size() - 1
							- position);
					m.put("Phone", phoneNum);
					m.put("num", xh);
					DataInfo.listItem.set(DataInfo.listItem.size() - 1
							- position, m);
					Toast.makeText(context.getApplicationContext(), "�޸ĳɹ�",
							Toast.LENGTH_SHORT).show();

					notifyDataSetChanged();
				}
			}

		}

		private void initBuild(Builder builder) {
			builder.setTitle("��ʾ")
					// ���öԻ������
					.setMessage("ȷ��ɾ������ô")
					// ������ʾ������
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (DataInfo.ORDER == 1) {//��Ϊ˳��
										DataInfo.listItem
												.remove(DataInfo.listItem
														.size() - 1 - position);
										if (position != 0) {
											for (int i = DataInfo.listItem
													.size() - position; i < DataInfo.listItem
													.size(); i++) {
												System.out.println("i" + i);
												HashMap<String, Object> m;
												m = DataInfo.listItem.get(i);
												m.put("num", DataInfo.QuHao
														+ (i + 1 + ""));
												DataInfo.listItem.set(i, m);
											}
										}
									}else if(DataInfo.ORDER==0){//��Ϊ�̶�
										DataInfo.listItem
										.remove(DataInfo.listItem
												.size() - 1 - position);
									}
									notifyDataSetChanged();
									Toast.makeText(context, "��ɾ��",
											Toast.LENGTH_SHORT).show();
								}

							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {// ��ӷ��ذ�ť
								public void onClick(DialogInterface dialog,
										int which) {// ��Ӧ�¼�
								}

							}).show();
		}

	}

	public final class ViewHolder {
		public EditText phone;
		public TextView xuhao;
		public ImageButton delete;

	}
}
