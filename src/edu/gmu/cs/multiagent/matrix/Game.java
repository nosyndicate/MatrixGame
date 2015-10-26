package edu.gmu.cs.multiagent.matrix;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.stream.*;

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
	private String gameName;
	private int agentOneActionNum;
	private int agentTwoActionNum;
	private ArrayList<State> stateList;
	private double[] rewards;

	public Game(String gameFile) {
		parseGame(gameFile);
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
					System.out.println(gameName);
				}
				else if (name.equals("AgentOneAction")) {
					agentOneActionNum = jsonReader.nextInt();
				}
				else if (name.equals("AgentTwoAction")) {
					agentTwoActionNum = jsonReader.nextInt();
				}
				else if (name.equals("Matrices")) {
					jsonReader.beginArray();

					// read the data of each state
					while (jsonReader.hasNext()) {
						State state = new State(jsonReader, agentOneActionNum, agentTwoActionNum);
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
}
