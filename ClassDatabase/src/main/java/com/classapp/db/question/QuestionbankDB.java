package com.classapp.db.question;

import java.io.Serializable;
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
import com.service.beans.GenerateQuestionPaperServicebean;

public class QuestionbankDB {
public int saveQuestion(Questionbank questionbank) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	session.saveOrUpdate(questionbank);
	transaction.commit();
	System.out.println("Quesbank ID:-"+questionbank.getQue_id());
	if(session!=null){
		session.close();
	}
	return  questionbank.getQue_id();

}

public boolean editQuestion(Questionbank questionbank) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	session.saveOrUpdate(questionbank);
	transaction.commit();
	if(session!=null){
		session.close();
	}
	return  true;

}

public boolean updateSubjectiveQuestion(int que_id,int inst_id,int sub_id,int div_id,String que_text,int marks) {
	Transaction transaction=null;
	Session session=null;
	try{
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Query query = session.createQuery("update Questionbank set que_text = :que_text , marks = :marks where  inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and que_id = :que_id");
	query.setParameter("inst_id", inst_id);
	query.setParameter("div_id", div_id);
	query.setParameter("sub_id", sub_id);
	query.setParameter("que_id", que_id);
	query.setParameter("que_text", que_text);
	query.setParameter("marks", marks);
	query.executeUpdate();
	transaction.commit();
	}catch(Exception e){
	e.printStackTrace();
	if(null!=transaction){
		transaction.rollback();
	}
	
	}finally{
	if(session!=null){
		session.close();
	}
	}
	return  true;

}

public boolean updateParagraphQuestion(int que_id,int inst_id,int sub_id,int div_id,int marks) {
	Transaction transaction=null;
	Session session=null;
	try{
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Query query = session.createQuery("update Questionbank set marks = :marks where  inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and que_id = :que_id");
	query.setParameter("inst_id", inst_id);
	query.setParameter("div_id", div_id);
	query.setParameter("sub_id", sub_id);
	query.setParameter("que_id", que_id);
	query.setParameter("marks", marks);
	query.executeUpdate();
	transaction.commit();
	}catch(Exception e){
	e.printStackTrace();
	if(null!=transaction){
		transaction.rollback();
	}
	
	}finally{
	if(session!=null){
		session.close();
	}
	}
	return  true;

}

public boolean updateDeleteQuestionStatus(int que_id,int inst_id,int sub_id,int div_id) {
	Transaction transaction=null;
	Session session=null;
	try{
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Query query = session.createQuery("update Questionbank set ques_status = 'N' where  inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and que_id = :que_id");
	query.setParameter("inst_id", inst_id);
	query.setParameter("div_id", div_id);
	query.setParameter("sub_id", sub_id);
	query.setParameter("que_id", que_id);
	transaction.commit();
	query.executeUpdate();
	}catch(Exception e){
	e.printStackTrace();
	if(null!=transaction){
		transaction.rollback();
	}
	
	}finally{
	if(session!=null){
		session.close();
	}
	}
	return  true;

}

public boolean deleteQuestion(int que_id,int inst_id,int sub_id,int div_id) {
	Transaction transaction=null;
	Session session=null;
	try{
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Query query = session.createQuery("delete from Questionbank where  inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and que_id = :que_id");
	query.setParameter("inst_id", inst_id);
	query.setParameter("div_id", div_id);
	query.setParameter("sub_id", sub_id);
	query.setParameter("que_id", que_id);
	transaction.commit();
	query.executeUpdate();
	}catch(Exception e){
	e.printStackTrace();
	if(null!=transaction){
		transaction.rollback();
	}
	
	}finally{
	if(session!=null){
		session.close();
	}
	}
	return  true;

}

