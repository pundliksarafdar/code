package com.transaction.batch;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.batch.AddBatch;
import com.classapp.db.batch.BatchDB;
import com.classapp.db.batch.BatchData;
import com.classapp.db.batch.DeleteBatch;
import com.classapp.db.subject.SubjectDb;
import com.datalayer.batch.Batch;

public class BatchTransactions {
	public List getAllBatchClass(){
		/*List list = new ArrayList();
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
		batchDataClass.setCandidatesInBatch(10);
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
		batchDataClass.setCandidatesInBatch(112);
		list.add(batchDataClass);
		return list;
		*/
		return new ArrayList();
	}
	

	public List getBatchData(Integer regId){
		BatchDB batchDB = new BatchDB();
		return batchDB.getBatchData(regId);
	}
	
	public boolean addUpdateDb(Batch batch){
		AddBatch addBatch = new AddBatch();
		if (!isBatchExist(batch)) {
			addBatch.addOrUpdateBatch(batch);
			return true;
		}		
		return false;
	}
	
	public boolean addUpdateDatchTime(Batch batch){
		AddBatch addBatch = new AddBatch();
		if (isBatchExist(batch)) {
			addBatch.addOrUpdateBatch(batch);
			return true;
		}		
		return false;
	}

	public boolean isBatchExist(Batch batch){
		BatchData batchData = new BatchData();
		return batchData.isBatchExist(batch.getClass_id(), batch.getBatch_name());
	}
	
	public boolean deleteBatch(Batch batch){
		boolean status = false;
		DeleteBatch deleteBatch = new DeleteBatch();
		com.classapp.db.batch.Batch batchDbLayer =new com.classapp.db.batch.Batch();
		try {
			BeanUtils.copyProperties(batchDbLayer, batch);
			status = deleteBatch.deletebatch(batchDbLayer);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			status = false;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			status = false;
		}
		
		return status;
	}
	
	public List getAllBatches(int regID) {
		BatchDB batchDB=new BatchDB();
		return batchDB.getAllBatches(regID);
		
	}
	
	public String getBatcheSubjects(String batchID) {
		BatchDB batchDB=new BatchDB();
		List Subjects=batchDB.getBatchSubjects(batchID);
		String subject=(String) Subjects.get(0);
		String[] subjectids=subject.split(",");
		List subidsList = new ArrayList();
		for(int i=0;i<subjectids.length;i++)
		{
			subidsList.add(Integer.parseInt(subjectids[i]));
		}
		SubjectDb db=new SubjectDb();
		List Batchsubjects=db.getSubjects(subidsList);
		for(int i=0;i<Batchsubjects.size();i++)
		{
			
			subjectids[i]=(String) Batchsubjects.get(i);
		}
		subject="";
		for(int i=0;i<subjectids.length;i++)
		{
			if(i==0)
			{
			subject=subject+subjectids[i];
			}else{
				
				subject=subject+","+subjectids[i];
			}
		
		}
		return subject;
		
	}
	
	

}
