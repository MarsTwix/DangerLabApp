package Map.Portals;

import java.util.List;

import Player.Player;
import java.math.*;
import dangerlabapp.DangerLabApp;
import nl.han.ica.oopg.collision.ICollidableWithGameObjects;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.objects.SpriteObject;


public class PortalAim extends SpriteObject{
	
	private static String spriteImage = "portalAim.png";
	Player player;
	private double angle;
	private double xpos;
	private double ypos;
	
	/**
	 * 
	 * @param player
	 * @author marnix
	 */
	public PortalAim(Player player) {
		super(new Sprite(DangerLabApp.MEDIA_URL.concat(spriteImage)));
		this.player = player;
		super.setX(player.getCenterX());
		super.setY(player.getCenterY());
	}

	@Override
	public void update() {
		xpos=50*Math.cos(Math.toRadians(angle+270)) + (player.getCenterX()-super.getWidth()/2);
		ypos=50*Math.sin(Math.toRadians(angle+270)) + (player.getCenterY()-super.getHeight()/2-player.getySpeed());
		super.setX((float)xpos);
		super.setY((float)ypos);
		
	}
	@Override
	public void mouseMoved(int x, int y){
		angle = player.getAngleFrom(x, y);
	}
	
	public double getAngle() {
		return angle;
	}
}
