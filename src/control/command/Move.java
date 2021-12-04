package control.command;

import control.DungeonCommandController;
import dungeon.Dungeon;

/**
 * This class is the representation of the move command in the controller. This will help the player
 * to move in the given direction.
 */
public class Move implements DungeonCommandController {
  private String move;

  /**
   * Constructs the move command with a string that will store the results.
   *
   * @param move String to store the command execution results.
   */
  public Move(String move) {
    this.move = move;
  }

  @Override
  public String goPlay(Dungeon dungeon) throws IllegalArgumentException {
    StringBuilder sb = new StringBuilder();
    if (dungeon == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    try {
      sb.append(dungeon.maze(this.move));
    } catch (IllegalArgumentException exception) {
      sb.append(exception.getMessage());
    }
    return sb.toString();
  }
}
