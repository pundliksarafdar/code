
package com.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Topics;
import com.config.Constants;
import com.service.beans.AddBatchBean;
import com.service.beans.GenerateQuestionPaperResponse;
import com.service.beans.ImageListBean;
import com.service.beans.NewQuestionRequest;
import com.service.beans.QuestionPaperPattern;
import com.service.beans.QuestionPaperStructure;
import com.service.beans.SubjectsWithTopics;
import com.serviceinterface.ClassownerServiceApi;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.image.ImageTransactions;
import com.transaction.pattentransaction.QuestionPaperPatternTransaction;
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
		imageTransactions.copyLogoImage(getRegId()+imageId);
		return Response.status(200).build();
	}

	@Override
	@GET
	@Path("/logoImages")
	@Produces("application/json")
	public Response logoImage() {
		List<ImageListBean> list = new ArrayList<ImageListBean>(); 
		ImageTransactions imageTransactions = new ImageTransactions(Constants.STORAGE_PATH);
		com.classapp.utils.Constants.IMAGE_TYPE type = com.classapp.utils.Constants.IMAGE_TYPE.LOGO;
		list = imageTransactions.getImageList(type);
		return Response.status(200).entity(list).build();
	}
	
	@POST
	@Path("/generateQuestionPaper/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateQuestionPaperSpecific(@PathParam("division") String division,NewQuestionRequest newQuestionRequest){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId(),userBean.getUserStatic().getExamPath());
		GenerateQuestionPaperResponse questionPaperResponse=patternTransaction.generateQuestionPaperSpecific(Integer.parseInt(division), newQuestionRequest);
		return Response.status(Status.OK).entity(questionPaperResponse).build();
	}
}
