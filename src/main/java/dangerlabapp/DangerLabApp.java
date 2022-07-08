package dangerlabapp;

import Map.Portals.*;
import Map.objects.Ball;
import Map.objects.BlinkPower;
import Map.objects.DeathBall;
import Map.objects.FlightPower;
import Map.objects.PowerUpp;
import Map.objects.ResetBall;
import Map.tiles.FloorTile;
import Map.tiles.Glass;
import Map.tiles.KillTile;
import Map.tiles.NormalWall;
import Map.tiles.ExitTile;
import Player.Player;
import nl.han.ica.oopg.dashboard.Dashboard;
import nl.han.ica.oopg.engine.GameEngine;
import nl.han.ica.oopg.objects.Sprite;
import nl.han.ica.oopg.objects.TextObject;
import nl.han.ica.oopg.tile.Tile;
import nl.han.ica.oopg.tile.TileMap;
import nl.han.ica.oopg.tile.TileType;
import nl.han.ica.oopg.view.View;
import processing.core.PVector;

@SuppressWarnings("serial")
public class DangerLabApp extends GameEngine {

	private Player player;

	private TextObject dashboardPortalInfo;

	private Ball[] gameBalls = new Ball[6];
	private PowerUpp[] powerUps = new PowerUpp[2];

	private PortalAim portalAim;
	private PortalBullet portalBullet;
	
	final int TOTALLEVELS = 2;
	
	private Portal portalRed;
	private Portal portalBlue;
	public static String MEDIA_URL = "src/main/java/media/";
	private int level;
	private PVector spawnPos;
	public static final int BACKGROUND_R = 50;
	public static final int BACKGROUND_G = 50;
	public static final int BACKGROUND_B = 50;

	@Override
	public void setupGame() {
		int worldWidth = 1120;
		int worldHeight = 760;
		
		level = 0;

		// Dashboard
		createDashboard();

		// Tile map
		initializeTileMap();

		// Portals startup
		initializePortals();

		//Adds the powerups to the world
		addPowerUps();
		
		// Adds special balls
		addBalls();


		View view = new View(worldWidth, worldHeight);
		// view.setBackground(loadImage(DangerLabApp.MEDIA_URL.concat("test_Background.png")));
		setView(view);
		view.setBackground(BACKGROUND_R, BACKGROUND_G, BACKGROUND_B);
		size(worldWidth, worldHeight);

		// Player
		spawnPlayer();

		// Balls
		// addBalls();

	}

	@Override
	public void update() {
		// Dit doet nog niets

	}

	public static void main(String[] args) {
		DangerLabApp hw = new DangerLabApp();

		// deze methode start de game.
		hw.runSketch();
	}

