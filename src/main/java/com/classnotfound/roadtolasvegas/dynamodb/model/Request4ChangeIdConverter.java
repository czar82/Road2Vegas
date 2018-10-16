package com.classnotfound.roadtolasvegas.dynamodb.model;

import java.time.LocalDate;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class Request4ChangeIdConverter implements DynamoDBTypeConverter<String, Request4ChangeId> {

    @Override
    public String convert( final Request4ChangeId id ) {

        return id.getDay2change().toString() + "*" + id.getProposingWorkerId();
    }

    @Override
    public Request4ChangeId unconvert( final String stringValue ) {
    	String[] parts = stringValue.split("*");
    	Request4ChangeId id = new Request4ChangeId();
    	id.setDay2change(LocalDate.parse(parts[0]));
    	id.setProposingWorkerId(parts[1]);
        return id;
    }
}