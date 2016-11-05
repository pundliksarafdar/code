package com.tranaction.syllabusplanner;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.classapp.db.syllabusplanner.SyllabusPlannerDb;
import com.classapp.utils.Constants.SYLLABUS_STATE;
import com.service.beans.SyllabusBean;
import com.service.beans.SyllabusFilterBean;

public class SyllabusPlannerTransaction {
	public boolean saveSyallabus(SyllabusBean syllabusBean){
		SyllabusPlannerDb syllabusPlannerDb = new SyllabusPlannerDb();
		if(syllabusBean.getDate().before(new Date())){
			return false;
		}
		return syllabusPlannerDb.saveSyllabusPlanner(syllabusBean);
	}
	
	public List<SyllabusBean> getSyllabus(String yyyymm,int instId,int classId,int subId,String batchId,int regId){
		String[] ym = yyyymm.split("-");
		String year = ym[0];
		String month = ym[1];
		System.out.println(year+":"+month);
		SyllabusPlannerDb syllabusPlannerDb = new SyllabusPlannerDb();
		List<String>batchIds = Arrays.asList(batchId.split(","));
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-mm-dd");
		Date todayDateOnly = getTodaysDate();
		
		List<SyllabusBean> syllabusBeans = syllabusPlannerDb.getSyllabus(Integer.parseInt(year), Integer.parseInt(month),instId,classId,subId,batchIds,regId);
		syllabusBeans.stream().forEach(bean -> {
			if (bean.getDate().compareTo(todayDateOnly)>-1 || SYLLABUS_STATE.EDITABLE.toString().equalsIgnoreCase(bean.getStatus())) {
				bean.setEditable(true);
			} else {
				bean.setEditable(false);
			}
		});
		return syllabusBeans;
	}
	
	public Date getTodaysDate(){
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime( date );
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar.getTime();
	}
	
	public boolean editSyllabus(long id,int instId,int classId,int subId,int regId,String syllabus,Date date,String teacherStatus){
		SyllabusPlannerDb syllabusPlannerDb = new SyllabusPlannerDb();
		return syllabusPlannerDb.updateSyllabus(id, instId, classId, subId, regId, syllabus, date,teacherStatus);
	}
	
	public boolean deleteSyllabus(long id,int instId,int classId,int subId,int regId){
		SyllabusPlannerDb syllabusPlannerDb = new SyllabusPlannerDb();
		return syllabusPlannerDb.deleteSyllabus(id, instId, classId, subId, regId);
	}
	
	public boolean deleteSyllabus(SyllabusBean syllabusBean){
		SyllabusPlannerDb syllabusPlannerDb = new SyllabusPlannerDb();
		return syllabusPlannerDb.deleteSyllabus(syllabusBean);
	}
	
