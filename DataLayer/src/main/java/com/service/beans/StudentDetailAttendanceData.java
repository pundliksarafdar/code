package com.service.beans;

import java.util.List;

public class StudentDetailAttendanceData {
	List<StudentDetailBatchData> batchDataList;
	List<StudentDetailMonthWiseTotalLectures> monthWiseTotalLectureList;
	List<StudentDetailMonthWiseAttendance> monthWiseAttendanceList;
	public List<StudentDetailBatchData> getBatchDataList() {
		return batchDataList;
	}
	public void setBatchDataList(List<StudentDetailBatchData> batchDataList) {
		this.batchDataList = batchDataList;
	}
	public List<StudentDetailMonthWiseTotalLectures> getMonthWiseTotalLectureList() {
		return monthWiseTotalLectureList;
	}
	public void setMonthWiseTotalLectureList(List<StudentDetailMonthWiseTotalLectures> monthWiseTotalLectureList) {
		this.monthWiseTotalLectureList = monthWiseTotalLectureList;
	}
	public List<StudentDetailMonthWiseAttendance> getMonthWiseAttendanceList() {
		return monthWiseAttendanceList;
	}
	public void setMonthWiseAttendanceList(List<StudentDetailMonthWiseAttendance> monthWiseAttendanceList) {
		this.monthWiseAttendanceList = monthWiseAttendanceList;
	}
	
}
