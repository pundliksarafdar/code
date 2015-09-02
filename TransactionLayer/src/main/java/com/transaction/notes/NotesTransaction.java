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

public Boolean deleteNotes(int notesid) {
	
	NotesDB db=new NotesDB();
	db.deletenotes(notesid);
	return true;
	
}

public List<Notes> getNotesPath(int divid,int subid,int classid) {
	NotesDB notesDB=new NotesDB();
	List<Notes> list=notesDB.getNotesPath(divid, subid, classid);
	return list;
	
}

public List<Notes> getStudentNotesPath(String batch,int subid,int classid) {
	NotesDB notesDB=new NotesDB();
	List<Notes> list=notesDB.getStudentNotesPath(batch, subid, classid);
	return list;
	
}

public String getNotepathById(int id) {
	NotesDB db=new NotesDB();
	List<Notes> notes=db.getNotesPathById(id);
	if(notes!=null)
	{
		return notes.get(0).getNotespath();
	}
	return null;
}

public Notes getNotesById(int id) {
	NotesDB db=new NotesDB();
	List<Notes> notes=db.getNotesPathById(id);
	if(notes!=null)
	{
		return notes.get(0);
	}
	return null;
}

public boolean validatenotesname(String notesname,int regID) {
	NotesDB db=new NotesDB();
	return db.validatenotesname(notesname, regID);
}

public void updatenotes(String notesname,int notesid,String batchids) {
	NotesDB db=new NotesDB();
	db.updatenotes(notesname, notesid, batchids);
}

public boolean validatenotesnamebyID(String notesname,int regID,int notesID) {
	NotesDB db=new NotesDB();
	return db.validatenotesnamebyID(notesname, regID, notesID);
}
}
