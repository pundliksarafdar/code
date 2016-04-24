package com.tranaction.subject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.batch.Batch;
import com.classapp.db.batch.BatchDB;
import com.classapp.db.exam.ExamDB;
import com.classapp.db.exam.ExamPaperDB;
import com.classapp.db.exam.Exam_Paper;
import com.classapp.db.Schedule.Schedule;
import com.classapp.db.subject.AddSubject;
import com.classapp.db.subject.GetSubject;
import com.classapp.db.subject.SubjectDb;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Subjects;
import com.classapp.db.subject.Topics;
import com.classapp.logger.AppLogger;
import com.transaction.attendance.AttendanceTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.notes.NotesTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.transaction.schedule.ScheduleTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.transaction.teacher.TeacherTransaction;


public class SubjectTransaction {
	public boolean addUpdateSubjectToDb(Subject subject,int regID){
		boolean status = false;
		AddSubject addSubject = new AddSubject();
		GetSubject getSubject = new GetSubject();	
		String result =getSubject.isSubjectExists(subject.getSubjectName(),regID);
		if(result=="false"){
			status = addSubject.addSubject(subject,regID);
		//	addSubject.addSubjecttoclass(subject,regID);
		}
		else{
			status = false;
		}
		return status;
	}
	
	public List<Subjects> getAllClassSubjects(int regId){
		GetSubject getSubject = new GetSubject();
		//List subids=getSubject.getAllClassSubjectcodes(regId+"");
		
		return getSubject.getAllClassSubjectsNames(regId);
	}
	
	public int getSubjectID(int regID,String subname) {
		SubjectDb db=new SubjectDb();
		List list=db.getSubjectID(subname);
		int id=(Integer) list.get(0);
		
		return id;
		
	}
	
	public boolean addUpdateSubjectToDb(Subject subject){
		boolean status = false;
		AddSubject addSubject = new AddSubject();
		GetSubject getSubject = new GetSubject();	
		if(!getSubject.isSubjectExists(subject.getSubjectId(), subject.getSubjectName())){
			status = addSubject.addSubject(subject);
		}else{
			status = false;
		}
		return status;
	}
	
	public List<String> getScheduleSubject(List<Schedule> schedule) {
		SubjectDb db=new SubjectDb();
		int i=0;
		List<String> subList=new ArrayList<String>();
		while(i<schedule.size())
		{
			String subject;
		subject=db.getschedulesubject(schedule.get(i).getSub_id());
		subList.add(subject);
		i++;
		}
		return subList;
		
	}
	
	public Boolean modifySubject(Subject subject) {
		SubjectDb db=new SubjectDb();
		return db.modifySubject(subject);
		
	}
	public boolean deleteSubject(int inst_id,int sub_id) {
		TeacherTransaction teacherTransaction = new TeacherTransaction();
		teacherTransaction.deletesubjectfromteacherlist(inst_id,sub_id+"");
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		attendanceTransaction.deleteAttendanceRelatedToSubject(inst_id, sub_id);
		BatchTransactions batchTransactions = new BatchTransactions();
		batchTransactions.deletesubjectfrombatch(inst_id,sub_id+"");
		ExamPaperDB examPaperDB = new ExamPaperDB();
		examPaperDB.deleteExamPaperRelatedToSubject(inst_id, sub_id);
		NotesTransaction notesTransaction = new NotesTransaction();
		notesTransaction.deleteNotesRelatedToSubject(inst_id,sub_id);
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		bankTransaction.deleteQuestionrelatedtoSubject(inst_id, sub_id);
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		scheduleTransaction.deleteschedulerelatedsubject(inst_id,sub_id);
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		marksTransaction.deleteStudentMarksrelatedtosubject(inst_id,sub_id);
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		subjectTransaction.deleteTopicsrelatedToSubject(inst_id, sub_id);
		SubjectDb subjectDb=new SubjectDb();
		subjectDb.deleteSubject(sub_id);
		return true;
	}
	
	public List<com.datalayer.subject.Subject> getSubjectsOfBatch(int batchId){
		BatchDB batchDB = new BatchDB();
		Batch batch = batchDB.getBatchFromID(batchId,1,1);
		List<com.datalayer.batch.Batch> batchesDataLayer = new ArrayList<com.datalayer.batch.Batch>();
		StringBuilder subjectIds = new StringBuilder();
		
		subjectIds.append(batch.getSub_id());
		AppLogger.logger(subjectIds);
		List<String> subjectIdList = Arrays.asList(subjectIds.toString().split("\\s*,\\s*"));
		Set<Integer> set = new HashSet<Integer>();
		for(String subjectId:subjectIdList){
			set.add(Integer.parseInt(subjectId));
		}
		
		
		SubjectDb subjectDb = new SubjectDb();
		List<Integer> subjectIdListUnique = new ArrayList<Integer>();
		subjectIdListUnique.addAll(set);
		
		List<Subjects> subjectsForDivision = subjectDb.getSubjects(subjectIdListUnique);
		List<com.datalayer.subject.Subject> subjectsofDivision = new ArrayList<com.datalayer.subject.Subject>();
		for (Subjects subject:subjectsForDivision) {
			com.datalayer.subject.Subject subjectDataLayer = new com.datalayer.subject.Subject();
			try {BeanUtils.copyProperties(subjectDataLayer, subject);} catch (IllegalAccessException e) {e.printStackTrace();} catch (InvocationTargetException e) {e.printStackTrace();	}
			subjectsofDivision.add(subjectDataLayer);
		}
		return subjectsofDivision;
		
	}
	
