package com.tranaction.systemstatus;

import java.io.File;

public class SystemStatusTransaction {
	public static SystemStatusBean getSystemStatus(){
		SystemStatusBean systemStatusBean = new SystemStatusBean();
		Runtime runtime = Runtime.getRuntime();
		long freeMemory = runtime.freeMemory();
		long totalMemory = runtime.totalMemory();
		
		long usedMem = totalMemory - freeMemory;
		systemStatusBean.setFreeRamSpace(freeMemory/(1024.0*1024.0));
		systemStatusBean.setTotalRamSpace(totalMemory/(1024.0*1024.0));
		systemStatusBean.setUsedRamSpace(usedMem/(1024.0*1024.0));
	
		String userDir = System.getProperty("user.dir");
		systemStatusBean.setUserDir(userDir);
		
		File file = new File(userDir);
		long totalSpace = file.getTotalSpace();
		long freeSpace = file.getFreeSpace();
		long usedSpace = totalSpace - freeSpace;
		
		systemStatusBean.setFreeDiskSpace(freeSpace/(1024.0*1024.0));
		systemStatusBean.setTotalDiskSpace(totalSpace/(1024.0*1024.0));
		systemStatusBean.setUsedDiskSpace(usedSpace/(1024.0*1024.0));
		
		com.sun.management.OperatingSystemMXBean bean =
				  (com.sun.management.OperatingSystemMXBean)
				    java.lang.management.ManagementFactory.getOperatingSystemMXBean();
		
		long totalPhysicalMemory = bean.getTotalPhysicalMemorySize();
		long freePhysicalMemory = bean.getFreePhysicalMemorySize();
		long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;
		
		systemStatusBean.setTotalPhysicalMemory(totalPhysicalMemory/(1024.0*1024.0));
		systemStatusBean.setFreePhysicalMemory(freePhysicalMemory/(1024.0*1024.0));
		systemStatusBean.setUsedPhysicalMemory(usedPhysicalMemory/(1024.0*1024.0));
		return systemStatusBean;
	}
	
	public static void main(String[] args) {
		SystemStatusBean systemStatusBean = getSystemStatus();
		System.out.println();
	}
}
