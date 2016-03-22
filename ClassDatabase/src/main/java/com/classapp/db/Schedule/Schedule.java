package com.classapp.db.Schedule;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class Schedule implements Serializable {
	int inst_id;
	int div_id;
	int batch_id;
	int sub_id;
	int teacher_id;
	Time start_time;
	Time end_time;
	Date date;
	int schedule_id;
	int grp_id;
	String rep_days;
	public int getSchedule_id() {
		return schedule_id;
	}

	public void setSchedule_id(int schedule_id) {
		this.schedule_id = schedule_id;
	}

	public int getInst_id() {
		return inst_id;
	}

	public void setInst_id(int class_id) {
		this.inst_id = class_id;
	}

	public int getDiv_id() {
		return div_id;
	}

	public void setDiv_id(int div_id) {
		this.div_id = div_id;
	}

	public int getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}

	public int getSub_id() {
		return sub_id;
	}

	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}

	public int getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(int teacher_id) {
		this.teacher_id = teacher_id;
	}

	public Time getStart_time() {
		return start_time;
	}

	public void setStart_time(Time start_time) {
		this.start_time = start_time;
	}

	public Time getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Time end_time) {
		this.end_time = end_time;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public int getGrp_id() {
		return grp_id;
	}

	public void setGrp_id(int grp_id) {
		this.grp_id = grp_id;
	}

	public String getRep_days() {
		return rep_days;
	}

	public void setRep_days(String rep_days) {
		this.rep_days = rep_days;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + batch_id;
		result = prime * result + inst_id;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + div_id;
		result = prime * result + ((end_time == null) ? 0 : end_time.hashCode());
		result = prime * result + schedule_id;
		result = prime * result + ((start_time == null) ? 0 : start_time.hashCode());
		result = prime * result + sub_id;
		result = prime * result + teacher_id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Schedule other = (Schedule) obj;
		if (batch_id != other.batch_id)
			return false;
		if (inst_id != other.inst_id)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (div_id != other.div_id)
			return false;
		if (end_time == null) {
			if (other.end_time != null)
				return false;
		} else if (!end_time.equals(other.end_time))
			return false;
		if (schedule_id != other.schedule_id)
			return false;
		if (start_time == null) {
			if (other.start_time != null)
				return false;
		} else if (!start_time.equals(other.start_time))
			return false;
		if (sub_id != other.sub_id)
			return false;
		if (teacher_id != other.teacher_id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Schedule [class_id=" + inst_id + ", div_id=" + div_id + ", batch_id=" + batch_id + ", sub_id=" + sub_id
				+ ", teacher_id=" + teacher_id + ", start_time=" + start_time + ", end_time=" + end_time + ", date="
				+ date + ", schedule_id=" + schedule_id + "]";
	}

}
