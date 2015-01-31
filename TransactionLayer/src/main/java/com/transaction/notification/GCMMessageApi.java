package com.transaction.notification;

import java.util.List;

public class GCMMessageApi {
	Data data;
	List<String> registration_ids;
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public List<String> getRegistration_ids() {
		return registration_ids;
	}
	public void setRegistration_ids(List<String> registration_ids) {
		this.registration_ids = registration_ids;
	}
}
