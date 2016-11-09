<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
var loadFlag = false;
var child_mod_access  = [];
$(document).ready(function(){
	var dataTable = $("#studentNoticeTable").DataTable();
	  dataTable.on( 'order.dt search.dt', function () {
	        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
				});
			}).draw();  
	 child_mod_access  = $("#accessRights").val().split(",");
	 $("#noticeType").change(function(){
		 if($("#noticeType").val()== 1){
			 $("#studentNoticeDiv").show();
			 $("#staffNoticeDiv").hide();
		 }else{
		 $("#studentNoticeDiv").hide();
		 $("#staffNoticeDiv").show();
		if(!loadFlag){	 
		 var commonHandler = {};
			commonHandler.success = function(data){
				dataTable = $('#staffNoticeTable').DataTable({
					bDestroy:true,
					data: data,
					columns: [
						{title:"Sr No.",data:null,render:function(data,event,row){
							return "";
						},sWidth:"10%"},
						{ title: "Notice",data:data,render:function(data,event,row){
							return row.notice;
						},sWidth:"40%"} ,
						{ title: "Role",data:data,render:function(data,event,row){
							return row.role_Desc;
						},sWidth:"20%"} ,
						{ title: "Start Date",data:data,render:function(data,event,row){
							return row.start_date;
						},sWidth:"10%"},
						{ title: "End Date",data:null,render:function(data,event,row){
							return row.end_date;
							},sWidth:"10%"},
						{ title: "Action",data:null,render:function(data,event,row){
							if($.inArray( "59", child_mod_access) != "-1"){
								return '<button class="btn btn-xs btn-danger staffNotice" id="'+row.notice_id+'">Delete</button>';
							}else{
								return "-";
							}
								},sWidth:"10%"}
					]
				});
				 dataTable.on( 'order.dt search.dt', function () {
				        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
				            cell.innerHTML = i+1;
							});
						}).draw(); 
			}
			commonHandler.error = function(e){
				$.notify({message: "Notice not saved successfully"},{type: 'danger'});
				}
			rest.get("rest/customuserservice/getStaffNotice",commonHandler);	
			loadFlag = true;
		}	 
		}	
		 });
	 
	 $(".studentNotice").click(function(){
		 modal.modalConfirm("Delete","Are you sure?","Cancel","Delete",deleteStudentNotice,[$(this).prop("id"),$(this)]);
	 });
	 
	 $("#staffNoticeTable").on("click",".staffNotice",function(){
		 modal.modalConfirm("Delete","Are you sure?","Cancel","Delete",deleteStaffNotice,[$(this).prop("id"),$(this)]);
	 });
});

function deleteStudentNotice(id,that){
	var handlers = {};
	handlers.success = function(){
		var table = $("#studentNoticeTable").DataTable();
		that.closest("tr").addClass('selected');
		table.row('.selected').remove().draw( false );
		$.notify({message: "Notice deleted successfully"},{type: 'success'});	
	}
	handlers.error = function(e){
		$.notify({message: "Notice not deleted successfully"},{type: 'danger'});
		}
	rest.deleteItem("rest/customuserservice/deleteStudentNotice/"+id,handlers);
}

function deleteStaffNotice(id,that){
	var handlers = {};
	handlers.success = function(){
		var table = $("#staffNoticeTable").DataTable();
		that.closest("tr").addClass('selected');
		table.row('.selected').remove().draw( false );
		$.notify({message: "Notice deleted successfully"},{type: 'success'});	
	}
	handlers.error = function(e){
		$.notify({message: "Notice not deleted successfully"},{type: 'danger'});
		}
	rest.deleteItem("rest/customuserservice/deleteStaffNotice/"+id,handlers);
}
</script>
</head>
<body>
<% String[] child_mod_access = (String[])session.getAttribute("child_mod_access"); %>
<jsp:include page="NoticeBoardHeader.jsp" >
		<jsp:param value="active" name="customUserViewNoticeBoard"/>
	</jsp:include>
<div class="well">
<div class="row">
<div class="col-md-3">
<select class="form-control" id="noticeType">
<option value="1">Student Notice</option>
<option value="2">Staff Notice</option>
</select>
</div>
</div>
</div>
<div class="container" style="padding: 1%" id="studentNoticeDiv">
<table class="table table-striped" id="studentNoticeTable">
<thead>
	<tr>
		<th>#</th>
		<th>Notice</th>
		<th>Class</th>
		<th>Batch</th>
		<th>Start Date</th>
		<th>End Date</th>
		<th>Action</th>
	</tr>
</thead>
<tbody>
	<c:forEach items="${studentNoticeList}" var="notice" varStatus="counter">
	<c:set var="start_date" value='${notice.start_date}' />
	<c:set var="end_date" value='${notice.end_date}' />
	<%
  String resp [] = new String[3];
  String start_date = (String)pageContext.getAttribute("start_date").toString();
  resp = start_date.split("-");
  pageContext.setAttribute("start_date", resp[2]+"-"+resp[1]+"-"+resp[0]);
  String end_date = (String)pageContext.getAttribute("end_date").toString();
  resp = end_date.split("-");
  pageContext.setAttribute("end_date", resp[2]+"-"+resp[1]+"-"+resp[0]);
%>
	<tr>
	<td></td>
	<td><c:out value="${notice.notice}"></c:out></td>
	<td><c:out value="${notice.div_name}"></c:out></td>
	<td><c:out value="${notice.batch_names}"></c:out></td>
	<td><c:out value="${start_date}"></c:out></td>
	<td><c:out value="${end_date}"></c:out></td>
	<td>
	<% if(ArrayUtils.contains(child_mod_access,"59")){ %>
	<button class="btn btn-xs btn-danger studentNotice" id="<c:out value="${notice.notice_id}"></c:out>">Delete</button>
	<%}else{ %>
	-
	<%} %>
	</td>
	</tr>
	</c:forEach>
</tbody>
</table>
</div>
<input type="hidden" class="form-control" id="accessRights" value='<%=String.join(",",child_mod_access)%>'>
<div class="container" style="padding: 1%;display: none;" id="staffNoticeDiv">
<table class="table table-striped" id="staffNoticeTable"></table>
</div>
</body>
</html>