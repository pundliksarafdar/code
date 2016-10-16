package com.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.classapp.logger.AppLogger;
import com.classapp.logger.TracingErrorPrintStream;
import com.classapp.logger.TracingPrintStream;
import com.classapp.persistence.Constants;
import com.classapp.servicetable.ServiceMap;
import com.transaction.urlaccess.URLTransaction;

public class LoadConfig extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		ServiceMap.loadServiceMap();
		TimeZone.setDefault(TimeZone.getTimeZone("IST"));
		try {
			URL resource = getServletContext().getResource("/WEB-INF/classes/struts.xml");
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(resource.openStream()));
					
			        String inputLine,xmlString = "";
			        while ((inputLine = in.readLine()) != null){
			            xmlString += inputLine;
			        }
			        
			        com.config.Constants.ALL_ACTION_URLs  = getPathFromXml(xmlString);
			        in.close();
			
		} catch (MalformedURLException e) {	
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		{
		ServletContext servletContext = getServletContext();
		String servletPath = getServletContext().getRealPath("/");
		File file = new File(servletPath);
		String parent = System.getProperty("catalina.base");
		/*
		do{
			parent = file.getParent();
			file = new File(parent);
		}while(!parent.endsWith("standalone"));
		*/
		//parent = file.getParent();
		System.setProperty("catalina.home", parent);
		try{
		file = new File(parent+"/storage");
		if(!file.exists()){
			file.mkdir();
		}
		
		com.config.Constants.STORAGE_PATH = file.getAbsolutePath();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		}
		
		URLTransaction urlTransaction = new URLTransaction();
		com.classapp.utils.Constants.PATH_ACCESS_LIST = urlTransaction.getPathsFromDb();
		String level=ServiceMap.getSystemParam(Constants.SERVICE_LOGGER, "level");
		if("INFO".equals(level)){
		Logger.getLogger(AppLogger.class).setLevel(Level.INFO);
		}else if("ERROR".equals(level)){
			Logger.getLogger(AppLogger.class).setLevel(Level.ERROR);
			}else {
				Logger.getLogger(AppLogger.class).setLevel(Level.OFF);
			}
		System.setOut(new TracingPrintStream(System.out));
		System.setErr(new TracingErrorPrintStream(System.err));
	
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			doPost(req, resp);
			
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		init();
		resp.getWriter().write("Loaded successfully");
		
		
	}
	
	static List<String> getPathFromXml(String xml){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Node actionPath;
		List<String>actionList = new ArrayList<String>();
		try {
		    db = dbf.newDocumentBuilder();
		    InputSource is = new InputSource();
		    StringReader stringReader = new StringReader(xml);
		    is.setCharacterStream(stringReader);
		    try {
		        Document doc = db.parse(is);
		        NodeList actions = doc.getChildNodes();
		        int size = actions.getLength();
		        for (int i = 0;i<size;i++) {
		        	actionPath = actions.item(i);
		        	NodeList node = actionPath.getChildNodes();
		        	int size1 = node.getLength();
		        	for (int i1 = 0;i1<size1;i1++) {
		        		Node actionPath1 = node.item(i1);
			        	if(actionPath1.getNodeName().equals("package")){
			        		NodeList actionPacage = actionPath1.getChildNodes();
			        		int actionSize = actionPacage.getLength();
				        	for (int iAction = 0;iAction<actionSize;iAction++) {
				        		Node actionNode = actionPacage.item(iAction);
					        	//AppLogger.logger("Action Name:"+actionNode.getNodeName());
					        	if (actionNode.getNodeName().equals("action")) {
					        		actionList.add(actionNode.getAttributes().getNamedItem("name").getNodeValue());
								}
				        	}
			        	}
			        	
					}
		        	
				}
		        
		    } catch (SAXException e) {
		    	e.printStackTrace();
		        // handle SAXException
		    } catch (IOException e) {
		        // handle IOException
		    	e.printStackTrace();
		    }
		} catch (ParserConfigurationException e1) {
		    e1.printStackTrace();
		}
		return actionList;
	}

}
