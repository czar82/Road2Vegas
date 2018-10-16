package com.classnotfound.roadtolasvegas.dynamodb.model;

import java.io.Serializable;
import java.time.LocalDate;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonFormat;

@DynamoDBTable(tableName = "ExchangeRequest")
public class Request4ChangeId implements Serializable {
	private static final long serialVersionUID = 1L;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate day2change;
	private String proposingWorkerId;

	public Request4ChangeId() {
		super();
	}

	public Request4ChangeId(LocalDate day2change, String proposingWorkerId) {
		super();
		this.day2change = day2change;
		this.proposingWorkerId = proposingWorkerId;
	}

	@DynamoDBRangeKey
	public String getProposingWorkerId() {
		return proposingWorkerId;
	}

	public void setProposingWorkerId(String proposingWorkerId) {
		this.proposingWorkerId = proposingWorkerId;
	}

	@DynamoDBHashKey
	public LocalDate getDay2change() {
		return day2change;
	}

	public void setDay2change(LocalDate day2change) {
		this.day2change = day2change;
	}

	@Override
	public String toString() {
		return "Request4ChangeId [day2change=" + day2change + ", proposingWorkerId=" + proposingWorkerId + "]";
	}

	@DynamoDBIgnore
	public String convert() {
		return this.getDay2change().toString() + "*" + this.getProposingWorkerId();
	}

    public Request4ChangeId( final String unconvert ) {
    	String[] parts = unconvert.split("*");
    	this.setDay2change(LocalDate.parse(parts[0]));
    	this.setProposingWorkerId(parts[1]);
    }

}
