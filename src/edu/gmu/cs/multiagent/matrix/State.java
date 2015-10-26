package edu.gmu.cs.multiagent.matrix;

import java.io.IOException;

import com.google.gson.stream.JsonReader;



/**
 * 
 * @author Ermo Wei
 *
 */

public class State {

	private Transition[][] transitionMatrix;
	private Reward[][] rewardMatrix;
	private int nextState;
	private double[] rewards;
	private boolean terminalState = false;
	private int agentOneActionNum = 0;
	private int agentTwoActionNum = 0;
	
	public State(JsonReader jsonReader, int agentOneActionNum, int agentTwoActionNum) {
		this.agentOneActionNum = agentOneActionNum;
		this.agentTwoActionNum = agentTwoActionNum;
		
		transitionMatrix = new Transition[agentOneActionNum][agentTwoActionNum];
		rewardMatrix = new Reward[agentOneActionNum][agentTwoActionNum];
	
		try {
			
			jsonReader.beginObject();
			String matrixName = jsonReader.nextName();
			if (matrixName.equals("Rewards")) {
				parseRewards(jsonReader);
			} else if (matrixName.equals("Transitions")) {
				parseTransitions(jsonReader, agentOneActionNum, agentTwoActionNum);
			} else if (matrixName.equals("Type")) {
				String value = jsonReader.nextString();
				if(value.equals("NULL"))
				{
					terminalState = true;
				}
			}
			jsonReader.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private void parseRewards(JsonReader jsonReader) {
		try {
			int rowCounter = 0;
			int columnCounter = 0;
			
			jsonReader.beginArray();
			// parsing it row by row
			while(jsonReader.hasNext()) {
				jsonReader.beginArray();
				while(jsonReader.hasNext()) {
					String rewardValue = jsonReader.nextString();
					rewardMatrix[rowCounter][columnCounter] = new Reward(rewardValue);
					columnCounter++;
				}
				rowCounter++;
				columnCounter = 0;
				jsonReader.endArray();
			}
			jsonReader.endArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseTransitions(JsonReader jsonReader, int agentOneActionNum, int agentTwoActionNum) {
		try {
			jsonReader.beginArray();
			while(jsonReader.hasNext()) {
				
			}
			jsonReader.endArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void play(int agentOneAction, int agentTwoAction) {
		Reward reward = rewardMatrix[agentOneAction][agentTwoAction];
		Transition transition = transitionMatrix[agentOneAction][agentTwoAction];
		
		rewards = reward.getReward();
		nextState = transition.getNextState();
	}

	public int getNextState() {
		return nextState;
	}
	
	public double[] getReward() {
		return rewards;
	}

	public boolean isTerminalState() {
		return terminalState;
	}

}
