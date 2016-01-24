package edu.gmu.cs.multiagent.matrix;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.stream.*;

/**
 * 
 * @author Ermo Wei
 * 
 */

public class Game {
	public String gameName;
	public int numStates;
	public int currentState;
	private int startState;
	private boolean gameEnded;
	public ArrayList<State> stateList;
	private double[] rewards;
	private int agentOneLastAction;
	private int agentTwoLastAction;

	public Game(String gameFile) {
		stateList = new ArrayList<State>();
		startState = 0; // default start state
		parseGame(gameFile);
		numStates = stateList.size();
		gameEnded = false;
	
	}

	private void parseGame(String gameFile) {
		try {
			JsonReader jsonReader = new JsonReader(new FileReader(gameFile));
			// start to read objects
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {
				String name = jsonReader.nextName();
				if (name.equals("Name")) {
					gameName = jsonReader.nextString();
				}
				else if (name.equals("StartState")) {
					startState = jsonReader.nextInt();
				}
				else if (name.equals("Matrices")) {
					jsonReader.beginArray();

					// read the data of each state
					while (jsonReader.hasNext()) {
						State state = new State(jsonReader);
						stateList.add(state);
						
					}
					jsonReader.endArray();
				}
			}

			jsonReader.endObject();
			jsonReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public void play(int agentOneAction, int agentTwoAction) {
		
		State state = getCurrentState();
		state.play(agentOneAction, agentTwoAction);
		rewards = state.getReward();
		currentState = state.getNextState();

		if (getCurrentState().isTerminalState())
			gameEnded = true;
		
		agentOneLastAction = agentOneAction;
		agentTwoLastAction = agentTwoAction;
	}
	
	public void resetGame() {
		currentState = startState;
	}

	private State getCurrentState() {
		return stateList.get(currentState);
	}

	public double getReward(boolean firstAgent) {
		if (firstAgent)
			return rewards[0];
		else
			return rewards[1];
	}

	public boolean isGameEnd() {
		return gameEnded;
	}
	
	public int getAgentOneLastAction() {
		return agentOneLastAction;
	}

	public int getAgentTwoLastAction() {
		return agentTwoLastAction;
	}

	public int checkPolicy(int[] policy1, int[] policy2) {
		boolean complete = true;
		
		for(int i = 0;i<stateList.size();++i)
		{
			State state = stateList.get(i);
			if(!state.checkPolicy(policy1[i],policy2[i])) {
				complete = false;
				if(state.correctPolicyState()) {
					return 2; // fail
				}
					
			}
		}
		
		return complete?0:1;
	}
}
