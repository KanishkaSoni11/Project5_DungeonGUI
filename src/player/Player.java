package player;

import java.util.List;

import location.Direction;
import location.Location;
import location.Treasure;

/**
 * Player is the representation of the player which moves across the dungeon.
 */

public interface Player {
  /**
   * Method to get the name of the player.
   *
   * @return name of the player
   */
  String getName();

  /**
   * Method to get the current location of the player based on the moves.
   *
   * @return location of the player
   */
  Location getCurrentCave();

  /**
   * Method to get the list of treasures that the player has picked from caves.
   *
   * @return list of treasures with the player
   */
  List<Treasure> getTreasureList();

  /**
   * Method for the player to pick the treasure from the cave.
   *
   * @param caveTreasureList treasure list present in the cave
   */
  void pickTreasure(List<Treasure> caveTreasureList);

  /**
   * Method to move the player from current to next cave based on the direction passed.
   *
   * @param directions direction in which the player has to be moved
   * @return if the player moved or not
   */
  boolean move(Direction directions);

  /**
   * Method to remove the treasure from the cave after the player has picked the treasure from
   * that particular cave.
   *
   * @param cave cave from which the treasure has to be removed.
   */
  void removeTreasure(Location cave);

  /**
   * Method to get the number of arrows with the player.
   *
   * @return number of arrows with the player
   */
  int getArrowCount();

  /**
   * Method to ask the player to pick up the arrows in the cave and increase the count of arrows
   * with the player.
   */
  void pickArrow();

  /**
   * Method for the player to shoot the arrow and decrease the arrow count with player.
   */
  void shootArrow();

  /**
   * Method to maintain the health status of the player and to kill the player if he has been eaten
   * by the monster.
   */
  void killPlayer();

  /**
   * Method to get the health status of the player.
   *
   * @return true or false depending if the player is alive or dead.
   */
  boolean getPlayerLive();

  void removeTreausuresWithPlayer();

  int getHealth();

  void injure();

  Location getStartCave();

  void resetPlayer();
}
