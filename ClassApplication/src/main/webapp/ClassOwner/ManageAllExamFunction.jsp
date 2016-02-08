<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script>
$(document).ready(function(){
	$('#myTab a').click(function (e) {
	  e.preventDefault()
	  $(this).tab('show');
	});
	loadLogoImage();
	$("#imageUploadBtn").on("click",uploadFile);	
});

function successImageUpload(e){
	console.log(e);
	var uri = "rest/classownerservice/addLogoImages/"+e.fileid+"/image";
	var handlers = {};
	handlers.success = function(e){console.log("Success",e)}
	handlers.error = function(e){console.log("Error",e)}
	rest.post(uri,handlers);
}

function uploadFile(){
	var handler = {}
	handler.success = successImageUpload;
	handler.error = function(){};
	
	var submitDataFile = $("#imageUpload")[0]
	rest.uploadImageFile(submitDataFile ,handler,false);
}

function loadLogoImage(){
	var handler = {};
	handler.success = loadImageSuccess;
	handler.error = loadImageError;
	rest.get("rest/classownerservice/logoImages",handler,true);
}

function loadImageSuccess(images){
	var row = "";
	for(var index in images){
		if(index%6 == 0){
			row = row + "<div class='row'>";
		}
		row = row + "<div class='col-md-2'>"+"<img height='64px' src='/rest/commonservices/showimage/LOGO/"+images[index].imagename+"'/>"+"</div>";
		if(index%6 == 5){
			row = row+"</div>";
		}
	}
	console.log(row);
	$("#logoImages").html(row);
}

function loadImageError(){
	
}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
	  <li class="active"><a href="#ManageExam" data-toggle="tab">Manage exam</a></li>
	  <li><a href="#ManageHeader" data-toggle="tab">Manage header</a></li>
	  <li><a href="#ManageImage" data-toggle="tab">Manage image</a></li>
	</ul>

	<!-- Tab panes -->
	<div class="tab-content">
	  <div class="tab-pane active" id="ManageExam">ManageExam</div>
	  <div class="tab-pane" id="ManageHeader">ManageHeader</div>
	  <div class="tab-pane" id="ManageImage">
		<input type="file" id="imageUpload"/>
		<input type="button" id="imageUploadBtn" value="Upload"/>
		<div class="container" id="logoImages"></div>
	</div>
</body>
</html>