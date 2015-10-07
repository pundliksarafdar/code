/*Jquery validator*/

/*Tag property
	maxlength:Define maximum length
	minlength:Define minimum length
	
*/
$.fn.validate = function(globalOption){
	var $form = $(this);
	var hasError = false;
	var ERROR_NO_REGULAR_EXPRESSION = "Regular expression is not available";
	var ERROR_NO_MESSAGE_CONTAINER = "Message container not defined";
	
	var validationMessages = {
		fieldRequired:"This field is mandatory",
		minRequired:"Minimum value required is {[minval]}",
		maxRequired:"Maximum value required is {[maxval]}",
	}
	var MAX_LENGTH = "maxlength";
	var MIN_LENGTH = "minlength";
	var REQUIRED = "required";
	var MIN = "min";
	var MAX = "max";
	
	var maxLengthValidation = function(elm){
		
	}
	
	var minLengthValidation = function(elm){
		
	}
	
	var regualrExpressionValidation = function(elm){
		
	}
	
	var requiredValidation = function(elm){
		if($(elm).val().trim().length == 0){
			$(elm).parents('.form-group').find('.validation-message').removeClass('hide').html(validationMessages.fieldRequired);
			return false;
		}
		return true;
	}
	
	var minValidation = function(elm,value){
		var message = validationMessages.minRequired.replace("{[minval]}",value);
		var elmVal = isNaN(parseInt($(elm).val()))?0:parseInt($(elm).val());
		var attrVal = isNaN(parseInt(value))?0:parseInt(value);
		if(elmVal < attrVal){
			$(elm).parents('.form-group').find('.validation-message').removeClass('hide').html(message);			
			return false;
		}
		return true;
	}
	
	var maxValidation = function(elm,value){
		var message = validationMessages.maxRequired.replace("{[maxval]}",value);
		var elmVal = isNaN(parseInt($(elm).val()))?0:parseInt($(elm).val());
		var attrVal = isNaN(parseInt(value))?0:parseInt(value);
		if(elmVal > attrVal){
			$(elm).parents('.form-group').find('.validation-message').removeClass('hide').html(message);			
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
			max:maxValidation
		}
	var option = {
			messageContainer:undefined,
			validationMessage:'field does not meet criteria',
			validationTags:['input[type=\'text\']','input[type=\'number\']','textarea']	
	}
	option = $.extend(option,globalOption);
	
	$.each(option.validationTags,function(index,tags){
		var childNode = $form.find(tags).not('[novalidate]');
		childNode.on("focus",function(){
			$(this).parents('.form-group').find('.validation-message').addClass('hide').html("");	
		});
		
		$.each(validationAttributes,function(key,val){
			var attrValue = childNode.attr(key);
			if(attrValue){
				hasError = true;
				var result = val(childNode,attrValue);
				return result;
			}
		});
		
	});
	
	return hasError;
}