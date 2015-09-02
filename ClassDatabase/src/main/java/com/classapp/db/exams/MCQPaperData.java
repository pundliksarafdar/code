package com.classapp.db.exams;

import java.util.List;

public class MCQPaperData {
	MCQDataDB mcqDataDB;
	public MCQPaperData() {
		this.mcqDataDB= new MCQDataDB();
	}
	
	public void addOrUpdateMCQExam(MCQData mcqData) {
		this.mcqDataDB.updateDb(mcqData);
	}

/*	
	public List<MCQDetails> getAllExamPapersDetailsFromID(int class_id){
		return this.mcqDataDB.getAllExamPapersDetails(class_id);
	}
	
	public List<MCQData> getAllExamPapers(int class_id){
		return this.mcqDataDB.getExamPapers(class_id);
	}
	
	public MCQData getMCQPaperFromClass(int exam_id,int class_id){
		return this.mcqDataDB.getMCQPaperFromClass(exam_id, class_id);
	}
	*/
}
