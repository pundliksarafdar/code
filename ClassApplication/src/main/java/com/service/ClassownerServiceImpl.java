
package com.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

import com.config.Constants;
import com.service.beans.AddBatchBean;
import com.service.beans.ImageListBean;
import com.service.beans.QuestionPaperPattern;
import com.serviceinterface.ClassownerServiceApi;
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
	@Path("/searchQuestionPaperPattern/{division}/{patternType}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchQuestionPaperPattern(@PathParam("division") String division,@PathParam("patternType") String patternType){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getRegId());
		List<QuestionPaperPattern> questionPaperPatternList = patternTransaction.getQuestionPaperPatternList(Integer.parseInt(division));
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
}
