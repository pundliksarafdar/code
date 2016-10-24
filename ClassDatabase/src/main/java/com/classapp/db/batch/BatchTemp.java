package com.classapp.db.batch;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;

@Entity
@Table(name="BatchTemp",schema="classdb")
public class BatchTemp implements Serializable {
	@Column(name="id")
	private int batch_id;
	private int class_id;
	private int div_id;
	private String batch_name;
	private String sub_id;
	private String status;
	private int roll_count;
	public int getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	public int getDiv_id() {
		return div_id;
	}
	public void setDiv_id(int div_id) {
		this.div_id = div_id;
	}
	public String getBatch_name() {
		return batch_name;
	}
	public void setBatch_name(String batch_name) {
		this.batch_name = batch_name;
	}
	public String getSub_id() {
		return sub_id;
	}
	public void setSub_id(String sub_id) {
		this.sub_id = sub_id;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getRoll_count() {
		return roll_count;
	}
	public void setRoll_count(int roll_count) {
		this.roll_count = roll_count;
	}
	@Override
	public boolean equals(Object obj) {
		BatchTemp batch=(BatchTemp)obj;
		if(batch.getBatch_id()==this.batch_id && batch.getBatch_name().equals(this.batch_name) && batch.getClass_id()==this.class_id && batch.getDiv_id()==this.div_id && batch.getSub_id().equals(this.sub_id)){
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	public static void main(String[] args) {
		String status = "0";
		Session session = null;
		Transaction transaction = null;
		BatchTemp batch = new BatchTemp();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(batch);
			transaction.commit();
		}catch(Exception e){
			status = "1";
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
	}
}
