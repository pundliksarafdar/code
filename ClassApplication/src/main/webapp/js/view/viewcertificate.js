var allHeaderUri = "/rest/classownerservice/header";
var getHeaderUri = "/rest/classownerservice/getHeader/";
var globalBackgroundColor = "white";
var globalBorder = "none";
var globalBorderColor = "black";
var cert_id = "";
var addtionalFieldsString = "";
$(document).ready(function(){
	var data = JSON.parse($("#AdditionalFieldJson").val());
	 $.each(data,function(key,val){
		 addtionalFieldsString = addtionalFieldsString + "<li value="+key+">"+val+"</li>";
	 })
	getAllHeader();
	$("#certificateTable").DataTable();
	$("body").on("change","#headerSelect",showHeaders);
	$("body").on("click","#save",save);
	var toolbarOption = $.summernote.options.toolbar;
	$.summernote.options.buttons = { addStudentNameButton:AddStudentNameButton,addAddressButton:AddAddressButton,addBorderDropDown:AddBorderDropDown,addBorderButton:AddBorderButton,
			addPageBackgroundButton:AddPageBackgroundButton,addPageBackgroundDropDown:AddPageBackgroundDropDown,addBorderColorButton:AddBorderColorButton,
			addBorderColorDropDown:AddBorderColorDropDown,addDetailsDropDown:AddDetailsDropDown,addInstituteDetailsDropDown:AddInstituteDetailsDropDown};
	toolbarOption.push(['fontsize', ['fontsize']]);
	toolbarOption.push(['misc', ['print']]);
	toolbarOption.push(['mybutton', ['addStudentNameButton','addAddressButton']]);
	toolbarOption.push(['studentDetails', ['addDetailsDropDown']]);
	toolbarOption.push(['instituteDetails', ['addInstituteDetailsDropDown']]);
	toolbarOption.push(['border', ['addBorderButton','addBorderDropDown']]);
	toolbarOption.push(['borderColor', ['addBorderColorButton','addBorderColorDropDown']]);
	toolbarOption.push(['pageColor', ['addPageBackgroundButton','addPageBackgroundDropDown']]);
	$(".summernote").summernote();
	
	$(".view").click(function(){
		var uri = "rest/classownerservice/getCerificateTemplate/"+$(this).prop("id");
		var handlers = {};
		handlers.success = function(e){
			$(".viewCertificate").show();
			$(".certificateList").hide();
			$(".viewCertificateData").html(e);
		}
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});}
		rest.get(uri,handlers);
	});
	
	$(".backToList").click(function(){
		$(".viewCertificate").hide();
		$(".editCertificate").hide();
		$(".certificateList").show();
	});
	
	$(".edit").click(function(){
		cert_id = $(this).prop("id");
		var uri = "rest/classownerservice/getCerificateTemplate/"+$(this).prop("id");
		var that = $(this);
		var handlers = {};
		handlers.success = function(e){
			$(".editCertificate").show();
			$(".certificateList").hide();
			$(".summernote").html(e);
			$(".note-editing-area").css("cssText","background:"+$(".certificatePanel").css("background-color")+" !important");
       	  	$(".panel-body").css("cssText","background:"+$(".certificatePanel").css("background-color")+" !important;border:"+$(".certificateMargin").css("border-style")+
       			  ";border-color:"+$(".certificateMargin").css("border-color"));
       	 $(".panel-body").html($(".certificateBody").html())
       	 $("#certificateName").val($(that.closest("tr")).find(".cert_desc").html());
       	 $("#headerSelect").select2().val($(that.closest("td")).find(".headerID").val()).change();
       	 globalBackgroundColor = $(".certificatePanel").css("background-color");
       	 globalBorder = $(".certificateMargin").css("border-style");
       	 globalBorderColor = $(".certificateMargin").css("border-color");
		}
		handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});}
		rest.get(uri,handlers);
	});
	
	$(".delete").click(function(){
		modal.modalConfirm("Delete","Are you sure?","Cancel","Delete",deleteCertificate,[$(this)]);
	});
});