public Questionbank getQuestion(int que_id,int inst_id,int sub_id,int div_id) {
	Questionbank questionbank=new Questionbank();
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("from Questionbank where  inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and que_id = :que_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("sub_id", sub_id);
		query.setParameter("que_id", que_id);
		transaction.commit();
		questionbank=(Questionbank) query.uniqueResult();
		}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
		}finally{
		if(session!=null){
			session.close();
		}
		}
	return  questionbank;
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
		
	}finally{
		if(null!=session){
			session.close();
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
		
	}finally{
		if(null!=session){
			session.close();
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
		
	}finally{
		if(null!=session){
			session.close();
		}
	}
		
return 1;
}

public List<Questionbank> getSearchedQuestion(int marks,int sub_id,int inst_id,int div_id,int currentPage,int topic_id,String quesType) {
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

	if(marks!=-1){
		criterion = Restrictions.eq("marks", marks);
		criteria.add(criterion);
	}
	
	if(topic_id!=-1){
		criterion = Restrictions.eq("topic_id", topic_id);
		criteria.add(criterion);
	}
	criterion = Restrictions.eq("que_type", quesType);
	criteria.add(criterion);
	criterion = Restrictions.ne("ques_status", "N");
	criteria.add(criterion);
	if(currentPage!=0 &&  currentPage!=1){
		criteria.setFirstResult((currentPage-1)*50);
	}
	
	criteria.setMaxResults(50);
	criteria.addOrder(Order.asc("que_id"));
	List<Questionbank> questionList = criteria.list();
	transaction.commit();
	session.close();
	return  questionList;	
}

public int getSearchedQuestionCount(int marks,int sub_id,int inst_id,int div_id,int topic_id,String quesType) {
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
	if(marks!=-1){
		criterion = Restrictions.eq("marks", marks);
		criteria.add(criterion);
	}
	if(topic_id!=-1){
		criterion = Restrictions.eq("topic_id", topic_id);
		criteria.add(criterion);
	}
	criterion = Restrictions.eq("que_type", quesType);
	criteria.add(criterion);
	criterion = Restrictions.ne("ques_status", "N");
	criteria.add(criterion);
	//criteria.setMaxResults(10);
	List<Questionbank> questionList = criteria.list();
	transaction.commit();
	session.close();
	if(questionList!=null){
		return questionList.size();
	}
	return  0;	
}

public List<Integer> getdistinctQuestionMarks(int sub_id,int inst_id,int div_id,String ques_type) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Criteria criteria = session.createCriteria(Questionbank.class).setProjection(Projections.distinct(Projections.property("marks"))).addOrder(Order.asc("marks"));;
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", sub_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("que_type", ques_type);
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
	for (int i = 0; i < list.size(); i++) {
	Criteria criteria = session.createCriteria(Questionbank.class).setProjection(Projections.rowCount());;
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", sub_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	
		if(list.get(i).getMarks()!=-1){
			criteria.add(Restrictions.eq("marks", list.get(i).getMarks()));
		}
		if(list.get(i).getMaximumRepeatation()!=-1){
			criteria.add(Restrictions.le("maximumRepeatation", list.get(i).getMaximumRepeatation()));
		}
		list.get(i).setAvailiblityCount((Long) criteria.uniqueResult());
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
	for (int i = 0; i < list.size(); i++) {
	Criteria criteria = session.createCriteria(Questionbank.class).setProjection(Projections.property("que_id")).addOrder(Order.asc("created_dt"));
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", sub_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	
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

public List<GenerateQuestionPaperServicebean> getQuestionsForGenerateExam(int inst_id,int div_id,List<GenerateQuestionPaperServicebean> list) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	for (int i = 0; i < list.size(); i++) {
	Criteria criteria = session.createCriteria(Questionbank.class).setProjection(Projections.property("que_id")).addOrder(Order.asc("created_dt"));
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", list.get(i).getSubject_id());
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	
		if(list.get(i).getMarks()!=-1){
			criteria.add(Restrictions.eq("marks", list.get(i).getMarks()));
		}
		
		List<Integer> questionList = criteria.list();
	list.get(i).setQuestion_ids(questionList);
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
	Criteria criteria = session.createCriteria(Questionbank.class).setProjection(Projections.property("ans_id")).addOrder(Order.asc("que_id"));
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", sub_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	criterion=Restrictions.in("que_id", que_ids);
	criteria.add(criterion);
	List questionAnsList = criteria.list();
	transaction.commit();
	session.close();
	return  questionAnsList;	
}

public List<Integer> getQuestionrelatedtoTopics(int sub_id,int inst_id,int div_id,int topic_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Criteria criteria = session.createCriteria(Questionbank.class).setProjection(Projections.distinct(Projections.property("que_id"))).addOrder(Order.asc("que_id"));;
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", sub_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("topic_id", topic_id);
	criteria.add(criterion);
	List<Integer> questionList = criteria.list();
	transaction.commit();
	session.close();
	return  questionList;	
}

public boolean deleteQuestionList(List<Integer> que_id,int inst_id,int sub_id,int div_id) {
	Transaction transaction=null;
	Session session=null;
	List<Exam> list = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		String queryString="delete from Questionbank where inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and que_id in :que_id";
		
		Query query = session.createQuery(queryString);
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("sub_id", sub_id);
		query.setParameterList("que_id", que_id);
		 query.executeUpdate();
		
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
	}finally{
		if(null!=session){
			session.close();
		}
	}
		
return true;

}

public boolean ExamQuestionStatus(List<Integer> que_id,int inst_id,int sub_id,int div_id) {
	Transaction transaction=null;
	Session session=null;
	List<Exam> list = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		String queryString="update Questionbank set ques_status ='N' where inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and que_id in :que_id";
		
		Query query = session.createQuery(queryString);
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("sub_id", sub_id);
		query.setParameterList("que_id", que_id);
		 query.executeUpdate();
		
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
	}finally{
		if(null!=session){
			session.close();
		}
	}
		
return true;

}

public List<Integer> getDisabledQuestions(List<Integer> que_id,int inst_id,int sub_id,int div_id) {
	Transaction transaction=null;
	Session session=null;
	List<Integer> list = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		String queryString="select que_id from Questionbank where ques_status ='N' and inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and que_id in :que_id";
		
		Query query = session.createQuery(queryString);
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("sub_id", sub_id);
		query.setParameterList("que_id", que_id);
		list= query.list();
		
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
	}finally{
		if(null!=session){
			session.close();
		}
	}
		
return list;

}

