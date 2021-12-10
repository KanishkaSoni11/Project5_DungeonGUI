package location;

/**
 * CaveCreature is the representation of the creatures that can be present in each cave.
 */
public interface LocationMonster {

  /**
   * Method to reduce the health of the creature when it is shot.
   */
  void injure();

  /**
   * Method to get the health of the creature.
   *
   * @return health of the creature
   */
  int getHealth();

}
