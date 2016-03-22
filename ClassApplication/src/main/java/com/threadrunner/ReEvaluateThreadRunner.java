package com.threadrunner;

import com.classapp.logger.AppLogger;

public class ReEvaluateThreadRunner extends Thread{
	@Override
	public void run() {
		AppLogger.logger("Starting thread....");
		try {
			Thread.sleep(5*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AppLogger.logger("Ending thread......");
	}
	
	public ReEvaluateThreadRunner() {
		this.start();
	}
}
