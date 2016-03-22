package com.classapp.servicetable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.hibernate.Session;

import com.classapp.logger.AppLogger;
import com.classapp.persistence.Constants;
import com.classapp.persistence.HibernateUtil;

public class ServiceMap {
	
	private ServiceMap(){
		
	}
	public static Map SERVICEMAP = new HashMap();
	@SuppressWarnings("unchecked")
	public static void loadServiceMap(){
		Session session = HibernateUtil.getSessionfactory().openSession();
		AppLogger.logger("In Load Service map");
		
		try{			
			List services = session.createQuery("from ServiceTable").list();
			for (int i=0; i<services.size(); i++) {
				Map dataMap = new HashMap();
				AppLogger.logger("Service Map:"+((ServiceTable)services.get(i)).getServiceId());
				dataMap.put(Constants.SERVICE_ID, ((ServiceTable)services.get(i)).getServiceId());
				dataMap.put(Constants.SERVICE_NAME, ((ServiceTable)services.get(i)).getServiceName());
				dataMap.put(Constants.SERVICE_PARAM, ((ServiceTable)services.get(i)).getServiceParam());
				dataMap.put(Constants.SERVICE_URL, ((ServiceTable)services.get(i)).getServiceUrl());
				dataMap.put(Constants.SERVICE_NOTE, ((ServiceTable)services.get(i)).getNote());
				SERVICEMAP.put(((ServiceTable)services.get(i)).getServiceId().toString(), dataMap);
				
			}
		}catch(Exception ex){
			AppLogger.logger("Exception in servicemap..."+ex.getMessage());
			ex.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}	
	}
	
	public static String getSystemParam(String serviceId,String serviceParam){
		String serviceParamValue="";
		
		Map mapTemp = (Map) SERVICEMAP.get(serviceId);
		if (null!=mapTemp && mapTemp.containsKey(Constants.SERVICE_PARAM)) {
			String serviceParam1 = (String) mapTemp.get(Constants.SERVICE_PARAM);
			StringTokenizer stringTokenizer = new StringTokenizer(serviceParam1,";");
			
			while (stringTokenizer.hasMoreElements()) {
				String params = stringTokenizer.nextToken();
				if(params.toLowerCase().startsWith(serviceParam.toLowerCase())){
					serviceParamValue = params.substring(params.indexOf('=')+1);
				}
			}
		}
		
		return serviceParamValue;
	}
}
