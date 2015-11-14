package com.bhe.util.common;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;


public class SendMsg_webchinese {

	private static final String Uid = "y.liu";

	private static final String Key = "057b5d9348e29358b4ec";
	//635497

	/**
	 * 查询短信数量
	 * 
	 * @return Sms
	 */
	public static String queryBalance(String smsMob, String smsText) {
		String url = "http://sms.webchinese.cn/web_api/SMS/?Action=SMS_Num";

		NameValuePair[] data = { new NameValuePair("Uid", Uid),
				new NameValuePair("Key", Key) };
		String r = exec(url, data);
		return r;
	}

	/**
	 * 发送及时短信
	 * 
	 * @param sms
	 *            Sms
	 * @return boolean 发送是否成功
	 */
	public static boolean sendSms(SendSms sms) throws SmsException {
		String url = "http://gbk.sms.webchinese.cn";
		NameValuePair[] data = { new NameValuePair("Uid", Uid),
				new NameValuePair("Key", Key),
				new NameValuePair("smsMob", sms.getPhone()),
				new NameValuePair("smsText", sms.getContent()) };
		String r = exec(url, data);
		return result(buildSms(r));
	}

	/**
	 * exec方法
	 * 
	 * @param url
	 *            带参的链接
	 * @return 结果
	 */
	private static String exec(String url, NameValuePair[] data) {
		String result = null;
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
		post.setRequestBody(data);
		try {
			client.executeMethod(post);
			Header[] headers = post.getResponseHeaders();
			int statusCode = post.getStatusCode();
			result = new String(post.getResponseBodyAsString().getBytes("gbk"));
			post.releaseConnection();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 给Message的属性赋值
	 * 
	 * @return SendSms -1没有该用户账户,-2 密钥不正确
	 *         ,-3短信数量不足,-11该用户被禁用,-14短信内容出现非法字符,-4手机号格式不正确,-41手机号码为空,
	 *         -42短信内容为空,大于0短信发送数量
	 */
	private static SendSms buildSms(String r) {

		SendSms sms = new SendSms();
		Integer count = Integer.valueOf(r);
		sms.setResult(r);
		switch (count) {
		case -1:
			sms.setMessage("没有该用户账户");
			break;
		case -2:
			sms.setMessage("密钥不正确");
			break;
		case -3:
			sms.setMessage("短信数量不足");
			break;
		case -11:
			sms.setMessage("该用户被禁用");
			break;
		case -14:
			sms.setMessage("短信内容出现非法字符");
			break;
		case -4:
			sms.setMessage("手机号格式不正确");
			break;
		case -41:
			sms.setMessage("手机号码为空");
			break;
		case -42:
			sms.setMessage("短信内容为空");
			break;
		}

		return sms;
	}

	/**
	 * 判断结果
	 * 
	 * @param Sms
	 *            sms
	 * @return boolean
	 */
	private static boolean result(SendSms sms) throws SmsException {

		if (sms.getResult() != null && sms.getResult().trim().length() > 0
				&& Integer.parseInt(sms.getResult()) > 0) {

			return true;

		} else {
			throw new SmsException(sms.getMessage());
		}

	}

	 public static void main(String[] args) throws Exception {
	 SendSms sms = new SendSms();
	 sms.setPhone("15223005510");
	 sms.setContent("test 代碼");
	 Boolean isSend = sendSms(sms);
	 if(isSend){
		 System.out.println("发送成功");
	 }else{
		 System.out.println("发送失败");
	 }
	 }
}
