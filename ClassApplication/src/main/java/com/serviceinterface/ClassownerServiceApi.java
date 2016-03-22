package com.serviceinterface;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.service.beans.AddBatchBean;

@Path("/classownerservice") 
public interface ClassownerServiceApi {
	@GET
	@Path("/test")
	public Response serviceOn();
	
	@POST
	@Path("/addBatch")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addBatch(AddBatchBean addBatchBean);
	
	@POST
	@Path("/addLogoImages/{imageId}/{imageName}")
	@Produces("application/json")
	public Response addLogoImage(@PathParam("imageId") String imageId,@PathParam("imageName") String imageName);
	
	@GET
	@Path("/logoImages")
	@Produces("application/json")
	public Response logoImage();

	@POST
	@Path("/saveHeader/headerName")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces("application/json")
	public Response saveHeader(String headerElement,@PathParam("headerName")String headerName);

	
	
}
