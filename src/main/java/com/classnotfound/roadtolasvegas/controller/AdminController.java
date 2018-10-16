package com.classnotfound.roadtolasvegas.controller;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.classnotfound.roadtolasvegas.controller.request.Propose4Change;
import com.classnotfound.roadtolasvegas.dynamodb.model.ShiftWorker;
import com.classnotfound.roadtolasvegas.dynamodb.model.TurnsSetting;
import com.classnotfound.roadtolasvegas.dynamodb.repo.ShiftWorkerRepository;
import com.classnotfound.roadtolasvegas.turnengine.TurnChanger;
import com.classnotfound.roadtolasvegas.turnengine.TurnManipulator;

@RestController
public class AdminController {

	@Autowired
	private ShiftWorkerRepository repository;
	
	@Autowired
	private TurnManipulator turnManipulator;

	@Autowired
	private TurnChanger turnChanger;

	@RequestMapping("/populate")
	public String save() {

		// save a list of ShiftWorker
		repository.save(Arrays.asList(
				new ShiftWorker("1", "Adam", "Johnson"), 
				new ShiftWorker("2", "Kim", "Smith"),
				new ShiftWorker("3", "David", "Williams"), 
				new ShiftWorker("4", "Peter", "Davis"),
				new ShiftWorker("5", "Liliana", "Lo Mulo"),
				new ShiftWorker("6", "Laura", "Lari"),
				new ShiftWorker("7", "Vita", "Puccia"),
				new ShiftWorker("8", "Mirco", "Dalle Marche"),
				new ShiftWorker("9", "Marco", "Tedesco"),
				new ShiftWorker("10", "Liuccia", "Dalla")
				));

		return "Done";
	}

	@RequestMapping("/generate-new-turns")
	public String generateNewTurns() {

		TurnsSetting turnsSetting = new TurnsSetting(LocalDate.of(2018, 8, 07), 10000, 2, 28, 18);
		turnManipulator.generateNewTurns(turnsSetting);
		return "Done";
	}

	@RequestMapping("/find-all")
	public String findAll() {
		String result = "";
		Iterable<ShiftWorker> shiftworkers = repository.findAll();

		for (ShiftWorker sw : shiftworkers) {
			result += sw.toString() + "<br>";
		}

		return result;
	}

	@RequestMapping("/find-by-id")
	public String findById(@RequestParam("id") String id) {
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}
	
	@PostMapping("/authorize-turn-exchange")
	public String authorizeTurnExchange(@RequestBody Propose4Change propose4Change) {
		turnChanger.authorizeTurnExchange(propose4Change.getRequest4ChangeId(), propose4Change.getPropose4ChangeId());
		return "Done";
	}
	
	
	
}
