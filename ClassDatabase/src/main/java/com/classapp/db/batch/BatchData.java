package com.classapp.db.batch;

import java.util.List;

import com.classapp.persistence.Constants;
import com.datalayer.batch.BatchDataClass;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BatchData {
	public boolean isBatchExist(Integer regID,String batch){
		BatchDB batchDB = new BatchDB();
		Gson gson = new Gson();
		boolean status = false;
		List<BatchDataClass> batchList = batchDB.getBatchData(regID);
		if (null!=batchList && batchList.size() != 0) {
			for(int i = 0;i<batchList.size();i++){
				BatchDataClass batchDataClass = batchList.get(i);
				if(batchDataClass.getBatchName().equalsIgnoreCase(batch)){
					status = true;
					break;
				}
			}
		}else{
			status = false;
		}
		return status;
	}
}
