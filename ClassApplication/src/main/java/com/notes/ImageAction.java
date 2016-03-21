package com.notes;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.user.UserBean;

public class ImageAction extends BaseAction {

	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		
		String imageId=(String) request.getParameter("imageId");
	//	String image=(String) request.getSession().getAttribute(imageId);
		try {
			BufferedImage bufferedImage= (BufferedImage) request.getSession().getAttribute(imageId); 
			OutputStream stream=response.getOutputStream();
			 ImageIO.write(bufferedImage, "png", stream);
			 request.getSession().setAttribute(imageId, "");
			//response.getOutputStream().print(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