	public List<Subject> getSubjectRelatedToDiv(int div_id,int inst_id) {
		List<Subject> subjects = new ArrayList<Subject>();
		SubjectDb subjectDb = new SubjectDb();
		subjects = subjectDb.getSubjectRelatedToDiv(div_id, inst_id);
		return subjects;
	}
	
	public Subject getSubject(int subid) {
		SubjectDb db=new SubjectDb();
		return db.retrive(subid);
	}
	
	public List<Topics> getTopics(int inst_id,int sub_id,int div_id) {
	SubjectDb subjectDb=new SubjectDb();
	return subjectDb.GetSubjectTopics(inst_id, sub_id, div_id);
		
	}
	
	public boolean deleteTopics(int inst_id,int sub_id,int div_id,int topicid) {
		QuestionBankTransaction bankTransaction = new  QuestionBankTransaction();
		bankTransaction.updateQuestionrelatedtoTopic(inst_id, sub_id, div_id, topicid);
		SubjectDb subjectDb=new SubjectDb();
		return subjectDb.deleteTopics(inst_id, sub_id, div_id,topicid);
			
		}
	
	public boolean deleteTopicsrelatedToSubject(int inst_id,int sub_id) {
		SubjectDb subjectDb=new SubjectDb();
		return subjectDb.deleteTopicsrelatedToSubject(inst_id, sub_id);
			
		}
	
	public boolean deleteTopicsrelatedToDivision(int inst_id,int div_id) {
		SubjectDb subjectDb=new SubjectDb();
		return subjectDb.deleteTopicsrelatedToDivision(inst_id, div_id);
			
		}
	
	public boolean addTopic(int inst_id,int sub_id,int div_id,String topicname) {
		SubjectDb subjectDb=new SubjectDb();
		int topicid=subjectDb.getNextTopicID(inst_id, div_id, sub_id);
		Topics topic=new Topics();
		topic.setDiv_id(div_id);
		topic.setInst_id(inst_id);
		topic.setSub_id(sub_id);
		topic.setTopic_id(topicid);
		topic.setTopic_name(topicname);
		return subjectDb.saveorupdateTopic(topic);
			
		}
	
	public boolean updateTopic(int inst_id,int sub_id,int div_id,String topicname,int topicid) {
		SubjectDb subjectDb=new SubjectDb();
	//	int topicid=subjectDb.getNextTopicID(inst_id, div_id, sub_id);
		Topics topic=new Topics();
		topic.setDiv_id(div_id);
		topic.setInst_id(inst_id);
		topic.setSub_id(sub_id);
		topic.setTopic_id(topicid);
		topic.setTopic_name(topicname);
		return subjectDb.saveorupdateTopic(topic);
			
		}
	
	public boolean isTopicExists(int inst_id,int sub_id,int div_id,String topic_name) {
		SubjectDb subjectDb=new SubjectDb();
		return subjectDb.isTopicExists(inst_id, div_id, sub_id, topic_name);
	}
	
	public boolean isEditTopicExists(int inst_id,int sub_id,int div_id,String topic_name,int topicid) {
		SubjectDb subjectDb=new SubjectDb();
		return subjectDb.isEditTopicExists(inst_id, div_id, sub_id, topic_name,topicid);
	}

	public List<com.datalayer.subject.Subject> getSubjectsOfBatch(int batchId,int inst_id,int div_id){
		BatchDB batchDB = new BatchDB();
		Batch batch = batchDB.getBatchFromID(batchId,inst_id,div_id);
		List<com.datalayer.batch.Batch> batchesDataLayer = new ArrayList<com.datalayer.batch.Batch>();
		StringBuilder subjectIds = new StringBuilder();
		
		subjectIds.append(batch.getSub_id());
		AppLogger.logger(subjectIds);
		List<String> subjectIdList = Arrays.asList(subjectIds.toString().split("\\s*,\\s*"));
		Set<Integer> set = new HashSet<Integer>();
		for(String subjectId:subjectIdList){
			set.add(Integer.parseInt(subjectId));
		}
		
		
		SubjectDb subjectDb = new SubjectDb();
		List<Integer> subjectIdListUnique = new ArrayList<Integer>();
		subjectIdListUnique.addAll(set);
		
		List<Subjects> subjectsForDivision = subjectDb.getSubjects(subjectIdListUnique);
		List<com.datalayer.subject.Subject> subjectsofDivision = new ArrayList<com.datalayer.subject.Subject>();
		for (Subjects subject:subjectsForDivision) {
			com.datalayer.subject.Subject subjectDataLayer = new com.datalayer.subject.Subject();
			try {BeanUtils.copyProperties(subjectDataLayer, subject);} catch (IllegalAccessException e) {e.printStackTrace();} catch (InvocationTargetException e) {e.printStackTrace();	}
			subjectsofDivision.add(subjectDataLayer);
		}
		return subjectsofDivision;
		
	}

	public static void main(String[] args) {
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		//subjectTransaction.getSubjectsOfDivision(13,12);
	}
}
