package edu.gmu.cs.multiagent.matrix;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.stream.JsonReader;

import edu.gmu.cs.multiagent.dist.Discrete;
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
			String rewardType = reader.nextString();
			if(rewardType.equals("Discrete")) {
				distribution = new Discrete();
				parseDiscreteReward(reader);
			}
			reader.endObject();
		} catch (IOException e) {
			System.err.println("Error at parsing reward object");
		}
		
		
	}

	private void parseDiscreteReward(JsonReader reader) throws IOException {

		while(reader.hasNext()) {
			double rewardValue = Double.parseDouble(reader.nextName());
			double rewardProb = reader.nextDouble();
			((Discrete)distribution).addItem(rewardValue, rewardProb);
		}
		
	}

	public double getReward() {
		return distribution.sample();
	}

}
