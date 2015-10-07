<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script>
$(document).ready(function(){
	var examID;
	var subject;
	var division;
	var that;
	$('.hasDatepicker').datetimepicker({
		inline: true,
        sideBySide: true
	});
	
	$('.hasTimepicker').datetimepicker({
		pickDate:false
	});
	
	$(".disableExam").on("click",function(){
		examID=$(this).prop("id");
		subject=$("#subject").val();
		division=$("#division").val();
		that=$(this);	
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "disableExam",
			    	 examID:examID,
			    	 subject:subject,
			    	 division:division
			   		},
			   		success:function(data){
			   			 $(that).find("button").text("Enable");
			   			$(that).find("button").prop("class","btn btn-success");
			   			 $(that).prop("class","enableExam"); 
			   		},
			   		error:function(error){
			   		
			   		}
		});
		
	});
	
	$("#enableOK").on("click",function(){
		$(that).find("button").text("Enabling...");
		var starttime=$("#starttime").val();
		var endtime=$("#endtime").val();
		$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "publishExam",
			    	 examID:examID,
			    	 subject:subject,
			    	 division:division,
			    	 starttime:starttime,
			    	 endtime:endtime
			   		},
			   		success:function(data){
			   			$(that).find("button").text("Disable");
			   			$(that).find("button").prop("class","btn btn-warning");
			   			$(that).prop("class","disableExam");
			   		},
			   		error:function(error){
			   		
			   		}
		});
	});
	
$(".enableExam").on("click",function(){
	$("#enableexammodal").modal({
	    backdrop: 'static',
	    keyboard: false
	});
	//$("#enableexammodal").modal('toggle');
	examID=$(this).prop("id");
	subject=$("#subject").val();
	division=$("#division").val();
	that=$(this);
	//$(this).find("button").text("Enabling...");
});

$(".attemptExam").on("click",function(){
	$("#examID").val($(this).prop("id"));
	$("#actionform").submit();/* var formData = $("#actionform").serialize();
	console.log(formData);
	window.open("attemptExam?"+formData,"","width=500, height=500"); */
});

$(".page").on("click",function(){
	$("form#paginateform #currentPage").val($(this).text());
	var answers="";
	var count=1;
	while(count<11){
		var checkedanswers = [];
		var name="answers"+count;
			$.each($("input[name='"+name+"']:checked"), function(){            
				checkedanswers.push($(this).val());
    });
		if(count==1){
			
			answers=checkedanswers;
		}else{
			answers=answers+"/"+checkedanswers;
		}
		count++;
	}
	$("#answers").val(answers);
	$("#paginateform").submit();
});

$(".start").on("click",function(){
	$("form#paginateform #currentPage").val("1");
	var answers="";
	var count=1;
	while(count<11){
		var checkedanswers = [];
		var name="answers"+count;
			$.each($("input[name='"+name+"']:checked"), function(){            
				checkedanswers.push($(this).val());
    });
		if(count==1){
			
			answers=checkedanswers;
		}else{
			answers=answers+"/"+checkedanswers;
		}
		count++;
	}
	$("#answers").val(answers);
	$("#paginateform").submit();
});

$(".end").on("click",function(){
	$("form#paginateform #currentPage").val($("#totalPages").val());
	var answers="";
	var count=1;
	while(count<11){
		var checkedanswers = [];
		var name="answers"+count;
			$.each($("input[name='"+name+"']:checked"), function(){            
				checkedanswers.push($(this).val());
    });
		if(count==1){
			
			answers=checkedanswers;
		}else{
			answers=answers+"/"+checkedanswers;
		}
		count++;
	}
	$("#answers").val(answers);
	$("#paginateform").submit();
});

$(".examSubmit").on("click",function(){
	$("form#paginateform #currentPage").val($("#totalPages").val());
	var answers="";
	var count=1;
	while(count<11){
		var checkedanswers = [];
		var name="answers"+count;
			$.each($("input[name='"+name+"']:checked"), function(){            
				checkedanswers.push($(this).val());
    });
		if(count==1){
			
			answers=checkedanswers;
		}else{
			answers=answers+"/"+checkedanswers;
		}
		count++;
	}
	$("#answers").val(answers);
	$("form#paginateform #actionname").val("examSubmit");
	$("#paginateform").submit();
});

});
</script>
</head>
<body>
<c:if test="${actionname eq 'initiateexam' }">
<div class="container">
<div class="container bs-callout bs-callout-danger white-back" style="margin-bottom: 5px;">
	<div class="row">
		<div class="alert alert-danger" style="padding-bottom: 10px;display:none">
			 
		</div>
	</div>
