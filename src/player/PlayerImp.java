package player;

import java.util.ArrayList;
import java.util.List;

import location.Cave;
import location.Direction;
import location.Location;
import location.Treasure;

/**
 * PlayerImp is the implementation of the player which forms the player and then updates the
 * location and treasures with the player.
 */
public class PlayerImp implements Player {
  private String name;
  private Location cave;
  private List<Treasure> playerTreasureList;
  private int arrowsWithPlayers;
  private boolean playerAlive;
  private int health;

  /**
   * Constructs the player based on the name and the location from which the player has to start.
   *
   * @param name name of the player
   * @param cave start location of the player
   * @throws IllegalArgumentException if the name is null
   * @throws IllegalArgumentException if the start location is null
   */
  public PlayerImp(String name, Location cave) throws IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null");
    }
    if (cave == null) {
      throw new IllegalArgumentException("Cave cannot be null");
    }
    this.name = name;
    this.cave = cave;
    this.playerTreasureList = new ArrayList<Treasure>();
    this.arrowsWithPlayers = 3;
    this.playerAlive = true;
    this.health = 100;
  }

  /**
   * Copy constructor of the Player class to maintain a defensive copy.
   *
   * @param player Player object for which the copy is constructed.
   * @throws IllegalArgumentException if the player is null
   */
  public PlayerImp(Player player) throws IllegalArgumentException {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null.");
    }
    this.name = player.getName();
    playerTreasureList = player.getTreasureList();
    this.cave = player.getCurrentCave();
    this.playerAlive = player.getPlayerLive();
    this.arrowsWithPlayers = player.getArrowCount();
    this.health = player.getHealth();
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Location getCurrentCave() {
    return new Cave(cave);
  }

  @Override
  public List<Treasure> getTreasureList() {
    return new ArrayList<>(this.playerTreasureList);
  }

  @Override
  public void pickTreasure(List<Treasure> caveTreasureList) throws IllegalArgumentException {
    if (caveTreasureList == null) {
      throw new IllegalArgumentException("Treasure list cannot be null");
    }
    playerTreasureList.addAll(this.getCurrentCave().getTreasureList());

  }

  @Override
  public boolean move(Direction directions) throws IllegalArgumentException {
    if (directions == null) {
      throw new IllegalArgumentException("Direction cannot be null");
    }
    if (getCurrentCave().getCaveList().containsKey(directions)) {
      cave = getCurrentCave().getCaveList().get(directions);
      return true;
    }
    return false;
  }

  @Override
  public void removeTreasure(Location cave) throws IllegalArgumentException {
    if (cave == null) {
      throw new IllegalArgumentException("Cave cannot be null.");
    }
    this.cave.removeTreasure();
  }

  @Override
  public int getArrowCount() {
    return this.arrowsWithPlayers;
  }

  @Override
  public void pickArrow() {
    this.arrowsWithPlayers = this.arrowsWithPlayers + this.getCurrentCave().getArrow();
    this.cave.removeArrow();
  }

  @Override
  public void shootArrow() {
    this.arrowsWithPlayers--;
  }

  @Override
  public void killPlayer() {
    this.playerAlive = false;
  }

  @Override
  public boolean getPlayerLive() {
    return this.playerAlive;
  }

  @Override
  public void removeTreausuresWithPlayer() {
    this.playerTreasureList.clear();
  }

  @Override
  public int getHealth() {
    return this.health;
  }

  @Override
  public void injure() {
    if (this.health > 0) {
      health = health - 50;
    }
  }


}
