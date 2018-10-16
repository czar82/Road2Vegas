package com.classnotfound.roadtolasvegas.dynamodb.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

@DynamoDBTable(tableName = "SwitchedTurns")
public class SwitchedTurn implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	private Request4ChangeId request4ChangeId;
	private Turn newTurn;
	
	public SwitchedTurn() {
		super();
	}

	public SwitchedTurn(Request4ChangeId request4ChangeId, Turn proposingTurn) {
		super();
		this.request4ChangeId = request4ChangeId;
		this.newTurn = proposingTurn;
	}

	public SwitchedTurn(LocalDate day2change, String proposingWorkerId, Turn proposingTurn) {
		super();
		this.request4ChangeId = new Request4ChangeId(day2change, proposingWorkerId);
		this.newTurn = proposingTurn;
	}

	@DynamoDBRangeKey(attributeName = "ProposingWorkerId")
	public String getProposingWorkerId() {
		return request4ChangeId != null ? request4ChangeId.getProposingWorkerId() : null;
	}

	public void setProposingWorkerId(String proposingWorkerId) {
		if (request4ChangeId==null)
		{
			request4ChangeId = new Request4ChangeId();
		}
		request4ChangeId.setProposingWorkerId(proposingWorkerId);
	}

	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute(attributeName = "ProposingTurn")
	public Turn getNewTurn() {
		return newTurn;
	}

	public void setNewTurn(Turn proposingTurn) {
		this.newTurn = proposingTurn;
	}

    @DynamoDBTypeConverted( converter = LocalDateConverter.class )
	@DynamoDBHashKey(attributeName = "Day2change")
	public LocalDate getDay2change() {
		return request4ChangeId != null ? request4ChangeId.getDay2change() : null;
	}

	public void setDay2change(LocalDate day2change) {
		if (request4ChangeId==null)
		{
			request4ChangeId = new Request4ChangeId();
		}
		request4ChangeId.setDay2change(day2change);
	}

}
