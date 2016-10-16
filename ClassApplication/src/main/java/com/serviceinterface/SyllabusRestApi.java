package com.serviceinterface;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.service.beans.SyllabusBean;

@Path("/syllabus")
@Produces(MediaType.APPLICATION_JSON)
public interface SyllabusRestApi {
	@GET
	@Path("/test")
	public String serviceOn();

	@POST
	public Response savePlannedSyllabus(SyllabusBean syllabusBean);
	
	@PUT
	public Response editPlannedSyllabus(SyllabusBean syllabusBean);
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePlannedSyllabus(SyllabusBean syllabusBean);

	@GET
	@Path("/{yyyymm}")
	Response getPlannedSyllabusForMonth(@PathParam("yyyymm") String yyyymm,
			@QueryParam("instId")int instId,
			@QueryParam("classId")int classId,
			@QueryParam("subjectId")int subId,
			@QueryParam("batchId")String batchId);
	
	
}
