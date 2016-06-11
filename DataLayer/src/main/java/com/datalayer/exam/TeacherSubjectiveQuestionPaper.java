package com.datalayer.exam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * A class to load all Question paper.
 * 
 * @author amit_meshram
 *
 */

public class TeacherSubjectiveQuestionPaper {
	private String fileName;
	private ArrayList<Question> questions;
	private HashMap<String, ArrayList<String>> invalidQuestionResponseMap;

	/**
	 * Initialize question paper
	 * 
	 * @param fileName
	 *            is a parameter having complete path of .xls file
	 * 
	 */
	public TeacherSubjectiveQuestionPaper(String fileName) {
		this.fileName = fileName;
		this.questions = new ArrayList<Question>();
		this.invalidQuestionResponseMap = new HashMap<String, ArrayList<String>>();
	}
	

	public String getFileName() {
		return fileName;
	}

	public HashMap<String, ArrayList<String>> getInvalidQuestionResponseMap() {
		return invalidQuestionResponseMap;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}


	/**
	 * This method is to load questions present in .xsl file. It generates list
	 * of questions as a output.
	 */
	public void LoadQuestionPaper() {

		File myFile = new File(fileName);
		POIFSFileSystem pfs = null;
		FileInputStream fis = null;
		org.apache.poi.ss.usermodel.Workbook workbook = null;

		try {

			fis = new FileInputStream(myFile);
			pfs = new POIFSFileSystem(fis);
			// Finds the workbook instance for XLSX file
			workbook = WorkbookFactory.create(pfs);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Return first sheet from the XLSX workbook
		// XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();

		if (rowIterator.hasNext()) {
			Row rowHeader = rowIterator.next();
			//skip reading of headers
		}
		
		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			try{
				if (row.getCell(1).getStringCellValue().length() <= 500
						&& isDigit(Double.toString(row.getCell(2).getNumericCellValue()))
						&& row.getCell(2).getNumericCellValue() > 0) {
					SubjectiveQuestion question = new SubjectiveQuestion(row.getRowNum(),row.getCell(1).getStringCellValue(),row.getCell(2).getNumericCellValue());
					System.out.println(question.toString());
					questions.add(question);
				} else {
					ArrayList<String> listOfErrors = new ArrayList<String>();
					if (row.getCell(1).getStringCellValue().toString().length() > 500) {
						listOfErrors.add("Question text is greater than 500 words.");
					}
					if (!isDigit(Double.toString(row.getCell(2).getNumericCellValue()))) {
						listOfErrors.add("Invalid marks value. Remove non numeric value.");
					} else {
						if (row.getCell(2).getNumericCellValue() < 0) {
							listOfErrors.add("Invalid marks value.");
						}
					}
					invalidQuestionResponseMap.put(""+row.getRowNum(),listOfErrors);
				}
			} catch(IllegalStateException e){
				ArrayList<String> listOfErrors=new ArrayList<String>();
				listOfErrors.add(e.getMessage());
				invalidQuestionResponseMap.put(""+row.getRowNum(),listOfErrors);
			}catch(NullPointerException e){
				ArrayList<String> listOfErrors=new ArrayList<String>();
				listOfErrors.add(e.getMessage());
				invalidQuestionResponseMap.put(""+row.getRowNum(),listOfErrors);
				System.out.println(listOfErrors);
			}
		}
		System.out.println("Number of questions:" + questions.size());
	}

	private boolean isDigit(String number) {
		boolean result = true;
		if (!number.matches("^\\d+(\\.\\d+)?")) {
			result = false;
		}
		return result;
	}

	public static void main(String[] args) {
		TeacherSubjectiveQuestionPaper paper = new TeacherSubjectiveQuestionPaper(
				"E:\\exam2.xls");
		paper.LoadQuestionPaper();
	}
}
