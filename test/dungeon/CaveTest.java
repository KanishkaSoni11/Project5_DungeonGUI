package dungeon;

import org.junit.Before;
import org.junit.Test;

import location.Cave;
import location.Direction;
import location.Location;
import location.LocationType;
import location.Treasure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Class to test the location cave of the dungeon.
 */
public class CaveTest {
  private Location cave;

  @Before
  public void setup() {
    cave = new Cave(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCaveId() {
    new Cave(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDirectionInvalid() {
    cave.addCaveList(null, new Cave(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCaveConsInvalid() {
    new Cave(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCaveInvalid() {
    cave.addCaveList(Direction.EAST, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTreasureInvalid() {
    cave.addTreasureToCave(null);
  }

  @Test
  public void testAddCaveList() {
    cave.addCaveList(Direction.EAST, new Cave(3));
    assertEquals(1, cave.getCaveList().size());
  }

  @Test
  public void testGetCaveList() {
    cave.addCaveList(Direction.EAST, new Cave(3));
    assertEquals(3, cave.getCaveList().get(Direction.EAST).getCaveId());

  }

  @Test
  public void testAddTreasureToCave() {
    cave.addTreasureToCave(Treasure.RUBY);
    cave.addTreasureToCave(Treasure.SAPPHIRE);
    assertEquals(2, cave.getTreasureList().size());
  }

  @Test
  public void testGetLocationType() {
    cave.addCaveList(Direction.EAST, new Cave(3));
    cave.addCaveList(Direction.NORTH, new Cave(4));
    assertEquals(LocationType.TUNNEL, cave.getLocationType());

    cave.addCaveList(Direction.WEST, new Cave(5));
    assertEquals(LocationType.CAVE, cave.getLocationType());

  }

  @Test
  public void testGetCaveId() {
    assertEquals(1, cave.getCaveId());
  }

  @Test
  public void testRemoveTreasure() {
    cave.addTreasureToCave(Treasure.RUBY);
    cave.addTreasureToCave(Treasure.SAPPHIRE);
    assertEquals(2, cave.getTreasureList().size());

    cave.removeTreasure();
    assertEquals(0, cave.getTreasureList().size());

  }
}