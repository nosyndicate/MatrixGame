package edu.gmu.cs.multiagent.matrix;

/**
 * 
 * @author Ermo Wei
 *
 */

public class State {

	private Transition[][] transitionMatrix;
	private Reward[][] rewardMatrix;
	private int nextState;
	private double[] rewardArray;
	
	public void play(int agentOneAction, int agentTwoAction) {
		Reward reward = rewardMatrix[agentOneAction][agentTwoAction];
		Transition transition = transitionMatrix[agentOneAction][agentTwoAction];
		
		rewardArray = reward.getReward();
		nextState = transition.getNextState();
	}

	public int getNextState() {
		return nextState;
	}
	
	public double[] getReward() {
		return rewardArray;
	}

}
