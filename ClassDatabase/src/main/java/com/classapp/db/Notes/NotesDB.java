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
	
public List<Notes> getStudentNotesPath(String batch,int subid,int classid) {
		
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Notes where classid=:classid and subid=:subid and (batch like :batch or batch like :batch1 or batch like :batch2 or batch = :batch3)");
			query.setParameter("classid", classid);
			query.setParameter("subid", subid);
			query.setParameter("batch", batch+",%");
			query.setParameter("batch1", "%,"+batch+",%");
			query.setParameter("batch2", "%,"+batch);
			query.setParameter("batch3", batch);
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
	
	public  boolean validatenotesname(String notesname,int regID) {
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Notes where name=:notesname and classid=:classid" );
			query.setParameter("notesname", notesname);
			query.setParameter("classid", regID);
			notesList = query.list();
			if (notesList.size()>0) {
				return true;
			}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		return false;
		
	}
	
	public  void updatenotes(String notesname,int notesid,String batchids) {
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update Notes set name=:notesname , batch=:batch where notesid=:notesid" );
			query.setParameter("notesname", notesname);
			query.setParameter("batch", batchids);
			query.setParameter("notesid", notesid);
			query.executeUpdate();
			transaction.commit();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		
	}
	
	public  void deletenotes(int notesid) {
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete from Notes where notesid=:notesid" );
			query.setParameter("notesid", notesid);
			query.executeUpdate();
			transaction.commit();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		
	}
}
