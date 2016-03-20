var TABLE = "#ClassFeatureTable";
var DataDetails = ".DataDetails";
var UpdateStatus = ".updateStatus",
	ENABLE_EMAIL = ".enableEmail",
	ENABLE_SMS = ".enableSms",
	ALLOCATED_SPACE = ".allocatedSpace",
	ALLOCATED_SMS = ".allocatedSms";
	ALLOCATED_ID = ".allocatedStudentId"

var featureDataUrl = "/rest/admin/classes/classlist";

var datatable;

var optionSelect = {
	onText:"Enable",
	offText:"Disable"
}

$(document).ready(function(){
	getClassFeatureData();
	
	$("body").on("click",DataDetails,showDetailInfo)
		.on("click",UpdateStatus,updateFeatures);
});

function getClassFeatureData(){
	var handler = {};
	handler.success = getClassFeatureDataSuccess;
	handler.error = getClassFeatureDataError;
	rest.get(featureDataUrl,handler);
}

function getClassFeatureDataSuccess(data){
	displayDataTable(data);
}

function getClassFeatureDataError(e){}

function showDetailInfo(){
	 var tr = $(this).closest('tr');
     var row = datatable.row( tr );
	 if($(this).hasClass("glyphicon-minus-sign")){
		 row.child().hide();
		 $(this).removeClass("glyphicon-minus-sign");
		 $(this).addClass("glyphicon-plus-sign");
	 }else{
		row.child(formatTableData(row.data())).show();
		$(this).removeClass("glyphicon-plus-sign");
		$(this).addClass("glyphicon-minus-sign");
	 }
	 $(TABLE).find("input[type=\"checkbox\"].enableSms,input[type=\"checkbox\"].enableEmail").bootstrapSwitch(optionSelect);
}

function displayDataTable(data){
	datatable = $(TABLE).DataTable({
		bDestroy:true,
		data: data,
		lengthChange: false,
		columns:[
		{
			"orderable": false,
			"data": null,
			sWidth:"5%",
			"render": function ( data, type, row ) {
					return '<div class="DataDetails glyphicon glyphicon-plus-sign"></div>';
		   }
        },
		{
			title: "Institute name",data:"classname",sDefault:'&mdash;'
		},
		{
			title: "<i class='glyphicon glyphicon-floppy-disk'></i> Storage",data:"avail_memory",sDefault:'&mdash;',
			"render": function ( data, type, row ) {
					return row.avail_memory+"/"+row.alloc_memory+' MB';
			}
		},
		{
			title: "<i class='glyphicon glyphicon-user'></i> Student id",data:"avail_ids",sDefault:'&mdash;',
			"render": function ( data, type, row ) {
					return row.used_ids+"/"+row.alloc_ids;
			}
		},
		{
			title: "<i class='glyphicon glyphicon-envelope'></i> Sms",data:"avail_memory",sDefault:'&mdash;',
			"render": function ( data, type, row ) {
					if(row.smsAccess){
						return row.smsLeft+"/"+row.smsAlloted;
					}else{
						return "No sms access";
					}
			}
		},
		{
			title: "<i class='glyphicon glyphicon-envelope'></i> Email",data:"avail_memory",sDefault:'&mdash;',
			"render": function ( data, type, row ) {
					if(row.emailAccess){
						return "Enabled";
					}else{
						return "No email access";
					}
			}
		}
		]
	});
}

function formatTableData(data){
	var dataDom = "<table>"+
	"<tr><td>Available student</td><td><input type='text' class='form-control allocatedStudentId'/></td></tr>"+
	"<tr><td>Available space</td><td><input type='text' class='form-control allocatedSpace'/></td><td>MB</td></tr>";
	
	if(data.smsAccess){
		dataDom = dataDom + "<tr><td>Sms</td><td><input type='text' class='form-control allocatedSms'/></td><td><input type='checkbox' checked data-size=\"mini\" class='enableSms'/></td></tr>";
	}else{
		dataDom = dataDom + "<tr><td>Sms</td><td><input type='text' class='form-control allocatedSms'/></td><td><input type='checkbox' data-size=\"mini\" class='enableSms'/></td></tr>";
	}
	
	if(data.emailAccess){
		dataDom = dataDom + "<tr><td>Email</td><td></td><td><input type='checkbox' class='enableEmail' data-size=\"mini\" checked/></td></tr>";
	}else{
		dataDom = dataDom + "<tr><td>Email</td><td></td><td><input type='checkbox' class='enableEmail' data-size=\"mini\" /></td></tr>";
	}
	dataDom = dataDom + "<tr><td><input type='button' value='Update' class='btn btn-default updateStatus'></td><td></td></tr>"+
	"</table>"
	return dataDom;
}

function updateFeatures(){
	var formTable = $(this).closest('table');
	var tr = formTable.closest('tr').prev();
	var data = datatable.row(tr).data();
	data.smsAccess = formTable.find(ENABLE_SMS).is(":checked");
	data.emailAccess = formTable.find(ENABLE_EMAIL).is(":checked");
	data.alloc_ids = formTable.find(ALLOCATED_ID).val();
	data.alloc_memory = formTable.find(ALLOCATED_SPACE).val();
	data.smsAlloted = formTable.find(ALLOCATED_SMS).val();
	console.log(data);
	var handler = {};
	handler.success = function(e){
		$.notify({message: "Status saved successfully"},{type: 'success'});
		getClassFeatureData();
	};
	handler.error = function(){};
	rest.put(featureDataUrl,handler,JSON.stringify(data));
}