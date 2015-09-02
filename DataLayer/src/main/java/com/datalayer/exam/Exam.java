package com.datalayer.exam;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Exam implements Serializable{
	private int exam_id;
	private String exam_name;
	private int institute_id;
	private int sub_id;
	private int div_id;
	private int total_marks;
	private int pass_marks;
	private int created_by;
	private int modified_by;
	private Date created_dt;
	private Date modified_dt;
	private String que_ids;
	private Timestamp start_time;
	private Timestamp end_time;
	private String exam_status;
	private String batch_id;
	private String ans_ids;
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}
	public String getExam_name() {
		return exam_name;
	}
	public void setExam_name(String exam_name) {
		this.exam_name = exam_name;
	}
	public int getInstitute_id() {
		return institute_id;
	}
	public void setInstitute_id(int institute_id) {
		this.institute_id = institute_id;
	}
	public int getSub_id() {
		return sub_id;
	}
	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}
	public int getDiv_id() {
		return div_id;
	}
	public void setDiv_id(int div_id) {
		this.div_id = div_id;
	}
	public int getTotal_marks() {
		return total_marks;
	}
	public void setTotal_marks(int total_marks) {
		this.total_marks = total_marks;
	}
	public int getPass_marks() {
		return pass_marks;
	}
	public void setPass_marks(int pass_marks) {
		this.pass_marks = pass_marks;
	}
	public int getCreated_by() {
		return created_by;
	}
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}
	public int getModified_by() {
		return modified_by;
	}
	public void setModified_by(int modified_by) {
		this.modified_by = modified_by;
	}
	public Date getCreated_dt() {
		return created_dt;
	}
	public void setCreated_dt(Date created_dt) {
		this.created_dt = created_dt;
	}
	public Date getModified_dt() {
		return modified_dt;
	}
	public void setModified_dt(Date modified_dt) {
		this.modified_dt = modified_dt;
	}
	public String getQue_ids() {
		return que_ids;
	}
	public void setQue_ids(String que_ids) {
		this.que_ids = que_ids;
	}
	public Timestamp getStart_time() {
		return start_time;
	}
	public void setStart_time(Timestamp start_time) {
		this.start_time = start_time;
	}
	public Timestamp getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Timestamp end_time) {
		this.end_time = end_time;
	}
	public String getExam_status() {
		return exam_status;
	}
	public void setExam_status(String exam_status) {
		this.exam_status = exam_status;
	}
	public String getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}
	public String getAns_ids() {
		return ans_ids;
	}
	public void setAns_ids(String ans_ids) {
		this.ans_ids = ans_ids;
	}
	
	

}
