package com.classnotfound.roadtolasvegas.controller.response;

import java.time.LocalDate;

public class LessThan28Days {

	private LocalDate startingDate;
	private LocalDate endingDate;
	private Integer totalMissingDays;
	
	public LocalDate getStartingDate() {
		return startingDate;
	}
	public void setStartingDate(LocalDate startingDate) {
		this.startingDate = startingDate;
	}
	public LocalDate getEndingDate() {
		return endingDate;
	}
	public void setEndingDate(LocalDate endingDate) {
		this.endingDate = endingDate;
	}
	public Integer getTotalMissingDays() {
		return totalMissingDays;
	}
	public void setTotalMissingDays(Integer totalMissingDays) {
		this.totalMissingDays = totalMissingDays;
	}
}
