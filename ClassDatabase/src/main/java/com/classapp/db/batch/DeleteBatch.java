package com.classapp.db.batch;

public class DeleteBatch {
	public boolean deletebatch(Batch batch){
		boolean status = false;
		String statusStr;
		BatchDB batchDB = new BatchDB();
		statusStr = batchDB.deleteBatch(batch);
		
		if("0".equals(statusStr)){
			status = true;
		}
		return status;
	}
}
