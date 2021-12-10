package location;

/**
 * Class to represent the Monster in the cave along with it's health.
 */
public class Monster implements LocationMonster {

  private int health;

  /**
   * Constructs the monster with the health as 100.
   */
  public Monster() {
    health = 100;
  }

  /**
   * Copy constructor of the monster to avoid mutating of the cave object.
   *
   * @param monster object of monster
   * @throws IllegalArgumentException if the monster is null
   */
  public Monster(LocationMonster monster) throws IllegalArgumentException {
    if (monster == null) {
      throw new IllegalArgumentException("Monster cannot be null.");
    }
    this.health = monster.getHealth();
  }

  @Override
  public void injure() {
    if (health > 0) {
      health = health - 50;
    }
  }

  @Override
  public int getHealth() {
    return this.health;
  }



}
