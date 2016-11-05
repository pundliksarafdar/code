package com.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.JsonObject;
import com.service.beans.SyllabusBean;
import com.service.beans.SyllabusFilterBean;
import com.serviceinterface.SyllabusRestApi;
import com.tranaction.syllabusplanner.SyllabusPlannerTransaction;

@Path("/syllabus")
@Produces(MediaType.APPLICATION_JSON)
public class SyllabusRestImpl  extends ServiceBase implements SyllabusRestApi{

	@Override
	@GET
	@Path("/test")
	public String serviceOn() {
		return "Service is running";
	}

	@Override
	@GET
	@Path("/{yyyymm}")
	public Response getPlannedSyllabusForMonth(@PathParam("yyyymm") String yyyymm,
			@QueryParam("instId")int instId,
			@QueryParam("classId")int classId,
			@QueryParam("subjectId")int subId,
			@QueryParam("batchId")String batchId) {
		SyllabusPlannerTransaction transaction = new SyllabusPlannerTransaction();
		List<SyllabusBean> syllabusBeans = transaction.getSyllabus(yyyymm,instId,classId,subId,batchId,getRegId());
		return Response.ok(syllabusBeans).build();
	}

	@Override
	public Response savePlannedSyllabus(SyllabusBean syllabusBean) {
		SyllabusPlannerTransaction transaction = new SyllabusPlannerTransaction();
		syllabusBean.setTeacherId(getRegId());
		boolean status = transaction.saveSyallabus(syllabusBean);
		if(status){
			return Response.ok().build();
		}else{
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
	}

	@Override
	@PUT
	public Response editPlannedSyllabus(SyllabusBean syllabusBean) {
		SyllabusPlannerTransaction transaction = new SyllabusPlannerTransaction();
		boolean status = transaction.editSyllabus(syllabusBean.getId(),syllabusBean.getInstId(), syllabusBean.getClassId(), 
				syllabusBean.getSubjectId(), getRegId(), syllabusBean.getSyllabus(), 
				syllabusBean.getDate(),syllabusBean.getTeacherStatus());
		if(status){
			return Response.ok().build();
		}else{
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@Override
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePlannedSyllabus(SyllabusBean syllabusBean) {
		SyllabusPlannerTransaction transaction = new SyllabusPlannerTransaction();
		transaction.deleteSyllabus(syllabusBean);
		return null;
	}

	
	@GET
	@Path("view/{yyyymmdd}")
	public Response getAllPlannedSyllabusForMonth(@PathParam("yyyymmdd") String yyyymmdd,
			@QueryParam("division")List<Integer> classId,
			@QueryParam("subject")List<Integer> subId,
			@QueryParam("batchId")List<Integer> batchId,
			@QueryParam("teacher")List<Integer> teacherId,
			@QueryParam("view")String view) {
		SyllabusPlannerTransaction transaction = new SyllabusPlannerTransaction();
		List<SyllabusBean> syllabusBeans = transaction.getAllPlannedSyllabus(yyyymmdd, getRegId(), classId, subId, batchId, teacherId,view);
		return Response.ok(syllabusBeans).build();
	}
	
	@GET
	@Path("changeStatus/{yyyymmdd}")
	public Response setPlannedSyallbusStatus(@PathParam("yyyymmdd") String yyyymmdd,
			@QueryParam("id")Long id,
			@QueryParam("classId")int classId,
			@QueryParam("subjectId")int subId,
			@QueryParam("batchId")int batchId,
			@QueryParam("teacherId")int teacherId,
			@QueryParam("status")String status) {
		SyllabusPlannerTransaction transaction = new SyllabusPlannerTransaction();
		boolean res = transaction.setPlannedSyllabusStatus(yyyymmdd, id, getRegId(), classId, subId, batchId, teacherId, status);
		return Response.ok(res).build();
	}
	
	@GET
	@Path("getFilterResult")
	public Response getFilteredResult(){
		SyllabusPlannerTransaction syllabusPlannerTransaction = new SyllabusPlannerTransaction();
		HashMap<String, List<SyllabusFilterBean>> resultMap = syllabusPlannerTransaction.getSyllabusFilter(getRegId());
		return Response.ok(resultMap).build();
	}
	
	@GET
	@Path("print/{yyyymmdd}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response printSyllabus(@PathParam("yyyymmdd") String yyyymmdd,
			@QueryParam("division")List<Integer> classId,
			@QueryParam("subject")List<Integer> subId,
			@QueryParam("batchId")List<Integer> batchId,
			@QueryParam("teacher")List<Integer> teacherId,
			@QueryParam("view")String view) throws IOException {
		SyllabusPlannerTransaction syllabusPlannerTransaction = new SyllabusPlannerTransaction();
		XSSFWorkbook workbook = syllabusPlannerTransaction.getPrintXSSFWorkbook(yyyymmdd, getRegId(), classId, subId, batchId, teacherId,view);
		StreamingOutput fileDownload = new StreamingOutput(){
		    @Override
		        public void write(OutputStream arg0) throws IOException, WebApplicationException {
		    	BufferedOutputStream bus = new BufferedOutputStream(arg0);
		            try {
		                workbook.write(bus);
		            } catch (Exception e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		            }
		        }
		    };
	   return Response.ok(fileDownload).header("Content-Disposition", "attachment; filename=\"" + "syllabus.xlsx" + "\"" ).build();
	}
}
