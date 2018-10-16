package com.classnotfound.roadtolasvegas.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.classnotfound.roadtolasvegas.controller.response.TurnDayList;
import com.classnotfound.roadtolasvegas.turnengine.TurnManipulator;

@RestController
public class CommonController {
	
	@Autowired
	private TurnManipulator turnManipulator;

	@RequestMapping("/shift-workers-turn")
	public List<TurnDayList> shiftWorkersInDay(@RequestParam("on") String from, @RequestParam(value="to", required=false) String to) {
		LocalDate fromDate = LocalDate.parse(from);
		LocalDate toDate = to!=null ? LocalDate.parse(to) : null;		
		List<TurnDayList> turnList = turnManipulator.shiftWorkersInDayInterval(fromDate, toDate);
		return turnList;
	}

}
