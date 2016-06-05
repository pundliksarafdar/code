package com.classapp.db.Notes;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.Teacher.Teacher;
import com.classapp.db.exam.Exam;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.Schedule.Schedule;
import com.classapp.persistence.HibernateUtil;

public class NotesDB {
	public Boolean add(Notes notes) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		int notesID=getNotesNextID(notes.getSubid(), notes.getInst_id(), notes.getDivid());
		notes.setNotesid(notesID);
			session.save(notes);
			transaction.commit();
			session.close();
		return  true;
	
	}
	
public int getNotesNextID(int subid,int classid,int div_id) {
		
		Session session = null;
		Transaction transaction = null;
		List<Integer> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select max(notesid)+1 from Notes where classid=:classid and subid=:subid and divid=:divid");
			query.setParameter("classid", classid);
			query.setParameter("subid", subid);
			query.setParameter("divid", div_id);
			notesList = query.list();
			if(notesList!=null){
				if(notesList.get(0)!=null){
				return notesList.get(0);
				}
			}
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
				
		return 1;
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
				startindex=(currentPage-1)*10;
			}
			query.setFirstResult(startindex);
			query.setMaxResults(10);
			notesList = query.list();
			
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
				
		return notesList;
	}
	
/*
 * Remove above function if not required
 * */	
public List<Notes> getNotesPath(int divid,int subid,int classid,String batchids) {
		
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
			
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
				
		return notesList;
	}

public List<Notes> getNotesPath(int divid,int subid,int classid) {
	
	Session session = null;
	Transaction transaction = null;
	List<Notes> notesList = null;
	String queryString="from Notes where inst_id=:inst_id and divid=:divid and subid=:subid";
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery(queryString);
		query.setParameter("inst_id", classid);
		query.setParameter("divid", divid);
		query.setParameter("subid", subid);
		
		notesList = query.list();
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
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
			}finally{
				if(null!=session){
					session.close();
				}
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
			}finally{
				if(null!=session){
					session.close();
				}
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
			Query query = session.createQuery("from Notes where notesid=:notesid and inst_id=:inst_id and divid=:divid and subid=:subid");
			query.setParameter("notesid", id);
			query.setParameter("inst_id", inst_id);
			query.setParameter("divid", div_id);
			query.setParameter("subid", sub_id);
			notesList = query.list();
			
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
				
		return notesList;
		
	}
	
	public  boolean validatenotesname(String notesname,int inst_id,int division,int subject) {
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Notes where name=:notesname and inst_id=:inst_id and divid = :divid and subid = :subid" );
			query.setParameter("notesname", notesname);
			query.setParameter("inst_id", inst_id);
			query.setParameter("divid", division);
			query.setParameter("subid", subject);
			notesList = query.list();
			if (notesList.size()>0) {
				return true;
			}
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
				
		return false;
		
	}
	
	public  boolean validateUpdateNotesName(String notesname,int inst_id,int notesID,int div_id,int sub_id) {
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Notes where name=:notesname and notesid!=:notesid and inst_id=:inst_id and divid = :divid and subid =:subid" );
			query.setParameter("notesname", notesname);
			query.setParameter("notesid", notesID);
			query.setParameter("inst_id", inst_id);
			query.setParameter("divid", div_id);
			query.setParameter("subid", sub_id);
			notesList = query.list();
			if (notesList.size()>0) {
				return true;
			}
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
				
		return false;
		
	}
	
	
	public  void updatenotes(String notesname,String notespath,int notesid,String batchids,int inst_id,int div_id,int sub_id) {
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update Notes set name=:notesname,notespath=:notespath , batch=:batch where notesid=:notesid and inst_id=:inst_id and divid=:divid and subid=:subid" );
			query.setParameter("notesname", notesname);
			query.setParameter("notespath", notespath);
			query.setParameter("batch", batchids);
			query.setParameter("notesid", notesid);
			query.setParameter("inst_id", inst_id);
			query.setParameter("divid", div_id);
			query.setParameter("subid", sub_id);
			query.executeUpdate();
			transaction.commit();
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
				
		
	}
	
	public  void deletenotes(int notesid,int inst_id,int div_id,int sub_id) {
		Session session = null;
		Transaction transaction = null;
		List<Notes> notesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete from Notes where notesid=:notesid and inst_id=:inst_id and divid=:divid and subid=:subid" );
			query.setParameter("notesid", notesid);
			query.setParameter("inst_id", inst_id);
			query.setParameter("divid", div_id);
			query.setParameter("subid", sub_id);
			query.executeUpdate();
			transaction.commit();
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
				
		
	}
	
	public Boolean deleteNotesRelatedToSubject(int inst_id,int sub_id) {
		Transaction transaction=null;
		Session session=null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete from Notes where classid=:classid and subid=:subid" );
			query.setParameter("classid", inst_id);
			query.setParameter("subid", sub_id);
			query.executeUpdate();
			transaction.commit();
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
		return  true;
	
	}
	
	public Boolean deleteNotesRelatedToDivision(int inst_id,int div_id) {
		Transaction transaction=null;
		Session session=null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete from Notes where classid=:classid and divid=:divid" );
			query.setParameter("classid", inst_id);
			query.setParameter("divid", div_id);
			query.executeUpdate();
			transaction.commit();
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
		return  true;
	
	}
	
	public boolean removebatchfromnotes(int inst_id,int div_id,String batchid) {
		//Exam exam=new Exam();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(Notes.class);
		Criterion criterion = Restrictions.eq("classid", inst_id);
		criteria.add(criterion);
		
		if(div_id!=-1){
		criterion = Restrictions.eq("divid", div_id);
		criteria.add(criterion);
		}
		if(!"-1".equals(batchid)){
			
			criterion=Restrictions.or(Restrictions.like("batch", batchid+",%"),Restrictions.like("batch","%,"+batchid),Restrictions.like("batch", "%,"+batchid+",%"),Restrictions.eq("batch", batchid));
			criteria.add(criterion);
		}
	
		List<Notes> notesList = criteria.list();
		if(notesList!=null){
			for (int i = 0; i < notesList.size(); i++) {
				Notes notes=notesList.get(i);
				String batchidarr[]=notes.batch.split(",");
				String newbatches="";
				int index=1;
				for (int j = 0; j < batchidarr.length; j++) {
					if(!batchid.equals(batchidarr[j].trim())){
						if(index==1){
							newbatches=batchidarr[j];
						}else{
							newbatches=newbatches+","+batchidarr[j];
						}
						index++;
					}
				}
				notes.setBatch(newbatches);
				session.save(notes);
			}
		}
		transaction.commit();
		session.close();
		return  true;
	}
}
