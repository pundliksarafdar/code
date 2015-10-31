package com.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class CorexServiceApi {
	@GET
	@Path("/test")
	public Response serviceOn(){
		System.out.println("Hello................");
		return Response.status(200).entity("Successfullllll on").build();
		
	}
}
