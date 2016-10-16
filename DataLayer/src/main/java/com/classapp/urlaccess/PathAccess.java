package com.classapp.urlaccess;

public class PathAccess {
	String paths;
	String access;
	
	public String getPaths() {
		return paths;
	}
	public void setPaths(String paths) {
		this.paths = paths;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.paths.equals(((PathAccess)obj).getPaths());
	}
	
	@Override
	public int hashCode() {
		return this.paths.hashCode();
	}
}
