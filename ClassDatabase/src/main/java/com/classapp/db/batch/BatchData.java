package com.classapp.db.batch;

import java.util.List;

import com.datalayer.batch.BatchDataClass;

public class BatchData {
	public boolean isBatchExist(Integer regID,String batch){
		BatchDB batchDB = new BatchDB();
		boolean status = false;
		List<BatchDataClass> batchList = batchDB.getBatchData(regID);
		if (null!=batchList && !batchList.isEmpty()) {
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
