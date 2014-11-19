package com.custom.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class Switch extends SimpleTagSupport {
	private String switchId;
	@Override
	public void doTag() throws JspException, IOException {
		if(switchId.equals("23")){
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
