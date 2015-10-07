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
	private static final String DB_NAME = "message.db"; // 数据库名
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
		// System.out.println("DatabaseHelper构造器-----");
	}

	/**
	 * 自动建表
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
	 * onUpgrade 更新数据库
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table " + TBL_NAME;
		// System.out.println("update");
		db.execSQL(sql);
		// createTable();
	}

	/**
	 * 手动创建数据表
	 */
	public void createTable() {
		SQLiteDatabase db = getWritableDatabase();
		// System.out.println("create a createTable by hand");
		// SQL璇彞
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
			// 擦除原来的账号
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

	// 清空所有发送记录
	public void sendRecord_deleteALL() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id",
				"phone" }, "id>?", new String[] { "-1" }, null, null, null);
		// 绉诲姩娓告爣
		while (cursor.moveToNext()) {
			String id = cursor.getString(cursor.getColumnIndex("id"));
			// System.out.println("id=" + id);
			db.delete(TBL_SENDRECORD_NAME, "id=?", new String[] { id });
		}
	}

	/**
	 * 删除定时发送的记录
	 */
	public void timerSend_delete(String id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id", },
				"id>?", new String[] { "-1" }, null, null, null);
		// 绉诲姩娓告爣
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
	// 修改短信内容
	public void update(int id, String message) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("message", message);
		// 鍦╥d=de 鍒犻櫎杩欎竴琛?
		db.update(TBL_NAME, contentValues, "id=?", new String[] { id + "" });

	}

	// 修改发送结果
	public void sendRecord_update(String phone, String number, String time,
			String result) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("result", result);
		contentValues.put("time", time);
		// 更新
		// System.out.println("修改编号："+number);
		db.update(TBL_SENDRECORD_NAME, contentValues, "phone=? and number=?",
				new String[] { phone, number });

	}

	// 重发修改发送结果
	public void resend_update(String id, String result) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("result", result);
		// 更新
		db.update(TBL_SENDRECORD_NAME, contentValues, "id=?",
				new String[] { id });

	}

	// 根据字段修改信息
	public void updateUserInfo(String name, String key, String value) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(key, value);
		// update
		db.update(TBL_USERINFO_NAME, contentValues, "name=?",
				new String[] { name });
		// System.out.println("修改成功");
	}

	/**
	 * 遍历短信模板表
	 * 
	 */
	public ArrayList<Map<String, String>> searchAll() {
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<Map<String, String>> messages = new ArrayList<Map<String, String>>();

		// 璁剧疆娓告爣
		// System.out.println("建表" + TBL_NAME);
		Cursor cursor = db.query(TBL_NAME, new String[] { "id", "message" },
				"id>?", new String[] { "-1" }, null, null, null);
		// 绉诲姩娓告爣
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

	// 遍历发送记录表
	public ArrayList<Map<String, String>> sendRecord_searchAll() {
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<Map<String, String>> sendRecord = new ArrayList<Map<String, String>>();

		// 璁剧疆娓告爣
		// System.out.println("建表" + TBL_NAME);
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id",
				"number", "phone", "time", "result" }, "id>?",
				new String[] { "-1" }, null, null, null);
		// 绉诲姩娓告爣
		while (cursor.moveToNext()) {
			Map<String, String> mapPlan = new HashMap<String, String>();
			String number = cursor.getString(cursor.getColumnIndex("number"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));
			String result = cursor.getString(cursor.getColumnIndex("result"));
			String time = cursor.getString(cursor.getColumnIndex("time"));

			if (result.equals("定时") && afterTimer(time)) {
				sendRecord_update(phone, number, time, "成功");
				mapPlan.put("number", number);
				mapPlan.put("phone", phone);
				mapPlan.put("time", time);
				mapPlan.put("result", "成功");
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

	// 遍历发送失败记录
	public ArrayList<Map<String, String>> sendFailed_searchAll() {
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<Map<String, String>> sendFailed = new ArrayList<Map<String, String>>();

		// 璁剧疆娓告爣
		// System.out.println("建表" + TBL_NAME);
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id",
				"number", "phone", "time", "result" }, "id>?",
				new String[] { "-1" }, null, null, null);
		// 绉诲姩娓告爣
		while (cursor.moveToNext()) {
			Map<String, String> mapPlan = new HashMap<String, String>();
			String result = cursor.getString(cursor.getColumnIndex("result"));
			if (result.equals("失败")) {
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

	// 遍历定时发送记录
	public ArrayList<Map<String, String>> timerSend_searchAll() {
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<Map<String, String>> timersend = new ArrayList<Map<String, String>>();

		// 璁剧疆娓告爣
		// System.out.println("建表" + TBL_NAME);
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id",
				"number", "phone", "time", "result" }, "id>?",
				new String[] { "-1" }, null, null, null);
		// 绉诲姩娓告爣
		while (cursor.moveToNext()) {
			Map<String, String> mapPlan = new HashMap<String, String>();
			String result = cursor.getString(cursor.getColumnIndex("result"));
			if (result.equals("定时")) {
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

	// 对比系统时间，判断是否时间未到
	private boolean afterTimer(String time) {
		// 分隔时间
		String timer[] = time.split("\n");
		System.out.println("");
		// System.out.println("日期：" + timer[0]);
		// System.out.println("时间：" + timer[1]);
		String dates[] = timer[0].split("/");
		String times[] = timer[1].split(":");
		int timer_year = Integer.parseInt(dates[0]);
		int timer_month = Integer.parseInt(dates[1]);
		int timer_day = Integer.parseInt(dates[2]);
		int timer_hour = Integer.parseInt(times[0]);
		int timer_minute = Integer.parseInt(times[1]);
		int timer_second = Integer.parseInt(times[2]);
		// System.out.println("年：" + timer_year);
		// System.out.println("月：" + timer_month);
		// System.out.println("日：" + timer_day);
		// System.out.println("时：" + timer_hour);
		// System.out.println("分：" + timer_minute);
		// System.out.println("秒：" + timer_second);

		// 当前时间
		Time t = new Time();
		t.setToNow(); // 取得系统时间
		int year = t.year;
		int month = (t.month + 1);
		int day = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;
		// System.out.println("_时：" + hour);
		// System.out.println("_分：" + minute);
		// System.out.println("_秒：" + second);

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

	// 查询用户是否存在
	public boolean query(String name) {

		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.query(TBL_USERINFO_NAME,
				new String[] { "id", "name" }, "id>?", new String[] { "-1" },
				null, null, null);
		while (cursor.moveToNext()) {
			String name2 = cursor.getString(cursor.getColumnIndex("name"));
			// System.out.println("name:" + name2);
			if (name2.equals(name)) {
				// System.out.println("重复query--->find" + name);

				return true;
			}
		}

		return false;
	}

	// 查询短信是否存在
	public boolean query_message(String SMS) {

		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.query(TBL_NAME, new String[] { "id", "message" },
				"id>?", new String[] { "-1" }, null, null, null);
		while (cursor.moveToNext()) {
			String m = cursor.getString(cursor.getColumnIndex("message"));
			// System.out.println("message:" + m);
			if (m.equals(SMS)) {
				// System.out.println("重复query--->find" + m);

				return true;
			}
		}

		return false;
	}

	// 返回第一行记录的用户名
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

	// 查询有几个用户
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

	// 根据关键字段key_type的值查询某字段type的值
	public String query(String type, String key_type, String key_value) {
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.query(TBL_USERINFO_NAME, new String[] { "id",
				"name", "pwd", "msgcount", "token", "nicheng", "address",
				"area", "company" }, "id>?", new String[] { "-1" }, null, null,
				null);
		while (cursor.moveToNext()) {
			// 参考值
			String key_t = cursor.getString(cursor.getColumnIndex(key_type));
			// 查询值
			String _type = cursor.getString(cursor.getColumnIndex(type));
			// System.out.println("查询所得值为:" + _type);
			// 当要参考的值相同时，返回查询值
			if (key_value.equals(key_t)) {
				// System.out.println("query--->find" + key_value);
				// System.out.println("query--->" + _type);
				return _type;
			}
		}

		return "";
	}

	// 根据手机号查询编号
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

	// 统计成功数
	public int sendSuccess_count() {
		SQLiteDatabase db = getReadableDatabase();
		int sum = 0;
		// 璁剧疆娓告爣
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id",
				"result" }, "id>?", new String[] { "-1" }, null, null, null);
		// 绉诲姩娓告爣
		while (cursor.moveToNext()) {
			String result = cursor.getString(cursor.getColumnIndex("result"));
			if (result.equals("成功")) {
				sum++;
			}
		}
		return sum;

	}

	// 统计失败数
	public int sendFail_count() {
		SQLiteDatabase db = getReadableDatabase();
		int sum = 0;
		// 璁剧疆娓告爣
		Cursor cursor = db.query(TBL_SENDRECORD_NAME, new String[] { "id",
				"result" }, "id>?", new String[] { "-1" }, null, null, null);
		// 绉诲姩娓告爣
		while (cursor.moveToNext()) {
			String result = cursor.getString(cursor.getColumnIndex("result"));
			if (result.equals("失败")) {
				sum++;
			}
		}
		return sum;
	}

}