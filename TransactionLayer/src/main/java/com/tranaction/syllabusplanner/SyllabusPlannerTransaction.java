package com.tranaction.syllabusplanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.classapp.db.syllabusplanner.SyllabusPlannerDb;
import com.classapp.utils.Constants.SYLLABUS_STATE;
import com.service.beans.SyllabusBean;
import com.service.beans.SyllabusFilterBean;

public class SyllabusPlannerTransaction {
	public boolean saveSyallabus(SyllabusBean syllabusBean){
		SyllabusPlannerDb syllabusPlannerDb = new SyllabusPlannerDb();
		return syllabusPlannerDb.saveSyllabusPlanner(syllabusBean);
	}
	
	public List<SyllabusBean> getSyllabus(String yyyymm,int instId,int classId,int subId,String batchId,int regId){
		String[] ym = yyyymm.split("-");
		String year = ym[0];
		String month = ym[1];
		System.out.println(year+":"+month);
		SyllabusPlannerDb syllabusPlannerDb = new SyllabusPlannerDb();
		List<String>batchIds = Arrays.asList(batchId.split(","));
		List<SyllabusBean> syllabusBeans = syllabusPlannerDb.getSyllabus(Integer.parseInt(year), Integer.parseInt(month),instId,classId,subId,batchIds,regId);
		syllabusBeans.stream().forEach(bean -> {
			if (bean.getDate().compareTo(new Date()) > -1 || SYLLABUS_STATE.EDITABLE.toString().equals(bean.getStatus())) {
				bean.setEditable(true);
			} else {
				bean.setEditable(false);
			}
		});
		return syllabusBeans;
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
	
	public List<SyllabusBean> getAllPlannedSyllabus(String yyyymmdd,int instId,List<Integer> classId,List<Integer> subId,List<Integer> batchId,List<Integer> teacherId){
		String[] ymd = yyyymmdd.split("-");
		String year = ymd[0];
		String month = ymd[1];
		String day = ymd[2];
		
		SyllabusPlannerDb syllabusPlannerDb = new SyllabusPlannerDb();
		return syllabusPlannerDb.getSyllabus(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), instId, classId, subId, batchId, teacherId);
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
}
