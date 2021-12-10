package control;

import dungeon.Dungeon;
import location.Direction;

/**
 * IDungeonController represents the controller for the model which is being called to run all
 * the commands.
 */
public interface IDungeonController {

  /**
   * Method to start the game by the player in the dungeon.
   *
   * @param dungeon object of the dungeon for which the game has to be played.
   * @throws IllegalArgumentException if the dungeon is null
   */
  void play(Dungeon dungeon) throws IllegalArgumentException;

  /**
   * Method to start the game using the GUI.
   */
  void playView();

  /**
   * Method to display the result of a particular move on the panel.
   *
   * @param s Text to be displayed
   */
  void move(String s);

  void moveDir(String direction);

  void pickTreasure();

  void pickArrow();

  void shoot(String shootingDistance, Direction direction);

}
