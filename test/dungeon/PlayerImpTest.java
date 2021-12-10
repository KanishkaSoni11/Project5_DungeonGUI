package dungeon;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import location.Cave;
import location.Direction;
import location.Location;
import location.Treasure;
import player.Player;
import player.PlayerImp;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the player in the dungeon and verify the moves and locations of the player along
 * with the treasures picked.
 */
public class PlayerImpTest {
  private Player player;

  @Before
  public void setup() {
    player = new PlayerImp("Tom", new Cave(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testName() {
    String s = null;
    new PlayerImp(s, new Cave(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerNull() {
    new PlayerImp(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCave() {
    new PlayerImp("Harry", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTreasureListNull() {
    player.pickTreasure(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDirectionNull() {
    player.move(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCaveNull() {
    player.removeTreasure(null);
  }

  @Test
  public void testTestGetName() {
    assertEquals("Tom", player.getName());
  }

  @Test
  public void testGetCurrentCave() {
    assertEquals(1, player.getPlayerCave().getCaveId());
  }

  @Test
  public void testGetTreasureList() {
    Location cave = new Cave(1);
    Player player1 = new PlayerImp("Tom", cave);
    cave.addTreasureToCave(Treasure.RUBY);
    cave.addTreasureToCave(Treasure.SAPPHIRE);

    List<Treasure> treasureList = new ArrayList<Treasure>();
    treasureList.add(Treasure.SAPPHIRE);
    treasureList.add(Treasure.RUBY);

    player1.pickTreasure(treasureList);

    assertEquals(2, player1.getTreasureList().size());
  }

  @Test
  public void testMove() {

    assertEquals(1, player.getPlayerCave().getCaveId());
    player.move(Direction.EAST);
  }

  @Test
  public void testRemoveTreasure() {
    Location cave1 = new Cave(2);
    player.removeTreasure(cave1);
    assertEquals(0, cave1.getTreasureList().size());
  }
}