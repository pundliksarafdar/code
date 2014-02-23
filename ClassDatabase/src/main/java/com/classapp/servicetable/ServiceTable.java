package com.classapp.servicetable;

import java.io.Serializable;

public class ServiceTable implements Serializable{
	/**
	 * Class to set service servicetable data
	 */
	private static final long serialVersionUID = 1L;
	private Integer serviceId;
	private String serviceUrl;
	private String serviceName;
	private String note;
	private String serviceParam;
	
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceUrl() {
		return serviceUrl;
	}
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getServiceParam() {
		return serviceParam;
	}
	public void setServiceParam(String serviceParam) {
		this.serviceParam = serviceParam;
	}
	
	
}
