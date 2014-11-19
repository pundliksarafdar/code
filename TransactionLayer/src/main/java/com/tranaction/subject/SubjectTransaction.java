package com.tranaction.subject;

import java.util.ArrayList;
import java.util.List;

import com.classapp.db.Schedule.Schedule;
import com.classapp.db.subject.AddSubject;
import com.classapp.db.subject.ClassSubjects;
import com.classapp.db.subject.GetSubject;
import com.classapp.db.subject.SubjectDb;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Subjects;


public class SubjectTransaction {
	public boolean addUpdateSubjectToDb(Subject subject,int regID){
		boolean status = false;
		AddSubject addSubject = new AddSubject();
		GetSubject getSubject = new GetSubject();	
		ClassSubjects classSubjects=new ClassSubjects();
		String result =getSubject.isSubjectExists(subject.getSubjectName(),regID);
		if(result=="false"){
			status = addSubject.addSubject(subject,regID);
			addSubject.addSubjecttoclass(subject,regID);
		}else if(result=="patiallytrue"){
			status=addSubject.addSubjecttoclass(subject,regID);
		}
		else{
			status = false;
		}
		return status;
	}
	
	public List getAllClassSubjects(int regId){
		GetSubject getSubject = new GetSubject();
		List subids=getSubject.getAllClassSubjectcodes(regId+"");
		
		return getSubject.getAllClassSubjectsNames(subids);
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
}
