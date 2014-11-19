package com.corex.responsebean;


public class IResponse {
	
	private Object object;
	private ResultBean resultBean;
	
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
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
	
	
}
