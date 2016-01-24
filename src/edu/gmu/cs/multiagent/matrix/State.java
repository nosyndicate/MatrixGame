package edu.gmu.cs.multiagent.matrix;

import java.io.IOException;
import java.util.ArrayList;
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
	private ArrayList<Integer[]> policy;
	private boolean correctPolicyState;
	private boolean terminalState = false;
	public int numAgent1Action;
	public int numAgent2Action;

	public State(JsonReader jsonReader) {
		rewards = new double[2];
		policy = new ArrayList<Integer[]>();
		correctPolicyState = false;

		try {
			int counter = 0;
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {
				String matrixName = jsonReader.nextName();
				if (matrixName.equals("Rewards") && counter == 0) {
					rewardMatrixOne = parseRewards(jsonReader);
					counter++;
				} else if (matrixName.equals("Rewards") && counter == 1) {
					rewardMatrixTwo = parseRewards(jsonReader);
					counter++;
				} else if (matrixName.equals("Transitions")) {
					transitionMatrix = parseTransitions(jsonReader);
				} else if (matrixName.equals("Type")) {
					String value = jsonReader.nextString();
					if (value.equals("NULL")) {
						terminalState = true;
					}
				} else if (matrixName.equals("Policy")) {
					parsePolicy(jsonReader);
				} else if (matrixName.equals("Trajectory")) {
					int value = jsonReader.nextInt();
					if (value == 1)
						correctPolicyState = true;
				}
			}
			jsonReader.endObject();

			// we have a coopertive game
			if (rewardMatrixTwo == null) {
				rewardMatrixTwo = rewardMatrixOne;
			}
			
			if(!terminalState) {
				int rewardOneRow = rewardMatrixOne.length;
				int rewardTwoRow = rewardMatrixTwo.length;
				int transRow = transitionMatrix.length;
				
				
				int rewardOneColumn = rewardMatrixOne[0].length;
				int rewardTwoColumn = rewardMatrixTwo[0].length;
				int transColumn = transitionMatrix[0].length;
				
				if(!equals(rewardOneRow, rewardTwoRow, transRow))
					new Exception("The row number do not match");
				
				if(!equals(rewardOneColumn, rewardTwoColumn, transColumn))
					new Exception("The column number do not match");
				
				numAgent1Action = rewardOneRow;
				numAgent2Action = rewardOneColumn;
			}
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean equals(int a, int b, int c) {
		if(a==b&&a==c&&b==c)
			return true;
		return false;
	}
	

	private void parsePolicy(JsonReader jsonReader) {
		
		try {
			jsonReader.beginArray();
			while (jsonReader.hasNext()) {
				jsonReader.beginArray();
				Integer[] policy = new Integer[2];
				int counter = 0;
				while(jsonReader.hasNext()) {
					int value = jsonReader.nextInt();
					policy[counter++] = value;
				}
				jsonReader.endArray();
				this.policy.add(policy);
			}
			jsonReader.endArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Reward[][] parseRewards(JsonReader jsonReader) {
		Reward[][] matrix = null;

		try {
			int rowCounter = 0;
			int columnCounter = 0;
			int rows = 0;
			int columns = 0;
			
			ArrayList<Reward> list = new ArrayList<Reward>();

			jsonReader.beginArray();
			// parsing it row by row
			while (jsonReader.hasNext()) {
				jsonReader.beginArray();
				while (jsonReader.hasNext()) {
					list.add(new Reward(jsonReader));
					columnCounter++;
				}
				rowCounter++;
				columns = columnCounter;
				columnCounter = 0;
				jsonReader.endArray();
			}
			rows = rowCounter;
			jsonReader.endArray();

			matrix = transformReward(list, rows, columns);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matrix;
	}

	private Reward[][] transformReward(ArrayList<Reward> list, int rows, int columns) throws Exception {
		int rowCounter = 0;
		int columnCounter = 0;

		Reward[][] matrix = new Reward[rows][columns];
		if (rows * columns != list.size()) {
			throw new Exception("The size did match");
		}

		for (int i = 0; i < list.size(); ++i) {
			matrix[rowCounter][columnCounter] = list.get(i);
			columnCounter++;
			if (columnCounter == columns) {
				rowCounter++;
				columnCounter = 0;
			}
		}

		return matrix;
	}

	private Transition[][] transformTransition(ArrayList<Transition> list, int rows, int columns) throws Exception {
		int rowCounter = 0;
		int columnCounter = 0;

		Transition[][] matrix = new Transition[rows][columns];
		if (rows * columns != list.size()) {
			throw new Exception("The size did match");
		}

		for (int i = 0; i < list.size(); ++i) {
			matrix[rowCounter][columnCounter] = list.get(i);
			columnCounter++;
			if (columnCounter == columns) {
				rowCounter++;
				columnCounter = 0;
			}
		}

		return matrix;
	}

	private Transition[][] parseTransitions(JsonReader jsonReader) {
		Transition[][] matrix = null;

		try {
			int rowCounter = 0;
			int columnCounter = 0;
			int rows = 0;
			int columns = 0;
		
			ArrayList<Transition> list = new ArrayList<Transition>();

			jsonReader.beginArray();
			// parsing it row by row
			while (jsonReader.hasNext()) {
				jsonReader.beginArray();
				while (jsonReader.hasNext()) {
					list.add(new Transition(jsonReader));
					columnCounter++;
				}
				rowCounter++;
				columns = columnCounter;
				columnCounter = 0;
				jsonReader.endArray();
			}
			rows = rowCounter;
			jsonReader.endArray();

			matrix = transformTransition(list, rows, columns);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matrix;
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

	public boolean checkPolicy(int i, int j) {
		if(this.policy.size()==0)  //all policy is valid
			return true;
		for(int k = 0;k<policy.size();++k) {
			if (policy.get(k)[0] == i && policy.get(k)[1] == j)
				return true;
		}
		
		return false;
	}

	public boolean correctPolicyState() {
		return correctPolicyState;
	}

}
