package Map.Portals;

import Player.Player;
import dangerlabapp.DangerLabApp;
import nl.han.ica.oopg.objects.GameObject;
import processing.core.PGraphics;

public class PortalScreen extends GameObject {

	private Portal ownerPortal;
	private boolean placedSide;

	private boolean overWall;

	// Side false == left || above, true == right || below
	// orientation false = vertical true == horizontal
	
	/**
	 * Generates a new portalScreen, hides object if it is behind it
	 * @param ownerPortal The portal that owes the screen
	 * @param side The side of the portal at which the screen would be placed
	 */
	public PortalScreen(Portal ownerPortal, boolean side) {
		super(getTargetX(ownerPortal, side), getTargetY(ownerPortal, side), getTargetWidth(ownerPortal, side),
				getTargetHeight(ownerPortal, side));
		this.ownerPortal = ownerPortal;
		this.placedSide = side;
		this.overWall = false;
	}

	@Override
	public void update() {
		this.x = getTargetX(ownerPortal, placedSide);
		this.y = getTargetY(ownerPortal, placedSide);
	}

	@Override
	public void draw(PGraphics g) {
		if (overWall) {
			g.stroke(255, 255, 255);
			g.fill(33, 33, 33);
		} else {
			g.noStroke();
			g.fill(DangerLabApp.BACKGROUND_R, DangerLabApp.BACKGROUND_G, DangerLabApp.BACKGROUND_B);
		}
		g.rect(x, y, width, height);
	}

//Getters & Setters

	private static float getTargetX(Portal target, boolean side) {
		if (!side && !target.isOrientation()) {
			return (target.getX() - Player.getCharacterWidth());
		} else if (!target.isOrientation() && side) {
			return (target.getX() + 2);
		} else {
			return (target.getX());
		}
	}

	private static float getTargetY(Portal target, boolean side) {
		if (side && target.isOrientation()) {
			return (target.getY() + target.getHeight());
		} else if (!side && target.isOrientation()) {
			return (target.getY() - target.getHeight() - Player.getCharacterHeight());
		} else {
			return (target.getY());
		}
	}

	private static float getTargetWidth(Portal target, boolean side) {
		if (!target.isOrientation()) {
			return (Player.getCharacterWidth());
		} else {
			return (Player.getCharacterHeight());
		}
	}

	private static float getTargetHeight(Portal target, boolean side) {
		if (!target.isOrientation()) {
			return (Player.getCharacterHeight());
		} else {
			return (Player.getCharacterWidth());
		}
	}

	/**
	 * Sets the side of the portal the screen should be placed on
	 * @param newSide The new side of the screen
	 */
	public void setTargetSide(boolean newSide) {
		this.placedSide = newSide;
	}

}
