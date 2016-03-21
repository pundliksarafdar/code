package com.websocketapi;

import java.io.IOException;
import java.util.ArrayList;
/*
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
*/
 
//@ServerEndpoint("/notifications")
public class NotificationWebSocketApi {
//	public static ArrayList<Session> sessions = new ArrayList<Session>();
//    
//    @OnMessage
//    public String hello(String message) {
//        AppLogger.logger("Received : "+ message);
//        sendToAllConnectedSessions(message);
//        return message;
//    }
//
//    @OnOpen
//    public void myOnOpen(Session session) {
//        AppLogger.logger("WebSocket opened: " + session.getId());
//        sessions.add(session);
//    }
//
//    @OnClose
//    public void myOnClose(CloseReason reason) {
//        AppLogger.logger("Closing a WebSocket due to " + reason.getReasonPhrase());
//    }
//    
//    private void sendToAllConnectedSessions(String message ) {
//        for (Session session : sessions) {
//            sendToSession(session, message);
//        }
//    }
//
//    private void sendToSession(Session session, String message) {
//        try {
//            session.getBasicRemote().sendText(message.toString());
//        } catch (IOException ex) {
//            sessions.remove(session);
//        }
//    }

}