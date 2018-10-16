package com.classnotfound.roadtolasvegas.turnengine;

import static com.classnotfound.roadtolasvegas.dynamodb.model.TurnsSetting.TURN;
import static com.classnotfound.roadtolasvegas.dynamodb.model.TurnsSetting.WINDOW_SIZE;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classnotfound.roadtolasvegas.controller.response.LessThan28Days;
import com.classnotfound.roadtolasvegas.controller.response.TurnDayList;
import com.classnotfound.roadtolasvegas.dynamodb.model.Request4ChangeId;
import com.classnotfound.roadtolasvegas.dynamodb.model.ShiftWorker;
import com.classnotfound.roadtolasvegas.dynamodb.model.SwitchedTurn;
import com.classnotfound.roadtolasvegas.dynamodb.model.Turn;
import com.classnotfound.roadtolasvegas.dynamodb.model.TurnsSetting;
import com.classnotfound.roadtolasvegas.dynamodb.repo.SettingsRepository;
import com.classnotfound.roadtolasvegas.dynamodb.repo.ShiftWorkerRepository;
import com.classnotfound.roadtolasvegas.dynamodb.repo.SwitchedTurnsRepository;

/**
 * Core of turn manipulation engine.
 * Generate the turns pattern and assign it to shift workers.
 * 
 * @author Ivan Di Paola
 *
 */
@Service
public class TurnManipulator {

	private int shiftWorkerPatternId;
	private Iterable<ShiftWorker> shiftworkers;

	@Autowired
	private ShiftWorkerRepository shiftWorkerRepository;
	
	@Autowired
	private SettingsRepository settingsRepository;
	
	@Autowired
	private SwitchedTurnsRepository switchedTurnsRepository;
	
	public TurnManipulator() {
		super();
	}

	public void generateNewTurns(TurnsSetting turnsSetting)
	{
		shiftWorkerPatternId = 0;
		shiftworkers = shiftWorkerRepository.findAll();
		int [] shiftWorkerPattern = findBestShiftWorkersPattern(shiftWorkerRepository.count(), turnsSetting);
		updateShiftWorkersTurns(shiftWorkerPattern);
		settingsRepository.save(turnsSetting);		
	}
	
	private void updateShiftWorkersTurns(int [] shiftWorkerPattern)
	{
		for (ShiftWorker sw : shiftworkers) 
		{
			while (shiftWorkerPattern[shiftWorkerPatternId]==0)
			{
				shiftWorkerPatternId++;
			}
			//remove workers that will be added following the shiftWorkerPattern:
			shiftWorkerPattern[shiftWorkerPatternId]--;
			sw.setPatternId(shiftWorkerPatternId);
		}
		shiftWorkerRepository.save(shiftworkers);
	}
	
	/**
	 * Add a shift worker to database.
	 * 
	 * @param sw	Shift worker.
	 */
	public void addShiftWorker(ShiftWorker sw)
	{
		shiftWorkerRepository.save(sw);
	}
		
	/**
	 * Return a timetable for a given day.
	 * 
	 * @param day		Day for which the shift workers timetable is requested.
	 * @return			{@link Day} turn timetable.
	 */
	private TurnDayList shiftWorkersInDay(LocalDate day)
	{
		TurnDayList dayTurn = new TurnDayList(day);
		Iterable<ShiftWorker> shiftWorkers = shiftWorkerRepository.findAll();
		TurnsSetting turnsSetting = settingsRepository.findOne(TurnsSetting.settingName);
		LocalDate settingStartingDay = turnsSetting.getStartingDay();
		for (ShiftWorker sw:shiftWorkers)
		{
			dayTurn.addShiftWorker(sw, getShiftWorkerTurnInDay(sw, day, settingStartingDay));
		}
		return dayTurn;
	}
	
	/**
	 * Return a timetable for a given interval of days.
	 * 
	 * @param from		Starting day of shift workers timetable.
	 * @param to		Ending day of shift workers timetable.
	 * @return			Turns timetable.
	 */
	public List<TurnDayList> shiftWorkersInDayInterval(LocalDate from, LocalDate to)
	{
		int daysDiff = 1;
		if (to!=null && to.isAfter(from))
		{
			Period period = Period.between(from, to);
			daysDiff += period.getDays();
		}
		List<TurnDayList> turnList = new ArrayList<>(daysDiff);
		for (int i=0; i<daysDiff; i++)
		{
			TurnDayList day = shiftWorkersInDay(from.plusDays(i));
			turnList.add(day);
			System.out.println(day);
			System.out.println("\n--------------\n");
		}
		return turnList;
	}

	public Turn getShiftWorkerTurnInDay(ShiftWorker sw, LocalDate day)
	{
		TurnsSetting turnsSetting = settingsRepository.findOne(TurnsSetting.settingName);
		LocalDate settingStartingDay = turnsSetting.getStartingDay();
		return getShiftWorkerTurnInDay(sw, day, settingStartingDay);
	}
	
