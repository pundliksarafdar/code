<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="com.classapp.db.subject.Subjects"%>
<%@page import="com.datalayer.batch.BatchDataClass"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<script type="text/javascript" src="js/AddSubject.js"></script>
<script>
	$(document).ready(function(){
		
		$("#addteacher").click(function() 
				{  	alert("Yo Yo1");
		             $.ajax({
		                url: 'classOwnerServlet',
		                type: 'post',
		                data: {
					    	 methodToCall: "fetchsubjects",
					   		},
		                success: function(){
		                	$('#addTeacherModal').modal({show:true});
		                }, error: function(){
		                    alert('ajax failed');
		                }
		            }); 
		            
		        });
		
		$('.batchName').tooltip({'placement':'right','html':'true'}).on('click',function(){
			$(this).tooltip('hide');
		});
		
		$('.addsubject2batch').tooltip();
/*		$('[data-toggle="popover"]').popover({container: '.popoverContainer'+$(this).attr('popovername')});
*/	
/*
		$('[data-toggle="popover"]').on('click',function(){
			$(this).popover('show');
		});
		*/
		
		$('.addsubject2batch').popover({'placement':'bottom','content':$('#allSubject').html(),'html':true});

	});
	
	
	
</script>
<div>
	<span class="btn btn-info pull-right addsubject2batch" data-toggle="popover" title="Select menu to add, remove, rename subjects"><i class="glyphicon glyphicon-plus"></i>&nbsp;Subjects</span>
</div>
<div class="btn-group btn-group-sm">
 
  <button type="button" class="btn btn-info" data-target="#addSubjectModal" data-toggle="modal">Add Subject</button>
  <button data-target="#addclassModal" type="button" class="btn btn-info" data-toggle="modal">Add Class</button>
 </div>
<br><br>
