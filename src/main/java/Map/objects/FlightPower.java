package Map.objects;

import java.util.List;

import Player.Player;
import dangerlabapp.DangerLabApp;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;

public class FlightPower extends PowerUpp {

	public FlightPower(DangerLabApp world) {
		super(world, new Sprite(DangerLabApp.MEDIA_URL.concat("flightPower.png")));
	}
	
	@Override
	public void gameObjectCollisionOccurred(List<GameObject> collidedGameObjects) {
		for (GameObject go : collidedGameObjects) {
			if (go instanceof Player) {
				((Player) go).setMayFly(true);
				world.deleteGameObject(this);
			}
		}
	}

}
