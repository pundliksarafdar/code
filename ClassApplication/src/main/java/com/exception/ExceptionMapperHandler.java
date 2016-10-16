package com.exception;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapperHandler implements ExceptionMapper<Exception>{
	private static Properties EXCEPTION_MAP;
	
	@Override
	public Response toResponse(Exception exception) {
		if(null==EXCEPTION_MAP || EXCEPTION_MAP.isEmpty()){
			EXCEPTION_MAP = getExceptionProperties();
		}
		
		
		// TODO Auto-generated method stub
		ExceptionBean exceptionBean = new ExceptionBean();
		exceptionBean.setCode(exception.getMessage());
		exceptionBean.setMessage(EXCEPTION_MAP.getProperty(exception.getMessage()));
		exception.printStackTrace();
		return Response.status(Status.BAD_REQUEST).entity(exceptionBean).type(MediaType.APPLICATION_JSON).build();
	}
	
	private Properties getExceptionProperties(){
		InputStream fileInput;
		Properties properties = new Properties();
		try {
			fileInput = getClass().getClassLoader().getResourceAsStream("exception.properties");
			properties.load(fileInput);
			fileInput.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}

}
