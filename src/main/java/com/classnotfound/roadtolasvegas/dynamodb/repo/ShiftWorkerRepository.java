package com.classnotfound.roadtolasvegas.dynamodb.repo;

import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.classnotfound.roadtolasvegas.dynamodb.model.ShiftWorker;

@EnableScan
public interface ShiftWorkerRepository extends CrudRepository<ShiftWorker, String> {

	List<ShiftWorker> findBySurname(String surname);
}
