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
 * @author amit_meshram
 *
 */

public class MCQuestionPaper {
	
	private String fileName;
	private ArrayList<Question> questions;
	private HashMap<String,ArrayList<String>> invalidQuestionResponseMap;
	
	public String getFileName() {
		return fileName;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	public HashMap<String, ArrayList<String>> getInvalidQuestionResponseMap() {
		return invalidQuestionResponseMap;
	}

	/**
	 * Initialize question paper
	 * @param fileName is a parameter having complete path of .xls file
	 * 
	 */
	public MCQuestionPaper(String fileName) {
		this.fileName = fileName;
		this.questions = new ArrayList<Question>();
		this.invalidQuestionResponseMap= new HashMap<String,ArrayList<String>>();
	}

	/**
	 * This method is to load questions present in .xsl file. It generates list of questions as a output.
	 */
	public void LoadQuestionPaper() {

		File myFile = new File(fileName);
		POIFSFileSystem pfs = null;
		FileInputStream fis;

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
			//skip reading of header row
		}
		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			
			String correctAnswerStr="";
			if(row.getCell(2).getCellType()==0){
				correctAnswerStr=((int)row.getCell(2).getNumericCellValue())+"";
			}else{
				correctAnswerStr=row.getCell(2).getStringCellValue();
			}
			
			try{
				if(row.getCell(1).getStringCellValue().toString().length()<=500 
						&& correctAnswerStr.length() <=255 
						&& isValidCorrectAnswer(correctAnswerStr.toString())
						&& isDigit(Double.toString(row.getCell(13).getNumericCellValue()))
						&& row.getCell(13).getNumericCellValue()>0 
						&& row.getCell(3).getStringCellValue().length()<=100
						&& row.getCell(4).getStringCellValue().length()<=100
						&& row.getCell(5).getStringCellValue().length()<=100
						&& row.getCell(6).getStringCellValue().length()<=100
						&& row.getCell(7).getStringCellValue().length()<=100
						&& row.getCell(8).getStringCellValue().length()<=100
						&& row.getCell(9).getStringCellValue().length()<=100
						&& row.getCell(10).getStringCellValue().length()<=100
						&& row.getCell(11).getStringCellValue().length()<=100
						&& row.getCell(12).getStringCellValue().length()<=100){
					MCQuestion question = new MCQuestion(row.getRowNum(),correctAnswerStr
					, row.getCell(1).getStringCellValue(), row.getCell(13).getNumericCellValue());
		
					HashMap<String, String> optionMap = question.getOptions();
					optionMap.put("1", row.getCell(3).getStringCellValue());
					optionMap.put("2", row.getCell(4).getStringCellValue());
					optionMap.put("3", row.getCell(5).getStringCellValue());
					optionMap.put("4", row.getCell(6).getStringCellValue());
					optionMap.put("5", row.getCell(7).getStringCellValue());
					optionMap.put("6", row.getCell(8).getStringCellValue());
					optionMap.put("7", row.getCell(9).getStringCellValue());
					optionMap.put("8", row.getCell(10).getStringCellValue());
					optionMap.put("9", row.getCell(11).getStringCellValue());
					optionMap.put("10", row.getCell(12).getStringCellValue());
					
					System.out.println(question.toString());
					questions.add(question);
				}else{
					ArrayList<String> listOfErrors= new ArrayList<String>();
					
					if(row.getCell(1).getStringCellValue().toString().length()>500){
						listOfErrors.add("Question text is greater than 500 words. Current legth is "+row.getCell(1).getStringCellValue().toString().length());
					}
					if(correctAnswerStr.length() >255 ){
						listOfErrors.add("Correct answer text is greater than 255 words. Current legth is "+row.getCell(2).getStringCellValue().toString().length());
					}
					if(!(isValidCorrectAnswer(correctAnswerStr))){
						listOfErrors.add("Correct answer contains invalid options. only comma separated option numbers 1 to 10 are valid options.");
					}
					if(!(isDigit(Double.toString(row.getCell(13).getNumericCellValue())))){
						listOfErrors.add("Invalid marks value. Remove non numeric value.");
					}else{
						if(row.getCell(13).getNumericCellValue()<0 ){
							listOfErrors.add("Invalid marks value.");
						}
					}
					if(row.getCell(3).getStringCellValue().length()>100){
						listOfErrors.add("Option1 text is greater than 100 words. Current legth is "+row.getCell(3).getStringCellValue().length());
					}
					if(row.getCell(4).getStringCellValue().length()>100){
						listOfErrors.add("Option2 text is greater than 100 words. Current length is "+row.getCell(4).getStringCellValue().length());
					}
					if(row.getCell(5).getStringCellValue().length()>100){
						listOfErrors.add("Option3 text is greater than 100 words. Current length is "+row.getCell(5).getStringCellValue().length());
					}
					if(row.getCell(6).getStringCellValue().length()>100){
						listOfErrors.add("Option4 text is greater than 100 words. Current length is "+row.getCell(6).getStringCellValue().length());
					}
					if(row.getCell(7).getStringCellValue().length()>100){
						listOfErrors.add("Option5 text is greater than 100 words. Current length is "+row.getCell(7).getStringCellValue().length());
					}
					if(row.getCell(8).getStringCellValue().length()>100){
						listOfErrors.add("Option6 text is greater than 100 words. Current length is "+row.getCell(8).getStringCellValue().length());
					}
					if(row.getCell(9).getStringCellValue().length()>100){
						listOfErrors.add("Option7 text is greater than 100 words. Current length is "+row.getCell(9).getStringCellValue().length());
					}
					if(row.getCell(10).getStringCellValue().length()>100){
						listOfErrors.add("Option8 text is greater than 100 words. Current length is "+row.getCell(10).getStringCellValue().length());
					}
					if(row.getCell(11).getStringCellValue().length()>100){
						listOfErrors.add("Option9 text is greater than 100 words. Current length is "+row.getCell(11).getStringCellValue().length());
					}
					if(row.getCell(12).getStringCellValue().length()>100){
						listOfErrors.add("Option10 text is greater than 100 words. Current length is "+row.getCell(12).getStringCellValue().length());
					}				
					invalidQuestionResponseMap.put(""+row.getRowNum(),listOfErrors);
					System.out.println(listOfErrors);
				}
			}catch(IllegalStateException e){
				ArrayList<String> listOfErrors=new ArrayList<String>();
				listOfErrors.add(e.getMessage());
				invalidQuestionResponseMap.put(""+row.getRowNum(),listOfErrors);
				System.out.println(listOfErrors);
			}catch(NullPointerException e){
				ArrayList<String> listOfErrors=new ArrayList<String>();
				listOfErrors.add(e.getMessage());
				invalidQuestionResponseMap.put(""+row.getRowNum(),listOfErrors);
				System.out.println(listOfErrors);
			}
			
		}
		
		System.out.println("Number of questions:"+questions.size());
	}
	
	public boolean isValidCorrectAnswer(String correctAnswer){
		boolean result=true;
		for (String optionNumber : correctAnswer.split(",")) {			
			if(!optionNumber.matches("[1-9]|10")){
				result=false;
				break;
			}
		}
		return result;
	}	
	
	private boolean isDigit(String number){
		boolean result=true;		
		if(!number.matches("^\\d+(\\.\\d+)?")){
			result=false;			
		}		
		return result;
	}
	public static void main(String[] args) {
		MCQuestionPaper paper= new MCQuestionPaper("E:\\exam1.xls");
		paper.LoadQuestionPaper();
		
		//System.out.println("Is valid :"+isValidCorrectAnswer("1,10"));
	}
}
