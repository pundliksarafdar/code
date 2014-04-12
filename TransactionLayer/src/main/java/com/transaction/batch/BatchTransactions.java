package com.transaction.batch;

import java.util.ArrayList;
import java.util.List;

import com.datalayer.batch.BatchDataClass;
import com.datalayer.batch.SubjectList;
import com.datalayer.batch.Timmings;

public class BatchTransactions {
	public List getAllBatchClass(){
		List list = new ArrayList();
		Timmings timmings = new Timmings();
		SubjectList subjectList = new SubjectList();
		BatchDataClass batchDataClass = new BatchDataClass();
		
		timmings.setStartTimming("22-11-2014 10:30");
		timmings.setEndTimming("22-11-2014 11:30");
		
		subjectList.setSubjectName("Math");
		subjectList.setTimmings(timmings);
		batchDataClass.setSubjectList(subjectList);
		batchDataClass.setBatchName("bATCH nAME");
		batchDataClass.setTimmings(timmings);
		list.add(batchDataClass);
		
		
		timmings = new Timmings();
		subjectList = new SubjectList();
		batchDataClass = new BatchDataClass();
		
		timmings.setStartTimming("22-11-2014 10:30");
		timmings.setEndTimming("22-11-2014 11:30");
		
		subjectList.setSubjectName("Math");
		subjectList.setTimmings(timmings);
		batchDataClass.setSubjectList(subjectList);
		batchDataClass.setBatchName("Batch Name");
		batchDataClass.setTimmings(timmings);
		list.add(batchDataClass);
		return list;
	}

	

}
