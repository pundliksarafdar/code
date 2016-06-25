package com.classapp.db.question;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Questionbank implements Serializable {

	private int inst_id;
	private int div_id; 
	private int sub_id; 
	private int que_id; 
	private int added_by; 
	private int modified_by; 
	private Date created_dt; 
	private Date modified_dt; 
	private int rep; 
	private int marks;
	private String ans_id;
	private String exam_rep;
	String ques_status;
	int topic_id;
	String que_text;
	String opt_1;
	String opt_2;
	String opt_3;
	String opt_4;
	String opt_5;
	String opt_6;
	String opt_7;
	String opt_8;
	String opt_9;
	String opt_10;
	String que_type;
	
	/*
	 * Primary image are images of question in subjective question objective question or paragraph image
	 * Secondary image are images of options in objective and question in paragraph image  
	 */
	List<String> primaryImage;
	HashMap<Integer, List<String>>secondaryImage;
	HashMap<String, List<String>>secondaryImageStr;
	
	
	public HashMap<String, List<String>> getSecondaryImageStr() {
		return secondaryImageStr;
	}
	public void setSecondaryImageStr(HashMap<String, List<String>> secondaryImageStr) {
		this.secondaryImageStr = secondaryImageStr;
	}
	public List<String> getPrimaryImage() {
		return primaryImage;
	}
	public void setPrimaryImage(List<String> primaryImage) {
		this.primaryImage = primaryImage;
	}
	public HashMap<Integer, List<String>> getSecondaryImage() {
		return secondaryImage;
	}
	public void setSecondaryImage(HashMap<Integer, List<String>> secondaryImage) {
		this.secondaryImage = secondaryImage;
		this.secondaryImageStr = new HashMap<String, List<String>>();
		Map<Integer, List<String>> map = secondaryImage;
		for (Entry<Integer, List<String>> entry : map.entrySet())
		{
		    System.out.println(entry.getKey() + "/" + entry.getValue());
		    this.secondaryImageStr.put(entry.getKey().toString(), entry.getValue());
		}
		
	}
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public int getDiv_id() {
		return div_id;
	}
	public void setDiv_id(int div_id) {
		this.div_id = div_id;
	}
	public int getSub_id() {
		return sub_id;
	}
	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}
	public int getQue_id() {
		return que_id;
	}
	public void setQue_id(int que_id) {
		this.que_id = que_id;
	}
	public int getAdded_by() {
		return added_by;
	}
	public void setAdded_by(int added_by) {
		this.added_by = added_by;
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
	public int getRep() {
		return rep;
	}
	public void setRep(int rep) {
		this.rep = rep;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public String getAns_id() {
		return ans_id;
	}
	public void setAns_id(String ans_id) {
		this.ans_id = ans_id;
	}
	public String getExam_rep() {
		return exam_rep;
	}
	public void setExam_rep(String exam_rep) {
		this.exam_rep = exam_rep;
	}
	public String getQues_status() {
		return ques_status;
	}
	public void setQues_status(String ques_status) {
		this.ques_status = ques_status;
	}
	public int getTopic_id() {
		return topic_id;
	}
	public void setTopic_id(int topic_id) {
		this.topic_id = topic_id;
	}
	public String getQue_text() {
		return que_text;
	}
	public void setQue_text(String que_text) {
		this.que_text = que_text;
	}
	public String getOpt_1() {
		return opt_1;
	}
	public void setOpt_1(String opt_1) {
		this.opt_1 = opt_1;
	}
	public String getOpt_2() {
		return opt_2;
	}
	public void setOpt_2(String opt_2) {
		this.opt_2 = opt_2;
	}
	public String getOpt_3() {
		return opt_3;
	}
	public void setOpt_3(String opt_3) {
		this.opt_3 = opt_3;
	}
	public String getOpt_4() {
		return opt_4;
	}
	public void setOpt_4(String opt_4) {
		this.opt_4 = opt_4;
	}
	public String getOpt_5() {
		return opt_5;
	}
	public void setOpt_5(String opt_5) {
		this.opt_5 = opt_5;
	}
	public String getOpt_6() {
		return opt_6;
	}
	public void setOpt_6(String opt_6) {
		this.opt_6 = opt_6;
	}
	public String getOpt_7() {
		return opt_7;
	}
	public void setOpt_7(String opt_7) {
		this.opt_7 = opt_7;
	}
	public String getOpt_8() {
		return opt_8;
	}
	public void setOpt_8(String opt_8) {
		this.opt_8 = opt_8;
	}
	public String getOpt_9() {
		return opt_9;
	}
	public void setOpt_9(String opt_9) {
		this.opt_9 = opt_9;
	}
	public String getOpt_10() {
		return opt_10;
	}
	public void setOpt_10(String opt_10) {
		this.opt_10 = opt_10;
	}
	public String getQue_type() {
		return que_type;
	}
	public void setQue_type(String que_type) {
		this.que_type = que_type;
	}
	
	
}
