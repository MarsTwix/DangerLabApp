package Map.objects;

import dangerlabapp.DangerLabApp;
import nl.han.ica.oopg.objects.Sprite;

public class DeathBall extends Ball {

	/**
	 * Generates a new DeathBall
	 * @param world     The world the ball is located in
	 * @param direction The direction the ball is moving in, false for vertical &
	 *                  true for horizontal movement.
	 * @param speed     The speed at which the ball travels.
	 * @author R3omar
	 */
	public DeathBall(DangerLabApp world, boolean direction, float speed) {
		super(world, new Sprite(DangerLabApp.MEDIA_URL.concat("deathBall.png")), direction, speed);
	}

	/**
	 * Generates a new DeathBall
	 * @param world     The world the ball is located in
	 * @param direction The direction the ball is moving in, false for vertical &
	 *                  true for horizontal movement.
	 * @author R3omar
	 */
	public DeathBall(DangerLabApp world, boolean direction) {
		super(world, new Sprite(DangerLabApp.MEDIA_URL.concat("deathBall.png")), direction);
	}
}
