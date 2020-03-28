package eu.wauz.wauzunit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.wauz.wauzcore.players.calc.ExperienceCalculator;

/**
 * Tests the leveling mechanics from WauzCore.
 * 
 * @author Wauzmons
 */
public class LevelingTest {
	
	/**
	 * Tests the sequence, used to calculate the exp to the next level.
	 */
	@Test
	public void testExpSequence() {
		assertEquals(15, ExperienceCalculator.getExpToLevel(2));
		assertEquals(1255, ExperienceCalculator.getExpToLevel(20));
		assertEquals(79304, ExperienceCalculator.getExpToLevel(60));
	}

}