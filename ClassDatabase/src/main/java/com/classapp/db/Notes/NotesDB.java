package com.classapp.db.Notes;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.Schedule.Schedule;
import com.classapp.db.Teacher.Teacher;
import com.classapp.db.register.RegisterBean;
import com.classapp.persistence.HibernateUtil;

public class NotesDB {
	public Boolean add(Notes notes) {
		Transaction transaction=null;
		Session session=null;
		List<RegisterBean> list=null;
		List<Teacher> list2=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
			session.save(notes);
			transaction.commit();
		return  true;
	
	}
	
	public List<Notes> getNotesPath(int divid,int subid,int classid) {
		
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Notes where classid=:classid and divid=:divid and subid=:subid");
			query.setParameter("classid", classid);
			query.setParameter("divid", divid);
			query.setParameter("subid", subid);
			notesList = query.list();
			
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		return notesList;
	}
	
	public List<Notes> getNotesPathById(int id) {
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Notes where notesid=:notesid");
			query.setParameter("notesid", id);
			notesList = query.list();
			
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		return notesList;
		
	}
}
