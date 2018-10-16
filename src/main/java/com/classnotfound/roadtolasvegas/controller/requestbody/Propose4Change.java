package com.classnotfound.roadtolasvegas.controller.requestbody;

import java.io.Serializable;

import com.classnotfound.roadtolasvegas.dynamodb.model.Request4ChangeId;

public class Propose4Change implements Serializable {

	private static final long serialVersionUID = 1L;
	private Request4ChangeId request4ChangeId;
	private Request4ChangeId propose4ChangeId;
	private String reason;
	private Integer likingLevel;
	public Request4ChangeId getRequest4ChangeId() {
		return request4ChangeId;
	}
	public void setRequest4ChangeId(Request4ChangeId request4ChangeId) {
		this.request4ChangeId = request4ChangeId;
	}
	public Request4ChangeId getPropose4ChangeId() {
		return propose4ChangeId;
	}
	public void setPropose4ChangeId(Request4ChangeId propose4ChangeId) {
		this.propose4ChangeId = propose4ChangeId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Integer getLikingLevel() {
		return likingLevel;
	}
	public void setLikingLevel(Integer likingLevel) {
		this.likingLevel = likingLevel;
	}
}
