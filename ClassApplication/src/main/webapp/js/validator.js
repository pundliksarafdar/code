/*Jquery validator*/

/*Tag property
	maxlength:Define maximum length
	minlength:Define minimum length
	validationMessage:is to provide validation message
*/
$.fn.validateCustome = function(globalOption){
	var $form = $(this);
	var hasError = false;
	var ERROR_NO_REGULAR_EXPRESSION = "Regular expression is not available";
	var ERROR_NO_MESSAGE_CONTAINER = "Message container not defined";
	
	var validationMessages = {
		fieldRequired:"This field is mandatory",
		minRequired:"Minimum value required is {[val]}",
		maxRequired:"Maximum value required is {[val]}",
		minSelectionRequired:"Please select {[val]} selection"
	}
	var MAX_LENGTH = "maxlength";
	var MIN_LENGTH = "minlength";
	var REQUIRED = "required";
	var MIN = "min";
	var MAX = "max";
	
	var maxLengthValidation = function(elm,value,validationMessage){
		return true;
	}
	
	var minLengthValidation = function(elm,value,validationMessage){
		return true;
	}
	
	var regualrExpressionValidation = function(elm,value,validationMessage){
		return true;
	}
	
	var requiredValidation = function(elm,value,validationMessage){
		var msgStr;
		if(!validationMessage){
			msgStr = validationMessages.fieldRequired;
		}else{
			msgStr = validationMessage;
		}
		if($(elm).val().trim().length == 0){
			$(elm).prev('.validation-message').removeClass('hide').html(msgStr);
			return false;
		}
		return true;
	}
	
	var minValidation = function(elm,value,validationMessage){
		var msgStr;
		if(!validationMessage){
			msgStr = validationMessages.minRequired.replace("{[val]}",value);
		}else{
			msgStr = validationMessage.replace("{[val]}",value);
		}
		
		var elmVal = isNaN(parseInt($(elm).val()))?0:parseInt($(elm).val());
		var attrVal = isNaN(parseInt(value))?0:parseInt(value);
		if(elmVal < attrVal){
			$(elm).prev('.validation-message').removeClass('hide').html(msgStr);			
			return false;
		}
		return true;
	}
	
	var maxValidation = function(elm,value,validationMessage){
		var msgStr;
		if(!validationMessage){
			msgStr = validationMessages.maxRequired.replace("{[val]}",value);
		}else{
			msgStr = validationMessage.replace("{[val]}",value);
		}
		var elmVal = isNaN(parseInt($(elm).val()))?0:parseInt($(elm).val());
		var attrVal = isNaN(parseInt(value))?0:parseInt(value);
		if(elmVal > attrVal){
			$(elm).prev('.validation-message').removeClass('hide').html(msgStr);			
			return false;
		}
		return true;
	}
	
	var mandatorySelectionValidation = function(elm,value,validationMessage){
		var selectedLength = $(elm).find("input[type='checkbox']:checked").length;
		mandateSelValue = isNaN(parseInt(value))?0:parseInt(value);
		if(selectedLength<mandateSelValue){
			var msgStr;
			if(!validationMessage){
				msgStr = validationMessages.minSelectionRequired.replace("{[val]}",value);
			}else{
				msgStr = validationMessage.replace("{[val]}",value);
			}
			
			$($(elm).find('.validation-message').get(0)).html(msgStr).removeClass("hide");	
			return false;
		}
		return true;
	}
	
	var validationAttributes = {
			maxlength:maxLengthValidation,
			minlength:minLengthValidation,
			regexp:regualrExpressionValidation,
			required:requiredValidation,
			min:minValidation,
			max:maxValidation,
			mandatorySelection:mandatorySelectionValidation
		}
	var option = {
			messageContainer:undefined,
			validationMessage:'field does not meet criteria',
			validationTags:['input[type=\'text\']','input[type=\'number\']','textarea','div.form-btn-group']	
	}
	option = $.extend(option,globalOption);
	
	$.each(option.validationTags,function(index,tags){
		var childNode = $form.find(tags).not('[novalidate]');
		childNode.on("click",function(){
			$(this).prev('.validation-message').addClass('hide').html("");	
			$(this).find('.validation-message').addClass('hide').html("");	
		});
		
		$.each(validationAttributes,function(key,fn){
			var attrValue = childNode.attr(key);
			if(attrValue){
				var validationMessage = childNode.attr("validationMessage");
				var result = fn(childNode,attrValue,validationMessage);
				if(!result){
						hasError = true;
				}	
				return result;
			}
		});
		
	});
	
	return hasError;
}