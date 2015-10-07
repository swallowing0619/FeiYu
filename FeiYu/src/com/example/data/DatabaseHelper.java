package com.example.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;

/**
 * SQLite
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "message.db"; // ���ݿ���
	private static String TBL_NAME = "template";
	private static String TBL_USERINFO_NAME = "userinfo";
	private static String TBL_SENDRECORD_NAME = "sendrecord";
	private static final String SQLstring = "(id INTEGER PRIMARY KEY, message varchar)";
	private static final String USERINFO_SQLstring = "(id INTEGER PRIMARY KEY,name varchar,"
			+ "pwd varchar,token varchar,msgcount varchar,nicheng varchar,"
			+ "address varchar,area varchar,company varchar)";
	private static final String SENDRECORD_SQLstring = "(id INTEGER PRIMARY KEY,number varchar,"
			+ "phone varchar,time varchar,result varchar)";

	/**
	 * @param
	 */
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		// System.out.println("DatabaseHelper������-----");
	}

	/**
	 * �Զ�����
	 * 
	 * @param SQLiteDatabase
	 */
	public void onCreate(SQLiteDatabase db) {
		// System.out.println("create table");
		// SQL
		String sql = "CREATE TABLE " + TBL_NAME + SQLstring;
		db.execSQL(sql);
		String sql_userinfo = "CREATE TABLE " + TBL_USERINFO_NAME
				+ USERINFO_SQLstring;
		db.execSQL(sql_userinfo);
		String sql_sendRecord = "CREATE TABLE " + TBL_SENDRECORD_NAME
				+ SENDRECORD_SQLstring;
		db.execSQL(sql_sendRecord);
	}

	/**
	 * onUpgrade �������ݿ�
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table " + TBL_NAME;
		// System.out.println("update");
		db.execSQL(sql);
		// createTable();
	}

	/**
	 * �ֶ��������ݱ�
	 */
	public void createTable() {
		SQLiteDatabase db = getWritableDatabase();
		// System.out.println("create a createTable by hand");
		// SQL语句
		String sql = "CREATE TABLE " + TBL_NAME + SQLstring;
		// System.out.println(sql);
		try {
			db.execSQL(sql);
		} catch (Exception ex) {
		}
	}

	/**
	 * insert
	 * 
	 * @param message
	 */
	public void insert(String message) {
		// SQLiteDatabase
		SQLiteDatabase db = getWritableDatabase();
		// ContentValues
		ContentValues contentValues = new ContentValues();
		contentValues.put("message", message);
		// System.out.println("insert ---- message=" + message);
		db.insert(TBL_NAME, null, contentValues);

	}

	public void userinfo_insert(String name, String pwd, String token,
			String msgcount, String nicheng, String address, String area,
			String company) {

		SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		if (query(name)) {
			// ����ԭ�����˺�
			userinfo_delete(name);

			contentValues.put("name", name);
			contentValues.put("pwd", pwd);
			contentValues.put("token", token);
			contentValues.put("msgcount", msgcount);
			contentValues.put("nicheng", nicheng);
			contentValues.put("address", address);
			contentValues.put("area", area);
			contentValues.put("company", company);

			// System.out.println("insert ---- name=" + name);
			// System.out.println("insert ---- pwd=" + pwd);
			// System.out.println("insert ---- token=" + token);
			// System.out.println("insert ---- msgcount=" + msgcount + nicheng
			// + address + area + company);
			db.insert(TBL_USERINFO_NAME, null, contentValues);

		} else if (!query(name)) {

			contentValues.put("name", name);
			contentValues.put("pwd", pwd);
			contentValues.put("token", token);
			contentValues.put("msgcount", msgcount);
			contentValues.put("nicheng", nicheng);
			contentValues.put("address", address);
			contentValues.put("area", area);
			contentValues.put("company", company);

			// System.out.println("insert ---- name=" + name);
			// System.out.println("insert ---- pwd=" + pwd);
			// System.out.println("insert ---- token=" + token);
			// System.out.println("insert ---- msgcount=" + msgcount + nicheng
			// + address + area + company);
			db.insert(TBL_USERINFO_NAME, null, contentValues);
		}
	}

	public void sendRecord_insert(String number, String phone, String time,
			String result) {
		// SQLiteDatabase
		SQLiteDatabase db = getWritableDatabase();
		// ContentValues
		ContentValues contentValues = new ContentValues();
		contentValues.put("number", number);
		contentValues.put("phone", phone);
		contentValues.put("time", time);
		contentValues.put("result", result);

		db.insert(TBL_SENDRECORD_NAME, null, contentValues);
	}

	/**
	 * delete
	 * 
	 * @param id
	 */

	public void delete(int id) {
		// System.out.println("id=" + id);
		SQLiteDatabase db = getWritableDatabase();
		// ?
		db.delete(TBL_NAME, "id=?", new String[] { id + "" });

	}

	public void userinfo_delete(String name) {
		SQLiteDatabase db = getWritableDatabase();

		db.delete(TBL_USERINFO_NAME, "name=?", new String[] { name });
	}

	// ������з��ͼ�¼
	public void sendRecord_deleteALL() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id",
				"phone" }, "id>?", new String[] { "-1" }, null, null, null);
		// 移动游标
		while (cursor.moveToNext()) {
			String id = cursor.getString(cursor.getColumnIndex("id"));
			// System.out.println("id=" + id);
			db.delete(TBL_SENDRECORD_NAME, "id=?", new String[] { id });
		}
	}

	/**
	 * ɾ����ʱ���͵ļ�¼
	 */
	public void timerSend_delete(String id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id", },
				"id>?", new String[] { "-1" }, null, null, null);
		// 移动游标
		while (cursor.moveToNext()) {
			String id2 = cursor.getString(cursor.getColumnIndex("id"));
			if (id.equals(id2)) {
				db.delete(TBL_SENDRECORD_NAME, "id=?", new String[] { id });
				return;
			}
		}
	}

	/**
	 * update
	 * 
	 * @param id
	 * @param message
	 */
	// �޸Ķ�������
	public void update(int id, String message) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("message", message);
		// 在id=de 删除这一�?
		db.update(TBL_NAME, contentValues, "id=?", new String[] { id + "" });

	}

	// �޸ķ��ͽ��
	public void sendRecord_update(String phone, String number, String time,
			String result) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("result", result);
		contentValues.put("time", time);
		// ����
		// System.out.println("�޸ı�ţ�"+number);
		db.update(TBL_SENDRECORD_NAME, contentValues, "phone=? and number=?",
				new String[] { phone, number });

	}

	// �ط��޸ķ��ͽ��
	public void resend_update(String id, String result) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("result", result);
		// ����
		db.update(TBL_SENDRECORD_NAME, contentValues, "id=?",
				new String[] { id });

	}

	// �����ֶ��޸���Ϣ
	public void updateUserInfo(String name, String key, String value) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(key, value);
		// update
		db.update(TBL_USERINFO_NAME, contentValues, "name=?",
				new String[] { name });
		// System.out.println("�޸ĳɹ�");
	}

	/**
	 * ��������ģ���
	 * 
	 */
	public ArrayList<Map<String, String>> searchAll() {
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<Map<String, String>> messages = new ArrayList<Map<String, String>>();

		// 设置游标
		// System.out.println("����" + TBL_NAME);
		Cursor cursor = db.query(TBL_NAME, new String[] { "id", "message" },
				"id>?", new String[] { "-1" }, null, null, null);
		// 移动游标
		while (cursor.moveToNext()) {
			Map<String, String> mapPlan = new HashMap<String, String>();
			String id = cursor.getString(cursor.getColumnIndex("id"));
			mapPlan.put("id", id);
			String message2 = cursor
					.getString(cursor.getColumnIndex("message"));
			mapPlan.put("message", message2);

			messages.add(mapPlan);
		}
		return messages;

	}

	// �������ͼ�¼��
	public ArrayList<Map<String, String>> sendRecord_searchAll() {
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<Map<String, String>> sendRecord = new ArrayList<Map<String, String>>();

		// 设置游标
		// System.out.println("����" + TBL_NAME);
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id",
				"number", "phone", "time", "result" }, "id>?",
				new String[] { "-1" }, null, null, null);
		// 移动游标
		while (cursor.moveToNext()) {
			Map<String, String> mapPlan = new HashMap<String, String>();
			String number = cursor.getString(cursor.getColumnIndex("number"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));
			String result = cursor.getString(cursor.getColumnIndex("result"));
			String time = cursor.getString(cursor.getColumnIndex("time"));

			if (result.equals("��ʱ") && afterTimer(time)) {
				sendRecord_update(phone, number, time, "�ɹ�");
				mapPlan.put("number", number);
				mapPlan.put("phone", phone);
				mapPlan.put("time", time);
				mapPlan.put("result", "�ɹ�");
				sendRecord.add(mapPlan);
			}
			// System.out.println("number:" + number);
			else {
				mapPlan.put("number", number);
				mapPlan.put("phone", phone);
				mapPlan.put("time", time);
				mapPlan.put("result", result);
				sendRecord.add(mapPlan);
			}

		}
		return sendRecord;
	}

	// ��������ʧ�ܼ�¼
	public ArrayList<Map<String, String>> sendFailed_searchAll() {
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<Map<String, String>> sendFailed = new ArrayList<Map<String, String>>();

		// 设置游标
		// System.out.println("����" + TBL_NAME);
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id",
				"number", "phone", "time", "result" }, "id>?",
				new String[] { "-1" }, null, null, null);
		// 移动游标
		while (cursor.moveToNext()) {
			Map<String, String> mapPlan = new HashMap<String, String>();
			String result = cursor.getString(cursor.getColumnIndex("result"));
			if (result.equals("ʧ��")) {
				String id = cursor.getString(cursor.getColumnIndex("id"));
				String number = cursor.getString(cursor
						.getColumnIndex("number"));
				String phone = cursor.getString(cursor.getColumnIndex("phone"));
				String time = cursor.getString(cursor.getColumnIndex("time"));
				mapPlan.put("id", id);
				mapPlan.put("number", number);
				mapPlan.put("phone", phone);
				mapPlan.put("time", time);
				mapPlan.put("result", result);
				sendFailed.add(mapPlan);
			}
		}
		return sendFailed;
	}

	// ������ʱ���ͼ�¼
	public ArrayList<Map<String, String>> timerSend_searchAll() {
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<Map<String, String>> timersend = new ArrayList<Map<String, String>>();

		// 设置游标
		// System.out.println("����" + TBL_NAME);
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id",
				"number", "phone", "time", "result" }, "id>?",
				new String[] { "-1" }, null, null, null);
		// 移动游标
		while (cursor.moveToNext()) {
			Map<String, String> mapPlan = new HashMap<String, String>();
			String result = cursor.getString(cursor.getColumnIndex("result"));
			if (result.equals("��ʱ")) {
				String id = cursor.getString(cursor.getColumnIndex("id"));
				String number = cursor.getString(cursor
						.getColumnIndex("number"));
				String phone = cursor.getString(cursor.getColumnIndex("phone"));
				String timer = cursor.getString(cursor.getColumnIndex("time"));
				if (!afterTimer(timer)) {

					mapPlan.put("id", id);
					mapPlan.put("number", number);
					mapPlan.put("phone", phone);
					mapPlan.put("time", timer);
					mapPlan.put("result", result);
					timersend.add(mapPlan);
				}
			}
		}
		return timersend;
	}

	// �Ա�ϵͳʱ�䣬�ж��Ƿ�ʱ��δ��
	private boolean afterTimer(String time) {
		// �ָ�ʱ��
		String timer[] = time.split("\n");
		System.out.println("");
		// System.out.println("���ڣ�" + timer[0]);
		// System.out.println("ʱ�䣺" + timer[1]);
		String dates[] = timer[0].split("/");
		String times[] = timer[1].split(":");
		int timer_year = Integer.parseInt(dates[0]);
		int timer_month = Integer.parseInt(dates[1]);
		int timer_day = Integer.parseInt(dates[2]);
		int timer_hour = Integer.parseInt(times[0]);
		int timer_minute = Integer.parseInt(times[1]);
		int timer_second = Integer.parseInt(times[2]);
		// System.out.println("�꣺" + timer_year);
		// System.out.println("�£�" + timer_month);
		// System.out.println("�գ�" + timer_day);
		// System.out.println("ʱ��" + timer_hour);
		// System.out.println("�֣�" + timer_minute);
		// System.out.println("�룺" + timer_second);

		// ��ǰʱ��
		Time t = new Time();
		t.setToNow(); // ȡ��ϵͳʱ��
		int year = t.year;
		int month = (t.month + 1);
		int day = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;
		// System.out.println("_ʱ��" + hour);
		// System.out.println("_�֣�" + minute);
		// System.out.println("_�룺" + second);

		if (year > timer_year) {
			return true;
		} else if (year == timer_year && month > timer_month) {
			return true;
		} else if (year == timer_year && month == timer_month
				&& day > timer_day) {
			return true;
		} else if (year == timer_year && month == timer_month
				&& day == timer_day && hour > timer_hour) {
			return true;
		} else if (year == timer_year && month == timer_month
				&& day == timer_day && hour == timer_hour
				&& minute > timer_minute) {
			return true;
		} else if (year == timer_year && month == timer_month
				&& day == timer_day && hour == timer_hour
				&& minute == timer_minute && second > timer_second) {
			return true;
		} else {
			System.out.println("after");
			return false;
		}

	}

	// ��ѯ�û��Ƿ����
	public boolean query(String name) {

		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.query(TBL_USERINFO_NAME,
				new String[] { "id", "name" }, "id>?", new String[] { "-1" },
				null, null, null);
		while (cursor.moveToNext()) {
			String name2 = cursor.getString(cursor.getColumnIndex("name"));
			// System.out.println("name:" + name2);
			if (name2.equals(name)) {
				// System.out.println("�ظ�query--->find" + name);

				return true;
			}
		}

		return false;
	}

	// ��ѯ�����Ƿ����
	public boolean query_message(String SMS) {

		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.query(TBL_NAME, new String[] { "id", "message" },
				"id>?", new String[] { "-1" }, null, null, null);
		while (cursor.moveToNext()) {
			String m = cursor.getString(cursor.getColumnIndex("message"));
			// System.out.println("message:" + m);
			if (m.equals(SMS)) {
				// System.out.println("�ظ�query--->find" + m);

				return true;
			}
		}

		return false;
	}

	// ���ص�һ�м�¼���û���
	public String first_account() {

		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.query(TBL_USERINFO_NAME,
				new String[] { "id", "name" }, "id>?", new String[] { "-1" },
				null, null, null);

		if (cursor.moveToNext()) {
			String name2 = cursor.getString(cursor.getColumnIndex("name"));
			return name2;
		}
		return "";
	}

	// ��ѯ�м����û�
	public int query_sum() {

		SQLiteDatabase db = getReadableDatabase();
		int i = 0;
		Cursor cursor = db.query(TBL_USERINFO_NAME,
				new String[] { "id", "name" }, "id>?", new String[] { "-1" },
				null, null, null);
		while (cursor.moveToNext()) {
			String name2 = cursor.getString(cursor.getColumnIndex("name"));
			// System.out.println("name:" + name2);
			i++;
		}

		return i;
	}

	// ���ݹؼ��ֶ�key_type��ֵ��ѯĳ�ֶ�type��ֵ
	public String query(String type, String key_type, String key_value) {
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.query(TBL_USERINFO_NAME, new String[] { "id",
				"name", "pwd", "msgcount", "token", "nicheng", "address",
				"area", "company" }, "id>?", new String[] { "-1" }, null, null,
				null);
		while (cursor.moveToNext()) {
			// �ο�ֵ
			String key_t = cursor.getString(cursor.getColumnIndex(key_type));
			// ��ѯֵ
			String _type = cursor.getString(cursor.getColumnIndex(type));
			// System.out.println("��ѯ����ֵΪ:" + _type);
			// ��Ҫ�ο���ֵ��ͬʱ�����ز�ѯֵ
			if (key_value.equals(key_t)) {
				// System.out.println("query--->find" + key_value);
				// System.out.println("query--->" + _type);
				return _type;
			}
		}

		return "";
	}

	// �����ֻ��Ų�ѯ���
	public ArrayList<Map<String, String>> query_number(String phone) {
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<Map<String, String>> query_record = new ArrayList<Map<String, String>>();
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id",
				"number", "phone", "time", "result" }, "id>?",
				new String[] { "-1" }, null, null, null);
		while (cursor.moveToNext()) {
			String phone2 = cursor.getString(cursor.getColumnIndex("phone"));
			if (phone.equals(phone2)) {
				Map<String, String> mapPlan = new HashMap<String, String>();
				String number = cursor.getString(cursor
						.getColumnIndex("number"));
				String time = cursor.getString(cursor.getColumnIndex("time"));
				String result = cursor.getString(cursor
						.getColumnIndex("result"));
				mapPlan.put("number", number);
				mapPlan.put("phone", phone2);
				mapPlan.put("time", time);
				mapPlan.put("result", result);
				query_record.add(mapPlan);
			}
		}

		return query_record;
	}

	// ͳ�Ƴɹ���
	public int sendSuccess_count() {
		SQLiteDatabase db = getReadableDatabase();
		int sum = 0;
		// 设置游标
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id",
				"result" }, "id>?", new String[] { "-1" }, null, null, null);
		// 移动游标
		while (cursor.moveToNext()) {
			String result = cursor.getString(cursor.getColumnIndex("result"));
			if (result.equals("�ɹ�")) {
				sum++;
			}
		}
		return sum;

	}

	// ͳ��ʧ����
	public int sendFail_count() {
		SQLiteDatabase db = getReadableDatabase();
		int sum = 0;
		// 设置游标
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id",
				"result" }, "id>?", new String[] { "-1" }, null, null, null);
		// 移动游标
		while (cursor.moveToNext()) {
			String result = cursor.getString(cursor.getColumnIndex("result"));
			if (result.equals("ʧ��")) {
				sum++;
			}
		}
		return sum;
	}

}