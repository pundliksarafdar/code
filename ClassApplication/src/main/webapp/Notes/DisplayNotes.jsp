<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="s" uri="/struts-tags"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery-1.10.2.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<style>
.navigationposiotion{
font-size: x-large;
font-weight: bolder;
}
</style>

<script type="text/javascript">
$(document).ready(function(){
	 $(document).on("contextmenu",function(){
		    return false;
		    });
	var visitedpages=[];
	visitedpages.push(0);
	  if(parseInt($("#totalpages").val())-1==0){
		  $("#next").css("color","#728080");
		  $("#next").attr("disabled",true); 
	  }
	  
$("#prev").click(function(){
	$("#prev").attr("disabled",true); 
	var pageno=parseInt($("#currentpage").val())-1;
	var visited=jQuery.inArray(pageno,visitedpages);
	if(visited=="-1"){
$.ajax({
	 
	   url: "classOwnerServlet",
	   data: {
	    	 methodToCall: "navigatepage",
	    	 pageno:pageno
	   		},
	   		success:function(data){
	   			var resultJson = JSON.parse(data);
				   var base64=resultJson.base64;
				   visitedpages.push(pageno);
				 // $("#notesimage").attr("src","data:image/png;base64,"+base64);
				 $("#notesimage_"+$("#currentpage").val()).hide();
				  $("#currentpage").val(pageno);
				  // $("#imagesdiv").append("<img src='data:image/png;base64,"+base64+"' id='notesimage_"+$("#currentpage").val()+"'>");
				  $("#imagesdiv").append("<img src=<s:url action='ImageAction?imageId=notesimage_"+$("#currentpage").val()+"' /> id='notesimage_"+$("#currentpage").val()+"'>");
				  if(pageno==0){
					  $("#prev").css("color","#728080");
					  $("#prev").attr("disabled",true); 
				  }else{
					  $("#prev").attr("disabled",false); 
					  $("#prev").css("color","#428bca");
				  }
				  if(parseInt($("#totalpages").val())-1==pageno){
					  $("#next").css("color","#728080");
					  $("#next").attr("disabled",true); 
				  }else{
					  $("#next").attr("disabled",false); 
					  $("#next").css("color","#428bca");
				  }
				  $("#currentpageno").html(pageno+1);
	   		},
	   		error:function(error){
	   		
	   		}
});}else{
	 $("#notesimage_"+$("#currentpage").val()).hide();
	$("#notesimage_"+pageno).show()
	 $("#currentpage").val(pageno);
	if(pageno==0){
		  $("#prev").css("color","#728080");
		  $("#prev").attr("disabled",true); 
	  }else{
		  $("#prev").attr("disabled",false); 
		  $("#prev").css("color","#428bca");
	  }
	  if(parseInt($("#totalpages").val())-1==pageno){
		  $("#next").css("color","#728080");
		  $("#next").attr("disabled",true); 
	  }else{
		  $("#next").attr("disabled",false); 
		  $("#next").css("color","#428bca");
	  }
	  $("#currentpageno").html(pageno+1);
	 
}

});
$("#next").click(function(){
	$("#next").attr("disabled",true);
	var pageno=parseInt($("#currentpage").val())+1;
	var visited=jQuery.inArray(pageno,visitedpages);
	if(visited=="-1"){
$.ajax({
	 
	   url: "classOwnerServlet",
	   data: {
	    	 methodToCall: "navigatepage",
	    	 pageno:pageno
	   		},
	   		success:function(data){
	   			var resultJson = JSON.parse(data);
				   var base64=resultJson.base64;
				   visitedpages.push(pageno);
				 // $("#notesimage").attr("src","data:image/png;base64,"+base64);
				 $("#notesimage_"+$("#currentpage").val()).hide();
				  $("#currentpage").val(pageno);
				   //$("#imagesdiv").append("<img src='data:image/png;base64,"+base64+"' id='notesimage_"+$("#currentpage").val()+"'>");
				   $("#imagesdiv").append("<img src=<s:url action='ImageAction?imageId=notesimage_"+$("#currentpage").val()+"' /> id='notesimage_"+$("#currentpage").val()+"'>");
				  if(parseInt($("#totalpages").val())-1==pageno){
					  $("#next").css("color","#728080");
					  $("#next").attr("disabled",true); 
				  }else{
					  $("#next").attr("disabled",false); 
					  $("#next").css("color","#428bca");
				  }
				  if(pageno==0){
					  $("#prev").css("color","#728080");
					  $("#prev").attr("disabled",true); 
				  }else{
					  $("#prev").attr("disabled",false); 
					  $("#prev").css("color","#428bca");
				  }
				  $("#currentpageno").html(pageno+1);
	   		},
	   		error:function(error){
	   		
	   		}
});
	}else{
		 $("#notesimage_"+$("#currentpage").val()).hide();
		$("#notesimage_"+pageno).show()
		 $("#currentpage").val(pageno);
		if(pageno==0){
			  $("#prev").css("color","#728080");
			  $("#prev").attr("disabled",true); 
		  }else{
			  $("#prev").attr("disabled",false); 
			  $("#prev").css("color","#428bca");
		  }
		  if(parseInt($("#totalpages").val())-1==pageno){
			  $("#next").css("color","#728080");
			  $("#next").attr("disabled",true); 
		  }else{
			  $("#next").attr("disabled",false); 
			  $("#next").css("color","#428bca");
		  }
		  $("#currentpageno").html(pageno+1);
	}

});

$("#goto").click(function(){
	var numberExpr = /^[0-9]+$/;
	if($("#gotopageno").val().match(numberExpr)){
		if(parseInt($("#gotopageno").val()) <= parseInt($("#totalpages").val()) && parseInt($("#gotopageno").val())>0){
		$("#pagenoerror").html("");
	$("#next").attr("disabled",true);
	$("#prev").attr("disabled",true);
	var pageno=parseInt($("#gotopageno").val())-1;
	var visited=jQuery.inArray(pageno,visitedpages);
	if(visited=="-1"){
$.ajax({
	 
	   url: "classOwnerServlet",
	   data: {
	    	 methodToCall: "navigatepage",
	    	 pageno:pageno
	   		},
	   		success:function(data){
	   			var resultJson = JSON.parse(data);
				   var base64=resultJson.base64;
				   visitedpages.push(pageno);
				 // $("#notesimage").attr("src","data:image/png;base64,"+base64);
				 $("#notesimage_"+$("#currentpage").val()).hide();
				  $("#currentpage").val(pageno);
				 //  $("#imagesdiv").append("<img src='data:image/png;base64,"+base64+"' id='notesimage_"+$("#currentpage").val()+"'>");
				 $("#imagesdiv").append("<img src=<s:url action='ImageAction?imageId=notesimage_"+$("#currentpage").val()+"' /> id='notesimage_"+$("#currentpage").val()+"'>");
				  if(parseInt($("#totalpages").val())-1==pageno){
					  $("#next").css("color","#728080");
					  $("#next").attr("disabled",true); 
				  }else{
					  $("#next").attr("disabled",false); 
					  $("#next").css("color","#428bca");
				  }
				  if(pageno==0){
					  $("#prev").css("color","#728080");
					  $("#prev").attr("disabled",true); 
				  }else{
					  $("#prev").attr("disabled",false); 
					  $("#prev").css("color","#428bca");
				  }
				  $("#currentpageno").html(pageno+1);
	   		},
	   		error:function(error){
	   		
	   		}
});
	}else{
		 $("#notesimage_"+$("#currentpage").val()).hide();
		$("#notesimage_"+pageno).show()
		 $("#currentpage").val(pageno);
		if(pageno==0){
			  $("#prev").css("color","#728080");
			  $("#prev").attr("disabled",true); 
		  }else{
			  $("#prev").attr("disabled",false); 
			  $("#prev").css("color","#428bca");
		  }
		  if(parseInt($("#totalpages").val())-1==pageno){
			  $("#next").css("color","#728080");
			  $("#next").attr("disabled",true); 
		  }else{
			  $("#next").attr("disabled",false); 
			  $("#next").css("color","#428bca");
		  }
		  $("#currentpageno").html(pageno+1);
	}
		}
		else{
			$("#pagenoerror").html("Invalid Page No");
		}
	}else{
		$("#pagenoerror").html("Invalid Page No");
	}

});

});
</script>
</head>
<body>

