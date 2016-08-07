package com.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tranaction.systemstatus.SystemStatusBean;
import com.tranaction.systemstatus.SystemStatusTransaction;

@Path("/admin/status") 
public class AdminStatusService extends ServiceBase{
	
	@GET
	@Path("/systemStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSystemStatus(){
		SystemStatusTransaction systemStatusTransaction = new SystemStatusTransaction();
		SystemStatusBean statusBean = systemStatusTransaction.getSystemStatus();
		return Response.ok(statusBean).build();
	}
}
