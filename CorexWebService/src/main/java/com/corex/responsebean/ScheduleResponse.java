package com.corex.responsebean;

import com.classapp.schedule.ScheduleBean;


public class ScheduleResponse extends ResultBean{
	
	private ScheduleBean scheduleBean ;

	public ScheduleBean getScheduleBean() {
		return scheduleBean;
	}

	public void setScheduleBean(ScheduleBean scheduleBean) {
		this.scheduleBean = scheduleBean;
	}
	
	
	/*
	public ResultBean getResultBean() {
		if(null == resultBean){
			resultBean = new ResultBean();
		}
		return resultBean;
	}
	public void setResultBean(ResultBean resultBean) {
		this.resultBean = resultBean;
	}
	
	public void setCode(String code, String message) {
		getResultBean().setCode(code);
		getResultBean().setMessage(message);
	}
	*/
	
}
