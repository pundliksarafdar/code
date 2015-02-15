package com.transaction.feedback;

import com.classapp.db.Feedbacks.Feedback;
import com.classapp.db.Feedbacks.FeedbackDB;

public class feedbackTransaction {
	public void addFeedback(Feedback feedback) {
		FeedbackDB feedbackDB=new FeedbackDB();
		feedbackDB.addFeedback(feedback);
		
	}

}
