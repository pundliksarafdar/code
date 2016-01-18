
package com.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.spi.HttpRequest;

import com.config.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.service.beans.TimetableBean;

@Path("/timetableservices")
public class TimetableServiceApi extends ServiceBase{
	enum VIEW{month,week,day};
	
	@Context
	private HttpServletRequest request; 

	@GET
	@Path("/timetable/{view}/{date}/{classid}/{batch}/{subject}/{techer}")
	public Response getTimetableData(@DefaultValue("month")@PathParam("view")String view,
			@DefaultValue("0")@PathParam("date")Long date,
			@PathParam("classid")Integer classid,
			@PathParam("batch")Integer batch,
			@PathParam("subject")Integer subject,
			@PathParam("techer")Integer techer){
		//date is reference date should calculate start and end date according to view
		List<TimetableBean> timetableBeans = new ArrayList<TimetableBean>();
		return Response.status(Response.Status.OK).entity(timetableBeans).build();
	}
	
	@POST
	@Path("/timetable")
	public Response createTimetable(TimetableBean timetableBean){
		//Timetable id is will generate on and return on successful saving
		return Response.status(Response.Status.OK).entity(timetableBean).build();
	}
	
	@PUT
	@Path("/timetable")
	public Response modifyTimetable(TimetableBean timetableBean){
		//Timetable id is used to modify the timetable
		return Response.status(Response.Status.OK).entity(timetableBean).build();
	}
	
	@DELETE
	@Path("/timetable/{classid}/{batch}/{subject}/{techer}/{timetableid}")
	public Response modifyTimetable(@PathParam("classid")Integer classid,
			@PathParam("batch")Integer batch,
			@PathParam("subject")Integer subject,
			@PathParam("techer")Integer techer,
			@PathParam("timetableid")Integer timetableid){
		//Timetable id is used to delete the timetable based on other parameter
		return Response.status(Response.Status.OK).build();
	}
}