var borderTypes = ['none', 'solid', 'double', 'dashed'];
var  menuCheck = 'note-icon-check';
var  colors = [
              ['#000000', '#424242', '#636363', '#9C9C94', '#CEC6CE', '#EFEFEF', '#F7F7F7', '#FFFFFF'],
              ['#FF0000', '#FF9C00', '#FFFF00', '#00FF00', '#00FFFF', '#0000FF', '#9C00FF', '#FF00FF'],
              ['#F7C6CE', '#FFE7CE', '#FFEFC6', '#D6EFD6', '#CEDEE7', '#CEE7F7', '#D6D6E7', '#E7D6DE'],
              ['#E79C9C', '#FFC69C', '#FFE79C', '#B5D6A5', '#A5C6CE', '#9CC6EF', '#B5A5D6', '#D6A5BD'],
              ['#E76363', '#F7AD6B', '#FFD663', '#94BD7B', '#73A5AD', '#6BADDE', '#8C7BC6', '#C67BA5'],
              ['#CE0000', '#E79439', '#EFC631', '#6BA54A', '#4A7B8C', '#3984C6', '#634AA5', '#A54A7B'],
              ['#9C0000', '#B56308', '#BD9400', '#397B21', '#104A5A', '#085294', '#311873', '#731842'],
              ['#630000', '#7B3900', '#846300', '#295218', '#083139', '#003163', '#21104A', '#4A1031']
            ];
var AddBorderButton = function (context) {
	  var ui = $.summernote.ui;
	  
	  // create button
	  var button = ui.button({
	    contents: 'Border',
	    tooltip: 'Border',
	    click: function () { }
	  });

	  return button.render();   // return button as jquery object 
	}

var AddBorderDropDown = function (context) {

    var ui = $.summernote.ui;
    var list = "<li>None</li><li>Solid</li><li>Double</li><li>Dashed</li><li>Groove</li><li>Ridge</li><li>Inset</li><li>Outset</li>";

    var button = ui.buttonGroup([
        ui.button({
            className: 'dropdown-toggle',
            contents: ' <span class="selectedBorder">None</span> <span class="caret"></span>',
            data: {
                toggle: 'dropdown'
            }
        }),
        ui.dropdown({
            className: 'drop-default summernote-list',
            contents: "<ul type='none' style='cursor: pointer;padding-left: 25%;margin-bottom:0%'>"+list+"</ul>",
            callback: function ($dropdown) {                    
                $dropdown.find('li').each(function () {
                    $(this).click(function () {                                                       
                   	 $($($($(this).closest("div")).closest(".note-btn-group ")).find(".dropdown-toggle")).find(".selectedBorder").html($(this).html());
                   	$(".panel-body").css("border-style", $(this).html());
                   	/* $(".certificateMargin").css("border-style", $(this).html());*/
                   	 globalBorder = $(this).html();
                    });
                });
            }
        })
    ]);        

    return button.render();   // return button as jquery object 
}

var AddBorderColorButton = function (context) {
	  var ui = $.summernote.ui;
	  
	  // create button
	  var button = ui.button({
	    contents: 'Border Color',
	    tooltip: 'Border Color',
	    click: function () { }
	  });

	  return button.render();   // return button as jquery object 
	}

