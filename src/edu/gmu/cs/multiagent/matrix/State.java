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
	private Reward[][] rewardMatrixOne;
	private Reward[][] rewardMatrixTwo;
	private int nextState;
	private double[] rewards;
	private boolean terminalState = false;
	private int agentOneActionNum = 0;
	private int agentTwoActionNum = 0;
	
	public State(JsonReader jsonReader, int agentOneActionNum, int agentTwoActionNum) {
		rewards = new double[2];
		this.agentOneActionNum = agentOneActionNum;
		this.agentTwoActionNum = agentTwoActionNum;
		
		transitionMatrix = new Transition[agentOneActionNum][agentTwoActionNum];
		try {
			int counter = 0;
			jsonReader.beginObject();
			String matrixName = jsonReader.nextName();
			if (matrixName.equals("Rewards")&&counter==0) {
				rewardMatrixOne = new Reward[agentOneActionNum][agentTwoActionNum];
				parseRewards(jsonReader, rewardMatrixOne);
				counter++;
			} else if(matrixName.equals("Rewards")&&counter==1) {
				rewardMatrixTwo = new Reward[agentOneActionNum][agentTwoActionNum];
				parseRewards(jsonReader, rewardMatrixTwo);
				counter++;
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
			
			// we have a coopertive game
			if(rewardMatrixTwo==null) { 
				rewardMatrixTwo = rewardMatrixOne;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private void parseRewards(JsonReader jsonReader, Reward[][] rewardMatrix) {
		try {
			int rowCounter = 0;
			int columnCounter = 0;
			
			jsonReader.beginArray();
			// parsing it row by row
			while(jsonReader.hasNext()) {
				jsonReader.beginArray();
				while(jsonReader.hasNext()) {
					rewardMatrix[rowCounter][columnCounter] = new Reward(jsonReader);
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
		Reward rewardOne = rewardMatrixOne[agentOneAction][agentTwoAction];
		Reward rewardTwo = rewardMatrixTwo[agentOneAction][agentTwoAction];
		Transition transition = transitionMatrix[agentOneAction][agentTwoAction];
		
		rewards[0] = rewardOne.getReward();
		rewards[1] = rewardTwo.getReward();
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
