package com.custom.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.classapp.persistence.Constants;
import com.classapp.servicetable.ServiceMap;

public class Switch extends SimpleTagSupport {
	private String switchId;
	@Override
	public void doTag() throws JspException, IOException {
		boolean isOn = false;
		String swithcValue = ServiceMap.getSystemParam(switchId, "on");
		
		if(null!= swithcValue && "true".equals(swithcValue)){
			isOn = true;
		}
		if(isOn){
		StringWriter stringWriter = new StringWriter();
		getJspBody().invoke(stringWriter);
			getJspContext().getOut().println(stringWriter.toString()+switchId);
		}
	}
	public String getSwitchId() {
		return switchId;
	}
	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}
	
	
}
