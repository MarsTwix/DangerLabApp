package Map.Portals;

import java.util.List;

import Map.tiles.FloorTile;
import Player.Player;
import dangerlabapp.DangerLabApp;
import nl.han.ica.oopg.collision.CollidedTile;
import nl.han.ica.oopg.collision.CollisionSide;
import nl.han.ica.oopg.collision.ICollidableWithGameObjects;
import nl.han.ica.oopg.collision.ICollidableWithTiles;
import nl.han.ica.oopg.exceptions.TileNotFoundException;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.objects.SpriteObject;
import processing.core.PVector;

public class Portal extends SpriteObject implements ICollidableWithGameObjects, ICollidableWithTiles {

	private final boolean FANCYPORTALS = false;

	// True = Horizontal, False == Vertical
	protected boolean orientation;

	// True == Red, False == Blue
	protected boolean color;

	// True == left || Above, false == right || below
	private boolean visibleSide;

	// 0 = non, 1 == left, 2 == right, 3 == above, 4 == below
	private int enteredSide = 0;

	private boolean entryPortal;

	// False == left || Up, true == right || below
	private boolean exitSide;

	private final float portalSafetyMargin = 6;

	private static final float portalSafetyDistance = 32;

	// Contains linked portal
	protected Portal linkedPortal;

	private PortalScreen screen;

	private DangerLabApp world;

	private PlayerMimic mimic;

	/**
	 * @param world       The world the portal is placed in.
	 * @param x           The X-coordinate of the portal.
	 * @param y           The Y-coordinate of the portal.
	 * @param color       The color of the portal, false for blue & true for red.
	 * @param orientation The orientation of the portal, false for vertical & true
	 *                    for horizontal.
	 * @param exitSide    The side at witch a character leaves the portal, false for
	 *                    left, up & true for right, below.
	 * @author R3omar
	 */
	public Portal(DangerLabApp world, float x, float y, boolean color, boolean orientation, boolean exitSide) {
		super(new Sprite(DangerLabApp.MEDIA_URL.concat(PortalTypes.getPortalAddress(color, orientation))));
		this.world = world;

		super.x = x;
		super.y = y;

		this.color = color;
		this.orientation = orientation;
		this.exitSide = exitSide;

		this.screen = new PortalScreen(this, true);
		this.screen.setVisible(false);
		world.addGameObject(this.screen);

		if (FANCYPORTALS) {
			mimic = new PlayerMimic(world.getPlayer(), x, y);
			mimic.setVisible(false);
			world.addGameObject(mimic);
		}
	}

	/**
	 * 
	 * @param world        The world the portal is placed in.
	 * @param x            The X-coordinate of the portal.
	 * @param y            The Y-coordinate of the portal.
	 * @param linkedPortal The portal this portal is linked to.
	 * @param color        The color of the portal, false for blue & true for red.
	 * @param orientation  The orientation of the portal, false for vertical & true
	 *                     for horizontal.
	 * @param exitSide     The side at witch a character leaves the portal, false
	 *                     for left, up & true for right, below.
	 * @author R3omar
	 */
	public Portal(DangerLabApp world, float x, float y, Portal linkedPortal, boolean color, boolean orientation,
			boolean exitSide) {
		super(new Sprite(DangerLabApp.MEDIA_URL.concat(PortalTypes.getPortalAddress(color, orientation))));
		this.linkedPortal = linkedPortal;
		this.world = world;

		super.x = x;
		super.y = y;

		this.color = color;
		this.orientation = orientation;
		this.exitSide = exitSide;

		this.screen = new PortalScreen(this, true);
		this.screen.setVisible(false);
		world.addGameObject(this.screen);

		if (FANCYPORTALS) {
			mimic = new PlayerMimic(world.getPlayer(), x, y);
			mimic.setVisible(false);
			world.addGameObject(mimic);
		}
	}

	@Override
	public void update() {
		if (this.y + super.getHeight() > world.getHeight()) {
			this.setY(world.getHeight() - super.getHeight());
		}

		if (screen.isVisible()) {
			world.deleteGameObject(screen);
			// screen.setTargetSide(!visibleSide);
			world.addGameObject(screen);
		}

//		if (!world.getPlayer().isInPortal() && world.getPlayer().getTeleportTimer() == 0) {
//			this.screen.setVisible(false);
//		}

		if (linkedPortal.isEntryPortal()) {
			this.entryPortal = false;
			this.screen.setVisible(true);
		}
	}

