package control.command;

import control.DungeonCommandController;
import dungeon.Dungeon;

/**
 * This class is the representation of the pickup command in the controller for treasures.
 * This will help the player to pick up treasures in the given cave.
 */

public class PickTreasure implements DungeonCommandController {

  @Override
  public String goPlay(Dungeon dungeon) throws IllegalArgumentException {
    StringBuilder sb = new StringBuilder();
    if (dungeon == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    sb.append(dungeon.treasureDesc()).append("\n");
    sb.append(dungeon.playerToPickTreasure());
    return sb.toString();
  }
}
