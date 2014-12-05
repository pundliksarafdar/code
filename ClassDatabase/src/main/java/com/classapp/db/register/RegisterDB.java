package com.classapp.db.register;

import java.lang.reflect.Method;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;

public class RegisterDB {
	public List getTeacherName(List TeacherIDs) {
		
		Session session = null;
		Transaction transaction = null;
		List subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  regId in :teacherids");
			query.setParameterList("teacherids", TeacherIDs);
			subidList = query.list();
			
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return subidList;
	}
	
public List getTeachersClassName(List classIDs) {
		
		Session session = null;
		Transaction transaction = null;
		List subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  regId in :teacherids");
			query.setParameterList("teacherids", classIDs);
			subidList = query.list();
			
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return subidList;
	}
	
	public RegisterBean getscheduleTeacher(int TeacherId) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  regId in :teacherids");
			query.setParameter("teacherids", TeacherId);
			subidList = query.list();
			if(subidList!=null)
			{
				subidList.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return subidList.get(0);
	}
	
	public RegisterBean getRegisterclass(int regId) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  regId in :teacherids");
			query.setParameter("teacherids", regId);
			subidList = query.list();
			if(subidList!=null)
			{
				subidList.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return subidList.get(0);
	}
	
	/*Pundlik*/
	public boolean isMobileExists(String mobileNo){
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> list = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  phone1 in :mobileno");
			query.setParameter("mobileno", mobileNo);
			list = query.list();
			if(list!=null)
			{
				if(list.size()>0){
					return true;
				}else{
					return false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
		}
		return true;
	}

	/*Pundlik*/
	public boolean isUserExits(String loginName) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> list = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  loginName in :loginName");
			query.setParameter("loginName", loginName);
			list = query.list();
			if(list!=null)
			{
				if(list.size()>0){
					return true;
				}else{
					return false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
		}
		return true;
	}

	/*Pundlik*/
	public boolean updateUser(RegisterBean registerBean,Integer regId) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> list = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			RegisterBean registerBean2 = (RegisterBean) session.get(RegisterBean.class, regId);
			//RegisterBean regBeanMerged = (RegisterBean) session.merge(registerBean2);
			//registerBean2.setRegId(regId);
			merge(registerBean2, registerBean);
			registerBean2.setRegId(regId);
			session.update(registerBean2);
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			return false;
		}
		return true;
	}
	
	public void merge(Object obj, Object update){
	    if(!obj.getClass().isAssignableFrom(update.getClass())){
	        return;
	    }

	    Method[] methods = obj.getClass().getMethods();

	    for(Method fromMethod: methods){
	        if(fromMethod.getDeclaringClass().equals(obj.getClass())
	                && fromMethod.getName().startsWith("get")){

	            String fromName = fromMethod.getName();
	            String toName = fromName.replace("get", "set");
	            
	            if(!"getRole".equals(fromName)){
	            try {
	                Method toMetod = obj.getClass().getMethod(toName, fromMethod.getReturnType());
	                Object value = fromMethod.invoke(update, (Object[])null);
	                if(value != null){
	                    toMetod.invoke(obj, value);
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            } 
	            }
	        }
	    }
	}
	
public List getStudentInfo(List StudentIDs,int pagenumber, int resultPerPage) {
		
		Session session = null;
		Transaction transaction = null;
		List subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			//Query query = session.createQuery("SELECT  reg.fname,reg.lname,reg.loginName FROM SELECT  :rownum := :rownum + 1 RowNumber, t.fname,t.lname,t.loginName FROM RegisterBean t, (SELECT :rownum := 0) s where regId=:studentids  ORDER BY t.regId reg where reg.RowNumber>:lowerlimit and reg.RowNumber<:upperlimit");
			
			Query query = session.createQuery("from RegisterBean where  regId in :studentids order by regId");
			query.setParameterList("studentids", StudentIDs);
			query.setFirstResult((pagenumber -1)*resultPerPage);
			query.setMaxResults(resultPerPage);			
			subidList = query.list();
			
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return subidList;
	}
}
