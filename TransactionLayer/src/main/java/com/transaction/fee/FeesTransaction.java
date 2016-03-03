package com.transaction.fee;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.fees.BatchFeesDistribution;
import com.classapp.db.fees.Fees;
import com.classapp.db.fees.FeesDB;
import com.classapp.db.fees.FeesStructure;
import com.service.beans.BatchFees;
import com.service.beans.BatchFeesDistributionServiceBean;
import com.service.beans.FeeStructure;

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
		feesDB.deleteFees(inst_id, fees_id);
		feesDB.deleteFeesStructure(inst_id, fees_id);
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
		    for (Iterator iterator = serviceBean.getBatchFeesDistribution().iterator(); iterator
					.hasNext();) {
				com.service.beans.BatchFeesDistribution batchFeesDistribution = (com.service.beans.BatchFeesDistribution) iterator
						.next();
				batchFeesDistribution.setInst_id(inst_id);
				BatchFeesDistribution feesDistribution = new BatchFeesDistribution();
				BeanUtils.copyProperties(feesDistribution,batchFeesDistribution);
				feesDB.updateBatchFeesDistribution(feesDistribution);
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
}
