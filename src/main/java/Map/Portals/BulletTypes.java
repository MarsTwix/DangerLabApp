package Map.Portals;

public enum BulletTypes {
	REDBULLET("RedBulletPortal.png"),BLUEBULLET("BlueBulletPortal.png");
	
	String location;
	
	BulletTypes(String location) {
		this.location = location;
	}
	/**
	 * 
	 * @param color
	 * @return de kleur blauw of rood met hun png naam
	 * @author
	 */
	public static String getBulletAddress(boolean color) {
		if(color) {
			return BLUEBULLET.location;
		}
		else {
			return REDBULLET.location;
		}
	}
}
