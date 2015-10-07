package com.example.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract.Contacts.Data;
import android.text.format.Time;
import android.widget.Toast;

import com.example.data.DataInfo;

public class HttpRequest {

	public HttpRequest() {
	}

	// 联网服务数据
	private String PATH = "http://115.28.37.46:7772/myApp";
	private HttpPost request = new HttpPost(PATH);

	private HttpResponse HTTPRequest(JSONObject jsonObject)
			throws UnsupportedEncodingException {
		StringEntity se = new StringEntity(jsonObject.toString(), "GBK");
		request.setEntity(se);

		// 发送请求
		HttpClient httpclient = getHttpClient();
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpclient.execute(request);
		} catch (ClientProtocolException e) {
			System.out.println("连接失败");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpResponse;
	}

	private HttpClient getHttpClient() {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
		HttpConnectionParams.setSoTimeout(httpParams, 30000);
		HttpClient httpclient = new DefaultHttpClient(httpParams);
		return httpclient;
	}

	/**
	 * 登录方法
	 */
	public boolean login(String myname, String pwd) throws JSONException,
			IOException {
		/*-----------------一开始写的连接服务端的代码--------------------------------------------------------------------*/

		// 创建一个JSON对象
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			jsonObject.put("index", "login");
			jsonObject.put("name", myname);
			jsonObject.put("pwd", pwd);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JSONObject result = null;
		try {

			// 请求连接
			System.out.println("发送请求……");
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				System.out.println("response =null");
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("发送请求成功");
			}

			// 响应码
			int code = httpResponse.getStatusLine().getStatusCode();

			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				// DataInfo.ResponseCode = code;
				return false;
			} else if (code == 200) {
				// 得到应答的字符串，这也是一个 JSON 格式保存的数据
				String retSrc = null;
				System.out.println("正在获取返回数据！");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// 生成JSON对象
				System.out.println("retSrc" + retSrc);
				result = new JSONObject(retSrc);

			}

			System.out.println("result" + result);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 得到返回的消息
		String type = null;
		String token = null;
		String name = null;
		try {
			type = (String) result.get("type");
			System.out.println("type" + type);
			token = (String) result.get("token");
			System.out.println("token" + token);
			name = (String) result.get("name");
			System.out.println("name" + name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (type.equals("Login_OK")) {
			System.out.println("Login_ok");
			// 口令保存到本地
			DataInfo.DBHelper.userinfo_insert(name, pwd, token, "10", "未设置",
					"未设置", "未设置", "未设置");

			return true;
		} else {

			return false;
		}
	}

	/**
	 * 注册方法
	 */
	public boolean register(String myname, String pwd, String ptcha) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			jsonObject.put("index", "Register");
			jsonObject.put("name", myname);
			jsonObject.put("Psw", pwd);
			jsonObject.put("captcha", ptcha);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject result = null;
		try {

			// 请求连接
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("发送请求成功");
			}

			// 响应码
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// 得到应答的字符串，这也是一个 JSON 格式保存的数据
				String retSrc = null;
				System.out.println("正在获取返回数据！");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// 生成JSON对象
				System.out.println("retSrc" + retSrc);
				result = new JSONObject(retSrc);

			}

			System.out.println("result" + result);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String type = null;
		String name = null;
		try {
			type = (String) result.get("type");
			name = (String) result.get("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (type.equals("Register_OK")) {
			// 回登录界面
			System.out.println("Register_OK");
			return true;
		} else {
			return false;

		}

	}

	/**
	 * 发送验证码的方法
	 * 
	 * @throws JSONException
	 * 
	 */
	public boolean getIdentify(String myname) throws JSONException {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			jsonObject.put("index", "Identification");
			jsonObject.put("name", myname);
			if (DataInfo.forget_identify) {
				jsonObject.put("isChangePsw", "true");
			} else {
				jsonObject.put("isChangePsw", "false");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject result = null;
		try {

			// 请求连接
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("发送请求成功");
			}

			// 响应码
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// 得到应答的字符串，这也是一个 JSON 格式保存的数据
				String retSrc = null;
				System.out.println("正在获取返回数据！");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// 生成JSON对象
				System.out.println("retSrc" + retSrc);
				result = new JSONObject(retSrc);

			}

			System.out.println("result" + result);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 得到返回的消息
		String type = null;
		type = (String) result.get("type");
		System.out.println(type);

		if (type.equals("Identify_OK")) {
			// 更新本地数据库的密码
			System.out.println("iiiiiii");
			// 回登录界面
			return true;

		} else {
			return false;
		}

	}

