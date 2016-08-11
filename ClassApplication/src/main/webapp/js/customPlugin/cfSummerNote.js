$.fn.cfSummerNote = function(option){
	var toolbarOption = $.summernote.options.toolbar;
	$.summernote.options.buttons = { addMarksButton: AddMarksButton,addSubjectNameButton:AddSubjectNameButton,addStandardButton:AddStandardButton,
			addStreamButton:AddStreamButton,addExamNameButton:AddExamNameButton};
	toolbarOption.push(['fontsize', ['fontsize']]);
	toolbarOption.push(['misc', ['print']]);
	toolbarOption.push(['mybutton', ['addMarksButton','addSubjectNameButton','addStandardButton','addStreamButton','addExamNameButton']]);
	
	$(this).summernote(option);
	
	var AddMarksButton = function (context) {
		  var ui = $.summernote.ui;
		  
		  // create button
		  var button = ui.button({
		    contents: 'Marks',
		    tooltip: 'Marks will be added automatically on exam generation',
		    click: function () {
		      // invoke insertText method with 'hello' on editor module.
		      context.invoke('editor.insertText', '{{marks}}');
		    }
		  });

		  return button.render();   // return button as jquery object 
		}

	var AddSubjectNameButton = function (context) {
		  var ui = $.summernote.ui;
		  
		  // create button
		  var button = ui.button({
		    contents: 'Subject',
		    tooltip: 'Subject name will be added automatically on exam generation',
		    click: function () {
		      // invoke insertText method with 'hello' on editor module.
		      context.invoke('editor.insertText', '{{SubjectName}}');
		    }
		  });

		  return button.render();   // return button as jquery object 
		}

	var AddStandardButton = function (context) {
		  var ui = $.summernote.ui;
		  
		  // create button
		  var button = ui.button({
		    contents: 'Standard',
		    tooltip: 'Standard will be added automatically on exam generation',
		    click: function () {
		      // invoke insertText method with 'hello' on editor module.
		      context.invoke('editor.insertText', '{{Standard}}');
		    }
		  });

		  return button.render();   // return button as jquery object 
		}

	var AddStreamButton = function (context) {
		  var ui = $.summernote.ui;
		  
		  // create button
		  var button = ui.button({
		    contents: 'Stream',
		    tooltip: 'Stream will be added automatically on exam generation',
		    click: function () {
		      // invoke insertText method with 'hello' on editor module.
		      context.invoke('editor.insertText', '{{StreamName}}');
		    }
		  });

		  return button.render();   // return button as jquery object 
		}

	var AddExamNameButton = function (context) {
		  var ui = $.summernote.ui;
		  
		  // create button
		  var button = ui.button({
		    contents: 'Exam name',
		    tooltip: 'Exam name will be added automatically on exam generation',
		    click: function () {
		      // invoke insertText method with 'hello' on editor module.
		      context.invoke('editor.insertText', '{{ExamName}}');
		    }
		  });

		  return button.render();   // return button as jquery object 
		}
}