package Map.objects;

import java.util.List;

import dangerlabapp.DangerLabApp;
import nl.han.ica.oopg.collision.ICollidableWithGameObjects;
import nl.han.ica.oopg.objects.GameObject;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.objects.SpriteObject;

public class PowerUpp extends SpriteObject implements ICollidableWithGameObjects {

	protected DangerLabApp world;
	
	public PowerUpp(DangerLabApp world, Sprite sprite) {
		super(sprite);
		this.world = world;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void gameObjectCollisionOccurred(List<GameObject> collidedGameObjects) {
		// TODO Auto-generated method stub
		
	}

}
