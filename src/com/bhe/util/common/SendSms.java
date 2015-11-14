package com.bhe.util.common;

public class SendSms {

	private String Uid;// 注册号(必填)

	private String Key;// 短信密匙(必填)

	private String sourceadd;// 子通道号(可填)

	private String phone;// 手机号码(多个用逗号(,)隔开，最多50个)(必填)

	private String content;// 短信内容(必填)

	private String result;// 结果码， -1没有该用户账户,-2 密钥不正确
							// ,-3短信数量不足,-11该用户被禁用,-14短信内容出现非法字符,-4手机号格式不正确,-41手机号码为空,
							// -42短信内容为空,大于0短信发送数量

	private String message;// 结果码介绍

	public String getUid() {
		return Uid;
	}

	public void setUid(String uid) {
		Uid = uid;
	}

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public String getSourceadd() {
		return sourceadd;
	}

	public void setSourceadd(String sourceadd) {
		this.sourceadd = sourceadd;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
