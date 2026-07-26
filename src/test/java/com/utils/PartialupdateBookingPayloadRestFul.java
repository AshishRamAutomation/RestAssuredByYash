package com.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartialupdateBookingPayloadRestFul {
private String firstname;
private String lastname;
private Integer totalprice;
public PartialupdateBookingPayloadRestFul() {
}
public PartialupdateBookingPayloadRestFul(String firstname, String lastname, int totalprice) {
	this.firstname = firstname;
	this.lastname = lastname;
	this.totalprice = totalprice;
}
public String getFirstname() {
	return firstname;
}
public void setFirstname(String firstname) {
	this.firstname = firstname;
}
public String getLastname() {
	return lastname;
}
public void setLastname(String lastname) {
	this.lastname = lastname;
}
public int getTotalprice() {
	return totalprice;
}
public void setTotalprice(int totalprice) {
	this.totalprice = totalprice;
}
}
