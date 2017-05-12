/**
 * 
 */
package JUnitTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Controller.Driver;
import Model.Athlete;
import Model.Game;
import Model.Swimmer;
import Model.Cyclist;
import Model.Sprinter;

/**
 * @author Arion
 *
 */
public class AthleteTest_compareTo {

	Athlete swimmer1, swimmer2;
	Athlete cyclist1, cyclist2;
	Athlete sprinter1, sprinter2;
	double time1, time2;
	
	@Before
	public void setUp() throws Exception {
		swimmer1 = new Swimmer("swimmer1 id", "swimmer1 name", 20, "swimmer1 state");
		swimmer2 = new Swimmer("swimmer2 id", "swimmer2 name", 21, "swimmer2 state");
		cyclist1 = new Cyclist("cyclist1 id", "cyclist1 name", 20, "cyclist1 state");
		cyclist2 = new Cyclist("cyclist2 id", "cyclist2 name", 21, "cyclist2 state");
		sprinter1 = new Sprinter("sprinter1 id", "sprinter1 name", 20, "sprinter1 state");
		sprinter2 = new Sprinter("sprinter2 id", "sprinter2 name", 20, "sprinter2 state");
	}

	/**
	 * Test compareTo method for swimmers.
	 */
	@Test
	public void testCompareTo_Swimmer() {
		Driver.currentGame = new Game("Swimming");
		time1 = swimmer1.Compete();
		time2 = swimmer2.Compete();
		swimmer1.setExecuteTime(time1);
		swimmer2.setExecuteTime(time2);
		System.out.println("Swimmer1 time: " + time1 + "\nSwimmer2 time: " + time2);
		
		if (time1 > time2) assertTrue(swimmer1.compareTo(swimmer2) > 0);
		if (time1 < time2) assertTrue(swimmer1.compareTo(swimmer2) < 0);
		if (time1 == time2) assertEquals(0,swimmer1.compareTo(swimmer2));
	}

	/**
	 * Test compareTo method for cyclists.
	 */
	@Test
	public void testCompareTo_Cyclist() {
		Driver.currentGame = new Game("Cycling");
		time1 = cyclist1.Compete();
		time2 = cyclist2.Compete();
		cyclist1.setExecuteTime(time1);
		cyclist2.setExecuteTime(time2);
		System.out.println("Cyclist1 time: " + time1 + "\nCyclist2 time: " + time2);
		
		if (time1 > time2) assertTrue(cyclist1.compareTo(cyclist2) > 0);
		if (time1 < time2) assertTrue(cyclist1.compareTo(cyclist2) < 0);
		if (time1 == time2) assertEquals(0,cyclist1.compareTo(cyclist2));
	}
	
	/**
	 * Test compareTo method for sprinters.
	 */
	@Test
	public void testCompareTo_Sprinter() {
		Driver.currentGame = new Game("Running");
		time1 = sprinter1.Compete();
		time2 = sprinter2.Compete();
		sprinter1.setExecuteTime(time1);
		sprinter2.setExecuteTime(time2);
		System.out.println("Sprinter1 time: " + time1 + "\nSprinter2 time: " + time2);
		
		if (time1 > time2) assertTrue(sprinter1.compareTo(sprinter2) > 0);
		if (time1 < time2) assertTrue(sprinter1.compareTo(sprinter2) < 0);
		if (time1 == time2) assertEquals(0,sprinter1.compareTo(sprinter2));
	}
}
