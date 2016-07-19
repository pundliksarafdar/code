package com.notification.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.notification.sms.SMSSender;

public class SMSSentMock {
	public void sms(Long contactNo,String sms) throws IOException{
		SMSSender smsSender = new SMSSender();
		smsSender.sendSms(sms, contactNo+"");
		/*String fileName = contactNo.toString();
		File file = new File("mobile"+File.separatorChar+fileName);
		String path = file.getAbsolutePath();
		if (!file.exists()) {
				file.createNewFile();
		}
		
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(new Date()+"-"+sms);
			bw.close();
		*/
	}
	
	public static void main(String[] args) {
		SMSSentMock smsSentMock = new SMSSentMock();
		try {
			smsSentMock.sms(100000L, "sms sent to 12.....");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
