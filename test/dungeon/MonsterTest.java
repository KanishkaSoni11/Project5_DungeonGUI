package dungeon;

import org.junit.Before;
import org.junit.Test;

import location.LocationMonster;
import location.Monster;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the monster class.
 */
public class MonsterTest {

  private LocationMonster monster;

  @Before
  public void setup() {
    monster = new Monster();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMonster() {
    new Monster(null);
  }

  @Test
  public void injure() {
    monster.injure();
    assertEquals(50, monster.getHealth());
  }

  @Test
  public void getHealth() {
    assertEquals(100, monster.getHealth());
  }
}