package com.classapp.servicetable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class SiteMap {
	public static List<SiteMapData> getSiteMapList(){
		List<SiteMapData> siteMapDatas = new ArrayList<SiteMapData>();
		
		for(int i=0;i<10;i++){
			SiteMapData siteMapData = new SiteMapData();
			siteMapData.setSrno("1"+i);
			siteMapData.setHref("href1"+i);
			siteMapData.setLinkName("link1"+i);
			siteMapData.setTitle("title1"+i);
			siteMapDatas.add(siteMapData);
		}
		return siteMapDatas;
	}
	
	
}
