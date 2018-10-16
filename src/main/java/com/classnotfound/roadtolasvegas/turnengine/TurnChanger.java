package com.classnotfound.roadtolasvegas.turnengine;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classnotfound.roadtolasvegas.controller.response.TurnDayList;
import com.classnotfound.roadtolasvegas.dynamodb.model.Request4Change;
import com.classnotfound.roadtolasvegas.dynamodb.model.Request4ChangeId;
import com.classnotfound.roadtolasvegas.dynamodb.model.ShiftWorker;
import com.classnotfound.roadtolasvegas.dynamodb.model.SwitchedTurn;
import com.classnotfound.roadtolasvegas.dynamodb.model.Turn;
import com.classnotfound.roadtolasvegas.dynamodb.repo.ChangeRequestRepository;
import com.classnotfound.roadtolasvegas.dynamodb.repo.ShiftWorkerRepository;
import com.classnotfound.roadtolasvegas.dynamodb.repo.SwitchedTurnsRepository;

/**
 * Handle the turns change between shift workers.
 * 
 * @author Ivan Di Paola
 *
 */
@Service
public class TurnChanger {
	@Autowired
	private TurnManipulator turnManipulator;

	@Autowired
	private ShiftWorkerRepository shiftWorkerRepository;
	
	@Autowired
	private ChangeRequestRepository changeRequestRepository;
	
	@Autowired
	private SwitchedTurnsRepository switchedTurnsRepository;
	
	/**
	 * First phase of negotiation, a shift worker propose a day in which he need a substitute.
	 * 
	 * @param request4ChangeId		Requester. ID of the request submitted by shift worker,
	 * 								at which the current shift worker (proposer) is replying. 
	 */
	public void inquiry4TurnExchange(Request4ChangeId request4ChangeId)
	{
		ShiftWorker sw = shiftWorkerRepository.findOne(request4ChangeId.getProposingWorkerId());
		Turn turn = turnManipulator.getShiftWorkerTurnInDay(sw, request4ChangeId.getDay2change());
		//the day that worker require to switch must be a working day:
		if (turn.getWorkingDay()==1)
		{
			Request4Change curr = new Request4Change(request4ChangeId, turn);
			Request4Change check4duplicate = changeRequestRepository.findOne(request4ChangeId);
			
			if (check4duplicate==null)
			{
				changeRequestRepository.save(curr);
			}
			else
			{
				//TODO: EXIT: error, request already present.
			}
		}
		else
		{
			//TODO: Error, day salect is no-working day
		}
	}

	/**
	 * Return turn exchange requests for a given day.
	 * 
	 * @param on		Starting day of shift workers timetable.
	 * @return			Turns timetable.
	 */
	public List<TurnDayList> getTurnExchangeRequests(LocalDate on)
	{
		List<Request4Change> exchangeRequests = changeRequestRepository.findByDay2change(on);
		exchangeRequests.forEach(System.out::println);
		return null;
	}
	
	/**
	 * Return turn exchange requests for a given interval of days.
	 * 
	 * @param from		Starting day of shift workers timetable.
	 * @param to		Ending day of shift workers timetable.
	 * @return			Turns timetable.
	 */
	public List<TurnDayList> getTurnExchangeRequests(LocalDate from, LocalDate to)
	{
		//TODO
		return null;
	}
	
	/**
	 * Second phase of negotiation, a shift worker select a request from somebody that need
	 * a change, and propose a date in which swap the turns.
	 * 
	 * @param request4ChangeId		Requester. ID of the request submitted by shift worker,
	 * 								at which the current shift worker (proposer) is replying. 
	 * @param propose4ChangeId		Proposer. ID of the request for turn exchange submitted by the current
	 * 								shift worker (proposer) in help to a requester.
	 */
	public void proposing4TurnExchange(Request4ChangeId request4ChangeId, Request4ChangeId propose4ChangeId)
	{
		ShiftWorker swRequester = shiftWorkerRepository.findOne(request4ChangeId.getProposingWorkerId());
		ShiftWorker swProposing = shiftWorkerRepository.findOne(propose4ChangeId.getProposingWorkerId());
		
		//Turn in which proposer is actually working
		Turn workingProposedTurn = turnManipulator.getShiftWorkerTurnInDay(swProposing, propose4ChangeId.getDay2change());
		//Turn that proposer has in the day that he is switching
		Turn newTurn4proposer = turnManipulator.getShiftWorkerTurnInDay(swProposing, request4ChangeId.getDay2change());
		
		//Turn that requester will have in the day that the proposer propose to switching
		Turn newTurn4requester = turnManipulator.getShiftWorkerTurnInDay(swRequester, propose4ChangeId.getDay2change());
		
		Request4Change propose4Change = new Request4Change(propose4ChangeId, workingProposedTurn);
		Request4Change request4Change = changeRequestRepository.findOne(request4ChangeId);
		
		//the day to propose for switching must be a working day:
		if (checkShiftWorkerCanChangeTurn(request4Change, newTurn4requester, propose4Change, newTurn4proposer))
		{
			if (request4Change.getExchangeProposals()==null)
			{
				request4Change.setExchangeProposals(new HashMap<>());
			}
			request4Change.getExchangeProposals().put(propose4ChangeId.convert(), propose4Change);
			changeRequestRepository.save(request4Change);
		}
	}
	
