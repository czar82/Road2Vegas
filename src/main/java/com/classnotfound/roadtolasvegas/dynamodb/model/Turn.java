package com.classnotfound.roadtolasvegas.dynamodb.model;

public enum Turn {
	M (1, 0),
	P (1, 1),
	N (1, 2),
	R (0, 3),
	LN (0, 4);

	//1: working day, 0: day-off
	private final int workingDay;
	private final int idTurn;

	Turn(int workingDay, int idTurn) {
		this.workingDay = workingDay;
		this.idTurn = idTurn;
	}

	public int getWorkingDay() {
		return this.workingDay;
	}

	public int getIdTurn() {
		return idTurn;
	}

}
