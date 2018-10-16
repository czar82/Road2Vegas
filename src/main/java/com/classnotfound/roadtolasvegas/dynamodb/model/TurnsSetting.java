package com.classnotfound.roadtolasvegas.dynamodb.model;

import java.time.LocalDate;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

@DynamoDBTable(tableName = "Settings")
public class TurnsSetting {

	public static final Turn [] TURN = {Turn.M, Turn.M, Turn.M, Turn.R, Turn.N, Turn.N, Turn.N, Turn.LN, Turn.R, Turn.R, Turn.P, Turn.P, Turn.P, Turn.R, Turn.R};
	public static final int WINDOW_SIZE = TURN.length;
	
	public static String settingName = "TurnsSetting";
	//first day from which calculate turns:
	private LocalDate startingDay = LocalDate.of(2018, 8, 07);
	private int iteration = 10000;
	private int minWorkers4Turn = 2;
	//A working month is made up by 28 days:
	private int workingMonthSize = 28;
	private int minimumWorkingDays = 18;

	public TurnsSetting() {
		super();
	}

	public TurnsSetting(LocalDate startingDay, int iteration, int minWorkers4Turn, int workingMonthSize, int minimumWorkingDays) {
		super();
		this.startingDay = startingDay;
		this.iteration = iteration;
		this.minWorkers4Turn = minWorkers4Turn;
		this.workingMonthSize = workingMonthSize;
		this.minimumWorkingDays = minimumWorkingDays;
	}

    @DynamoDBTypeConverted( converter = LocalDateConverter.class )
	@DynamoDBAttribute(attributeName = "StartingDay")
	public LocalDate getStartingDay() {
		return startingDay;
	}

	public void setStartingDay(LocalDate sTARTING_DAY) {
		startingDay = sTARTING_DAY;
	}

	@DynamoDBAttribute(attributeName = "Iteration")
	public int getIteration() {
		return iteration;
	}

	public void setIteration(int sUGGESTED_ITERATION) {
		iteration = sUGGESTED_ITERATION;
	}

	@DynamoDBAttribute(attributeName = "MinWorkers4Turn")
	public int getMinWorkers4Turn() {
		return minWorkers4Turn;
	}

	public void setMinWorkers4Turn(int minWorkers4Turn) {
		this.minWorkers4Turn = minWorkers4Turn;
	}

	@DynamoDBIgnore
	public static Turn[] getTurn() {
		return TURN;
	}

	@DynamoDBIgnore
	public static int getWindowSize() {
		return WINDOW_SIZE;
	}

	@DynamoDBHashKey(attributeName = "SettingName")
	public static String getSettingName() {
		return settingName;
	}

	public static void setSettingName(String settingName) {
		TurnsSetting.settingName = settingName;
	}

	@DynamoDBAttribute(attributeName = "WorkingMonthSize")
	public int getWorkingMonthSize() {
		return workingMonthSize;
	}

	public void setWorkingMonthSize(int workingMonthSize) {
		this.workingMonthSize = workingMonthSize;
	}

	public int getMinimumWorkingDays() {
		return minimumWorkingDays;
	}

	@DynamoDBAttribute(attributeName = "MinimumWorkingDays")
	public void setMinimumWorkingDays(int minimumWorkingDays) {
		this.minimumWorkingDays = minimumWorkingDays;
	}

}