	@Override
	public void keyPressed(int keyCode, char key) {
		if (key == 'q') {
			removePortal();
		}
	}

	public void removePortal() {
		this.screen.setVisible(false);
		setVisible(false);
	}

	@SuppressWarnings("unused")
	@Override
	public void gameObjectCollisionOccurred(List<GameObject> collidedGameObjects) {
		if (isVisible()) {
			for (GameObject go : collidedGameObjects) {
				if (go instanceof Player) {
					if (linkedPortal != null && ((Player) go).getTeleportTimer() == 0 && linkedPortal.isVisible()) {

						if (!FANCYPORTALS) {
							teleportNonFancy((Player) go);
						}
					} else if (FANCYPORTALS && linkedPortal.isOrientation() == this.orientation) {
						if (!orientation) {
							if (!exitSide) {
								if (go.getY() <= this.getY() + portalSafetyMargin
										&& go.getY() + go.getHeight() >= this.getY() + this.getHeight()) {
									teleportVertDiff((Player) go);
								}
							} else {
								if (go.getY() <= this.getY() + portalSafetyMargin
										&& go.getY() + go.getHeight() >= this.getY() + this.getHeight()) {
									teleportVertDiff((Player) go);
								}
							}
						} else {
							// ------------------------------------------------------------------------------------------------------------------------------------
							if (exitSide) {
								if (go.getX() >= this.getX()
										&& go.getX() + go.getWidth() <= this.getX() + this.getWidth()) {

									teleportHorzDiff((Player) go);

								}
							} else {
								if (go.getX() >= this.getX()
										&& go.getX() + go.getWidth() <= this.getX() + this.getWidth()) {
									teleportHorzSame((Player) go);
								}
							}
						}

					}
				}
			}
		}
	}

