<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style>
.page {
        width: 21cm;
        min-height: 29.7cm;
        margin: 1cm auto;
        border: 1px #D3D3D3 solid;
        border-radius: 5px;
        background: white;
        box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
    }
	
	.grabbable:active { 
    cursor: grabbing;
    cursor: -moz-grabbing;
    cursor: -webkit-grabbing;
}

.headerElement[placeholder]:empty:before {
    content: attr(placeholder);
    color: #555; 
}

.headerElement[placeholder]:empty:focus:before {
    content: "";
}
	
@media print {
  body * {
    visibility: hidden;
  }
  #headerContainer, #headerContainer * {
    visibility: visible;
  }
  #headerContainer {
    position: absolute;
    left: 0;
    top: 0;
  }
}
</style>
<script>
$(document).ready(function(){
	var manageImage = new ManageImage();
	var manageHeader = new ManageHeader();
	/*
	$('#myTab a').click(function (e) {
	  e.preventDefault()
	  $(this).tab('show');
	});
	*/
	manageImage.loadLogoImage();
	$("#imageUploadBtn").on("click",manageImage.uploadFile);	
	$("#addLogo").on("click",manageHeader.loadLogoImage)
	$("body").on("click",".useThisLogo",function(e){
		var img = $(this).closest('div').find('img');
		$("#headerContainer").append(img);
	});	
	/*Header function need to move to respective closure*/
		$("#addRow").on("click",function(){
		$("#headerContainer").append("<div contenteditable='true' class='headerElement' placeholder='This element is empty Click here to edit or delete it'>Click here to edit</div>");
	});
	
	$("#getFocus").on("click",function(){
		console.log($("div:focus"));
	});
	
	$("body").on("click",".headerElement",function(){
		$(".headerElement").removeClass("selected");
		$(this).addClass("selected");
	});
	
	$("body").on("click","img",function(){
		
	});
	
	$("#justifyLeft").on("click",function(){
		$(".selected").css("text-align","left");
	});
	
	$("#justifyCenter").on("click",function(){
		$(".selected").css("text-align","center");
	});
	
	$("#justifyRight").on("click",function(){
		$(".selected").css("text-align","right");
	});
	
	$("#increseSize").on("click",function(){
		$(".selected").css("font-size",parseFloat($(".selected").css("font-size"))+1);
	});
	
	$("#saveHeader").on("click",function(){
		var data = $("#headerContainer").html();
		var headerName = $("#headerName").val();
			
		if(headerName.trim().length != 0){
			var handlers = {};
			var uri = "rest/classownerservice/saveHeader/"+headerName;
			handlers.success = function(e){$.notify({message: "Header saved"},{type: 'success'});}
			handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});}
			rest.postString(uri,handlers,data);		
		}
	});
	var x,y;
	document.body.ondragover = function(evt) {
		x = evt.x;
		y = evt.y;
		var position = $("#headerContainer").offset();
		
		x = x - position.left;
		y = y - position.top;
		
		//$("#logo").css({"top":y,"left":x});
		
	};
	
	$("body").on("dragend","img",function(evt) {
		$(this).css({"top":y,"left":x});
	});

});

function ManageImage(){
var successImageUpload = function(e){
	var uri = "rest/classownerservice/addLogoImages/"+e.fileid+"/image";
	var handlers = {};
	handlers.success = function(e){$.notify({message: "Logo image saved"},{type: 'success'});}
	handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});}
	rest.post(uri,handlers);
}

this.uploadFile = function(){
	var handler = {}
	handler.success = successImageUpload;
	handler.error = function(){$.notify({message: "Error"},{type: 'danger'});};
	
	var submitDataFile = $("#imageUpload")[0]
	rest.uploadImageFile(submitDataFile ,handler,false);
}

this.loadLogoImage = function(){
	var handler = {};
	handler.success = loadImageSuccess;
	handler.error = loadImageError;
	rest.get("rest/classownerservice/logoImages",handler,true);
}

var loadImageSuccess = function(images){
	var row = "";
	for(var index in images){
		if(index%6 == 0){
			row = row + "<div class='row'>";
		}
		row = row + "<div class='col-md-2'>"+"<img draggable='true' height='64px' src='/rest/commonservices/showimage/L/"+images[index].image_id+"'/>"+images[index].image_name+"</div>";
		if(index%6 == 5){
			row = row+"</div>";
		}
	}
	$("#logoImages").html(row);
}

var loadImageError = function(){
	$.notify({message: "Error"},{type: 'danger'});
}
}

function ManageHeader(){
this.loadLogoImage = function(){
	var handler = {};
	handler.success = loadImageSuccess;
	handler.error = loadImageError;
	rest.get("rest/classownerservice/logoImages",handler,true);
}

loadImageSuccess = function(images){
	var row = "";
	for(var index in images){
		if(index%6 == 0){
			row = row + "<div class='row'>";
		}
		row = row + "<div class='col-md-2'>"+"<img draggable='true' style='position: relative;' height='64px' src='/rest/commonservices/showimage/L/"+images[index].image_id+"'/>"+images[index].image_name+"<input type='button' class='btn btn-default useThisLogo' value='Use this'/></div>";
		if(index%6 == 5){
			row = row+"</div>";
		}
	}
	$("#logoImageContainer").html(row).removeClass("hide");
}

loadImageError = function(){
	$.notify({message: "Error"},{type: 'danger'});
}	
}


</script>

</head>
<body>
	<jsp:include page="ClassownerSettingHeader.jsp" >
		<jsp:param value="active" name="notificationSetting"/>
	</jsp:include>
	<ul class="nav nav-tabs innermenu">
	  <li class="active"><a href="#ManageExam" data-toggle="tab">Manage exam</a></li>
	  <li><a href="#ManageHeader" data-toggle="tab">Manage header</a></li>
	  <li><a href="#ManageImage" data-toggle="tab">Manage image</a></li>
	</ul>

	<!-- Tab panes -->
	<div class="tab-content">
	  <div class="tab-pane active" id="ManageExam">ManageExam</div>
	  <div class="tab-pane" id="ManageHeader">
		<div class="btn-group">
			<input type="button" class="btn btn-default" value="Add logo" id="addLogo"/>
0			<div class="input-group" style="width:30%;">
			  <input type="text" class="form-control" placeholder="Header pattern name" id="headerName">
			  <div class="input-group-btn">
				<input type="button" class="btn btn-default" value="Save header" id="saveHeader"/>
			  </div>
			</div>
		</div>
		
		<div class="logoImageContainer hide" id="logoImageContainer">
			
		</div>
		<!--Header creation-->
		<a id="addRow" class="btn btn-default">Add row</a>
		<a id="justifyLeft" class="btn btn-default"><i class='glyphicon glyphicon-align-left'></i></a>
		<a id="justifyCenter" class="btn btn-default"><i class='glyphicon glyphicon-align-center'></i></a>
		<a id="justifyRight" class="btn btn-default"><i class='glyphicon glyphicon-align-right'></i></a>
		<a id="increseSize" class="btn btn-default"><i class='glyphicon glyphicon-zoom-in'></i></a>
		<a id="decreseSize" class="btn btn-default"><i class='glyphicon glyphicon-zoom-out'></i></a>
		
		<div id="headerContainer" class="page" droppable="true">
		</div>
		<div id="fontSelect"></div>
		<!--End header creation-->
	  </div>
	  
	  <div class="tab-pane" id="ManageImage">
		<input type="file" id="imageUpload"/>
		<input type="button" id="imageUploadBtn" value="Upload"/>
		<div class="container" id="logoImages"></div>
	</div>
</body>
</html>