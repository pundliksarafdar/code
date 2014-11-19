package com.corex.iservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.corex.requestbean.IRequest;
import com.corex.responsebean.IResponse;
import com.corex.responsebean.LoginResponse;
import com.corex.responsebean.ScheduleResponse;

@Path("/corex")
public interface IService {
	
	@GET
	@Path("/test")
	public String test();
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResponse login(IRequest request);

	@POST
	@Path("/getscheduledate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ScheduleResponse getScheduleByDate(IRequest request);

}
