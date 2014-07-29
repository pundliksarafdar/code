package com.classapp.db.batch;

import com.datalayer.batch.Batch;

public class AddBatch {
	public void addOrUpdateBatch(Batch batch){
		 BatchDB batchDbFunction = new BatchDB();
		 com.classapp.db.batch.Batch batchDb = new com.classapp.db.batch.Batch();
		 batchDb.setBatch_name(batch.getBatch_name());
		 batchDb.setClass_id(batch.getClass_id());
		 batchDbFunction.updateDb(batchDb);
	}
}
