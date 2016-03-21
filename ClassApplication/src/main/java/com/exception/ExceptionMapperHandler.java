package com.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapperHandler implements ExceptionMapper<NoUserBeanException>{

	@Override
	public Response toResponse(NoUserBeanException exception) {
		// TODO Auto-generated method stub
		ExceptionBean exceptionBean = new ExceptionBean();
		exceptionBean.setCode("NO_USER_BEAN");
		exceptionBean.setMessage(exception.getMessage());
		return Response.status(Status.BAD_REQUEST).entity(exceptionBean).type(MediaType.APPLICATION_JSON).build();
	}

}
