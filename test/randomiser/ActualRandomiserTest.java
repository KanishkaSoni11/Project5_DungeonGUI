package randomiser;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import location.Cave;
import location.Location;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

/**
 * Class to test the Random Number generator for Randomiser interface.
 */

public class ActualRandomiserTest {

  private Randomiser number1;
  private List<Location> caveList;

  @Before
  public void setup() {
    number1 = new ActualRandomiser();
    caveList = new ArrayList<Location>();
    caveList.add(new Cave(1));
    caveList.add(new Cave(2));
    caveList.add(new Cave(3));

  }

  @Test
  public void getRandom() {
    assertTrue(number1.getRandom(4, 8) >= 4 || number1.getRandom(4, 8) <= 8);
  }

  @Test
  public void shuffle() {
    List<Location> unshuffledList = new ArrayList<>(caveList);
    number1.randomList(caveList);
    assertNotEquals(unshuffledList, caveList);
  }
}