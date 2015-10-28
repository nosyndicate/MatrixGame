package edu.gmu.cs.multiagent.matrix;

import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.stream.JsonReader;
import edu.gmu.cs.multiagent.dist.DiscreteInteger;


/**
 * 
 * @author Ermo Wei
 *
 */

public class Transition {

	DiscreteInteger distribution;
	
	public Transition(JsonReader reader) {
		ArrayList<Integer> states = new ArrayList<Integer>();
		ArrayList<Double> probs = new ArrayList<Double>();
		try {
			reader.beginObject();
			while(reader.hasNext()) {
				int state = Integer.parseInt(reader.nextName());
				double stateProb = reader.nextDouble();
				states.add(state);
				probs.add(stateProb);
			}
			reader.endObject();
			distribution = new DiscreteInteger(states, probs);
		} catch (IOException e) {
			System.err.println("Error at parsing transition object");
		}
		
	}
	
	
	public int getNextState() {
		return (int)distribution.sample();
	}
	
}
