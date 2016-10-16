var filterUrl = "rest/syllabus/getFilterResult";
var viewFilterUrl = "rest/syllabus/view/";
var changeStatus = "/rest/syllabus/changeStatus/";

var SYLLABUS_SEARCH_MONTH = "#syllabusSearchTime";
var SYLLABUS_TABLE = "#syllabusTable";

var TEMPL = '<li class="list-group-item"><span><input type="checkbox" value=""></span><label></label></li>';

var syllabusData;
$(document).ready(function(){
	var pSyllabus = new PlannedSyllabus();
	pSyllabus.getPlannedSyllabusFilter();
	
	$(SYLLABUS_SEARCH_MONTH).datetimepicker({
		pickTime: false,
		format:"YYYY-MM-DD",
		useCurrent:true,
	}).on("dp.change",pSyllabus.onDateChange);
	$(SYLLABUS_SEARCH_MONTH).data("DateTimePicker").setDate(new Date());
	$("ul[data-filter]").on("change","input",pSyllabus.onFilterChange)
});

function PlannedSyllabus(){
	var filterHandler = {};
	filterHandler.success = populateFilter;
	filterHandler.error = function(){}
	
	var onFilterChange = function(){
		var queryParam = "";
		$("ul[data-filter]").find("input:checked").each(function(){
			queryParam = queryParam +$(this).attr("name")+"="+$(this).val()+"&";
		});
		if(queryParam.length){queryParam = queryParam.substring(0,queryParam.length-1)}
		getFilteredResult(queryParam);
	}
	
	var getPlannedSyllabusFilter = function(){
		rest.get(filterUrl,filterHandler);
	}
	
	var onDateChange = function(){
		getFilteredResult("");
	}
	
	function getFilteredResult(filter){
		var date = $(SYLLABUS_SEARCH_MONTH).find("input").val();
		var handler = {};
		handler.success = populateDataTable;
		handler.error = function(e){console.log(e)};
		rest.get(viewFilterUrl+date+"?"+filter,handler);
	}
	
	function formatFilterData(data){
		var divisionData = {}
		if(data.division){
			$.each(data.division,function(index,val){
				divisionData[val.id] = val.name;
			});
		}
		
		var subjectData = {}
		if(data.subject){
			$.each(data.subject,function(index,val){
				subjectData[val.id] = val.name;
			});
		}
		
		var teacherData = {}
		if(data.teacher){
			$.each(data.teacher,function(index,val){
				teacherData[val.id] = val.name;
			});
		}
		
		var allData = {};
		allData.division = divisionData;
		allData.subject = subjectData;
		allData.teacher = teacherData;
		return allData;
		
	}
	
	function populateDataTable(data){
		var oTable  = $(SYLLABUS_TABLE).DataTable( {
			bDestroy:true,
			data: data,
			lengthChange: false,
			bFilter: false,
			columns:[
			
			{
				title: "Date",data:"date",sDefault:'&mdash;',render:function(data){return moment(data).format("YYYY-MM-DD");}
			},
			{
				title: "Teacher",data:"teacherId",sDefault:'&mdash;',render:function(data){
					return syllabusData.teacher[data];
				}
			},
			{
				title: "Subject",data:"subjectId",sDefault:'&mdash;',render:function(data){
					return syllabusData.subject[data];
				}
			}/*,
			{
				title: "Batch",data:"batchId",sDefault:'&mdash;'
			}*/,
			{
				title: "Division",data:"classId",sDefault:'&mdash;',render:function(data){
					return syllabusData.division[data];
				}
			},
			{
				title: "Syllabus",data:"syllabus",sDefault:'&mdash;'
			},
			{
				title: "Status",data:"status",render:function(data){
					var optionComplete = $("<option/>",{value:"Complete",text:"Complete",selected:data!=null && data.toLowerCase()=="complete"});
					var optionInComplete = $("<option/>",{value:"Incomplete",text:"Incomplete",selected:data==null || data.toLowerCase()=="incomplete"});
					var optionEditable = $("<option/>",{value:"Editable",text:"Editable",selected:data!=null && data.toLowerCase()=="editable"});
					var select = $("<select/>").append(optionComplete).append(optionInComplete).append(optionEditable).attr("class","btn btn-default btn-xs");
					var div = $("<div/>").append(select);
					return div.html();
				}
			}
			],
			rowCallback:function(row, data, displayIndex, displayIndexFull){
				$(row).find("select").on("change",function(){onStatusChange(data,this)});
			}} );
	}
	
	function onStatusChange(data,that){
		var handler = {};
		handler.success = function(e){console.log(e)};
		handler.error = function(e){console.log(e)};
		var date = $(SYLLABUS_SEARCH_MONTH).find("input").val();
		data.status = $(that).val();
		rest.get(changeStatus+date+"?"+$.param(data),handler);
	}
	
	function populateFilter(data){
		syllabusData = formatFilterData(data);
		var divisions = data.division;
		if(divisions){
		$.each(divisions,function(index,val){
			var listItem = $(TEMPL);
			listItem.find("label").text(val.name).attr("for","division"+index);
			listItem.find("input").val(val.id).attr("id","division"+index).attr("name","division");
			$('[data-filter="division"]').append(listItem);
		});
		}
		
		var subjects = data.subject;
		if(subjects){
		$.each(subjects,function(index,val){
			var listItem = $(TEMPL);
			listItem.find("label").text(val.name).attr("for","subjects"+index);
			listItem.find("input").val(val.id).attr("id","subjects"+index).attr("name","subject");
			$('[data-filter="subject"]').append(listItem);
		});
		}
		
		var teachers = data.teacher;
		if(teachers){
		$.each(teachers,function(index,val){
			var listItem = $(TEMPL);
			listItem.find("label").text(val.name).attr("for","teacher"+index);
			listItem.find("input").val(val.id).attr("id","teacher"+index).attr("name","teacher");
			$('[data-filter="teacher"]').append(listItem);
		});
		}
	}
	
	this.getPlannedSyllabusFilter = getPlannedSyllabusFilter;
	this.onDateChange = onDateChange;
	this.onFilterChange = onFilterChange;
}