package com.transaction;

import com.datalayer.batchnsubject.BatchAndSubject;
import com.tranaction.batchnsubject.BatchNSubjectTransaction;

public class TestBatchNSubject {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BatchAndSubject batchAndSubject = new BatchAndSubject();
		BatchNSubjectTransaction batchNSubjectTransaction = new BatchNSubjectTransaction();
		batchAndSubject.setRegId(34);
		batchAndSubject.setBatch_id(2);
		batchAndSubject.setSubjectCode(3);
		batchNSubjectTransaction.addBatchNSubject(batchAndSubject);
		

	}

}
