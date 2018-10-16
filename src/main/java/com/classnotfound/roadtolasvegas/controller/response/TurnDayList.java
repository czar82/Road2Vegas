package com.classnotfound.roadtolasvegas.controller.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.classnotfound.roadtolasvegas.dynamodb.model.ShiftWorker;
import com.classnotfound.roadtolasvegas.dynamodb.model.Turn;

public class TurnDayList {

	//List of ShiftWorker mapped by turn:
	private Map<Turn, List<ShiftWorker>> dayTurn;
	//Date of this day:
	private LocalDate date;
		
	public TurnDayList(LocalDate day) {
		super();
		this.date = day;
		dayTurn = new HashMap<>();
		for (Turn t:Turn.values())
		{
			dayTurn.put(t, new ArrayList<ShiftWorker>());
		}
	}

	@Override
	public String toString() {
		return "Day [date=" + date + "]\n" +

        dayTurn.entrySet()
                .stream()
                .map(entry -> "\n" + entry.getKey() + "\n" + entry.getValue().stream().map(Object::toString).collect(Collectors.joining(",\n")))
                .collect(Collectors.joining(",\n "));

	}

	public void addShiftWorker(ShiftWorker sw, Turn turn)
	{
		dayTurn.get(turn).add(sw);
	}
	
	public Map<Turn, List<ShiftWorker>> getDayTurn() {
		return dayTurn;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate day) {
		this.date = day;
	}
}