	/**
	 * Makes the tileMap, stores currentMap in a bitMap.
	 */
	private void initializeTileMap() {
		// Load Sprites
		Sprite floorSprite = new Sprite(DangerLabApp.MEDIA_URL.concat("portalFloor.png"));
		Sprite spawnSprite = new Sprite(DangerLabApp.MEDIA_URL.concat("SpawnBlock.png"));
		Sprite exitSprite = new Sprite(DangerLabApp.MEDIA_URL.concat("ExitBlock.png"));
		Sprite wallSprite = new Sprite(DangerLabApp.MEDIA_URL.concat("normalWall.png"));
		Sprite lavaSprite = new Sprite(DangerLabApp.MEDIA_URL.concat("LavaBlock.png"));
		Sprite glassSprite = new Sprite(DangerLabApp.MEDIA_URL.concat("glassBlockB.png"));

		// Create tile types with the right Tile class and sprite
		TileType<FloorTile> floorTileType = new TileType<>(FloorTile.class, floorSprite);
		TileType<FloorTile> spawnTileType = new TileType<>(FloorTile.class, spawnSprite);
		TileType<ExitTile> exitTileType = new TileType<>(ExitTile.class, exitSprite);
		TileType<NormalWall> NormalWall = new TileType<>(NormalWall.class, wallSprite);
		TileType<KillTile> lavaTileType = new TileType<>(KillTile.class, lavaSprite);
		TileType<Glass> glassTileType = new TileType<>(Glass.class, glassSprite);

		TileType[] tileTypes = { floorTileType, spawnTileType, exitTileType, NormalWall, lavaTileType, glassTileType };
		int tileSize = 40;
		// TileMap length is fixed for all, current: 14		
		int levels[][][] = {
				{
					{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1, -1 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, -1, -1, -1, -1, 4, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1,-1, 0 },
					{ 0, -1, -1, -1, -1, 4, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1,-1, 0 },
					{ 0, -1, -1, 0, 5, 4, 5, 0, 5, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1,-1, 0 },
					{ 0, -1, -1, 0, 5, 4, 5, 4, 5, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1,-1, 0 },
					{ 0, -1, -1, 0, 5, 0, 5, 4, 5, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1,-1, 0 },
					{ 0, -1, -1, 0, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, 5, 0, 0, -1, -1, -1,0 },
					{ 0, -1, -1, 0, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 4, 5, 4, 0, -1, -1, -1,0 },
					{ 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 0, 4, 5, 4, 0, -1, -1, -1, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, -1, -1, 0, 4, 5, 4, 0, -1, -1, -1, 0 },
					{ 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, -1, -1, 0, 4, 5, 4, 0, -1, -1, -1,0 },
					{ 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, 0, -1, -1,-1, 0 },
					{ 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, 0, -1, -1,-1, 0 },
					{ 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, 0, -1, -1,-1, 0 },
					{ 0, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, 0 },
					{ 0, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 },
					{ 0, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0 },
					{ 0, 0, 1, 0, 0, 0, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0 }
			},
			{
				{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,-1, -1, -1 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, 0, 0, 0, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, 0, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, 0, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, 0, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, 0, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, 0, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, 0, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, 0, -1, -1, -1, -1, -1, -1, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, 0, -1, -1, -1, -1, -1, -1, 0, -1, -1, 0, 4, 0, 4, 0, 4, 0, 4, 0, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, 0, 0, 0, 0, 0, -1, -1, 0, -1, -1, 0, 4, 0, 4, 0, 4, 0, 4, 0, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, -1, -1, -1, 0, -1, -1, -1, 0, -1, -1, 0, 4, 0, 4, 0, 4, 0, 4, 0, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, -1, -1, -1, 0, -1, -1, -1, 0, -1, -1, 0, 4, 0, 4, 0, 4, 0, 4, 0, -1, -1, -1, -1, -1,0},
				{ 0, -1, -1, -1, -1, -1, 0, -1, -1, -1, 0, -1, -1, 0, 4, 0, 4, 0, 4, 0, 4, 0, -1, -1, -1, -1, -1,0},
				{ 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
			},
		};
		
		
		tileMap = new TileMap(tileSize, tileTypes, levels[level]);
		
	}

	private void initializePortalAim() {
		portalAim = new PortalAim(player);
		addGameObject(portalAim);
	}

	/**
	 * Gets the world spawn location
	 * @return The PVector which contains the spawn X & Y
	 * @author marnix
	 */
	public void getSpawn() {
		int[][] map = tileMap.getTileMap();
		for (int y = 0; y < 19; y++) {
			for (int x = 0; x < 28; x++) {
				if (map[y][x] == 1) {
					Tile spawnTile = tileMap.getTileOnIndex(x, y);
					spawnPos = tileMap.getTilePixelLocation(spawnTile);
				}
			}
		}
	}
	
	/**
	 * Spawns a new player
	 * @author Marnix
	 */
	public void spawnPlayer() {
		// Player
		player = new Player(this);
		getSpawn();
		PVector spawnPoint = spawnPos;
		addGameObject(player, spawnPoint.x, spawnPoint.y);

		// init portalgun
		initializePortalAim();
		player.setPortalAim(portalAim);
	}

	/**
	 * Adds the moving balls to the game
	 * @see DeathBall.java
	 * @see ResetBall.java
	 */
	private void addBalls() {
		if(level == 0) {
			for(Ball temp : gameBalls) {
				deleteGameObject(temp);
			}
			
			gameBalls[0] = new ResetBall(this, true, 3);
			addGameObject(gameBalls[0], 850, 80);
			gameBalls[1] = new DeathBall(this, true, 3);
			addGameObject(gameBalls[1], 350, 550);
			gameBalls[2] = new DeathBall(this, true, 3);
			addGameObject(gameBalls[2], 580, 100);
			gameBalls[3] = new DeathBall(this, true, 3);
			addGameObject(gameBalls[3], 380, 200);
			gameBalls[4] = new DeathBall(this, false, 1);
			addGameObject(gameBalls[4], 288, 300);
			gameBalls[5] = new ResetBall(this, true, 3);
			addGameObject(gameBalls[5], 715, 80);
		}
		else if(level == 1){
			for(Ball temp : gameBalls) {
				deleteGameObject(temp);
			}
		}
	}
	
	private void addPowerUps() {
		if(level == 0) {
			for(PowerUpp temp : powerUps) {
				deleteGameObject(temp);
			}
		}
		else if(level == 1){
			for(PowerUpp temp : powerUps) {
				deleteGameObject(temp);
			}
			powerUps[0] = new FlightPower(this);
			addGameObject(powerUps[0], 170, 670);
			powerUps[1] = new BlinkPower(this);
			addGameObject(powerUps[1], 300, 200);
		}
	}

	/**
	 * Adds the dashboard to the game
	 */
	private void createDashboard() {
		Dashboard dashboard = new Dashboard(0, 0, 250, 100);
		dashboardPortalInfo = new TextObject("", 25);
		dashboardPortalInfo.setForeColor(255, 255, 255, 255);
		dashboard.addGameObject(dashboardPortalInfo, 0, 0);
		addDashboard(dashboard);
	}

	/**
	 * Starts the portals
	 */
	private void initializePortals() {
		portalBlue = new Portal(this, 500f, 300f, false, false, false);
		portalRed = new Portal(this, 700f, 300f, portalBlue, true, false, false);
		portalRed.setVisible(false);
		portalBlue.setVisible(false);
		portalBlue.setLinkedPortal(portalRed);
		portalRed.setLinkedPortal(portalBlue);

	}

	// Blue == true, Red == false
	// Horizontal == true, Vertical == false

	/**
	 * Places a new portal in the world
	 * @param x The portals X coordinate
	 * @param y The portals Y coordinate
	 * @param color The color of the portal
	 * @param orientation The orientation of the portal, false for vertical & true for horizontal
	 * @param exitSide The side at which a player would exit the portal
	 * @author R3omar
	 */
	public void placePortal(float x, float y, boolean color, boolean orientation, boolean exitSide) {
		if (color) {
			if (orientation) {
				if ((portalBlue.getX() + Portal.getPortalsafetydistance() <= x
						|| portalBlue.getX() - Portal.getPortalsafetydistance() >= x)
						|| (portalBlue.getY() - portalBlue.getHeight() >= y
								|| portalBlue.getY() + portalBlue.getHeight() <= y)) {
					portalRed.deleteScreen();
					deleteGameObject(portalRed);
					portalRed = new Portal(this, x, y, portalBlue, color, orientation, exitSide);
					portalRed.setLinkedPortal(portalBlue);
					portalBlue.setLinkedPortal(portalRed);
					addGameObject(portalRed);
					System.out.println("New Horizontal Red portal on: " + x + ", " + y);
				}
			} else {
				if ((portalBlue.getX() + Portal.getPortalsafetydistance() <= x
						|| portalBlue.getX() - Portal.getPortalsafetydistance() >= x)
						|| (portalBlue.getY() - portalBlue.getHeight() >= y
								|| portalBlue.getY() + portalBlue.getHeight() <= y)) {
					portalRed.deleteScreen();
					deleteGameObject(portalRed);
					portalRed = new Portal(this, x, y, portalBlue, color, orientation, exitSide);
					portalRed.setLinkedPortal(portalBlue);
					portalBlue.setLinkedPortal(portalRed);
					addGameObject(portalRed);
					System.out.println("New Vertical Red portal on: " + x + ", " + y);
				}
			}
		} else {
			if (orientation) {
				if ((portalRed.getX() + Portal.getPortalsafetydistance() <= x
						|| portalRed.getX() - Portal.getPortalsafetydistance() >= x)
						|| (portalRed.getY() - portalRed.getHeight() >= y
								|| portalRed.getY() + portalRed.getHeight() <= y)) {
					portalBlue.deleteScreen();
					deleteGameObject(portalBlue);
					portalBlue = new Portal(this, x, y, portalRed, color, orientation, exitSide);
					portalBlue.setLinkedPortal(portalRed);
					portalRed.setLinkedPortal(portalBlue);
					addGameObject(portalBlue);
					System.out.println("New Horizontal Blue portal on: " + x + ", " + y);
				}
			} else {
				if ((portalRed.getX() + Portal.getPortalsafetydistance() <= x
						|| portalRed.getX() - Portal.getPortalsafetydistance() >= x)
						|| (portalRed.getY() - portalRed.getHeight() >= y
								|| portalRed.getY() + portalRed.getHeight() <= y)) {
					portalBlue.deleteScreen();
					deleteGameObject(portalBlue);
					portalBlue = new Portal(this, x, y, portalRed, color, orientation, exitSide);
					portalBlue.setLinkedPortal(portalRed);
					portalRed.setLinkedPortal(portalBlue);
					addGameObject(portalBlue);
					System.out.println("New Vertical Blue portal on: " + x + ", " + y);
				}
			}
		}
	}

	public void schootPortal(boolean color) {
		if (!player.isShooting()) {
			portalBullet = new PortalBullet(this, player, color);
			addGameObject(portalBullet, player.getCenterX() - 10, player.getCenterY() - 3);
			portalBullet.shoot();
		}
	}

	public double getPortalAimAngle() {
		return portalAim.getAngle();
	}

	/**
	 * Refreshes the dashboard.
	 * @author Marnix en R3omar
	 */
	public void refreshDasboardText() {
		if (player.getPortalColor()) {
			dashboardPortalInfo.setText("Selected Portal: BLUE");
		} else {
			dashboardPortalInfo.setText("Selected Portal: RED");
		}
	}
/**
 * Deletes all the portals in the world
 * @author R3omar
 */
	public void deleteAllPortals() {
		portalBlue.removePortal();
		portalRed.removePortal();
	}

	public Player getPlayer() {
		return player;
	}

	public PortalBullet getPortalBullet() {
		return portalBullet;
	}

	/**
	 * Updates the current map to match the level.
	 * @author Marnix
	 */
	public void levelUp() {
		if(level < TOTALLEVELS - 1) {
			level++;
		} else {
			level = 0;
		}

		initializeTileMap();
		addBalls();
		addPowerUps();
		spawnPlayer();
	}
	
	public PVector getSpawnPos() {
		return spawnPos;
	}
}
