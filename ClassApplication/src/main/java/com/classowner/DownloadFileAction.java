package com.classowner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.persistence.Constants;
import com.config.BaseAction;
import com.opensymphony.xwork2.ActionSupport;
import com.user.UserBean;

public class DownloadFileAction extends BaseAction{


@Override
public String performBaseAction(UserBean userBean, HttpServletRequest request, HttpServletResponse response,
		Map<String, Object> session) {
	response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition","attachment;filename=SampleStudent.xls");

    try
    {
      	//Get it from file system
    	String instituteSampleFilepath =userBean.getUserStatic().getStorageSpace()+File.separator+"SampleFiles"+File.separator+"SampleStudent.xls";
    	String globalSampleFilepath =com.config.Constants.STORAGE_PATH+File.separator+"SampleStudent.xls";
    	File file = new File(instituteSampleFilepath);
    	if(!file.exists()){
    		file= new File(globalSampleFilepath);
    	}
    	
      	FileInputStream in =
     		new FileInputStream(file);

          ServletOutputStream out = response.getOutputStream();

       byte[] outputByte = new byte[4096];
       //copy binary content to output stream
       while(in.read(outputByte, 0, 4096) != -1){
       	out.write(outputByte, 0, 4096);
       }
       in.close();
       out.flush();
       out.close();

    }catch(Exception e){
   	e.printStackTrace();
  }

  return null;
}
}