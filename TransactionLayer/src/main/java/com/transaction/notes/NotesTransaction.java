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

public List<Notes> getNotesPath(int divid,int subid,int classid) {
	NotesDB notesDB=new NotesDB();
	List<Notes> list=notesDB.getNotesPath(divid, subid, classid);
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
}
