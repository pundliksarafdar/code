var FIELD_GROUP = "#fieldGroup";
var FORM_FIELD = "#formField";
var REMOVE_ITEM = ".removeItem";
var SAVE = "#saveField";

var formFieldUrl = "rest/ClassownerSettings/formField";
$(document).ready(function(){
	loadFormValue();
	$("#addField").on("click",addField);
	$(FORM_FIELD).on("click",REMOVE_ITEM,removeItem);
	$(SAVE).on("click",saveFormField);
});

function addField(fieldValue){
	$(FORM_FIELD).find(".alert").remove();
	var fieldItem = $(FIELD_GROUP).html();
	var f = $(fieldItem); 
	if(typeof fieldValue != 'object'){
		f.find("input").val(fieldValue);
	}
	$(FORM_FIELD).append(f);	
}

function removeItem(){
	$(this).closest(".input-group").remove();
}

function saveFormField(){
	var newFormField = {};
	$(FORM_FIELD).find("input").each(function(key,value){
		var regEx = new RegExp(" ","g");
		newFormField[$(value).val().replace(regEx,"")] = $(value).val(); 
	});
	
	var handler = {};
	handler.success = function(e){$.notify({message: 'Additional form field saved successfully'},{type: 'success'});}
	handler.error = function(){}
	rest.post(formFieldUrl,handler,JSON.stringify(newFormField));
}

function loadFormValue(){
	var handler = {};
	handler.success = loadFormValueSuccess;
	handler.error = loadFormValueError;
	rest.get(formFieldUrl,handler);
}

function loadFormValueSuccess(e){
	
	var formField = JSON.parse(e.formField);
	if(formField){
		$.each(formField,function(key,val){
			addField(val);
		});
	}
}

function loadFormValueError(e){
	console.log("err",e);
}