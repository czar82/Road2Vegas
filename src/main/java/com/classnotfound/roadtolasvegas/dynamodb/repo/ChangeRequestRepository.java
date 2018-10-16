package com.classnotfound.roadtolasvegas.dynamodb.repo;

import java.time.LocalDate;
import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.classnotfound.roadtolasvegas.dynamodb.model.Request4Change;
import com.classnotfound.roadtolasvegas.dynamodb.model.Request4ChangeId;

@EnableScan
public interface ChangeRequestRepository extends CrudRepository<Request4Change, Request4ChangeId> {

	  Request4Change findByDay2changeAndProposingWorkerId(LocalDate day2change, String proposingWorkerId);
	  List<Request4Change> findByDay2change(LocalDate day2change);
}
