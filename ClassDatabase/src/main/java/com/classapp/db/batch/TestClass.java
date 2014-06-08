package com.classapp.db.batch;

public class TestClass {

	private TestClass() {
		
	}
	public static void main(String[] args) {
		Batch batch = new Batch();
		batch.setBatch("batchName");
		batch.setRegId(34);
		BatchDB batchDB = new BatchDB();
		batchDB.updateDb(batch);
	}

}
