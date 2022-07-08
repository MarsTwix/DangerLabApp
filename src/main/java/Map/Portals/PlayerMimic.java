package Map.Portals;

import Player.Player;
import dangerlabapp.DangerLabApp;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.objects.SpriteObject;

public class PlayerMimic extends SpriteObject {

	private Player originalPlayer;

	/**
	 * Generates a new PlayerMimic
	 * @param original The original player the mimic will mimic
	 * @param x The starting X-coordinate of the mimic
	 * @param y The starting Y-coordinate of the mimic
	 */
	public PlayerMimic(Player original, float x, float y) {
		super(new Sprite(DangerLabApp.MEDIA_URL.concat(Player.getSpriteLocation())));
		this.originalPlayer = original;
		super.x = x;
		super.y = y;
	}

	@Override
	public void update() {
		if (originalPlayer != null) {
			if (originalPlayer.isInPortal()) {
				this.setVisible(true);

			} else {
				this.setVisible(false);
			}
		}
	}

	// Getters & Setters
	public Player getOriginalPlayer() {
		return originalPlayer;
	}

	public void setOriginalPlayer(Player originalPlayer) {
		this.originalPlayer = originalPlayer;
	}

}
