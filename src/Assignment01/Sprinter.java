package Assignment01;

/**Author: Arion
 * inheritance from Athlete
 * initializing the basic information through superclass constructor
 */
public class Sprinter extends Athlete {
	
	public Sprinter(String id, String name, int age, String state)
	{
		super(id, SPRINTER, name, age, state);
	}
}
