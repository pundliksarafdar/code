<div>
<script>
$(document).ready(function(){
	var timer = new RenderTimer(0,30);
	timer.startTimer();
});

function RenderTimer(completedTime,totalTime){
	var ctx = document.getElementById("myChart").getContext("2d");
	var option = {
		segmentShowStroke : false,
		animationSteps :1,
		showTooltips:false
	};		
	
	this.startTimer = function(){
		var timeInterval = setInterval(function(){
			var totalSec = totalTime - completedTime;
			var hours = parseInt( totalSec / 3600 ) % 24;
			var minutes = parseInt( totalSec / 60 ) % 60;
			var seconds = parseInt(totalSec % 60);
			var result = (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds  < 10 ? "0" + seconds : seconds);
			$("#timerRemainingTime").html(result);
			
			var complete = {value: completedTime,color:"white"};
			var incomplete = {value: (totalTime-completedTime),color:"red"}				
			var data = [complete,incomplete];
			var completed = data[0];
			var incompleted = data[1];
			completedTime +=1;
			completed.value = completedTime;
			incompleted.value = totalTime-completedTime;
			var completedPer = completedTime/totalTime*100;
			console.log(completedPer);
			if(completedPer <50){
				incompleted.color = "white";
			$("#timerRemainingTime").css({"color":"white"});
			}else if(completedPer <90){
				incompleted.color = "yellow";
				$("#timerRemainingTime").css({"color":"yellow"});
			}else if(completedPer <100.0){
				incompleted.color = "red";
			$("#timerRemainingTime").css({"color":"red"});
			}else{
				clearInterval(timeInterval);
				$(window).trigger("timeroverflow");
			}			
			var myDoughnutChart = new Chart(ctx).Doughnut(data,option);
		},1000);
	}
}	

</script>
<nav class="navbar navbar-apple-custom" role="navigation">
  <!-- Brand and toggle get grouped for better mobile display -->
  <div class="navbar-header">
  	  <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
	    <ul class="nav navbar-nav">
	    <li>
	    	<a href="#" >
				<canvas id="myChart" width="20" height="20"></canvas>
				<span id="timerRemainingTime"></span>
			</a>
	    </li>
  		</ul>
  	</div>
    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" style="border-color: white;border-width: 1px;">
      <i class="glyphicon glyphicon-th-large"></i>
    </button>
    <a class="navbar-brand">
	<img src="images/cxlogo.jpg" alt="cxlogo" style="height: 20px;" class="img-rounded"/>
	CoreX</a>
  </div>
</nav>
</div>