	public Turn getShiftWorkerTurnInDay(ShiftWorker sw, LocalDate day, LocalDate settingStartingDate)
	{
		//check if there is a switched turn:
		SwitchedTurn switchedTurn = switchedTurnsRepository.findOne(new Request4ChangeId(day, sw.getId()));
		if (switchedTurn!=null)
		{
			return switchedTurn.getNewTurn();
		}
		
		return TURN[ (Period.between(settingStartingDate, day).getDays() + sw.getPatternId()) % WINDOW_SIZE ];
	}
	
	/**
	 * Find the best shift workers pattern.
	 * 
	 * @param workersTotal		Numbers of available shift workers.
	 * @param turnsSetting		Settings to generate shift workers pattern.
	 * @return					Array with the the number of workers requested in for a single turn.
	 * 							Each item in the array is a turn, its value is the number of requested worker.
	 */
	private int [] findBestShiftWorkersPattern(long workersTotal, TurnsSetting turnsSetting)
	{
		int [] bestPattern = new int[WINDOW_SIZE];
		Random rand = new Random();
		//28 is the month size, 28*15 is just to have a window 15 times bigger to find the best pattern
		final int TURN_SIZE = 28*15;
		int uncoveredTurnsBestPerfomance = 99999999;

		int iteration = turnsSetting.getIteration();
		//GO BY BRUTE FORCE :D
		for (int it=0; it<iteration; it++)
		{
			int i = 0;
			int assignedWorkers = 0;
			//days[turn type (morning, afternoon,...][day in which the turn is done]
			int days[][] = new int[5][TURN_SIZE];
			int [] currentPattern = new int[WINDOW_SIZE];
			int uncoveredTurns = 0;
			while (assignedWorkers<workersTotal)
			{
				int  isOk = rand.nextInt(2);
				if (isOk==1)
				{
					//set turns worked for only one shift worker
					for (int k=0; k<TURN_SIZE; k++)
					{
						//i: turn typology from the fixed array turn table (from 0 to WINDOW_SIZE-1)
						//k: day in which turn is done
						days[TURN[(i+k) % WINDOW_SIZE].getIdTurn()][k]++;
					}
					//add a worker to the "i" pattern: 
					currentPattern[i]++;
					assignedWorkers++;
				}
				i = (i+1) % WINDOW_SIZE;
			}
			//check if every turn for the day has at least minWorkers4Turn workers: 
			for (int k=0; k<TURN_SIZE; k++)
			{
				int todayUncovered = Math.min(turnsSetting.getMinWorkers4Turn(), days[0][k]) + Math.min(turnsSetting.getMinWorkers4Turn(), days[1][k]) + Math.min(turnsSetting.getMinWorkers4Turn(), days[2][k]);
				//minWorkers4Turn*3 because 3 turns, morning, afternoon and night:
				uncoveredTurns = uncoveredTurns + (turnsSetting.getMinWorkers4Turn()*3 - todayUncovered);
			}
			if (uncoveredTurns<uncoveredTurnsBestPerfomance)
			{
				uncoveredTurnsBestPerfomance = uncoveredTurns;
				bestPattern = currentPattern;
				System.out.println("\nDAYS morning:\n" + Arrays.toString(days[0]));
				System.out.println("\nDAYS afternoon:\n" + Arrays.toString(days[1]));
				System.out.println("\nDAYS night:\n" + Arrays.toString(days[2]));
				System.out.println(uncoveredTurnsBestPerfomance);
				if (uncoveredTurnsBestPerfomance==0)
				{
					break;
				}
			}
		}
		return bestPattern;
	}

	public void printTurn(int[] turnPattern, int daysToPrint)
	{
		for (int j=0; j<turnPattern.length; j++)
		{
			for (int k=0; k<turnPattern[j]; k++)
			{
				for (int i=0; i<daysToPrint; i++)
				{
					Turn curr = TURN[(i+j)%15];
					System.out.print(String.format("%2s,", curr));
				}
			}
			System.out.println("---");
		}
	}

	public LessThan28Days checkIfWorkedLess28Days(Request4ChangeId request4ChangeId) {
		TurnsSetting turnsSetting = settingsRepository.findOne(TurnsSetting.settingName);
		LocalDate settingStartingDay = turnsSetting.getStartingDay();
		int workingMonthSize = turnsSetting.getWorkingMonthSize();
		ShiftWorker sw = shiftWorkerRepository.findOne(request4ChangeId.getProposingWorkerId());
		
		Period period = Period.between( settingStartingDay, request4ChangeId.getDay2change() );
		int deviation = period.getDays() % workingMonthSize;
		LessThan28Days lessThan28Days = new LessThan28Days();
		lessThan28Days.setStartingDate(request4ChangeId.getDay2change().minusDays(deviation));
		lessThan28Days.setEndingDate(lessThan28Days.getStartingDate().plusDays(workingMonthSize-1));
		
		lessThan28Days.setTotalMissingDays( turnsSetting.getMinimumWorkingDays() - 
				IntStream.range(0, workingMonthSize-1)
				.map(i -> getShiftWorkerTurnInDay(sw, lessThan28Days.getStartingDate().plusDays(i), settingStartingDay).getWorkingDay())
				.sum() );		
		
		return lessThan28Days;
	}
	
}
