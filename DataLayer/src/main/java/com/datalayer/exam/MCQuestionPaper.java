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

import com.classapp.logger.AppLogger;

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
		ArrayList<String> listOfErrors=new ArrayList<>();
		this.invalidQuestionResponseMap= new HashMap<String,ArrayList<String>>();
		this.invalidQuestionResponseMap.put("ERROR", listOfErrors);
	}

	/**
	 * This method is to load questions present in .xsl file. It generates list of questions as a output.
	 */
	public void LoadQuestionPaper() {

		File myFile = new File(fileName);
		org.apache.poi.ss.usermodel.Workbook workbook = null;
		try {

			workbook = WorkbookFactory.create(myFile);

		} catch (FileNotFoundException e) {
			AppLogger.logError(e);
			
		} catch (IOException e) {
			AppLogger.logError(e);
		}catch (EncryptedDocumentException e) {
			AppLogger.logError(e);
		} catch (InvalidFormatException e) {
			AppLogger.logError(e);
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
			
			ArrayList<String> listOfErrors= new ArrayList<String>();
			if(null==row.getCell(1) || null==row.getCell(2) ||null==row.getCell(13)){
				listOfErrors.add("Required columns values are missig for row "+row.getRowNum()+".");
			}
			else{
			
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
							&& row.getCell(13).getNumericCellValue()>0){
								
						String updatedCorrectOptions="";		
						
						for(String option:correctAnswerStr.split(",")){
							updatedCorrectOptions=updatedCorrectOptions.concat((Integer.parseInt(option)-1)+"");
						}
						
						MCQuestion question = new MCQuestion(row.getRowNum(),updatedCorrectOptions
						, row.getCell(1).getStringCellValue(), row.getCell(13).getNumericCellValue());
			
						
						HashMap<String, String> optionMap = question.getOptions();
						boolean isMinimumTwoOptionsSet=false;
						boolean[] optionArray={false,false,false,false,false,false,false,false,false,false};
						if(null!=row.getCell(3) && row.getCell(3).getStringCellValue().toString().length()<=100){
							optionMap.put("1", row.getCell(3).getStringCellValue());	
							optionArray[0]=true;
							if(null!=row.getCell(4) && row.getCell(4).getStringCellValue().toString().length()<=100 ){
								optionMap.put("2", row.getCell(4).getStringCellValue());
								optionArray[1]=true;
								isMinimumTwoOptionsSet=true;
							}
							else{
								listOfErrors.add("Either option no. 2 is empty or Option text is greater than 100 words. Remaining options will not be set.");
							}
						}else{
							listOfErrors.add("Either option no. 1 is empty or Option text is greater than 100 words. Remaining options will not be set.");
						
						}
						if(isMinimumTwoOptionsSet){
							if(null!=row.getCell(5) && row.getCell(5).getStringCellValue().toString().length()<=100){
										optionMap.put("3", row.getCell(5).getStringCellValue());
										optionArray[2]=true;
							}						
							
							if(null!=row.getCell(6) && row.getCell(6).getStringCellValue().toString().length()<=100){	
								optionMap.put("4", row.getCell(6).getStringCellValue());
								optionArray[3]=true;
							}
							if(null!=row.getCell(7) && row.getCell(7).getStringCellValue().toString().length()<=100){
								optionMap.put("5", row.getCell(7).getStringCellValue());
								optionArray[4]=true;
							}
							if(null!=row.getCell(8) && row.getCell(8).getStringCellValue().toString().length()<=100){
								optionMap.put("6", row.getCell(8).getStringCellValue());
								optionArray[5]=true;
							}
							if(null!=row.getCell(9) && row.getCell(9).getStringCellValue().toString().length()<=100){
								optionMap.put("7", row.getCell(9).getStringCellValue());
								optionArray[6]=true;
							}
							if(null!=row.getCell(10) && row.getCell(10).getStringCellValue().toString().length()<=100){	
								optionMap.put("8", row.getCell(10).getStringCellValue());
								optionArray[7]=true;
							}
							if(null!=row.getCell(11) && row.getCell(11).getStringCellValue().toString().length()<=100){	
								optionMap.put("9", row.getCell(11).getStringCellValue());
								optionArray[8]=true;
							}
							if(null!=row.getCell(12) && row.getCell(12).getStringCellValue().toString().length()<=100){	
								optionMap.put("10", row.getCell(12).getStringCellValue());
								optionArray[9]=true;
							}
							
							
							for(int j=optionArray.length-1;j>=0;j--){
								if(!optionArray[j]){
									continue;
								}else{
									if(j>0 && !optionArray[j-1]){
										listOfErrors.add("Invalid options. Options must be set sequentially.");
										break;
									}
								}
							}
						}
						
						if(!correctAnswerStr.equals("")){
							String[] correctOptions=correctAnswerStr.split(",");
							boolean isValidCorrectOptions=true;
							for (String correctOptionStr : correctOptions) {								
								try{
									int correctOption=Integer.parseInt(correctOptionStr);
									if(correctOption<=optionMap.size() && optionArray[correctOption-1]){
										continue;
									}else{
										isValidCorrectOptions=false;
										listOfErrors.add("Invalid value in column 'Correct Option'. Correct Option number does not match with available options.");
										break;
									}
								} catch(NumberFormatException e){
									listOfErrors.add("Invalid value in column 'Correct Option'.");
								}
							}
							if(isValidCorrectOptions && isMinimumTwoOptionsSet){
							//	System.out.println(question.toString());
								questions.add(question);
							}
							
							if(!isMinimumTwoOptionsSet){
								listOfErrors.add("Atleast 2 options are compulsory. Please set atleast two valid options first.");
							}
						}						
					}else{
											
						if(null!=row.getCell(1) && row.getCell(1).getStringCellValue().toString().length()>500){
							listOfErrors.add("Question text is greater than 500 words. Current legth is "+row.getCell(1).getStringCellValue().toString().length());
						}
						if(correctAnswerStr.length() >255 ){
							listOfErrors.add("Correct answer text is greater than 255 words. Current legth is "+row.getCell(2).getStringCellValue().toString().length());
						}
						if(!(isValidCorrectAnswer(correctAnswerStr))){
							listOfErrors.add("Correct answer contains invalid options. only comma separated option numbers 1 to 10 are valid options.");
						}
						if(null!=row.getCell(13) && !(isDigit(Double.toString(row.getCell(13).getNumericCellValue())))){
							listOfErrors.add("Invalid marks value. Remove non numeric value.");
						}else{
							if(null!=row.getCell(13) && row.getCell(13).getNumericCellValue()<0 ){
								listOfErrors.add("Invalid marks value.");
							}
						}
						if(null!=row.getCell(3) && row.getCell(3).getStringCellValue().length()>100){
							listOfErrors.add("Option1 text is greater than 100 words. Current legth is "+row.getCell(3).getStringCellValue().length());
						}
						if(null!=row.getCell(4) && row.getCell(4).getStringCellValue().length()>100){
							listOfErrors.add("Option2 text is greater than 100 words. Current length is "+row.getCell(4).getStringCellValue().length());
						}
						if(null!=row.getCell(5) && row.getCell(5).getStringCellValue().length()>100){
							listOfErrors.add("Option3 text is greater than 100 words. Current length is "+row.getCell(5).getStringCellValue().length());
						}
						if(null!=row.getCell(6) && row.getCell(6).getStringCellValue().length()>100){
							listOfErrors.add("Option4 text is greater than 100 words. Current length is "+row.getCell(6).getStringCellValue().length());
						}
						if(null!=row.getCell(7) && row.getCell(7).getStringCellValue().length()>100){
							listOfErrors.add("Option5 text is greater than 100 words. Current length is "+row.getCell(7).getStringCellValue().length());
						}
						if(null!=row.getCell(8) && row.getCell(8).getStringCellValue().length()>100){
							listOfErrors.add("Option6 text is greater than 100 words. Current length is "+row.getCell(8).getStringCellValue().length());
						}
						if(null!=row.getCell(9) && row.getCell(9).getStringCellValue().length()>100){
							listOfErrors.add("Option7 text is greater than 100 words. Current length is "+row.getCell(9).getStringCellValue().length());
						}
						if(null!=row.getCell(10) && row.getCell(10).getStringCellValue().length()>100){
							listOfErrors.add("Option8 text is greater than 100 words. Current length is "+row.getCell(10).getStringCellValue().length());
						}
						if(null!=row.getCell(11) && row.getCell(11).getStringCellValue().length()>100){
							listOfErrors.add("Option9 text is greater than 100 words. Current length is "+row.getCell(11).getStringCellValue().length());
						}
						if(null!=row.getCell(12) && row.getCell(12).getStringCellValue().length()>100){
							listOfErrors.add("Option10 text is greater than 100 words. Current length is "+row.getCell(12).getStringCellValue().length());
						}	
					}
				}catch(IllegalStateException e){
					listOfErrors.add(e.getMessage());
				}catch(NullPointerException e){
					listOfErrors.add("No value defined for some columns. please check your excel sheet.");
				}
					
			}
			String errorResponseString=convertToStringResponse(listOfErrors, row.getRowNum());
			ArrayList<String> resultStringList=invalidQuestionResponseMap.get("ERROR");
			if(!errorResponseString.equals("")){
				resultStringList.add(errorResponseString);
			}
			invalidQuestionResponseMap.put("ERROR",resultStringList);
		}
		
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
		MCQuestionPaper paper= new MCQuestionPaper("G:\\Codewar\\exam1.xls");
		paper.LoadQuestionPaper();
		
		//System.out.println("Is valid :"+isValidCorrectAnswer("1,10"));
	}
}
