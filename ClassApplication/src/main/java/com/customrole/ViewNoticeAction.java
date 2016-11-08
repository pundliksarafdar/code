package com.customrole;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.batch.Batch;
import com.classapp.db.batch.division.Division;
import com.classapp.db.notice.StudentNotice;
import com.config.BaseAction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.notice.NoticeTransaction;
import com.user.UserBean;

public class ViewNoticeAction extends BaseAction {
	List<StudentNotice> studentNoticeDBList;
	List<com.datalayer.notice.StudentNotice> studentNoticeList;
	@Override
	public String performBaseAction(UserBean userBean, HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		NoticeTransaction noticeTransaction = new NoticeTransaction();
		studentNoticeDBList = noticeTransaction.getStudentNotice(userBean.getInst_id());
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		List<Division> divisionList = divisionTransactions.getAllDivisions(userBean.getInst_id());
		BatchTransactions batchTransactions = new BatchTransactions();
		studentNoticeList = new ArrayList<com.datalayer.notice.StudentNotice>();
		if(studentNoticeDBList != null){
			for (StudentNotice studentNotice : studentNoticeDBList) {
				com.datalayer.notice.StudentNotice  notice  =  new com.datalayer.notice.StudentNotice();
				try {
					BeanUtils.copyProperties(notice, studentNotice);
				} catch (IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(notice.getDiv_id() != 0){
					List<Division> list = divisionList.stream().filter(Division -> Division.getDivId() == notice.getDiv_id()).collect(Collectors.toList());
					if(list!=null){
						if(list.size() > 0){
							notice.setDiv_name(list.get(0).getDivisionName()+" "+list.get(0).getStream());
						}
					}
					List<Batch> batchList = batchTransactions.getAllBatchesOfDivision(notice.getDiv_id(), userBean.getInst_id());
					String [] batchArray = notice.getBatch_id().split(",");
					String batchString = "";
					for (int i = 0; i < batchArray.length; i++) {
						
						String tempBatch = batchArray[i].trim();
						if(batchList!=null){
							if(batchList.size() > 0){
								List<Batch> batches = batchList.stream().filter(Batch -> Batch.getBatch_id() == Integer.parseInt(tempBatch)).collect(Collectors.toList());
								if(batches != null){
									if(batches.size() > 0){
										batchString = batchString + batches.get(0).getBatch_name()+",";
									}
								}
							}
						}
					}
					if(batchString.length()>0){
						batchString = batchString.substring(0, batchString.length()-1);
					}
					notice.setBatch_names(batchString);
				}else{
					notice.setDiv_name("All");
					notice.setBatch_names("All");
				}
				studentNoticeList.add(notice);
			}
		}
		return SUCCESS;
	}
	public List<StudentNotice> getStudentNoticeDBList() {
		return studentNoticeDBList;
	}
	public void setStudentNoticeDBList(List<StudentNotice> studentNoticeDBList) {
		this.studentNoticeDBList = studentNoticeDBList;
	}
	public List<com.datalayer.notice.StudentNotice> getStudentNoticeList() {
		return studentNoticeList;
	}
	public void setStudentNoticeList(List<com.datalayer.notice.StudentNotice> studentNoticeList) {
		this.studentNoticeList = studentNoticeList;
	}
	
	

}
