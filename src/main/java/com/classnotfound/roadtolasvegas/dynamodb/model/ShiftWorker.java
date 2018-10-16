package com.classnotfound.roadtolasvegas.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ShiftWorker")
public class ShiftWorker extends People {

	/**
	 * It's not unique! Used only for turnover purpose.
	 */
	private int patternId;

	@DynamoDBAttribute(attributeName = "PatternId")
	public int getPatternId() {
		return patternId;
	}

	public void setPatternId(int shiftId) {
		this.patternId = shiftId;
	}

	public ShiftWorker(String name, String surname) {
		super(name, surname);
	}
	public ShiftWorker(String id, String name, String surname) {
		super(id, name, surname);
	}

	public ShiftWorker() {
		super();
		// TODO Auto-generated constructor stub
	}
}
