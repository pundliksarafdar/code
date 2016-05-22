package com.transaction.notes;

import java.io.File;
import java.util.List;

import com.classapp.db.Notes.Notes;
import com.classapp.db.Notes.NotesDB;

public class NotesTransaction {
public Boolean addNotes(Notes notes) {
	
	NotesDB db=new NotesDB();
	db.add(notes);
	return true;
	
}

public Boolean deleteNotes(int notesid,int inst_id,int div_id,int sub_id) {
	
	NotesDB db=new NotesDB();
	db.deletenotes(notesid,inst_id,div_id,sub_id);
	return true;
	
}

/*Remove this function if not require*/
public List<Notes> getNotesPath(int divid,int subid,int classid,int currentPage,String batchids) {
	NotesDB notesDB=new NotesDB();
	List<Notes> list=notesDB.getNotesPath(divid, subid, classid,currentPage,batchids);
	return list;
	
}

public List<Notes> getNotesPath(int divid,int subid,int classid,String batchids) {
	NotesDB notesDB=new NotesDB();
	List<Notes> list=notesDB.getNotesPath(divid, subid, classid,batchids);
	return list;
	
}

public List<Notes> getNotesPath(int divid,int subid,int classid) {
	NotesDB notesDB=new NotesDB();
	List<Notes> list=notesDB.getNotesPath(divid, subid, classid);
	return list;
	
}

public int getNotescount(int divid,int subid,int classid,String batchids) {
	NotesDB notesDB=new NotesDB();
	return notesDB.getNotescount(divid, subid, classid,batchids);
	
}

public List<Notes> getStudentNotesPath(String batch,int subid,int classid,int div_id) {
	NotesDB notesDB=new NotesDB();
	List<Notes> list=notesDB.getStudentNotesPath(batch, subid, classid,div_id);
	return list;
	
}

public String getNotepathById(int id,int inst_id,int sub_id,int div_id) {
	NotesDB db=new NotesDB();
	List<Notes> notes=db.getNotesPathById(id,inst_id, sub_id,div_id);
	if(notes!=null)
	{
		return notes.get(0).getNotespath();
	}
	return null;
}

public Notes getNotesById(int id,int inst_id,int sub_id,int div_id) {
	NotesDB db=new NotesDB();
	List<Notes> notes=db.getNotesPathById(id,inst_id,sub_id,div_id);
	if(notes!=null)
	{
		return notes.get(0);
	}
	return null;
}

public boolean validatenotesname(String notesname,int inst_id,int division,int subject) {
	NotesDB db=new NotesDB();
	return db.validatenotesname(notesname, inst_id,division,subject);
}

public boolean updatenotes(String notesname,int notesid,String batchids,int inst_id,int div_id,int sub_id) {
	NotesDB db=new NotesDB();
	if(!db.validateUpdateNotesName(notesname, inst_id, notesid, div_id, sub_id)){
	db.updatenotes(notesname, notesid, batchids,inst_id,div_id,sub_id);
	}else{
		return true;
	}
	return false;
}

/*public boolean validatenotesnamebyID(String notesname,int regID,int notesID) {
	NotesDB db=new NotesDB();
	return db.validateUpdateNotesName(notesname, regID, notesID);
}*/

public boolean deleteNotesRelatedToSubject(int inst_id,int sub_id) {
	NotesDB db=new NotesDB();
	return db.deleteNotesRelatedToSubject(inst_id,sub_id);
}

public boolean deleteNotesRelatedToDivision(int inst_id,int div_d) {
	NotesDB db=new NotesDB();
	return db.deleteNotesRelatedToDivision(inst_id,div_d);
}

public boolean removebatchfromnotes(int inst_id,int div_id,String batchid) {
	NotesDB db=new NotesDB();
	return db.removebatchfromnotes(inst_id, div_id, batchid);
}
}
