package com.transaction.teacher;

import java.util.ArrayList;
import java.util.List;

import com.classapp.db.Schedule.Schedule;
import com.classapp.db.Schedule.ScheduleDB;
import com.classapp.db.Teacher.Teacher;
import com.classapp.db.Teacher.TeacherDB;
import com.classapp.db.Teacher.TeacherDetails;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.subject.Subject;
import com.transaction.register.RegisterTransaction;

public class TeacherTransaction {
	TeacherDB teacherDB;
	int class_id;

	public TeacherTransaction() {
		teacherDB = new TeacherDB();
	}

	public TeacherTransaction(int class_id) {
		this.class_id = class_id;
		teacherDB = new TeacherDB();
	}

/*	public String addTeacher(String teacherLoginName, int regID, String subjects) {
		if (teacherDB.isTeacherRegistered(teacherLoginName)) {
			if (!teacherDB.isTeacherExists(teacherLoginName, regID)) {
				teacherDB.add(teacherLoginName, regID, subjects);
				return "added";
			} else {
				return "exists";
			}

		} else {

			return "false";
		}

	}*/

	public boolean updateTeacher(Teacher teacher) {
		Teacher oldteacher = teacherDB.getTeacher(teacher.getUser_id(),
				teacher.getClass_id());
		String[] newsubids = teacher.getSub_ids().split(",");
		String[] oldsubids = oldteacher.getSub_ids().split(",");
		ScheduleDB db = new ScheduleDB();
		int counter = 0;
		if(!oldsubids[0].equals("")){
		while (oldsubids.length > counter) {
			int innercounter = 0;
			boolean flag = false;
			while (newsubids.length > innercounter) {
				if (oldsubids[counter].equals(newsubids[innercounter])) {
					flag = true;
				}
				innercounter++;
			}
			if (flag == false) {
			
				db.deleteschedulerelatedtoteachersubject(teacher.getUser_id(),
						Integer.parseInt(oldsubids[counter]));
			}
			counter++;
		}}

		return teacherDB.updateDb(teacher);
	}

	public Teacher getTeacher(int user_id, int class_id) {
		return teacherDB.getTeacher(user_id, class_id);
	}

	public boolean deleteTeacher(int user_id, int class_id) {
		return teacherDB.deleteTeacher(user_id, class_id);
	}

	public List<TeacherDetails> getAllTeachersFromClass(int regId) {
		return teacherDB.getAllTeachersFromClass(regId);
	}

	public List getSubjectTeacher(String subid) {

		TeacherDB teacherDB = new TeacherDB();
		List list = teacherDB.getSubjectTeacher(subid);
		return list;

	}

	public List getSubjectTeacher(String subid, int regId) {

		TeacherDB teacherDB = new TeacherDB();
		List<Teacher> list = teacherDB.getSubjectTeacher(subid, regId);
		List teacherids=new ArrayList();
		int index=0;
		while (list.size()>index) {
			
			teacherids.add(list.get(index).getUser_id());
			index++;	
		}
		return teacherids;

	}

	public List getTeachersClass(int regId) {

		TeacherDB teacherDB = new TeacherDB();
		List list = teacherDB.getTeachersClass(regId);
		return list;

	}

	public List<TeacherDetails> getAllTeachersFromClass() {
		return teacherDB.getAllTeachersFromClass(class_id);
	}

	public List getSubjectTeacherFromClass(String subid) {
		List<Teacher> list = teacherDB.getSubjectTeacher(subid, class_id);
		List teacherids=new ArrayList();
		int index=0;
		while (list.size()>index) {
			
			teacherids.add(list.get(index).getUser_id());
			index++;	
		}
		return teacherids;
	}

	public com.classapp.db.Teacher.Teacher getTeacher(int user_id) {
		return teacherDB.getTeacher(user_id, class_id);
	}

	public List<List<RegisterBean>> getScheduleTeacher(
			List<Schedule> Schedulelist, int regId) {
		TeacherDB teacherDB = new TeacherDB();
		int counter = 0;
		List<List<RegisterBean>> registerBeans = new ArrayList<List<RegisterBean>>();
		while (counter < Schedulelist.size()) {
			List<Teacher> list = teacherDB.getSubjectTeacher(Schedulelist.get(counter)
					.getSub_id() + "", regId);
			List teacherids=new ArrayList();
			int index=0;
			while (list.size()>index) {
				
				teacherids.add(list.get(index).getUser_id());
				index++;	
			}
			RegisterBean bean = new RegisterBean();
			RegisterTransaction registerTransaction = new RegisterTransaction();
			List<RegisterBean> teachers = registerTransaction
					.getTeacherName(teacherids);
			registerBeans.add(teachers);
			counter++;
		}
		return registerBeans;
	}

	public boolean deletesubjectfromteacherlist(String subjectid) {
		TeacherDB teacherDB = new TeacherDB();
		List<Teacher> list = teacherDB.getteacherrelatedtosubject(subjectid);
		if (list.size() > 0) {
			int counter = 0;
			while (list.size() > counter) {
				Teacher teacher = list.get(counter);
				int innercounter = 0;
				String subids[] = teacher.getSub_ids().split(",");
				String subjectids = "";
				int position = 0;
				while (subids.length > innercounter) {
					if (!subids[innercounter].equals(subjectid)) {
						if (position == 0) {
							subjectids = subids[innercounter];
						} else {
							subjectids = subjectids + ","
									+ subids[innercounter];
						}
						position++;
					}
					innercounter++;
				}
				teacher.setSub_ids(subjectids);
				teacherDB.updateDb(teacher);
				counter++;
			}
		}
		return true;
	}

	public List<String> getTeachersPrefix(List<Schedule> schedules,int regID) {
		TeacherDB db=new TeacherDB();
		List<String> prefixs=new ArrayList<String>();
		int counter=0;
		while(schedules.size()>counter)
		{
			String pre=db.getTeachersPrefix(schedules.get(counter).getTeacher_id(), regID);
			prefixs.add(pre);
			counter++;
		}
		return prefixs;
	}
	
	public Integer getTeacherCount(int regID){
		TeacherDB db=new TeacherDB();
		return db.getTeacherCount(regID);
	}
	
	public List<Subject> getTeacherSubject(int teacherid,int classid) {
		TeacherDB db=new TeacherDB();
		return db.getTeacherSubjects(teacherid, classid);
	}
	
}
