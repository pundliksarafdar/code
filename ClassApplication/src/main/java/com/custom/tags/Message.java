package com.custom.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.classapp.persistence.Constants;
import com.classapp.servicetable.ServiceMap;

public class Message extends SimpleTagSupport {
	private String messagename;
	private String filename;
	@Override
	public void doTag() throws JspException, IOException {
		String message = "Message not defined";
		JspWriter out = getJspContext().getOut();
	    out.println(message);
	}
	public String getMessagename() {
		return messagename;
	}
	public void setMessagename(String messagename) {
		this.messagename = messagename;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
}
