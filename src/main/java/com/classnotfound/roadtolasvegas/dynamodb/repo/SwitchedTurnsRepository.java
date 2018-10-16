package com.classnotfound.roadtolasvegas.dynamodb.repo;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.classnotfound.roadtolasvegas.dynamodb.model.Request4ChangeId;
import com.classnotfound.roadtolasvegas.dynamodb.model.SwitchedTurn;

@EnableScan
public interface SwitchedTurnsRepository extends CrudRepository<SwitchedTurn, Request4ChangeId> {
}
