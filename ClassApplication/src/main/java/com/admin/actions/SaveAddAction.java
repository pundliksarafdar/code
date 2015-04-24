package com.admin.actions;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.user.UserBean;

public class SaveAddAction extends BaseAction {

	String globaladd;
	String advertiseOptionCountry;
	String advertiseOptionState;
	String advertiseOptionCity;
	String textadvertise;

	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// System.out.println(globaladd+":"+advertiseOptionCountry+":"+advertiseOptionState+":"+advertiseOptionCity+":"+textadvertise);
		if ("no".equals(globaladd)) {
			if (advertiseOptionCountry == null
					|| advertiseOptionCountry.trim().length() == 0) {
				addActionError("Please select country");	
			}

			if (advertiseOptionState == null
					|| advertiseOptionState.trim().length() == 0) {
				addActionError("Please select State");
			}

			if (advertiseOptionCity == null
					|| advertiseOptionCity.trim().length() == 0) {
				addActionError("Please select City");
			}

			if (textadvertise == null
					|| textadvertise.trim().length() == 0) {
				addActionError("Please insert advertising text");
			}
		}
		return SUCCESS;
	}

	public String getGlobaladd() {
		return globaladd;
	}

	public void setGlobaladd(String globaladd) {
		this.globaladd = globaladd;
	}

	public String getAdvertiseOptionCountry() {
		return advertiseOptionCountry;
	}

	public void setAdvertiseOptionCountry(String advertiseOptionCountry) {
		this.advertiseOptionCountry = advertiseOptionCountry;
	}

	public String getAdvertiseOptionState() {
		return advertiseOptionState;
	}

	public void setAdvertiseOptionState(String advertiseOptionState) {
		this.advertiseOptionState = advertiseOptionState;
	}

	public String getTextadvertise() {
		return textadvertise;
	}

	public void setTextadvertise(String textadvertise) {
		this.textadvertise = textadvertise;
	}

	public String getAdvertiseOptionCity() {
		return advertiseOptionCity;
	}

	public void setAdvertiseOptionCity(String advertiseOptionCity) {
		this.advertiseOptionCity = advertiseOptionCity;
	}

}
