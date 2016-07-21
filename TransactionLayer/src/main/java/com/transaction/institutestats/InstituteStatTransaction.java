package com.transaction.institutestats;

import com.classapp.db.institutestats.InstituteStats;
import com.classapp.db.institutestats.InstituteStatsDB;

public class InstituteStatTransaction {
	public void save(InstituteStats stats) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();
	instituteStatsDB.save(stats);
	}
	
	public boolean updateStudentIdLimit(int inst_id,int noOfIds) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();
	return instituteStatsDB.updateStudentIdLimit(inst_id, noOfIds);
	}
	
	public boolean updateMemoryLimit(int inst_id,double memory) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
	return instituteStatsDB.updateMemoryLimit(inst_id, memory);
	}
	
	public boolean increaseUsedStudentIds(int inst_id) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
	return instituteStatsDB.increaseUsedStudentIds(inst_id);	
	}
	
	public boolean decreaseUsedStudentIds(int inst_id) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
	return instituteStatsDB.decreaseUsedStudentIds(inst_id);		
	}
	
	public boolean increaseUsedMemory(int inst_id,double memory) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
	return instituteStatsDB.increaseUsedMemory(inst_id, memory);		
	}
	
	public boolean decreaseUsedMemory(int inst_id,double memory) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
	return instituteStatsDB.decreaseUsedMemory(inst_id, memory);	
	}
	
	public InstituteStats getStats(int inst_id) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
	return instituteStatsDB.getStats(inst_id);
	}
	
	public boolean isIDsAvailable(int inst_id,int noOfIds) {
		InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
		InstituteStats instituteStats = instituteStatsDB.getStats(inst_id);
		if(instituteStats != null){
			if(instituteStats.getAvail_ids() >= noOfIds){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
		
		}
	
	public boolean increaseUsedStudentIds(int inst_id,int noOfIds) {
		InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
		return instituteStatsDB.increaseUsedStudentIds(inst_id,noOfIds);	
		}
}