	private boolean checkShiftWorkerCanChangeTurn(Request4Change request4Change, Turn newTurn4requester, Request4Change propose4Change, Turn newTurn4proposer)
	{
		Turn proposingTurn = propose4Change.getProposingTurn();

		//CASE 1: in the proposing day for change, the proposer is working && in the requested day the proposer is not working && in the proposed day the requester is not working
		//CASE 2: in the proposing day for change, the proposer is working && is the same day && is different turns
		if ( (proposingTurn.getWorkingDay()==1 && newTurn4proposer.getWorkingDay()==0 && newTurn4requester.getWorkingDay()==0) || 
				(proposingTurn.getWorkingDay()==1 && propose4Change.getDay2change().isEqual(request4Change.getDay2change()) && propose4Change.getProposingTurn()!=request4Change.getProposingTurn()))
		{
			//TODO: verificare eventuali vincoli violati e segnalarli in propose4Change.
			return true;
		}
		return false;
	}

	/**
	 * Third phase of negotiation, the first applicant for the request of turn change, select
	 * a set of proposes for switching turns giving each a "liking level".
	 * 
	 * @param request4Change
	 */
	public void acceptTurnExchange(Request4Change request4Change)
	{
		changeRequestRepository.save(request4Change);
	}

	/**
	 * Last phase of negotiation, the turn administrator authorize the turn change, choosing the
	 * most liked propose, or another if this can create problems.
	 * 
	 * @param request4ChangeId		Requester. ID of the request submitted by shift worker,
	 * 								at which the current shift worker (proposer) is replying. 
	 * @param acceptedPropose4ChangeId		Proposer. ID of the request for turn exchange submitted by the current
	 * 								shift worker (proposer) in help to a requester.
	 */
	public void authorizeTurnExchange(Request4ChangeId request4ChangeId, Request4ChangeId acceptedPropose4ChangeId) {
		//Check the if input request and proposal exist:
		Request4Change originalRequest = changeRequestRepository.findOne(request4ChangeId);
		if (originalRequest!=null && originalRequest.getExchangeProposals()!=null && originalRequest.getExchangeProposals().containsKey(acceptedPropose4ChangeId.convert()))
		{
			changeRequestRepository.delete(request4ChangeId);
			
			//adding new turns for requester:

			ShiftWorker swRequester = shiftWorkerRepository.findOne(request4ChangeId.getProposingWorkerId());
			ShiftWorker swAccepting = shiftWorkerRepository.findOne(acceptedPropose4ChangeId.getProposingWorkerId());

			//Turn in which proposer is actually working
			Turn workingProposedTurn = turnManipulator.getShiftWorkerTurnInDay(swAccepting, acceptedPropose4ChangeId.getDay2change());
			
			//Turn in which requester is actually working
			Turn oldRequesterTurn = turnManipulator.getShiftWorkerTurnInDay(swRequester, request4ChangeId.getDay2change());

			//Turn that proposer has in the day of requester:
			Turn newTurn4proposer = turnManipulator.getShiftWorkerTurnInDay(swAccepting, request4ChangeId.getDay2change());

			//Turn that requester has in the day of proposer:
			Turn newTurn4requester = turnManipulator.getShiftWorkerTurnInDay(swRequester, acceptedPropose4ChangeId.getDay2change());
			
			//Save new turn for requester in the day switched with proposer:
			Request4ChangeId newTurn4RequesterId = new Request4ChangeId(acceptedPropose4ChangeId.getDay2change(), request4ChangeId.getProposingWorkerId());
			SwitchedTurn newTurn4Requester = new SwitchedTurn(newTurn4RequesterId, workingProposedTurn);
			switchedTurnsRepository.save(newTurn4Requester);
			
			//Save new turn for proposer in the day switched with requester:
			Request4ChangeId newTurn4ProposerId = new Request4ChangeId(request4ChangeId.getDay2change(), acceptedPropose4ChangeId.getProposingWorkerId());
			SwitchedTurn newTurn4Proposer = new SwitchedTurn(newTurn4ProposerId, oldRequesterTurn);
			switchedTurnsRepository.save(newTurn4Proposer);
			
			//If the turn switch is not on same day turn, the original turn of proposer and
			//requester will be switched too:
			if (!request4ChangeId.getDay2change().isEqual(acceptedPropose4ChangeId.getDay2change()))
			{
				//switch turn of requester with proposer:
				SwitchedTurn newTurn4RequesterInHisPreviousWorkingDay = new SwitchedTurn(request4ChangeId, newTurn4proposer);
				switchedTurnsRepository.save(newTurn4RequesterInHisPreviousWorkingDay);
				
				//switch turn of proposer with requester:
				SwitchedTurn newTurn4ProposerInHisPreviousWorkingDay = new SwitchedTurn(acceptedPropose4ChangeId, newTurn4requester);			
				switchedTurnsRepository.save(newTurn4ProposerInHisPreviousWorkingDay);
			}
		}
		
	}
	
}