var AddBorderColorDropDown = function (context) {

  var ui = $.summernote.ui;
  var list = "<li>None</li><li>Solid</li><li>Double</li><li>Dashed</li><li>Groove</li><li>Ridge</li><li>Inset</li><li>Outset</li>";

  var button = ui.buttonGroup([
      ui.button({
          className: 'dropdown-toggle',
          contents: ' <div style="border:1px black solid;width:25px;height:15px;background:black" class="borderColor"></div> <span class="caret"></span>',
          data: {
              toggle: 'dropdown'
          }
      }),
      ui.dropdown({
          items: [
                  '<li>',
                  '<div class="btn-group" style="min-width:170px">',
                 /* '  <div class="note-palette-title">Page Color</div>',
                  '  <div>',
                  '    <button type="button" class="note-color-reset btn btn-default" data-event="backColor" data-value="inherit">',
                  'Transparent',
                  '    </button>',
                  '  </div>',*/
                  '  <div class="note-holder" data-event="backColor"/>',
                  '</div>',
                  '<div class="btn-group" style="display:none">',
                  '  <div class="note-palette-title">Fo</div>',
                  '  <div>',
                  '    <button type="button" class="note-color-reset btn btn-default" data-event="removeFormat" data-value="foreColor">',
                  'Default',
                  '    </button>',
                  '  </div>',
                  '  <div class="note-holder" data-event="foreColor"/>',
                  '</div>',
                  '</li>'
                ].join(''),
                callback: function ($dropdown) {
                  $dropdown.find('.note-holder').each(function () {
                    var $holder = $(this);
                    $holder.append(ui.palette({
                      colors: colors,
                      eventName: $holder.data('event')
                    }).render());
                  });
                },
                click: function ($dropdown) {
              	  /*$(".certificateMargin").css("border-color",$($dropdown.target).attr("data-value"));*/
              	  $($($($(this).closest("div")).closest(".note-btn-group ")).find(".dropdown-toggle")).find(".borderColor").css("background",$($dropdown.target).attr("data-value"));
                 $(".panel-body").css("border-color",$($dropdown.target).attr("data-value"));
              	  globalBorderColor = $($dropdown.target).attr("data-value");
                }
              })
  ]);        

  return button.render();   // return button as jquery object 
}


var AddPageBackgroundButton = function (context) {
	  var ui = $.summernote.ui;
	  
	  // create button
	  var button = ui.button({
	    contents: 'Page Color',
	    tooltip: 'Page Color',
	    click: function () { }
	  });

	  return button.render();   // return button as jquery object 
	}

var AddPageBackgroundDropDown = function (context) {

   var ui = $.summernote.ui;
   var list = "<li>None</li><li>Solid</li><li>Double</li><li>Dashed</li><li>Groove</li><li>Ridge</li><li>Inset</li><li>Outset</li>";

   var button = ui.buttonGroup([
       ui.button({
           className: 'dropdown-toggle',
           contents: ' <div style="border:1px black solid;width:25px;height:15px;background:white" class="selectedPageColor"></div> <span class="caret"></span>',
           data: {
               toggle: 'dropdown'
           }
       }),
       ui.dropdown({
           items: [
                   '<li>',
                   '<div class="btn-group" style="min-width:170px">',
                   /*'  <div class="note-palette-title">Page Color</div>',
                   '  <div>',
                   '    <button type="button" class="note-color-reset btn btn-default" data-event="backColor" data-value="inherit">',
                   'Transparent',
                   '    </button>',
                   '  </div>',*/
                   '  <div class="note-holder" data-event="backColor"/>',
                   '</div>',
                   '<div class="btn-group" style="display:none">',
                   '  <div class="note-palette-title">Fo</div>',
                   '  <div>',
                   '    <button type="button" class="note-color-reset btn btn-default" data-event="removeFormat" data-value="foreColor">',
                   'Default',
                   '    </button>',
                   '  </div>',
                   '  <div class="note-holder" data-event="foreColor"/>',
                   '</div>',
                   '</li>'
                 ].join(''),
                 callback: function ($dropdown) {
                   $dropdown.find('.note-holder').each(function () {
                     var $holder = $(this);
                     $holder.append(ui.palette({
                       colors: colors,
                       eventName: $holder.data('event')
                     }).render());
                   });
                 },
                 click: function ($dropdown) {
               	  $(".note-editing-area").css("cssText","background:"+$($dropdown.target).attr("data-value")+" !important");
               	  $(".panel-body").css("cssText","background:"+$($dropdown.target).attr("data-value")+" !important;border:"+globalBorder+
               			  ";border-color:"+globalBorderColor);
               	// $(".certificatePanel").css("cssText","background:"+$($dropdown.target).attr("data-value")+" !important");
               	 /* $(".certificateBody").css("background",$($dropdown.target).attr("data-value"));*/
               	  $($($($(this).closest("div")).closest(".note-btn-group ")).find(".dropdown-toggle")).find(".selectedPageColor").css("background",$($dropdown.target).attr("data-value"));
               	  globalBackgroundColor = $($dropdown.target).attr("data-value");
                 }
               })
   ]);        

   return button.render();   // return button as jquery object 
}

