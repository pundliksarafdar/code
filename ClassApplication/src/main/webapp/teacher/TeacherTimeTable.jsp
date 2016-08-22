<%@page import="com.classapp.db.register.RegisterBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="js/underscore-min.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
	<link rel="stylesheet" href="css/calendar.css">
<script type="text/javascript">
_.templateSettings = {
		interpolate: /\<\@\=(.+?)\@\>/gim,
		evaluate: /\<\@(.+?)\@\>/gim,
		escape: /\<\@\-(.+?)\@\>/gim
	};
var view = "";
$(document).ready(function(){
	$('.btn-group button[data-calendar-view]').click(function() {
		 view = $(this).data('calendar-view');
		calendar.view(view);
		var classid=$("#teacherTimeTableClassnameDropDown").val();
		var date=$("#date").val();
		var handler = {};
		handler.success = function(e){
			setTimetable(e)
		}
		handler.error = function(e){
			$.notify({message: e.message},{type: 'danger'});
		}
			rest.get("rest/teacher/teacherSchedule/"+date+"/"+classid,handler);
		
	});
	var calendar = $('#calendar').calendar({
		
		events_source: function() { return [
		{
		"url": ""
		}]}
		});
	  $( "#datetimepicker" ).datetimepicker({
		  pickTime: false,
		  minViewMode:'months',
		  format: 'MM/YYYY'
	  }).data("DateTimePicker");
	  
	  $("#submit").click(function(){
			var classid=$("#teacherTimeTableClassnameDropDown").val();
			var date=$("#date").val();
			 view = $('.btn-group button[data-calendar-view].active').data('calendar-view');
			var handler = {};
			handler.success = function(e){
				setTimetable(e)
			}
			handler.error = function(e){
				$.notify({message: e.message},{type: 'danger'});
			}
		
				rest.get("rest/teacher/teacherSchedule/"+date+"/"+classid,handler);
				
		});
	  
	
})

function setTimetable(data){
	var dateTime =$("#date").val().split("/");
	/* if(dateTime.trim().length == 0){
		dateTime = "now";
	}else if(dateTime.trim().length<=7){ */
		dateTime = dateTime[1]+"-"+dateTime[0]+"-01";
	/* } */
	timtableData = data;
	var options = {
		events_source: data,
		tmpl_path: 'teacherTmpls/',
		tmpl_cache: false,
		day: dateTime,
		view:view,
		modal:"#events-modal",
		onAfterEventsLoad: function(events) {
			if(!events) {
				return;
			}
			var list = $('.events-list');
			list.html('');

			$.each(events, function(key, val) {
				$(document.createElement('li'))
					.html('<a href="' + val.url + '">' + val.title + '</a>')
					.appendTo(list);
			});
		},
		onAfterViewLoad: function(view) {
			$('.page-header h3').text(this.getTitle());
			$('.btn-group button').removeClass('active');
			$('button[data-calendar-view="' + view + '"]').addClass('active');
		},
		classes: {
			months: {
				general: 'label'
			}
		}
	};

	calendar = $('#calendar').calendar(options);
	
	}
</script>
</head>
<body>
<div class="well" style="margin-bottom: 5px;">
			<div align="center" style="font-size: larger;"><u>Time Table</u></div>
<form role="form" class="form-inline">
<div class="container">
<div align="left" class="container">
<div class="col-md-3">
<%List<RegisterBean> list=(List<RegisterBean>)request.getAttribute("Classes"); %>
<select id="teacherTimeTableClassnameDropDown"  class='form-control'>
<option value="-1">Select Institute</option>
<%
int counter=0;
if(list!=null){
while(list.size()>counter){ %>
<option value="<%=list.get(counter).getRegId()%>"><%=list.get(counter).getClassName() %></option>
<%counter++;
} }%>
</select>
</div>

<div class="col-md-3">
<div id="datetimepicker" class="input-group" style="width :190px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="date" placeholder="Select Date" readonly/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
</div>
<div class="col-md-3">
   <button type="button" class="btn btn-danger" 
      data-loading-text="Loading..."  id="submit">Submit
   </button>
</div>
</div>
</div>
</form>
</div>
<div id="calendarContainer">
<div class="btn-group">
				<button class="btn btn-warning active" data-calendar-view="month">Month</button>
				<button class="btn btn-warning active" data-calendar-view="week">Week</button>
				<button class="btn btn-warning" data-calendar-view="day">Day</button>
</div>
</div>
<div id="calendar"></div>
<div class="container">
<table id="scheduletable" border="1" style="display:none;background-color: white;" class="table table-bordered">
<thead>
<tr style="background-color: rgb(0, 148, 255);">
<th>Class</th>
<th>Batch</th>
<th>Subject</th>
<th>Start Time</th>
<th>End Time </th>
 </tr>
</thead>
</table>
</div>
<div class="modal fade" id="lectureupdatemodal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" 
               aria-hidden="true">×
            </button>
            <h4 class="modal-title" id="myModalLabel">
               Lecture
            </h4>
         </div>
         <div class="modal-body">
           Lectures Not Available..
         </div>
         </div>
   </div>
</div>
</body>
</html>