package com.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.classapp.db.Notes.Notes;
import com.classapp.db.batch.Batch;
import com.classapp.db.batch.division.Division;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.datalayer.subject.Subject;
import com.service.beans.MonthlyScheduleServiceBean;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.notes.NotesTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.schedule.ScheduleTransaction;
import com.transaction.student.StudentTransaction;

@Path("student")
public class StudentService extends ServiceBase{
	
	@GET
	@Path("test")
	public Response test(){
		return Response.ok("test").build();
	}
	
	@GET
	@Path("getClasses")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getClasses(){
		List<HashMap<Integer, String>> list = new ArrayList<HashMap<Integer, String>>();
		HashMap<Integer, String>classIdNName = new HashMap<Integer, String>();
		
		BatchTransactions batchTransactions=new BatchTransactions();
		int regID=getRegId();
		StudentTransaction studentTransaction=new StudentTransaction();
		List<Student> sList=studentTransaction.getStudent(regID);
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List<RegisterBean> beans= registerTransaction.getclassNames(sList);
		
		for(RegisterBean registerBean:beans){
			int cRegId = registerBean.getRegId();
			String className = registerBean.getClassName(); 
			classIdNName.put(cRegId, className);
		}
		//list.add(classIdNName);
		return Response.ok(classIdNName).build();
	}
	
	@GET
	@Path("getDivision/{classId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDivision(@PathParam("classId")int classId){
		
		HashMap<Integer, String>divisionIdNName = new HashMap<Integer, String>();
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		int regID=getRegId();
		
		List<Division> divisions = divisionTransactions.getAllDivisions(classId);
		
		for (Division division:divisions) {
			int divId = division.getDivId();
			String divName = division.getDivisionName()+" "+division.getStream();
			divisionIdNName.put(divId, divName);
		}
		
		return Response.ok(divisionIdNName).build();
	}
	
	@GET
	@Path("getBatch/{divId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBatch(@PathParam("divId")int divId){
		
		HashMap<Integer, String>batchIdNName = new HashMap<Integer, String>();
		BatchTransactions batchTransactions = new BatchTransactions();
		List<Batch> batches = batchTransactions.getBatchRelatedtoDivision(divId);
		
		for(Batch batch:batches){
			int batchId = batch.getBatch_id();
			String batchName = batch.getBatch_name();
			batchIdNName.put(batchId, batchName);
		}
		
		return Response.ok(batchIdNName).build();
	}
	
	@GET
	@Path("getSubject/{divId}/{batchId}/{inst_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubject(
			@PathParam("batchId")int batchId,
			@PathParam("inst_id")int inst_id,
			@PathParam("divId")int divId
			){
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List<Subject> subjects = subjectTransaction.getSubjectsOfBatch(batchId, inst_id, divId);
		HashMap<Integer, String>subjectIdNName = new HashMap<Integer, String>();
		
		for(Subject subject:subjects){
			int subjectIdId = subject.getSubjectId();
			String subjectName = subject.getSubjectName();
			subjectIdNName.put(subjectIdId, subjectName);
		}
		return Response.ok(subjectIdNName).build();
	}
	
	@GET
	@Path("/monthlySchedule/{instituteId}/{batch}/{division}/{startDate}/{endDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScheduleForMonth(
			@PathParam("instituteId") int instId,
			@PathParam("batch") int batch,
			@PathParam("division") int division,
			@PathParam("startDate") long startDate,
			@PathParam("endDate") long endDate
		) {
		
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<MonthlyScheduleServiceBean> scheduleList = new ArrayList<MonthlyScheduleServiceBean>();
		if(endDate == 0){
			scheduleList = scheduleTransaction.getMonthSchedule(batch, new Date(startDate), instId, division);
		}else{
			scheduleList = scheduleTransaction.getMonthSchedule(batch, new Date(startDate),new Date(endDate), instId, division);
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@GET
	@Path("/notes/{instituteId}/{batch}/{division}/{subjectId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNotes(
			@PathParam("instituteId") int instId,
			@PathParam("batch") int batch,
			@PathParam("division") int division,
			@PathParam("subjectId")int subjectId
		) {
		NotesTransaction notesTransaction=new NotesTransaction();
		
		List<Notes> noteslist = notesTransaction.getNotesPath(division, subjectId, instId,batch+"");
		
		return Response.status(Response.Status.OK).entity(noteslist).build();
	}

}
