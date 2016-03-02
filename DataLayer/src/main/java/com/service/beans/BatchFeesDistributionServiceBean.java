package com.service.beans;

import java.util.List;

public class BatchFeesDistributionServiceBean {
	BatchFees batchFees;
	List<BatchFeesDistribution> batchFeesDistribution;
	List<FeesStructure> feesStructureList;
	public BatchFees getBatchFees() {
		return batchFees;
	}
	public void setBatchFees(BatchFees batchFees) {
		this.batchFees = batchFees;
	}
	public List<BatchFeesDistribution> getBatchFeesDistribution() {
		return batchFeesDistribution;
	}
	public void setBatchFeesDistribution(
			List<BatchFeesDistribution> batchFeesDistribution) {
		this.batchFeesDistribution = batchFeesDistribution;
	}
	public List<FeesStructure> getFeesStructureList() {
		return feesStructureList;
	}
	public void setFeesStructureList(List<FeesStructure> feesStructureList) {
		this.feesStructureList = feesStructureList;
	}
	
}
