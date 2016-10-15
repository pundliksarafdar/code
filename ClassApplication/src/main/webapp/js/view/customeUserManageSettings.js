var allHeaderUri = "/rest/customuserservice/header";
var getHeaderUri = "/rest/customuserservice/getHeader/";
var deleteHeaderUri = "/rest/customuserservice/header/";
$(document).ready(function(){
	manageSettings();
	var toolbarOption = $.summernote.options.toolbar;
	$.summernote.options.buttons = { addMarksButton: AddMarksButton,addSubjectNameButton:AddSubjectNameButton,addStandardButton:AddStandardButton,
			addStreamButton:AddStreamButton,addExamNameButton:AddExamNameButton};
	toolbarOption.push(['fontsize', ['fontsize']]);
	toolbarOption.push(['misc', ['print']]);
	toolbarOption.push(['mybutton', ['addMarksButton','addSubjectNameButton','addStandardButton','addStreamButton','addExamNameButton']]);
	$(".summernote").summernote();
});

var AddMarksButton = function (context) {
	  var ui = $.summernote.ui;
	  
	  // create button
	  var button = ui.button({
	    contents: 'Marks',
	    tooltip: 'Marks will be added automatically on exam generation',
	    click: function () {
	      // invoke insertText method with 'hello' on editor module.
	      context.invoke('editor.insertText', '{{marks}}');
	    }
	  });

	  return button.render();   // return button as jquery object 
	}

var AddSubjectNameButton = function (context) {
	  var ui = $.summernote.ui;
	  
	  // create button
	  var button = ui.button({
	    contents: 'Subject',
	    tooltip: 'Subject name will be added automatically on exam generation',
	    click: function () {
	      // invoke insertText method with 'hello' on editor module.
	      context.invoke('editor.insertText', '{{SubjectName}}');
	    }
	  });

	  return button.render();   // return button as jquery object 
	}

var AddStandardButton = function (context) {
	  var ui = $.summernote.ui;
	  
	  // create button
	  var button = ui.button({
	    contents: 'Standard',
	    tooltip: 'Standard will be added automatically on exam generation',
	    click: function () {
	      // invoke insertText method with 'hello' on editor module.
	      context.invoke('editor.insertText', '{{Standard}}');
	    }
	  });

	  return button.render();   // return button as jquery object 
	}

var AddStreamButton = function (context) {
	  var ui = $.summernote.ui;
	  
	  // create button
	  var button = ui.button({
	    contents: 'Stream',
	    tooltip: 'Stream will be added automatically on exam generation',
	    click: function () {
	      // invoke insertText method with 'hello' on editor module.
	      context.invoke('editor.insertText', '{{StreamName}}');
	    }
	  });

	  return button.render();   // return button as jquery object 
	}

var AddExamNameButton = function (context) {
	  var ui = $.summernote.ui;
	  
	  // create button
	  var button = ui.button({
	    contents: 'Exam name',
	    tooltip: 'Exam name will be added automatically on exam generation',
	    click: function () {
	      // invoke insertText method with 'hello' on editor module.
	      context.invoke('editor.insertText', '{{ExamName}}');
	    }
	  });

	  return button.render();   // return button as jquery object 
	}
var manageSettings = function(){
	$("body").on("click","#save",save);
	$("body").on("mouseover",".headerIds",showHeaders)
	.on("click",".delete",deleteHeader);
	getAllHeader();
	
	function save(){
		var data  = $('.summernote').summernote('code');
		var headerName = $("#headerName").val();
		if(!$("#header-form").valid()){
			return;
		}
		var handlers = {};
		var uri = "rest/customuserservice/saveHeader/"+headerName;
		handlers.success = function(e){$.notify({message: "Header saved"},{type: 'success'});getAllHeader();}
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});}
		rest.postString(uri,handlers,data);
		
	}
	
	function getAllHeader(){
		var handler = {};
		handler.success = showHeaderInfo;
		handler.error = function(){};
		rest.get(allHeaderUri,handler);
	}
	
	function showHeaderInfo(headers){
		$("#headers_name_table").dataTable({
			bDestroy:true,
			data: headers,
			lengthChange: false,
			columns:[
			{
				title: "Name",data:"header_name",render:function(data,event,row){
					return "<div class='headerIds' headerId='"+row.header_id+"'><a href='#'>"+data+"</a></div>";
				}
			},
			{
				title: "",data:null,render:function(data,event,row){
					return "<i title='Delete' class='delete glyphicon glyphicon-trash' headerId='"+row.header_id+"'></i>";
				},bSortable:false
			}]
		});
	}
	
	function showHeaders(){
		$("#headers_html").html("<i class=''></i>Loading...");
		var headerId= $(this).attr("headerId");
		var handler = {};
		handler.success = function(e){$("#headers_html").html(e)};
		handler.error = function(){}
		rest.get(getHeaderUri+headerId,handler,false);
	}
	
	function deleteHeader(){
		var headerId = $(this).attr("headerId");
		var handler = {};
		handler.success = function(e){getAllHeader();};
		handler.error = function(e){};
		rest.deleteItem(deleteHeaderUri+headerId,handler);
	}
}