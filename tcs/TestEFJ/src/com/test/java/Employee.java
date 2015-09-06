package com.test.java;

import java.util.Date;

public class Employee implements Cloneable {

	private String name = null;
	private String office = null;
	private final Date id;
	
	public Employee () {
		id = new Date();
	}

	public Number test () {
		System.out.println("test");
	//	id = new Date();
		return null;

	}
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getOffice() {
		return office;
	}


	public void setOffice(String office) {
		this.office = office;
	}
	
	@Override
	public Employee clone(){
		return null;
		
	}
}
