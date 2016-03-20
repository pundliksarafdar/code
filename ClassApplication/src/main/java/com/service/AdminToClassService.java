package com.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.JsonObject;
import com.service.beans.ClassSearchForm;
import com.service.beans.InstituteStats;
import com.tranaction.admintoclass.AdminToClassTransaction;

@Path("/admin/classes") 
public class AdminToClassService extends ServiceBase{
	
	@GET
	@Path("/classlist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchClass(){
		AdminToClassTransaction adminToClassTransaction = new AdminToClassTransaction();
		List<InstituteStats> instituteStats = adminToClassTransaction.searchClassData();
		return Response.status(200).entity(instituteStats).build();
	}
	
	@PUT
	@Path("/classlist")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateClass(InstituteStats instituteStats){
		AdminToClassTransaction adminToClassTransaction = new AdminToClassTransaction();
		boolean status = adminToClassTransaction.updateFeatures(instituteStats);
		return Response.status(Status.OK).build();
	}
	
}
