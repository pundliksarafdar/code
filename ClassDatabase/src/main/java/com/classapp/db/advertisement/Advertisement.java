package com.classapp.db.advertisement;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Date;

import com.mysql.jdbc.Blob;

public class Advertisement {
String booking_id;
String first_name;
String last_name;
String email;
byte[] image;
Date advdate;
public String getBooking_id() {
	return booking_id;
}
public void setBooking_id(String booking_id) {
	this.booking_id = booking_id;
}
public String getFirst_name() {
	return first_name;
}
public void setFirst_name(String first_name) {
	this.first_name = first_name;
}
public String getLast_name() {
	return last_name;
}
public void setLast_name(String last_name) {
	this.last_name = last_name;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}

public byte[] getImage() {
	return image;
}
public void setImage(byte[] image) {
	this.image = image;
}
public Date getAdvdate() {
	return advdate;
}
public void setAdvdate(Date advdate) {
	this.advdate = advdate;
}


}
