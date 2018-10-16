package com.classnotfound.roadtolasvegas.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;

@DynamoDBDocument
public abstract class People {

	/**
	 * Unique ID inside company.
	 */
	private String id;
	private String name;
	private String surname;
	
	@DynamoDBHashKey(attributeName = "Id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@DynamoDBAttribute(attributeName = "Name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@DynamoDBAttribute(attributeName = "Surname")
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	@Override
	public String toString() {
		return "People [name=" + name + ", surname=" + surname + "]";
	}
	public People(String name, String surname) {
		super();
		this.name = name;
		this.surname = surname;
	}
	public People(String id, String name, String surname) {
		super();
		this.name = name;
		this.surname = surname;
		this.id = id;
	}
	public People() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
