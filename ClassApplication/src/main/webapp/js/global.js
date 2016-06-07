$(document).ready(function(){
	//***//
	$("#mathsExpressionModal").find("#mathInput").on("input",function(){
		PreviewModal.Update();
	});
	

	$(".limit").on("click",function(){
		if($(this).hasClass('hasLimit')){
			$("#limitData").hide();
			$(this).removeClass('hasLimit')
		}else{
			$("#limitData").show();
			$(this).addClass('hasLimit')
		}
	});
	
	$("#mathsExpressionModal").find(".mathControl").on("click",function(){
		var mathExp = $(this).attr("data-val");
		appendMathInput(mathExp);
	});
	
	$("#mathsExpressionModal").find("#updateLimit").on("click",function(){
		var lowerLimit = '('+$("#limitData").find("#lowerLimit").val()+')';
		var upperLimit = '('+$("#limitData").find("#upperLimit").val()+')';
		
		if(lowerLimit && lowerLimit.trim().length>0){
			appendMathInput("_"+lowerLimit+"^"+upperLimit);
		}else{
			appendMathInput("^"+upperLimit);
		}
		
	});
	
	$("#mathsExpressionModal").find("#addRoot").on("click",function(){
		var degree = $("#rootData").find("#degree").val();
		var radicand = $("#rootData").find("#radicand").val();
		var rootTxt;
		if(!degree || degree.trim().length==0 || degree.trim()==2){
			rootTxt = "sqrt("+radicand+")";
		}else{
			rootTxt = "root("+degree+")("+radicand+")";
		}
		appendMathInput(rootTxt);
	});
	
	$("#mathsExpressionModal").find("#addRootShow").on("click",function(){
		$("#rootData").show();
	});
	
	
	
	$("#mathsExpressionModal").find("#mathLimitUpdate").on("click",function(){
		var xV = $("#addMathLimitData").find("#xValue").val();
		var yV = $("#addMathLimitData").find("#yValue").val();
		appendMathInput("lim_("+xV+"rarr"+yV+")");
	});
	
	$("#mathsExpressionModal").find("#addMathLimit").on("click",function(){
		$("#addMathLimitData").show();
	});
	
	
	function appendMathInput(data){
		$("#mathInput").val($("#mathInput").val()+data);
		PreviewModal.Update();
	}

	
	//
	//  Cache a callback to the CreatePreview action
	//
	
	$("#submitfeedback").click(function(){
		$("#namespan").html("");
		   $("#emailspan").html("");
		   $("#commentspan").html("");
		var regStringExpr = /^[a-zA-Z]+$/;
		var regPasswordExpr = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%]).{5,20}/;
		var name=$("#feedbackname").val();
		var email=$("#feedbackemail").val();
		var comment=$("#comment").val();
		var status="";
		if(name==""){
			$("#namespan").html("<p style='color:red;'>Please enter name<p>");
			status="1";
		}else if(!name.match(regStringExpr)){
			$("#namespan").html("<p style='color:red;'>Please enter name valid<p>");
			status="1";
		}
		if(email.match(regPasswordExpr)){
		$("#emailspan").html("<p style='color:red;'>Enter Valid email<p>");
		status="1";
		}else if(email==""){
			$("#emailspan").html("<p style='color:red;'>Enter email<p>");
			status="1";
		}
		if(comment==""){
			$("#commentspan").html("<p style='color:red;'>Enter comment<p>");
			status="1";
		}
		if(status==""){
			$.ajax({
			 
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "addfeedback",
			    	 name:name,
			    	 email:email,
			    	 comment:comment
			    	
			   		},
			   type:"POST",
			   success:function(data){
				   $("#namespan").html("");
				   $("#emailspan").html("");
				   $("#commentspan").html("");
				   $("#feedbacksuccess").show();
				   $("#form").hide();
			   },
			   error:function(){
				   
			   }
			   });
		}
	});
	
	$("#contactuslink").click(function(){
					$("#namespan").html("");
				   $("#emailspan").html("");
				   $("#commentspan").html("");
				   $("#feedbacksuccess").hide();
				   $("#feedbackname").val("");
					$("#feedbackemail").val("");
					$("#comment").val("");
				   $("#form").show();
	});
	
	$.fn.addExpresssion = function(isObjectiveOption){
		PreviewModal.callback = MathJax.Callback(["CreatePreview",PreviewModal]);
		PreviewModal.callback.autoReset = true;  // make sure it can run more than once
		PreviewModal.Init();
		
		var PreviewQuestionAndOption1 = PreviewQuestionAndOption;
		PreviewQuestionAndOption1.callback = MathJax.Callback(["CreatePreview",PreviewQuestionAndOption1]);
		PreviewQuestionAndOption1.callback.autoReset = true;  // make sure it can run more than once
		PreviewQuestionAndOption1.Init();
		
		var that = $(this);
		var option = {
				triggerOn:'focus',
				triggerOff:'blur',
				triggerTarget:'#subjectiveQuestion'
		}
		
		var preview = null;
		var buffer = null;
		if(isObjectiveOption){
			$(that).closest('.options').append("<div id='QueAndOpttextPreview'></div>");
			preview = $(that).closest('.options').find("#QueAndOpttextPreview");
			$(that).closest('.options').append("<div id='QueAndOptMathBuffer'></div>")
			buffer = $(that).closest('.options').find("#QueAndOptMathBuffer");
		}else{
			$(that).parent().append("<div id='QueAndOpttextPreview'></div>");
			preview = $(that).parent().find("#QueAndOpttextPreview");
			$(that).parent().append("<div id='QueAndOptMathBuffer'></div>")
			buffer = $(that).parent().find("#QueAndOptMathBuffer");
		}
		
		$(this).on(option.triggerOn,function(){
			$(".floatButton").show();
			$(".floatButton").on("click",function(){
				$("#mathsExpressionModal").modal('show');
				PreviewModal.Init();
				//PreviewQuestionAndOption1.Init();
				PreviewQuestionAndOption1.preview = preview[0];
				PreviewQuestionAndOption1.buffer = buffer[0];
				//PreviewQuestionAndOption1.InputId = that.attr("id"); 
				PreviewQuestionAndOption1.InputDom = that;
				
				$("#mathsExpressionModal").find(".addExpression").off().on("click",function(){
					var mathExp = "`"+$(this).parents("#mathsExpressionModal").find("#mathInput").val()+"`";
					that.val(that.val()+mathExp);
					PreviewQuestionAndOption1.Update();
				});
			});
			
			$(this).on(option.triggerOff,function(){
				$(".floatButton").hide(400);
			});
			
		});
		that.on("keyup",function(){
			PreviewQuestionAndOption1.Update();
		});
		
		
			}
});

