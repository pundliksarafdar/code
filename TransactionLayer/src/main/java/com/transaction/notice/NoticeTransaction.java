package com.transaction.notice;

import java.sql.Date;
import java.util.List;

import com.classapp.db.notice.NoticeDB;
import com.classapp.db.notice.StaffNotice;
import com.classapp.db.notice.StudentNotice;

public class NoticeTransaction {
	public Boolean saveStudentNotice(StudentNotice notice) {
		NoticeDB noticeDB = new NoticeDB();
		return noticeDB.saveStudentNotice(notice);
	}
	
	public Boolean saveStaffNotice(StaffNotice notice) {
		NoticeDB noticeDB = new NoticeDB();
		return noticeDB.saveStaffNotice(notice);
	}
	
	public List<StudentNotice> getStudentNotice(int inst_id) {
		NoticeDB noticeDB = new NoticeDB();
		return noticeDB.getStudentNotice(inst_id);
	}
	
	public List<StaffNotice> getStaffNotice(int inst_id) {
		NoticeDB noticeDB = new NoticeDB();
		return noticeDB.getStaffNotice(inst_id);
	}
	
	public boolean deleteStudentNotice(int inst_id,int notice_id){
		NoticeDB noticeDB = new NoticeDB();
		return noticeDB.deleteStudentNotice(inst_id, notice_id);
	}
	
	public boolean deleteStaffNotice(int inst_id,int notice_id){
		NoticeDB noticeDB = new NoticeDB();
		return noticeDB.deleteStaffNotice(inst_id, notice_id);
	}
	
	public List<StudentNotice> getStudentNotice(int inst_id,Date date) {
		NoticeDB noticeDB = new NoticeDB();
		return noticeDB.getStudentNotice(inst_id,date);
	}
	
	public List<StaffNotice> getStaffNotice(int inst_id,Date date) {
		NoticeDB noticeDB = new NoticeDB();
		return noticeDB.getStaffNotice(inst_id,date);
	}
	
	public List<StaffNotice> getStaffNotice(int inst_id,Date date,String role){
		NoticeDB noticeDB = new NoticeDB();
		return noticeDB.getStaffNotice(inst_id,date,role);
	}
	public List<StudentNotice> getStudentNotice(int inst_id,Date date,int div_id,String batch_id){
		NoticeDB noticeDB = new NoticeDB();
		return noticeDB.getStudentNotice(inst_id,date,div_id,batch_id);
	}
}