public List<Integer> getQuestionrelatedtoSubject(int sub_id,int inst_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Criteria criteria = session.createCriteria(Questionbank.class).setProjection(Projections.distinct(Projections.property("que_id"))).addOrder(Order.asc("que_id"));;
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", sub_id);
	criteria.add(criterion);
	List<Integer> questionList = criteria.list();
	transaction.commit();
	session.close();
	return  questionList;	
}

public List<Integer> getQuestionrelatedtoClass(int inst_id,int div_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Criteria criteria = session.createCriteria(Questionbank.class).setProjection(Projections.distinct(Projections.property("que_id"))).addOrder(Order.asc("que_id"));;
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	List<Integer> questionList = criteria.list();
	transaction.commit();
	session.close();
	return  questionList;	
}

public boolean deleteQuestionrelatedtoClass(int inst_id,int div_id) {
	Transaction transaction=null;
	Session session=null;
	List<Integer> list = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		String queryString="delete from Questionbank where  inst_id = :inst_id and div_id=:div_id ";
		
		Query query = session.createQuery(queryString);
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.executeUpdate();
		
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
	}finally{
		if(null!=session){
			session.close();
		}
	}
		
	return  true;	
}

public boolean deleteQuestionrelatedtosubject(int inst_id,int sub_id) {
	Transaction transaction=null;
	Session session=null;
	List<Integer> list = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		String queryString="delete from Questionbank where inst_id = :inst_id and sub_id=:sub_id";
		
		Query query = session.createQuery(queryString);
		query.setParameter("inst_id", inst_id);
		query.setParameter("sub_id", sub_id);
		query.executeUpdate();
		transaction.commit();
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
	}finally{
		if(null!=session){
			session.close();
		}
	}
	return true;	
}

}