var AddAddressButton = function (context) {
	  var ui = $.summernote.ui;
	  
	  // create button
	  var button = ui.button({
	    contents: 'Address',
	    tooltip: 'Student address will be added automatically on certificate generation',
	    click: function () {
	      // invoke insertText method with 'hello' on editor module.
	      context.invoke('editor.insertText', '{{StudentAddress}}');
	    }
	  });

	  return button.render();   // return button as jquery object 
	}

var AddStudentNameButton = function (context) {
	  var ui = $.summernote.ui;
	  
	  // create button
	  var button = ui.button({
	    contents: 'Student',
	    tooltip: 'Student name will be added automatically on certificate generation',
	    click: function () {
	      // invoke insertText method with 'hello' on editor module.
	      context.invoke('editor.insertText', '{{StudentName}}');
	    }
	  });

	  return button.render();   // return button as jquery object 
	}

var AddDetailsDropDown = function (context) {
	var con = context;
   var ui = $.summernote.ui;
   var list = "<li value='StudentFullName'>Student full name</li>" +
   			  "<li value='StudentFirstName'>Student first name</li>" +
   			  "<li value='StudentLastName'>Student last name</li>" +
   			  "<li value='ParentFullName'>Parent full name</li>" +
   			  "<li value='ParentFirstName'>Parent first name</li>" +
   			  "<li value='ParentLastName'>Parent last name</li>" +
   			  "<li value='StudentAddress'>Address</li>" +
   			  "<li value='StudentDOB'>Date Of Birth</li>" +
   			  "<li value='StudentJoiningDate'>Joining Date</li>" +
   			  "<li value='StudentMobile'>Mobile</li>" +
   			  "<li value='StudentEmail'>Email</li>"+addtionalFieldsString;

   var button = ui.buttonGroup([
       ui.button({
           className: 'dropdown-toggle',
           contents: ' <span class="selectedBorder">Select Student Placeholders</span> <span class="caret"></span>',
           data: {
               toggle: 'dropdown'
           },
           click: function () {
        	   context.invoke('editor.saveRange');
        	   }
       }),
       ui.dropdown({
           className: 'drop-default summernote-list',
           contents: "<ul type='none' style='cursor: pointer;padding-left: 1%;font-size:small'>"+list+"</ul>",
           callback: function ($dropdown) {
               $dropdown.find('li').each(function () {
                  /* $(this).click(function () {            
                	   $(".summernote").summernote('editor.saveRange');
                	   $(".summernote").summernote('editor.restoreRange');
                	   $(".summernote").summernote('editor.focus');
                	   con.summernote('editor.insertText', '{{Po}}');
                   });*/
                   $(this).hover(function(){
                       $(this).css("background-color", "#eeeeee");
                       $(this).attr("data-val", "1");
                       }, function(){
                       $(this).css("background-color", "white");
                       $(this).attr("data-val", "0");
                   });
               });
           },
           click:function(){
        	   context.invoke('editor.restoreRange');
        	   context.invoke('editor.focus');
        	   context.invoke('editor.insertText', '{{'+$($(this).find("li[data-val=1]")).attr("value")+'}}');
           }
       })
   ]);        

   return button.render();   // return button as jquery object 
}

