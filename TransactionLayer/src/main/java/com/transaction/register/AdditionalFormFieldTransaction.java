package com.transaction.register;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.register.AdditionalFormFieldBean;
import com.classapp.db.register.AdditionalFormFieldBeanDl;
import com.classapp.db.register.AdditionalFormFieldDb;
import com.google.gson.Gson;

public class AdditionalFormFieldTransaction {
	public void saveAdditionalFormField(HashMap<String, String> jsonObject,int instId){
		AdditionalFormFieldDb additionalFormFieldDb = new AdditionalFormFieldDb();
		AdditionalFormFieldBeanDl additionalFormFieldBeanDl = new AdditionalFormFieldBeanDl();
		Gson gson = new Gson(); 
		String jsonStr = gson.toJson(jsonObject);
		additionalFormFieldBeanDl.setFormField(jsonStr);
		additionalFormFieldBeanDl.setInstId(instId);
		AdditionalFormFieldBean additionalFormFieldBean = new AdditionalFormFieldBean();
		try {
			BeanUtils.copyProperties(additionalFormFieldBean, additionalFormFieldBeanDl);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		additionalFormFieldDb.saveAdditionalFormField(additionalFormFieldBean);
	}
	
	public AdditionalFormFieldBeanDl getAdditionalFormFieldBean(int instId){
		AdditionalFormFieldBean additionalFormFieldBean = new AdditionalFormFieldBean();
		AdditionalFormFieldDb additionalFormFieldDb = new AdditionalFormFieldDb();
		additionalFormFieldBean = additionalFormFieldDb.getAdditionalFormFieldBean(instId);
		AdditionalFormFieldBeanDl additionalFormFieldBeanDl = new AdditionalFormFieldBeanDl();
		try {
			BeanUtils.copyProperties(additionalFormFieldBeanDl,additionalFormFieldBean);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return additionalFormFieldBeanDl;
	}
}