	/**
	 * 忘记密码的方法
	 * 
	 * @throws JSONException
	 * 
	 */
	public boolean forgetPwd(String myname, String newPwd, String captcha)
			throws JSONException {
		// 创建一个JSON对象
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			jsonObject.put("index", "ChangePsw");
			jsonObject.put("name", myname);
			jsonObject.put("newPsw", newPwd);
			jsonObject.put("captcha", captcha);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject result = null;
		try {

			// 请求连接
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("发送请求成功");
			}
			System.out.println("发送请求成功");

			// 响应码
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// 得到应答的字符串，这也是一个 JSON 格式保存的数据
				String retSrc = null;
				System.out.println("正在获取返回数据！");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// 生成JSON对象
				System.out.println("retSrc" + retSrc);
				result = new JSONObject(retSrc);

			}

			System.out.println("result" + result);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 得到返回的消息
		String type = null;
		String name = null;
		type = (String) result.get("type");

		if (type.equals("ChangePsw_OK")) {
			name = (String) result.get("name");
			// 更新本地数据库的密码
			DataInfo.DBHelper.updateUserInfo(name, "pwd", newPwd);
			System.out.println("changeOK");
			// 回登录界面
			return true;

		} else {
			return false;
		}

	}

	/**
	 * 获取用户信息的方法
	 * 
	 * @throws JSONException
	 * 
	 */
	public boolean getUserInfo() throws JSONException {
		// 实例化一个json对象
		JSONObject jsonObject = null;
		String account = DataInfo.DBHelper.first_account();
		String token = DataInfo.DBHelper.query("token", "name", account);
		jsonObject = new JSONObject();
		jsonObject.put("index", "UserInfo");
		jsonObject.put("name", account);
		jsonObject.put("token", token);

		JSONObject result = null;
		try {

			// 请求连接
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("发送请求成功");
			}

			// 响应码
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// 得到应答的字符串，这也是一个 JSON 格式保存的数据
				String retSrc = null;
				System.out.println("正在获取返回数据！");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// 生成JSON对象
				System.out.println("retSrc" + retSrc);
				result = new JSONObject(retSrc);

			}

			System.out.println("result" + result);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String type = null;
		String name = null;
		String msgcount = null;
		type = (String) result.get("type");

		if (type.equals("UserInfo_OK")) {
			name = (String) result.get("name");
			msgcount = (String) result.get("msgcount");
			// 更新本地数据库
			DataInfo.DBHelper.updateUserInfo(name, "msgcount", msgcount);

			return true;
		} else {
			return false;
		}

	}

	/**
	 * 获取用户所有信息
	 * 
	 * @return
	 * @throws JSONException
	 */
	public boolean getAllUserInfo() throws JSONException {
		// 实例化一个json对象
		JSONObject jsonObject = null;
		String account = DataInfo.DBHelper.first_account();
		String token = DataInfo.DBHelper.query("token", "name", account);
		jsonObject = new JSONObject();
		jsonObject.put("index", "UserInformation");
		jsonObject.put("name", account);
		jsonObject.put("token", token);

		JSONObject result = null;
		try {

			// 请求连接
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("发送请求成功");
			}

			// 响应码
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// 得到应答的字符串，这也是一个 JSON 格式保存的数据
				String retSrc = null;
				System.out.println("正在获取返回数据！");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// 生成JSON对象
				System.out.println("retSrc" + retSrc);
				result = new JSONObject(retSrc);

			}

			System.out.println("result" + result);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String type = null;
		String name = null;
		type = (String) result.get("type");

		if (type.equals("Infornamtion_OK")) {
			name = (String) result.get("name");
			String nickname = result.getString("nickname");
			String address = result.getString("address");
			String area = result.getString("deliverarea");
			String express = result.getString("express");

			// 更新本地数据库
			DataInfo.DBHelper.updateUserInfo(name, "nicheng", nickname);
			// String picname = DataInfo.DBHelper.query("nicheng", "name",name);
			// System.out.println("picname"+picname);
			DataInfo.DBHelper.updateUserInfo(name, "address", address);
			DataInfo.DBHelper.updateUserInfo(name, "area", area);
			DataInfo.DBHelper.updateUserInfo(name, "company", express);

			return true;
		} else {
			return false;
		}

	}

	/**
	 * 更新用户信息
	 * 
	 * @param nickname
	 * @param address
	 * @param deliveryarea
	 * @param express
	 * @return
	 * @throws JSONException
	 */
	public boolean chgUserInfo(String nickname, String address,
			String deliveryarea, String express) throws JSONException {
		// 实例化一个json对象
		JSONObject jsonObject = null;
		// String token = DataInfo.DBHelper.query("token", "name",
		// DataInfo.ACCOUNT);
		String account = DataInfo.DBHelper.first_account();
		jsonObject = new JSONObject();
		jsonObject.put("index", "ChgInformation");
		jsonObject.put("name", account);

		jsonObject.put("nickname", nickname);
		jsonObject.put("address", address);
		jsonObject.put("deliveryarea", deliveryarea);
		jsonObject.put("express", express);
		// jsonObject.put("token", token);

		JSONObject result = null;
		try {

			// 请求连接
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				System.out.println("lianjieyouwen");
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("发送请求成功");
			}

			// 响应码
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// 得到应答的字符串，这也是一个 JSON 格式保存的数据
				String retSrc = null;
				System.out.println("正在获取返回数据！");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// 生成JSON对象
				System.out.println("retSrc" + retSrc);
				result = new JSONObject(retSrc);

			}

			System.out.println("result" + result);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String type = null;
		String name = null;
		String msgcount = null;
		type = (String) result.get("type");

		if (type.equals("Chgrmation_OK")) {
			name = (String) result.get("name");
			// 更新本地数据库
			// DataInfo.DBHelper.updateUserInfo(name, "msgcount", msgcount);

			return true;
		} else {
			return false;
		}

	}

	/**
	 * 立即发送信息
	 * 
	 * @return
	 * @throws JSONException
	 */

	public Boolean sendMessage(String message) throws JSONException {
		// 实例化一个json对象
		JSONObject jsonObject = null;

		String phones = "";
		String number = "";

		for (int i = 0; i < DataInfo.sendlist.size(); i++) {
			String phone = DataInfo.sendlist.get(i).get("Phone").toString();
			phones += phone;
			if (i != (DataInfo.sendlist.size() - 1)) {
				phones += "&&";
			}
		}

		for (int i = 0; i < DataInfo.sendlist.size(); i++) {
			String num = DataInfo.sendlist.get(i).get("num").toString();
			number += num;
			System.out.println("num" + num);
			if (i != (DataInfo.sendlist.size() - 1)) {
				number += "&&";
			}
		}

		System.out.println("message" + message);
		String account = DataInfo.DBHelper.first_account();
		System.out.println("account" + account);
		String token = DataInfo.DBHelper.query("token", "name", account);
		System.out.println("token" + token);
		jsonObject = new JSONObject();
		jsonObject.put("index", "Send");
		jsonObject.put("name", account);
		jsonObject.put("token", token);
		jsonObject.put("template", message);
		jsonObject.put("recipient", phones);
		jsonObject.put("number", number);

		JSONObject result = null;
		try {

			// 请求连接
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("发送请求成功");
				// 数据存入记录表
				System.out.println("发送号码个数：" + DataInfo.sendlist.size());
				if (DataInfo.RESEND_FLAG) {
					// System.out.println("sendRecord_OK");
					for (int i = 0; i < DataInfo.RESEND_ID.size(); i++) {
						DataInfo.DBHelper.resend_update(
								DataInfo.RESEND_ID.get(i), "成功");
					}
					DataInfo.RESEND_FLAG = false;
				} else {
					System.out.println("sendRecord_OK");
					for (int i = 0; i < DataInfo.sendlist.size(); i++) {

						DataInfo.DBHelper.sendRecord_insert(DataInfo.sendlist
								.get(i).get("num").toString(),
								DataInfo.sendlist.get(i).get("Phone")
										.toString(), getTime(), "成功");
					}
				}
			}

			// 响应码
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// 得到应答的字符串，这也是一个 JSON 格式保存的数据
				String retSrc = null;
				System.out.println("正在获取返回数据！");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// 生成JSON对象
				System.out.println("retSrc" + retSrc);
				result = new JSONObject(retSrc);

			}

			System.out.println("result" + result);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 得到返回的消息
		String type = null;
		String name = null;
		String failList = null;

		try {
			type = (String) result.get("type");
			name = (String) result.get("name");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (type.equals("Send_OK")) {
			name = (String) result.get("name");
			DataInfo.SEND_type = "ALL_SEND";

			return true;

		}
		// 部分信息有误
		else if (type.equals("Send_OK_D")) {
			System.out.println("bufen");

			name = (String) result.get("name");
			// 有误的序号
			String msg_index = result.getString("msg_index");
			// 有误的号码
			failList = result.getString("fail");

			DataInfo.faillist_Index = msg_index.split("&&");
			DataInfo.failList = failList.split("&&");
			// 修改发送状态!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!服务器问题
			for (int i = 0; i < DataInfo.failList.length; i++) {
				System.out.println("失败编号" + DataInfo.faillist_Index[i]);
				DataInfo.DBHelper.sendRecord_update(DataInfo.failList[i],
						DataInfo.faillist_Index[i], getTime(), "失败");
			}

			DataInfo.SEND_type = "_SEND";
			return true;
		} else {
			// 余额不足
			String errCode = result.getString("errCode");
			DataInfo.ERR_CODE = errCode;
			return false;
		}

	}

	/**
	 * 定时发送信息
	 * 
	 * @return
	 * @throws JSONException
	 */

	public Boolean Regularly_sendMessage(String message, String timer_hours,
			String timer_minutes) throws JSONException {
		// 实例化一个json对象
		JSONObject jsonObject = null;

		String phones = "";
		String number = "";

		for (int i = 0; i < DataInfo.sendlist.size(); i++) {
			String phone = DataInfo.sendlist.get(i).get("Phone").toString();
			phones += phone;
			if (i != (DataInfo.sendlist.size() - 1)) {
				phones += "&&";
			}
		}

		for (int i = 0; i < DataInfo.sendlist.size(); i++) {
			String num = DataInfo.sendlist.get(i).get("num").toString();
			number += num;
			if (i != (DataInfo.sendlist.size() - 1)) {
				number += "&&";
			}
		}
		 System.out.println("号码" + phones);
		 System.out.println("编号" + number);
		System.out.println("定时信息" + message);
		String account = DataInfo.DBHelper.first_account();
		String token = DataInfo.DBHelper.query("token", "name", account);
		jsonObject = new JSONObject();
		jsonObject.put("index", "RegularlySend");
		jsonObject.put("name", account);
		// jsonObject.put("token", token);
		jsonObject.put("message", message);
		jsonObject.put("recipient", phones);
		jsonObject.put("number", number);
		jsonObject.put("timerhours", timer_hours);
		jsonObject.put("timerminutes", timer_minutes);

		JSONObject result = null;
		try {

			// 请求连接
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("定时发送请求成功");
				System.out.println("RESEND_FLAG"+DataInfo.RESEND_FLAG);
				// 数据存入记录表
				if (DataInfo.RESEND_FLAG) {
					 System.out.println("sendRecord_OK");
//					for (int i = 0; i < DataInfo.RESEND_ID.size(); i++) {
//						DataInfo.DBHelper.resend_update(
//								DataInfo.RESEND_ID.get(i), "定时");
//					}
					DataInfo.RESEND_FLAG = false;
				} else {
					System.out.println("发送记录：" + DataInfo.sendlist.size());
					for (int i = 0; i < DataInfo.sendlist.size(); i++) {
						Time t = new Time();
						String time = "";
						t.setToNow(); // 取得系统时间
						String year = t.year + "";
						String month = (t.month + 1) + "";
						String date = t.monthDay + "";
						String dates = year + "/" + month + "/" + date + "\n";
						DataInfo.DBHelper.sendRecord_insert(DataInfo.sendlist
								.get(i).get("num").toString(),
								DataInfo.sendlist.get(i).get("Phone")
										.toString(), dates + timer_hours + ":"
										+ timer_minutes + ":0", "定时");
					}
				}

			}

			// 响应码
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// 得到应答的字符串，这也是一个 JSON 格式保存的数据
				String retSrc = null;
				System.out.println("正在获取返回数据！");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// 生成JSON对象
				System.out.println("retSrc" + retSrc);
				result = new JSONObject(retSrc);

			}

			System.out.println("result" + result);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 得到返回的消息
		String type = null;
		String name = null;
		String failList = null;

		try {
			type = (String) result.get("type");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (type.equals("Send_OK")) {
			DataInfo.SEND_type = "ALL_SEND";
			return true;
			
		}  else {
			// 余额不足
			String errCode = result.getString("errCode");
			DataInfo.ERR_CODE = errCode;
			return false;
		}

	}

	// 获取系统时间
	private String getTime() {
		// 时间
		Time t = new Time();
		String time = "";
		t.setToNow(); // 取得系统时间
		String year = t.year + "";
		String month = (t.month + 1) + "";
		String date = t.monthDay + "";
		String hour = t.hour + ""; // 0-23
		String minute = t.minute + "";
		String second = t.second + "";
		time = year + "/" + month + "/" + date + "\n" + hour + ":" + minute
				+ ":" + second;

		return time;
	}
}
