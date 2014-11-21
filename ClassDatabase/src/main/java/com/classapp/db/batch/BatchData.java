package com.classapp.db.batch;

import java.util.List;

import com.datalayer.batch.BatchDataClass;

public class BatchData {
	BatchDB batchDB;
	
	public BatchData() {
		batchDB= new BatchDB();
	}
/*	public boolean isBatchExist(int regID,String batch){
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
	}*/


	public boolean isBatchExist(int regID,String batchName){
		return batchDB.isBatchExist(regID, batchName);		
		
	}
	
	public List<Batch> getAllBatchesOfClass(int class_id){
		return batchDB.retriveAllBatches(class_id);
	}
	
	public List<Batch> getAllBatchesOfClass(List<String> class_id){
		return batchDB.retriveAllBatches(class_id);
	}
	
	public List<BatchDetails> getAllBatchDetailsList(int class_id){
			return batchDB.getAllBatchesOfClass(class_id);
	}
	
	public List<Batch> getAllRelatedBatchesOfClass(int class_id, int div_id){
		return batchDB.retriveAllRelatedBatches(class_id, div_id);
	}
}
