package edu.gmu.cs.multiagent.test;

import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import org.junit.Test;

import edu.gmu.cs.multiagent.matrix.Game;

public class GameMatrixTest {
	@Test
	public void readJson() {
		Game game = new Game("test.json");
		game.play(0, 1);
		double r = game.getReward(true);
		assertTrue(r==4);
		assertTrue(game.isGameEnd()==true);
	}
}
