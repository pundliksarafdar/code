package com.threadrunner;

public class ReEvaluateThreadRunner extends Thread{
	@Override
	public void run() {
		System.out.println("Starting thread....");
		try {
			Thread.sleep(5*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Ending thread......");
	}
	
	public ReEvaluateThreadRunner() {
		this.start();
	}
}
