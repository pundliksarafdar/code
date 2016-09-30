package com.transaction.instroll;

import java.util.List;

import com.classapp.db.roll.InstRollDB;
import com.classapp.db.roll.Inst_roll;

public class InstRollTransaction {
	public boolean saveInstRoll(Inst_roll inst_roll) {
		InstRollDB db = new InstRollDB();
		if(db.validateRoll(inst_roll)){
		return db.saveInstRoll(inst_roll);
		}
		return false;
	}
	
	public List<Inst_roll> getInstituteRoles(int inst_id) {
		InstRollDB db = new InstRollDB();
		return db.getInstituteRoles(inst_id);
	}
	
	public Inst_roll getRole(int inst_id,int roll_id) {
		InstRollDB db = new InstRollDB();
		return db.getRole(inst_id,roll_id);
	}
	
	public boolean updateInstRoll(Inst_roll inst_roll) {
		InstRollDB db = new InstRollDB();
		if(db.validateUpdateRoll(inst_roll)){
		return db.updateRoll(inst_roll);
		}
		return false;
	}
}
