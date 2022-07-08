package Map.Portals;

public enum PortalTypes {

	REDVERTICAL("portalRedVert.png"), REDHORIZONTAL("portalRedHorz.png"), BLUEVERTICAL("portalBlueVert.png"), BLUEHORIZONTAL("portalBlueHorz.png");
	
	String location;
	
	PortalTypes(String location) {
		this.location = location;
	}
	
	public static String getPortalAddress(boolean color, boolean orientation) {
		if(color) {
			if(orientation) {
				return(BLUEHORIZONTAL.location);
			} else {
				return(BLUEVERTICAL.location);

			}
		}
		if(orientation) {
			return(REDHORIZONTAL.location);
		} else {
			return(REDVERTICAL.location);

		}
	}
	
}
