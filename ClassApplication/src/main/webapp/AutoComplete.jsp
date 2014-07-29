<%@page import="com.classapp.db.subject.GetSubject"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>

<%
    GetSubject  getSubject=new GetSubject();
 
    String query = request.getParameter("c");
     
    List<String> countries = getSubject.getAllSubjects("C");
 
    Iterator<String> iterator = countries.iterator();
    while(iterator.hasNext()) {
        String country = (String)iterator.next();
        out.println(country);
    }
%>