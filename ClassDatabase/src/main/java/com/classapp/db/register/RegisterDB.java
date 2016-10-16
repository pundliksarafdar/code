package com.classapp.db.register;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
			
		}finally{
			if(null!=session){
				session.close();
			}
		}
			
		return subidList;
	}
	
public List getTeachersInstitutes(List inst_IDs) {
		
		Session session = null;
		Transaction transaction = null;
		List list = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select regId,className from RegisterBean where  regId in :inst_ids");
			query.setParameterList("inst_ids", inst_IDs);
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
			
		}finally{
			if(null!=session){
				session.close();
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
			
		}finally{
			if(null!=session){
				session.close();
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
		}finally{
			if(null!=session){
				session.close();
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
		}finally{
			if(null!=session){
				session.close();
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
		}finally{
			if(null!=session){
				session.close();
			}
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
	
public List getStudentInfo(List StudentIDs) {
		
		Session session = null;
		Transaction transaction = null;
		List subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			//Query query = session.createQuery("SELECT  reg.fname,reg.lname,reg.loginName FROM SELECT  :rownum := :rownum + 1 RowNumber, t.fname,t.lname,t.loginName FROM RegisterBean t, (SELECT :rownum := 0) s where regId=:studentids  ORDER BY t.regId reg where reg.RowNumber>:lowerlimit and reg.RowNumber<:upperlimit");
			
			Query query = session.createQuery("from RegisterBean where  regId in :studentids order by regId asc");
			query.setParameterList("studentids", StudentIDs);
			//query.setFirstResult((pagenumber -1)*resultPerPage);
			//query.setMaxResults(resultPerPage);			
			subidList = query.list();
			
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
			
		return subidList;
	}

public List getStudents(List StudentIDs) {
	
	Session session = null;
	Transaction transaction = null;
	List students = null;
	
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		//Query query = session.createQuery("SELECT  reg.fname,reg.lname,reg.loginName FROM SELECT  :rownum := :rownum + 1 RowNumber, t.fname,t.lname,t.loginName FROM RegisterBean t, (SELECT :rownum := 0) s where regId=:studentids  ORDER BY t.regId reg where reg.RowNumber>:lowerlimit and reg.RowNumber<:upperlimit");
		
		Query query = session.createQuery("from RegisterBean where  regId in :studentids order by regId");
		query.setParameterList("studentids", StudentIDs);
		students = query.list();
		
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
		
	return students;
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
			
		}finally{
			if(null!=session){
				session.close();
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
			
		}finally{
			if(null!=session){
				session.close();
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
			
		}finally{
			if(null!=session){
				session.close();
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
			
		}finally{
			if(null!=session){
				session.close();
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
			
		}finally{
			if(null!=session){
				session.close();
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
		}finally{
			if(null!=session){
				session.close();
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
		}finally{
			if(null!=session){
				session.close();
			}
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
			
		}finally{
			if(null!=session){
				session.close();
			}
		}	
		return null;
		
	}
	
	public RegisterBean getInstitute(String loginName) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  loginName = :loginName and role=1");
			query.setParameter("loginName", loginName);
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
			
		}finally{
			if(null!=session){
				session.close();
			}
		}	
		return null;
		
	}
	
	public RegisterBean getInstitute(int inst_id) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  regId = :inst_id and role=1");
			query.setParameter("inst_id", inst_id);
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
			
		}finally{
			if(null!=session){
				session.close();
			}
		}	
		return null;
		
	}
	
	public boolean updateInstituteStatus(int regId,String inst_status) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update RegisterBean set inst_status=:inst_status where  regId = :regId and role=1");
			query.setParameter("regId", regId);
			query.setParameter("inst_status", inst_status);
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
	
	public boolean updateScheduler() {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update RegisterBean reg set reg.inst_status='disabled' where reg.nextRenewalDate < CURRENT_DATE and " +
					"reg.regId in (SELECT inst_id FROM InstituteStats where alloc_ids > 25 and alloc_memory > 100)");
			int rows=query.executeUpdate();
			query = session.createQuery("update InstituteStats set alloc_ids=25 , alloc_memory=100, avail_memory=0,avail_ids=0 where " +
					"inst_id in (SELECT regId FROM RegisterBean where inst_status='disabled')");
			rows=query.executeUpdate();
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
	
	public boolean updateRenewalDates(int inst_id) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> subidList = null;
		Calendar date = Calendar.getInstance();
	    date.setTime(new Date());
	    date.add(Calendar.YEAR,1);
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update RegisterBean set nextRenewalDate=:nextdate,renewedDates=CURRENT_DATE where  regId = :regId" +
					" and (nextRenewalDate<CURRENT_DATE or nextRenewalDate=null)");
			query.setParameter("regId", inst_id);
			query.setParameter("nextdate", date.getTime());
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
	
	public RegisterBean getRegisteredTeacher(String username,String email,int inst_id) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  loginName = :loginName and email = :email and (role=2 or ( inst_id = :inst_id and role = 5))");
			query.setParameter("loginName", username);
			query.setParameter("email", email);
			query.setParameter("inst_id", inst_id);
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
			
		}finally{
			if(null!=session){
				session.close();
			}
		}	
		return null;
	}
	
	public RegisterBean getRegisteredUserByLoginID(String username) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  loginName = :loginName and role=3");
			query.setParameter("loginName", username);
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
			
		}finally{
			if(null!=session){
				session.close();
			}
		}	
		return null;
	}
	
	public List<String> getNamesForSuggestion(int inst_id) {
		Session session = null;
		Transaction transaction = null;
		List<String> namesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select CONCAT(fname,' ',lname) from RegisterBean where  regId in (select student_id from Student where class_id=:inst_id)");
			query.setParameter("inst_id", inst_id);
			namesList = query.list();
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
		return namesList;
	}
	
	public List<RegisterBean> getStudentByNames(int inst_id,String fname,String lname) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> namesList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  regId in (select student_id from Student where class_id=:inst_id) and fname like :fname and lname like :lname order by regId asc");
			query.setParameter("inst_id", inst_id);
			query.setParameter("fname", fname+"%");
			query.setParameter("lname", lname+"%");
			namesList = query.list();
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
		return namesList;
	}
	
	public List getManuallyRegisteredStudentData(int inst_id,List<Integer> studentIDs) {
		Session session = null;
		Transaction transaction = null;
		List studentDataList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select reg.regId,reg.loginName,reg.loginPass,reg.fname,reg.lname, reg.email,std.parentFname,std.parentLname,std.parentEmail from RegisterBean reg,Student std where  reg.regId in :list and std.class_id = :inst_id and "
											+ "std.student_id = reg.regId");
			query.setParameter("inst_id", inst_id);
			query.setParameterList("list", studentIDs);
			studentDataList = query.list();
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
		return studentDataList;
	}
	
	public boolean updateActivationCode(int regID,String activationCode) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> subidList = null;
		Calendar date = Calendar.getInstance();
	    date.setTime(new Date());
	    date.add(Calendar.YEAR,1);
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update RegisterBean set activationcode=:activationCode where  regId = :regId");
			query.setParameter("regId", regID);
			query.setParameter("activationCode", activationCode);
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
	
	public boolean updateEmail(int regID,String email) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> subidList = null;
		Calendar date = Calendar.getInstance();
	    date.setTime(new Date());
	    date.add(Calendar.YEAR,1);
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update RegisterBean set email=:email,status = '' where  regId = :regId");
			query.setParameter("regId", regID);
			query.setParameter("email", email);
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
