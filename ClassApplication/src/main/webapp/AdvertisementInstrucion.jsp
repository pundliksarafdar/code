<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home</title>
 <meta name="viewport" content="width=device-width, initial-scale=1.0">
 <!-- <link href="css/bootstrap.min.css" rel="stylesheet">
 <link href="css/bootstrap-responsive.css" rel="stylesheet"> -->
 <link href="css/common.css" rel="stylesheet">
 <title>Rayansh-Official Home Page</title>
    <!-- BOOTSTRAP CORE STYLE CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- FONTAWESOME STYLE CSS -->
    <link href="assets/css/font-awesome.min.css" rel="stylesheet" />
    <!-- CUSTOM STYLE CSS -->
    <link href="assets/css/style.css" rel="stylesheet" />    
    <!-- GOOGLE FONT -->
    <!-- <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css' /> -->
 <style>
 /*body { padding-bottom: 70px; }*/
 /*.navbar li {
	float: left;
	}
	*/
	body{
    font-family: cursive;
}

#toTop {
   
    position: fixed;
    bottom: 5px;
    right: 5px;
    width: 64px;
    height: 64px;
    background-image: url('images/up.png');
    background-repeat: no-repeat;
    opacity: 0.4;
    filter: alpha(opacity=40); /* For IE8 and earlier */
}
#toTop:hover {
    opacity: 0.8;
    filter: alpha(opacity=80); /* For IE8 and earlier */
}

 </style>
 <script src="js/jquery-1.10.2.min.js"></script>
 <script src="js/bootstrap.min.js"></script>
 <script src="js/moment.min.js"></script>
 <script src="js/bootstrap-datetimepicker.min.js"></script>
 <script>
 $(document).ready(function(){
	 $( "#datetimepicker" ).datetimepicker({
		  pickTime: false,
		  minDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear()),
		  format: 'DD/MM/YYYY'
	  }).data("DateTimePicker");
	 
	 $( "#bookdatetimepicker" ).datetimepicker({
		  pickTime: false,
		  minDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear()),
		  format: 'DD/MM/YYYY'
	  }).data("DateTimePicker");
	$("#header").load("home_head.html");
	$("#previewModalBodyFrame").load("Preview.jsp");
	$('.carousel').carousel({interval: 7000});
	$("#previewButton").click(function(){
		 var reader = new FileReader();
		var img=$("#previewfile").get(0).files[0];
		var advposition=$('input[name=advposition]:checked').val();
         reader.onload = function (e) {
   			if("side"==advposition){
        	 $("#previewModalBodyFrame").contents().find("#adv1").attr("src",e.target.result);
   			}else{
   				$("#previewModalBodyFrame").contents().find("#adv2").attr("src",e.target.result);
   			}
        	 $("#previewModal").modal("toggle");
         };

         reader.readAsDataURL(img);
		
	});
	
	$("#availabilityButton").click(function(){
		var advdate= $("#advdate").val();
		 $.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "getAdvertiseAvailability",
			    	 advdate:advdate
			   		},
			   type:"POST",
			   success:function(data){
				   var resultJson = JSON.parse(data);
				   $("#availability").html("no");
				   				   		},
			   	error:function(){
			   		modal.launchAlert("Error","Error");
			   	}	
			}); 
	});
 });
 
 function resizeIframe(obj) {
    obj.style.height = obj.contentWindow.document.body.scrollHeight/2 + 'px';
  }
 </script>
</head>
<body>

	<div id="outerDiv" align ="center">
		<div id = "innerDiv" >
			<div id="header">

			</div>
			<div id="body" style="background-color: white;margin-top: 50px">
			<!-- 	<a href="#top" id="toTop"></a>
				<section id="home" class="text-center"> -->
				<ul type="1">
    			<li> You can upload your advertisement on our website.</li>
    			<li> There are two section where you can upload.<br>      
					<img alt="Example" src="/images/preview.png" style="height: 300px">	
           		</li>
           		<li>See your advertisement preview.<br>
           		Select image for preview<input type="file" id="previewfile"><br>
           		<input type="radio" name="advposition" value="side" checked="checked">Side Vertical Advertisement
           		<input type="radio" name="advposition" value="bottom">Bottom Horizontal Advertisement
           		<button id="previewButton" class="btn btn-success" >Preview</button></li>
           		<div class="modal fade" id="previewModal">
    	<div class="modal-dialog" style="width:100%;padding: 0px;">
    		<div class="modal-content" style="background-color: rgba(0,0,0,0)">
 			<div class="modal-header" style="background-color: rgba(0,0,0,0.9)">
            <button type="button"  data-dismiss="modal" aria-hidden="true" style="background-color: white;color: black;float: right;border-radius:20px">×</button>
          	<h4 class="modal-title" id="myModalLabel" style="color: white;font-family: cursive;">Preview</h4>
        </div>
        	<div class="modal-body container" id="previewModalBody">
          	<iframe id="previewModalBodyFrame" style="width:100%;" src="http://localhost:8080/Preview.jsp" frameborder="0" scrolling="no" onload='javascript:resizeIframe(this);'></iframe>
       		 </div>
      	<div class="modal-footer">
	        <button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
      	</div>
    </div>
</div>
</div>
<li>For booking advertisement first check availablity below.
	<div style="border: 1;border-radius:5px">
	<div id="datetimepicker" class="input-group" style="width :190px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="advdate" placeholder="Select Date" readonly/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
				<span id="availability" style="color: red"></span>
				<button class="btn btn-success" id="availabilityButton">Check Availability</button>
	</div>
</li>
<li>If bookings are available fill below form.After booking you will get one booking ID.Within next 30 minutes you will have to pay 1000rs and send us email and mention your booking ID. For identification while paying in remark box mention your booking ID.For mor detail Contact 9766120685</li>
           	
           	<div style="border: 1;border-radius:5px">
           		<input type="text" id="firstname" name="firstname" placeholder="First Name">
           		<input type="text" id="lastname" name="lastname" placeholder="Last Name">
           		<input type="text" id="email" name="email" placeholder="Email">
           		<div id="bookdatetimepicker" class="input-group" style="width :190px;">
					<input class="form-control" data-format="MM/dd/yyyy HH:mm:ss PP"
						type="text" id="bookadvdate" placeholder="Booking Date" readonly/> <span class="input-group-addon add-on"> <i
						class="glyphicon glyphicon-calendar glyphicon-time"></i>
					</span>
				</div>
				<button class="btn btn-success">Book</button>
           	</div>
           		</ul>
       		<!-- 	</section> -->
       </div>	
			</div>
			<div id="footer">
 			<section id="footer-sec" >
             
            <div class="container">
           <div class="row  pad-bottom" >
               <div class="col-md-4">
                    <h4> <strong>SOCIAL LINKS</strong> </h4>
                   <p>
                     <a href="#"><i class="fa fa-facebook-square fa-3x"  ></i></a>  
                        <a href="#"><i class="fa fa-twitter-square fa-3x"  ></i></a>  
                        <a href="#"><i class="fa fa-linkedin-square fa-3x"  ></i></a>  
                       <a href="#"><i class="fa fa-google-plus-square fa-3x"  ></i></a>  
                   </p>
                </div>
              </div>
            </div>
    </section> 
			</div>
		</div>

</body>
</html>
