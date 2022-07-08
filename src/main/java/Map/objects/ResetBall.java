package Map.objects;

import java.util.List;

import Map.Portals.PortalBullet;
import dangerlabapp.DangerLabApp;
import nl.han.ica.oopg.collision.ICollidableWithGameObjects;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;

public class ResetBall extends Ball implements ICollidableWithGameObjects {

	/**
	 * Generates a new ResetBall, resets all portals on contact
	 * @param world     The world the ball is located in
	 * @param direction The direction the ball is moving in, false for vertical &
	 *                  true for horizontal movement.
	 * @param speed     The speed at which the ball travels.
	 * @author R3omar
	 */
	public ResetBall(DangerLabApp world, boolean direction, float speed) {
		super(world, new Sprite(DangerLabApp.MEDIA_URL.concat("resetBall.png")), direction, speed);
	}

	/**
	 * Generates a new ResetBall, resets all portals on contact
	 * @param world     The world the ball is located in
	 * @param direction The direction the ball is moving in, false for vertical &
	 *                  true for horizontal movement.
	 * @author R3omar
	 */
	public ResetBall(DangerLabApp world, boolean direction) {
		super(world, new Sprite(DangerLabApp.MEDIA_URL.concat("resetBall.png")), direction);
	}

	@Override
	public void gameObjectCollisionOccurred(List<GameObject> collidedGameObjects) {
		for (GameObject go : collidedGameObjects) {
			if (go instanceof PortalBullet) {
				super.world.deleteGameObject(go);
				world.getPlayer().setShooting(false);
			}

		}
	}

}
