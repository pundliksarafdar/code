package com.classapp.db.question;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.exam.Exam;
import com.classapp.db.notificationpkg.Notification;
import com.classapp.persistence.HibernateUtil;
import com.datalayer.exam.QuestionSearchRequest;

public class QuestionbankDB {
public boolean saveQuestion(Questionbank questionbank) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	session.saveOrUpdate(questionbank);
	transaction.commit();
	return  true;

}

public boolean editQuestion(Questionbank questionbank) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	session.saveOrUpdate(questionbank);
	transaction.commit();
	return  true;

}

public boolean deleteQuestion(int que_id,int inst_id,int sub_id,int div_id) {
	Questionbank questionbank=new Questionbank();
	questionbank.setInst_id(inst_id);
	questionbank.setDiv_id(div_id);
	questionbank.setSub_id(sub_id);
	questionbank.setQue_id(que_id);
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	session.delete(questionbank);
	transaction.commit();
	return  true;

}

public Questionbank getQuestion(int que_id,int inst_id,int sub_id,int div_id) {
	Questionbank questionbank=new Questionbank();
	questionbank.setInst_id(inst_id);
	questionbank.setDiv_id(div_id);
	questionbank.setSub_id(sub_id);
	questionbank.setQue_id(que_id);
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Questionbank question=(Questionbank) session.get(Questionbank.class,questionbank);
	transaction.commit();
	return  question;
}

public HashMap<Integer, Integer> getQuestionIds(int inst_id,int sub_id,int div_id,int repeat,List<Integer> marks) {
	Transaction transaction=null;
	Session session=null;
	List<Questionbank> list = null;
	marks.add(1);
	HashMap<Integer, Integer> quesids=new HashMap<Integer, Integer>();
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("from Questionbank where  inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and rep <= :rep and marks in :marks");
		if(repeat==-1){
			query = session.createQuery("from Questionbank where  inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and marks in :marks");
		}
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("sub_id", sub_id);
		query.setParameter("rep", repeat);
		query.setParameterList("marks", marks);
		list = query.list();
		if(list.size()>0){
			int i=0;
			while(i<list.size()){
				Questionbank obj= list.get(i);
				quesids.put(obj.getQue_id()	, obj.getMarks());
				i++;
			}
		}
		
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
	}
return quesids;
}

public List<Integer> getQuestionMarks(int inst_id,int sub_id,int div_id,List<Integer> que_id) {
	Transaction transaction=null;
	Session session=null;
	List<Integer> list = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("select marks from Questionbank where  inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and que_id in :que_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("sub_id", sub_id);
		query.setParameterList("que_id", que_id);
		list = query.list();
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
	}
return list;
}

public int getNextQuestionID(int inst_id,int div_id,int sub_id) {
	Transaction transaction=null;
	Session session=null;
	List<Integer> list = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("select max(que_id)+1 from Questionbank where  inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id");
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

public List<Questionbank> getSearchedQuestion(int rep,String compexam_id,int marks,int sub_id,int inst_id,int div_id,int currentPage) {
	Exam exam=new Exam();
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Criteria criteria = session.createCriteria(Questionbank.class);
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", sub_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	if(rep!=-1){
		criterion = Restrictions.eq("rep", rep);
		criteria.add(criterion);
	}
	if(!"-1".equals(compexam_id)){
		
		criterion=Restrictions.or(Restrictions.like("exam_rep", compexam_id+",%"),Restrictions.like("exam_rep","%,"+compexam_id),Restrictions.like("exam_rep", "%,"+compexam_id+",%"),Restrictions.eq("exam_rep", compexam_id));
		criteria.add(criterion);
	}
	if(marks!=-1){
		criterion = Restrictions.eq("marks", marks);
		criteria.add(criterion);
	}
	if(currentPage!=0 &&  currentPage!=1){
		criteria.setFirstResult((currentPage-1)*2);
	}
	criteria.setMaxResults(2);
	List<Questionbank> questionList = criteria.list();
	transaction.commit();
	session.close();
	return  questionList;	
}

