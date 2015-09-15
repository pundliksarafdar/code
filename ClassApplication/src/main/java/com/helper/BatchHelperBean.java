package com.helper;

import java.util.ArrayList;
import java.util.List;

import com.classapp.db.batch.Batch;
import com.classapp.db.batch.BatchData;
import com.classapp.db.batch.BatchDetails;

public class BatchHelperBean {
	private int class_id;
	private List<Batch> batches=null;
	private List<BatchDetails> batchDetailsList= null;
	BatchData batchData= null;
	int sub_id;
	int div_id;
	public BatchHelperBean() {	
		batchData= new BatchData();		
	}
	
	public BatchHelperBean(int class_id) {	
		batchData= new BatchData();
		this.class_id = class_id;
	}
	
	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

	public List<Batch> getBatches() {		
		this.batches = batchData.getAllBatchesOfClass(class_id);
		System.out.println("Size of list of subjects :"+this.batches.size());
		for (Batch batch : this.batches) {
			System.out.println("Batch name :"+batch.getBatch_name());	
			
		}
		return batches;
	}
	
	public List<Batch> getBatches(List<String>class_id) {		
		this.batches = batchData.getAllBatchesOfClass(class_id);
		System.out.println("Size of list of subjects :"+this.batches.size());
		for (Batch batch : this.batches) {
			System.out.println("Batch name :"+batch.getBatch_name());	
			
		}
		return batches;
	}
	
	public void setBatchDetailsList() {
		this.batchDetailsList = batchData.getAllBatchDetailsList(class_id);
	}

	public List<BatchDetails> getBatchDetailsList() {
		return batchDetailsList;
	}
	
	public List<Batch> getAllRelatedBatches(int div_id){
		return batchData.getAllRelatedBatchesOfClass(class_id, div_id);
	}
}
