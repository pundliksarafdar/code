package com.classapp.db;

/**
 * Hello world!
 *
 */
public class App {
   /* public static void main( String[] args )
    {
    	Session session = HibernateUtil.getSessionfactory().openSession();
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
    	
    	/*Query query = session.createQuery("from Stock where stockCode = :code ");
    	query.setParameter("code", "7277");
    	List list = query.list();
    	*/
/*		}*/
}
