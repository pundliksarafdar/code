package com.classapp.db.batch;


public class AddBatch {
	public void addOrUpdateBatch(Batch batch){
		 BatchDB batchDbFunction = new BatchDB();		 
		batchDbFunction.updateDb(batch);
	}
}
