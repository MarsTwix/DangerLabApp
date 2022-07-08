package Player;

import java.util.List;
import java.math.*;
import dangerlabapp.DangerLabApp;
import nl.han.ica.oopg.collision.CollidedTile;
import nl.han.ica.oopg.collision.CollisionSide;
import nl.han.ica.oopg.collision.ICollidableWithGameObjects;
import nl.han.ica.oopg.collision.ICollidableWithTiles;
import nl.han.ica.oopg.exceptions.TileNotFoundException;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.objects.SpriteObject;
import processing.core.PConstants;
import processing.core.PVector;
import Map.Portals.PlayerMimic;
import Map.Portals.Portal;
import Map.objects.DeathBall;
import Map.objects.ResetBall;
import Map.Portals.PortalAim;
import Map.tiles.*;

public class Player extends SpriteObject implements ICollidableWithTiles, ICollidableWithGameObjects {

	private DangerLabApp world;
	private PortalAim portalAim;
	private int teleportTimer;
	private boolean isJumping;
	private boolean isFlying;
	private boolean shooting;
	private static String spriteLocation = "character_robot_idle_small.png";

	private boolean mayFly;
	private boolean mayBlink;

	private static final int characterWidth = 44;
	private static final int characterHeight = 64;

	private float speed = 5;
	private float jumpheight = 35;

	private boolean[] keys = new boolean[4];

	private final int UPKEY = 0;
	private final int DOWNKEY = 1;
	private final int RIGHTKEY = 2;
	private final int LEFTKEY = 3;

	private boolean inPortal;
	private boolean portalColor;

	/**
	 * Generates a new player
	 * 
	 * @param world The world the player is placed in.
	 * @author R3omar, MarsTwix
	 */
	public Player(DangerLabApp world) {
		super(new Sprite(DangerLabApp.MEDIA_URL.concat(spriteLocation)));
		this.world = world;
		isJumping = true;
		setGravity(1f);
		setFriction(0.2f);
		inPortal = false;
		portalColor = false;
		shooting = false;
		mayFly = false;
		mayBlink = false;
	}

	/**
	 * Sets the portalAim of the player
	 * 
	 * @param portalAim The portalAim to set to the player
	 * @author MarsTwix
	 */
	public void setPortalAim(PortalAim portalAim) {
		this.portalAim = portalAim;
	}

	@Override
	public void update() {
		keyLoop();
		restrainPlayer();
		world.refreshDasboardText();
		managePortal();
		manageFlight();
	}

	/**
	 * Manages the portal
	 * 
	 * @author R3omar
	 */
	private void managePortal() {
		if (teleportTimer > 0) {
			teleportTimer--;
		}

		if (teleportTimer == 0) {
			this.inPortal = false;

		}
	}

	private void manageFlight() {
		if (getGravity() == 0) {
			isJumping = false;
		}
	}

	@Override
	public void keyPressed(int keyCode, char key) {
		if (key == 'w' || key == UP || key == ' ') {
			keys[UPKEY] = true;
		}

		if (key == 's' || key == DOWN) {
			keys[DOWNKEY] = true;
		}

		if (key == 'd' || key == RIGHT) {
			keys[RIGHTKEY] = true;
		}

		if (key == 'a' || key == LEFT) {
			keys[LEFTKEY] = true;
		}

		else if (key == 'r' && mayFly) {
			setGravity(0);
			isFlying = true;
		} else if (key == 't') {
			setGravity(0.2f);
			isFlying = false;
		}
	}

	@Override
	public void keyReleased(int keyCode, char key) {
		if (key == 'w' || keyCode == UP || key == ' ') {
			keys[UPKEY] = false;
		}

		if (key == 's' || keyCode == DOWN) {
			keys[DOWNKEY] = false;
		}

		if (key == 'd' || keyCode == RIGHT) {
			keys[RIGHTKEY] = false;
		}

		if (key == 'a' || keyCode == LEFT) {
			keys[LEFTKEY] = false;
		}
	}

