
package com.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

@Path("/commonservices")
public class CorexServiceApi extends ServiceBase{
	
	private static final String UPLOADED_FILE_PATH = "C:"+File.separatorChar+"imageuploaded"+File.separatorChar;
	@Context
	private HttpServletRequest request; 

	@GET
	@Path("/test")
	public Response serviceOn(){
		String dob = (String) request.getSession().getAttribute("userdob");
		System.out.println("Hello................"+dob);
		return Response.status(200).entity("Successfullllll on"+dob).build();
		
	}
	
	@GET
	@Path("/showimage/{imageType}/{imageId}")
	@Produces("image/jpeg")
	public byte[] getImageRepresentation(@PathParam("imageId")String imageId,@PathParam("imageType")String imageType){
		String imagePath = null;
		//String imgFolder = Constants.STORAGE_PATH + File.separatorChar+ getRegId() + File.separatorChar + imageId;
		if (com.classapp.utils.Constants.IMAGE_TYPE.L.toString().equalsIgnoreCase(imageType)){
			imagePath = com.classapp.utils.Constants.LOGO_IMAGE_PATH; 
		}
		
		String imagefileName = Constants.STORAGE_PATH + File.separatorChar+ imagePath + File.separatorChar +  imageId;
		try {
			FileInputStream stream = new FileInputStream(imagefileName);
			InputStream resourceStream = new BufferedInputStream(stream);
			return IOUtils.toByteArray(resourceStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GET
	@Path("/image/{folderPath}")
	@Produces("image/jpeg")
	public byte[] showImage(@PathParam("folderPath")String folderPath){
		String folderActualPath = folderPath.replaceAll("_", "\\"+File.separatorChar); 
		String imagefileName = Constants.STORAGE_PATH + File.separatorChar+ getRegId() + File.separatorChar + folderActualPath;
		try {
			FileInputStream stream = new FileInputStream(imagefileName);
			InputStream resourceStream = new BufferedInputStream(stream);
			return IOUtils.toByteArray(resourceStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@POST
	@Path("/uploadImage")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadImage(MultipartFormDataInput input){
		String fileName="";
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");
		
		for (InputPart inputPart : inputParts) {

			 try {
				 	
				MultivaluedMap<String, String> header = inputPart.getHeaders();
				fileName = getFileName(header);

				//convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class,null);

				byte [] bytes = IOUtils.toByteArray(inputStream);
					
				//constructs upload file path
				fileName = UPLOADED_FILE_PATH + fileName;
					
				writeFile(bytes,fileName);
					
				System.out.println("Done");

			  } catch (IOException e) {
				e.printStackTrace();
			  }

			}
		return Response.status(200).entity("success").build();
	}
	
	@POST
	@Path("/uploadImageTemp")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.MULTIPART_FORM_DATA)
	public Response uploadImageTemp(MultipartFormDataInput input){
		String fileName="";
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");
		
		for (InputPart inputPart : inputParts) {

			 try {
				 	
				MultivaluedMap<String, String> header = inputPart.getHeaders();
				fileName = getFileName(header);
				
				//Extract file extention and replace filename with fileid
				int extentionStart = fileName.lastIndexOf(".");
				String fileId = UUID.randomUUID().toString()+getRegId();
				fileName = fileId+fileName.substring(extentionStart);
				//convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class,null);

				byte [] bytes = IOUtils.toByteArray(inputStream);
					
				//constructs upload file path
				
				//create image tempfolder if not exist
				String imgTempFolder = Constants.STORAGE_PATH + File.separatorChar+ "imageTemp";
				File file = new File(imgTempFolder);
				if(!file.exists()){
					file.mkdirs();
				}
				
				//Above generated folder will be used to save the image temparirily on successfull call files will be moved to main folder and old files will be deleted
				String filePath = Constants.STORAGE_PATH + File.separatorChar+ "imageTemp" + File.separatorChar + getRegId() + fileName ;
					
				writeFile(bytes,filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}

			}
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("fileid", fileName);
		//jsonObject.
		return Response.status(200).entity(jsonObject.toString()).type(MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * header sample
	 * {
	 * 	Content-Type=[image/png], 
	 * 	Content-Disposition=[form-data; name="file"; filename="filename.extension"]
	 * }
	 **/
	//get uploaded filename, is there a easy way in RESTEasy?
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");
				
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	//save to somewhere
	private void writeFile(byte[] content, String filename) throws IOException {

		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
}
