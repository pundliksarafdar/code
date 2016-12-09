package com.classapp.db.roll;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.Teacher.Teacher;
import com.classapp.db.register.RegisterBean;
import com.classapp.persistence.HibernateUtil;

public class InstRollDB {
	public boolean saveInstRoll(Inst_roll inst_roll) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(inst_roll);
		transaction.commit();
		if(session!=null){
			session.close();
		}
		return  true;

	}
	
	public boolean validateRoll(Inst_roll inst_roll) {
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		Query query = session.createQuery("from Inst_roll where  inst_id = :inst_id and roll_desc=:roll_desc");
		query.setParameter("inst_id", inst_roll.getInst_id());
		query.setParameter("roll_desc", inst_roll.getRoll_desc());
		List list = query.list();
		if(list.size() > 0){
			return false;
		}
		}catch(Exception e){
		e.printStackTrace();
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  true;

	}
	
	public List<Inst_roll> getInstituteRoles(int inst_id) {
		Session session=null;
		List list = null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		Query query = session.createQuery("from Inst_roll where  inst_id = :inst_id");
		query.setParameter("inst_id", inst_id);
		 list = query.list();
		}catch(Exception e){
		e.printStackTrace();
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  list;

	}
	
	public Inst_roll getRole(int inst_id,int roll_id) {
		Session session=null;
		List<Inst_roll> list = null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		Query query = session.createQuery("from Inst_roll where  inst_id = :inst_id and roll_id = :roll_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("roll_id", roll_id);
		 list = query.list();
		 if(list.size() > 0){
			 return list.get(0);
		 }
		}catch(Exception e){
		e.printStackTrace();
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  null;

	}
	
	public boolean updateRoll(Inst_roll inst_roll) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("update Inst_roll set  roll_desc=:roll_desc,parent_mod_access =:parent_mod_access,"
				+ "child_mod_access = :child_mod_access, teacher = :teacher  where  inst_id = :inst_id and roll_id = :roll_id");
		query.setParameter("inst_id", inst_roll.getInst_id());
		query.setParameter("roll_desc", inst_roll.getRoll_desc());
		query.setParameter("parent_mod_access", inst_roll.getParent_mod_access());
		query.setParameter("child_mod_access", inst_roll.getChild_mod_access());
		query.setParameter("teacher", inst_roll.isTeacher());
		query.setParameter("roll_id", inst_roll.getRoll_id());
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
	
	public boolean validateUpdateRoll(Inst_roll inst_roll) {
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		Query query = session.createQuery("from Inst_roll where  inst_id = :inst_id and roll_desc=:roll_desc and roll_id != :roll_id");
		query.setParameter("inst_id", inst_roll.getInst_id());
		query.setParameter("roll_desc", inst_roll.getRoll_desc());
		query.setParameter("roll_id", inst_roll.getRoll_id());
		List list = query.list();
		if(list.size() > 0){
			return false;
		}
		}catch(Exception e){
		e.printStackTrace();
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  true;

	}
	
	public boolean saveInstUser(Inst_user inst_user) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(inst_user);
		transaction.commit();
		if(session!=null){
			session.close();
		}
		return  true;

	}
	
	public List getInstUsers(int inst_id) {
		Session session=null;
		List list = null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		Query query = session.createQuery("select reg.regId,reg.fname,reg.lname,role.roll_desc from RegisterBean reg,"
				+ "Inst_roll role where  reg.inst_id = :inst_id and reg.inst_id = role.inst_id  and "
				+ "reg.inst_roll = role.roll_id");
		query.setParameter("inst_id", inst_id);
		list = query.list();
		}catch(Exception e){
		e.printStackTrace();
		
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  list;

	}
	
	public RegisterBean getInstUserRegisterBean(int inst_id,int user_id) {
		Session session=null;
		List<RegisterBean> list = null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		Query query = session.createQuery("from RegisterBean  where  inst_id = :inst_id and regId = :regId");
		query.setParameter("inst_id", inst_id);
		query.setParameter("regId", user_id);
		list = query.list();
		if(list.size() > 0){
			return list.get(0);
		}
		}catch(Exception e){
		e.printStackTrace();
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  null;

	}
	
	public Inst_user getInstUser(int inst_id,int user_id) {
		Session session=null;
		List<Inst_user> list = null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		Query query = session.createQuery("from Inst_user  where  inst_id = :inst_id and user_id = :user_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("user_id", user_id);
		list = query.list();
		if(list.size() > 0){
			return list.get(0);
		}
		}catch(Exception e){
		e.printStackTrace();
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  null;

	}
	
	public boolean updateInstUserRegisterBean(RegisterBean registerBean) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("update RegisterBean set  fname = :fname,lname = :lname,addr1 =:addr1,city = :city ,"
				+ " state =:state,phone1=:phone1,inst_roll = :inst_roll,email = :email,dob = :dob"
				+ " where  regId = :regId ");
		//query.setParameter("inst_id", registerBean.getInst_id());
		query.setParameter("fname", registerBean.getFname());
		query.setParameter("lname", registerBean.getLname());
		query.setParameter("addr1", registerBean.getAddr1());
		query.setParameter("phone1", registerBean.getPhone1());
		query.setParameter("city", registerBean.getCity());
		query.setParameter("state", registerBean.getState());
		query.setParameter("inst_roll", registerBean.getInst_roll());
		query.setParameter("regId", registerBean.getRegId());
		query.setParameter("email", registerBean.getEmail());
		query.setParameter("dob", registerBean.getDob());
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
	
	public Inst_user updateInstUser(Inst_user user) {
		Transaction transaction=null;
		Session session=null;
		List<Inst_user> list = null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("update Inst_user set role_id= :role_id,education=:education,joining_date=:joining_date"
				+ "  where  inst_id = :inst_id and user_id = :user_id");
		query.setParameter("inst_id", user.getInst_id());
		query.setParameter("user_id", user.getUser_id());
		query.setParameter("role_id", user.getRole_id());
		query.setParameter("education", user.getEducation());
		query.setParameter("joining_date", user.getJoining_date());
		list = query.list();
		transaction.commit();
		if(list.size() > 0){
			return list.get(0);
		}
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
		return  null;

	}
	
	public boolean updateInstUser(int inst_id,int user_id,boolean teacher) {
		Transaction transaction=null;
		Session session=null;
		List<Inst_user> list = null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("update Inst_user set teacher= :teacher"
				+ "  where  inst_id = :inst_id and user_id = :user_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("user_id", user_id);
		query.setParameter("teacher", teacher);
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
	
	public boolean isCustomUserTeacher(int inst_id,int user_id) {
		Transaction transaction=null;
		Session session=null;
		List<Teacher> list = null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("from Teacher"
				+ "  where  class_id = :inst_id and user_id = :user_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("user_id", user_id);
		 list = query.list();
		 if(list != null){
			 if(list.size() > 0){
				 return true;
			 }
		 }
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
		return  false;

	}
	
	public boolean deleteInstUser(int inst_id,int user_id) {
		Transaction transaction=null;
		Session session=null;
		List<Inst_user> list = null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("delete from Inst_user "
				+ "  where  inst_id = :inst_id and user_id = :user_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("user_id", user_id);
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
}
