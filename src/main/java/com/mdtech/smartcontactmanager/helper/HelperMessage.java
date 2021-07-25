package com.mdtech.smartcontactmanager.helper;

public class HelperMessage {
	
	private String msgContent;
	private String msgType;
	
	public HelperMessage(String msgContent, String msgType) {
		super();
		this.msgContent = msgContent;
		this.msgType = msgType;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	
}
