
var previewPattern = function patternPreview() {
	var recursionLevel = 0;
	$("#previewModal").find(".modal-body").empty();
	var sectionCount = $("#examPattern").children().length;
	var counter = 0;
	while(counter < sectionCount){
		recursive($("#examPattern").children()[counter],recursionLevel);
		counter++;
	}
	$("#previewModal").modal('toggle');
};

function recursive(element,recursionLevel){
	if($(element).hasClass("ORClass")){
		$("#previewModal").find(".modal-body").append("<div align='center'>OR</div>")
	}else{
	var innerElement = $(element).find("ul");
	var itemType = innerElement.attr("data-item-type");
	if(itemType == "Section"){
		var itemName = innerElement.find("#createExamItemName").val();
		$("#previewModal").find(".modal-body").append("<div align='center'>"+itemName+"</div>")
	}else if(itemType == "Instruction"){
		var itemName = innerElement.find("#createExamItemName").val();
		var itemNo = innerElement.find("#questionNo").val();
		var itemMarks = innerElement.find("#createExamItemMarks").val();
		$("#previewModal").find(".modal-body").append("<div class='row' ><div class='col-md-1' style='padding-left:"+recursionLevel+"%'>"+itemNo+"</div><div class='col-md-8'>"+itemName+"</div><div class='col-md-1'>"+itemMarks+"</div></div>")
	}else{
		var itemName = innerElement.find("#createExamItemName").val();
		var itemNo = innerElement.find("#questionNo").val();
		var itemMarks = innerElement.find("#createExamItemMarks").val();
		$("#previewModal").find(".modal-body").append("<div class='row' ><div class='col-md-1' style='padding-left:"+recursionLevel+"%'>"+itemNo+"</div><div class='col-md-8'>"+itemName+"</div><div class='col-md-1'>"+itemMarks+"</div></div>")
	}
	recursionLevel++;
	var sectionCount = innerElement.first().children().not('div').length;
	var counter = 0;
	while(counter < sectionCount){
		recursive(innerElement.first().children().not('div')[counter],recursionLevel);
		counter++;
	}
	}
}