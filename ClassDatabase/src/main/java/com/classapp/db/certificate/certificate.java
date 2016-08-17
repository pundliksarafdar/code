package com.classapp.db.certificate;

public class certificate {
	int inst_id;
	int cert_id;
	String cert_desc;
	String header_id;
	public int getInst_id() {
		return inst_id;
	}
	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}
	public int getCert_id() {
		return cert_id;
	}
	public void setCert_id(int cert_id) {
		this.cert_id = cert_id;
	}
	public String getCert_desc() {
		return cert_desc;
	}
	public void setCert_desc(String cert_desc) {
		this.cert_desc = cert_desc;
	}
	public String getHeader_id() {
		return header_id;
	}
	public void setHeader_id(String header_id) {
		this.header_id = header_id;
	}
	
}
