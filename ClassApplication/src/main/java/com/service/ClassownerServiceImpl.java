
package com.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import com.classapp.db.batch.Batch;
import com.classapp.db.exam.Exam;
import com.classapp.db.exam.Exam_Paper;
import com.classapp.db.questionPaper.QuestionPaper;
import com.classapp.db.student.StudentDetails;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Topics;
import com.config.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.service.beans.AddBatchBean;
import com.service.beans.GenerateQuestionPaperResponse;
import com.service.beans.ImageListBean;
import com.service.beans.NewQuestionRequest;
import com.service.beans.QuestionPaperData;
import com.service.beans.QuestionPaperEditFileObject;
import com.service.beans.QuestionPaperFileElement;
import com.service.beans.QuestionPaperFileObject;
import com.service.beans.QuestionPaperPattern;
import com.service.beans.QuestionPaperStructure;
import com.service.beans.RegisterBean;
import com.service.beans.Student;
import com.service.beans.StudentRegisterServiceBean;
import com.service.beans.Student_Fees;
import com.service.beans.SubjectsWithTopics;
import com.serviceinterface.ClassownerServiceApi;
import com.tranaction.header.HeaderTransaction;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.fee.FeesTransaction;
import com.transaction.image.ImageTransactions;
import com.transaction.pattentransaction.QuestionPaperPatternTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.student.StudentTransaction;
import com.user.UserBean;

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
	@Produces(MediaType.APPLICATION_JSON)
	public Response addQuestionPaperPattern(QuestionPaperPattern examPattern){
		Gson gson = new Gson();
		System.out.println(gson.toJson(examPattern));
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId());
		boolean patternStatus= patternTransaction.saveQuestionPaperPattern(examPattern);
		if(patternStatus == false){
			return Response.status(Status.OK).entity(patternStatus).build();
		}
		return Response.status(Status.OK).entity(patternStatus).build();
	}
	
	@POST
	@Path("/updateQuestionPaperPattern")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateQuestionPaperPattern(QuestionPaperPattern examPattern){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId());
		boolean patternStatus= patternTransaction.updateQuestionPaperPattern(examPattern);
		if(patternStatus == false){
			return Response.status(Status.OK).entity(patternStatus).build();
		}
		return Response.status(Status.OK).entity(patternStatus).build();
	}
	
	@POST
	@Path("/searchQuestionPaperPattern/{division}/{patternType}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchQuestionPaperPattern(@PathParam("division") String division,@PathParam("patternType") String patternType){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId());
		List<QuestionPaperPattern> questionPaperPatternList = patternTransaction.getQuestionPaperPatternList(Integer.parseInt(division),patternType);
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
	@Path("/getQuestionPaperList/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestionPaperList(@PathParam("division") String division){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
		List<QuestionPaper> questionPaperList = patternTransaction.getQuestionPaperList(Integer.parseInt(division));
		return Response.status(Status.OK).entity(questionPaperList).build();
	}
	
	@POST
	@Path("/deleteQuestionPaper/{division}/{paper_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteQuestionPaper(@PathParam("division") String division,@PathParam("paper_id") String paper_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
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
		boolean status = patternTransaction.saveQuestionPaper(fileObject, getRegId());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/saveQuestionPaper/{patternId}/{questionPaperName}/{divisionId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveQuestionPaper(@PathParam("patternId") String patternId,@PathParam("questionPaperName") String questionPaperName,
			@PathParam("divisionId") String divisionId
			,Map<String, String>questionAndItem){
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
		fileObject.setPaper_description(questionAndItem.get("desc"));
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
				int question_number = Integer.parseInt(questionAndItem.get(itemId));
				questionPaperFileElement.setQues_no(question_number);
			}
		}
		
		fileObject.setQuestionPaperFileElementList(questionPaperFileElements);
		boolean status = patternTransaction.saveQuestionPaper(fileObject, getRegId());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/updateQuestionPaper/{patternId}/{questionPaperName}/{divisionId}/{paperId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateQuestionPaper(@PathParam("patternId") String patternId,@PathParam("questionPaperName") String questionPaperName,
			@PathParam("divisionId") String divisionId,@PathParam("paperId") int paperId
			,Map<String, String>questionAndItem){
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
		fileObject.setPaper_description(questionAndItem.get("desc"));
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
				int question_number = Integer.parseInt(questionAndItem.get(itemId));
				questionPaperFileElement.setQues_no(question_number);
			}
		}
		
		fileObject.setQuestionPaperFileElementList(questionPaperFileElements);
		fileObject.setPaper_id(paperId);
		boolean status = patternTransaction.updateQuestionPaper(fileObject, getRegId());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getQuestionPaper/{division}/{paper_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestionPaper(@PathParam("division") String division,@PathParam("paper_id") String paper_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
		QuestionPaperEditFileObject fileObject = patternTransaction.getQuestionPaper(Integer.parseInt(division), Integer.parseInt(paper_id));
		return Response.status(Status.OK).entity(fileObject).build();
	}
	
	@POST
	@Path("/saveExamPaper/{examName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveExamPaper(@PathParam("examName") String exam,List<Exam_Paper> exam_PaperList){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		examTransaction.saveExamPaper(userBean.getRegId(), exam, exam_PaperList, userBean.getRegId());
		return Response.status(Status.OK).entity("test").build();
	}
	
	@POST
	@Path("/getExamList/{division}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExamList(@PathParam("division") String division){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		List<Exam> examList = examTransaction.getExamList(Integer.parseInt(division), userBean.getRegId());
		return Response.status(Status.OK).entity(examList).build();
	}
	
	@POST
	@Path("/getExam/{division}/{exam_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExam(@PathParam("division") String division,@PathParam("exam_id") String exam_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		List<Exam_Paper> exam_PaperList = examTransaction.getExamPapers(Integer.parseInt(division), userBean.getRegId(), Integer.parseInt(exam_id));
		return Response.status(Status.OK).entity(exam_PaperList).build();
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
	
	@POST
	@Path("/updateExamPaper/{examID}/{division}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateExamPaper(@PathParam("examID") String examID,List<Exam_Paper> exam_PaperList,@PathParam("division") String division){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		boolean status = examTransaction.updateExamPapers(Integer.parseInt(division), userBean.getRegId(), Integer.parseInt(examID), exam_PaperList,userBean.getRegId());
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
	
	@POST
	@Path("/addStudentByManually")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addStudentByManually(StudentRegisterServiceBean serviceBean) {
		FeesTransaction feesTransaction = new FeesTransaction();
		RegisterTransaction registerTransaction = new RegisterTransaction();
		int student_id = registerTransaction.registerStudentManually(getRegId(),serviceBean.getRegisterBean(), serviceBean.getStudent());
		for (Iterator iterator = serviceBean.getStudent_FeesList().iterator(); iterator
				.hasNext();) {
			Student_Fees student_Fees = (Student_Fees) iterator.next();
			student_Fees.setStudent_id(student_id);
		}
		boolean status = feesTransaction.saveStudentBatchFees(getRegId(), serviceBean.getStudent_FeesList());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/addStudentByID")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addStudentByID(StudentRegisterServiceBean serviceBean) {
		FeesTransaction feesTransaction = new FeesTransaction();
		StudentTransaction studentTransaction = new StudentTransaction();
		studentTransaction.addStudentByID(getRegId(),serviceBean.getStudent());
		boolean status = feesTransaction.saveStudentBatchFees(getRegId(), serviceBean.getStudent_FeesList());
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
		List<StudentDetails>studentDetails = studentTransaction.generateRollNumber(batchId, divId, 4);
		studentTransaction.updateStudentRollNumber(batchId, 4, Integer.parseInt(divId), studentDetails);
		return Response.status(Status.OK).entity(studentDetails).build();
	}
}
