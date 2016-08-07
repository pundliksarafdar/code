<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>A Simple Page with CKEditor</title>
        <!-- Make sure the path to CKEditor is correct. -->
        <link href="js/summernote/summernote.css" rel="stylesheet">
		<script src="js/summernote/summernote.js"></script>
		<script src="js/summernote-ext-print.js"></script>
    	 <script>
                // Replace the <textarea id="editor1"> with a CKEditor
                // instance, using default configuration.
                
            </script>
    </head>
    
    <body>
    <jsp:include page="ClassownerSettingHeader.jsp" >
		<jsp:param value="active" name="notificationSetting"/>
	</jsp:include>
	<div>
        <form>
        	<div class="summernote"><p>Hello World</p></div>
           
        </form>
        <form id="header-form">
            <div>
            	<input placeholder="Header name" id="headerName" name="headerName" type="text" class="form-control" required style="width: 50%;"></input>
            </div>
	        <input type="button" value="Save" class="btn btn-success" id="save"/>
	    </form>    
    </div>
    <hr/>
    <h4>All headers</h4>
    <div class="row">
		<div id="headers_name" class="col-md-6">
			<table id="headers_name_table"></table>
		</div>
		<div id="headers_html" class="col-md-6">
			<div class="jumbotron">
				<h3>Preview</h3>
				<p>Move cursor on header name to view preview</p>
			</div>
		</div>
	</div>
	
    </body>
</html>