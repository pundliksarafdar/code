package com.transaction.advertisetransaction;

import java.sql.Date;

import com.classapp.db.advertisement.Advertisement;
import com.classapp.db.advertisement.AdvertisementDB;

public class AdvertiseTransaction {

	public void save(Advertisement advertisement){
	AdvertisementDB advertisementDB=new AdvertisementDB();
	advertisementDB.save(advertisement);
	}
	
	public void updateDb(Advertisement advertisement){
		AdvertisementDB advertisementDB=new AdvertisementDB();
		advertisementDB.updateDb(advertisement);
	}
	
	public int getCount(Date date){
		AdvertisementDB advertisementDB=new AdvertisementDB();
		return advertisementDB.getCount(date);
	}
}
