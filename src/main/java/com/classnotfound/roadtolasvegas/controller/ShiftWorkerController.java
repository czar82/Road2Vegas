package com.classnotfound.roadtolasvegas.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.classnotfound.roadtolasvegas.controller.request.Propose4Change;
import com.classnotfound.roadtolasvegas.controller.response.LessThan28Days;
import com.classnotfound.roadtolasvegas.controller.response.TurnDayList;
import com.classnotfound.roadtolasvegas.dynamodb.model.Request4Change;
import com.classnotfound.roadtolasvegas.dynamodb.model.Request4ChangeId;
import com.classnotfound.roadtolasvegas.turnengine.TurnChanger;
import com.classnotfound.roadtolasvegas.turnengine.TurnManipulator;

@RestController
public class ShiftWorkerController {
	
	@Autowired
	private TurnChanger turnChanger;

	@Autowired
	private TurnManipulator turnManipulator;

	@PostMapping("/change-turn")
	public String inquiryChangeTurn(@RequestBody Request4ChangeId request4ChangeId) {
		turnChanger.inquiry4TurnExchange(request4ChangeId);
		return "Done";
	}
	
	@RequestMapping("/turn-exchange-requests")
	public List<TurnDayList> turnExchangeRequests(@RequestParam("on") String from, @RequestParam(value="to", required=false) String to) {
		LocalDate fromDate = LocalDate.parse(from);
		LocalDate toDate = to!=null ? LocalDate.parse(to) : null;		
		List<TurnDayList> turnList = turnChanger.getTurnExchangeRequests(fromDate, toDate);
		return turnList;
	}

	@PostMapping("/propose-change-turn")
	public String proposeChangeTurn(@RequestBody Propose4Change propose4Change) {
		turnChanger.proposing4TurnExchange(propose4Change.getRequest4ChangeId(), propose4Change.getPropose4ChangeId());
		return "Done";
	}
		
	@PostMapping("/accept-turn-exchange")
	public String acceptTurnExchange(@RequestBody Request4Change request4Change) {
		turnChanger.acceptTurnExchange(request4Change);
		return "Done";
	}

	@PostMapping("/check-if-worked-less-28-days")
	public LessThan28Days checkIfWorkedLess28Days(@RequestBody Request4ChangeId request4ChangeId) {
		LessThan28Days lessThan28Days = turnManipulator.checkIfWorkedLess28Days(request4ChangeId);
		return lessThan28Days;
	}
	
}
