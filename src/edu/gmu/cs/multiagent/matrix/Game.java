package edu.gmu.cs.multiagent.matrix;

import java.util.ArrayList;

/**
 * 
 * @author Ermo Wei
 *
 */

public class Game {
	
	public int numActions;
	public int numStates;
	public int currentState;
	private int startState;
	private boolean gameEnded;
	private ArrayList<State> stateList;
	private double[] rewards;
	
	public Game(String gameFile) {
		
	}
	
	public void play(int agentOneAction, int agentTwoAction) {
		State state = getCurrentState();
		state.play(agentOneAction,agentTwoAction);
		currentState = state.getNextState();
	}

	private State getCurrentState() {
		return stateList.get(currentState);
	}
	
	public double getReward(boolean firstAgent) {
		if(firstAgent)
			return rewards[0];
		else 
			return rewards[1];
	}
}
