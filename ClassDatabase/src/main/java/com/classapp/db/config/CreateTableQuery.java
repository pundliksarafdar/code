package com.classapp.db.config;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;

import javassist.bytecode.stackmap.BasicBlock.Catch;

public class CreateTableQuery {
	static Session session = null;
	static Transaction transaction = null;
	
	public static void runScript(String script){
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createSQLQuery(script);
			query.executeUpdate();
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		runScript("");
	}
}
