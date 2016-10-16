package com.service;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.transaction.filter.FilterTransaction;

@Path("filter")
@Produces(MediaType.APPLICATION_JSON)
public class FilterServiceImpl extends ServiceBase{
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getFilter(@QueryParam("division")List<Integer> classIds,
			@QueryParam("subjects")List<Integer> subjectIds,
			@QueryParam("batchs")List<Integer>batchIds,
			@QueryParam("teacher")List<Integer>teacherId){
		FilterTransaction transaction = new FilterTransaction();
		
		HashMap<String, List<Integer>>filterMap = new HashMap<String, List<Integer>>();
		filterMap.put("subjectId", subjectIds);
		filterMap.put("divId", classIds);
		filterMap.put("user_id", teacherId);
		HashMap<String, List> result = transaction.getFilteredResult(filterMap, getRegId());
		return Response.ok(result).build();
	}
}
