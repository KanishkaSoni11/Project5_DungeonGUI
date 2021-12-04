package control.command;

import control.DungeonCommandController;
import dungeon.Dungeon;

/**
 * This class is the representation of the pickup command in the controller for arrow
 * This will help the player to pick up arrows in the given cave.
 */

public class PickArrow implements DungeonCommandController {

  @Override
  public String goPlay(Dungeon dungeon) throws IllegalArgumentException {

    if (dungeon == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    return dungeon.playerToPickArrow();
  }
}
