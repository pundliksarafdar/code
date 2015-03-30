package com.transaction.addtransaction;

import java.util.List;

import com.classapp.db.advertise.AdvertiseDb;

public class AdvertiseTransaction {
	public List<String> getAllCounrty(){
		AdvertiseDb advertiseDb = new AdvertiseDb();
		return advertiseDb.getAllCounrty();
	}
	
	public List<String> getAllStateInCountry(String country){
		AdvertiseDb advertiseDb = new AdvertiseDb();
		return advertiseDb.getAllStateInCountry(country);
	}
	
	public List<String> getAllCityInStateAndCountry(String country,String state){
		AdvertiseDb advertiseDb = new AdvertiseDb();
		return advertiseDb.getAllCityInStateAndCountry(country, state);
	}
}
