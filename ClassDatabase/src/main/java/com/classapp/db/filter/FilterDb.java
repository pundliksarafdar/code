package com.classapp.db.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.subject.Subject;
import com.classapp.persistence.HibernateUtil;

public class FilterDb {
	public <E> List<E> getFilteredResult(HashMap<String, List<Integer>> filterParamMap,int instId,E e,List<String>paramList,String instLabel){
		Session session = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Criteria criteria = session.createCriteria(e.getClass());
			Criterion criterion = Restrictions.eq(instLabel, instId);
			criteria.add(criterion);
			if(null!=filterParamMap){
				for (Entry<String, List<Integer>> entry : filterParamMap.entrySet())
				{
					if(paramList.contains(entry.getKey()) && !entry.getValue().isEmpty()){
					    criterion = Restrictions.in(entry.getKey(), entry.getValue());
						criteria.add(criterion);
					}
				} 
			}
			
			return criteria.list();
		}catch(Exception exp){
			exp.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return new ArrayList<E>();
		
	}
	
	public void getFilteredResult(int instId,List<Integer> classId,List<Integer> subjectId,List<Integer> batchId,List<Integer> teacherId){
		
	}
	
	public static void main(String[] args) {
		FilterDb filterDb = new FilterDb();
		List<Integer> subIds =  new ArrayList<>();
		subIds.add(1);
		subIds.add(3);
		HashMap<String, List<Integer>> filterParamMap = new HashMap<String, List<Integer>>();
		filterParamMap.put("subjectId", subIds);
		List<Subject> subjects = filterDb.getFilteredResult(filterParamMap, 190,new Subject(),null,"institute_id");
		System.out.println(subjects);
		
	}
}