	@Override
	public void tileCollisionOccurred(List<CollidedTile> collidedTiles) {
		boolean collidedWithTile = false;
		for (CollidedTile ct : collidedTiles) {
			if (ct.getTile() instanceof FloorTile) {
				try {
					removePortal();
				} catch (TileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// TeleportFunctions

	/**
	 * 
	 * @param go The player to teleport
	 * @author R3omar
	 */
	private void teleportNonFancy(Player go) {
		if (orientation) {
			// if (go.getX() >= this.getX() && go.getX() + go.getWidth() <= this.getX() +
			// this.getWidth()) {
			if (linkedPortal.orientation == this.orientation) {
				if (go.getY() + go.getHeight() > this.y) {
					if (linkedPortal.isExitSide()) {
						go.setX(linkedPortal.getX());
						go.setY(linkedPortal.getY() + 5);

					} else {
						go.setX(linkedPortal.getX());
						go.setY(linkedPortal.getY() - go.getHeight());
						go.setDirectionSpeed(0, 15);
					}
				} else {
					if (linkedPortal.isExitSide()) {
						go.setX(linkedPortal.getX());
						go.setY(linkedPortal.getY());
					} else {
						go.setX(linkedPortal.getX());
						go.setY(linkedPortal.getY());
					}
				}
			} else {
				if (linkedPortal.isExitSide()) {
					go.setX(linkedPortal.getX() + linkedPortal.getWidth());
					go.setY(linkedPortal.getY());
				} else {
					go.setX(linkedPortal.getX() - go.getWidth());
					go.setY(linkedPortal.getY());
				}

				((Player) go).setTeleportTimer(10);
			}
			// }
		} else

		{
			if (go.getY() <= this.getY() + portalSafetyMargin
					&& go.getY() + go.getHeight() >= this.getY() + this.getHeight()) {

				if (linkedPortal.orientation == this.orientation) {
					if (linkedPortal.isExitSide()) {
						go.setX(linkedPortal.getX());
						go.setY(linkedPortal.getY());
						go.setDirectionSpeed(90, 5);
					} else {
						go.setX(linkedPortal.getX() - go.getWidth());
						go.setY(linkedPortal.getY());
						go.setDirectionSpeed(270, 5);
					}
					((Player) go).setTeleportTimer(50);
				} else {
					if (linkedPortal.isExitSide()) {
						go.setX(linkedPortal.getX());
						go.setY(linkedPortal.getY());
						go.setDirectionSpeed(0, 5);
					} else {
						go.setX(linkedPortal.getX());
						go.setY(linkedPortal.getY() - go.getHeight());
						go.setDirectionSpeed(180, 5);
					}
					((Player) go).setTeleportTimer(10);
				}
			}
		}
	}

	/**
	 * Deletes the screen of this portal
	 */
	public void deleteScreen() {
		world.deleteGameObject(this.screen);
	}

	// Getters & Setters

	/**
	 * @return The portal this portal is linked to.
	 */
	public Portal getLinkedPortal() {
		return linkedPortal;
	}

	/**
	 * Links the portal to a new portal.
	 * 
	 * @param linkedPortal The new portal to link to.
	 */
	public void setLinkedPortal(Portal linkedPortal) {
		this.linkedPortal = linkedPortal;
	}

	public boolean isOrientation() {
		return orientation;
	}

	public boolean isColor() {
		return color;
	}

	public static float getPortalsafetydistance() {
		return portalSafetyDistance;
	}

	public PortalScreen getPortalScreen() {
		return this.screen;
	}

	public boolean isEntryPortal() {
		return entryPortal;
	}

	/**
	 * Gets the mimic that belongs to this portal
	 * 
	 * @return This portals mimic
	 * @deprecated
	 */
	public PlayerMimic getMimic() {
		return mimic;
	}

	public boolean isVisibleSide() {
		return visibleSide;
	}

	public void setVisibleSide(boolean visibleSide) {
		this.visibleSide = visibleSide;
	}

	public boolean isExitSide() {
		return exitSide;
	}

	// Fancy portal code, niet 100% functioneel

	/**
	 * Teleports the player with a fancy way.
	 * 
	 * @param go The player to teleport
	 * @deprecated
	 */
	private void teleportVertDiff(Player go) {
		this.mimic.setVisible(true);

		if (entryPortal && mimic.isVisible()) {
			this.mimic.setX(go.getX() - this.x + linkedPortal.getX());
			this.mimic.setY(linkedPortal.getY());
		}

		if (this.enteredSide == 2 && go.getCenterX() < this.x) {
			go.setX(linkedPortal.getX() - go.getWidth());
			go.setY(linkedPortal.getY());
			((Player) go).setTeleportTimer(10);
			this.enteredSide = 0;
			this.screen.setVisible(false);
			linkedPortal.getPortalScreen().setVisible(false);
			this.mimic.setVisible(false);
			this.entryPortal = false;

		} else if (this.enteredSide == 1 && go.getCenterX() > this.x) {
			go.setX(linkedPortal.getX());
			go.setY(linkedPortal.getY());
			((Player) go).setTeleportTimer(10);
			this.enteredSide = 0;
			this.screen.setVisible(false);
			linkedPortal.getPortalScreen().setVisible(false);
			this.mimic.setVisible(false);
			this.entryPortal = false;

		} else if (go.getCenterX() > this.x && this.enteredSide == 0) {
			// Player entered rechts
			this.mimic.setOriginalPlayer((Player) go);
			linkedPortal.getMimic().setOriginalPlayer((Player) go);
			this.visibleSide = false;
			this.enteredSide = 2;
			this.entryPortal = true;
			((Player) go).setInPortal(true);
			if (entryPortal) {
				this.mimic.setX(go.getX() - this.x + linkedPortal.getX());
				this.mimic.setY(linkedPortal.getY());
				this.mimic.setVisible(true);
			}
			this.screen.setTargetSide(visibleSide);
			this.screen.setVisible(true);
			linkedPortal.getPortalScreen().setVisible(true);
			linkedPortal.getPortalScreen().setTargetSide(!visibleSide);

		} else if (go.getCenterX() < this.x && this.enteredSide == 0) {
			// Player entered links
			this.mimic.setOriginalPlayer((Player) go);
			linkedPortal.getMimic().setOriginalPlayer((Player) go);
			this.visibleSide = true;
			this.enteredSide = 1;
			this.entryPortal = true;
			((Player) go).setInPortal(true);
			if (entryPortal) {
				this.mimic.setX(go.getX() - this.x + linkedPortal.getX());
				this.mimic.setY(linkedPortal.getY());
				this.mimic.setVisible(true);
			}
			this.screen.setTargetSide(visibleSide);
			this.screen.setVisible(true);
			linkedPortal.visibleSide = true;
			linkedPortal.getPortalScreen().setTargetSide(!visibleSide);
			linkedPortal.getPortalScreen().setVisible(true);

		}
	}

	/**
	 * Teleports the player with a fancy way.
	 * 
	 * @param go The player to teleport
	 * @author R3omar
	 * @deprecated
	 */
	private void teleportVertSame(Player go) {
		this.mimic.setVisible(true);

		if (entryPortal && mimic.isVisible()) {
			this.mimic.setX(linkedPortal.getX() - ((go.getX() - this.x) * -1));
			this.mimic.setY(linkedPortal.getY());
		}

		if (this.enteredSide == 2 && go.getCenterX() < this.x) {
			go.setX(linkedPortal.getX());
			go.setY(linkedPortal.getY());
			((Player) go).setTeleportTimer(10);
			this.enteredSide = 0;
			this.screen.setVisible(false);
			linkedPortal.getPortalScreen().setVisible(false);
			this.mimic.setVisible(false);
			this.entryPortal = false;

		} else if (this.enteredSide == 1 && go.getCenterX() > this.x) {
			go.setX(linkedPortal.getX() - go.getWidth());
			go.setY(linkedPortal.getY());
			((Player) go).setTeleportTimer(10);
			this.enteredSide = 0;
			this.screen.setVisible(false);
			linkedPortal.getPortalScreen().setVisible(false);
			this.mimic.setVisible(false);
			this.entryPortal = false;

		} else if (go.getCenterX() > this.x && this.enteredSide == 0) {
			// Player entered rechts
			this.mimic.setOriginalPlayer((Player) go);
			linkedPortal.getMimic().setOriginalPlayer((Player) go);
			this.visibleSide = false;
			this.enteredSide = 2;
			this.entryPortal = true;
			((Player) go).setInPortal(true);
			if (entryPortal) {
				this.mimic.setX(go.getX() - this.x + linkedPortal.getX());
				this.mimic.setY(linkedPortal.getY());
				this.mimic.setVisible(true);
			}

			this.screen.setTargetSide(visibleSide);
			this.screen.setVisible(true);
			linkedPortal.getPortalScreen().setVisible(true);
			linkedPortal.getPortalScreen().setTargetSide(visibleSide);

		} else if (go.getCenterX() < this.x && this.enteredSide == 0) {
			// Player entered links
			this.mimic.setOriginalPlayer((Player) go);
			linkedPortal.getMimic().setOriginalPlayer((Player) go);
			this.visibleSide = true;
			this.enteredSide = 1;
			this.entryPortal = true;
			((Player) go).setInPortal(true);
			if (entryPortal) {
				this.mimic.setX(go.getX() - this.x + linkedPortal.getX());
				this.mimic.setY(linkedPortal.getY());
				this.mimic.setVisible(true);
			}

			this.screen.setTargetSide(visibleSide);
			this.screen.setVisible(true);
			linkedPortal.visibleSide = true;
			linkedPortal.getPortalScreen().setTargetSide(visibleSide);
			linkedPortal.getPortalScreen().setVisible(true);

		}
	}

	/**
	 * Teleports the player with a fancy way.
	 * 
	 * @param go The player to teleport
	 * @author R3omar
	 * @deprecated
	 */
	private void teleportHorzDiff(Player go) {
		this.mimic.setVisible(true);

		if (entryPortal && mimic.isVisible()) {
			this.mimic.setY(go.getX() - this.y + linkedPortal.getY());
			this.mimic.setX(linkedPortal.getX());
		}

		if (this.enteredSide == 4 && go.getCenterY() < this.y) {
			go.setX(linkedPortal.getX());
			go.setY(linkedPortal.getY() - go.getHeight());
			((Player) go).setTeleportTimer(10);
			this.enteredSide = 0;
			this.screen.setVisible(false);
			linkedPortal.getPortalScreen().setVisible(false);
			this.mimic.setVisible(false);
			this.entryPortal = false;

		} else if (this.enteredSide == 3 && go.getCenterY() > this.y) {
			go.setX(linkedPortal.getX());
			go.setY(linkedPortal.getY());
			((Player) go).setTeleportTimer(10);
			this.enteredSide = 0;
			this.screen.setVisible(false);
			linkedPortal.getPortalScreen().setVisible(false);
			this.mimic.setVisible(false);
			this.entryPortal = false;

		} else if (go.getCenterY() > this.y && this.enteredSide == 0) {
			// Player entered boven
			this.mimic.setOriginalPlayer((Player) go);
			linkedPortal.getMimic().setOriginalPlayer((Player) go);
			this.visibleSide = true;
			this.enteredSide = 3;
			this.entryPortal = true;
			this.screen.setTargetSide(true);
			this.screen.setVisible(true);
			linkedPortal.getPortalScreen().setTargetSide(false);
			linkedPortal.getPortalScreen().setVisible(true);
			((Player) go).setInPortal(true);
			if (entryPortal) {
				this.mimic.setY(go.getY() - this.y + linkedPortal.getY() + go.getWidth());
				this.mimic.setX(linkedPortal.getX());
				this.mimic.setVisible(true);
			}

		} else if (go.getCenterX() < this.x && this.enteredSide == 0) {
			// Player entered onder
			this.mimic.setOriginalPlayer((Player) go);
			linkedPortal.getMimic().setOriginalPlayer((Player) go);
			this.visibleSide = false;
			this.enteredSide = 4;
			this.entryPortal = true;
			this.screen.setTargetSide(false);
			this.screen.setVisible(true);
			linkedPortal.getPortalScreen().setTargetSide(true);
			linkedPortal.getPortalScreen().setVisible(true);
			((Player) go).setInPortal(true);
			if (entryPortal) {
				this.mimic.setY(go.getY() - this.y + linkedPortal.getY());
				this.mimic.setX(linkedPortal.getX());
				this.mimic.setVisible(true);
			}
		}
	}

	/**
	 * Teleports the player with a fancy way.
	 * 
	 * @param go The player to teleport
	 * @author R3omar
	 * @deprecated
	 */
	private void teleportHorzSame(Player go) {
		this.mimic.setVisible(true);

		if (entryPortal && mimic.isVisible()) {
			this.mimic.setY(go.getX() - this.y + linkedPortal.getY());
			this.mimic.setX(linkedPortal.getX());
		}

		if (this.enteredSide == 4 && go.getCenterY() < this.y) {
			go.setX(linkedPortal.getX());
			go.setY(linkedPortal.getY() - go.getHeight());
			((Player) go).setTeleportTimer(10);
			this.enteredSide = 0;
			this.screen.setVisible(false);
			linkedPortal.getPortalScreen().setVisible(false);
			this.mimic.setVisible(false);
			this.entryPortal = false;

		} else if (this.enteredSide == 3 && go.getCenterY() > this.y) {
			go.setX(linkedPortal.getX());
			go.setY(linkedPortal.getY());
			((Player) go).setTeleportTimer(10);
			this.enteredSide = 0;
			this.screen.setVisible(false);
			linkedPortal.getPortalScreen().setVisible(false);
			this.mimic.setVisible(false);
			this.entryPortal = false;

		} else if (go.getCenterY() > this.y && this.enteredSide == 0) {
			// Player entered boven
			this.mimic.setOriginalPlayer((Player) go);
			linkedPortal.getMimic().setOriginalPlayer((Player) go);
			this.visibleSide = true;
			this.enteredSide = 3;
			this.entryPortal = true;
			this.screen.setTargetSide(true);
			this.screen.setVisible(true);
			linkedPortal.getPortalScreen().setTargetSide(false);
			linkedPortal.getPortalScreen().setVisible(true);
			((Player) go).setInPortal(true);
			if (entryPortal) {
				this.mimic.setY(go.getY() - this.y + linkedPortal.getY() + go.getWidth());
				this.mimic.setX(linkedPortal.getX());
				this.mimic.setVisible(true);
			}

		} else if (go.getCenterX() < this.x && this.enteredSide == 0) {
			// Player entered onder
			this.mimic.setOriginalPlayer((Player) go);
			linkedPortal.getMimic().setOriginalPlayer((Player) go);
			this.visibleSide = false;
			this.enteredSide = 4;
			this.entryPortal = true;
			this.screen.setTargetSide(false);
			this.screen.setVisible(true);
			linkedPortal.getPortalScreen().setTargetSide(true);
			linkedPortal.getPortalScreen().setVisible(true);
			((Player) go).setInPortal(true);
			if (entryPortal) {
				this.mimic.setY(go.getY() - this.y + linkedPortal.getY());
				this.mimic.setX(linkedPortal.getX());
				this.mimic.setVisible(true);
			}
		}
	}

}
