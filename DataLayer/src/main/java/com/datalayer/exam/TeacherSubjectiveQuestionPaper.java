package com.datalayer.exam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;





import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

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
		ArrayList<String> listOfErrors=new ArrayList<>();
		this.invalidQuestionResponseMap= new HashMap<String,ArrayList<String>>();
		this.invalidQuestionResponseMap.put("ERROR", listOfErrors);
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
		org.apache.poi.ss.usermodel.Workbook workbook = null;

		try {

			workbook = WorkbookFactory.create(myFile);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
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
			ArrayList<String> listOfErrors= new ArrayList<String>();
			if(null==row.getCell(1) || null==row.getCell(2)){
				listOfErrors.add("Required columns values are missig for row "+row.getRowNum()+".");
				//invalidQuestionResponseMap.put(""+row.getRowNum(),listOfErrors);
			}else{
				try{
					if (row.getCell(1).getStringCellValue().length() <= 500
							&& isDigit(Double.toString(row.getCell(2).getNumericCellValue()))
							&& row.getCell(2).getNumericCellValue() > 0) {
						SubjectiveQuestion question = new SubjectiveQuestion(row.getRowNum(),row.getCell(1).getStringCellValue(),row.getCell(2).getNumericCellValue());
						System.out.println(question.toString());
						questions.add(question);
					} else {
						//ArrayList<String> listOfErrors = new ArrayList<String>();
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
						//invalidQuestionResponseMap.put(""+row.getRowNum(),listOfErrors);
					}
				} catch(IllegalStateException e){
					//ArrayList<String> listOfErrors=new ArrayList<String>();
					listOfErrors.add(e.getMessage());
					//invalidQuestionResponseMap.put(""+row.getRowNum(),listOfErrors);
				}catch(NullPointerException e){
					//ArrayList<String> listOfErrors=new ArrayList<String>();
					listOfErrors.add("No value defined for some columns. please check your excel sheet.");
					//invalidQuestionResponseMap.put(""+row.getRowNum(),listOfErrors);
					//System.out.println(listOfErrors);
				}
			}
			String errorResponseString=convertToStringResponse(listOfErrors, row.getRowNum());
			ArrayList<String> resultStringList=invalidQuestionResponseMap.get("ERROR");
			if(!errorResponseString.equals("")){
				resultStringList.add(errorResponseString);
			}
			invalidQuestionResponseMap.put("ERROR",resultStringList);
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

	private String convertToStringResponse(ArrayList<String> listOfErrors, int rownum){
		String errorResponse="";
		if(listOfErrors.size()==0){
			return errorResponse;
		}
			if(listOfErrors.size()==1){
				errorResponse=rownum+"#"+listOfErrors.get(0);
			}else{
				errorResponse=rownum+"#";
				for(String error:listOfErrors){
					errorResponse=errorResponse.concat(error+"#");
				}
				errorResponse=errorResponse.substring(0, errorResponse.length()-1);
			}
			System.out.println(errorResponse);
		return errorResponse;
	}
	public static void main(String[] args) {
		TeacherSubjectiveQuestionPaper paper = new TeacherSubjectiveQuestionPaper(
				"E:\\exam2.xls");
		paper.LoadQuestionPaper();
	}
}
