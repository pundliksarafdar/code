package com.corex.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.notice.StaffNotice;
import com.classapp.db.roll.Inst_roll;
import com.classapp.schedule.Scheduledata;
import com.datalayer.division.InstituteData;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.instroll.InstRollTransaction;
import com.transaction.notice.NoticeTransaction;
import com.transaction.schedule.ScheduleTransaction;
import com.transaction.teacher.TeacherTransaction;

@Path("/corex/institute")
@Produces(MediaType.APPLICATION_JSON)
public class InstituteService extends MobileServiceBase{
	@GET
	@Path("instituteStats")
	@Produces(MediaType.APPLICATION_JSON) 
	public Response getInstitute(){
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		//List<InstituteData> division = divisionTransactions.getAllBatches(getRegId());
		return Response.ok().build();
	}
	
	@GET
	@Path("instituteTeachers")
	@Produces(MediaType.APPLICATION_JSON) 
	public Response getTeacherInClass(){
		TeacherTransaction teacherTransaction = new TeacherTransaction(); 
		return Response.ok(teacherTransaction.getTeachersInClass(getRegId())).build();
	}
	
	
	@GET
	@Path("/getStaffNotice")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStaffNotice(){
		NoticeTransaction noticeTransaction = new NoticeTransaction();
		List<StaffNotice> staffNoticeList =  noticeTransaction.getStaffNotice(getRegId());
		InstRollTransaction rollTransaction = new InstRollTransaction();
		List<Inst_roll> roleList = rollTransaction.getInstituteRoles(getRegId());
		List<com.datalayer.notice.StaffNotice> list = new ArrayList<com.datalayer.notice.StaffNotice>(); 
		if(staffNoticeList != null){
			for (StaffNotice staffNotice : staffNoticeList) {
				com.datalayer.notice.StaffNotice notice = new com.datalayer.notice.StaffNotice();
				try {
					BeanUtils.copyProperties(notice, staffNotice);
				} catch (IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!"0".equals(notice.getRole())){
					String [] roleArray = notice.getRole().split(",");
					String roleString = "";
					for (String string : roleArray) {
						if("2s".equalsIgnoreCase(string)){
							roleString = roleString + "Teacher,";
						}else{
						List<Inst_roll> roles = roleList.stream().filter(Inst_roll -> (Inst_roll.getRoll_id()+"c").equalsIgnoreCase(string.trim())).collect(Collectors.toList());
						if(roles != null){
							if(roles.size() >0){
								roleString = roleString + roles.get(0).getRoll_desc()+",";
							}
						}
					}
					}
					if(roleString.length()>0){
						roleString = roleString.substring(0, roleString.length()-1);
					}
					notice.setRole_Desc(roleString);
				}else{
					notice.setRole_Desc("All");
				}
				list.add(notice);
			}
		}
		return Response.status(200).entity(list).build();
	}
	
	@GET
	@Path("todaysLecture")
	@Produces(MediaType.APPLICATION_JSON) 
	public Response getTodaysLecture(@QueryParam("classids")List<Integer>classids){
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<Scheduledata> scheduledatas=scheduleTransaction.getteacherstodaysSchedule(classids, getRegId());
		return Response.ok(scheduledatas).build();
	}

}
