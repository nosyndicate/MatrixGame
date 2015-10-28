package edu.gmu.cs.multiagent.matrix;

import java.io.IOException;
import java.util.ArrayList;




import com.google.gson.stream.JsonReader;

import edu.gmu.cs.multiagent.dist.DiscreteInteger;
import edu.gmu.cs.multiagent.dist.DiscreteReal;
import edu.gmu.cs.multiagent.dist.Distribution;

/**
 * 
 * @author Ermo Wei
 *
 */

public class Reward {

	Distribution distribution;

	public Reward(JsonReader reader) { 
		
		try {
			reader.beginObject();
			reader.nextName();
			String rewardType = reader.nextString();
			if(rewardType.equals("Discrete")) {
				parseDiscreteReward(reader);
			}
			reader.endObject();
		} catch (IOException e) {
			System.err.println("Error at parsing reward object");
		}
		
		
	}

	private void parseDiscreteReward(JsonReader reader) throws IOException {
		ArrayList<Double> rewards = new ArrayList<Double>();
		ArrayList<Double> probs = new ArrayList<Double>();
		while(reader.hasNext()) {
			double rewardValue = Double.parseDouble(reader.nextName());
			double rewardProb = reader.nextDouble();
			rewards.add(rewardValue);
			probs.add(rewardProb);
		}
		
		distribution = new DiscreteReal(rewards, probs);
	}

	public double getReward() {
		return distribution.sample();
	}

}
