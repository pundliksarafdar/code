package com.tranaction.admintoclass;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.institutestats.InstituteStats;
import com.classapp.db.institutestats.InstituteStatsDB;

public class AdminToClassTransaction {
	public List<com.service.beans.InstituteStats> searchClassData(){
		InstituteStatsDB instituteStatsDB = new InstituteStatsDB();
		List<com.service.beans.InstituteStats> instituteStats = new ArrayList<com.service.beans.InstituteStats>();
		List<InstituteStats> list = instituteStatsDB.getAllInstStats();
		for(InstituteStats instituteStatsSrc:list){
			com.service.beans.InstituteStats instituteStatsDest = new com.service.beans.InstituteStats();
			instituteStats.add(instituteStatsDest);
			try {
				BeanUtils.copyProperties(instituteStatsDest, instituteStatsSrc);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return instituteStats;
	}
	
	public boolean updateFeatures(com.service.beans.InstituteStats instituteStats){
		boolean status = true;
		InstituteStats instituteStatsDb = new InstituteStats();
		InstituteStatsDB db = new InstituteStatsDB();
		try {
			BeanUtils.copyProperties(instituteStatsDb, instituteStats);
			db.updateLimit(instituteStatsDb);
		} catch (IllegalAccessException e) {
			status = false;
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}
}
