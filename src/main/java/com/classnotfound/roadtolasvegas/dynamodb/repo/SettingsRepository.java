package com.classnotfound.roadtolasvegas.dynamodb.repo;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.classnotfound.roadtolasvegas.dynamodb.model.TurnsSetting;

@EnableScan
public interface SettingsRepository extends CrudRepository<TurnsSetting, String> {

}
