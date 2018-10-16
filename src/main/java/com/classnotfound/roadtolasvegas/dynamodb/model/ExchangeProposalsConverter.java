package com.classnotfound.roadtolasvegas.dynamodb.model;

import java.util.HashMap;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.google.gson.GsonBuilder;

public class ExchangeProposalsConverter implements DynamoDBTypeConverter<String, HashMap<Request4ChangeId, Request4Change>> {

	@Override
	public String convert(HashMap<Request4ChangeId, Request4Change> object) {
		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<Request4ChangeId, Request4Change> unconvert(String object) {
		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().fromJson(object, HashMap.class);
	}

}