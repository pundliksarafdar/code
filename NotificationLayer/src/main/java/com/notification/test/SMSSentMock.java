package com.notification.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.notification.sms.SMSSender;

public class SMSSentMock {
	public void sms(Long contactNo,String sms,String channel) throws IOException{
		SMSSender smsSender = new SMSSender();
		smsSender.sendSms(sms, contactNo+"",channel);
	}
	
	public static void main(String[] args) {
		SMSSentMock smsSentMock = new SMSSentMock();
		try {
			smsSentMock.sms(100000L, "sms sent to 12.....","1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
