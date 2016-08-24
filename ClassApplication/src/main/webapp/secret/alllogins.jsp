<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <script>

			//Updating contains function
		$(document).ready(function(){
				
			jQuery.expr[':'].Contains = function(a, i, m) {
			  return jQuery(a).text().toUpperCase()
				  .indexOf(m[3].toUpperCase()) >= 0;
			};

			// OVERWRITES old selecor
			jQuery.expr[':'].contains = function(a, i, m) {
			  return jQuery(a).text().toUpperCase()
				  .indexOf(m[3].toUpperCase()) >= 0;
			};
			
    		$("#alllogin_searches input[type='text']").on("keyup",function(){
				$(".all_login_table .hide").removeClass("hide");	
				var textSearch = $(this).val();
				var id=$(this).attr("id");
				var selector = "."+id+":contains('"+textSearch+"')";
				console.log($(selector).length);
				$("."+id).not(selector).parents("tr").addClass("hide");
    		});
    	});
    </script>
	<c:set value="${requestScope.alllogins}" var="registerBeans"> </c:set>
	<table class="table table-striped all_login_table">
	<tr >
	<th>First Name</th><th>Last Name</th><th>User Name</th><th>Password</th><th>Role</th>
	</tr>
	<tr id="alllogin_searches">
	<th><input type="text" class="form-control" id="alllogin_firstname" class="form-control"></th>
	<th><input type="text" class="form-control" id="alllogin_lastname" class="form-control"></th>
	<th><input type="text" class="form-control" id="alllogin_loginname" class="form-control"></th>
	<th><input type="text" class="form-control" id="alllogin_password" class="form-control"></th>
	<th><input type="text" class="form-control" id="alllogin_phone" class="form-control"></th>
	<th><input type="text" class="form-control" id="alllogin_role" class="form-control"></th>
	</tr>
	<c:forEach items="${registerBeans}" var="registerBean">
		 
		<tr>
		<td class="alllogin_firstname"><c:out value="${registerBean.fname}"></c:out></td>
		<td class="alllogin_lastname"><c:out value="${registerBean.lname}"></c:out></td>
		<td class="alllogin_loginname"><c:out value="${registerBean.loginName}"></c:out></td>
		<td class="alllogin_password"><c:out value="${registerBean.loginPass}"></c:out></td>
		<td class="alllogin_phone"><c:out value="${registerBean.phone1}"></c:out></td>
		<td class="alllogin_role">
		<c:choose>
			<c:when test="${registerBean.role eq 0}">
				<c:out value="Admin"></c:out>
			</c:when>
			<c:when test="${registerBean.role eq 1}">
				<c:out value="Class Owner"></c:out>
			</c:when>
			<c:when test="${registerBean.role eq 2}">
				<c:out value="Teacher"></c:out>
			</c:when>
			<c:when test="${registerBean.role eq 3}">
				<c:out value="Student"></c:out>
			</c:when>
		</c:choose></td>
		</tr>
	</c:forEach>
	</table>
