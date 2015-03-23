<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>    

<style>
.urlSelector{
	width:50%;
	padding-top : 2px;
	padding-bottom : 2px;
}

 .btn-noaccess,.btn-noaccess:hover{
	background-color: #d9534f !important;	
	color:white;
 }
 
 
 .btn-access,.btn-access:hover{
	background-color: #4cae4c !important;	
	color:white;
 }
</style>

<script>
	$(document).ready(function(){
		$(".urlSelector a").click(function(){
			$(this).toggleClass("btn-access");
			if($(this).hasClass("btn-access")){
				/*Adding value*/
				console.log("Adding value");
				var accessvariable = $(this).parents('.urlSelector').find("#linkaccess");
				
				if(accessvariable.val().trim() == "")
					accessvariable.val($(this).attr("value"));	
				else
					accessvariable.val(accessvariable.val()+","+$(this).attr("value"));		
				
			}else{
				console.log("Removinging value");
				var accessvariable = $(this).parents('.urlSelector').find("#linkaccess");
				if(accessvariable.val().indexOf($(this).attr("value")) != -1){
					/*if value doesn't contains (,) means only one user is having access so clear the variable*/
					if(accessvariable.val().indexOf(',') == -1){
						accessvariable.val("");
					}else{
						var accessbleUser;
						/*if pattern is var, then element is at first*/
						if(accessvariable.val().indexOf(','+$(this).attr("value")+',')!=-1){
							accessbleUser = accessvariable.val().replace(','+$(this).attr("value"),"");
						}else if(accessvariable.val().indexOf($(this).attr("value")+',')!=-1){ /*if patter is ,var, elem is at center*/
							accessbleUser = accessvariable.val().replace($(this).attr("value")+',',"");
						}else{ /*element at end*/
							accessbleUser = accessvariable.val().replace(','+$(this).attr("value"),"");
						}
						accessvariable.val(accessbleUser);
					}		
				}	
			}
		});
		
		/*Load access*/	
		$.each($(".urlSelector"),function(){
			var $that = $(this);
			//console.log($(this).find("#linkaccess").val());
			$.each($(this).find("a"),function(){
				console.log("Value "+$(this).attr("value"));
				if($that.find("#linkaccess").val().indexOf($(this).attr("value"))!=-1){
					$(this).addClass("btn-access");
				}
			});
		});
		
		$(".saveBtn").click(function(){
			var linkAccessJSONArray = [];
			var count = 0;
			$.each($(".urlSelector"),function(){
				var linkName = $(this).find("#linkname").val();
				var linkaccess = $(this).find("#linkaccess").val();
				
				/*
				var linkAccessJSON = {};
				count++;
				linkAccessJSON.linkName = linkName;
				linkAccessJSON.linkaccess = linkaccess;
				linkAccessJSONArray[count] = linkAccessJSON;
				*/
				$("form#saveurlauthentication").append('<input type="hidden" name="linkname" value="'+linkName+'">');
				$("form#saveurlauthentication").append('<input type="hidden" name="linkAccess" value="'+linkaccess+'">');
			});
			console.log(JSON.stringify(linkAccessJSONArray));
			$("form#saveurlauthentication").submit();
		});
	});
	
	
</script>
<c:set value="${requestScope.actionUrl}" var="allURLs"></c:set>
<h3>Set Access you URL</h3>
<div>
	<a class="btn btn-success">Access ON</a>&nbsp;
	<a class="btn btn-danger">Access OFF</a>
</div>
<c:forEach items="${allURLs}" var="urls">
<div class="input-group input-group-sm urlSelector">
  <input type="hidden" id="linkaccess" value='<c:out value="${urls.access}"></c:out>'>	
  <input type="hidden" id="linkname" value='<c:out value="${urls.paths}"></c:out>'>	
  <input type="text" class="form-control" placeholder="Username" aria-describedby="sizing-addon3" value='<c:out value="${urls.paths}"></c:out>' readonly>
  <a class="input-group-addon btn btn-noaccess" value="0">Admin</a>
  <a class="input-group-addon btn btn-noaccess" value="1">Owner</a>
  <a class="input-group-addon btn btn-noaccess" value="2">Teacher</a>
  <a class="input-group-addon btn btn-noaccess" value="3">Student</a>
</div>
</c:forEach>

<form action="saveurlauthentication" id="saveurlauthentication" method="post">
	
</form>
<input type="button" class="btn btn-info saveBtn" value="Save"/>