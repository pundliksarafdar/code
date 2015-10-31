package com.notes;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import org.apache.fontbox.cff.DataOutput;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;
import org.apache.pdfbox.util.PDFImageWriter;
import org.apache.struts2.ServletActionContext;

import com.classapp.db.Notes.Notes;
import com.classapp.db.batch.division.Division;
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.config.Constants;
/*import com.sun.xml.internal.messaging.saaj.util.Base64;*/
/*import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;*/
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.notes.NotesTransaction;
import com.user.UserBean;

public class DisplayNotesAction extends BaseAction{
	private String division;
	private String subject;
	private String batch;
	private String institute;
	private int notesid;
	private List<ByteArrayOutputStream> outputStream;
	private String base64;
	private byte[] bytearray;
	private int totalpages; 
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		NotesTransaction notesTransaction=new NotesTransaction();
		int inst_id=userBean.getRegId();
		if(!"".equals(institute) && institute!=null){
			inst_id=Integer.parseInt(institute);
		}
		String filename=notesTransaction.getNotepathById(notesid,inst_id,Integer.parseInt(subject),Integer.parseInt(division));
		UserStatic userStatic = userBean.getUserStatic();
		if(userBean.getRegId()!=null)
		{
			if(!"".equals(institute) && institute!=null){
				String storagePath = Constants.STORAGE_PATH+File.separator+institute;
				userStatic.setStorageSpace(storagePath);
			}
		String path=userStatic.getNotesPath()+File.separator+subject+File.separator+division+File.separator+filename;
		File file = new File(path);
        try {
				PDDocument document = PDDocument.loadNonSeq(new File(path), null);
				List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();
				totalpages=pdPages.size();
				int page = 0;
				PDFImageWriter imageWriter=new PDFImageWriter();
					ByteArrayOutputStream stream=new ByteArrayOutputStream();
				    BufferedImage bim = pdPages.get(0).convertToImage(BufferedImage.TYPE_INT_RGB, 100);
				    request.getSession().setAttribute("notesimage_0", bim);
				     ImageIO.write(bim, "png", stream);
				     stream.flush();
				     byte b [] = stream.toByteArray();
				   	base64= javax.xml.bind.DatatypeConverter.printBase64Binary(b);
				    	stream.close();
				document.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		Notes notes=new Notes();
		notes.setClassid(inst_id);
		notes.setSubid(Integer.parseInt(subject));
		notes.setDivid(Integer.parseInt(division));
		notes.setNotesid(notesid);
		request.getSession().setAttribute("notes", notes);
		request.setAttribute("imagefile", base64);
		return "displaynotes";
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getInstitute() {
		return institute;
	}
	public void setInstitute(String institute) {
		this.institute = institute;
	}
	public int getNotesid() {
		return notesid;
	}
	public void setNotesid(int notesid) {
		this.notesid = notesid;
	}
	public String getBase64() {
		return base64;
	}
	public void setBase64(String base64) {
		this.base64 = base64;
	}
	public byte[] getBytearray() {
		return bytearray;
	}
	public void setBytearray(byte[] bytearray) {
		this.bytearray = bytearray;
	}
	public int getTotalpages() {
		return totalpages;
	}
	public void setTotalpages(int totalpages) {
		this.totalpages = totalpages;
	}
	
	
}
