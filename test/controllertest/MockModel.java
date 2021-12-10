package controllertest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeon.Dungeon;
import location.Direction;
import location.Location;
import location.Smell;
import player.Player;
import randomiser.Randomiser;

public class MockModel implements Dungeon {

  StringBuilder log;

  public MockModel(StringBuilder log) {
    this.log = log;
  }

  @Override
  public ArrayList<Location> endUsingBfs(Location src) {
    log.append("Calling the bfs function").append("\n");
    return null;
  }

  @Override
  public String playerToPickTreasure() {
    log.append("Player picking treasures").append("\n");
    return null;
  }

  @Override
  public boolean isTreasure() {
    log.append("Checking if there is treasure").append("\n");
    return false;
  }

  @Override
  public String maze(String move) throws IllegalArgumentException {
    log.append("Moving the player").append("\n");
    return null;
  }

  @Override
  public String getDungeon() {
    log.append("Showing dungeon").append("\n");
    return null;
  }

  @Override
  public String playerToPickArrow() {
    log.append("Player picking arrows").append("\n");
    return null;
  }

  @Override
  public String shootArrow(String distance, Direction direction) throws IllegalArgumentException {
    log.append("Player shooting arrow").append("\n");
    return null;
  }

  @Override
  public String treasureDesc() {
    log.append("Getting treasure description").append("\n");
    return null;
  }

  @Override
  public int getRow() {
    log.append("Getting rows").append("\n");
    return 0;
  }

  @Override
  public int getColumns() {
    log.append("Getting columns").append("\n");
    return 0;
  }

  @Override
  public int getInterconnectivity() {
    log.append("Getting interconnectivity").append("\n");
    return 0;
  }

  @Override
  public boolean getWrapping() {
    log.append("Getting wrapping").append("\n");
    return false;
  }

  @Override
  public int getNumberOfMonsters() {
    log.append("Getting number of monsters").append("\n");
    return 0;
  }

  @Override
  public float getTreasurePercent() {
    log.append("Getting treasure percentage").append("\n");
    return 0;
  }

  @Override
  public Player getPlayer() {
    log.append("Getting player details").append("\n");
    return null;
  }

  @Override
  public List<Location> getListOfCaves() {
    log.append("Getting details of every location").append("\n");
    return null;
  }

  @Override
  public Smell getSmell() {
    log.append("Checking smell").append("\n");
    return null;
  }

  @Override
  public boolean getPit(Location cave) {
    log.append("Checking if there is a pit nearby").append("\n");
    return false;
  }

  @Override
  public Location getStart() {
    log.append("Getting start").append("\n");
    return null;
  }

  @Override
  public boolean hasReached() {
    log.append("Checking if player has reached the end").append("\n");
    return false;
  }

  @Override
  public boolean hasFallenIntoPit() {
    log.append("Checking if player has fallen into pit").append("\n");
    return false;
  }

  @Override
  public boolean getSoil() {
    log.append("Getting soil").append("\n");
    return false;
  }

  @Override
  public String getPlayerDesc() {
    log.append("Getting player description").append("\n");
    return null;
  }

  @Override
  public String getLocationDesc() {
    log.append("Getting location description").append("\n");
    return null;
  }

  @Override
  public Randomiser getRandomiser() {
    log.append("Getting randomiser").append("\n");
    return null;
  }

  @Override
  public int getFinalPercent() {
    log.append("Getting percentage").append("\n");
    return 0;
  }

  @Override
  public ArrayList<Location> getCaveFinalList() {
    log.append("Getting cave list copy").append("\n");
    return null;
  }

  @Override
  public Location getEnd() {
    log.append("Getting end").append("\n");
    return null;
  }
}
