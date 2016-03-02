package com.service.beans;

import java.util.List;

public class BatchFeesDistributionServiceBean {
	BatchFees batchFees;
	List<BatchFeesDistribution> batchFeesDistribution;
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
	
}
