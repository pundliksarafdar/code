package com.classapp.db.exam;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.hql.internal.ast.tree.OrderByClause;

import com.classapp.db.question.Questionbank;
import com.classapp.persistence.HibernateUtil;

public class ExamDB {

	public boolean saveExam(Exam exam) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(exam);
		transaction.commit();
		session.close();
		return  true;

	}
	
	public int getNextExamID(int inst_id,int div_id,int sub_id) {
		Transaction transaction=null;
		Session session=null;
		List<Integer> list = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select max(exam_id)+1 from Exam where  institute_id = :inst_id and div_id=:div_id and sub_id=:sub_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("sub_id", sub_id);
			list = query.list();
			if(list!=null){
				return list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
	return 1;
	}

	public boolean editExam(Exam exam) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(exam);
		transaction.commit();
		session.close();
		return  true;

	}
	
	public boolean deleteExam(int exam_id,int inst_id,int sub_id,int div_id) {
		Exam exam=new Exam();
		exam.setExam_id(exam_id);
		exam.setInstitute_id(inst_id);
		exam.setDiv_id(div_id);
		exam.setSub_id(sub_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.delete(exam);
		transaction.commit();
		session.close();
		return  true;

	}
	
	public boolean deleteExamrelatedtosubject(int sub_id) {
		Exam exam=new Exam();
		exam.setSub_id(sub_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.delete(exam);
		transaction.commit();
		session.close();
		return  true;

	}
	
	public boolean deleteExamrelatedtodivision(int div_id) {
		Exam exam=new Exam();
		exam.setDiv_id(div_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.delete(exam);
		transaction.commit();
		session.close();
		return  true;

	}
	
	public Exam getExam(int exam_id,int inst_id,int sub_id,int div_id) {
		Exam exam=new Exam();
		exam.setExam_id(exam_id);
		exam.setInstitute_id(inst_id);
		exam.setDiv_id(div_id);
		exam.setSub_id(sub_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		exam=(Exam) session.get(Exam.class,exam);
		transaction.commit();
		session.close();
		return  exam;
	}
	
	public boolean publishExam(int exam_id,int inst_id,int sub_id,int div_id,Timestamp start_time,Timestamp end_time) {
		Exam exam=new Exam();
		exam.setExam_id(exam_id);
		exam.setInstitute_id(inst_id);
		exam.setDiv_id(div_id);
		exam.setSub_id(sub_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		exam=(Exam) session.get(Exam.class,exam);
		exam.setStart_time(start_time);
		exam.setEnd_time(end_time);
		session.saveOrUpdate(exam);
		transaction.commit();
		session.close();
		return  true;
	}
	
	public boolean enableExam(int exam_id,int inst_id,int sub_id,int div_id,Timestamp start_time,Timestamp end_time) {
		Exam exam=new Exam();
		exam.setExam_id(exam_id);
		exam.setInstitute_id(inst_id);
		exam.setDiv_id(div_id);
		exam.setSub_id(sub_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		exam=(Exam) session.get(Exam.class,exam);
		exam.setStart_time(start_time);
		exam.setEnd_time(end_time);
		exam.setExam_status("Y");
		session.saveOrUpdate(exam);
		transaction.commit();
		session.close();
		return  true;
	}
	
	public boolean disableExam(int exam_id,int inst_id,int sub_id,int div_id) {
		Exam exam=new Exam();
		exam.setExam_id(exam_id);
		exam.setInstitute_id(inst_id);
		exam.setDiv_id(div_id);
		exam.setSub_id(sub_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		exam=(Exam) session.get(Exam.class,exam);
		exam.setExam_status("N");
		session.saveOrUpdate(exam);
		transaction.commit();
		session.close();
		return  true;
	}
	
	public boolean isenabledExam(int exam_id,int inst_id,int sub_id,int div_id) {
		Exam exam=new Exam();
		exam.setExam_id(exam_id);
		exam.setInstitute_id(inst_id);
		exam.setDiv_id(div_id);
		exam.setSub_id(sub_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		exam=(Exam) session.get(Exam.class,exam);
		if(exam.getExam_status()=="Y"){
			return true;
		}else if(exam.getExam_status()=="N"){
			return false;
		}
		transaction.commit();
		session.close();
		return  true;
	}
	
	public boolean removebatchfromexam(int inst_id,int div_id,String batchid) {
		//Exam exam=new Exam();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(Exam.class);
		Criterion criterion = Restrictions.eq("institute_id", inst_id);
		criteria.add(criterion);
		
		if(div_id!=-1){
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		}
		if(!"-1".equals(batchid)){
			
			criterion=Restrictions.or(Restrictions.like("batch_id", batchid+",%"),Restrictions.like("batch_id","%,"+batchid),Restrictions.like("batch_id", "%,"+batchid+",%"),Restrictions.eq("batch_id", batchid));
			criteria.add(criterion);
		}
	
		List<Exam> examList = criteria.list();
		if(examList!=null){
			for (int i = 0; i < examList.size(); i++) {
				Exam exam=examList.get(i);
				String batchidarr[]=exam.getBatch_id().split(",");
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
				exam.setBatch_id(newbatches);
				session.save(exam);
			}
		}
		transaction.commit();
		session.close();
		return  true;
	}
	public List<Exam> getExamList(int inst_id,int sub_id,int div_id,String exam_status,int currentPage,String batchids) {
	Transaction transaction=null;
	Session session=null;
	List<Exam> list = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		String queryString="from Exam where  institute_id = :inst_id and div_id=:div_id and sub_id=:sub_id";
		if(!"-1".equals(batchids) && !"".equals(batchids)){
			String batchidsarr[]=batchids.split(",");
			for (int i = 0; i < batchidsarr.length; i++) {
				if(i==0){
					queryString=queryString+" and ((batch_id like :batch_id"+i+"a or batch_id like :batch_id"+i+"b or batch_id like :batch_id"+i+"c or batch_id = :batch_id"+i+"d)";
				}else{
					queryString=queryString+"or (batch_id like :batch_id"+i+"a or batch_id like :batch_id"+i+"b or batch_id like :batch_id"+i+"c or batch_id = :batch_id"+i+"d)";
				}
			}
			queryString=queryString+")";
		}
		int startindex=0;
		if (currentPage!=1 && currentPage!=0) {
			startindex=(currentPage-1)*10;
		}
		Query query = session.createQuery(queryString);
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("sub_id", sub_id);
		if(!"-1".equals(batchids) && !"".equals(batchids)){
			String batchidsarr[]=batchids.split(",");
			for (int i = 0; i < batchidsarr.length; i++) {
				query.setParameter("batch_id"+i+"a", batchidsarr[i].trim()+",%");
				query.setParameter("batch_id"+i+"b","%,"+batchidsarr[i].trim()+",%");	
				query.setParameter("batch_id"+i+"c", "%,"+batchidsarr[i].trim());
				query.setParameter("batch_id"+i+"d", batchidsarr[i].trim());
		}
		}
		if(!"-1".equals(exam_status) && !"".equals(exam_status)){
			query.setParameter("exam_status", exam_status);
			queryString=queryString+"and exam_status= :exam_status";
		}
		query.setFirstResult(startindex);
		query.setMaxResults(10);
		list = query.list();
		
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
	}
return list;
}

	
	public List<CompExam> getCompExamSugg(String startwith) {
		CompExam exam=new CompExam();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(CompExam.class);
		Criterion criterion = Restrictions.eq("compexam_name", startwith+"%");
		List<CompExam> examList = criteria.list();
		transaction.commit();
		session.close();
		return  examList;
	}
	
	public List<CompExam> getAllCompExam(){
		CompExam exam=new CompExam();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(CompExam.class);
		Criterion criterion = Restrictions.eq("compexam_name", "%");
		criteria.addOrder(Order.asc("compexam_name"));
		List<CompExam> examList = criteria.list();
		transaction.commit();
		session.close();
		return  examList;
	}
	
	public int getExamListCount(int inst_id,int sub_id,int div_id,String exam_status,String batchid) {
		Exam exam=new Exam();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(Exam.class).setProjection(Projections.rowCount());
		Criterion criterion = Restrictions.eq("institute_id", inst_id);
		criteria.add(criterion);
		if(sub_id!=-1){
		criterion = Restrictions.eq("sub_id", sub_id);
		criteria.add(criterion);
		}
		if(div_id!=-1){
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		}
		if(exam_status!="-1"){
			criterion = Restrictions.eq("exam_status", exam_status);
			criteria.add(criterion);
		}
		if(!"-1".equals(batchid)){
			
			criterion=Restrictions.or(Restrictions.like("batch_id", batchid+",%"),Restrictions.like("batch_id","%,"+batchid),Restrictions.like("batch_id", "%,"+batchid+",%"),Restrictions.eq("batch_id", batchid));
			criteria.add(criterion);
		}
		long examListCount =  (Long) criteria.uniqueResult();
		//int count=examListCount;
		transaction.commit();
		session.close();
		return (int)examListCount;
	}
	
	public List<Exam> isQuestionAvailableInExam(int inst_id,int sub_id,int div_id,String ques_id) {
		Exam exam=new Exam();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(Exam.class);
		Criterion criterion = Restrictions.eq("institute_id", inst_id);
		criteria.add(criterion);
		if(sub_id!=-1){
		criterion = Restrictions.eq("sub_id", sub_id);
		criteria.add(criterion);
		}
		if(div_id!=-1){
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		}
		
		if(!"-1".equals(ques_id)){
			
			criterion=Restrictions.or(Restrictions.like("que_ids", ques_id+",%"),Restrictions.like("que_ids","%,"+ques_id),Restrictions.like("que_ids", "%,"+ques_id+",%"),Restrictions.eq("que_ids", ques_id));
			criteria.add(criterion);
		}
		List<Exam> list=  criteria.list();
		//int count=examListCount;
		transaction.commit();
		session.close();
		return list;
	}
	
	public List<Exam> getExamByIDs(int inst_id,int sub_id,int div_id,List<Integer> examids) {
		Exam exam=new Exam();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(Exam.class);
		Criterion criterion = Restrictions.eq("institute_id", inst_id);
		criteria.add(criterion);
		if(sub_id!=-1){
		criterion = Restrictions.eq("sub_id", sub_id);
		criteria.add(criterion);
		}
		if(div_id!=-1){
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		}
		
		criterion=Restrictions.in("exam_id", examids);
		criteria.add(criterion);
		
		List<Exam> list=  criteria.list();
		//int count=examListCount;
		transaction.commit();
		session.close();
		return list;
	}
	
	public List<Exam> getExamforStudents(int inst_id,int sub_id,int div_id,List<Integer> examids) {
		Exam exam=new Exam();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(Exam.class);
		Criterion criterion = Restrictions.eq("institute_id", inst_id);
		criteria.add(criterion);
		if(sub_id!=-1){
		criterion = Restrictions.eq("sub_id", sub_id);
		criteria.add(criterion);
		}
		if(div_id!=-1){
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		}
		if(examids.size()>0){
		criterion =Restrictions.not(Restrictions.in("exam_id", examids));
		criteria.add(criterion);
		}
		List<Exam> list=  criteria.list();
		//int count=examListCount;
		transaction.commit();
		session.close();
		return list;
	}
	
	public int getcount(int inst_id,int div_id,int sub_id,String batchids,String examstatus) {
		Transaction transaction=null;
		Session session=null;
		List<Integer> list = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			String queryString="from Exam where  institute_id = :inst_id and div_id=:div_id and sub_id=:sub_id";
			if(!"-1".equals(batchids) && !"".equals(batchids)){
				String batchidsarr[]=batchids.split(",");
				for (int i = 0; i < batchidsarr.length; i++) {
					if(i==0){
						queryString=queryString+" and ((batch_id like :batch_id"+i+"a or batch_id like :batch_id"+i+"b or batch_id like :batch_id"+i+"c or batch_id = :batch_id"+i+"d)";
					}else{
						queryString=queryString+"or (batch_id like :batch_id"+i+"a or batch_id like :batch_id"+i+"b or batch_id like :batch_id"+i+"c or batch_id = :batch_id"+i+"d)";
					}
				}
				queryString=queryString+")";
			}
			if(!"-1".equals(examstatus) && !"".equals(examstatus)){
				queryString=queryString+"and exam_status= :exam_status";
			}
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("sub_id", sub_id);
			if(!"-1".equals(batchids) && !"".equals(batchids)){
				String batchidsarr[]=batchids.split(",");
				for (int i = 0; i < batchidsarr.length; i++) {
					query.setParameter("batch_id"+i+"a", batchidsarr[i].trim()+",%");
					query.setParameter("batch_id"+i+"b","%,"+batchidsarr[i].trim()+",%");	
					query.setParameter("batch_id"+i+"c", "%,"+batchidsarr[i].trim());
					query.setParameter("batch_id"+i+"d", batchidsarr[i].trim());
			}
			}
			if(!"-1".equals(examstatus) && !"".equals(examstatus)){
				query.setParameter("exam_status", examstatus);
				queryString=queryString+"and exam_status= :exam_status";
			}
			list = query.list();
			if(list!=null){
				return list.size();
			}
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
	return 0;
	}
	
	public boolean isExamExists(int inst_id,String examname,String examID) {
		Exam exam=new Exam();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(Exam.class).setProjection(Projections.property("exam_name"));
		Criterion criterion = Restrictions.eq("institute_id", inst_id);
		criteria.add(criterion);
				
		criterion=Restrictions.eq("exam_name", examname);
		criteria.add(criterion);
		if(!"".equals(examID) && examID!=null){
			criterion=Restrictions.ne("exam_id", Integer.parseInt(examID));
			criteria.add(criterion);
		}
		List<Exam> list=  criteria.list();
		if(list!=null){
			if(list.size()>0){
				return true;
			}
		}
		//int count=examListCount;
		transaction.commit();
		session.close();
		return false;
	}
}
