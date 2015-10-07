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

	// ������������
	private String PATH = "http://115.28.37.46:7772/myApp";
	private HttpPost request = new HttpPost(PATH);

	private HttpResponse HTTPRequest(JSONObject jsonObject)
			throws UnsupportedEncodingException {
		StringEntity se = new StringEntity(jsonObject.toString(), "GBK");
		request.setEntity(se);

		// ��������
		HttpClient httpclient = getHttpClient();
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpclient.execute(request);
		} catch (ClientProtocolException e) {
			System.out.println("����ʧ��");
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
	 * ��¼����
	 */
	public boolean login(String myname, String pwd) throws JSONException,
			IOException {
		/*-----------------һ��ʼд�����ӷ���˵Ĵ���--------------------------------------------------------------------*/

		// ����һ��JSON����
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

			// ��������
			System.out.println("�������󡭡�");
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				System.out.println("response =null");
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("��������ɹ�");
			}

			// ��Ӧ��
			int code = httpResponse.getStatusLine().getStatusCode();

			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				// DataInfo.ResponseCode = code;
				return false;
			} else if (code == 200) {
				// �õ�Ӧ����ַ�������Ҳ��һ�� JSON ��ʽ���������
				String retSrc = null;
				System.out.println("���ڻ�ȡ�������ݣ�");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// ����JSON����
				System.out.println("retSrc" + retSrc);
				result = new JSONObject(retSrc);

			}

			System.out.println("result" + result);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// �õ����ص���Ϣ
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
			// ����浽����
			DataInfo.DBHelper.userinfo_insert(name, pwd, token, "10", "δ����",
					"δ����", "δ����", "δ����");

			return true;
		} else {

			return false;
		}
	}

	/**
	 * ע�᷽��
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

			// ��������
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("��������ɹ�");
			}

			// ��Ӧ��
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// �õ�Ӧ����ַ�������Ҳ��һ�� JSON ��ʽ���������
				String retSrc = null;
				System.out.println("���ڻ�ȡ�������ݣ�");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// ����JSON����
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
			// �ص�¼����
			System.out.println("Register_OK");
			return true;
		} else {
			return false;

		}

	}

	/**
	 * ������֤��ķ���
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

			// ��������
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("��������ɹ�");
			}

			// ��Ӧ��
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// �õ�Ӧ����ַ�������Ҳ��һ�� JSON ��ʽ���������
				String retSrc = null;
				System.out.println("���ڻ�ȡ�������ݣ�");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// ����JSON����
				System.out.println("retSrc" + retSrc);
				result = new JSONObject(retSrc);

			}

			System.out.println("result" + result);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// �õ����ص���Ϣ
		String type = null;
		type = (String) result.get("type");
		System.out.println(type);

		if (type.equals("Identify_OK")) {
			// ���±������ݿ������
			System.out.println("iiiiiii");
			// �ص�¼����
			return true;

		} else {
			return false;
		}

	}

	/**
	 * ��������ķ���
	 * 
	 * @throws JSONException
	 * 
	 */
	public boolean forgetPwd(String myname, String newPwd, String captcha)
			throws JSONException {
		// ����һ��JSON����
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

			// ��������
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("��������ɹ�");
			}
			System.out.println("��������ɹ�");

			// ��Ӧ��
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// �õ�Ӧ����ַ�������Ҳ��һ�� JSON ��ʽ���������
				String retSrc = null;
				System.out.println("���ڻ�ȡ�������ݣ�");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// ����JSON����
				System.out.println("retSrc" + retSrc);
				result = new JSONObject(retSrc);

			}

			System.out.println("result" + result);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// �õ����ص���Ϣ
		String type = null;
		String name = null;
		type = (String) result.get("type");

		if (type.equals("ChangePsw_OK")) {
			name = (String) result.get("name");
			// ���±������ݿ������
			DataInfo.DBHelper.updateUserInfo(name, "pwd", newPwd);
			System.out.println("changeOK");
			// �ص�¼����
			return true;

		} else {
			return false;
		}

	}

	/**
	 * ��ȡ�û���Ϣ�ķ���
	 * 
	 * @throws JSONException
	 * 
	 */
	public boolean getUserInfo() throws JSONException {
		// ʵ����һ��json����
		JSONObject jsonObject = null;
		String account = DataInfo.DBHelper.first_account();
		String token = DataInfo.DBHelper.query("token", "name", account);
		jsonObject = new JSONObject();
		jsonObject.put("index", "UserInfo");
		jsonObject.put("name", account);
		jsonObject.put("token", token);

		JSONObject result = null;
		try {

			// ��������
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("��������ɹ�");
			}

			// ��Ӧ��
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// �õ�Ӧ����ַ�������Ҳ��һ�� JSON ��ʽ���������
				String retSrc = null;
				System.out.println("���ڻ�ȡ�������ݣ�");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// ����JSON����
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
			// ���±������ݿ�
			DataInfo.DBHelper.updateUserInfo(name, "msgcount", msgcount);

			return true;
		} else {
			return false;
		}

	}

	/**
	 * ��ȡ�û�������Ϣ
	 * 
	 * @return
	 * @throws JSONException
	 */
	public boolean getAllUserInfo() throws JSONException {
		// ʵ����һ��json����
		JSONObject jsonObject = null;
		String account = DataInfo.DBHelper.first_account();
		String token = DataInfo.DBHelper.query("token", "name", account);
		jsonObject = new JSONObject();
		jsonObject.put("index", "UserInformation");
		jsonObject.put("name", account);
		jsonObject.put("token", token);

		JSONObject result = null;
		try {

			// ��������
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("��������ɹ�");
			}

			// ��Ӧ��
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// �õ�Ӧ����ַ�������Ҳ��һ�� JSON ��ʽ���������
				String retSrc = null;
				System.out.println("���ڻ�ȡ�������ݣ�");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// ����JSON����
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

			// ���±������ݿ�
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
	 * �����û���Ϣ
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
		// ʵ����һ��json����
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

			// ��������
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				System.out.println("lianjieyouwen");
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("��������ɹ�");
			}

			// ��Ӧ��
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// �õ�Ӧ����ַ�������Ҳ��һ�� JSON ��ʽ���������
				String retSrc = null;
				System.out.println("���ڻ�ȡ�������ݣ�");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// ����JSON����
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
			// ���±������ݿ�
			// DataInfo.DBHelper.updateUserInfo(name, "msgcount", msgcount);

			return true;
		} else {
			return false;
		}

	}

	/**
	 * ����������Ϣ
	 * 
	 * @return
	 * @throws JSONException
	 */

	public Boolean sendMessage(String message) throws JSONException {
		// ʵ����һ��json����
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

			// ��������
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("��������ɹ�");
				// ���ݴ����¼��
				System.out.println("���ͺ��������" + DataInfo.sendlist.size());
				if (DataInfo.RESEND_FLAG) {
					// System.out.println("sendRecord_OK");
					for (int i = 0; i < DataInfo.RESEND_ID.size(); i++) {
						DataInfo.DBHelper.resend_update(
								DataInfo.RESEND_ID.get(i), "�ɹ�");
					}
					DataInfo.RESEND_FLAG = false;
				} else {
					System.out.println("sendRecord_OK");
					for (int i = 0; i < DataInfo.sendlist.size(); i++) {

						DataInfo.DBHelper.sendRecord_insert(DataInfo.sendlist
								.get(i).get("num").toString(),
								DataInfo.sendlist.get(i).get("Phone")
										.toString(), getTime(), "�ɹ�");
					}
				}
			}

			// ��Ӧ��
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// �õ�Ӧ����ַ�������Ҳ��һ�� JSON ��ʽ���������
				String retSrc = null;
				System.out.println("���ڻ�ȡ�������ݣ�");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// ����JSON����
				System.out.println("retSrc" + retSrc);
				result = new JSONObject(retSrc);

			}

			System.out.println("result" + result);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// �õ����ص���Ϣ
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
		// ������Ϣ����
		else if (type.equals("Send_OK_D")) {
			System.out.println("bufen");

			name = (String) result.get("name");
			// ��������
			String msg_index = result.getString("msg_index");
			// ����ĺ���
			failList = result.getString("fail");

			DataInfo.faillist_Index = msg_index.split("&&");
			DataInfo.failList = failList.split("&&");
			// �޸ķ���״̬!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!����������
			for (int i = 0; i < DataInfo.failList.length; i++) {
				System.out.println("ʧ�ܱ��" + DataInfo.faillist_Index[i]);
				DataInfo.DBHelper.sendRecord_update(DataInfo.failList[i],
						DataInfo.faillist_Index[i], getTime(), "ʧ��");
			}

			DataInfo.SEND_type = "_SEND";
			return true;
		} else {
			// ����
			String errCode = result.getString("errCode");
			DataInfo.ERR_CODE = errCode;
			return false;
		}

	}

	/**
	 * ��ʱ������Ϣ
	 * 
	 * @return
	 * @throws JSONException
	 */

	public Boolean Regularly_sendMessage(String message, String timer_hours,
			String timer_minutes) throws JSONException {
		// ʵ����һ��json����
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
		 System.out.println("����" + phones);
		 System.out.println("���" + number);
		System.out.println("��ʱ��Ϣ" + message);
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

			// ��������
			HttpResponse httpResponse = HTTPRequest(jsonObject);
			if (httpResponse == null) {
				DataInfo.Request_diable = true;
				return false;
			} else {
				DataInfo.Request_diable = false;
				System.out.println("��ʱ��������ɹ�");
				System.out.println("RESEND_FLAG"+DataInfo.RESEND_FLAG);
				// ���ݴ����¼��
				if (DataInfo.RESEND_FLAG) {
					 System.out.println("sendRecord_OK");
//					for (int i = 0; i < DataInfo.RESEND_ID.size(); i++) {
//						DataInfo.DBHelper.resend_update(
//								DataInfo.RESEND_ID.get(i), "��ʱ");
//					}
					DataInfo.RESEND_FLAG = false;
				} else {
					System.out.println("���ͼ�¼��" + DataInfo.sendlist.size());
					for (int i = 0; i < DataInfo.sendlist.size(); i++) {
						Time t = new Time();
						String time = "";
						t.setToNow(); // ȡ��ϵͳʱ��
						String year = t.year + "";
						String month = (t.month + 1) + "";
						String date = t.monthDay + "";
						String dates = year + "/" + month + "/" + date + "\n";
						DataInfo.DBHelper.sendRecord_insert(DataInfo.sendlist
								.get(i).get("num").toString(),
								DataInfo.sendlist.get(i).get("Phone")
										.toString(), dates + timer_hours + ":"
										+ timer_minutes + ":0", "��ʱ");
					}
				}

			}

			// ��Ӧ��
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code" + code);

			if (code != HttpStatus.SC_OK) {
				System.out.println("network error");
				return false;
			} else if (code == 200) {
				// �õ�Ӧ����ַ�������Ҳ��һ�� JSON ��ʽ���������
				String retSrc = null;
				System.out.println("���ڻ�ȡ�������ݣ�");
				retSrc = EntityUtils.toString(httpResponse.getEntity(), "GBK");
				// ����JSON����
				System.out.println("retSrc" + retSrc);
				result = new JSONObject(retSrc);

			}

			System.out.println("result" + result);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// �õ����ص���Ϣ
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
			// ����
			String errCode = result.getString("errCode");
			DataInfo.ERR_CODE = errCode;
			return false;
		}

	}

	// ��ȡϵͳʱ��
	private String getTime() {
		// ʱ��
		Time t = new Time();
		String time = "";
		t.setToNow(); // ȡ��ϵͳʱ��
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
