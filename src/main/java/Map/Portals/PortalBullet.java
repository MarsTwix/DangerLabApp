package Map.Portals;

import java.util.List;

import Map.tiles.FloorTile;
import Map.tiles.KillTile;
import Player.Player;
import dangerlabapp.DangerLabApp;
import nl.han.ica.oopg.collision.CollidedTile;
import nl.han.ica.oopg.collision.CollisionSide;
import nl.han.ica.oopg.collision.ICollidableWithTiles;
import nl.han.ica.oopg.exceptions.TileNotFoundException;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.objects.SpriteObject;
import processing.core.PVector;

public class PortalBullet extends SpriteObject implements ICollidableWithTiles {
	private DangerLabApp world;
	private Player player;
	private boolean color;
	private final int BULLETSPEED = 20;
	
	/**
	 * 
	 * @param world
	 * @param player
	 * @param color
	 * @author Marnix
	 */
	public PortalBullet(DangerLabApp world, Player player, boolean color) {
		super(new Sprite(DangerLabApp.MEDIA_URL.concat(BulletTypes.getBulletAddress(color))));
		this.world = world;
		this.color = color;
		this.player = player;
		this.player.setShooting(false);
	}

	@Override
	public void tileCollisionOccurred(List<CollidedTile> collidedTiles) {
		PVector vector;
		for (CollidedTile ct : collidedTiles) {
			if(ct.getTile() instanceof KillTile) {
				world.deleteGameObject(this);
				player.setShooting(false);
			}
			else if (ct.getTile() instanceof FloorTile) {

				try {
					vector = world.getTileMap().getTilePixelLocation(ct.getTile());

					if (ct.getCollisionSide().equals(CollisionSide.TOP)) {
						world.deleteGameObject(this);
						world.placePortal(vector.x , vector.y - 2, color, true, false);
						player.setShooting(false);
					} else if (ct.getCollisionSide().equals(CollisionSide.RIGHT)) {
						world.deleteGameObject(this);
						world.placePortal(vector.x + world.getTileMap().getTileSize(), vector.y, color, false, true);
						player.setShooting(false);
					} else if (ct.getCollisionSide().equals(CollisionSide.BOTTOM)) {
						world.deleteGameObject(this);
						world.placePortal(vector.x, vector.y + world.getTileMap().getTileSize(), color, true, true);
						player.setShooting(false);
					} else if (ct.getCollisionSide().equals(CollisionSide.LEFT)) {
						world.deleteGameObject(this);
						world.placePortal(vector.x-2, vector.y, color, false, false);
						player.setShooting(false);
					} else if (ct.getCollisionSide().equals(CollisionSide.INSIDE)) {
						world.deleteGameObject(this);
						player.setShooting(false);
					}

				} catch (TileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void update() {
		if (x < 0) {
			player.setShooting(false);
			world.deleteGameObject(this);
		}
		if (x > world.getWidth() - this.getWidth()) {
			player.setShooting(false);
			world.deleteGameObject(this);
		}
		if (y < 0) {
			player.setShooting(false);
			world.deleteGameObject(this);
		}
		if (y > world.getHeight() - this.getHeight()) {
			player.setShooting(false);
			world.deleteGameObject(this);
		}
	}

	public void shoot() {
		setDirectionSpeed((float) world.getPortalAimAngle(), BULLETSPEED);
		player.setShooting(true);
	}

}
