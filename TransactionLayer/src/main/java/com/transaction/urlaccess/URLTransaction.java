package com.transaction.urlaccess;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.urlaccess.URLAccessDB;
import com.classapp.urlaccess.PathAccess;

public class URLTransaction {
	
	/**
	 * @param List<PathAccess> strutsActions - from struts.xml
	 * @return List<PathAccess> mergedList
	 * <p>Merge list from db and struts.xml</p>
	 */
	public List<PathAccess> getURLAndAccess(List<PathAccess> strutsActions){
		URLAccessDB accessDB = new URLAccessDB();
		List<com.classapp.db.urlaccess.PathAccess> pathAccessesDb = accessDB.getURLAndAccess();
		List<PathAccess> pathAccesses = new ArrayList<PathAccess>();
		pathAccesses = mergeList(pathAccessesDb, strutsActions);
		//Merge url if it don't have urls in the database
		return pathAccesses; 
	}
	
	public List<PathAccess> getPathsFromDb(){
		URLAccessDB accessDB = new URLAccessDB();
		List<com.classapp.db.urlaccess.PathAccess> pathAccessesDb = accessDB.getURLAndAccess();
		List<PathAccess>accesses = new ArrayList<PathAccess>();
		for (com.classapp.db.urlaccess.PathAccess pathAccess:pathAccessesDb) {
			PathAccess pathAccessNew = new PathAccess();	
			try {
				BeanUtils.copyProperties(pathAccessNew, pathAccess);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			accesses.add(pathAccessNew);
		}
		
		return accesses;
	}
	
	public boolean setURLAndAccess(PathAccess pathAccess){
		URLAccessDB accessDB = new URLAccessDB();
		com.classapp.db.urlaccess.PathAccess pathAccessDb = new com.classapp.db.urlaccess.PathAccess();
		boolean result = false;
		try {
			BeanUtils.copyProperties(pathAccessDb, pathAccess);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			result = false;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			result = false;
		}
		result = accessDB.setURLAndAccess(pathAccessDb);
		return result;
	}
	
	public List<PathAccess> mergeList(List<com.classapp.db.urlaccess.PathAccess> dbUrl,List<PathAccess> pathAccesses){
		List<PathAccess> mergedList = new ArrayList<PathAccess>();
		for(com.classapp.db.urlaccess.PathAccess pathAccessDb:dbUrl){
			if(null==dbUrl || dbUrl.size()==0){
				
			}else{
			for(PathAccess pathAccess:pathAccesses){
				if (pathAccessDb.getPaths().equalsIgnoreCase(pathAccess.getPaths())){
					pathAccess.setAccess(pathAccessDb.getAccess());
					break;
				}else{
					mergedList.add(pathAccess);
				}
			}
			}
		}
		return pathAccesses;
	}
	
	public boolean setURLAccessBatch(List<PathAccess> listPathAccesses){
		URLAccessDB accessDB = new URLAccessDB();
		List<com.classapp.db.urlaccess.PathAccess> listPathAccessesDb = new ArrayList<com.classapp.db.urlaccess.PathAccess>();
		try {
			for(PathAccess pathAccess:listPathAccesses){
				com.classapp.db.urlaccess.PathAccess pathAccessDb = new com.classapp.db.urlaccess.PathAccess();
				BeanUtils.copyProperties(pathAccessDb, pathAccess);
				listPathAccessesDb.add(pathAccessDb);
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		accessDB.setURLAccessBatch(listPathAccessesDb);
		return false;
	}
	
	public boolean isAcessible(String action,int role){
		URLAccessDB accessDB = new URLAccessDB();
		return accessDB.isAcessible(action, role);
	}
	
}
