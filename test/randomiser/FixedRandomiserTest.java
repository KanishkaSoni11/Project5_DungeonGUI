package randomiser;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import location.Cave;
import location.Location;

import static org.junit.Assert.assertEquals;

/**
 * Class for testing Fixed mock random number generator in Randomiser interface.
 */

public class FixedRandomiserTest {
  private Randomiser number1;
  private List<Location> caveList;

  @Before
  public void setup() {
    number1 = new FixedRandomiser(2);
    caveList = new ArrayList<Location>();
    caveList.add(new Cave(1));
    caveList.add(new Cave(2));
    caveList.add(new Cave(3));

  }

  @Test
  public void getRandom() {
    assertEquals(2, number1.getRandom(2, 3));
  }

  @Test
  public void shuffle() {
    List<Location> unshuffledList = new ArrayList<>(caveList);
    number1.randomList(caveList);
    assertEquals(unshuffledList, caveList);
  }
}