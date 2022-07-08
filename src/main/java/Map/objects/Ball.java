package Map.objects;

import java.util.List;

import Map.tiles.FloorTile;
import Map.tiles.KillTile;
import dangerlabapp.DangerLabApp;
import nl.han.ica.oopg.collision.CollidedTile;
import nl.han.ica.oopg.collision.CollisionSide;
import nl.han.ica.oopg.collision.ICollidableWithTiles;
import nl.han.ica.oopg.exceptions.TileNotFoundException;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.objects.SpriteObject;
import processing.core.PVector;

public abstract class Ball extends SpriteObject implements ICollidableWithTiles {

	protected DangerLabApp world;

	private float speed;

	// false = vertical, true == horizontal
	private boolean direction;

	
	/**
	 * 
	 * @param world The world the ball is located in
	 * @param sprite The image the ball uses
	 * @param direction The direction the ball is moving in, false for vertical & true for horizontal movement.
	 * @param speed The speed at which the ball moves.
	 * @author R3omar
	 */
	public Ball(DangerLabApp world, Sprite sprite, boolean direction, float speed) {
		super(sprite);
		this.world = world;
		this.direction = direction;
		this.speed = speed;
		if (direction) {
			this.setDirectionSpeed(0, speed);
		} else {
			this.setDirectionSpeed(90, speed);
		}
	}

	/**
	 * 
	 * @param world The world the ball is located in
	 * @param sprite The image the ball uses
	 * @param direction The direction the ball is moving in, false for vertical & true for horizontal movement.
	 * @author R3omar
	 */
	public Ball(DangerLabApp world, Sprite sprite, boolean direction) {
		super(sprite);
		this.world = world;
		this.direction = direction;
		this.speed = 5;
		if (direction) {
			this.setDirectionSpeed(0, speed);
		} else {
			this.setDirectionSpeed(90, speed);
		}
	}

	@Override
	public void update() {

		if (x < 0) {
			setDirectionSpeed(90, speed);
		}
		if (x > world.getWidth() - this.getWidth()) {
			setDirectionSpeed(270, speed);
		}

		if (y < 0) {
			setDirectionSpeed(0, speed);
		}
		if (y > world.getHeight() - this.getHeight()) {
			setDirectionSpeed(180, speed);
		}
	}

	@Override
	public void tileCollisionOccurred(List<CollidedTile> collidedTiles) {
		PVector vector;
		for (CollidedTile ct : collidedTiles) {
			if (ct.getTile() instanceof FloorTile || ct.getTile() instanceof KillTile) {

				try {
					vector = world.getTileMap().getTilePixelLocation(ct.getTile());

					if (ct.getCollisionSide().equals(CollisionSide.TOP)) {
						setDirectionSpeed(0, speed);
					} else if (ct.getCollisionSide().equals(CollisionSide.RIGHT)) {
						setDirectionSpeed(270, speed);
					} else if (ct.getCollisionSide().equals(CollisionSide.BOTTOM)) {
						setDirectionSpeed(180, speed);
					} else if (ct.getCollisionSide().equals(CollisionSide.LEFT)) {
						setDirectionSpeed(90, speed);
					} else if (ct.getCollisionSide().equals(CollisionSide.INSIDE)) {
						setY(y - 50);
					}

				} catch (TileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
