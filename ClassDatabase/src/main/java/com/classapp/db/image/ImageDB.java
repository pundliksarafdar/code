package com.classapp.db.image;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.Feedbacks.Feedback;
import com.classapp.db.exam.Exam;
import com.classapp.persistence.HibernateUtil;

public class ImageDB {
	public void save(Image image) {
		
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.save(image);
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
	}
	
public Image getImageByID(int inst_id,String image_id) {
		
		Session session = null;
		Transaction transaction = null;
		Image image=new Image();
		image.setImage_id(image_id);
		image.setInst_id(inst_id);
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			image = (Image) session.get(Image.class, image);
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return image;
	}
	
public List<Image> getImage(int inst_id) {
	
	Session session = null;
	Transaction transaction = null;
	List<Image> imageList = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Criteria criteria = session.createCriteria(Exam.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
	    imageList = criteria.list();
		transaction.commit();
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
	}finally{
		if(null!=session){
			session.close();
		}
	}
	return imageList;
}

public List<Image> getImagesByType(int inst_id,String image_type) {
	Session session = null;
	Transaction transaction = null;
	List<Image> imageList = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Criteria criteria = session.createCriteria(Image.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("image_type", image_type);
		criteria.add(criterion);
	    imageList = criteria.list();
		transaction.commit();
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
	}finally{
		if(null!=session){
			session.close();
		}
	}
	return imageList;
}


	public static void main(String[] args) {
		
	}
	
}