<h3><font face="cursive">Welcome To Exam Corner </font></h3>
<div class="row">
<div class="col-md-12" align="center">Exam Details</div>
</div>
<div class="row">
		<div class="col-md-6">Exam Name </div> 
		<div class="col-md-3"><c:out value="${initiateExam.exam_name }"></c:out></div>
</div>
<div class="row">
		<div class="col-md-6">Total Marks</div> <div class="col-md-3"><c:out value="${initiateExam.total_marks }"></c:out></div>
</div>
<div class="row">
		<div class="col-md-6">Time</div> <div class="col-md-3"> 1 Hr</div>
</div>
<div class="row">
		<div class="col-md-12" align="center">
  <form action="attemptExam" id="paginateform">
  <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
  <input type="hidden" name="examID" id="examID" value='<c:out value="${examID}"></c:out>'>
  <input type="hidden" name="institute" id="institute" value='<c:out value="${institute}"></c:out>'>
  <input type="hidden" name="actionname" value="examattempted">
  <input type="submit" value="Attempt Exam">
  </form>
  </div>
  </div>
  </div>
  
</div>
</c:if>
 <c:if test="${(questionDataList != null)}">
	<div class="container">
  <h2><font face="cursive">Exam</font> </h2>            
  <table class="table table-striped">
    <thead>
      <tr>
        <th>Sr No.</th>
        <th>Question</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach items="${questionDataList}" var="item" varStatus="counter">
    <tr>
    	<c:if test="${currentPage eq 1}">
        <td class="col-md-1"><c:out value="${counter.count}"></c:out></td>
        </c:if>
        <c:if test="${currentPage gt 1 }">
        <td class="col-md-1"><c:out value="${counter.count + ((currentPage-1)*10)}"></c:out></td>
        </c:if>
        <td class="col-md-10">
			<c:out value="${item.question}"></c:out>
			<br/>
			<c:forEach items="${item.questionImage}" var="image">
				<img src='<c:out value="${image }"></c:out>' width="200px" height="200px" tyle="padding:5px;"/>
			</c:forEach>
		</td>
      </tr>
      <tr>
      <td class="col-md-1"></td>
      <td class="col-md-10">
     <c:if test="${item.options != null}"> 
    	<table>
    	<c:forEach items="${item.answers}" var="ans" varStatus="anscounter">
    	<c:set var="optionlistsize" value='${anscounter.count}' scope="page"></c:set>
		</c:forEach>
    		<c:forEach items="${item.options}" var="option" varStatus="innercounter">
    		<tr>
    		<c:set var="optionstatus" value="N" scope="page"></c:set>
    		<c:if test="${currentpageanswers !=null }">
    			<c:forEach items="${currentpageanswers[(counter.count-1)]}" var="anslist">
    				<c:if test="${anslist eq (innercounter.count-1)}">
    				<c:set var="optionstatus" value="Y" scope="page"></c:set>
    				</c:if>
    			</c:forEach>
    		</c:if>
    		<c:choose>
    		<c:when test="${optionlistsize eq '1' }">
    		
    		<c:choose>
    		<c:when test="${optionstatus eq 'Y' }">
    		<td>
    			<input type="radio" value='<c:out value="${innercounter.count-1}"></c:out>' id='answers<c:out value="${counter.count}"></c:out>' name='answers<c:out value="${counter.count}"></c:out>'' checked="checked"><c:out value="${option}"></c:out>
    			<br/>
						<c:choose>
							<c:when test="${innercounter.index eq 0}">
								<c:set var="startCountIndex" value="0"></c:set>
							</c:when>
							<c:otherwise>
								<c:set var="startCountIndex" value="${requestScope.optionImageEndCount[innercounter.index-1]}"></c:set>
							</c:otherwise>
						</c:choose>	
								<c:set var="endCountIndex" value="${requestScope.optionImageEndCount[innercounter.index-1]}"></c:set>
						
						<c:forEach items="${item.answerImage}" var="optionImage" begin="${startCountIndex}" end="${endCountIndex}">
							<img src='<c:out value="${optionImage}"></c:out>' width="200px" height="200px" tyle="padding:5px;"/>
						</c:forEach>			
    		</td>
    		</c:when>
    			<c:otherwise>
    			<td>
    				<input type="radio" value='<c:out value="${innercounter.count-1}"></c:out>' id='answers<c:out value="${counter.count}"></c:out>' name='answers<c:out value="${counter.count}"></c:out>''><c:out value="${option}"></c:out>
						<br/>
						<c:choose>
							<c:when test="${innercounter.index eq 0}">
								<c:set var="startCountIndex" value="0"></c:set>
							</c:when>
							<c:otherwise>
								<c:set var="startCountIndex" value="${requestScope.optionImageEndCount[innercounter.index-1]}"></c:set>
							</c:otherwise>
						</c:choose>	
								<c:set var="endCountIndex" value="${requestScope.optionImageEndCount[innercounter.index-1]}"></c:set>
						
						<c:forEach items="${item.answerImage}" var="optionImage" begin="${startCountIndex}" end="${endCountIndex}">
							<img src='<c:out value="${optionImage}"></c:out>' width="200px" height="200px" tyle="padding:5px;"/>
						</c:forEach>	
						
    			</td>
    			</c:otherwise>
    		</c:choose>
    		</c:when>
    		<c:otherwise>
    		<c:choose>
    		<c:when test="${optionstatus eq 'Y' }">
    		<td><input type="checkbox" value='<c:out value="${innercounter.count-1}"></c:out>' id='answers<c:out value="${counter.count}"></c:out>' name='answers<c:out value="${counter.count}"></c:out>'' checked="checked"><c:out value="${option}"></c:out></td>
    		</c:when>
    			<c:otherwise>
    			<td><input type="checkbox" value='<c:out value="${innercounter.count-1}"></c:out>' id='answers<c:out value="${counter.count}"></c:out>' name='answers<c:out value="${counter.count}"></c:out>''><c:out value="${option}"></c:out></td>
    			</c:otherwise>
    		</c:choose>
    		</c:otherwise>
    		</c:choose>
    		</tr>
    	</c:forEach>
    	</table>
      </c:if>
      </td>
      </tr>
      </c:forEach>
      <tr>
      <td><button class="examSubmit" value="Exam Submit">Exam Submit</button></td> 
      </tr>
    </tbody>
  </table>
  
  <form action="" id="actionform" method="post" target="blank">
  <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
  <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
  <input type="hidden" name="examID" id="examID" value='<c:out value="${examID}"></c:out>'>
  <input type="hidden" name="actionname" id="actionname">
  <input type="hidden" name="institute" id="institute" value='<c:out value="${institute}"></c:out>'>
  </form>
  <form action="attemptExam" id="paginateform">
  <input type="hidden" name="subject" value="<c:out value="${subject}" ></c:out>">
  <input type="hidden" name="batch" value="<c:out value="${batch}" ></c:out>">
  <input type="hidden" name="division" value="<c:out value="${division}" ></c:out>">
  <input type="hidden" name="currentPage" id="currentPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="totalPages" id="totalPages" value='<c:out value="${totalPages}"></c:out>'>
  <input type="hidden" name="searchedMarks" value='<c:out value="${searchedMarks}"></c:out>'>
  <input type="hidden" name="searchedExam" value='<c:out value="${searchedExam}"></c:out>'>
  <input type="hidden" name="searchedRep" value='<c:out value="${searchedRep}"></c:out>'>
  <input type="hidden" name="examID" id="examID" value='<c:out value="${examID}"></c:out>'>
  <input type="hidden" name="answers" id="answers">
  <input type="hidden" name="lastPage" id="lastPage" value='<c:out value="${currentPage}"></c:out>'>
  <input type="hidden" name="actionname" id="actionname">
  <input type="hidden" name="institute" id="institute" value='<c:out value="${institute}"></c:out>'>
  <ul class="pagination">
  <li><a class="start" >&laquo;</a></li>
  <c:forEach var="item" begin="1" end="${totalPages}">
  <c:if test="${item eq currentPage}">
  <li class="active"><a href="#" class="page"><c:out value="${item}"></c:out></a></li>
  </c:if>
  <c:if test="${item ne currentPage}">
  <li><a href="#" class="page"><c:out value="${item}"></c:out></a></li>
  </c:if>
  </c:forEach>
  <li><a href="#" class="end">&raquo;</a></li>
</ul>
</form>
</div>
	</c:if>	
</body>
</html>