<%String base64=(String)request.getAttribute("imagefile"); %>
<div class="container" style="margin-top: 10px">
<div class="row">
<div class="col-md-10" align="center">
<div class="col-md-2"></div>
<div class="col-md-2"><span id="pagenoerror" style="color: red"></span></div>
<div class="col-md-2">
 <input type="number" class="form-control " min="1" id="gotopageno" placeholder="Page No" max="<c:out value="${totalpages}"></c:out>"></div>
<div class="col-md-3" style="width: 0;"> <button class="btn btn-primary " id="goto">GO</button></div>
<div class="col-md-2" style="font-size: x-large;cursor: pointer;">|<label id="currentpageno">1</label>/<c:out value="${totalpages}"></c:out></div>

</div>
</div>
<hr>
</div>
<div class="container">
<div class="row">
<div class="col-md-1" align="left" style="margin-top: 30%;color:#728080 "><a id="prev" class="btn glyphicon glyphicon-step-backward navigationposiotion" style="color:#728080" disabled='disabled'></a></div>
<div class="col-md-10" id="imagesdiv">
<%-- <img src="data:image/png;base64, <%=base64 %>" id="notesimage_0"> --%>
<%-- <table style="background-image:url('data:image/png;base64, <%=base64 %>');width:Wpx;height:Hpx"></table> --%>
<img src=" <s:url action='ImageAction?imageId=notesimage_0' />"  id="notesimage_0">
</div>
<div class="col-md-1" align="right" style="margin-top: 30%"> <a id="next" class="btn glyphicon glyphicon-step-forward navigationposiotion"></a></div>
</div>
</div>
<input type="hidden" name="totalpages" id="totalpages" value="<c:out value="${totalpages}"></c:out>">
<input type="hidden" name="currentpage" id="currentpage" value="0">
</body>
</html>