var PreviewModal = {
		  delay: 150,        // delay after keystroke before updating
		  preview: null,     // filled in by Init below
		  buffer: null,      // filled in by Init below
		  timeout: null,     // store setTimout id
		  mjRunning: false,  // true when MathJax is processing
		  mjPending: false,  // true when a typeset has been queued
		  oldText: null,     // used to check if an update is needed
		  //
		  //  Get the preview and buffer DIV's
		  //
		  Init: function () {
		    this.preview = document.getElementById("textPreview");
		    this.buffer = document.getElementById("MathBuffer");
		  },
		  //
		  //  Switch the buffer and preview, and display the right one.
		  //  (We use visibility:hidden rather than display:none since
		  //  the results of running MathJax are more accurate that way.)
		  //
		  SwapBuffers: function () {
			  console.log("Swapping buffer");
		    var buffer = this.preview, preview = this.buffer;
		    this.buffer = buffer; this.preview = preview;
		    buffer.style.visibility = "hidden"; buffer.style.position = "absolute";
		    preview.style.position = ""; preview.style.visibility = "";
		  },
		  //
		  //  This gets called when a key is pressed in the textarea.
		  //  We check if there is already a pending update and clear it if so.
		  //  Then set up an update to occur after a small delay (so if more keys
		  //    are pressed, the update won't occur until after there has been 
		  //    a pause in the typing).
		  //  The callback function is set up below, after the Preview object is set up.
		  //
		  Update: function () {
		    if (this.timeout) {clearTimeout(this.timeout)}
		    this.timeout = setTimeout(this.callback,this.delay);
		  },
		  //
		  //  Creates the preview and runs MathJax on it.
		  //  If MathJax is already trying to render the code, return
		  //  If the text hasn't changed, return
		  //  Otherwise, indicate that MathJax is running, and start the
		  //    typesetting.  After it is done, call PreviewDone.
		  //  
		  CreatePreview: function () {
			  PreviewModal.timeout = null;
		    if (this.mjPending) return;
		    var text = "`"+document.getElementById("mathInput").value+"`";
		    if (text === this.oldtext) return;
		    if (this.mjRunning) {
		      this.mjPending = true;
		      MathJax.Hub.Queue(["CreatePreview",this]);
		    } else {
		      this.buffer.innerHTML = this.oldtext = text;
		      this.mjRunning = true;
		      console.log(this.buffer.innerHTML);
		      MathJax.Hub.Queue(
			["Typeset",MathJax.Hub,this.buffer],
			["PreviewDone",this]
		      );
		    }
		  },
		  //
		  //  Indicate that MathJax is no longer running,
		  //  and swap the buffers to show the results.
		  //
		  PreviewDone: function () {
			  console.log("PreviewDone");
		    this.mjRunning = this.mjPending = false;
		    this.SwapBuffers();
		  }
		};
		
