package com.transaction.teacher;

import java.util.ArrayList;
import java.util.List;

import com.classapp.db.Schedule.Schedule;
import com.classapp.db.Schedule.ScheduleDB;
import com.classapp.db.Teacher.Teacher;
import com.classapp.db.Teacher.TeacherDB;
import com.classapp.db.Teacher.TeacherDetails;
import com.classapp.db.register.RegisterBean;
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

	public String addTeacher(String teacherLoginName, int regID, String subjects) {
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

	}

	public boolean updateTeacher(Teacher teacher) {
		Teacher oldteacher = teacherDB.getTeacher(teacher.getUser_id(),
				teacher.getClass_id());
		String[] newsubids = teacher.getSub_ids().split(",");
		String[] oldsubids = oldteacher.getSub_ids().split(",");
		int counter = 0;
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
				ScheduleDB db = new ScheduleDB();
				db.deleteschedulerelatedtoteachersubject(teacher.getUser_id(),
						Integer.parseInt(oldsubids[counter]));
			}
			counter++;
		}

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
		List list = teacherDB.getSubjectTeacher(subid, regId);
		return list;

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
		List list = teacherDB.getSubjectTeacher(subid, class_id);
		return list;
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
			List list = teacherDB.getSubjectTeacher(Schedulelist.get(counter)
					.getSub_id() + "", regId);
			RegisterBean bean = new RegisterBean();
			RegisterTransaction registerTransaction = new RegisterTransaction();
			List<RegisterBean> teachers = registerTransaction
					.getTeacherName(list);
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

}
