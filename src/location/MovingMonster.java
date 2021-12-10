package location;

public class MovingMonster implements LocationMonster {

  private int health;

  public MovingMonster() {
    health = 100;
  }

  public MovingMonster(LocationMonster movingMonster) throws IllegalArgumentException {
    if (movingMonster == null) {
      throw new IllegalArgumentException("Moving Monster cannot be null");
    }
    health = movingMonster.getHealth();
  }

  @Override
  public void injure() {
    this.health = health - 50;
  }

  @Override
  public int getHealth() {
    return this.health;
  }

}
