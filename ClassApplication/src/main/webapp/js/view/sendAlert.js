(function(){
	var getTeacherListUrl = "/rest/classownerservice/teacher";
	var getBatchUrl = "/rest/classownerservice/getBatches/";
	var postnotificationUrl = "rest/notification/send";
	var getExamListUrl = "rest/classownerservice/getExamList/";
	
	var DIVISION_SELECT = "#sendAlert #divisionSelect";
	var BATCH_SELECT = "#sendAlert #batchSelect";
	var EXAM_SELECT = "#sendAlert #examSelect";
	var DATEPICKER = "#datepicker";
	var DAY_DATE_PICKER = DATEPICKER + "Day";
	var WEEK_DATE_PICKER = DATEPICKER + "Week";
	var MONTH_DATE_PICKER = DATEPICKER + "Month";
	
	
	$(document).ready(function(){
		$("body").on("change",DIVISION_SELECT,function(e){
			var handler = {};
			handler.success = function(data){
				loadBatchOfDiv(data);
			}
			handler.error = function(){
				
			}
			if($(this).val()!=-1){
				rest.get(getBatchUrl+$(this).val(),handler,true);
			}else{
				
			}
		}).on("change",BATCH_SELECT,loadExamList);
		$(DAY_DATE_PICKER).datetimepicker({pickTime: false,format:"YYYY-MM-DD"});
		$(WEEK_DATE_PICKER).datetimepicker({pickTime:false,format:"YYYY-MM-DD"});
		$(MONTH_DATE_PICKER).datetimepicker({pickTime:false,format:"YYYY-MM",minViewMode:'months'});
	});
	
	function loadBatchOfDiv(data){
		var option = "<option value='-1'>Select batch</option>";
		$.each(data,function(key,val){
			var text = val.batch_name;
			var id = val.batch_id;
			option += "<option value='"+id+"'>"+text+"</option>";
		});
		$(BATCH_SELECT).empty();
		$(BATCH_SELECT).append(option);
	}
	
	function loadExamList(){
		var handler = {};
		handler.success = showExamList;
		handler.error = function(){};
		rest.post(getExamListUrl+$(DIVISION_SELECT).val()+"/"+$(BATCH_SELECT).val(),handler)
	}
	
	
	function showExamList(data){
		console.log(data);
		var examList = "";
		$.each(data,function(key,val){
			examList += "<option value="+val.exam_id+">"+val.exam_name+"</option>"
		});
		$(EXAM_SELECT).html(examList);
		$(EXAM_SELECT).select2({placeholder: "Select exam list"});
	}
})();