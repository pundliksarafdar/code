package com.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.classapp.db.exam.Exam_Paper;
import com.classapp.db.fees.Fees;
import com.service.beans.FeeStructure;
import com.transaction.exams.ExamTransaction;
import com.transaction.fee.FeesTransaction;
import com.user.UserBean;

@Path("/feesservice")
public class FeeServiceImpl  extends ServiceBase {
	private HttpServletRequest request;
	
	@GET
	@Path("/test")
	public Response serviceOn(){
		int regId = getRegId();
		return Response.status(200).entity("Fees Service.."+regId).build();
	}
	
	@POST
	@Path("/saveFeeStructre")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveFeeStrucure(FeeStructure feeStructrure){
		feeStructrure.getFees().setInst_id(getRegId());
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.saveFeeStructure(feeStructrure.getFees(), feeStructrure.getFeesStructureList());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getFeeStructre/{fees_ID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFeeStrucure(@PathParam("fees_ID") String fees_id){
		FeesTransaction feesTransaction = new FeesTransaction();
		FeeStructure feeStructureList = feesTransaction.getFeeStructurelist(getRegId(), Integer.parseInt(fees_id));
		return Response.status(Status.OK).entity(feeStructureList).build();
	}
	
	@GET
	@Path("/getAllFeeStructre")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFeeStrucure(){
		FeesTransaction feesTransaction = new FeesTransaction();
		List<Fees> feesList = feesTransaction.getAllFees(getRegId());
		return Response.status(Status.OK).entity(feesList).build();
	}
	
	@POST
	@Path("/updateFeeStructre")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateFeeStrucure(FeeStructure feeStructrure){
		feeStructrure.getFees().setInst_id(getRegId());
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.updateFeeStructure(feeStructrure.getFees(), feeStructrure.getFeesStructureList());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@DELETE
	@Path("/deleteFeeStructre/{fees_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteFeeStrucure(@PathParam("fees_ID") String fees_id){
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.deleteFees(getRegId(), Integer.parseInt(fees_id));
		return Response.status(Status.OK).entity(status).build();
	}
}
