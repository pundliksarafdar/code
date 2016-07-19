package com.notification.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SMSSender {
	public static String retval = "";
	public static void sendSms(String msg,String number){
		try {
			msg = URLEncoder.encode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String sendSmsApi = "https://www.smsgatewayhub.com/api/mt/SendSMS?APIKey=9jg6gJzWwEGFF8xwRisTvw&senderid=CLSFLR&channel=2&DCS=0&flashsms=0&number={{phoneNo}}&text={{textMessage}}&route=1";
		sendSmsApi = sendSmsApi.replace("{{textMessage}}", msg);
		sendSmsApi = sendSmsApi.replace("{{phoneNo}}", number);
		try {
			sendGet(sendSmsApi);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void sendGet(String urlS) throws Exception {
		
		String url = urlS;
		//url = URLEncoder.encode(url, "UTF-8");
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		URLConnection conn = obj.openConnection();
		conn.setDoOutput(true);
		
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		//wr.write(data);
		wr.flush();
		// Read The Response
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		String retval = "";
		while ((line = rd.readLine()) != null) {
			// Process line...
			retval += line;
		}
		wr.close();
		rd.close();
		System.out.println(retval);
		
	}

}