	private void keyLoop() {
		if (keys[UPKEY] && !isJumping) {
			isJumping = true;
			if (isFlying) {
				setDirectionSpeed(0, speed);
			} else {
				setDirectionSpeed(0, jumpheight);
			}
		}
		if (keys[DOWNKEY]) {
			setDirectionSpeed(180, speed);
		}
		if (keys[RIGHTKEY]) {
			if (isJumping) {
				setDirectionSpeed(90, speed / 2);
			} else {
				setDirectionSpeed(90, speed);
			}
		}
		if (keys[LEFTKEY]) {
			if (isJumping) {
				setDirectionSpeed(270, speed / 2);
			} else {
				setDirectionSpeed(270, speed);
			}

		}
	}

	@Override
	public void mousePressed(int x, int y, int button) {
		if (button == LEFT) {
			world.schootPortal(portalColor);
			this.portalColor = !this.portalColor;

		}

		if (button == RIGHT) {
			this.portalColor = !this.portalColor;
		}

		if (button == CENTER && mayBlink) {
			super.setX(x - (super.getWidth() / 2));
			super.setY(y - (super.getHeight() / 2));
		}

		if (button == 0) {
			this.portalColor = !this.portalColor;
		}
	}

	private void restrainPlayer() {
		if (x + super.getWidth() > world.getWidth()) {
			super.setX(world.getWidth() - super.getWidth());
		} else if (x < 0) {
			super.setX(0);
		}
		if (y + super.getHeight() > world.getHeight()) {
			isJumping = false;
			super.setY(world.getHeight() - super.getHeight());
		} else if (y < 0) {
			super.setY(0);
		}
	}

	@Override
	public void tileCollisionOccurred(List<CollidedTile> collidedTiles) {
		PVector vector;
		for (CollidedTile ct : collidedTiles) {
			if (ct.getTile() instanceof KillTile) {
				PVector spawnpoint = world.getSpawnPos();
				setX(spawnpoint.x);
				setY(spawnpoint.y);
				world.deleteAllPortals();
			}

			else if (ct.getTile() instanceof ExitTile) {
				world.levelUp();
				world.deleteGameObject(this);
				world.deleteGameObject(portalAim);
			}

			else if (ct.getTile() instanceof FloorTile || ct.getTile() instanceof Glass) {

				try {
					vector = world.getTileMap().getTilePixelLocation(ct.getTile());
					if (ct.getCollisionSide().equals(CollisionSide.TOP)) {
						isJumping = false;
						setY(vector.y - getHeight());
					} else if (ct.getCollisionSide().equals(CollisionSide.RIGHT)) {
						setX(vector.x + world.getTileMap().getTileSize() + 1);
					} else if (ct.getCollisionSide().equals(CollisionSide.BOTTOM)) {
						setY(vector.y + world.getTileMap().getTileSize());
					} else if (ct.getCollisionSide().equals(CollisionSide.LEFT)) {
						setX(vector.x - getWidth() - 1);
					} else if (ct.getCollisionSide().equals(CollisionSide.INSIDE)) {
						setY(vector.y - getHeight() - 1);
					}

				} catch (TileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void gameObjectCollisionOccurred(List<GameObject> collidedGameObjects) {
		for (GameObject go : collidedGameObjects) {
			if (go instanceof ResetBall) {
				world.deleteAllPortals();
			} else if (go instanceof DeathBall) {
				world.deleteAllPortals();
				PVector spawn = world.getSpawnPos();
				this.x = spawn.x;
				this.y = spawn.y;
			}
		}
	}

	// Getters & Setters

	public int getTeleportTimer() {
		return teleportTimer;
	}

	public void setTeleportTimer(int teleportTimer) {
		this.teleportTimer = teleportTimer;
	}

	public static String getSpriteLocation() {
		return spriteLocation;
	}

	public float getSpeed() {
		return speed;
	}

	public float getJumpheight() {
		return jumpheight;
	}

	public boolean isInPortal() {
		return inPortal;
	}

	public void setInPortal(boolean inPortal) {
		this.inPortal = inPortal;
	}

	public boolean getPortalColor() {
		return portalColor;
	}

	public boolean isFlying() {
		return isFlying;
	}

	public boolean isFalling() {
		return isJumping;
	}

	public static float getCharacterWidth() {
		return characterWidth;
	}

	public static float getCharacterHeight() {
		return characterHeight;
	}

	public void setShooting(boolean b) {
		shooting = b;
	}

	public boolean isShooting() {
		return shooting;
	}

	public void setMayFly(boolean mayFly) {
		this.mayFly = mayFly;
	}

	public void setMayBlink(boolean mayBlink) {
		this.mayBlink = mayBlink;
	}
}