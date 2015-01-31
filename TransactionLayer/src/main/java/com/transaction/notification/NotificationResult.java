package com.transaction.notification;

import java.util.ArrayList;

import com.google.gson.JsonObject;

public class NotificationResult {
	String multicast_id;
	int success;
	int failure;
	int canonical_ids;
	ArrayList<JsonObject> results = new ArrayList<JsonObject>();
	public String getMulticast_id() {
		return multicast_id;
	}
	public void setMulticast_id(String multicast_id) {
		this.multicast_id = multicast_id;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public int getFailure() {
		return failure;
	}
	public void setFailure(int failure) {
		this.failure = failure;
	}
	public int getCanonical_ids() {
		return canonical_ids;
	}
	public void setCanonical_ids(int canonical_ids) {
		this.canonical_ids = canonical_ids;
	}
	public ArrayList<JsonObject> getResults() {
		return results;
	}
	public void setResults(ArrayList<JsonObject> results) {
		this.results = results;
	}
	
	
}
