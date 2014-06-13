package com.tranaction.batchnsubject;

import com.classapp.db.batchandsubject.BatchNSubjectDb;
import com.datalayer.batch.*;
import com.datalayer.batchnsubject.BatchAndSubject;

public class BatchNSubjectTransaction {
	public String addBatchNSubject(BatchAndSubject batchAndSubject){
		String status = "0";
		BatchNSubjectDb batchNSubjectDb = new BatchNSubjectDb();
		batchNSubjectDb.addSubjectNBatch(batchAndSubject);
		return status;
	}
	
}
