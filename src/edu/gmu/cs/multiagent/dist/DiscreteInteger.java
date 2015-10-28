package edu.gmu.cs.multiagent.dist;

import java.util.ArrayList;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

public class DiscreteInteger implements Distribution {

	EnumeratedIntegerDistribution distribution;
	
	public DiscreteInteger(ArrayList<Integer> values, ArrayList<Double> probs) {
		int[] valueArray = new int[values.size()];
		double[] probArray = new double[probs.size()];
		for(int i = 0;i<values.size();++i) {
			valueArray[i] = values.get(i);
		}
		for(int i = 0;i<probs.size();++i) {
			probArray[i] = probs.get(i);
		}
		distribution = new EnumeratedIntegerDistribution(valueArray, probArray);
	}

	@Override
	public double sample() {
		return distribution.sample();
	}

}
