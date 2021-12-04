package dungeon;

import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import location.Direction;
import location.Location;
import location.LocationType;
import location.Smell;
import location.Treasure;
import randomiser.FixedRandomiser;
import randomiser.Randomiser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class to test the dungeon and verify the moves, treasures and location present in the dungeon.
 */

public class DungeonTest {
  private Dungeon dungeon;
  private Dungeon dungeonWrapped;


  @Before
  public void setup() {
    dungeon = dungeonImp(5, 6, 2, 50
            , false, new FixedRandomiser(4), 5);
    dungeonWrapped = dungeonImp(5, 6, 3, 50
            , true, new FixedRandomiser(2, 3, 4), 5);
  }

  protected DungeonImpl dungeonImp(int rows, int columns, int interconnectivity
          , float treasurePercent, boolean wrapping, Randomiser randomiser, int numberOfMonster) {
    return new DungeonImpl(rows, columns, interconnectivity,
            wrapping, treasurePercent, randomiser, numberOfMonster);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidRows() {
    dungeonImp(-5, 6, 2, 50
            , false, new FixedRandomiser(4), 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidCol() {
    dungeonImp(5, -6, 2, 50
            , false, new FixedRandomiser(4), 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidInterConnectivity() {
    dungeonImp(5, 6, -2, 50, false
            , new FixedRandomiser(4), 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void largerInterConnectivity() {
    dungeonImp(5, 6, 60, 50, false
            , new FixedRandomiser(4), 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidHighTreasurePercent() {
    dungeonImp(5, 6, 2, 200, false
            , new FixedRandomiser(4), 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidLowTreasurePercent() {
    dungeonImp(5, 6, 2, -50, false
            , new FixedRandomiser(4), 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullRandomizer() {
    dungeonImp(5, 6, 2
            , 50, false, null, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void negMonster() {
    dungeonImp(5, 6, 2, 50, false
            , new FixedRandomiser(4), -5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void smallDungeon() {
    dungeonImp(2, 2, 2
            , 50, false, new FixedRandomiser(4), 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullBfs() {
    dungeon.endUsingBfs(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShootDisNeg() {
    dungeon.shootArrow(-1, Direction.EAST);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShootZeroDis() {
    dungeon.shootArrow(0, Direction.EAST);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testShootDirNull() {
    dungeon.shootArrow(10, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMove() {
    String s = null;
    dungeon.maze(s);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMonsters() {
    dungeon = dungeonImp(5, 6, 2, 50
            , false, new FixedRandomiser(4), 100);
  }


  @Test
  public void testGetPlayer() {
    assertEquals("John", dungeon.getPlayer().getName());
  }

  @Test
  public void testTunnelConnectivity() {

    //Test for Non wrapping
    List<Location> list = dungeon.getListOfCaves();

    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getLocationType() == LocationType.TUNNEL) {
        assertEquals(2, list.get(i).getCaveList().size());
      }
    }

    // Test for wrapping
    List<Location> list1 = dungeonWrapped.getListOfCaves();

    for (int i = 0; i < list1.size(); i++) {
      if (list1.get(i).getLocationType() == LocationType.TUNNEL) {
        assertEquals(2, list1.get(i).getCaveList().size());
      }
    }
  }

  @Test
  public void testCaveEntrance() {
    //Test for Non wrapping
    List<Location> list = dungeon.getListOfCaves();

    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getLocationType() == LocationType.CAVE) {
        assertTrue(list.get(i).getCaveList().size() == 1 ||
                list.get(i).getCaveList().size() == 3 || list.get(i).getCaveList().size() == 4);
      }
    }

    // Test for wrapping
    List<Location> list1 = dungeonWrapped.getListOfCaves();

    for (int i = 0; i < list1.size() - 1; i++) {
      if (list1.get(i).getLocationType() == LocationType.CAVE) {
        assertTrue(list1.get(i).getCaveList().size() == 1 ||
                list1.get(i).getCaveList().size() == 3 || list1.get(i).getCaveList().size() == 4);
      }
    }
  }

  @Test
  public void testConnectivity() {
    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
      //since the bfs gives the size of map, it means every cave is connected to every other cave.
      assertEquals(30, dungeon.endUsingBfs(dungeon.getListOfCaves().get(i)).size());
    }
  }

  @Test
  public void testConnectivityWrapping() {
    for (int i = 0; i < dungeonWrapped.getListOfCaves().size(); i++) {
      //since the bfs gives the size of map, it means every cave is connected to every other cave.
      assertEquals(30
              , dungeonWrapped.endUsingBfs(dungeonWrapped.getListOfCaves().get(i)).size());
    }
  }

  @Test
  public void testTreasurePercentAtleast() {
    int count = 0;
    List<Location> caveList = new ArrayList<Location>();
    dungeon = dungeonImp(5, 6, 2, 50
            , false, new FixedRandomiser(2), 5);
    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
      if (dungeon.getListOfCaves().get(i).getLocationType() == LocationType.CAVE) {
        caveList.add(dungeon.getListOfCaves().get(i));
      }
    }

    for (int i = 0; i < caveList.size(); i++) {
      if (caveList.get(i).getTreasureList().size() > 0) {
        count++;
      }

    }
    assertEquals(50, (count * 100) / caveList.size(), 0.01);
  }

  @Test
  public void testTreasurePercentAtleastWrapped() {
    int count = 0;
    List<Location> caveList = new ArrayList<Location>();
    dungeon = dungeonImp(5, 6, 2
            , 50, true, new FixedRandomiser(2), 5);
    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
      if (dungeon.getListOfCaves().get(i).getLocationType() == LocationType.CAVE) {
        caveList.add(dungeon.getListOfCaves().get(i));
      }
    }

    for (int i = 0; i < caveList.size(); i++) {
      if (caveList.get(i).getTreasureList().size() > 0) {
        count++;
      }

    }
    assertEquals(50, (count * 100) / caveList.size(), 0.01);
  }

  @Test
  public void testTreasurePercentExact() {
    int count = 0;
    List<Location> caveList = new ArrayList<Location>();
    dungeon = dungeonImp(5, 6, 1
            , 20, false, new FixedRandomiser(0), 5);
    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
      if (dungeon.getListOfCaves().get(i).getLocationType() == LocationType.CAVE) {
        caveList.add(dungeon.getListOfCaves().get(i));
      }
    }

    for (int i = 0; i < caveList.size(); i++) {
      if (caveList.get(i).getTreasureList().size() > 0) {
        count++;
      }
    }
    assertEquals(20, (count * 100) / caveList.size(), 0.01);
  }

  @Test
  public void testTreasurePercentExactWrapped() {
    int count = 0;
    List<Location> caveList = new ArrayList<Location>();
    dungeon = dungeonImp(5, 6, 1
            , 20, true, new FixedRandomiser(0), 5);
    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
      if (dungeon.getListOfCaves().get(i).getLocationType() == LocationType.CAVE) {
        caveList.add(dungeon.getListOfCaves().get(i));
      }
    }

    for (int i = 0; i < caveList.size(); i++) {
      if (caveList.get(i).getTreasureList().size() > 0) {
        count++;
      }
    }
    assertEquals(20, (count * 100) / caveList.size(), 0.01);
  }

  @Test
  public void testCaveTreasure() {
    int count = 0;
    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
      if (dungeon.getListOfCaves().get(i).getTreasureList().size() > 1) {
        count++;
      }
    }
    //4  caves have more than one treasure
    assertEquals(6, count);

    count = 0;
    for (int i = 0; i < dungeonWrapped.getListOfCaves().size(); i++) {
      if (dungeonWrapped.getListOfCaves().get(i).getTreasureList().size() > 1) {
        count++;
      }
    }
    //4  caves have more than one treasure
    assertEquals(10, count);

  }

  @Test
  public void testPickTreasure() {
    assertEquals(3, dungeon.getListOfCaves().get(1).getTreasureList().size());
    dungeon.playerToPickTreasure();
    assertEquals(3, dungeon.getPlayer().getTreasureList().size());
    assertEquals(0, dungeon.getListOfCaves().get(1).getTreasureList().size());
    assertEquals(Treasure.RUBY, dungeon.getPlayer().getTreasureList().get(0));
    assertEquals(Treasure.RUBY, dungeon.getPlayer().getTreasureList().get(1));
    assertEquals(Treasure.RUBY, dungeon.getPlayer().getTreasureList().get(2));
  }

  @Test
  public void testPickTreasureWrapped() {
    assertEquals(3, dungeonWrapped.getListOfCaves().get(0).getTreasureList().size());
    dungeonWrapped.playerToPickTreasure();
    assertEquals(3, dungeonWrapped.getPlayer().getTreasureList().size());
    assertEquals(0, dungeonWrapped.getListOfCaves().get(1).getTreasureList().size());
  }

  @Test
  public void testTreasureTunnel() {
    List<Location> tunnelList = new ArrayList<Location>();
    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
      if (dungeon.getListOfCaves().get(i).getLocationType() == LocationType.TUNNEL) {
        tunnelList.add(dungeon.getListOfCaves().get(i));
      }
    }

    for (int i = 0; i < tunnelList.size(); i++) {
      assertTrue(tunnelList.get(i).getTreasureList().size() == 0);
    }
  }

  @Test
  public void testPlayerDesc() {
    dungeon = dungeonImp(5, 6, 2, 50, false
            , new FixedRandomiser(4), 5);
    assertEquals("Player is at 1\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "EAST\n" +
            "WEST\n", dungeon.getPlayerDesc());
  }

  @Test
  public void testPlayerDescWithTreasure() {
    assertEquals(3, dungeon.getPlayer().getCurrentCave().getTreasureList().size());
    dungeon.playerToPickTreasure();
    assertEquals("Player is at 1\n" +
            "\n" +
            "Player has the following treasures :\n" +
            "RUBY\n" +
            "RUBY\n" +
            "RUBY\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "EAST\n" +
            "WEST\n", dungeon.getPlayerDesc());
  }

  @Test
  public void testLocationDesc() {
    assertEquals(3, dungeon.getPlayer().getCurrentCave().getTreasureList().size());


    assertEquals("Player is at 1\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "EAST\n" +
            "WEST\n", dungeon.getPlayerDesc());

    dungeon.playerToPickTreasure();
    dungeon.maze("E");
    assertEquals("Player is at 2\n" +
            "\n" +
            "Player has the following treasures :\n" +
            "RUBY\n" +
            "RUBY\n" +
            "RUBY\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "EAST\n" +
            "WEST\n", dungeon.getPlayerDesc());
  }

  @Test
  public void testIsTreasure() {
    assertTrue(dungeon.getPlayer().getCurrentCave().getTreasureList().size() > 0);
    assertTrue(dungeon.isTreasure());
  }

  @Test
  public void testMazeMoves() {
    assertEquals(1, dungeon.getPlayer().getCurrentCave().getCaveId());

    assertEquals(2
            , dungeon.getListOfCaves().get(1).getCaveList().get(Direction.EAST).getCaveId());
    assertEquals(7
            , dungeon.getListOfCaves().get(1).getCaveList().get(Direction.SOUTH).getCaveId());
    assertEquals(0
            , dungeon.getListOfCaves().get(1).getCaveList().get(Direction.WEST).getCaveId());

    dungeon.maze("E");
    assertEquals(2, dungeon.getPlayer().getCurrentCave().getCaveId());

    assertEquals(3
            , dungeon.getListOfCaves().get(2).getCaveList().get(Direction.EAST).getCaveId());
    assertEquals(8
            , dungeon.getListOfCaves().get(2).getCaveList().get(Direction.SOUTH).getCaveId());
    assertEquals(1
            , dungeon.getListOfCaves().get(2).getCaveList().get(Direction.WEST).getCaveId());

    dungeon.maze("S");
    assertEquals(8, dungeon.getPlayer().getCurrentCave().getCaveId());

    assertEquals(7
            , dungeon.getListOfCaves().get(8).getCaveList().get(Direction.WEST).getCaveId());
    assertEquals(9
            , dungeon.getListOfCaves().get(8).getCaveList().get(Direction.EAST).getCaveId());
    assertEquals(2
            , dungeon.getListOfCaves().get(8).getCaveList().get(Direction.NORTH).getCaveId());

    dungeon.maze("W");
    assertEquals(7, dungeon.getPlayer().getCurrentCave().getCaveId());

    assertEquals(6
            , dungeon.getListOfCaves().get(7).getCaveList().get(Direction.WEST).getCaveId());
    assertEquals(8
            , dungeon.getListOfCaves().get(7).getCaveList().get(Direction.EAST).getCaveId());

    assertEquals(1
            , dungeon.getListOfCaves().get(7).getCaveList().get(Direction.NORTH).getCaveId());
    dungeon.maze("N");
    assertEquals(1, dungeon.getPlayer().getCurrentCave().getCaveId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMove() {
    assertEquals(1, dungeon.getPlayer().getCurrentCave().getCaveId());

    assertEquals(2
            , dungeon.getListOfCaves().get(1).getCaveList().get(Direction.EAST).getCaveId());
    assertEquals(7
            , dungeon.getListOfCaves().get(1).getCaveList().get(Direction.SOUTH).getCaveId());
    assertEquals(0
            , dungeon.getListOfCaves().get(1).getCaveList().get(Direction.WEST).getCaveId());
    assertEquals("", dungeon.maze("MA"));

  }

  @Test
  public void testIncorrectMove() {
    assertEquals(1, dungeon.getPlayer().getCurrentCave().getCaveId());

    assertEquals(2, dungeon.getListOfCaves().get(1).getCaveList().get(Direction.EAST)
            .getCaveId());
    assertEquals(7, dungeon.getListOfCaves().get(1).getCaveList().get(Direction.SOUTH)
            .getCaveId());
    assertEquals(0, dungeon.getListOfCaves().get(1).getCaveList().get(Direction.WEST)
            .getCaveId());

    assertEquals("The entered direction was invalid\n", dungeon.maze("N"));
  }

  @Test
  public void testTreasureDesc() {
    assertEquals("Treasures in the destinations: \n" +
                    " RUBY RUBY RUBY"
            , dungeon.treasureDesc());
  }

  @Test
  public void testHasReachedEnd() {
    Dungeon dungeonUnWrapped1 = new DungeonImpl(4, 4, 2, true
            , 50, new FixedRandomiser(0), 5);
    int end = dungeonUnWrapped1.getEnd().getCaveId();
    dungeonUnWrapped1.maze("S");

    dungeonUnWrapped1.maze("S");

    dungeonUnWrapped1.maze("W");
    dungeonUnWrapped1.maze("W");
    dungeonUnWrapped1.maze("S");

    assertFalse(dungeonUnWrapped1.hasReached());
    assertNotEquals(dungeonUnWrapped1.getEnd().getCaveId(), dungeonUnWrapped1.getPlayer()
            .getCurrentCave().getCaveId());

  }

  @Test
  public void testStartEndAtleastFive() {

    //Unwrapped Dungeon
    dungeon = dungeonImp(5, 6, 2, 50, false
            , new FixedRandomiser(4), 5);

    Map<Location, Integer> bfsLevel = dungeon.endUsingBfs(dungeon.getStart());

    int distance = 0;

    for (Map.Entry<Location, Integer> levels : bfsLevel.entrySet()) {
      if (dungeon.getEnd().getCaveId() == levels.getKey().getCaveId()) {
        distance = levels.getValue();
      }
    }

    assertTrue(distance >= 5);

    //Wrapped Dungeon
    dungeonWrapped = dungeonImp(5, 6, 2, 50
            , true, new FixedRandomiser(1, 4), 5);

    bfsLevel = dungeonWrapped.endUsingBfs(dungeonWrapped.getStart());

    for (Map.Entry<Location, Integer> levels : bfsLevel.entrySet()) {
      if (dungeonWrapped.getEnd().getCaveId() == levels.getKey().getCaveId()) {
        distance = levels.getValue();
      }
    }
    assertTrue(distance >= 5);
  }

  @Test
  public void testStart() {
    assertEquals(dungeon.getPlayer().getCurrentCave().getCaveId(), dungeon.getStart().getCaveId());
  }

  @Test
  public void testStartWrapped() {
    assertEquals(dungeonWrapped.getPlayer().getCurrentCave().getCaveId()
            , dungeonWrapped.getStart().getCaveId());
  }


  @Test
  public void testDungeon() {
    dungeonWrapped = new DungeonImpl(5, 5, 1, true,
            20, new FixedRandomiser(1), 5);
    assertEquals("----------Dungeon----------\n"
                    + "\n"
                    + "Tunnel0\n"
                    + "This tunnel is connected to : \n"
                    + "SOUTH Cave5\n"
                    + "EAST Cave1\n"
                    + "Arrows = 1\n"
                    + "\n"
                    + "Cave1\n"
                    + "This cave is connected to the :\n"
                    + "SOUTH Cave6\n"
                    + "EAST Tunnel2\n"
                    + "WEST Tunnel0\n"
                    + "Arrows = 1\n"
                    + "\n"
                    + "Tunnel2\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel3\n"
                    + "WEST Cave1\n"
                    + "Arrows = 1\n"
                    + "\n"
                    + "Tunnel3\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Cave4\n"
                    + "WEST Tunnel2\n"
                    + "Arrows = 1\n"
                    + "\n"
                    + "Cave4\n"
                    + "This cave is connected to the :\n"
                    + "WEST Tunnel3\n"
                    + "There is a monster in the here\n"
                    + "Arrows = 1\n"
                    + "\n"
                    + "Cave5\n"
                    + "This cave is connected to the :\n"
                    + "NORTH Tunnel0\n"
                    + "SOUTH Cave10\n"
                    + "EAST Cave6\n"
                    + "There is a monster in the here\n"
                    + "\n"
                    + "Cave6\n"
                    + "This cave is connected to the :\n"
                    + "NORTH Cave1\n"
                    + "EAST Tunnel7\n"
                    + "WEST Cave5\n"
                    + "There is a monster in the here\n"
                    + "\n"
                    + "Tunnel7\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel8\n"
                    + "WEST Cave6\n"
                    + "\n"
                    + "Tunnel8\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Cave9\n"
                    + "WEST Tunnel7\n"
                    + "\n"
                    + "Cave9\n"
                    + "This cave is connected to the :\n"
                    + "WEST Tunnel8\n"
                    + "There is a monster in the here\n"
                    + "\n"
                    + "Cave10\n"
                    + "This cave is connected to the :\n"
                    + "NORTH Cave5\n"
                    + "SOUTH Cave15\n"
                    + "EAST Tunnel11\n"
                    + "\n"
                    + "Tunnel11\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel12\n"
                    + "WEST Cave10\n"
                    + "\n"
                    + "Tunnel12\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel13\n"
                    + "WEST Tunnel11\n"
                    + "\n"
                    + "Tunnel13\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Cave14\n"
                    + "WEST Tunnel12\n"
                    + "\n"
                    + "Cave14\n"
                    + "This cave is connected to the :\n"
                    + "WEST Tunnel13\n"
                    + "\n"
                    + "Cave15\n"
                    + "This cave is connected to the :\n"
                    + "NORTH Cave10\n"
                    + "SOUTH Tunnel20\n"
                    + "EAST Tunnel16\n"
                    + "\n"
                    + "Tunnel16\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel17\n"
                    + "WEST Cave15\n"
                    + "\n"
                    + "Tunnel17\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel18\n"
                    + "WEST Tunnel16\n"
                    + "\n"
                    + "Tunnel18\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Cave19\n"
                    + "WEST Tunnel17\n"
                    + "\n"
                    + "Cave19\n"
                    + "This cave is connected to the :\n"
                    + "WEST Tunnel18\n"
                    + "\n"
                    + "Tunnel20\n"
                    + "This tunnel is connected to : \n"
                    + "NORTH Cave15\n"
                    + "EAST Tunnel21\n"
                    + "\n"
                    + "Tunnel21\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel22\n"
                    + "WEST Tunnel20\n"
                    + "\n"
                    + "Tunnel22\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel23\n"
                    + "WEST Tunnel21\n"
                    + "\n"
                    + "Tunnel23\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Cave24\n"
                    + "WEST Tunnel22\n"
                    + "\n"
                    + "Cave24\n"
                    + "This cave is connected to the :\n"
                    + "WEST Tunnel23\n"
                    + "There is a monster in the here"
            , dungeonWrapped.getDungeon());
  }


  @Test
  public void testMonster() {
    int monster = 0;
    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
      if (dungeon.getListOfCaves().get(i).hasMonster()) {
        monster++;
      }
    }
    assertEquals(5, monster);
  }

  @Test
  public void testFreq() {
    dungeon = dungeonImp(5, 4, 1, 20
            , false, new FixedRandomiser(10), 5);
    int treasure = 0;
    int arrow = 0;
    int caves = 0;
    int total = 0;
    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
      if (dungeon.getListOfCaves().get(i).getTreasureList().size() > 0) {
        treasure++;
      }
      if (dungeon.getListOfCaves().get(i).getArrow() > 0) {
        arrow++;
      }
    }

    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
      if (dungeon.getListOfCaves().get(i).getLocationType() == LocationType.CAVE) {
        caves++;
      }
      total++;
    }
    double arrowFreq = (arrow * 100) / total;
    double treasureFreq = (treasure * 100) / caves;
    assertEquals(treasureFreq, arrowFreq, 0.01);
    assertEquals(30, arrowFreq, 0.01);
  }

  @Test
  public void testArrowPickCave() {
    assertEquals(4, dungeon.getPlayer().getCurrentCave().getArrow());
    assertEquals(3, dungeon.getPlayer().getArrowCount());
    assertEquals(dungeon.getPlayer().getCurrentCave().getLocationType(), LocationType.CAVE);
    dungeon.playerToPickArrow();

    assertEquals(7, dungeon.getPlayer().getArrowCount());
    assertEquals(0, dungeon.getPlayer().getCurrentCave().getArrow());
  }

  @Test
  public void testArrowPickTunnel() {
    dungeon.maze("W");
    assertEquals(4, dungeon.getPlayer().getCurrentCave().getArrow());
    assertEquals(3, dungeon.getPlayer().getArrowCount());
    assertEquals(dungeon.getPlayer().getCurrentCave().getLocationType(), LocationType.TUNNEL);
    dungeon.playerToPickArrow();

    assertEquals(7, dungeon.getPlayer().getArrowCount());
    assertEquals(0, dungeon.getPlayer().getCurrentCave().getArrow());
  }

  @Test
  public void testArrowExists() {
    int tunnel = 0;
    int cave = 0;
    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
      if (dungeon.getListOfCaves().get(i).getLocationType() == LocationType.CAVE) {
        cave++;
      } else {
        tunnel++;
      }
    }

    assertTrue(cave > 0);
    assertTrue(tunnel > 0);

  }

  @Test
  public void testArrowShoot() {
    assertEquals(1, dungeon.getPlayer().getCurrentCave().getCaveId());
    assertEquals("Player has injured the monster.\n",
            dungeon.shootArrow(1, Direction.EAST));
    assertEquals("Player has killed the monster.\n",
            dungeon.shootArrow(1, Direction.EAST));

    // After shooting twice at the same monster, the monster is killed.
    dungeon.maze("E");
    //assertFalse(dungeon.checkMonster());

    // The player has shot where the monster is not present.
    assertEquals("Player shot into into the darkness.\n",
            dungeon.shootArrow(4, Direction.WEST));

  }

  @Test
  public void testSmell() {
    assertEquals(1, dungeon.getPlayer().getCurrentCave().getCaveId());

    assertEquals(Smell.HIGH, dungeon.getSmell());

    dungeon.maze("W");

    dungeon.maze("S");
    dungeon.maze("S");
    dungeon.maze("S");

    assertEquals(Smell.LOW, dungeon.getSmell());

    dungeon.maze("S");

    assertEquals(Smell.NONE, dungeon.getSmell());

  }

  @Test
  public void testKill() {
    assertEquals(1, dungeon.getPlayer().getCurrentCave().getCaveId());
    assertEquals("Player has injured the monster.\n",
            dungeon.shootArrow(1, Direction.EAST));

    // Monster has been injured.
    assertEquals(50, dungeon.getListOfCaves().get(2).getMonster().getHealth());
    assertEquals("Player has killed the monster.\n",
            dungeon.shootArrow(1, Direction.EAST));


  }

  @Test
  public void testOtyughStartEnd() {
    assertFalse(dungeon.getStart().hasMonster());
    assertTrue(dungeon.getEnd().hasMonster());
  }

  @Test
  public void testOtyughInCave() {

    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
      if (dungeon.getListOfCaves().get(i).getLocationType() == LocationType.TUNNEL) {
        assertFalse(dungeon.getListOfCaves().get(i).hasMonster());
      }
    }
  }

  @Test
  public void testOtherWithOtyugh() {
    assertTrue(dungeon.getListOfCaves().get(2).getArrow() > 0);
    assertTrue(dungeon.getListOfCaves().get(2).getTreasureList().size() > 0);
    assertTrue(dungeon.getListOfCaves().get(2).hasMonster());

  }

  @Test
  public void testPlayerDead() {
    assertEquals(1, dungeon.getPlayer().getCurrentCave().getCaveId());
    dungeon.maze("E");
    assertEquals(2, dungeon.getPlayer().getCurrentCave().getCaveId());
    assertTrue(dungeon.getPlayer().getCurrentCave().hasMonster());
    //dungeon.checkMonster();

    assertFalse(dungeon.getPlayer().getPlayerLive());
  }

  @Test
  public void testPlayerArrowsStart() {
    dungeon = dungeonImp(5, 6, 2, 50
            , false, new FixedRandomiser(4), 5);

    assertEquals(3, dungeon.getPlayer().getArrowCount());

  }

  @Test
  public void testOtyughInjuredPlayerEscaped() {
    dungeon = dungeonImp(5, 6, 2, 50
            , false, new FixedRandomiser(4), 5);

    assertEquals(1, dungeon.getPlayer().getCurrentCave().getCaveId());
    assertEquals("Player has injured the monster.\n",
            dungeon.shootArrow(1, Direction.SOUTH));
    assertEquals("The player has escaped!\n", dungeon.maze("S"));
  }

  @Test
  public void testHasReachedEndWrapped() {

    Dungeon dungeonWrapped = new DungeonImpl(4, 5, 4, true,
            20, new FixedRandomiser(2, 3, 4), 5);

    assertEquals(3, dungeonWrapped.getPlayer().getCurrentCave().getCaveId());

    dungeonWrapped.maze("W");
    dungeonWrapped.shootArrow(1, Direction.SOUTH);
    dungeonWrapped.shootArrow(1, Direction.SOUTH);
    dungeonWrapped.maze("S");
    dungeonWrapped.maze("S");
    dungeonWrapped.maze("E");
    dungeonWrapped.shootArrow(1, Direction.WEST);
    dungeonWrapped.shootArrow(1, Direction.WEST);
    dungeonWrapped.maze("W");
    dungeonWrapped.maze("S");
    dungeonWrapped.shootArrow(1, Direction.WEST);
    dungeonWrapped.shootArrow(1, Direction.WEST);
    dungeonWrapped.maze("W");
    assertEquals(15, dungeonWrapped.getPlayer().getCurrentCave()
            .getCaveId());
    assertTrue(dungeonWrapped.hasReached());
  }


  @Test
  public void testTunnelTurn() {
    dungeon = dungeonImp(5, 6, 2, 50
            , false, new FixedRandomiser(4), 5);

    assertEquals(1, dungeon.getPlayer().getCurrentCave().getCaveId());
    assertEquals(0,
            dungeon.getPlayer().getCurrentCave().getCaveList().get(Direction.WEST).getCaveId());
    assertTrue(dungeon.getListOfCaves().get(0).getLocationType() == LocationType.TUNNEL);

    assertTrue(dungeon.getListOfCaves().get(6).hasMonster());

    assertEquals("Player has injured the monster.\n",
            dungeon.shootArrow(1, Direction.WEST));
    assertEquals("Player has killed the monster.\n",
            dungeon.shootArrow(1, Direction.WEST));

    assertFalse(dungeon.getListOfCaves().get(6).hasMonster());

  }

  @Test
  public void testArrowStraight() {
    dungeon = dungeonImp(5, 6, 2, 50
            , false, new FixedRandomiser(4), 5);

    assertEquals(1, dungeon.getPlayer().getCurrentCave().getCaveId());
    assertTrue(dungeon.getListOfCaves().get(2).hasMonster());

    assertTrue(dungeon.getListOfCaves().get(2).getLocationType() == LocationType.CAVE);

    dungeon.shootArrow(1, Direction.EAST);
    dungeon.shootArrow(1, Direction.EAST);

    assertFalse(dungeon.getListOfCaves().get(2).hasMonster());

    dungeon.maze("E");
    assertTrue(dungeon.getListOfCaves().get(5).hasMonster());

    dungeon.shootArrow(1, Direction.EAST);
    dungeon.shootArrow(1, Direction.EAST);

    dungeon.maze("E");

    dungeon.maze("E");

    dungeon.maze("E");

    assertFalse(dungeon.getListOfCaves().get(5).hasMonster());
    assertTrue(dungeon.getListOfCaves().get(5).getLocationType() == LocationType.CAVE);

  }




}

