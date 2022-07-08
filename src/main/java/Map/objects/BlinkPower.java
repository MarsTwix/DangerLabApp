package Map.objects;

import java.util.List;

import Player.Player;
import dangerlabapp.DangerLabApp;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;

public class BlinkPower extends PowerUpp{

	public BlinkPower(DangerLabApp world) {
		super(world, new Sprite(DangerLabApp.MEDIA_URL.concat("teleportPower.png")));
	}
	
	@Override
	public void gameObjectCollisionOccurred(List<GameObject> collidedGameObjects) {
		for (GameObject go : collidedGameObjects) {
			if (go instanceof Player) {
				((Player) go).setMayBlink(true);
				world.deleteGameObject(this);
			}
		}
	}
}
