
package com.service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.Notes.Notes;
import com.classapp.db.Teacher.TeacherDetails;
import com.classapp.db.batch.Batch;
import com.classapp.db.batch.division.Division;
import com.classapp.db.exam.Exam;
import com.classapp.db.exam.Exam_Paper;
import com.classapp.db.header.Header;
import com.classapp.db.question.Questionbank;
import com.classapp.db.questionPaper.QuestionPaper;
import com.classapp.db.questionPaper.QuestionPaperDB;
import com.classapp.db.student.StudentDetails;
import com.classapp.db.student.StudentMarks;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Subjects;
import com.classapp.db.subject.Topics;
import com.classapp.login.UserStatic;
import com.config.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mails.AllMail;
import com.service.beans.AddBatchBean;
import com.service.beans.EditQuestionPaper;
import com.service.beans.EvaluateExamBean;
import com.service.beans.ExamSubject;
import com.service.beans.ExamWiseStudentDetails;
import com.service.beans.GenerateQuestionPaperResponse;
import com.service.beans.ImageListBean;
import com.service.beans.NewQuestionRequest;
import com.service.beans.OnlineExam;
import com.service.beans.OnlineExamPaper;
import com.service.beans.OnlineExamPaperSubjects;
import com.service.beans.ProgressCardServiceBean;
import com.service.beans.QuestionPaperData;
import com.service.beans.QuestionPaperEditFileObject;
import com.service.beans.QuestionPaperFileElement;
import com.service.beans.QuestionPaperFileObject;
import com.service.beans.QuestionPaperPattern;
import com.service.beans.QuestionPaperStructure;
import com.service.beans.RegisterBean;
import com.service.beans.Student;
import com.service.beans.StudentData;
import com.service.beans.StudentDetailAttendanceData;
import com.service.beans.StudentFeesServiceBean;
import com.service.beans.StudentRegisterServiceBean;
import com.service.beans.Student_Fees;
import com.service.beans.SubjectsWithTopics;
import com.service.helper.NotificationServiceHelper;
import com.serviceinterface.ClassownerServiceApi;
import com.tranaction.header.HeaderTransaction;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.attendance.AttendanceTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.fee.FeesTransaction;
import com.transaction.image.ImageTransactions;
import com.transaction.institutestats.InstituteStatTransaction;
import com.transaction.notes.NotesTransaction;
import com.transaction.pattentransaction.QuestionPaperPatternTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;
import com.transaction.questionbank.ExcelFileTransaction;

@Path("/classownerservice") 
public class ClassownerServiceImpl extends ServiceBase implements ClassownerServiceApi{
	
	private static final String UPLOADED_FILE_PATH = "C:"+File.separatorChar+"imageuploaded"+File.separatorChar;
	@Context
	private HttpServletRequest request;
	

	@GET
	@Path("/test")
	public Response serviceOn(){
		int regId = getRegId();
		return Response.status(200).entity("Service.."+regId).build();
	}
	
	@POST
	@Path("/addBatch")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addBatch(AddBatchBean addBatchBean){
		return Response.status(200).entity("Service..").build();
	}
	
	@POST
	@Path("/addBatch/{pathParam}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addBatch(@PathParam("pathParam")String testPathParameter){
		return Response.status(200).entity("Service.."+testPathParameter).build();
	}
	
