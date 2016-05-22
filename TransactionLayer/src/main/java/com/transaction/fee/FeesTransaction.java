package com.transaction.fee;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.batch.Batch;
import com.classapp.db.batch.BatchDB;
import com.classapp.db.fees.BatchFeesDistribution;
import com.classapp.db.fees.Fees;
import com.classapp.db.fees.FeesDB;
import com.classapp.db.fees.FeesStructure;
import com.classapp.db.fees.Student_Fees;
import com.classapp.db.fees.Student_Fees_Transaction;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.classapp.utils.Constants;
import com.service.beans.BatchFees;
import com.service.beans.BatchFeesDistributionServiceBean;
import com.service.beans.BatchServiceBean;
import com.service.beans.BatchStudentFees;
import com.service.beans.FeeStructure;
import com.service.beans.StudentFeesServiceBean;
import com.transaction.register.RegisterTransaction;
import com.transaction.student.StudentTransaction;

public class FeesTransaction {
	
	public boolean saveFeeStructure(com.service.beans.Fees serviceFees,List<com.service.beans.FeesStructure> feesStructureList) {
		FeesDB feesDB = new FeesDB();
		Fees fees = new Fees();
		try {
		BeanUtils.copyProperties(fees, serviceFees);	
		int inst_id = fees.getInst_id();
		if(!feesDB.verifyFee(fees)){
		int fees_id = feesDB.saveFees(fees);
		for (Iterator iterator = feesStructureList.iterator(); iterator.hasNext();) {
			com.service.beans.FeesStructure serviceFeesStructure = (com.service.beans.FeesStructure) iterator.next();
			FeesStructure feesStructure = new FeesStructure();
			BeanUtils.copyProperties(feesStructure, serviceFeesStructure);	
			feesStructure.setFees_id(fees_id);
			feesStructure.setInst_id(inst_id);
			feesDB.saveFeesStructure(feesStructure);
		}
		}else{
			return false;
		}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public FeeStructure getFeeStructurelist(int inst_id,int fees_id) {
		FeesDB feesDB = new FeesDB();
		Fees fees = feesDB.getFees(inst_id, fees_id);
		List<FeesStructure> feesStructureList = feesDB.getFeesStructureList(inst_id, fees_id);
		FeeStructure feeStructure = new FeeStructure();
		com.service.beans.Fees serviceFees = new com.service.beans.Fees();
		List<com.service.beans.FeesStructure> serviceFeesStructureList = new ArrayList<com.service.beans.FeesStructure>();
		try {
			BeanUtils.copyProperties(serviceFees, fees);
			if(feesStructureList != null){
				for (Iterator iterator = feesStructureList.iterator(); iterator
						.hasNext();) {
					FeesStructure feesStructure = (FeesStructure) iterator
							.next();
					com.service.beans.FeesStructure serviceFeesStructure = new com.service.beans.FeesStructure();
					BeanUtils.copyProperties(serviceFeesStructure,feesStructure);
					serviceFeesStructureList.add(serviceFeesStructure);
				}
			}
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		feeStructure.setFees(serviceFees);
		feeStructure.setFeesStructureList(serviceFeesStructureList);
		return feeStructure;
	}
	
	public List<Fees> getAllFees(int inst_id) {
		FeesDB feesDB = new FeesDB();
		return feesDB.getFeesList(inst_id);
		
	}
	
	public boolean updateFeeStructure(com.service.beans.Fees serviceFees,List<com.service.beans.FeesStructure> updatedFeesStructureList) {
		FeesDB feesDB = new FeesDB();
		Fees fees = new Fees();
		try {
		BeanUtils.copyProperties(fees, serviceFees);
		int inst_id = fees.getInst_id();
		List<FeesStructure> feesStructureList = feesDB.getFeesStructureList(inst_id, fees.getFees_id());
		if(!feesDB.verifyUpdateFee(fees)){
		feesDB.updateFees(fees);
		for (Iterator iterator = feesStructureList.iterator(); iterator
				.hasNext();) {
			boolean flag = false;
			FeesStructure feesStructure = (FeesStructure) iterator.next();
			for (Iterator iterator2 = updatedFeesStructureList.iterator(); iterator2
					.hasNext();) {
				com.service.beans.FeesStructure updatedFeesStructure = (com.service.beans.FeesStructure) iterator2.next();
				if(updatedFeesStructure.getFees_structure_id() == feesStructure.getFees_structure_id()){
					feesStructure.setFees_structure_desc(updatedFeesStructure.getFees_structure_desc());
					feesDB.updateFeesStructure(feesStructure);
					flag = true;
					break;
				}
			}
			if(flag == false){
				feesDB.deleteFeesStructure(feesStructure);
			}
			
		}
		
		for (Iterator iterator = updatedFeesStructureList.iterator(); iterator
				.hasNext();) {
			com.service.beans.FeesStructure serviceFeesStructure = (com.service.beans.FeesStructure) iterator.next();
			if(serviceFeesStructure.getFees_structure_id() == 0){
				FeesStructure feesStructure = new FeesStructure();
				BeanUtils.copyProperties(feesStructure, serviceFeesStructure);	
				feesStructure.setFees_id(fees.getFees_id());
				feesStructure.setInst_id(inst_id);
				feesDB.saveFeesStructure(feesStructure);
			}
			
		}
		}else{
			return false;
		}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return true;
	}
	
	public boolean deleteFees(int inst_id,int fees_id){
		FeesDB feesDB = new FeesDB();
		feesDB.deleteBatchFees(inst_id, fees_id);
		feesDB.deleteBatchFeesStructure(inst_id, fees_id);
		feesDB.deleteFeesStructure(inst_id, fees_id);
		feesDB.deleteFees(inst_id, fees_id);
		return true;
	}
	
	public boolean saveBatchFeesDistribution(BatchFeesDistributionServiceBean serviceBean,int inst_id){
		FeesDB feesDB = new FeesDB();
		int batch_fees_id = 0;
		com.classapp.db.fees.BatchFees batchFees = new com.classapp.db.fees.BatchFees();
		try {
			BeanUtils.copyProperties(batchFees, serviceBean.getBatchFees());
			batchFees.setInst_id(inst_id);
			batch_fees_id = feesDB.saveBatchFees(batchFees);
		
		for (Iterator iterator = serviceBean.getBatchFeesDistribution().iterator(); iterator
				.hasNext();) {
			com.service.beans.BatchFeesDistribution batchFeesDistribution = (com.service.beans.BatchFeesDistribution) iterator
					.next();
			batchFeesDistribution.setInst_id(inst_id);
			batchFeesDistribution.setBatch_fees_id(batch_fees_id);
			BatchFeesDistribution feesDistribution = new BatchFeesDistribution();
			BeanUtils.copyProperties(feesDistribution,batchFeesDistribution);
			feesDB.saveBatchFeesDistribution(feesDistribution);
			
		}
	//	feesDB.updateStudentFeesRelatedToBatch(inst_id, serviceBean.getBatchFees().getDiv_id(), serviceBean.getBatchFees().getBatch_id(), serviceBean.getBatchFees().getBatch_fees());
		StudentTransaction studentTransaction = new StudentTransaction();
		List<Student> studentList = studentTransaction.getStudentsrelatedtobatch(serviceBean.getBatchFees().getBatch_id()+"", inst_id, serviceBean.getBatchFees().getDiv_id());
		feesDB.updateStudentFeesRelatedToBatch(inst_id, serviceBean.getBatchFees().getDiv_id(), serviceBean.getBatchFees().getBatch_id(), serviceBean.getBatchFees().getBatch_fees());
		List<Integer> studentIDList = feesDB.getStudentIdsFromStudentFees(inst_id, serviceBean.getBatchFees().getDiv_id(), serviceBean.getBatchFees().getBatch_id());
		studentList = studentList.stream().filter(x -> !studentIDList.contains(x.getStudent_id())).collect(Collectors.toList());
		System.out.println(studentList);
		if(studentList != null){
		for (Student student : studentList) {
			Student_Fees student_Fees = new Student_Fees();
			student_Fees.setInst_id(inst_id);
			student_Fees.setBatch_fees(batchFees.getBatch_fees());
			student_Fees.setBatch_id(batchFees.getBatch_id());
			student_Fees.setDiscount(0);
			student_Fees.setDiscount_type("amt");
			student_Fees.setDiv_id(batchFees.getDiv_id());
			student_Fees.setFees_due(batchFees.getBatch_fees());
			student_Fees.setFees_paid(0);
			student_Fees.setFinal_fees_amt(batchFees.getBatch_fees());
			student_Fees.setStudent_id(student.getStudent_id());
			feesDB.saveStudentFees(student_Fees);
		}
		}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean updateBatchFeesDistribution(BatchFeesDistributionServiceBean serviceBean,int inst_id){
		FeesDB feesDB = new FeesDB();
		int batch_fees_id = 0;
		com.classapp.db.fees.BatchFees batchFees = new com.classapp.db.fees.BatchFees();
		try {
			BeanUtils.copyProperties(batchFees, serviceBean.getBatchFees());
			batchFees.setInst_id(inst_id);
			 feesDB.updateBatchFees(batchFees);
			 feesDB.deleteBatchFeesDistribution(inst_id, batchFees.getBatch_fees_id());
		for (Iterator iterator = serviceBean.getBatchFeesDistribution().iterator(); iterator
				.hasNext();) {
			com.service.beans.BatchFeesDistribution batchFeesDistribution = (com.service.beans.BatchFeesDistribution) iterator
					.next();
			batchFeesDistribution.setInst_id(inst_id);
			batchFeesDistribution.setFees_id(batchFees.getFees_id());
			BatchFeesDistribution feesDistribution = new BatchFeesDistribution();
			BeanUtils.copyProperties(feesDistribution,batchFeesDistribution);
			feesDB.saveBatchFeesDistribution(feesDistribution);
			
		}
		feesDB.updateStudentFeesRelatedToBatch(inst_id, serviceBean.getBatchFees().getDiv_id(), serviceBean.getBatchFees().getBatch_id(), serviceBean.getBatchFees().getBatch_fees());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public BatchFeesDistributionServiceBean getBatchFeesDistribution(int inst_id,int div_id,int batch_id){
		FeesDB feesDB = new FeesDB();
		com.classapp.db.fees.BatchFees batchFees = feesDB.getBatchFees(inst_id, div_id, batch_id);
		BatchFeesDistributionServiceBean serviceBean = new BatchFeesDistributionServiceBean();
		List<com.service.beans.BatchFeesDistribution> serviceFeesDistributionList = new ArrayList<com.service.beans.BatchFeesDistribution>();
		List<com.service.beans.FeesStructure> serviceFeesStructureList = new ArrayList<com.service.beans.FeesStructure>();
		BatchFees serviceBatchFees = new BatchFees();
		if(batchFees!=null){
			List<BatchFeesDistribution> batchFeesDistributionList = feesDB.getBatchFeesDistribution(inst_id, batchFees.getBatch_fees_id());
			List<FeesStructure> feesStructureList = feesDB.getFeesStructureList(inst_id, batchFees.getFees_id());
			try {
				BeanUtils.copyProperties(serviceBatchFees, batchFees);
				for (Iterator iterator = batchFeesDistributionList.iterator(); iterator
						.hasNext();) {
					BatchFeesDistribution batchFeesDistribution = (BatchFeesDistribution) iterator
							.next();
					com.service.beans.BatchFeesDistribution feesDistribution = new com.service.beans.BatchFeesDistribution();
					BeanUtils.copyProperties(feesDistribution, batchFeesDistribution);
					serviceFeesDistributionList.add(feesDistribution);
				}
				for (Iterator iterator = feesStructureList.iterator(); iterator
						.hasNext();) {
					FeesStructure feesStructure = (FeesStructure) iterator
							.next();
					com.service.beans.FeesStructure structure = new com.service.beans.FeesStructure();
					BeanUtils.copyProperties(structure, feesStructure);
					serviceFeesStructureList.add(structure);
				}
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BatchFeesDistributionServiceBean batchFeesDistributionServiceBean = new BatchFeesDistributionServiceBean();
			batchFeesDistributionServiceBean.setBatchFees(serviceBatchFees);
			batchFeesDistributionServiceBean.setBatchFeesDistribution(serviceFeesDistributionList);
			batchFeesDistributionServiceBean.setFeesStructureList(serviceFeesStructureList);
			return batchFeesDistributionServiceBean;
		}
		return null;
	}
	
	public List<BatchServiceBean> getInstituteBatch(int division,int inst_id) {
		FeesDB feesDB = new FeesDB();
		BatchDB batchDB = new BatchDB();
		List<BatchServiceBean> serviceBeanList = new ArrayList<BatchServiceBean>();
		List<Batch> batchList = batchDB.retriveAllBatchesOfDivision(division, inst_id);
		List<Integer> batchIds = new ArrayList<Integer>();
		if(batchList != null){
			for (Iterator iterator = batchList.iterator(); iterator.hasNext();) {
				Batch batch = (Batch) iterator.next();
				batchIds.add(batch.getBatch_id());
			}
			List<com.classapp.db.fees.BatchFees> batchFeeList = feesDB.getBatchFeesList(inst_id, division, batchIds);
			for (Iterator iterator = batchList.iterator(); iterator
					.hasNext();) {
				Batch batch = (Batch) iterator.next();
				com.service.beans.Batch serviceBatch = new com.service.beans.Batch();
				BatchServiceBean serviceBean = new BatchServiceBean();
				try {
					BeanUtils.copyProperties(serviceBatch, batch);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				serviceBean.setBatch(serviceBatch);
				for (Iterator iterator2 = batchFeeList.iterator(); iterator2
						.hasNext();) {
					com.classapp.db.fees.BatchFees batchFees = (com.classapp.db.fees.BatchFees) iterator2.next();
					if(batch.getBatch_id() == batchFees.getBatch_id()){
						serviceBean.setFeesLinkStatus("Yes");
						break;
					}
				}
				serviceBeanList.add(serviceBean);
			}
		}
	
		return serviceBeanList;
	}
	
	public boolean reLinkBatchFeesDistribution(BatchFeesDistributionServiceBean serviceBean,int inst_id){
		FeesDB feesDB = new FeesDB();
		{
			com.classapp.db.fees.BatchFees batchFees =  feesDB.getBatchFees(inst_id, serviceBean.getBatchFees().getDiv_id(), serviceBean.getBatchFees().getBatch_id());
			if(batchFees!=null){
				feesDB.deleteBatchFees(inst_id, serviceBean.getBatchFees().getDiv_id(), serviceBean.getBatchFees().getBatch_id());
				feesDB.deleteBatchFeesDistribution(inst_id, batchFees.getBatch_fees_id());
			}
			
		}
		int batch_fees_id = 0;
		com.classapp.db.fees.BatchFees batchFees = new com.classapp.db.fees.BatchFees();
		try {
			BeanUtils.copyProperties(batchFees, serviceBean.getBatchFees());
			batchFees.setInst_id(inst_id);
			batch_fees_id = feesDB.saveBatchFees(batchFees);
		
		for (Iterator iterator = serviceBean.getBatchFeesDistribution().iterator(); iterator
				.hasNext();) {
			com.service.beans.BatchFeesDistribution batchFeesDistribution = (com.service.beans.BatchFeesDistribution) iterator
					.next();
			batchFeesDistribution.setInst_id(inst_id);
			batchFeesDistribution.setBatch_fees_id(batch_fees_id);
			BatchFeesDistribution feesDistribution = new BatchFeesDistribution();
			BeanUtils.copyProperties(feesDistribution,batchFeesDistribution);
			feesDB.saveBatchFeesDistribution(feesDistribution);
		}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean saveStudentBatchFees(int inst_id,List<com.service.beans.Student_Fees> student_FeesList ){
		FeesDB feesDB = new FeesDB();
		for (Iterator iterator = student_FeesList.iterator(); iterator
				.hasNext();) {
			com.service.beans.Student_Fees serciveStudent_Fees = (com.service.beans.Student_Fees) iterator.next();
			serciveStudent_Fees.setInst_id(inst_id);
			Student_Fees student_Fees = new Student_Fees();
			try {
				BeanUtils.copyProperties(student_Fees, serciveStudent_Fees);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if("per".equals(student_Fees.getDiscount_type())){
				double finalAmt =  student_Fees.getBatch_fees() - (student_Fees.getBatch_fees()*(student_Fees.getDiscount()/100));
				double due = finalAmt - student_Fees.getFees_paid();
				student_Fees.setFinal_fees_amt(finalAmt);
				student_Fees.setFees_due(due);
			}else{
				double finalAmt =  student_Fees.getBatch_fees() - student_Fees.getDiscount();
				double due = finalAmt - student_Fees.getFees_paid();
				student_Fees.setFinal_fees_amt(finalAmt);
				student_Fees.setFees_due(due);
			}
			feesDB.saveStudentFees(student_Fees);
			Student_Fees_Transaction fees_Transaction = new Student_Fees_Transaction();
			fees_Transaction.setAdded_by(inst_id);
			fees_Transaction.setAmt_paid(student_Fees.getFees_paid());
			fees_Transaction.setBatch_id(student_Fees.getBatch_id());
			fees_Transaction.setDiv_id(student_Fees.getDiv_id());
			fees_Transaction.setInst_id(inst_id);
			fees_Transaction.setStudent_id(student_Fees.getStudent_id());
			fees_Transaction.setTransaction_dt(new Date(new java.util.Date().getTime()));
			feesDB.saveStudentFeesTransaction(fees_Transaction);
		}
		return true;
	}
	
	public boolean saveStudentBatchFeesTransaction(int inst_id,com.service.beans.Student_Fees_Transaction serviceFees_Transaction ){
		FeesTransaction feesTransaction = new FeesTransaction();
		BatchFeesDistributionServiceBean batchFeesDistributionServiceBean = feesTransaction.getBatchFeesDistribution(inst_id, serviceFees_Transaction.getDiv_id(), serviceFees_Transaction.getBatch_id());
		
		if(null == batchFeesDistributionServiceBean){
			return false;
		}
		
		int bathcFee = batchFeesDistributionServiceBean.getBatchFees().getBatch_fees();
		FeesDB feesDB = new FeesDB();
		if(serviceFees_Transaction.getDiv_id() == 0){
			StudentTransaction studentTransaction = new StudentTransaction();
			Student student = studentTransaction.getStudentByStudentID(serviceFees_Transaction.getStudent_id(), inst_id);
			serviceFees_Transaction.setDiv_id(student.getDiv_id());		
		}
		serviceFees_Transaction.setAdded_by(inst_id);
		serviceFees_Transaction.setInst_id(inst_id);
		serviceFees_Transaction.setTransaction_dt(new Date(new java.util.Date().getTime()));
		Student_Fees_Transaction fees_Transaction = new Student_Fees_Transaction();
		try {
			BeanUtils.copyProperties(fees_Transaction, serviceFees_Transaction);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		feesDB.saveStudentFeesTransaction(fees_Transaction);
		feesDB.updateStudentFees(inst_id, fees_Transaction.getDiv_id(), 
				fees_Transaction.getBatch_id(), fees_Transaction.getStudent_id(), 
				fees_Transaction.getAmt_paid(),bathcFee,
				serviceFees_Transaction.getDisType(),serviceFees_Transaction.getDiscount());
		return true;
	}
	
	public StudentFeesServiceBean getStudentFees(int inst_id,int student_id){
		FeesDB feesDB = new FeesDB();
		List<Student_Fees> student_FeesList = feesDB.getStudentFees(inst_id, student_id);
		List<Student_Fees_Transaction> student_Fees_TransactionList = feesDB.getStudentFeesTransaction(inst_id, student_id);
		StudentFeesServiceBean studentFeesServiceBean = new StudentFeesServiceBean();
		List<com.service.beans.Student_Fees> serviceStudent_FeesList = new ArrayList<com.service.beans.Student_Fees>();
		List<com.service.beans.Student_Fees_Transaction> serviceStudent_Fees_TransactionList = new ArrayList<com.service.beans.Student_Fees_Transaction>();
		for (Iterator iterator = student_FeesList.iterator(); iterator
				.hasNext();) {
			Student_Fees student_Fees = (Student_Fees) iterator
					.next();
			com.service.beans.Student_Fees serviceStudent_Fees = new com.service.beans.Student_Fees();
			try {
				BeanUtils.copyProperties(serviceStudent_Fees, student_Fees);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			serviceStudent_FeesList.add(serviceStudent_Fees);
		}
		
		for (Iterator iterator = serviceStudent_Fees_TransactionList.iterator(); iterator
				.hasNext();) {
			Student_Fees_Transaction student_Fees_Transaction = (Student_Fees_Transaction) iterator
					.next();
			com.service.beans.Student_Fees_Transaction serviceStudent_Fees_Transaction = new com.service.beans.Student_Fees_Transaction();
			
			try {
				BeanUtils.copyProperties(serviceStudent_Fees_Transaction, student_Fees_Transaction);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			serviceStudent_Fees_TransactionList.add(serviceStudent_Fees_Transaction);
		}
		
		studentFeesServiceBean.setStudent_FeesList(serviceStudent_FeesList);
		studentFeesServiceBean.setStudent_Fees_TransactionList(serviceStudent_Fees_TransactionList);
		
		return studentFeesServiceBean;
	}
	
	public List<BatchStudentFees> getAllBatchStudentsFees(int inst_id,int div_id,int batch_id) {
		FeesDB feesDB = new FeesDB();
		FeesTransaction feesTransaction = new FeesTransaction();
		BatchFeesDistributionServiceBean batchFeesDistributionServiceBean = feesTransaction.getBatchFeesDistribution(inst_id, div_id, batch_id);
		
		if(null == batchFeesDistributionServiceBean){
			return new ArrayList<BatchStudentFees>();
		}
		int batchFee = batchFeesDistributionServiceBean.getBatchFees().getBatch_fees();
		
		StudentTransaction studentTransaction = new StudentTransaction();
		List list = feesDB.getAllBatchStudentsFees(inst_id, div_id, batch_id);
		List<Student> students = studentTransaction.getStudentsrelatedtobatch(batch_id+"", inst_id, div_id);
		List<BatchStudentFees> batchStudentFeesList = new ArrayList<BatchStudentFees>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			BatchStudentFees batchStudentFees = new BatchStudentFees();
			batchStudentFees.setStudent_id(((Number) object[0]).intValue());
			batchStudentFees.setFname((String) object[1]);
			batchStudentFees.setLname((String) object[2]);
			batchStudentFees.setBatch_fees(batchFee);
			batchStudentFees.setDiscount(((Number) object[4]).doubleValue());
			batchStudentFees.setDiscount_type((String) object[5]);
			batchStudentFees.setFinal_fees_amt(((Number) object[6]).doubleValue());
			batchStudentFees.setFees_paid(((Number) object[7]).doubleValue());
			batchStudentFees.setFees_due(((Number) object[8]).doubleValue());
			batchStudentFeesList.add(batchStudentFees);
		}
		
		List studentIds = new ArrayList();
		for(Student student:students){
			studentIds.add(student.getStudent_id());
		}
		
		RegisterTransaction registerTransaction = new RegisterTransaction();
		List<RegisterBean> registerBeans = registerTransaction.getStudentsInfo(studentIds, 0);
		
		for(Student student:students){
			boolean flag = false;
			for (BatchStudentFees batchStudentFees : batchStudentFeesList) {
				if(batchStudentFees.getStudent_id() == student.getStudent_id()){
					flag = true;
					break;
				}
			}
			if(flag == false){
			for(RegisterBean registerBean:registerBeans){
				BatchStudentFees batchStudentFees = new BatchStudentFees();
				batchStudentFees.setStudent_id(student.getStudent_id());
				if(registerBean.getRegId() == student.getStudent_id()){
					batchStudentFees.setFname(registerBean.getFname());
					batchStudentFees.setLname(registerBean.getLname());
					batchStudentFees.setBatch_fees(batchFee);
					batchStudentFeesList.add(batchStudentFees);
					break;
				}
			}
			}
		}
		return batchStudentFeesList;
		
	}
	
	public boolean updateStudentFeesAmt(int inst_id,int div_id,int batch_id,int student_id,double discount,String discount_type ){
		FeesDB feesDB = new FeesDB();
		feesDB.updateStudentFeesAmt(inst_id, div_id, batch_id, student_id, discount, discount_type);
		return true;
	}
	
	public List<com.classapp.db.fees.BatchFees> getBatchFeesList(int inst_id,int div_id,List<Integer> batchIdList) {
		FeesDB feesDB = new FeesDB();
		return feesDB.getBatchFeesList(inst_id, div_id, batchIdList);
	}
	
	public BatchStudentFees getStudentsTransactionForPrint(int inst_id,int div_id,int batch_id,int student_id){
		FeesDB feesDB = new FeesDB();
		List listTransactions = feesDB.getStudentsTransactionForPrint(inst_id, div_id, batch_id, student_id);
		Object[] list =  (Object[]) listTransactions.get(0);
		BatchStudentFees batchStudentFees = new BatchStudentFees();
		batchStudentFees.setBatch_fees((Double) list[Constants.LAST_FEE_PARAM.BATCH_FEE.getOrdinalvalue()]);
		batchStudentFees.setDiscount((Double) list[Constants.LAST_FEE_PARAM.DISCOUNT.getOrdinalvalue()]);
		batchStudentFees.setDiscount_type((String) list[Constants.LAST_FEE_PARAM.DISCOUNT_TYPE.getOrdinalvalue()]);
		batchStudentFees.setFees_due((Double) list[Constants.LAST_FEE_PARAM.FEES_DUE.getOrdinalvalue()]);
		batchStudentFees.setFees_paid((Double) list[Constants.LAST_FEE_PARAM.FEES_PAID.getOrdinalvalue()]);
		batchStudentFees.setFinal_fees_amt((Double) list[Constants.LAST_FEE_PARAM.FINAL_FEES_AMT.getOrdinalvalue()]);
		batchStudentFees.setPaidOn((Date)list[Constants.LAST_FEE_PARAM.TRANSACTION_DATE.getOrdinalvalue()]);
		batchStudentFees.setFname((String)list[Constants.LAST_FEE_PARAM.FIRST_NAME.getOrdinalvalue()]);
		batchStudentFees.setLname((String)list[Constants.LAST_FEE_PARAM.LAST_NAME.getOrdinalvalue()]);
		return batchStudentFees;		
	}
	
	public boolean updateStudentFeesRelatedToClass(int inst_id,int div_id){
		FeesDB feesDB = new FeesDB();
		feesDB.updateStudentFeesRelatedToClass(inst_id, div_id);
		feesDB.updateStudentFeesTransactionRelatedToClass(inst_id, div_id);
		return true;
	}
	
	public boolean updateStudentFeesRelatedToBatch(int inst_id,int div_id,int batch_id){
		FeesDB feesDB = new FeesDB();
		feesDB.updateStudentFeesRelatedToBatch(inst_id, div_id, batch_id);
		feesDB.updateStudentFeesTransactionRelatedToBatch(inst_id, div_id, batch_id);
		return true;
	}
}
