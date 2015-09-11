(function ( $ ) {

		/*
		 * function: multiselect
		 * Extended Bootstrap select to select multiple items
		 * $(elm).multiselect()
		 * 
		 * Events: selected,coutnofslectedElement - on checkbox selected
		 * 			allDeslected - on allelementDeselected
		 * 
		 * Usage:----------
		 * HTML:
		 * <div class="dropdown" id="classownerUploadexamSelectBatchName">
			  <button class="btn btn-default dropdown-toggle" type="button" id="classownerUploadexamBatchName" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
				Select Batch
				<span class="caret"></span>
			  </button>
			  <ul class="dropdown-menu" aria-labelledby="classownerUploadexamSubjectNameMenu" id="classownerUploadexamBatchNameMenu">
				<li class="staticMenu"><a href="#" id="selectAll"> <input type="checkbox" class="selectAllCheckbox" id="classownerUploadexamSubjectNameMenuselectAllRadio"><label for="classownerUploadexamSubjectNameMenuselectAllRadio">Select All<label></a></li>
				<li class="staticMenu divider" role="separator" class=""></li>
				
			  </ul>
			</div>
		 * 
		 * JS:
		 * $(#classownerUploadexamSelectBatchName).multiselect();
		 * 
		 * */
			$.fn.multiselect = function(){
				var $that = this;
				this.find('.dropdown-menu').click(function(e) {
					e.stopPropagation();
				});	
				
				this.find('.dropdown-menu').on("click","input",function(e){
				if($(event.target).attr('class') !== "selectAllCheckbox"){
					$that.find('.dropdown-menu').find(".selectAllCheckbox").prop("checked",false);
				}
				
				var lengthSelected = $that.find('.dropdown-menu').find('[type="checkbox"]:checked').not("#classownerUploadexamSubjectNameMenuselectAllRadio").length;
				if(lengthSelected!=0){
					$that.trigger("selected",lengthSelected);
				}else{
					$that.trigger("allDeslected");
				}
				});

				this.find('.dropdown-menu').find(".selectAllCheckbox").on("click",function(){
					if($(this).is(":checked")){
						$(this).parents("ul").find("input").prop("checked",true);
					}
				});
				return this;	
			}

}( jQuery ));
