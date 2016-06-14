package com.transaction.batch;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.batch.AddBatch;
import com.classapp.db.batch.Batch;
import com.classapp.db.batch.BatchDB;
import com.classapp.db.batch.BatchData;
import com.classapp.db.batch.DeleteBatch;
import com.classapp.db.fees.FeesDB;
import com.classapp.db.Schedule.Schedule;
import com.classapp.db.Schedule.ScheduleDB;
import com.classapp.db.attendance.AttendanceDB;
import com.classapp.db.subject.SubjectDb;
import com.transaction.attendance.AttendanceTransaction;
import com.transaction.fee.FeesTransaction;
import com.transaction.notes.NotesTransaction;
import com.transaction.schedule.ScheduleTransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.util.ClassAppUtil;


public class BatchTransactions {
	BatchDB batchDB= null;
	public BatchTransactions() {
		batchDB= new BatchDB();
	}
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
		return batchDB.retriveAllBatches(regId);
	}
	
	public boolean addUpdateDb(Batch batch){
		
		if (batchDB.updateDb(batch).equals("0")) {
			return true;
		}		
		return false;
	}
	
public int getNextBatchID(int inst_id,int div_id){
		return batchDB.getNextBatchID(inst_id, div_id);
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
		return batchData.isBatchExist(batch.getClass_id(), batch.getBatch_name(),batch.getDiv_id());
	}
	
	public boolean isUpdatedBatchExist(Batch batch){
		BatchData batchData = new BatchData();
		return batchData.isUpdatedBatchExist(batch.getBatch_id(),batch.getClass_id(), batch.getBatch_name(),batch.getDiv_id());
	}
	
	public boolean deleteBatch(int inst_id,int div_id,int batch_id){
		boolean status = false;
		AttendanceDB attendanceDB = new AttendanceDB();
		attendanceDB.deleteAttendanceRelatedToBatch(inst_id, div_id, batch_id);
		FeesDB feesDB = new FeesDB();
		feesDB.deleteBatchFeesRelatedToBatch(inst_id, div_id, batch_id);
		ScheduleDB scheduleDB = new ScheduleDB();
		scheduleDB.deleteGroupsRelatedToBatch(inst_id, div_id, batch_id);
		NotesTransaction notesTransaction = new NotesTransaction();
		notesTransaction.removebatchfromnotes(inst_id, div_id, batch_id+"");
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		scheduleTransaction.deleteSchedulerelatedoBatch(inst_id, div_id, batch_id);
		StudentTransaction  studentTransaction = new StudentTransaction();
		studentTransaction.removeBatchFromstudentslist(batch_id+"", inst_id, div_id);
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		marksTransaction.deleteStudentMarksrelatedtobatch(inst_id, div_id, batch_id);
		FeesTransaction feesTransaction = new FeesTransaction();
		feesTransaction.updateStudentFeesRelatedToBatch(inst_id, div_id, batch_id);
		batchDB.deleteBatch(inst_id, div_id, batch_id);
		
		return status;
	}
	
	public Batch getBatch(int batchId,int inst_id,int div_id){
		return batchDB.getBatchFromID(batchId,inst_id,div_id);
		
	}
	
	public List getAllBatches(int regID) {
		return batchDB.getAllBatches(regID);
		
	}
	
	
	public String getBatcheSubjects(String batchID) {
		List Subjects=batchDB.getBatchSubjects(batchID,1,0);
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
	
	public List getBatcheSubject(String batchID,int inst_id,int div_id) {
		BatchDB batchDB=new BatchDB();
		List Subjects=batchDB.getBatchSubjects(batchID, inst_id, div_id);
		String subject=(String) Subjects.get(0);
		if(!subject.equals("")){
		String[] subjectids=subject.split(",");
		List subidsList = new ArrayList();
		for(int i=0;i<subjectids.length;i++)
		{
			subidsList.add(Integer.parseInt(subjectids[i]));
		}
		SubjectDb db=new SubjectDb();
		List<com.classapp.db.subject.Subjects> Batchsubjects=db.getSubjects(subidsList);
		if(Batchsubjects != null){
			subidsList = new ArrayList();
			for (com.classapp.db.subject.Subjects batchSubjects : Batchsubjects) {
				if("1".equals(batchSubjects.getSub_type())){
					String ids[] = batchSubjects.getCom_subjects().split(",");
					for (String string : ids) {
						subidsList.add(Integer.parseInt(string));
					}
				}
				subidsList.add(batchSubjects.getSubjectId());
			}
			 Batchsubjects=db.getSubjects(subidsList);
		}
		return Batchsubjects;
		}
		return null;
	}
	public List<Batch> getTeachersBatch(List<Schedule> schedules) {
		List<Batch>  batchs=new ArrayList<Batch>();
		batchDB=new BatchDB();
		int counter=0;
		while(counter<schedules.size()){
		Batch batch=batchDB.getBatchFromID(schedules.get(counter).getBatch_id(),schedules.get(counter).getInst_id(),schedules.get(counter).getDiv_id());
		batchs.add(batch);
		counter++;
		}
		return batchs;
		
	}
	
	public List<Batch> getAllBatchesOfDivision(int divisionId, int class_id){
		BatchDB batchDB=new BatchDB();
		return batchDB.retriveAllBatchesOfDivision(divisionId, class_id);
	}
	
	public boolean deletesubjectfrombatch(int inst_id,String subjectid) {
		BatchDB batchDB=new BatchDB();
		List<Batch> batchs=batchDB.getbachesrelatedtosubject(inst_id,subjectid);
		if(batchs.size()>0){
			int counter=0;
			while(batchs.size()>counter){
				Batch batch=batchs.get(counter);
				String subids[]=batch.getSub_id().split(",");
				String subjectids="";
				int innercounter=0;
				int position=0;
				while(subids.length>innercounter){
					if(!subids[innercounter].equals(subjectid)){
						if(position==0){
							subjectids=subids[innercounter];
						}else{
							subjectids=subjectids+","+subids[innercounter];
						}
						position++;
					}
					innercounter++;	
				}
				batch.setSub_id(subjectids);
				batchDB.updateDb(batch);
				counter++;
			}
		}else{
			return false;
		}
		
		return true;
		
	}
	
	public boolean deletebatchrelatdtoclass(int inst_id,int classid) {
		BatchDB batchDB=new BatchDB();
		batchDB.deletebatchrelatedtoclass(classid);
		return true;
		
	}
	
	public Integer getBatchCount(int regID) {
		
		BatchDB batchDB=new BatchDB();
		return batchDB.getBatchCount(regID);
	}
	
	public List<Batch> getBatchRelatedtoDivision(int division) {
		BatchDB batchDB=new BatchDB();
		return batchDB.getbachesrelatedtoclass(division);
	}
	
	public List<Batch> getbachesrelatedtodivandsubject(String subjectid,int divId,int class_id) {
		List<Batch> batchs = new ArrayList<Batch>();
		BatchDB batchDB=new BatchDB();
		batchs = batchDB.getbachesrelatedtodivandsubject(subjectid, divId, class_id);
		return batchs;
		
	}
	public void updateBatchRollGeneratedStatus(int batchId,int inst_id,int div_id,String status){
		String rollGenerated = "rollGenerated";
		BatchDB batchDB=new BatchDB();
		Batch batch = batchDB.getBatchFromID(batchId, inst_id, div_id);
		String currentStatus = batch.getStatus();
		if(null != currentStatus){
			String statusValue = ClassAppUtil.getValue(currentStatus, rollGenerated);
			//rollGenerated status is not updated in db
			if(null==statusValue){
				currentStatus = currentStatus+rollGenerated+"="+status+";";
				batch.setStatus(currentStatus);
				batchDB.updateDb(batch);
			}
		}else{
			currentStatus = rollGenerated+"="+status+";";
			batch.setStatus(currentStatus);
			batchDB.updateDb(batch);
		}
		
	}
}
