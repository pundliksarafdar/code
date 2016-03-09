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
import com.classapp.db.fees.BatchFees;
import com.classapp.db.fees.BatchFeesDistribution;
import com.classapp.db.fees.Fees;
import com.classapp.db.fees.Student_Fees;
import com.service.beans.BatchFeesDistributionServiceBean;
import com.service.beans.BatchServiceBean;
import com.service.beans.FeeStructure;
import com.service.beans.StudentFeesServiceBean;
import com.service.beans.Student_Fees_Transaction;
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
	public Response deleteFeeStrucure(@PathParam("fees_id") String fees_id){
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.deleteFees(getRegId(), Integer.parseInt(fees_id));
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/saveBatchFeesDistribution")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveBatchFeesDistribution(BatchFeesDistributionServiceBean serviceBean){
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.saveBatchFeesDistribution(serviceBean,getRegId());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/updateBatchFeesDistribution")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateBatchFeesDistribution(BatchFeesDistributionServiceBean serviceBean){
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.saveBatchFeesDistribution(serviceBean,getRegId());
		return Response.status(Status.OK).entity(status).build();
	}
	
	
	@GET
	@Path("/getBatchFeesDistribution/{division}/{batchID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBatchFeesDistribution(@PathParam("division") String division,@PathParam("batchID") String batchID){
		FeesTransaction feesTransaction = new FeesTransaction();
		BatchFeesDistributionServiceBean serviceBean = feesTransaction.getBatchFeesDistribution(getRegId(), Integer.parseInt(division),Integer.parseInt(batchID));
		return Response.status(Status.OK).entity(serviceBean).build();
	}
	
	@GET
	@Path("/getInstituteBatch/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInstituteBatch(@PathParam("division") String division){
		FeesTransaction feesTransaction = new FeesTransaction();
		List<BatchServiceBean> serviceBeanList = feesTransaction.getInstituteBatch(Integer.parseInt(division), getRegId());
		return Response.status(Status.OK).entity(serviceBeanList).build();
	}
	
	@POST
	@Path("/reLinkBatchFeesDistribution")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response reLinkBatchFeesDistribution(BatchFeesDistributionServiceBean serviceBean){
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.saveBatchFeesDistribution(serviceBean,getRegId());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/saveStudentBatchFees")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveStudentBatchFees(List<com.service.beans.Student_Fees> student_FeesList){
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.saveStudentBatchFees(getRegId(), student_FeesList);
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/saveStudentBatchFeesTransaction")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveStudentBatchFeesTransaction(Student_Fees_Transaction serviceFees_Transaction){
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.saveStudentBatchFeesTransaction(getRegId(), serviceFees_Transaction);
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getStudentsFees/{student_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsFees(@PathParam("student_id")int student_id) {
		FeesTransaction feesTransaction = new FeesTransaction();
		StudentFeesServiceBean serviceBean = feesTransaction.getStudentFees(getRegId(), student_id);
		return Response.status(Status.OK).entity(serviceBean).build();
	}
	
	@GET
	@Path("/getAllBatchStudentsFees/{div_id}/{batch_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllBatchStudentsFees(@PathParam("div_id")int div_id,@PathParam("batch_id")int batch_id) {
		FeesTransaction feesTransaction = new FeesTransaction();
		List list = feesTransaction.getAllBatchStudentsFees(getRegId(), div_id, batch_id);
		return Response.status(Status.OK).entity(list).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateStudentFeesAmt")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateStudentFeesAmt(com.service.beans.Student_Fees student_Fees) {
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.updateStudentFeesAmt(getRegId(), student_Fees.getDiv_id(), student_Fees.getBatch_id(), student_Fees.getStudent_id(), student_Fees.getDiscount(), student_Fees.getDiscount_type());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getBatchFees/{div_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBatchFees(@PathParam("div_id")int div_id,List<Integer> batchIdList) {
		FeesTransaction feesTransaction = new FeesTransaction();
		List<BatchFees> batchFeesList = feesTransaction.getBatchFeesList(getRegId(), div_id, batchIdList);
		return Response.status(Status.OK).entity(batchFeesList).build();
	}
		
}
