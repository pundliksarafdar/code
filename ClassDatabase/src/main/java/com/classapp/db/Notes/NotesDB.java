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
	
	public List<Notes> getNotesPath(int divid,int subid,int classid,int currentPage,String batchids) {
		
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		String queryString="from Notes where classid=:classid and divid=:divid and subid=:subid";
		if(!"-1".equals(batchids) && !"".equals(batchids)){
			String batchidsarr[]=batchids.split(",");
			for (int i = 0; i < batchidsarr.length; i++) {
				if(i==0){
					queryString=queryString+" and ((batch like :batch_id"+i+"a or batch like :batch_id"+i+"b or batch like :batch_id"+i+"c or batch = :batch_id"+i+"d)";
				}else{
					queryString=queryString+"or (batch like :batch_id"+i+"a or batch like :batch_id"+i+"b or batch like :batch_id"+i+"c or batch = :batch_id"+i+"d)";
				}
			}
			queryString=queryString+")";
		}
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("classid", classid);
			query.setParameter("divid", divid);
			query.setParameter("subid", subid);
			if(!"-1".equals(batchids) && !"".equals(batchids)){
				String batchidsarr[]=batchids.split(",");
				for (int i = 0; i < batchidsarr.length; i++) {
					query.setParameter("batch_id"+i+"a", batchidsarr[i].trim()+",%");
					query.setParameter("batch_id"+i+"b","%,"+batchidsarr[i].trim()+",%");	
					query.setParameter("batch_id"+i+"c", "%,"+batchidsarr[i].trim());
					query.setParameter("batch_id"+i+"d", batchidsarr[i].trim());
			}
			}
			int startindex=0;
			if (currentPage!=1 && currentPage!=0) {
				startindex=(currentPage-1)*2;
			}
			query.setFirstResult(startindex);
			query.setMaxResults(2);
			notesList = query.list();
			
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		return notesList;
	}
	
public int getNotescount(int divid,int subid,int classid,String batchids) {
		
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		String queryString="from Notes where classid=:classid and divid=:divid and subid=:subid";
		if(!"-1".equals(batchids) && !"".equals(batchids)){
			String batchidsarr[]=batchids.split(",");
			for (int i = 0; i < batchidsarr.length; i++) {
				if(i==0){
					queryString=queryString+" and ((batch like :batch_id"+i+"a or batch like :batch_id"+i+"b or batch like :batch_id"+i+"c or batch = :batch_id"+i+"d)";
				}else{
					queryString=queryString+"or (batch like :batch_id"+i+"a or batch like :batch_id"+i+"b or batch like :batch_id"+i+"c or batch = :batch_id"+i+"d)";
				}
			}
			queryString=queryString+")";
		}
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("classid", classid);
			query.setParameter("divid", divid);
			query.setParameter("subid", subid);
			if(!"-1".equals(batchids) && !"".equals(batchids)){
				String batchidsarr[]=batchids.split(",");
				for (int i = 0; i < batchidsarr.length; i++) {
					query.setParameter("batch_id"+i+"a", batchidsarr[i].trim()+",%");
					query.setParameter("batch_id"+i+"b","%,"+batchidsarr[i].trim()+",%");	
					query.setParameter("batch_id"+i+"c", "%,"+batchidsarr[i].trim());
					query.setParameter("batch_id"+i+"d", batchidsarr[i].trim());
			}
			}
			notesList = query.list();
			if(notesList!=null){
				return notesList.size();
			}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		return 0;
	}
	
public List<Notes> getStudentNotesPath(String batch,int subid,int classid,int div_id) {
		
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Notes where classid=:classid and subid=:subid and (batch like :batch or batch like :batch1 or batch like :batch2 or batch = :batch3) and divid=:divid");
			query.setParameter("classid", classid);
			query.setParameter("subid", subid);
			query.setParameter("batch", batch+",%");
			query.setParameter("batch1", "%,"+batch+",%");
			query.setParameter("batch2", "%,"+batch);
			query.setParameter("batch3", batch);
			query.setParameter("divid", div_id);
			notesList = query.list();
			
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		return notesList;
	}
	
	public List<Notes> getNotesPathById(int id,int inst_id,int sub_id,int div_id) {
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Notes where notesid=:notesid and classid=:classid and divid=:divid and subid=:subid");
			query.setParameter("notesid", id);
			query.setParameter("classid", inst_id);
			query.setParameter("divid", div_id);
			query.setParameter("subid", sub_id);
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
	
	public  boolean validatenotesnamebyID(String notesname,int regID,int notesID) {
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Notes where name=:notesname and notesid!=:notesid" );
			query.setParameter("notesname", notesname);
			query.setParameter("notesid", notesID);
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
	
	
	public  void updatenotes(String notesname,int notesid,String batchids,int inst_id,int div_id,int sub_id) {
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update Notes set name=:notesname , batch=:batch where notesid=:notesid and classid=:classid and divid=:divid and subid=:subid" );
			query.setParameter("notesname", notesname);
			query.setParameter("batch", batchids);
			query.setParameter("notesid", notesid);
			query.setParameter("classid", inst_id);
			query.setParameter("divid", div_id);
			query.setParameter("subid", sub_id);
			query.executeUpdate();
			transaction.commit();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		
	}
	
	public  void deletenotes(int notesid,int inst_id,int div_id,int sub_id) {
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete from Notes where notesid=:notesid and classid=:classid and divid=:divid and subid=:subid" );
			query.setParameter("notesid", notesid);
			query.setParameter("classid", inst_id);
			query.setParameter("divid", div_id);
			query.setParameter("subid", sub_id);
			query.executeUpdate();
			transaction.commit();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		
	}
}
