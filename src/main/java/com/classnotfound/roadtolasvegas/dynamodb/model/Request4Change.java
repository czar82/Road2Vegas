package com.classnotfound.roadtolasvegas.dynamodb.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

import org.springframework.data.annotation.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

@DynamoDBTable(tableName = "ExchangeRequests")
public class Request4Change implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	private Request4ChangeId request4ChangeId;
	private Turn proposingTurn;
	private String reason;
	private Integer likingLevel;
	private Obligation obligation;
	private HashMap<String, Request4Change> exchangeProposals;
	
	public Request4Change() {
		super();
	}

	public Request4Change(Request4ChangeId request4ChangeId, Turn proposingTurn) {
		super();
		this.request4ChangeId = request4ChangeId;
		this.proposingTurn = proposingTurn;
	}

	public Request4Change(LocalDate day2change, String proposingWorkerId, Turn proposingTurn) {
		super();
		this.request4ChangeId = new Request4ChangeId(day2change, proposingWorkerId);
		this.proposingTurn = proposingTurn;
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
	public Turn getProposingTurn() {
		return proposingTurn;
	}

	public void setProposingTurn(Turn proposingTurn) {
		this.proposingTurn = proposingTurn;
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

	@DynamoDBTypeConverted( converter = ExchangeProposalsConverter.class )
	@DynamoDBAttribute(attributeName = "ExchangeProposals")
	public HashMap<String, Request4Change> getExchangeProposals() {
		return exchangeProposals;
	}

	public void setExchangeProposals(HashMap<String, Request4Change> exchangeProposal) {
		this.exchangeProposals = exchangeProposal;
	}

	@Override
	public String toString() {
		return "Request4Change [request4ChangeId=" + request4ChangeId + ", proposingTurn=" + proposingTurn
				+ ", exchangeProposals=" + exchangeProposals + "]";
	}

	@DynamoDBAttribute(attributeName = "Reason")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@DynamoDBAttribute(attributeName = "LikingLevel")
	public Integer getLikingLevel() {
		return likingLevel;
	}

	public void setLikingLevel(Integer likingLevel) {
		this.likingLevel = likingLevel;
	}

	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute(attributeName = "Obligation")
	public Obligation getObligation() {
		return obligation;
	}

	public void setObligation(Obligation obligation) {
		this.obligation = obligation;
	}

}
