package edu.gmu.cs.multiagent.matrix;

import java.io.IOException;

import com.google.gson.stream.JsonReader;

import edu.gmu.cs.multiagent.dist.Discrete;

/**
 * 
 * @author Ermo Wei
 *
 */

public class Transition {

	Discrete distribution;
	
	public Transition(JsonReader reader) {
		distribution = new Discrete();
		try {
			reader.beginObject();
			while(reader.hasNext()) {
				double state = Double.parseDouble(reader.nextName());
				double stateProb = reader.nextDouble();
				distribution.addItem(state, stateProb);
			}
			
			reader.endObject();
		} catch (IOException e) {
			System.err.println("Error at parsing reward object");
		}
		
	}
	
	
	public int getNextState() {
		return (int)distribution.sample();
	}
	
}