var PreviewQuestionAndOption = {
		  delay: 150,        // delay after keystroke before updating
		  preview: null,     // filled in by Init below
		  buffer: null,      // filled in by Init below
		  timeout: null,     // store setTimout id
		  mjRunning: false,  // true when MathJax is processing
		  mjPending: false,  // true when a typeset has been queued
		  oldText: null,     // used to check if an update is needed
		  
		  //
		  //  Get the preview and buffer DIV's
		  //
		  Init: function () {
		    if(!this.preview)this.preview = $("#QueAndOpttextPreview")[0];
			if(!this.buffer)this.buffer = $("#QueAndOptMathBuffer")[0];
		  },
		  //
		  //  Switch the buffer and preview, and display the right one.
		  //  (We use visibility:hidden rather than display:none since
		  //  the results of running MathJax are more accurate that way.)
		  //
		  SwapBuffers: function () {
			  console.log("Swapping buffer");
		    var buffer = this.preview, preview = this.buffer;
		    this.buffer = buffer; this.preview = preview;
		    buffer.style.visibility = "hidden"; buffer.style.position = "absolute";
		    preview.style.position = ""; preview.style.visibility = "";
		  },
		  //
		  //  This gets called when a key is pressed in the textarea.
		  //  We check if there is already a pending update and clear it if so.
		  //  Then set up an update to occur after a small delay (so if more keys
		  //    are pressed, the update won't occur until after there has been 
		  //    a pause in the typing).
		  //  The callback function is set up below, after the Preview object is set up.
		  //
		  Update: function () {
		    if (this.timeout) {clearTimeout(this.timeout)}
		    this.timeout = setTimeout(this.callback,this.delay);
		  },
		  //
		  //  Creates the preview and runs MathJax on it.
		  //  If MathJax is already trying to render the code, return
		  //  If the text hasn't changed, return
		  //  Otherwise, indicate that MathJax is running, and start the
		  //    typesetting.  After it is done, call PreviewDone.
		  //  
		  InputId:null,
		  InputDom:null,
		  CreatePreview: function () {
			  PreviewQuestionAndOption.timeout = null;
		    if (this.mjPending) return;
		    var text = this.InputDom!=null?this.InputDom.val():$(this.InputId).val;
		    if (text === this.oldtext) return;
		    if (this.mjRunning) {
		      this.mjPending = true;
		      MathJax.Hub.Queue(["CreatePreview",this]);
		    } else {
		      this.buffer.innerHTML = this.oldtext = text;
		      this.mjRunning = true;
		      console.log(this.buffer.innerHTML);
		      MathJax.Hub.Queue(
			["Typeset",MathJax.Hub,this.buffer],
			["PreviewDone",this]
		      );
		    }
		  },
		  //
		  //  Indicate that MathJax is no longer running,
		  //  and swap the buffers to show the results.
		  //
		  PreviewDone: function () {
			  console.log("PreviewDone");
		    this.mjRunning = this.mjPending = false;
		    this.SwapBuffers();
		  }
		};
