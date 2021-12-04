package control;

import dungeon.Dungeon;

/**
 * IDungeonController represents the controller for the model which is being called to run all
 * the commands.
 */
public interface IDungeonController{

  /**
   * Method to start the game by the player in the dungeon.
   *
   * @param dungeon object of the dungeon for which the game has to be played.
   * @throws IllegalArgumentException if the dungeon is null
   */
  void play(Dungeon dungeon) throws IllegalArgumentException;

  void playView();

  void move(String s);

}
