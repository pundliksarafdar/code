package com.classapp.db;

import org.hibernate.Session;

import com.classapp.db.register.RegisterBean;
import com.classapp.db.register.RegisterUser;
import com.classapp.persistence.HibernateUtil;
import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Session session =null;
    	try{
    	session = HibernateUtil.getSessionfactory().openSession();
    	session.beginTransaction();
    	
    	RegisterBean registerBean = new RegisterBean();
    	
    	registerBean.setAddr1("addr11");
    	registerBean.setAddr2("addr22");
    	registerBean.setCity("city1");
    	registerBean.setCountry("India");
    	registerBean.setState("state1");
    	registerBean.setPhone1("123456788");
    	registerBean.setPhone2("phone2");
    	
    	registerBean.setFname("Pundlik");
    	registerBean.setMname("Bhagatram");
    	registerBean.setLname("Sarafdar");
    	registerBean.setLoginName("nologin");
    	registerBean.setLoginPass("passwd");
    	registerBean.setLoginPassRe("passwd");
    	registerBean.setDob("19892211");
    	registerBean.setClassName("Class Name");
    	
    	RegisterUser registerUser = new RegisterUser();
    	Gson gson = new Gson();
    	String status = registerUser.registerUser(gson.toJson(registerBean));
    	System.out.println(status);
	    }catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(null!=session){
				session.close();
			}
		}
    	/*Query query = session.createQuery("from Stock where stockCode = :code ");
    	query.setParameter("code", "7277");
    	List list = query.list();
    	*/
		}
}
