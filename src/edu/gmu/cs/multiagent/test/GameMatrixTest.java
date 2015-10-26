package edu.gmu.cs.multiagent.test;

import org.junit.Test;

import edu.gmu.cs.multiagent.matrix.Game;

public class GameMatrixTest {
	@Test
	public void readJson() {
		Game game = new Game("test.json");
	}
}
