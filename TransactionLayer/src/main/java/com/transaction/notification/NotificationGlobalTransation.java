package com.transaction.notification;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.register.AllUserIdDb;
import com.classapp.notification.AllUserId;
import com.google.gson.Gson;
import com.mysql.jdbc.Util;

public class NotificationGlobalTransation {
	public NotificationResult notificationToAll(String message){
		GCMSender gcmSender = new GCMSender();
		NotificationResult notificationResult = new NotificationResult();
		List<AllUserId> userIds = getAllNotificationId();
		
		List<String> googleIds = new ArrayList<String>();
		
		for(AllUserId allUserId:userIds){
			googleIds.add(allUserId.getGoogleId());
		}
		String responce = gcmSender.sendMessage(message, (ArrayList<String>) googleIds);
		
		if(null != responce){
			Gson gson = new Gson();
			notificationResult = gson.fromJson(responce, NotificationResult.class);
		}
		return notificationResult;
	}
	
	public List<AllUserId> getAllNotificationId(){
		AllUserIdDb allUserIdDb = new AllUserIdDb();
		List<com.classapp.db.register.AllUserId> list = allUserIdDb.getAllUserId();
		List<AllUserId> userIds = new ArrayList<AllUserId>();
		try {
			for(com.classapp.db.register.AllUserId allUserId : list){
				AllUserId userId = new AllUserId();
				BeanUtils.copyProperties(userId, allUserId);
				userIds.add(userId);
			}
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userIds;
	}
	
	public Boolean sendUpdateLectureNotification(String batchname,String Batchid) {
		AllUserIdDb allUserIdDb=new AllUserIdDb();
		List<String> list=allUserIdDb.getAllUserIdrelatedtoBatch(Batchid);
		GCMSender gcmSender = new GCMSender();
		String responce = gcmSender.sendMessage("Lectures of batch "+batchname+" has been updated", (ArrayList<String>) list);
		return true;
	}
	
	public Boolean sendAddLectureNotification(String batchname,String Batchid) {
		AllUserIdDb allUserIdDb=new AllUserIdDb();
		List<String> list=allUserIdDb.getAllUserIdrelatedtoBatch(Batchid);
		GCMSender gcmSender = new GCMSender();
		if(list.size()>0){
		String responce = gcmSender.sendMessage("Lectures of batch "+batchname+" has been added", (ArrayList<String>) list);
		}
		return true;
	}
	
	public static void main(String[] args) {
		NotificationGlobalTransation notificationGlobalTransation = new NotificationGlobalTransation();
		notificationGlobalTransation.notificationToAll("Hi");
		
	}
}