	public List<SyllabusBean> getAllPlannedSyllabus(String yyyymmdd,int instId,List<Integer> classId,List<Integer> subId,List<Integer> batchId,List<Integer> teacherId,String view){
		String[] ymd = yyyymmdd.split("-");
		String year = ymd[0];
		String month = ymd[1];
		String day = ymd[2];
		
		SyllabusPlannerDb syllabusPlannerDb = new SyllabusPlannerDb();
		return syllabusPlannerDb.getSyllabus(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), instId, classId, subId, batchId, teacherId,view);
	}
	
	public boolean setPlannedSyllabusStatus(String yyyymmdd,Long id,int instId,int classId,int subId,int batchId,int teacherId,String status){
		String[] ymd = yyyymmdd.split("-");
		String year = ymd[0];
		String month = ymd[1];
		String day = ymd[2];
		
		SyllabusPlannerDb syllabusPlannerDb = new SyllabusPlannerDb();
		SyllabusBean syllabusBean = new SyllabusBean();
		syllabusBean.setClassId(classId);
		syllabusBean.setId(id);
		syllabusBean.setInstId(instId);
		syllabusBean.setStatus(status);
		syllabusBean.setSubjectId(subId);
		syllabusBean.setTeacherId(teacherId);
		syllabusPlannerDb.setStatus(syllabusBean);
		return true;
	}
	
	public HashMap<String, List<SyllabusFilterBean>> getSyllabusFilter(int instituteId){
		HashMap<String, List>filterMap = new HashMap<>();
		SyllabusPlannerDb plannerDb = new SyllabusPlannerDb();
		List<SyllabusFilterBean> syllabusFilterBeansDb = plannerDb.getSyllabusDb(instituteId);
		
		HashMap<String, List<SyllabusFilterBean>> hashMap = new HashMap<String, List<SyllabusFilterBean>>();
		
		for(SyllabusFilterBean bean:syllabusFilterBeansDb){
			List<SyllabusFilterBean> syllabusFilterBeans = hashMap.get(bean.getType());
			if(null==syllabusFilterBeans){
				List<SyllabusFilterBean> syllabusFilterBeansList = new ArrayList<SyllabusFilterBean>();
				syllabusFilterBeansList.add(bean);
				hashMap.put(bean.getType(),syllabusFilterBeansList);
			}else{
				hashMap.get(bean.getType()).add(bean);
			}
			
		}
		return hashMap;
	}
	
	public XSSFWorkbook getPrintXSSFWorkbook(String yyyymmdd,int instId,List<Integer> classId,List<Integer> subId,List<Integer> batchId,List<Integer> teacherId,String view){

		SyllabusPlannerTransaction syllabusPlannerTransaction = new SyllabusPlannerTransaction();
		/***********************************/
		HashMap<String, List<SyllabusFilterBean>> resultMap = syllabusPlannerTransaction.getSyllabusFilter(instId);
		HashMap<Integer,String> teacherName = new HashMap<Integer,String>();
		HashMap<Integer,String> subjectName = new HashMap<Integer,String>();
		HashMap<Integer,String> divisionName = new HashMap<Integer,String>();
		HashMap<String,String> batchName = new HashMap<String,String>();
		
		List<SyllabusFilterBean> teachersList = resultMap.get("teacher");
		for(SyllabusFilterBean teacher:teachersList){
			teacherName.put(teacher.getId(), teacher.getName());
		}
		
		List<SyllabusFilterBean> batchList = resultMap.get("batch");
		for(SyllabusFilterBean batch:batchList){
			batchName.put(batch.getId()+"", batch.getName());
		}
		
		List<SyllabusFilterBean> subjectList = resultMap.get("subject");
		for(SyllabusFilterBean subject:subjectList){
			subjectName.put(subject.getId(), subject.getName());
		}
		
		List<SyllabusFilterBean> divisionList = resultMap.get("division");
		for(SyllabusFilterBean division:divisionList){
			divisionName.put(division.getId(), division.getName());
		}
		
		/*************************************************************************************************************/
		SyllabusPlannerTransaction transaction = new SyllabusPlannerTransaction();
		List<SyllabusBean> syllabusBeans = transaction.getAllPlannedSyllabus(yyyymmdd, instId, classId, subId, batchId, teacherId,view);
		XSSFWorkbook workbook = new XSSFWorkbook(); 
	      //Create a blank sheet
	    XSSFSheet spreadsheet = workbook.createSheet(" Employee Info ");
	    XSSFRow row;
	    
	    row = spreadsheet.createRow(0);
	    row.createCell(0).setCellValue("Date");
    	row.createCell(1).setCellValue("Teacher");
    	row.createCell(2).setCellValue("Subject");
    	row.createCell(3).setCellValue("Division");
    	row.createCell(4).setCellValue("Batch");
    	row.createCell(5).setCellValue("Syllabus");
    	row.createCell(6).setCellValue("Teacher remark");
    	row.createCell(7).setCellValue("Status");
    	
	    int rowid = 1;
	    
	    SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MMM-dd");
	    for(SyllabusBean syllabusBean:syllabusBeans){
	    	row = spreadsheet.createRow(rowid++);
	    	row.createCell(0).setCellValue(dateFormat.format(syllabusBean.getDate()));
	    	row.createCell(1).setCellValue(teacherName.get(syllabusBean.getTeacherId()));
	    	row.createCell(2).setCellValue(subjectName.get(syllabusBean.getSubjectId()));
	    	row.createCell(3).setCellValue(divisionName.get(syllabusBean.getClassId()));
	    	row.createCell(4).setCellValue(batchName.get(syllabusBean.getBatchId()));
	    	row.createCell(5).setCellValue(syllabusBean.getSyllabus());
	    	row.createCell(6).setCellValue(syllabusBean.getTeacherStatus());
	    	row.createCell(7).setCellValue(syllabusBean.getStatus());
	    }
	    return workbook;
	}
}
