package control;

import dungeon.Dungeon;

/**
 * DungeonCommandController represents the functions to call the commands in the controller.
 */
public interface DungeonCommandController {

  /**
   * Method that is called in all the commands to run depending on the user inputs.
   *
   * @param dungeon Dungeon on which commands have to be executed.
   * @return String representation of the result of the execution of the commands.
   * @throws IllegalArgumentException if dungeon is null
   */
  String goPlay(Dungeon dungeon) throws IllegalArgumentException;
}
