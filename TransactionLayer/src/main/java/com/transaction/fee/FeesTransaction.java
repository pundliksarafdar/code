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
import com.service.beans.BatchFeesDistributionServiceBean;
import com.service.beans.FeeStructure;

public class FeesTransaction {
	
	public boolean saveFeeStructure(Fees fees,List<FeesStructure> feesStructureList) {
		FeesDB feesDB = new FeesDB();
		int inst_id = fees.getInst_id();
		if(!feesDB.verifyFee(fees)){
		int fees_id = feesDB.saveFees(fees);
		for (Iterator iterator = feesStructureList.iterator(); iterator.hasNext();) {
			FeesStructure feesStructure = (FeesStructure) iterator.next();
			feesStructure.setFees_id(fees_id);
			feesStructure.setInst_id(inst_id);
			feesDB.saveFeesStructure(feesStructure);
		}
		}else{
			return false;
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
		//feeStructure.setFees(serviceFees);
		feeStructure.setFeesStructureList(serviceFeesStructureList);
		return feeStructure;
	}
	
	public List<Fees> getAllFees(int inst_id) {
		FeesDB feesDB = new FeesDB();
		return feesDB.getFeesList(inst_id);
		
	}
	
	public boolean updateFeeStructure(Fees fees,List<FeesStructure> updatedFeesStructureList) {
		FeesDB feesDB = new FeesDB();
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
				FeesStructure updatedFeesStructure = (FeesStructure) iterator2.next();
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
			FeesStructure feesStructure = (FeesStructure) iterator.next();
			if(feesStructure.getFees_structure_id() == 0){
				feesStructure.setFees_id(fees.getFees_id());
				feesStructure.setInst_id(inst_id);
				feesDB.saveFeesStructure(feesStructure);
			}
			
		}
		}else{
			return false;
		}
		return true;
	}
	
	public boolean deleteFees(int inst_id,int fees_id){
		FeesDB feesDB = new FeesDB();
		feesDB.deleteFees(inst_id, fees_id);
		feesDB.deleteFeesStructure(inst_id, fees_id);
		return true;
	}
	
	public boolean saveBatchFeesDistribution(List<BatchFeesDistribution> batchFeesDistributionList,int inst_id){
		FeesDB feesDB = new FeesDB();
		for (Iterator iterator = batchFeesDistributionList.iterator(); iterator
				.hasNext();) {
			BatchFeesDistribution batchFeesDistribution = (BatchFeesDistribution) iterator
					.next();
			batchFeesDistribution.setInst_id(inst_id);
			feesDB.saveBatchFeesDistribution(batchFeesDistribution);
		}
		
		return true;
	}
	
	public List<BatchFeesDistributionServiceBean> getBatchFeesDistribution(int inst_id,int div_id,int batch_id){
		FeesDB feesDB = new FeesDB();
		List<BatchFeesDistribution> batchFeesDistributionList = feesDB.getBatchFeesDistribution(inst_id, div_id, batch_id);
		if(batchFeesDistributionList != null){
		List<BatchFeesDistributionServiceBean> serviceBeanList = new ArrayList<BatchFeesDistributionServiceBean>();
		Fees fees=feesDB.getFees(inst_id, batchFeesDistributionList.get(0).getFees_id());
		List<FeesStructure> feesStructureList = feesDB.getFeesStructureList(inst_id, fees.getFees_id());
		for (Iterator iterator = batchFeesDistributionList.iterator(); iterator
				.hasNext();) {
			BatchFeesDistribution batchFeesDistribution = (BatchFeesDistribution) iterator.next();
			for (Iterator iterator2 = feesStructureList.iterator(); iterator2
					.hasNext();) {
				FeesStructure feesStructure = (FeesStructure) iterator2.next();
				if(feesStructure.getFees_structure_id() == batchFeesDistribution.getFees_structure_id()){
					BatchFeesDistributionServiceBean serviceBean = new BatchFeesDistributionServiceBean();
					serviceBean.setBatch_id(batch_id);
					serviceBean.setDiv_id(div_id);
					serviceBean.setFees_id(fees.getFees_id());
					serviceBean.setFees_desc(fees.getFees_desc());
					serviceBean.setFees_structure_id(feesStructure.getFees_structure_id());
					serviceBean.setFees_structure_desc(feesStructure.getFees_structure_desc());
					serviceBeanList.add(serviceBean);
				}	
			}
			return serviceBeanList;
		}
		}
		return null;
	}
}
