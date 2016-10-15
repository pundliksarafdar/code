<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>A Simple Page with CKEditor</title>
        <!-- Make sure the path to CKEditor is correct. -->
         <script>
                // Replace the <textarea id="editor1"> with a CKEditor
                // instance, using default configuration.
                
            </script>
            <style type="text/css">
            /*  .certificatePanel{
           	 padding: 2%; 
            }  */
            .note-editing-area{
            padding: 2%
            } 
            </style>
    </head>
    
    <body>
    <jsp:include page="certificateHeaders.jsp" >
		<jsp:param value="active" name="customeUserCertificate"/>
	</jsp:include>
	<div class="container" style="padding: 1%">
		<div class="row">
			<div class="col-md-4">
				<select id="headerSelect" class="form-control">		
				</select>
				<span id="headerSelectError" class="error"></span>
			</div>
		</div>
	<div class="row">
	<div class="col-md-12">
        <form>
        	<div class="summernote">
        		<p >Hello World</p>
        	</div>
        </form>
        <span id="certificatePanelError" class="error"></span>
	    </div>
	 </div>
	 <div class="row">
	 <form id="header-form">
	 <div class="col-md-12">
            <div>
            	<input placeholder="Certificate Description" id="certificateName" name="certificateName" type="text" class="form-control" required style="width: 50%;" maxlength="50"></input>
            </div>
             <span id="certificateNameError" class="error"></span>
      </div>
      <div class="col-md-12">
	        <input type="button" value="Save" class="btn btn-success" id="save"/>
	   </div>
	    </form> 
	    </div>   
    </div>
    </body>
</html>