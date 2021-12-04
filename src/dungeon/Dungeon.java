package dungeon;

import java.util.List;
import java.util.Map;

import location.Direction;
import location.Location;
import location.Smell;
import player.Player;

/**
 * Dungeon is the representation of Dungeon maze model where the dungeon is formed based on
 * some constraints, the treasures are assigned to the caves and the player is then asked to move
 * from start to end point.
 */

public interface Dungeon extends ReadOnlyDungeonModel{



  /**
   * Method to find the end of the dungeon maze to get the distance between the start and end
   * points as atleast five.
   *
   * @param src the starting point
   * @return a list of all the cave locations the BFS has visited
   */
  Map<Location, Integer> endUsingBfs(Location src);

  /**
   * Method to ask the player to pick the treasure from a particular cave that is the
   * current cave of the player.
   *
   * @return String representation of treasures
   */
  String playerToPickTreasure();



  /**
   * Checks if the treasure is present in the particular cave of the dungeon.
   *
   * @return true or false based on the cave's treasure list.
   */
  boolean isTreasure();

  /**
   * Method to move the player in the direction that is specified as the input.
   *
   * @param move Input direction that the user has given for the player to move in the maze
   * @return String representation of whether the player was able to move or not
   * @throws IllegalArgumentException if the move is null
   */
 String maze(String move) throws IllegalArgumentException;
//
  /**
   * Method to dump the dungeon in the form of caves and tunnels and providing the directions in
   * which each cave and tunnel is connected.
   *
   * @return String representation of the dumped dungeon
   */
  String getDungeon();

  /**
   * Method to get the end cave of the maze.
   *
   * @return end cave
   */
  Location getEnd();





  /**
   * Method to ask the player to pick up the arrows from a particular cave that is the
   * current cave of the player.
   *
   * @return String representation of the arrow
   */

  String playerToPickArrow();


  /**
   * Method that allows the player to shoot the monster depending on the clues of smell given to
   * the player.
   *
   * @param distance  distance at which the arrow has to be shot.
   * @param direction direction in which the arrow has to be shot.
   * @return String representation of the shooting of monster by player
   * @throws IllegalArgumentException if the distance or direction is null
   */
  String shootArrow(int distance, Direction direction) throws IllegalArgumentException;


  /**
   * Method to get the description of the treasure in a particular cave.
   *
   * @return String representation of treasure in the cave
   */
  String treasureDesc();



  void putPit();








}