var AddInstituteDetailsDropDown = function (context) {
	var con = context;
   var ui = $.summernote.ui;
   var list = "<li value='InstituteName'>Institute name</li>" +
   			  "<li value='InstituteAddress'>Institute Address</li>" +
   			  "<li value='InstitutePhone'>Institute Phone</li>" +
   			  "<li value='InstituteEmail'>Institute Email</li>" ;

   var button = ui.buttonGroup([
       ui.button({
           className: 'dropdown-toggle',
           contents: ' <span class="selectedBorder">Select Institute Placeholders</span> <span class="caret"></span>',
           data: {
               toggle: 'dropdown'
           },
           click: function () {
        	   context.invoke('editor.saveRange');
        	   }
       }),
       ui.dropdown({
           className: 'drop-default summernote-list',
           contents: "<ul type='none' style='cursor: pointer;padding-left: 1%;font-size:small'>"+list+"</ul>",
           callback: function ($dropdown) {
               $dropdown.find('li').each(function () {
                  /* $(this).click(function () {            
                	   $(".summernote").summernote('editor.saveRange');
                	   $(".summernote").summernote('editor.restoreRange');
                	   $(".summernote").summernote('editor.focus');
                	   con.summernote('editor.insertText', '{{Po}}');
                   });*/
                   $(this).hover(function(){
                       $(this).css("background-color", "#eeeeee");
                       $(this).attr("data-val", "1");
                       }, function(){
                       $(this).css("background-color", "white");
                       $(this).attr("data-val", "0");
                   });
               });
           },
           click:function(){
        	   context.invoke('editor.restoreRange');
        	   context.invoke('editor.focus');
        	   context.invoke('editor.insertText', '{{'+$($(this).find("li[data-val=1]")).attr("value")+'}}');
           }
       })
   ]);        

   return button.render();   // return button as jquery object 
}


function getAllHeader(){
	var handler = {};
	handler.success = showHeaderInfo;
	handler.error = function(){};
	rest.get(allHeaderUri,handler);
}

function showHeaderInfo(headers){
	var headerArray = []
	if(headers.length > 0){
		var tempObj = {};
		tempObj.text = "Select Header"
		tempObj.id = "-1"
		headerArray.push(tempObj);
		for (var i = 0; i < headers.length; i++) {
		    tempObj = {};
			tempObj.text = headers[i].header_name
			tempObj.id = headers[i].header_id
			headerArray.push(tempObj);
		}
	}
	$("#headerSelect").select2({data:headerArray,placeholder:"Select Header"});
	/*$("#headers_name_table").dataTable({
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
	});*/
}

function showHeaders(){
	$("#headers_html").html("<i class=''></i>Loading...");
	var headerId= $(this).val();
	var handler = {};
	handler.success = function(e){
		$(".certificateHeader").remove();
		$('.summernote').summernote('insertNode', $("<div contenteditable='false' class='certificateHeader'>"+e+"</div>")[0]);
		/*$(".certificateHeader").html(e)*/
	};
	handler.error = function(){}
	rest.get(getHeaderUri+headerId,handler,false);
}

function save(){
	$(".error").html('');
	var data  = $('.summernote').summernote('code');
	var certificateName = $("#certificateName").val();
	var header = $("#headerSelect").val();
	var flag = true;
	if(data.trim() == ""){
		$("#certificatePanelError").html("Enter certificate details");
		flag = false;
	}
	if(header == null || header == "-1"){
		$("#headerSelectError").html("Select header");
		flag = false;
	}
	if(certificateName.trim() == ""){
		$("#certificateNameError").html("Enter certificate description");
		flag = false;
	}
	if(flag){
	var handlers = {};
	var uri = "rest/classownerservice/updateCerificateTemplate/"+certificateName+"/"+header+"/"+cert_id;
	handlers.success = function(e){
	if(e == true)	
	{
		$.notify({message: "Certificate saved"},{type: 'success'});
		$($(".edit[id="+cert_id+"]").closest("tr")).find(".cert_desc").html(certificateName);
		$(".backToList").click();
	}else{
		$.notify({message: "Certificate with same description already available.Please enter different description"},{type: 'danger'});
	}
	}
	handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});}
	data = "<div class='certificatePanel' style='background-color:"+globalBackgroundColor+";padding:2%'>" +
			"<div class='certificateMargin' style='border-color:"+globalBorderColor+";border-style:"+globalBorder+"'><div class='certificateBody' style='padding:2%'>"+data+"</div></div></div>" 
	rest.postString(uri,handlers,data);
	}
}

function deleteCertificate(that){
	var uri = "rest/classownerservice/deleteCerificateTemplate/"+that.prop("id");
	var handlers = {};
	handlers.success = function(e){
		var table = $("#certificateTable").DataTable();
		that.closest("tr").addClass('selected');
		table.row('.selected').remove().draw( false );
		$.notify({message: "Certificate deleted successfully."},{type: 'success'});
	}
	handlers.error = function(e){$.notify({message: "Error"},{type: 'danger'});}
	rest.get(uri,handlers);
}