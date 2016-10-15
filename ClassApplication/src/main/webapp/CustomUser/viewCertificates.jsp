<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
            <style type="text/css">
            .note-editing-area{
            padding: 2%
            } 
            </style>
</head>
<body>
<jsp:include page="certificateHeaders.jsp" >
		<jsp:param value="active" name="customeUserViewCertificate"/>
	</jsp:include>
	<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
	<div class="container certificateList" style="padding: 1%">
		<table class="table table-striped" id="certificateTable">
			<thead>
				<tr>
					<th>#</th>
					<th>Description</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${certificateList}" var="certificate" varStatus="counter">
					<tr>
						<td><c:out value="${counter.count}"></c:out></td>
						<td><div class="cert_desc"><c:out value="${certificate.cert_desc}"></c:out></div></td>
						<td>
							<input type="hidden" class="headerID" value="<c:out value="${certificate.header_id}"></c:out>">
							<% if(ArrayUtils.contains(child_mod_access,"49")){ %>
							<button class="btn btn-primary btn-xs edit" id="<c:out value="${certificate.cert_id}"></c:out>">Edit</button>
							<%} %>
							<button class="btn btn-info btn-xs view" id="<c:out value="${certificate.cert_id}"></c:out>">View</button>
							<% if(ArrayUtils.contains(child_mod_access,"50")){ %>
							<button class="btn btn-danger btn-xs delete" id="<c:out value="${certificate.cert_id}"></c:out>">Delete</button>
							<%} %>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<input type="hidden" class="form-control" id="accessRights" value='<%=String.join(",",child_mod_access)%>'>
	<div class="container viewCertificate" style="display: none;padding: 1%">
	<div class="row">
		<div class="col-md-2">
			<button class="btn btn-primary btn-sm backToList">Back To List</button>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12 viewCertificateData">
		
		</div>
	</div>
	</div>
	<div class="container editCertificate" style="padding: 1%;display: none;">
	<div class="row">
		<div class="col-md-2">
			<button class="btn btn-primary btn-sm backToList">Back To List</button>
		</div>
	</div>
	<div class="row">
			<div class="col-md-4">
				<select id="headerSelect" class="form-control" style="width: 100%">		
				</select>
				<span id="headerSelectError" class="error"></span>
			</div>
		</div>
	<div class="row">
		<div class="col-md-12 editCertificateData">
			<div class="summernote">
			</div>
		</div>
	</div>	
		 <div class="row">
	 <form id="header-form">
	 <div class="col-md-12">
            <div>
            	<input placeholder="Certificate Description" id="certificateName" name="certificateName" type="text" class="form-control" required style="width: 50%;" maxlength="50"></input>
            </div>
             <span id="certificateNameError" class="error"></span>
      </div>
      <div class="col-md-12">
	        <input type="button" value="Update" class="btn btn-success" id="save"/>
	   </div>
	    </form> 
	    </div>   
	
	</div>
</body>
</html>