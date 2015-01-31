package com.transaction.notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class GCMSender {
	public static void main(String[] args) {
		GCMSender sender = new GCMSender();
		//sender.sendMessage();
	}
	
	public String sendMessage(String message,ArrayList<String> userIds){
		List<String> strings = userIds;
		Data data = new Data();
		
		data.setMessage(message);
		GCMMessageApi gcmMessageApi = new GCMMessageApi();
		gcmMessageApi.setData(data);
		gcmMessageApi.setRegistration_ids(strings);
		String output = "";
		Gson gson = new Gson();
		System.out.println(gson.toJson(gcmMessageApi));
		try {
			 
			URL url = new URL("https://android.googleapis.com/gcm/send");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key=AIzaSyCC0ZTWttdhRWBBt1K-ZAzuhvtPjSlXR7U");
			String input = gson.toJson(gcmMessageApi);
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
	 /*
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode()+" Message");
			}
	 */
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
	 
			
			System.out.println("Output from Server .... \n");
			String out ;
			while ((out = br.readLine()) != null) {
				output = output+out; 
			}
			System.out.println(output);
			conn.disconnect();
			
		  } catch (MalformedURLException e) {
	 
			e.printStackTrace();
	 
		  } catch (IOException e) {
	 
			e.printStackTrace();
	 
		  }
		return output;
	}
}
