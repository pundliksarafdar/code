package com.classapp.db.register;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;
import com.classapp.servicetable.ServiceMap;

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
			if(null!=classIDs && classIDs.size()!=0){
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  regId in :teacherids");
			query.setParameterList("teacherids", classIDs);
			subidList = query.list();
			}else{
				subidList = new ArrayList();
			}
			
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

	public boolean isEmailAndMobileValid(String email,String phone) {
		Session session = null;
		Transaction transaction = null;
		List subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  email = :email and phone1=:phone");
			query.setParameter("email", email);
			query.setParameter("phone", phone);
			subidList = query.list();
			if(subidList.size()>0){
				return true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return false;
	}
	
	public boolean ActivationCodeValidation(int regID,String code) {
		Session session = null;
		Transaction transaction = null;
		List subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  regId = :regId and activationcode=:activationcode");
			query.setParameter("regId", regID);
			query.setParameter("activationcode", code);
			subidList = query.list();
			if(subidList.size()>0){
				return true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return false;
	}
	
	public void removeActivationCode(int regID) {
		Session session = null;
		Transaction transaction = null;
		List subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update RegisterBean set activationcode='' where  regId = :regId");
			query.setParameter("regId", regID);
			query.executeUpdate();
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
	}
	
	public String getPassword(String email,String phone) {
		Session session = null;
		Transaction transaction = null;
		List subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select loginPass from RegisterBean where  email = :email and phone1=:phone");
			query.setParameter("email", email);
			query.setParameter("phone", phone);
			subidList = query.list();
			if(subidList.size()>0){
				query = session.createQuery("update RegisterBean set status='F' where  email = :email and phone1=:phone");
				query.setParameter("email", email);
				query.setParameter("phone", phone);
				query.executeUpdate();
				transaction.commit();
				return subidList.get(0).toString();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return "";
	}
	
	public void resetpassword(int regID,String password) {
		Session session = null;
		Transaction transaction = null;
		List subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update RegisterBean set loginPass=:pass where  regId = :regId");
			query.setParameter("regId", regID);
			query.setParameter("pass", password);
			query.executeUpdate();
			query = session.createQuery("update RegisterBean set status='' where  regId = :regId");
			query.setParameter("regId", regID);
			query.executeUpdate();
			transaction.commit();
			
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
	}
	
	public boolean isEmailExists(String email) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> list = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  email in :email");
			query.setParameter("email", email);
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
	
	public List<RegisterBean> getAllRegisters(){
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> list = null;
		
		String show = ServiceMap.getSystemParam("7", "show");
		if(!"yes".equals("yes")){
			list = new ArrayList<RegisterBean>();
		}else{
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean");
			list = query.list();
		}catch(Exception e){
			list = new ArrayList<RegisterBean>();
		}
		}
		return list;
	}
	
	public RegisterBean getRegistereduser(int regID) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  regId = :regID");
			query.setParameter("regID", regID);
			subidList = query.list();
			if(subidList!=null)
			{
				return subidList.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return null;
		
	}
}
