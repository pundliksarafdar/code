package com.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletContext;
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
import com.classapp.db.printFees.PrintFees;
import com.classapp.db.printFees.PrintFeesDb;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.miscfunction.MiscFunction;
import com.notification.access.NotifcationAccess;
import com.service.beans.BatchFeesDistributionServiceBean;
import com.service.beans.BatchServiceBean;
import com.service.beans.BatchStudentFees;
import com.service.beans.FeeStructure;
import com.service.beans.PrintDetailResponce;
import com.service.beans.StudentFeesServiceBean;
import com.service.beans.Student_Fees_Transaction;
import com.service.helper.NotificationServiceHelper;
import com.transaction.exams.ExamTransaction;
import com.transaction.fee.FeesTransaction;
import com.user.UserBean;
import com.util.ClassAppUtil;
import com.util.CryptoException;
import com.util.CryptoUtils;

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
		boolean status = feesTransaction.updateBatchFeesDistribution(serviceBean,getRegId());
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
		boolean status = feesTransaction.updateBatchFeesDistribution(serviceBean,getRegId());
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
	public Response saveStudentBatchFeesTransaction(Student_Fees_Transaction serviceFees_Transaction) throws IOException{
		FeesTransaction feesTransaction = new FeesTransaction();
		
		boolean status = feesTransaction.saveStudentBatchFeesTransaction(getRegId(), serviceFees_Transaction);
		if(status){
			NotificationServiceHelper helper = new NotificationServiceHelper();
			helper.sendFeesPaymentNotification(getRegId(), serviceFees_Transaction);
			
			PrintDetailResponce printDetailResponce = feesTransaction.saveFees(serviceFees_Transaction, "path", "type");
			printDetailResponce.setInstituteName(getUserBean().getClassName());
			printDetailResponce.setAddress(getUserBean().getAddr1()+","+getUserBean().getCity());
			printDetailResponce.setFeesPayingNow(serviceFees_Transaction.getAmt_paid());
			DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		    
	    	sdf.setTimeZone(TimeZone.getTimeZone("IST"));
	    	Date date = new Date();
	    	printDetailResponce.setTodaysDate(sdf.format(date));
		    
	    	/***********Amount paid in *******/
	    	String amountInWords = ClassAppUtil.convert((long)serviceFees_Transaction.getAmt_paid());
	    	printDetailResponce.setAmmountInWords(amountInWords+" rupees only");
			MustacheFactory mf = new DefaultMustacheFactory();
			ServletContext context = MiscFunction.getServletContext();
			String fullPath = context.getRealPath("/WEB-INF/classes/htmlfile/feereceipt.tmpl");
			File f = new File(fullPath);
			if(!f.exists()){
				f.mkdirs();
			}
			String receiptPath = getUserBean().getUserStatic().getFeesReceiptPath();
			File receiptFile = new File(receiptPath);
			if(!receiptFile.exists()){
				receiptFile.mkdirs();
			}
			String fileName = receiptPath+File.separator+printDetailResponce.getFeeReceiptNumber()+".html";
			Mustache mustache = mf.compile(new InputStreamReader(new FileInputStream(f),Charset.forName("UTF-8")),f.getName());
			FileWriter fileWriter = new FileWriter(fileName);
			mustache.execute(fileWriter, printDetailResponce).flush();
			fileWriter.close();
			/*******************Encrypt file*******************/
			try {
				CryptoUtils.encrypt(fileName);
	        } catch (CryptoException ex) {
	            ex.printStackTrace();
	        }
	        
		}
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
	
	@GET
	@Path("/getPrintDetail/{divId}/{batchId}/{studentId}/{feeStructId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPrintDetail(@PathParam("divId")int div_id,
			@PathParam("batchId")int batchId,
			@PathParam("studentId")int studentId,
			@PathParam("feeStructId")String feeStructId) {
		int regId = getRegId();
		PrintDetailResponce responce = new PrintDetailResponce();
		UserBean userBean = getUserBean();
		responce.setAddress(userBean.getAddr1()+","+userBean.getAddr1());
		responce.setContactNo(userBean.getPhone1());
		responce.setInstituteName(userBean.getClassName());
		
		FeesTransaction feesTransaction = new FeesTransaction();
		FeeStructure feeStructureList = feesTransaction.getFeeStructurelist(regId, Integer.parseInt(feeStructId));
		responce.setFeeStructure(feeStructureList);
		
		BatchStudentFees batchStudentFees  = feesTransaction.getStudentsTransactionForPrint(regId, div_id, batchId, studentId);
		responce.setBatchStudentFees(batchStudentFees);
		return Response.status(Status.OK).entity(responce).build();
	}
	
	@GET
	@Path("/feeReceipt/{divId}/{batchId}/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllFeeReceiptsData(@PathParam("divId")int div_id,
			@PathParam("batchId")int batchId,
			@PathParam("studentId")int studentId){
		PrintFeesDb printFeesDb = new PrintFeesDb();
		List<PrintFees> printFees = printFeesDb.getFeesDetails(studentId, getRegId());
		return Response.accepted(printFees).build();
	}
	
	@GET
	@Path("/feeReceipt/{receiptId}")
	@Produces(MediaType.TEXT_HTML)
	public Response getReceipt(@PathParam("receiptId")int receiptId){
		String receiptPath = getUserBean().getUserStatic().getFeesReceiptPath();
		String feeReceipt = receiptPath+File.separator+receiptId+".html";
		File receipts = new File(feeReceipt);
		String printFees = null;
		
		try {
			printFees = ClassAppUtil.getFileDecryptResponce(feeReceipt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(null!=printFees){
			return Response.ok(printFees).build();
		}
		return Response.ok("<div style='color:red'><h3>Receipt not found</h3></div>").build();
	}
		
}
