package com.exam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.exam.CompExam;
import com.classapp.db.exam.Exam;

import com.classapp.db.question.Questionbank;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Topics;
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.QuestionData;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.user.UserBean;

public class ManualExamAction extends BaseAction{
	String division,batch,subject;
	List<CompExam> compExams;
	List<Integer> marks;
	List<Integer> repeatation;
	String selectedMarks="-1";
	String selectedExamID="-1";
	String selectedRep="-1";
	String selectedTopic="-1";
	String subjectname;
	String divisionName;
	List<QuestionData> questionDataList;
	int totalPages;
	int currentPage;
	String searchedMarks;
	String searchedExam;
	String searchedRep;
	String searchedTopic;
	String questionedit;
	String actionname;
	List<Integer> questionIds;
	String addedIds;
	String removedIds;
	int totalmarks;
	int noofquestions;
	String forwardhref;
	String institute;
	String examname;
	int passmarks;
	List<Topics> topics;
	int examHour;
	int examMinute;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		int inst_id=userBean.getRegId();
		if(institute!=null && !"".equals(institute)){
			UserStatic userStatic = userBean.getUserStatic();
			String storagePath = Constants.STORAGE_PATH+File.separator+institute;
			userStatic.setStorageSpace(storagePath);
			inst_id=Integer.parseInt(institute);
		}
		HashMap<Integer, String> questionIdsMap=(HashMap<Integer, String>) session.get("questionsIds");
		if(questionIdsMap==null){
			questionIdsMap=new HashMap<Integer, String>();
		}
		if(!"".equals(addedIds) && addedIds !=null){
			String addedIdsarr[]=addedIds.split(",");
					for (int i = 0; i < addedIdsarr.length; i++) {
						if (!addedIdsarr[i].equals("")) {
							questionIdsMap.put(Integer.parseInt(addedIdsarr[i]), addedIdsarr[i]);
						}
					}
		}
		
		if(!"".equals(removedIds) && removedIds != null){
			String removedIdsarr[]=removedIds.split(",");
					for (int i = 0; i < removedIdsarr.length; i++) {
						if (!removedIdsarr[i].equals("")) {
							questionIdsMap.remove(Integer.parseInt(removedIdsarr[i]));
						}
					}
		}
		
