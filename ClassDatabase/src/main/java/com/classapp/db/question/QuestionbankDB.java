package com.classapp.db.question;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.apache.commons.beanutils.BeanUtils;
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
	questionbank.setModified_dt(new Date(new java.util.Date().getTime()));
	Question_Bank question_Bank = new Question_Bank();
	try {
		BeanUtils.copyProperties(question_Bank, questionbank);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	session.saveOrUpdate(question_Bank);
	transaction.commit();
	System.out.println("Quesbank ID:-"+questionbank.getQue_id());
	if(session!=null){
		session.close();
	}
	return  question_Bank.getQue_id();


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

public boolean updateSubjectiveQuestion(int que_id,int inst_id,int sub_id,int div_id,String que_text,int marks,int topic_id) {
	Transaction transaction=null;
	Session session=null;
	try{
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Query query = session.createQuery("update Questionbank set que_text = :que_text , marks = :marks, topic_id = :topic_id where  inst_id = :inst_id "
			+ "and div_id=:div_id and sub_id=:sub_id and que_id = :que_id");
	query.setParameter("inst_id", inst_id);
	query.setParameter("div_id", div_id);
	query.setParameter("sub_id", sub_id);
	query.setParameter("que_id", que_id);
	query.setParameter("que_text", que_text);
	query.setParameter("marks", marks);
	query.setParameter("topic_id", topic_id);
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

public boolean updateObjectiveQuestion(Questionbank questionbank) {
	Transaction transaction=null;
	Session session=null;
	try{
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Query query = session.createQuery("update Questionbank set que_text = :que_text , marks = :marks,topic_id = :topic_id, opt_1 = :opt_1, opt_2 = :opt_2 " +
			", opt_3 = :opt_3, opt_4 = :opt_4, opt_5 = :opt_5, opt_6 = :opt_6, opt_7 = :opt_7, opt_8 = :opt_8, opt_9 = :opt_9, opt_10 = :opt_10 " +
			",ans_id=:ans_id where  inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and que_id = :que_id");
	query.setParameter("inst_id", questionbank.getInst_id());
	query.setParameter("div_id", questionbank.getDiv_id());
	query.setParameter("sub_id", questionbank.getSub_id());
	query.setParameter("que_id", questionbank.getQue_id());
	query.setParameter("que_text", questionbank.getQue_text());
	query.setParameter("marks", questionbank.getMarks());
	query.setParameter("opt_1", questionbank.getOpt_1());
	query.setParameter("opt_2", questionbank.getOpt_2());
	query.setParameter("opt_3", questionbank.getOpt_3());
	query.setParameter("opt_4", questionbank.getOpt_4());
	query.setParameter("opt_5", questionbank.getOpt_5());
	query.setParameter("opt_6", questionbank.getOpt_6());
	query.setParameter("opt_7", questionbank.getOpt_7());
	query.setParameter("opt_8", questionbank.getOpt_8());
	query.setParameter("opt_9", questionbank.getOpt_9());
	query.setParameter("opt_10", questionbank.getOpt_10());
	query.setParameter("ans_id", questionbank.getAns_id());
	query.setParameter("topic_id", questionbank.getTopic_id());
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

public boolean updateParagraphQuestion(int que_id,int inst_id,int sub_id,int div_id,int marks,int topic_id) {
	Transaction transaction=null;
	Session session=null;
	try{
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Query query = session.createQuery("update Questionbank set marks = :marks,topic_id = :topic_id where  inst_id = :inst_id and div_id=:div_id "
			+ "and sub_id=:sub_id and que_id = :que_id");
	query.setParameter("inst_id", inst_id);
	query.setParameter("div_id", div_id);
	query.setParameter("sub_id", sub_id);
	query.setParameter("que_id", que_id);
	query.setParameter("marks", marks);
	query.setParameter("topic_id", topic_id);
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
	List<Integer> idList = new ArrayList<Integer>();
	for(int j=0;j<i;j++){
		if(list.get(i).getSubject_id() == list.get(j).getSubject_id() && list.get(i).getMarks() == list.get(j).getMarks()){
			idList = list.get(j).getQuestion_ids();
		}
	}
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
		if(!"-1".equals(list.get(i).getQuestion_type())){
			criteria.add(Restrictions.eq("que_type", list.get(i).getQuestion_type()));
		}
		if(list.get(i).getTopic_id()!=-1){
			criteria.add(Restrictions.eq("topic_id", list.get(i).getTopic_id()));
		}
		if(idList.size() > 0){
			criteria.add(Restrictions.not(
				    Restrictions.in("que_id", idList)
					  ));
			}
		List<Integer> questionList = criteria.list();
	list.get(i).setQuestion_ids(generateRandomQuestionId(questionList, list.get(i).getCount()));
	}
	transaction.commit();
	session.close();
return list;
}

public List<Integer> getQuestionsForGenerateExam(int inst_id,int div_id,GenerateQuestionPaperServicebean questionPaperServicebean) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Criteria criteria = session.createCriteria(Questionbank.class).setProjection(Projections.property("que_id")).addOrder(Order.asc("created_dt"));
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", questionPaperServicebean.getSubject_id());
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	
		if(questionPaperServicebean.getMarks()!=-1){
			criteria.add(Restrictions.eq("marks", questionPaperServicebean.getMarks()));
		}
		if(!"-1".equals(questionPaperServicebean.getQuestion_type())){
			criteria.add(Restrictions.eq("que_type", questionPaperServicebean.getQuestion_type()));
		}
		if(questionPaperServicebean.getTopic_id()!=-1){
			criteria.add(Restrictions.eq("topic_id", questionPaperServicebean.getTopic_id()));
		}
		if(questionPaperServicebean.getQuestion_ids().size() > 0){
		criteria.add(Restrictions.not(
			    Restrictions.in("que_id", questionPaperServicebean.getQuestion_ids())
				  ));
		}
		List<Integer> questionList = criteria.list();
	transaction.commit();
	session.close();
return questionList;
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

public boolean updateQuestionrelatedtotopic(int inst_id,int sub_id,int div_id,int topic_id) {
	Transaction transaction=null;
	Session session=null;
	List<Integer> list = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		String queryString="update Questionbank set topic_id = 0 "
				+ "where inst_id = :inst_id and sub_id=:sub_id and div_id=:div_id and topic_id=:topic_id";
		
		Query query = session.createQuery(queryString);
		query.setParameter("inst_id", inst_id);
		query.setParameter("sub_id", sub_id);
		query.setParameter("div_id", div_id);
		query.setParameter("topic_id", topic_id);
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

public List<Questionbank> getQuestionBankList(int inst_id,int div_id,int sub_id,int topic_id,String ques_type,int marks){

	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	Criteria criteria = session.createCriteria(Questionbank.class).addOrder(Order.asc("created_dt"));
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("sub_id", sub_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("div_id", div_id);
	criteria.add(criterion);
	
		if(marks!=-1){
			criteria.add(Restrictions.eq("marks", marks));
		}
		if(!"-1".equals(ques_type)){
			criteria.add(Restrictions.eq("que_type", ques_type));
		}
		if(topic_id!=-1){
			criteria.add(Restrictions.eq("topic_id", topic_id));
		}
		
		List<Questionbank> questionList = criteria.list();
	
	transaction.commit();
	session.close();
return questionList;

}

public List<Questionbank> getQuestionBankList(List<List<Integer>> list,int inst_id){
	Transaction transaction=null;
	Session session=null;
	List<Questionbank> questionlist = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		String queryString="from Questionbank where inst_id = :inst_id and (div_id,sub_id,que_id) in :list";
		Integer [] temp = {14,7,4};
		queryString = queryString.replace(":list", list.toString());
		Query query = session.createQuery(queryString);
		query.setParameter("inst_id", inst_id);
		questionlist = query.list();
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
	return questionlist;
}

private List<Integer> generateRandomQuestionId(List<Integer> allQuestionId,int count){
	
	List<Integer> questionId = new ArrayList<Integer>();
	Set<Integer> questionIdSet = new HashSet<Integer>();
	Random random = new Random();
	while(questionIdSet.size()<count && allQuestionId.size()>questionIdSet.size()){
		int index = random.nextInt(allQuestionId.size());
		questionIdSet.add(allQuestionId.get(index));
	}
	questionId.addAll(questionIdSet);
	return questionId;
}

}
