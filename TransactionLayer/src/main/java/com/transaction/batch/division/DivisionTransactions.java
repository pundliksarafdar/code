package com.transaction.batch.division;

import java.util.List;

import com.classapp.db.Schedule.ScheduleDB;
import com.classapp.db.batch.division.AddDivision;
import com.classapp.db.batch.division.Division;
import com.classapp.db.batch.division.DivisionDB;
import com.classapp.db.batch.division.DivisionData;
import com.classapp.db.exam.ExamPaperDB;
import com.classapp.db.fees.FeesDB;
import com.classapp.db.questionPaper.QuestionPaperDB;
import com.classapp.db.syllabusplanner.SyllabusPlannerDb;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.attendance.AttendanceTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.fee.FeesTransaction;
import com.transaction.notes.NotesTransaction;
import com.transaction.pattentransaction.QuestionPaperPatternTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.transaction.schedule.ScheduleTransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;

public class DivisionTransactions {
	DivisionData divisionData;
	AddDivision addDivision;
	
	public DivisionTransactions() {
		divisionData= new DivisionData();
		addDivision= new AddDivision();
	}
	public boolean addUpdateDb(Division division){
		
		if (!isDivisionExist(division)) {
			addDivision.addOrUpdateDivision(division);
			return true;
		}		
		return false;
	}
	
	
	
	public boolean isDivisionExist(Division division){		
		return divisionData.isDivisionExist(division.getDivisionName());
	}
	
	 public int getDivisionID(Division division){
		 return divisionData.getDivisionId(division.getDivisionName(),division.getStream());
	 }
	 
	 public boolean rollbackDivision(Division division){
		 return divisionData.deleteDivision(division.getDivId());
	 }

	 public boolean addDivision(Division division,int classid) {
		 DivisionDB divisionDB=new DivisionDB();
		 if(!divisionDB.isDivisionExists(division))
		 {
			 divisionDB.updateDb(division);
			 return false;
		 }
		 return true;
	}

	public List<Division> getAllDivisions(Integer regId) {
		DivisionDB divisionDB=new DivisionDB();
		List<Division> divisions=divisionDB.getAllDivision(regId);
		return divisions;
		
	}
	 
	 public int getDivisionIDByName(String divisionName){
		 return divisionData.getDivisionId(divisionName);
	 }
	 
	 public boolean updateClass(Division division) {
		 DivisionDB db=new DivisionDB();
		 if(!db.isDivisionExists(division)){
			 
			 db.updateDb(division);
			 return false;
		 }
		return true;
	}
	 
	 public boolean deletedivision(int inst_id,int classid) {
		 AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		 attendanceTransaction.deleteAttendance(inst_id, classid);
		 FeesDB feesDB = new FeesDB();
		 feesDB.deleteBatchFeesRelatedToClass(inst_id, classid);
		 ExamPaperDB examPaperDB = new ExamPaperDB();
		 examPaperDB.deleteExamPaperRelatedToClass(inst_id, classid);
		 ScheduleDB scheduleDB = new ScheduleDB();
		 scheduleDB.deleteGroupsRelatedToClass(inst_id, classid);
		 NotesTransaction notesTransaction = new NotesTransaction();
		 notesTransaction.deleteNotesRelatedToDivision(inst_id, classid);
		 QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		 bankTransaction.deleteQuestionrelatedtoClass(inst_id, classid);
		 QuestionPaperPatternTransaction paperPatternTransaction = new QuestionPaperPatternTransaction("", inst_id);
		 paperPatternTransaction.deleteQuestionPaperRelatedToClass(inst_id, classid);
		 paperPatternTransaction.deleteQuestionPaperPatternRelatedToClass(inst_id, classid);
		 StudentTransaction studentTransaction = new StudentTransaction();
		 studentTransaction.updateStudentRelatedToClass(inst_id, classid);
		 FeesTransaction feesTransaction = new FeesTransaction();
		 feesTransaction.updateStudentFeesRelatedToClass(inst_id, classid);
		 StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		 marksTransaction.deleteStudentMarksrelatedtodivision(inst_id, classid);
		 SubjectTransaction subjectTransaction = new SubjectTransaction();
		 subjectTransaction.deleteTopicsrelatedToDivision(inst_id, classid);
		 BatchTransactions batchTransactions = new BatchTransactions();
		 batchTransactions.deletebatchrelatdtoclass(inst_id,classid);
		 DivisionDB db=new DivisionDB();
		 db.deletedivision(classid); 
		 
		 SyllabusPlannerDb plannerDb = new SyllabusPlannerDb();
		 plannerDb.deleteSyllabus(null, null, classid, null, inst_id);
		 return true;
	}
	 
	 public Division getDidvisionByID(int divID) {
		DivisionDB db=new DivisionDB();
		return db.retriveByID(divID);
	}
	 
}
