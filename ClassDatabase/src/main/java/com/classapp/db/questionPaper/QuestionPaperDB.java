package com.classapp.db.questionPaper;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;

public class QuestionPaperDB {

	public int save(QuestionPaper questionPaper) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(questionPaper);
		transaction.commit();
		System.out.println("Quesbank ID:-"+questionPaper.getPaper_id());
		if(session!=null){
			session.close();
		}
		return  questionPaper.getPaper_id();
	}
}
