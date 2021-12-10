package control.command;

import control.DungeonCommandController;
import dungeon.Dungeon;
import location.Direction;

/**
 * This class is the representation of the shoot command in the controller. This will help the
 * player to shoot the arrow in the given direction and at the given distance.
 */
public class ShootArrow implements DungeonCommandController {
  private String distance;
  private Direction direction;

  /**
   * Constructs the shoot command with the distance and direction.
   *
   * @param distance  distance at which the arrow has to be shot.
   * @param direction direction in which the arrow has to be shot.
   */
  public ShootArrow(String distance, Direction direction) {
    this.direction = direction;
    this.distance = distance;
  }

  @Override
  public String goPlay(Dungeon dungeon) throws IllegalArgumentException {
    StringBuilder sb = new StringBuilder();
    if (dungeon == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    try {
      if (dungeon.getPlayer().getArrowCount() > 0) {
        sb.append(dungeon.shootArrow(distance, direction));
      }else {
        sb.append("Player is not left with any arrows.");
      }
    } catch (IllegalArgumentException exception) {
      sb.append(exception.getMessage());
    }
    return sb.toString();
  }
}
