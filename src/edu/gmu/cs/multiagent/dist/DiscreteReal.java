package edu.gmu.cs.multiagent.dist;

import java.util.ArrayList;
import org.apache.commons.math3.distribution.EnumeratedRealDistribution;

public class DiscreteReal implements Distribution {
	private EnumeratedRealDistribution distribution;
	
	public DiscreteReal(ArrayList<Double> values, ArrayList<Double> probs) {
		double[] valueArray = new double[values.size()];
		double[] probArray = new double[probs.size()];
		for(int i = 0;i<values.size();++i) {
			valueArray[i] = values.get(i);
		}
		for(int i = 0;i<probs.size();++i) {
			probArray[i] = probs.get(i);
		}
		distribution = new EnumeratedRealDistribution(valueArray, probArray);
	}

	@Override
	public double sample() {
		return distribution.sample();
	}
}
