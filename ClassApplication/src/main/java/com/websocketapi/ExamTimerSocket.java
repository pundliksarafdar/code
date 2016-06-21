package com.websocketapi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.websocket.EncodeException;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.JsonObject;

@ServerEndpoint("/websocket/timer/{time}")
public class ExamTimerSocket {
	
	@OnOpen
	public void open(@PathParam("time")long time,Session session) {
		TimerMessage timerMessage = new TimerMessage();
		long millis = time;
		try {
			
			while(millis>0){
				String timeLeft = String.format("%02d:%02d:%02d", 
						TimeUnit.MILLISECONDS.toHours(millis),
						TimeUnit.MILLISECONDS.toMinutes(millis) -  
						TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
						TimeUnit.MILLISECONDS.toSeconds(millis) - 
						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
				Thread.sleep(1*1000);
				millis -= 1000;
				timerMessage.setTimer(timeLeft);
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(timerMessage);
				session.getBasicRemote().sendText(json);
			}
			Thread.sleep(1*1000);
			timerMessage.setExamEnded(true);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(timerMessage);
			session.getBasicRemote().sendText(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}
