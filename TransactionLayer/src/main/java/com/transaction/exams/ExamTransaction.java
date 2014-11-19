package com.transaction.exams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;

import com.classapp.db.exambean.Description;
import com.classapp.db.exambean.ExamData;
import com.classapp.db.exambean.Option;
import com.classapp.db.exambean.Question;
import com.classapp.db.exambean.QuestionData;
import com.classapp.db.exams.MCQData;
import com.classapp.db.exams.MCQDataDB;
import com.classapp.db.exams.MCQDetails;
import com.classapp.db.exams.MCQPaperData;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class ExamTransaction {
	int class_id;
	MCQPaperData paperData;

	public ExamTransaction() {
		this.paperData = new MCQPaperData();
	}

	public ExamTransaction(int class_Id) {
		paperData = new MCQPaperData();
		this.class_id = class_Id;
	}

	public void addUpdateDb(MCQData mcqData) {
		this.paperData.addOrUpdateMCQExam(mcqData);
	}

	public MCQData getMCQPaperFromClass(int exam_id, int class_id) {
		return this.paperData.getMCQPaperFromClass(exam_id, class_id);

	}

	public List<MCQDetails> getAllExamPapersDetailsFromID() {
		return paperData.getAllExamPapersDetailsFromID(class_id);
	}

	public List<MCQData> getAllExamPapers() {
		return this.paperData.getAllExamPapers(class_id);
	}

	public MCQData getMCQPaperFromClass(int exam_id) {
		return this.paperData.getMCQPaperFromClass(exam_id, class_id);
	}

	public List<com.datalayer.exam.MCQData> searchExam(int class_id,
			int subject, int teacher, int division, String startDate,
			String endDate) {
		MCQDataDB mcqDataDB = new MCQDataDB();
		List<MCQData> mcqData = mcqDataDB.searchExam(class_id, subject,
				teacher, division, startDate, endDate);
		List<com.datalayer.exam.MCQData> mcqDatas = new ArrayList<com.datalayer.exam.MCQData>();
		for (MCQData mcqData2 : mcqData) {
			com.datalayer.exam.MCQData mcqDataDest = new com.datalayer.exam.MCQData();
			try {
				BeanUtils.copyProperties(mcqDataDest, mcqData2);
				mcqDatas.add(mcqDataDest);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mcqDatas;

	}

	/*public List<QuestionData> getExamData(String path) {
		List<QuestionData> questionDatas = new ArrayList<QuestionData>();
		try {
			// File("c:\\ExamPapers\\34_2_0_1_1411903253699.xlsx");
			File file = new File(path);
			FileInputStream fileInputStream = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			boolean isValid = true;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// For each row, iterate through all the columns
				List<String> optionsList = new ArrayList<String>();
				Iterator<Cell> cellIterator = row.cellIterator();
				QuestionData questionData = new QuestionData();
				isValid = true;
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					// Check the cell type and format accordingly
					if (cell.getColumnIndex() == 0) {
						if (cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
							isValid = false;
							break;
						}else{
							questionData.setQuestionNumber(cell.getNumericCellValue());
						}
					}
					
					/*Question is at index 1
					if (cell.getColumnIndex() == 1) {
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_NUMERIC:
							questionData.setQuestion(cell.getNumericCellValue()+"");
							break;
						case Cell.CELL_TYPE_STRING:
							questionData.setQuestion(cell.getStringCellValue());
							break;
						}
					}
					
					Answers are at index 2
					if (cell.getColumnIndex() == 2) {
						List<String> ansList = new ArrayList<String>();
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_NUMERIC:
							if option is Numeric value then its single answer 
							ansList.add(cell.getNumericCellValue()+"");
							break;
						case Cell.CELL_TYPE_STRING:
							String options[] = cell.getStringCellValue().split(",");
							for (String option:options) {
								ansList.add(option);
							}
							break;
						}
						questionData.setAnswers(ansList);
					}
					
					Marks for question is at index 3
					if (cell.getColumnIndex() == 3) {
						
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_NUMERIC:
							questionData.setMarks((int) cell.getNumericCellValue());
							break;
						case Cell.CELL_TYPE_STRING:
							break;
						}
						
					}
					
					Options
					
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						optionsList.add( cell.getNumericCellValue()+"");
						break;
					case Cell.CELL_TYPE_STRING:
						optionsList.add( cell.getStringCellValue());
						break;
					}
					questionData.setOptions(optionsList);
				}
				if (isValid) {
					questionDatas.add(questionData);
				}
				
			}
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return questionDatas;
	}
*/
	/*
	public static void main(String[] args) {
		ExamTransaction examTransaction = new ExamTransaction();
		examTransaction.getExamData("c:\\ExamPapers\\34_2_0_1_1411903253699.xlsx");
	}
	
	*/
	
	public int saveExam(ExamData examData,String fileToSave){
		try {
			File file = new File(fileToSave);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fout = new FileOutputStream(
				fileToSave);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(examData);
			
			oos.close();
			fout.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public ExamData getExam(String fileToRead){
		ExamData examData = null;
		try {
			FileInputStream fin = new FileInputStream(fileToRead);
			ObjectInputStream ois = new ObjectInputStream(fin);
			examData = (ExamData) ois.readObject();
			ois.close();
			fin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return examData;
	}

	public static void main(String[] args) {
		ExamData examData = new ExamData();
		List<QuestionData> questionDatas = new ArrayList<QuestionData>();
		String fileName = "C:\\ExamPapers\\testing.data";
		
		for (int i1 = 0; i1 < 100; i1++) {
			
		QuestionData questionData = new QuestionData();
		Option option;
		Description description;
		Question question;
		
		List<Option> options = new ArrayList<Option>();
		List<String> imageFiles = new ArrayList<String>();
		String base64Image = "null";
		for (int i = 0; i < 4; i++) {
			File file = new File("E:\\SURAJ\\cxlogo.jpg");
			try {
				Files.probeContentType(file.toPath());
				//System.out.println(Files.probeContentType(file.toPath()));
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
			try {
				base64Image = Base64.encode(FileUtils.readFileToByteArray(file));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			imageFiles.add(base64Image);
		}
		
		for (int i = 0; i < 4; i++) {
			option = new Option();
			
			option.setImageFiles(imageFiles);
			option.setOptions("Option.." + i);
			options.add(option);
			
		}

		/*Set question*/
			question = new Question();
			question.setImage(imageFiles);
			question.setQuestion("Question1");
		
		
		/*Set description*/
			description = new Description();
			description.setImages(imageFiles);
			description.setDescription("Desription");
		
		
		questionData.setQuestion(question);
		questionData.setOptions(options);
		questionData.setDescription(description);
		questionData.setMarks(10);
		questionData.setMultipleChoice(true);
		if(i1%13==0)
			questionData.setSolved(true);
		else
			questionData.setSolved(false);
		
		questionDatas.add(questionData);
		}
		examData.setQuestionDatas(questionDatas);
		examData.setTimeToSolve(50);
		/**/
		ExamTransaction examTransaction = new ExamTransaction();
		examTransaction.saveExam(examData, fileName);
		ExamData examData2 = examTransaction.getExam(fileName);
		System.out.println(examData2);
	}

}