	@POST
	@Path("/addQuestionPaperPattern")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response addQuestionPaperPattern(QuestionPaperPattern examPattern){
		Gson gson = new Gson();
		System.out.println(gson.toJson(examPattern));
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId());
		int  pattern_id= patternTransaction.saveQuestionPaperPattern(examPattern,getRegId());
		InstituteStatTransaction instituteStatTransaction = new InstituteStatTransaction();
		if(pattern_id == 0){
			return Response.status(Status.OK).entity("name").build();
		}else{
			boolean patternStatus = instituteStatTransaction.updateStorageSpace(getRegId(), userBean.getUserStatic().getStorageSpace());
			if(patternStatus == false){
				patternTransaction.deleteQuestionPaperPattern(examPattern.getClass_id(), pattern_id);
				return Response.status(Status.OK).entity("memory").build();
			}
			
		}
		return Response.status(Status.OK).entity("").build();
	}
	
	@POST
	@Path("/updateQuestionPaperPattern")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateQuestionPaperPattern(QuestionPaperPattern examPattern){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId());
		boolean patternStatus= patternTransaction.updateQuestionPaperPattern(examPattern,getRegId());
		if(patternStatus == false){
			return Response.status(Status.OK).entity(patternStatus).build();
		}
		return Response.status(Status.OK).entity(patternStatus).build();
	}
	
	@POST
	@Path("/searchQuestionPaperPattern/{division}/{subject}/{patternType}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchQuestionPaperPattern(@PathParam("division") String division,@PathParam("subject") int subject,@PathParam("patternType") String patternType){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId());
		List<QuestionPaperPattern> questionPaperPatternList = patternTransaction.getQuestionPaperPatternList(Integer.parseInt(division),subject,patternType);
		return Response.status(Status.OK).entity(questionPaperPatternList).build();
	}
	
	@POST
	@Path("/getQuestionPaperPattern/{division}/{patternid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestionPaperPattern(@PathParam("division") String division,@PathParam("patternid") String patternId){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId());
		QuestionPaperPattern questionPaperPattern = patternTransaction.getQuestionPaperPattern(Integer.parseInt(division), Integer.parseInt(patternId));
		return Response.status(Status.OK).entity(questionPaperPattern).build();
	}
	
	@POST
	@Path("/deleteQuestionPaperPattern/{division}/{patternid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteQuestionPaperPattern(@PathParam("division") String division,@PathParam("patternid") String patternId){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId());
		boolean status = patternTransaction.deleteQuestionPaperPattern(Integer.parseInt(division), Integer.parseInt(patternId));
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/generateQuestionPaper/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response generateQuestionPaper(@PathParam("division") String division,List<QuestionPaperStructure> paperStructure){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
		GenerateQuestionPaperResponse questionPaperResponse=patternTransaction.generateQuestionPaper(Integer.parseInt(division), paperStructure);
		return Response.status(Status.OK).entity(questionPaperResponse).build();
	}
	
	@POST
	@Path("/getSubjectsAndTopics/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubjectsAndTopics(@PathParam("division") String division){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId());
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List<Subject> list = subjectTransaction.getSubjectRelatedToDiv(Integer.parseInt(division), userBean.getRegId());
		Map<Integer, SubjectsWithTopics> map = new HashMap<Integer,SubjectsWithTopics>();
		if(list != null){
			if(list.size() > 0){
				
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Subject subject = (Subject) iterator.next();
					List<Topics> topics = subjectTransaction.getTopics(userBean.getRegId(), subject.getSubjectId(), Integer.parseInt(division));
					SubjectsWithTopics subjectsWithTopics = new SubjectsWithTopics();
					subjectsWithTopics.setSubject(subject);
					subjectsWithTopics.setTopics(topics);
					map.put(subject.getSubjectId(), subjectsWithTopics);
				}
			}
		}
		return Response.status(Status.OK).entity(map).build();
	}
	
	@POST
	@Path("/getSubjectsAndTopicsForExam/{division}/{subject}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubjectsAndTopicsForExam(@PathParam("division") String division,@PathParam("subject") int subject_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId());
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		Map<Integer, SubjectsWithTopics> map = new HashMap<Integer,SubjectsWithTopics>();
		Subject subject = subjectTransaction.getSubject(subject_id);
		if("1".equals(subject.getSub_type())){
			String subjectArray[] = subject.getCom_subjects().split(",");
			for (String string : subjectArray) {
				Subject combineSubjects = subjectTransaction.getSubject( Integer.parseInt(string));
				List<Topics> topics = subjectTransaction.getTopics(userBean.getRegId(), Integer.parseInt(string), Integer.parseInt(division));
				SubjectsWithTopics subjectsWithTopics = new SubjectsWithTopics();
				subjectsWithTopics.setSubject(combineSubjects);
				subjectsWithTopics.setTopics(topics);
				map.put(Integer.parseInt(string), subjectsWithTopics);
			}
			
		}else{
		List<Topics> topics = subjectTransaction.getTopics(userBean.getRegId(), subject.getSubjectId(), Integer.parseInt(division));
		SubjectsWithTopics subjectsWithTopics = new SubjectsWithTopics();
		subjectsWithTopics.setSubject(subject);
		subjectsWithTopics.setTopics(topics);
		map.put(subject.getSubjectId(), subjectsWithTopics);
		}
		return Response.status(Status.OK).entity(map).build();
	}
	
	@POST
	@Path("/addQuestionPaperHeader/{headerName}")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addQuestionPaperHeader(@PathParam("patternName")String examPattern){
		return Response.status(200).entity("Service.."+examPattern).build();
	}

	@Override
	@POST
	@Path("/addLogoImages/{imageId}/{imageName}")
	@Produces("application/json")
	public Response addLogoImage(@PathParam("imageId") String imageId,@PathParam("imageName") String imageName) {
		ImageTransactions imageTransactions = new ImageTransactions(Constants.STORAGE_PATH);
		imageTransactions.copyLogoImage(getRegId()+imageId,imageName,getRegId());
		return Response.status(200).build();
	}

	@Override
	@GET
	@Path("/logoImages")
	@Produces("application/json")
	public Response logoImage() {
		List<ImageListBean> list = new ArrayList<ImageListBean>(); 
		ImageTransactions imageTransactions = new ImageTransactions(Constants.STORAGE_PATH);
		list = imageTransactions.getImageList(getRegId(),com.classapp.utils.Constants.IMAGE_TYPE.L);
		return Response.status(200).entity(list).build();
	}
	
	@Override
	@POST
	@Path("/saveHeader/{headerName}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces("application/json")
	public Response saveHeader(String headerElement,
			@PathParam("headerName")String headerName) {
		HeaderTransaction headerTransaction = new HeaderTransaction(Constants.STORAGE_PATH);
		headerTransaction.saveHeader(headerName, headerElement, getRegId());
		return Response.status(200).entity(true).build();
	}
	
	
	@POST
	@Path("/testValidation/{headerName}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces("application/json")
	public Response testValidation(@PathParam("headerName")
    String id){
		List list = new ArrayList();
		return Response.status(200).entity(list).build();
	}
	
	
	@POST
	@Path("/generateQuestionPaperSpecific/{division}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateQuestionPaperSpecific(@PathParam("division") String division,NewQuestionRequest newQuestionRequest){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
		GenerateQuestionPaperResponse questionPaperResponse=patternTransaction.generateQuestionPaperSpecific(Integer.parseInt(division), newQuestionRequest);
		return Response.status(Status.OK).entity(questionPaperResponse).build();
	}
	
	@POST
	@Path("/getQuestionList/{division}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestionList(@PathParam("division") String division,NewQuestionRequest newQuestionRequest){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
		List<QuestionPaperData> questionPaperDataList = patternTransaction.getQuestionList(Integer.parseInt(division), newQuestionRequest);
		return Response.status(Status.OK).entity(questionPaperDataList).build();
	}
	
	@GET
	@Path("/getQuestionPaperList/{division}/{subject}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestionPaperList(@PathParam("division") String division,@PathParam("subject") int subject){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
		List<QuestionPaper> questionPaperList = patternTransaction.getQuestionPaperList(Integer.parseInt(division),subject);
		return Response.status(Status.OK).entity(questionPaperList).build();
	}
	
	@DELETE
	@Path("/deleteQuestionPaper/{division}/{paper_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteQuestionPaper(@PathParam("division") String division,@PathParam("paper_id") String paper_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
		patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		boolean status = patternTransaction.deleteQuestionPaper(Integer.parseInt(division), Integer.parseInt(paper_id));
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/saveQuestionPaperold")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveQuestionPaperOld(QuestionPaperFileObject fileObject){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
	/*	boolean status = patternTransaction.saveQuestionPaper(fileObject, getRegId());*/
		return Response.status(Status.OK).entity(1).build();
	}
	
	@POST
	@Path("/saveQuestionPaper/{patternId}/{questionPaperName}/{divisionId}/{subject}/{compSubjectIds}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveQuestionPaper(@PathParam("patternId") String patternId,@PathParam("questionPaperName") String questionPaperName,
			@PathParam("divisionId") String divisionId,@PathParam("subject") int subject,@PathParam("compSubjectIds")String compSubjectIds
			,Map<String, QuestionPaperFileElement>questionAndItem){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId());
		patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		QuestionPaperPattern questionPaperPattern = patternTransaction.getQuestionPaperPattern(Integer.parseInt(divisionId), Integer.parseInt(patternId));
		List questionPaperPatternList = questionPaperPattern.getQuestionPaperStructure();
		
		/*Form element to save*/
		QuestionPaperFileObject fileObject = new QuestionPaperFileObject();
		fileObject.setClass_id(questionPaperPattern.getClass_id());
		fileObject.setInst_id(questionPaperPattern.getInst_id());
		fileObject.setMarks(questionPaperPattern.getMarks());
		//fileObject.setPaper_description(questionPaperPattern.);
		fileObject.setPattern_id(questionPaperPattern.getPattern_id());
		//Description is added in question item as its too long to send
		fileObject.setPaper_description(questionPaperName);
		List<QuestionPaperFileElement> questionPaperFileElements = new ArrayList<QuestionPaperFileElement>();
		for(int index=0;index<questionPaperPatternList.size();index++){
			QuestionPaperFileElement questionPaperFileElement = new QuestionPaperFileElement();
			QuestionPaperStructure questionPaperStructure = (QuestionPaperStructure) questionPaperPatternList.get(index);
			//Check if item is exist in it or not
			String itemId = questionPaperStructure.getItem_id();
			questionPaperFileElement.setAlternate_value(questionPaperStructure.getAlternate_value());
			questionPaperFileElement.setItem_description(questionPaperStructure.getItem_description());
			questionPaperFileElement.setItem_id(questionPaperStructure.getItem_id());
			questionPaperFileElement.setItem_marks(questionPaperStructure.getItem_marks());
			questionPaperFileElement.setItem_no(questionPaperStructure.getItem_no());
			questionPaperFileElement.setItem_type(questionPaperStructure.getItem_type());
			questionPaperFileElement.setParent_id(questionPaperStructure.getParent_id());
			//questionPaperFileElement.setQues_no(questionPaperStructure.get);
			questionPaperFileElement.setQuestion_topic(questionPaperStructure.getQuestion_topic());
			questionPaperFileElement.setQuestion_type(questionPaperStructure.getQuestion_type());
			questionPaperFileElement.setSubject_id(questionPaperStructure.getSubject_id());
			questionPaperFileElements.add(questionPaperFileElement);
			
			if(questionAndItem.containsKey(itemId)){
				QuestionPaperFileElement questionpaper= questionAndItem.get(itemId);
				questionPaperFileElement.setSubject_id(questionpaper.getSubject_id());
				questionPaperFileElement.setQuestion_topic(questionpaper.getQuestion_topic());
				questionPaperFileElement.setQues_no(questionpaper.getQues_no());
			}
		}
		fileObject.setQuestionPaperFileElementList(questionPaperFileElements);
		patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		boolean status = patternTransaction.saveQuestionPaper(fileObject, getRegId(),subject,compSubjectIds);
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/updateQuestionPaper/{patternId}/{questionPaperName}/{divisionId}/{paperId}/{compSubjectIds}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateQuestionPaper(@PathParam("patternId") String patternId,@PathParam("questionPaperName") String questionPaperName,
			@PathParam("divisionId") String divisionId,@PathParam("paperId") int paperId,@PathParam("compSubjectIds")String compSubjectIds
			,Map<String, QuestionPaperFileElement>questionAndItem){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId());
		QuestionPaperPattern questionPaperPattern = patternTransaction.getQuestionPaperPattern(Integer.parseInt(divisionId), Integer.parseInt(patternId));
		List questionPaperPatternList = questionPaperPattern.getQuestionPaperStructure();
		
		/*Form element to save*/
		QuestionPaperFileObject fileObject = new QuestionPaperFileObject();
		fileObject.setClass_id(questionPaperPattern.getClass_id());
		fileObject.setInst_id(questionPaperPattern.getInst_id());
		fileObject.setMarks(questionPaperPattern.getMarks());
		//fileObject.setPaper_description(questionPaperPattern.);
		fileObject.setPattern_id(questionPaperPattern.getPattern_id());
		//Description is added in question item as its too long to send
		fileObject.setPaper_description(questionPaperName);
		List<QuestionPaperFileElement> questionPaperFileElements = new ArrayList<QuestionPaperFileElement>();
		for(int index=0;index<questionPaperPatternList.size();index++){
			QuestionPaperFileElement questionPaperFileElement = new QuestionPaperFileElement();
			QuestionPaperStructure questionPaperStructure = (QuestionPaperStructure) questionPaperPatternList.get(index);
			//Check if item is exist in it or not
			String itemId = questionPaperStructure.getItem_id();
			questionPaperFileElement.setAlternate_value(questionPaperStructure.getAlternate_value());
			questionPaperFileElement.setItem_description(questionPaperStructure.getItem_description());
			questionPaperFileElement.setItem_id(questionPaperStructure.getItem_id());
			questionPaperFileElement.setItem_marks(questionPaperStructure.getItem_marks());
			questionPaperFileElement.setItem_no(questionPaperStructure.getItem_no());
			questionPaperFileElement.setItem_type(questionPaperStructure.getItem_type());
			questionPaperFileElement.setParent_id(questionPaperStructure.getParent_id());
			//questionPaperFileElement.setQues_no(questionPaperStructure.get);
			questionPaperFileElement.setQuestion_topic(questionPaperStructure.getQuestion_topic());
			questionPaperFileElement.setQuestion_type(questionPaperStructure.getQuestion_type());
			questionPaperFileElement.setSubject_id(questionPaperStructure.getSubject_id());
			questionPaperFileElements.add(questionPaperFileElement);
			
			if(questionAndItem.containsKey(itemId)){
				QuestionPaperFileElement questionpaper= questionAndItem.get(itemId);
				questionPaperFileElement.setSubject_id(questionpaper.getSubject_id());
				questionPaperFileElement.setQuestion_topic(questionpaper.getQuestion_topic());
				questionPaperFileElement.setQues_no(questionpaper.getQues_no());
			}
		}
		
		fileObject.setQuestionPaperFileElementList(questionPaperFileElements);
		fileObject.setPaper_id(paperId);
		patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		boolean status = patternTransaction.updateQuestionPaper(fileObject, getRegId(),compSubjectIds);
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getQuestionPaper/{division}/{paper_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestionPaper(@PathParam("division") String division,@PathParam("paper_id") String paper_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
		patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		patternTransaction.setStoragePath(Constants.STORAGE_PATH);
		EditQuestionPaper fileObject = patternTransaction.getQuestionPaper(Integer.parseInt(division), Integer.parseInt(paper_id));
		return Response.status(Status.OK).entity(fileObject).build();
	}
	
	@POST
	@Path("/saveExamPaper/{examName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response saveExamPaper(@PathParam("examName") String exam,List<Exam_Paper> exam_PaperList){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		String msg = examTransaction.saveExamPaper(userBean.getRegId(), exam, exam_PaperList, userBean.getRegId());
		return Response.status(Status.OK).entity(msg).build();
	}
	
	@POST
	@Path("/getExamList/{division}/{batch_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExamList(@PathParam("division") String division,@PathParam("batch_id") int batch_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		List<Exam> examList = examTransaction.getExamList(Integer.parseInt(division), userBean.getRegId(),batch_id);
		Gson gson = new Gson();
		String jsonElement = gson.toJson(examList);
		return Response.status(Status.OK).entity(jsonElement).build();
	}
	
	@POST
	@Path("/getExam/{division}/{exam_id}/{batch_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExam(@PathParam("division") String division,@PathParam("exam_id") String exam_id,@PathParam("batch_id") int batch_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		List<Exam_Paper> exam_PaperList = examTransaction.getExamPapers(Integer.parseInt(division), userBean.getRegId(), Integer.parseInt(exam_id),batch_id);
		return Response.status(Status.OK).entity(exam_PaperList).build();
	}
	
	@GET
	@Path("/header")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllHeader(){
		HeaderTransaction headerTransaction = new HeaderTransaction(getUserBean().getUserStatic().getHeaderPath());
		List<Header> header = headerTransaction.getHeaderList(getRegId());
		return Response.status(Status.OK).entity(header).build();
	}
	
	@GET
	@Path("/getHeader/{header_id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getHeader(@PathParam("header_id") String header_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		HeaderTransaction headerTransaction = new HeaderTransaction(userBean.getUserStatic().getHeaderPath());
		String header = headerTransaction.getHeader(header_id, userBean.getRegId(), userBean.getUserStatic().getHeaderPath());
		return Response.status(Status.OK).entity(header).build();
	}
	
	@DELETE
	@Path("/header/{header_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteHeader(@PathParam("header_id") String header_id){
		System.out.println("Deleting....."+header_id);
		return Response.ok().build();
	}
	
	@POST
	@Path("/updateExamPaper/{examID}/{division}/{batch_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateExamPaper(@PathParam("examID") String examID,List<Exam_Paper> exam_PaperList,@PathParam("division") String division,@PathParam("batch_id") int batch_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		boolean status = examTransaction.updateExamPapers(Integer.parseInt(division), userBean.getRegId(), Integer.parseInt(examID), exam_PaperList,userBean.getRegId(),batch_id);
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getBatches/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBatchs(@PathParam("division")String division){
		BatchTransactions transactions = new BatchTransactions();
		List<Batch>batches = transactions.getAllBatchesOfDivision(Integer.parseInt(division), getRegId());
		return Response.status(Status.OK).entity(batches).build();
	}
	
	@GET
	@Path("/getAllClasses")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllClasses(){
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		List<Division> divisionList = divisionTransactions.getAllDivisions(getRegId());
		return Response.status(Status.OK).entity(divisionList).build();
	}
	
	@POST
	@Path("/addStudentByManually/{division}/{batch}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response addStudentByManually(StudentRegisterServiceBean serviceBean,@PathParam("division")int division,@PathParam("batch")String batch ) {
		FeesTransaction feesTransaction = new FeesTransaction();
		RegisterTransaction registerTransaction = new RegisterTransaction();
		InstituteStatTransaction statTransaction = new InstituteStatTransaction();
		boolean status  = true;
		if(statTransaction.isIDsAvailable(getRegId(), 1)){
		if(!"".equals(serviceBean.getRegisterBean().getEmail()) && registerTransaction.isEmailExists(serviceBean.getRegisterBean().getEmail())){
			return Response.status(Status.OK).entity("email").build();
		}else{
		int student_id = registerTransaction.registerStudentManually(getRegId(),serviceBean.getRegisterBean(), serviceBean.getStudent(),division,batch);
		statTransaction.increaseUsedStudentIds(getRegId());
		for (Iterator iterator = serviceBean.getStudent_FeesList().iterator(); iterator
				.hasNext();) {
			Student_Fees student_Fees = (Student_Fees) iterator.next();
			student_Fees.setStudent_id(student_id);
		}
	  status = feesTransaction.saveStudentBatchFees(getRegId(), serviceBean.getStudent_FeesList());
		if(status){
			NotificationServiceHelper helper = new NotificationServiceHelper();
			helper.sendFeesPaymentNotification(getRegId(), serviceBean.getStudent_FeesList());
			List<Integer> list= new ArrayList<Integer>();
			list.add(student_id);
			if(!"".equals(serviceBean.getRegisterBean().getEmail())){
			helper.sendManualRegistrationNotification(getRegId(), list);
			}
		}
		}
		}else{
			return Response.status(Status.OK).entity("ID").build();
		}
		return Response.status(Status.OK).entity("").build();
	}
	
	@POST
	@Path("/addStudentByID")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addStudentByID(StudentRegisterServiceBean serviceBean) {
		FeesTransaction feesTransaction = new FeesTransaction();
		StudentTransaction studentTransaction = new StudentTransaction();
		InstituteStatTransaction statTransaction = new InstituteStatTransaction();
		boolean status  = true;
		if(statTransaction.isIDsAvailable(getRegId(), 1)){
		studentTransaction.addStudentByID(getRegId(),serviceBean.getStudent());
		statTransaction.increaseUsedStudentIds(getRegId());
		status = feesTransaction.saveStudentBatchFees(getRegId(), serviceBean.getStudent_FeesList());
		if(status){
			NotificationServiceHelper helper = new NotificationServiceHelper();
			helper.sendFeesPaymentNotification(getRegId(), serviceBean.getStudent_FeesList());
		}
		}else{
			status = false;
		}
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/setRollNumber/{divId}/{batchId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateRollNumber(
			@PathParam("divId")String divId,
			@PathParam("batchId")String batchId){
		StudentTransaction studentTransaction = new StudentTransaction();
		BatchTransactions batchTransactions = new BatchTransactions();
		List<StudentDetails>studentDetails = studentTransaction.generateRollNumber(batchId, divId, getRegId());
		batchTransactions.updateBatchRollGeneratedStatus(Integer.parseInt(batchId), getRegId(), Integer.parseInt(divId), "yes");
		studentTransaction.updateStudentRollNumber(batchId, getRegId(), Integer.parseInt(divId), studentDetails);
		return Response.status(Status.OK).entity(studentDetails).build();
	}
	
	@POST
	@Path("/getExamSubjects/{divId}/{batchId}/{exam_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExamSubjects(
			@PathParam("divId")int div_id,
			@PathParam("batchId")int batch_id,@PathParam("exam_id")int exam_id){
		ExamTransaction examTransaction = new ExamTransaction();
		List<ExamSubject> subjectList = new ArrayList<ExamSubject>();
		subjectList = examTransaction.getExamSubjects(getRegId(), div_id, batch_id, exam_id);
		return Response.status(Status.OK).entity(subjectList).build();
	}
	
	@POST
	@Path("/getStudentForMarksFill/{divId}/{batchId}/{exam}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentForMarksFill(
			@PathParam("divId")int div_id,@PathParam("exam")int exam_id,
			@PathParam("batchId")String batch_id){
		StudentTransaction studentTransaction = new StudentTransaction();
		List<StudentData> studentDatas = studentTransaction.getStudentForExamMarks(batch_id, getRegId(), div_id,exam_id);
		return Response.status(Status.OK).entity(studentDatas).build();
	}
	
	@POST
	@Path("/saveStudentMarks")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveStudentMarks(List<StudentData> studentDataList){
		StudentTransaction studentTransaction = new StudentTransaction();
		boolean status = studentTransaction.saveStudentMarks(studentDataList);
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/getStudentMarksForUpdate/{divId}/{batchId}/{exam_id}/{sub_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentMarksForUpdate(
			@PathParam("divId")int div_id,@PathParam("exam_id")int exam_id,@PathParam("sub_id")int sub_id,
			@PathParam("batchId")String batch_id){
		StudentTransaction studentTransaction = new StudentTransaction();
		List<StudentData> studentDatas = studentTransaction.getStudentForExamMarksUpdate(batch_id, getRegId(), div_id, exam_id, sub_id);
		return Response.status(Status.OK).entity(studentDatas).build();
	}
	
	@GET
	@Path("/getStudentForProgressCard/{divId}/{batchId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentForProgressCard(
			@PathParam("divId")int div_id,@PathParam("batchId")String batch_id){
		StudentTransaction studentTransaction = new StudentTransaction();
		List<StudentData> studentDatas = studentTransaction.getStudentForProgress(batch_id, getRegId(), div_id);
		return Response.status(Status.OK).entity(studentDatas).build();
	}
	
	@POST
	@Path("/addExcelFile/{fileId}")
	@Produces("application/json")
	public Response addExcelFile(@PathParam("fileId") String fileId) {
		ExcelFileTransaction excelTransactions = new ExcelFileTransaction(Constants.STORAGE_PATH);
		String path=excelTransactions.copyExcelFile(getRegId()+fileId,getRegId());
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("fileid", path);
		return Response.status(200).entity(jsonObject.toString()).build();
	}
	
	@GET
	@Path("/getOnlineExamList/{divId}/{batchId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOnlineExamList(
			@PathParam("divId")int div_id,@PathParam("batchId")int batch_id){
		ExamTransaction examTransaction = new ExamTransaction();
		List<Exam> examsList = examTransaction.getOnlineExamList(getRegId(), div_id, batch_id);
		return Response.status(Status.OK).entity(examsList).build();
	}
	
	@GET
	@Path("/getOnlineExamSubjectList/{divId}/{batchId}/{exam}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOnlineExamSubjectList(
			@PathParam("divId")int div_id,@PathParam("batchId")int batch_id,@PathParam("exam")int exam_id){
		ExamTransaction examTransaction = new ExamTransaction();
		List<OnlineExamPaperSubjects> paperSubjectList = examTransaction.getOnlineExamSubjectList(getRegId(), div_id, batch_id, exam_id);
		return Response.status(Status.OK).entity(paperSubjectList).build();
	}
	
	@GET
	@Path("/getOnlineExamPaper/{divId}/{paper}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOnlineExamPaper(
			@PathParam("divId")int div_id,@PathParam("paper")int paper_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
		patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		OnlineExamPaper onlineExamPaper = patternTransaction.getOnlineQuestionPaper(div_id,paper_id,getRegId());
	/*	for (Questionbank questionbank : fileObject.getQuestionPaperFileElementList()) {
			
		}*/
		return Response.status(Status.OK).entity(onlineExamPaper).build();
	}
	
	@POST
	@Path("/getOnlineExamPaperMarks")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOnlineExamPaperMarks(OnlineExam onlineExam){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
		patternTransaction.setQuestionPaperStorageURL(Constants.STORAGE_PATH+File.separator+(onlineExam.getInst_id()!=0?onlineExam.getInst_id():getRegId())+File.separator+"QuestionPaper");
		onlineExam = patternTransaction.getOnlineQuestionPaperMarks(onlineExam.getDiv_id(), onlineExam.getPaper_id(), onlineExam.getAnswers(),onlineExam);
		if(userBean.getRole() == 3){
			StudentMarks studentMarks = new StudentMarks();
			try {
				BeanUtils.copyProperties(studentMarks, onlineExam);
				studentMarks.setStudent_id(getRegId());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
			marksTransaction.saveStudentMarks(studentMarks);
		}
		return Response.status(Status.OK).entity(onlineExam).build();
	}
	
	@GET
	@Path("/getStudentDetails/{student_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentDetails(@PathParam("student_id")int student_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		RegisterTransaction registerTransaction=new RegisterTransaction();
		com.classapp.db.register.RegisterBean studentBean=registerTransaction.getregistereduser(student_id);
		if(!"M".equals(studentBean.getStatus()) && !"E".equals(studentBean.getStatus())){
		studentBean.setLoginPass("");
		}
		StudentTransaction studentTransaction=new StudentTransaction();
		com.classapp.db.student.Student student = studentTransaction.getStudentByStudentID(student_id, userBean.getRegId());
		BatchTransactions batchTransactions = new BatchTransactions();
		List<Batch> batchList = batchTransactions.getBatchRelatedtoDivision(student.getDiv_id());
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		Division division=divisionTransactions.getDidvisionByID(student.getDiv_id());
		String batcharray[] = student.getBatch_id().split(",");
		List<Batch> studentBatchList = new ArrayList<Batch>();
			for (int i = 0; i < batcharray.length; i++) {
			for (int j = 0; j < batchList.size(); j++) {
				if(Integer.parseInt(batcharray[i]) == batchList.get(j).getBatch_id()){
					studentBatchList.add(batchList.get(j));
					break;
				}
			}
			
		}
		StudentDetails studentDetails=new StudentDetails();
		studentDetails.setStudentUserBean(studentBean);
		studentDetails.setStudent(student);
		studentDetails.setBatches(studentBatchList);
		studentDetails.setDivision(division);
		StudentMarksTransaction marksTransaction = new  StudentMarksTransaction();
		ExamWiseStudentDetails examWiseStudentDetails = marksTransaction.getStudentDetailedMarks(getRegId(), student_id);
		studentDetails.setExamWiseStudentDetails(examWiseStudentDetails);
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		StudentDetailAttendanceData attendanceData  = attendanceTransaction.getStudentYearlyAttendance(getRegId(), student_id);
		studentDetails.setAttendanceData(attendanceData);
		FeesTransaction feesTransaction = new FeesTransaction();
		StudentFeesServiceBean feesServiceBean = feesTransaction.getStudentFees(getRegId(), student_id);
		studentDetails.setFeesServiceBean(feesServiceBean);
		return Response.status(Status.OK).entity(studentDetails).build();
	}
	
	@DELETE
	@Path("/exam/{exam_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeExam(@PathParam("exam_id")int examId){
		ExamTransaction examTransaction = new ExamTransaction();
		examTransaction.deleteExam(getRegId(), examId);
		return Response.ok().build();
	}
	
	@GET
	@Path("/teacher")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTeachers(){
		TeacherTransaction teacherTransaction = new TeacherTransaction();
		List<TeacherDetails> teachers = teacherTransaction.getAllTeachersFromClass(getRegId());
		return Response.ok(teachers).build();
	}
	
	@DELETE
	@Path("/getNotes/{division}/{subject}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNotes(@PathParam("division")int division,@PathParam("subject")int subject){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		NotesTransaction notesTransaction=new NotesTransaction();
		List<Notes> noteslist =notesTransaction.getNotesPath(division,subject, userBean.getRegId());
		return Response.status(Status.OK).entity(noteslist).build();
	}
	
	@POST
	@Path("/updateNotes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNotes(Notes notes){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
	    UserStatic userStatic = userBean.getUserStatic();
	    String destPath=  userStatic.getNotesPath()+File.separator+notes.getSubid()+File.separator+notes.getDivid();
		NotesTransaction notesTransaction=new NotesTransaction();
		Notes oldNotes = notesTransaction.getNotesById(notes.getNotesid(), userBean.getRegId(), notes.getSubid(), notes.getDivid());
		int extentionStart = oldNotes.getNotespath().lastIndexOf(".");
		notes.setNotespath(notes.getName()+oldNotes.getNotespath().substring(extentionStart));
		boolean status = notesTransaction.updatenotes(notes.getName(),notes.getNotespath(), notes.getNotesid(), notes.getBatch(), userBean.getRegId(), notes.getDivid(), notes.getSubid());
		if(status == false){
			File oldFile = new File(destPath+ File.separatorChar + oldNotes.getNotespath());
			File newFile = new File(destPath+ File.separatorChar + notes.getNotespath());	
			oldFile.renameTo(newFile);
		}
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/deleteNotes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteNotes(Notes notes){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		UserStatic userStatic = userBean.getUserStatic();
	    String notesPath=  userStatic.getNotesPath()+File.separator+notes.getSubid()+File.separator+notes.getDivid();
		NotesTransaction notesTransaction=new NotesTransaction();
		notesTransaction.deleteNotes( notes.getNotesid(), userBean.getRegId(), notes.getDivid(), notes.getSubid(),notesPath);
		return Response.status(Status.OK).entity(true).build();
	}
	
	@GET
	@Path("/allInstituteSubjects")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getallInstituteSubjects(){
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List<Subjects> subjectList =  subjectTransaction.getAllClassSubjects(getRegId());
		return Response.status(Status.OK).entity(subjectList).build();
	}
	
	@GET
	@Path("/getCombineSubjects/{subject}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCombineSubjects(@PathParam("subject")int subject){
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List<Subject> subjectList =  subjectTransaction.getCombineSubjects(getRegId(), subject);
		return Response.status(Status.OK).entity(subjectList).build();
	}
	
	@POST
	@Path("/updateCombineSubject")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCombineSubject(Subject subject){
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		subject.setInstitute_id(getRegId());
		subject.setSub_type("1");
		boolean status = subjectTransaction.modifySubject(subject);
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getExamQuestionPaperList/{division}/{exam_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExamQuestionPaperList(@PathParam("division")int division,@PathParam("exam_id")int exam_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
		List<QuestionPaper> questionPaperList = patternTransaction.getQuestionPaperListRelatedToExam(division, exam_id);

		return Response.status(Status.OK).entity(questionPaperList).build();
	}
	
	@GET
	@Path("/questionPaperAvailability/{paper_id}/{div_id}/{sub_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkQuestionPaperAvailability(@PathParam("paper_id")int paper_id,@PathParam("div_id")int div_id,@PathParam("sub_id")int sub_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
		boolean status = patternTransaction.checkQuestionPaperAvailability(paper_id, getRegId(), div_id, sub_id);

		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/evaluateExam")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response evaluateExam(EvaluateExamBean evaluateExamBean){
		UserBean userBean = getUserBean();
		QuestionPaperPatternTransaction patternTransaction = null;
		if(userBean.getRole()==1){
			patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
		}else{
			String examStoragePath = com.config.Constants.STORAGE_PATH+File.separator+evaluateExamBean.getInstId();
			userBean.getUserStatic().setStorageSpace(examStoragePath);
			patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),evaluateExamBean.getInstId(),userBean.getUserStatic().getExamPath());
		}
		patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		HashMap<String, Integer> marksObj = patternTransaction.evaluteExam(evaluateExamBean.getExamMap(),evaluateExamBean.getDivision(),evaluateExamBean.getQuestionPaperId(),evaluateExamBean.getInstId());
		StudentMarksTransaction studentMarksTransaction = new StudentMarksTransaction();
		
		StudentMarks studentMarks = new StudentMarks();
		studentMarks.setStudent_id(getRegId());
		studentMarks.setAns_ids("");
		studentMarks.setBatch_id(evaluateExamBean.getBatchId());
		studentMarks.setDiv_id(evaluateExamBean.getDivision());
		studentMarks.setExam_id(evaluateExamBean.getExamId());
		studentMarks.setInst_id(evaluateExamBean.getInstId());
		studentMarks.setMarks(marksObj.get("marks"));
		studentMarks.setSub_id(evaluateExamBean.getSubId());
		
		studentMarksTransaction.saveStudentMarks(studentMarks);
		return Response.status(Status.OK).entity(marksObj).build();
	}
	
	@GET
	@Path("/validateEmail/{email}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateEmail(@PathParam("email")String email){
		UserBean userBean = getUserBean();
		RegisterTransaction registerTransaction = new RegisterTransaction();
		boolean status =  false;
		if(!"".equalsIgnoreCase(email)){
		status = registerTransaction.isEmailExists(email);
		if(!status){
			String activationCode = new Date().getTime()+"";
			AllMail allMail = new AllMail();
			HashMap<String, String> hashMap = new  HashMap();
			hashMap.put("HEADER", "Activation Code");
			hashMap.put("ACTIVATION_CODE",activationCode);	
			hashMap.put("NAME", userBean.getFirstname());
			boolean result = allMail.sendMail(email, hashMap, "ActivationCode.html","ClassFloor - Activation");
			registerTransaction.registerActivationCode(getRegId(), activationCode);
		}
		}
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/resendActivation/{email}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response resendActivation(@PathParam("email")String email){
		UserBean userBean = getUserBean();
		RegisterTransaction registerTransaction = new RegisterTransaction();
		boolean status =  false;
		if(!"".equalsIgnoreCase(email)){
		status = registerTransaction.isEmailExists(email);
		if(!status){
			String activationCode = new Date().getTime()+"";
			AllMail allMail = new AllMail();
			HashMap<String, String> hashMap = new  HashMap();
			hashMap.put("HEADER", "Activation Code");
			hashMap.put("ACTIVATION_CODE",activationCode);	
			hashMap.put("NAME", userBean.getFirstname());
			boolean result = allMail.sendMail(email, hashMap, "ActivationCode.html","ClassFloor - Activation");
			registerTransaction.registerActivationCode(getRegId(), activationCode);
		}
		}
		return Response.status(Status.OK).entity(status).build();
	}
	
	
	@GET
	@Path("/activation/{email}/{activationCode}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response activation(@PathParam("email")String email,@PathParam("activationCode")String code){
		UserBean userBean = getUserBean();
		boolean status =  false;
		RegisterTransaction registerTransaction=new RegisterTransaction();
		if(registerTransaction.ActivationCodeValidation(getRegId(), code)){
			userBean.setActivationcode("");
			userBean.setStatus("");
			registerTransaction.removeActivationCode(getRegId());
			registerTransaction.updateEmail(getRegId(), email);
			return Response.status(Status.OK).entity(true).build();
		}
		return Response.status(Status.OK).entity(status).build();
	}
	
}
