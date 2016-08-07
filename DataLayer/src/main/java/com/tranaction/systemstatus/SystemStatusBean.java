package com.tranaction.systemstatus;

import java.util.List;

public class SystemStatusBean {
	private double freeRamSpace;
	private double usedRamSpace;
	private double totalRamSpace;
	private double freeDiskSpace;
	private double usedDiskSpace;
	private double totalDiskSpace;
	private double freePhysicalMemory;
	private double totalPhysicalMemory;
	private double usedPhysicalMemory;
	
	private String userDir;
	private List<FolderStruct> folderStructs;
	
	class FolderStruct{
		private String folderName;
		private String username;
		private double  usedDiskSpace;
	}

	public double getFreeRamSpace() {
		return freeRamSpace;
	}

	public void setFreeRamSpace(double freeRamSpace) {
		this.freeRamSpace = freeRamSpace;
	}

	public double getUsedRamSpace() {
		return usedRamSpace;
	}

	public void setUsedRamSpace(double usedRamSpace) {
		this.usedRamSpace = usedRamSpace;
	}

	public double getTotalRamSpace() {
		return totalRamSpace;
	}

	public void setTotalRamSpace(double totalRamSpace) {
		this.totalRamSpace = totalRamSpace;
	}

	public double getFreeDiskSpace() {
		return freeDiskSpace;
	}

	public void setFreeDiskSpace(double freeDiskSpace) {
		this.freeDiskSpace = freeDiskSpace;
	}

	public double getUsedDiskSpace() {
		return usedDiskSpace;
	}

	public void setUsedDiskSpace(double usedDiskSpace) {
		this.usedDiskSpace = usedDiskSpace;
	}

	public double getTotalDiskSpace() {
		return totalDiskSpace;
	}

	public void setTotalDiskSpace(double totalDiskSpace) {
		this.totalDiskSpace = totalDiskSpace;
	}

	public List<FolderStruct> getFolderStructs() {
		return folderStructs;
	}

	public void setFolderStructs(List<FolderStruct> folderStructs) {
		this.folderStructs = folderStructs;
	}

	public String getUserDir() {
		return userDir;
	}

	public void setUserDir(String userDir) {
		this.userDir = userDir;
	}

	public double getFreePhysicalMemory() {
		return freePhysicalMemory;
	}

	public void setFreePhysicalMemory(double freePhysicalMemory) {
		this.freePhysicalMemory = freePhysicalMemory;
	}

	public double getTotalPhysicalMemory() {
		return totalPhysicalMemory;
	}

	public void setTotalPhysicalMemory(double totalPhysicalMemory) {
		this.totalPhysicalMemory = totalPhysicalMemory;
	}

	public double getUsedPhysicalMemory() {
		return usedPhysicalMemory;
	}

	public void setUsedPhysicalMemory(double usedPhysicalMemory) {
		this.usedPhysicalMemory = usedPhysicalMemory;
	}
	
	

	}