public int getSearchedQuestionCount(int rep,String compexam_id,int marks,int sub_id,int inst_id,int div_id) {
	Exam exam=new Exam();
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Criteria criteria = session.createCriteria(Questionbank.class);
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", sub_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	if(rep!=-1){
		criterion = Restrictions.eq("rep", rep);
		criteria.add(criterion);
	}
	if(!"-1".equals(compexam_id)){
		
		criterion=Restrictions.or(Restrictions.like("exam_rep", compexam_id+",%"),Restrictions.like("exam_rep","%,"+compexam_id),Restrictions.like("exam_rep", "%,"+compexam_id+",%"),Restrictions.eq("exam_rep", compexam_id));
		criteria.add(criterion);
	}
	if(marks!=-1){
		criterion = Restrictions.eq("marks", marks);
		criteria.add(criterion);
	}
	
	//criteria.setMaxResults(10);
	List<Questionbank> questionList = criteria.list();
	transaction.commit();
	session.close();
	if(questionList!=null){
		return questionList.size();
	}
	return  0;	
}

public List<Integer> getdistinctQuestionMarks(int sub_id,int inst_id,int div_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Criteria criteria = session.createCriteria(Questionbank.class).setProjection(Projections.distinct(Projections.property("marks")));;
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", sub_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	
	List<Integer> questionList = criteria.list();
	transaction.commit();
	session.close();
	return  questionList;	
}
public List<Integer> getdistinctQuestionRep(int sub_id,int inst_id,int div_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Criteria criteria = session.createCriteria(Questionbank.class).setProjection(Projections.distinct(Projections.property("rep")));;
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", sub_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	
	List<Integer> questionList = criteria.list();
	transaction.commit();
	session.close();
	return  questionList;	
}

public List<QuestionSearchRequest> getCriteriaQuestionCount(int sub_id,int inst_id,int div_id,List<QuestionSearchRequest> list) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Criteria criteria = session.createCriteria(Questionbank.class).setProjection(Projections.rowCount());;
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", sub_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	for (int i = 0; i < list.size(); i++) {
		if(list.get(i).getMarks()!=-1){
			criteria.add(Restrictions.eq("marks", list.get(i).getMarks()));
		}
		if(list.get(i).getMaximumRepeatation()!=-1){
			criteria.add(Restrictions.le("maximumRepeatation", list.get(i).getMaximumRepeatation()));
		}
		list.get(i).setAvailiblityCount((Integer) criteria.uniqueResult());
	}

	//List<Integer> questionList = criteria.list();
	transaction.commit();
	session.close();
return list;
}

public List<QuestionSearchRequest> getCriteriaQuestion(int sub_id,int inst_id,int div_id,List<QuestionSearchRequest> list) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Criteria criteria = session.createCriteria(Questionbank.class).setProjection(Projections.property("que_id")).addOrder(Order.asc("created_dt"));
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", sub_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	
	for (int i = 0; i < list.size(); i++) {
		if(list.get(i).getMarks()!=-1){
			criteria.add(Restrictions.eq("marks", list.get(i).getMarks()));
		}
		if(list.get(i).getMaximumRepeatation()!=-1){
			criteria.add(Restrictions.le("rep", list.get(i).getMaximumRepeatation()));
		}
		List<Integer> questionList = criteria.list();
	list.get(i).setQuestionId(questionList);
	}
	transaction.commit();
	session.close();
return list;
}

public List<String> getQuestionAnsIds(int sub_id,int inst_id,int div_id,List<Integer> que_ids) {
	Exam exam=new Exam();
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Criteria criteria = session.createCriteria(Questionbank.class).setProjection(Projections.property("ans_id"));
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", sub_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	criterion=Restrictions.in("que_id", que_ids);
	criteria.setMaxResults(2);
	List<String> questionAnsList = criteria.list();
	transaction.commit();
	session.close();
	return  questionAnsList;	
}

}