		session.put("questionsIds",questionIdsMap);
		noofquestions=questionIdsMap.size();
		ExamTransaction examTransaction=new ExamTransaction();
		QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		compExams=examTransaction.getAllCompExamList();
		marks=bankTransaction.getDistinctQuestionMarks(Integer.parseInt(subject), Integer.parseInt(division), inst_id);
		repeatation=bankTransaction.getDistinctQuestionRep(Integer.parseInt(subject), Integer.parseInt(division), inst_id);
		topics=subjectTransaction.getTopics(inst_id, Integer.parseInt(subject), Integer.parseInt(division));
		if("advancesearch".equals(actionname)){
		//SubjectTransaction subjectTransaction=new SubjectTransaction();
		Subject subbean=subjectTransaction.getSubject(Integer.parseInt(subject));
		if(subbean!=null){
			subjectname=subbean.getSubjectName();
		}
		DivisionTransactions divisionTransactions=new DivisionTransactions();
		Division divbean= divisionTransactions.getDidvisionByID(Integer.parseInt(division));
		if(divbean!=null){
			divisionName=divbean.getDivisionName();
		}
		if(currentPage==0){
			searchedExam=selectedExamID;
			searchedMarks=selectedMarks;
			searchedRep=selectedRep;
			searchedTopic=selectedTopic;
		}
		
		if(currentPage==0){
			int totalCount=bankTransaction.getTotalSearchedQuestionCount(Integer.parseInt(selectedRep), selectedExamID, Integer.parseInt(selectedMarks), Integer.parseInt(subject), inst_id, Integer.parseInt(division),Integer.parseInt(selectedTopic));
			if(totalCount>0){
				int remainder=totalCount%50;
				totalPages=totalCount/50;
				if(remainder>0){
					totalPages++;
				}
			}	
			currentPage=1;
			}
			if("true".equals(questionedit)){
				int totalCount=bankTransaction.getTotalSearchedQuestionCount(Integer.parseInt(searchedRep), searchedExam, Integer.parseInt(searchedMarks), Integer.parseInt(subject), inst_id, Integer.parseInt(division),Integer.parseInt(searchedTopic));
				if(totalCount>0){
					int remainder=totalCount%50;
					totalPages=totalCount/50;
					if(remainder>0){
						totalPages++;
					}
				}else{
					totalPages=0;
				}	
			}
			if(totalPages<currentPage){
				currentPage--;
			}
		
		List<Questionbank> questionbanks=bankTransaction.getSearchedQuestions(Integer.parseInt(searchedRep), searchedExam, Integer.parseInt(searchedMarks), Integer.parseInt(subject), inst_id, Integer.parseInt(division),currentPage,Integer.parseInt(searchedTopic));
		UserStatic userStatic = userBean.getUserStatic();
		String questionPath = "";
		if(questionbanks!=null)
		{
			questionIds=new ArrayList<Integer>();
			questionDataList=new ArrayList<QuestionData>();
			for (int i = 0; i < questionbanks.size(); i++) {
				questionPath=userStatic.getExamPath()+File.separator+subjectname+divisionName+File.separator+questionbanks.get(i).getQue_id();
				if (questionIdsMap.containsKey(questionbanks.get(i).getQue_id())) {
					questionIds.add(1);
				}else {
					questionIds.add(0);
				}
				QuestionData questionData=(QuestionData) readObject(new File(questionPath));
				questionDataList.add(questionData);
			}
		}
		
		}else if("showaddedquestions".equals(actionname)){
		//	SubjectTransaction subjectTransaction=new SubjectTransaction();
			Subject subbean=subjectTransaction.getSubject(Integer.parseInt(subject));
			if(subbean!=null){
				subjectname=subbean.getSubjectName();
			}
			DivisionTransactions divisionTransactions=new DivisionTransactions();
			Division divbean= divisionTransactions.getDidvisionByID(Integer.parseInt(division));
			if(divbean!=null){
				divisionName=divbean.getDivisionName();
			}
			if(questionIdsMap!=null){
				questionDataList=new ArrayList<QuestionData>();
				UserStatic userStatic = userBean.getUserStatic();
				String questionPath = "";
				//Set<Integer> keys=questionIdsMap.keySet();
				for (Integer key:questionIdsMap.keySet()) {
					questionPath=userStatic.getExamPath()+File.separator+subjectname+divisionName+File.separator+questionIdsMap.get(key);
					QuestionData questionData=(QuestionData) readObject(new File(questionPath));
					questionDataList.add(questionData);
				}
			}
			currentPage=0;
		}else if("createexam".equals(actionname)){
			
		}else if("submitexam".equals(actionname)){
			if(questionIdsMap!=null){
				questionIds=new ArrayList<Integer>();
				for (Integer key:questionIdsMap.keySet()) {
					questionIds.add(key);
				}
				Collections.sort(questionIds);
				List<String> ansIdsList=examTransaction.getAnswers( Integer.parseInt(subject),inst_id, Integer.parseInt(division), questionIds);
				String ansIds="";
				if (ansIdsList!=null) {
					for (int i = 0; i < ansIdsList.size(); i++) {
						if(i==0){
							ansIds=ansIdsList.get(i)+"";
						}else{
							ansIds=ansIds+"/"+ansIdsList.get(i);
						}
					}
				}
				String queIds="";
				if (questionIds!=null) {
					for (int i = 0; i < questionIds.size(); i++) {
						if(i==0){
							queIds=questionIds.get(i)+"";
						}else{
							queIds=queIds+","+questionIds.get(i);
						}
					}
				}
				Exam exam=new Exam();
				examTransaction.saveExam(examname, inst_id, Integer.parseInt(subject), Integer.parseInt(division), totalmarks, passmarks, userBean.getRegId(), batch, queIds, ansIds,examHour,examMinute);
			}
			session.put("questionsIds",null);
			actionname="examadded";
			return "examadded";
		}else if("autosubmit".equals(actionname)){
			session.put("questionsIds",null);
			return "autosubmit";
		}else{

		//	SubjectTransaction subjectTransaction=new SubjectTransaction();
			Subject subbean=subjectTransaction.getSubject(Integer.parseInt(subject));
			if(subbean!=null){
				subjectname=subbean.getSubjectName();
			}
			DivisionTransactions divisionTransactions=new DivisionTransactions();
			Division divbean= divisionTransactions.getDidvisionByID(Integer.parseInt(division));
			if(divbean!=null){
				divisionName=divbean.getDivisionName();
			}
			if(currentPage==0){
				searchedExam=selectedExamID;
				searchedMarks=selectedMarks;
				searchedRep=selectedRep;
				searchedTopic=selectedTopic;
			}
			
			if(currentPage==0){
				int totalCount=bankTransaction.getTotalSearchedQuestionCount(-1, "-1", -1, Integer.parseInt(subject), inst_id, Integer.parseInt(division),-1);
				if(totalCount>0){
					int remainder=totalCount%50;
					totalPages=totalCount/50;
					if(remainder>0){
						totalPages++;
					}
				}	
				currentPage=1;
				}
				if("true".equals(questionedit)){
					int totalCount=bankTransaction.getTotalSearchedQuestionCount(-1, "-1", -1, Integer.parseInt(subject), inst_id, Integer.parseInt(division),-1);
					if(totalCount>0){
						int remainder=totalCount%2;
						totalPages=totalCount/2;
						if(remainder>0){
							totalPages++;
						}
					}else{
						totalPages=0;
					}	
				}
				if(totalPages<currentPage){
					currentPage--;
				}
			
			List<Questionbank> questionbanks=bankTransaction.getSearchedQuestions(-1, "-1", -1, Integer.parseInt(subject), inst_id, Integer.parseInt(division),currentPage,-1);
			UserStatic userStatic = userBean.getUserStatic();
			String questionPath = "";
			if(questionbanks!=null)
			{
				questionIds=new ArrayList<Integer>();
				questionDataList=new ArrayList<QuestionData>();
				for (int i = 0; i < questionbanks.size(); i++) {
					questionPath=userStatic.getExamPath()+File.separator+subjectname+divisionName+File.separator+questionbanks.get(i).getQue_id();
					if (questionIdsMap.containsKey(questionbanks.get(i).getQue_id())) {
						questionIds.add(1);
					}else {
						questionIds.add(0);
					}
					QuestionData questionData=(QuestionData) readObject(new File(questionPath));
					questionDataList.add(questionData);
				}
			}
			if("examadded".equals(actionname)){
			actionname="examaddedsuccessfully";
			}else{
				actionname="";
			}
		}
		if(userBean.getRole()==2){
			return "teachermanualexam";
		}
		return "addmanualexam";
	}
	
	private Object readObject(File file) {
		Object object = null;
		FileInputStream fin = null;
		ObjectInputStream objectInputStream = null;
		try{
			fin = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(fin);
			object =  objectInputStream.readObject();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=objectInputStream)try {objectInputStream.close();} catch (IOException e) {e.printStackTrace();}
			if(null!=fin)try {fin.close();} catch (IOException e) {e.printStackTrace();}
		}
		return object;
	}
	
	public String getDivision() {
		return division;
	}

	public List<CompExam> getCompExams() {
		return compExams;
	}

	public void setCompExams(List<CompExam> compExams) {
		this.compExams = compExams;
	}

	public List<Integer> getMarks() {
		return marks;
	}

	public void setMarks(List<Integer> marks) {
		this.marks = marks;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public List<Integer> getRepeatation() {
		return repeatation;
	}

	public void setRepeatation(List<Integer> repeatation) {
		this.repeatation = repeatation;
	}

	public String getSelectedMarks() {
		return selectedMarks;
	}

	public void setSelectedMarks(String selectedMarks) {
		this.selectedMarks = selectedMarks;
	}

	public String getSelectedExamID() {
		return selectedExamID;
	}

	public void setSelectedExamID(String selectedExamID) {
		this.selectedExamID = selectedExamID;
	}

	public String getSelectedRep() {
		return selectedRep;
	}

	public void setSelectedRep(String selectedRep) {
		this.selectedRep = selectedRep;
	}

	public List<QuestionData> getQuestionDataList() {
		return questionDataList;
	}

	public void setQuestionDataList(List<QuestionData> questionDataList) {
		this.questionDataList = questionDataList;
	}
	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getSearchedMarks() {
		return searchedMarks;
	}

	public void setSearchedMarks(String searchedMarks) {
		this.searchedMarks = searchedMarks;
	}

	public String getSearchedExam() {
		return searchedExam;
	}

	public void setSearchedExam(String searchedExam) {
		this.searchedExam = searchedExam;
	}

	public String getSearchedRep() {
		return searchedRep;
	}

	public void setSearchedRep(String searchedRep) {
		this.searchedRep = searchedRep;
	}

	public String getQuestionedit() {
		return questionedit;
	}

	public void setQuestionedit(String questionedit) {
		this.questionedit = questionedit;
	}

	public String getActionname() {
		return actionname;
	}

	public void setActionname(String actionname) {
		this.actionname = actionname;
	}

	public List<Integer> getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(List<Integer> questionIds) {
		this.questionIds = questionIds;
	}

	public String getAddedIds() {
		return addedIds;
	}

	public void setAddedIds(String addedIds) {
		this.addedIds = addedIds;
	}

	public String getRemovedIds() {
		return removedIds;
	}

	public void setRemovedIds(String removedIds) {
		this.removedIds = removedIds;
	}

	public int getTotalmarks() {
		return totalmarks;
	}

	public void setTotalmarks(int totalmarks) {
		this.totalmarks = totalmarks;
	}

	public int getNoofquestions() {
		return noofquestions;
	}

	public void setNoofquestions(int noofquestions) {
		this.noofquestions = noofquestions;
	}

	public String getForwardhref() {
		return forwardhref;
	}

	public void setForwardhref(String forwardhref) {
		this.forwardhref = forwardhref;
	}

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	public String getExamname() {
		return examname;
	}

	public void setExamname(String examname) {
		this.examname = examname;
	}

	public int getPassmarks() {
		return passmarks;
	}

	public void setPassmarks(int passmarks) {
		this.passmarks = passmarks;
	}

	public String getSelectedTopic() {
		return selectedTopic;
	}

	public void setSelectedTopic(String selectedTopic) {
		this.selectedTopic = selectedTopic;
	}

	public String getSearchedTopic() {
		return searchedTopic;
	}

	public void setSearchedTopic(String searchedTopic) {
		this.searchedTopic = searchedTopic;
	}

	public List<Topics> getTopics() {
		return topics;
	}

	public void setTopics(List<Topics> topics) {
		this.topics = topics;
	}

	public int getExamHour() {
		return examHour;
	}

	public void setExamHour(int examHour) {
		this.examHour = examHour;
	}

	public int getExamMinute() {
		return examMinute;
	}

	public void setExamMinute(int examMinute) {
		this.examMinute = examMinute;
	}
	
	
}
