<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html>
<style type="text/css">
	 #headersEle span{display:inline}
	 
	 #headerothernav
	 {
	 	height: 100px;
	 	width : 50%;	 
	 }		 
</style>
<body>
	<!-- Pundlik about us modal -->
<div class="modal fade" id="aboutUsModal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">x</button>
				<h4 class="modal-title" id="myModalLabel">About Us</h4>
			</div>
			<div class="modal-body">
								
					<ul id="myTab" class="nav nav-tabs  nav-justified" role="tablist">
						<li role="presentation" class="active"><a href="#home"
							id="home-tab" role="tab" data-toggle="tab" aria-controls="home"
							aria-expanded="true">CoreX</a></li>
						<li role="presentation" class=""><a href="#teachers"
							role="tab" id="profile-tab" data-toggle="tab"
							aria-controls="profile" aria-expanded="false">Teacher</a></li>
						<li role="presentation" class=""><a href="#student"
							role="tab" id="profile-tab" data-toggle="tab"
							aria-controls="profile" aria-expanded="false">Students</a></li>
						
					</ul>
					<div id="myTabContent" class="tab-content">
						<div role="tabpanel" class="tab-pane fade active in" id="home"
							aria-labelledby="home-tab">
							<br>
							Corex...<br> Right to success<br>

							<p>Our mission is to provide a complete interactive and
								innovative package to play around notes, homework, exam for the
								first time on the planet. We at Corex provide best platform to
								study online with ur smartphones or PC. Corex will change the
								future of learning with the technology & will bring about a
								complete transformation to tuition system. Our goal is to
								provide various program and services to students, teachers,
								administrators and parents under one roof. Corex help teachers
								and students to communicate more conveniently. Our purpose is to
								create more opportunities to learn to improve academic
								performance of the students.</p>
							
						</div>
						<div role="tabpanel" class="tab-pane fade" id="teachers"
							aria-labelledby="profile-tab">
							<br>
							<p>For teachers/ class owners: Corex help you to reach
								students all over the world. You can create course, set
								syllabus, share or upload notes, set a practice test in a very
								short time with ease. Teachers can upload notes in document
								format, presentations, audio notes, videos, animation for better
								understanding of concepts. Set timetable of complete course or
								year, send alerts/ notifications / change in schedule with
								calender. Corex also enables teachers to instantly assess and
								evaluate the students performance.</p>

							Features:
							<ul>
								<li>Manage access of the all students across batches and
									tuituions</li>
								<li>Manage access to exams notes and time table for each
									batch</li>
								<li>Upload notes in PDF, doc or ppt format</li>
								<li>Upload Exams in Excel format</li>
								<li>View the exams, and slive it.</li>
								<li>View the students result and send message to there
									registered device</li>
								<li>Manage timetable for batch</li>
								<li>Display your special events to students across the
									batches</li>
								<li>Dispaly ads to targeted students depend on there ages,
									education, region and gender</li>
							</ul>

						</div>
						<div role="tabpanel" class="tab-pane fade" id="student"
							aria-labelledby="dropdown1-tab">
							<br>
							<p>Learn with fun...for students</p>
							<br>
							<p>With Corex students can find unlimited options of classes
								with top names in market. Corex is one of the easy way to get
								expert created notes. Students can attend any class of their
								choise from any place. Each class with there unique features,
								study material, notes and practice tests to help students crack
								or excel in exam.</p>
							<br>
							<p>Corex will help you to create your timetable managing your
								school, college or ofice. Students can add reminder or task to
								their timetable.Corex will keep track of your progress and help
								you to work on weak points. You can share you results with
								social connectivity.</p>
							<br> Features:
							<ul>
								<li>Register to class with easy steps</li>
								<li>One time log in</li>
								<li>Manage your class items like notes exam and result</li>
								<li>Able to add own notes over provided one</li>
								<li>Apply for exams</li>
								<li>Reappear for exam and get the answers for the solved
									one</li>
								<li>Students can sync timetable across multiple devices</li>
								<li>Access your tuition over desktop or mobile</li>
								<li>View notes offline</li>
								<li>View results in graphical format</li>
								<li>Apply for special event through mobile app</li>
							</ul>
						</div>
						
					</div>
				
			</div>
		</div>
	</div>
</div>

<!-- Pundlik Contact us modal -->
<div class="modal fade" id="contactUsModal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">x</button>
				<h4 class="modal-title" id="myModalLabel">Contact Us</h4>
			</div>
			<div class="modal-body">
				Contact Us
			</div>
		</div>
	</div>
</div>

	

	<div id="headersEle" align="left" style="float: left; width: 50%;">
		<span><a href="login"><img src="images/cxlogo.jpg" alt="cxlogo" style="height: 100px;" class="img-rounded"/></a></span>
		<!-- <span><b style="font-size: 40px;">CoreX - <tiles:insertAttribute name="title" ignore="true" /></b></span>-->
	</div>
	<div id = "headerothernav" style="float:right;">
		<div align="right" style = "height:50px; float:right; width:100%;">
		<ul class="nav nav-pills pull-right" style ="color: aqua;">
		  <li><a href="login">Home</a></li>
		  <li><a href="#" data-toggle="modal" data-target="#aboutUsModal">About Us</a></li>
		  <li><a href="#" data-toggle="modal" data-target="#contactUsModal">Contact Us</a></li>
		</ul>
		</div>
		<div align="right" style = "height:50px; float:right; width:100%;">
		<span class="label"></span>
		</div>
	</div>
</body>
</html>