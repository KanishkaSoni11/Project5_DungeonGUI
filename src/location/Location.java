package location;

import java.util.List;
import java.util.Map;

/**
 * Location is the representation of each node of the dungeon from where player can move
 * classified as cave and tunnel.
 */
public interface Location {

  /**
   * Method to get the cave id of each location.
   *
   * @return cave id
   */
  int getCaveId();

  /**
   * Method to add the neighbours of a particular cave along with the direction.
   *
   * @param directions direction in which the cave is present with respect to the current cave
   * @param cave       cave to be added as the neighbour
   * @throws IllegalArgumentException if the direction is null
   * @throws IllegalArgumentException if the cave is null
   */
  void addCaveList(Direction directions, Location cave) throws IllegalArgumentException;

  /**
   * Method to get the neighbour list of a particular cave along with the direction.
   *
   * @return neighbour cave list
   */
  Map<Direction, Location> getCaveList();

  /**
   * Method to add treasure to the cave.
   *
   * @param treasure treasure to be added
   * @throws IllegalArgumentException if the treasure is null
   */
  void addTreasureToCave(Treasure treasure) throws IllegalArgumentException;

  /**
   * Method to assign and get the location type as cave or tunnel of the location.
   *
   * @return location type of the location
   */
  LocationType getLocationType();

  /**
   * Method to get the list of treasures of the cave.
   *
   * @return list of treasures
   */
  List<Treasure> getTreasureList();

  /**
   * Method to update the visit status of the cave while finding the route.
   *
   * @param visit visit status to be updated
   */
  void updateVisit(boolean visit);

  /**
   * Returns if the location is visited or not.
   *
   * @return true or false based on the visit status
   */
  boolean isVisited();

  /**
   * Method to remove the treasures of a particular cave.
   */
  void removeTreasure();

  /**
   * Method to add the arrows in the cave.
   *
   * @param number the number of arrows to be added in the cave.
   */
  void addArrow(int number);

  /**
   * Method to get the number of arrows in a particular cave.
   *
   * @return number of arrows
   */
  int getArrow();

  /**
   * Method to remove the arrows from a particular cave when the player picked them up.
   */
  void removeArrow();

  /**
   * Method to add the monster to a particular cave.
   */
  void addMonster();

  /**
   * Method to check whether the cave has the monster or not.
   *
   * @return true or false depending if the monster is present or not.
   */
  boolean hasMonster();

  /**
   * Method to get the monster object in the cave.
   *
   * @return monster in the cave
   */
  CaveCreature getMonster();

  /**
   * Method to reduce the health of the monster when the monster is shot by the player.
   */
  void hitMonster();

  void setPit();

  boolean getPit();

  void addThief();

  boolean getThief();

  void addMovingMonster();

  boolean hasMovingMonster();

  void removeMovingMonster();

  CaveCreature getMovingMonster();

  void hitMovingMonster();



}
