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
            , false, new FixedRandomiser(4), 2);
    dungeonWrapped = dungeonImp(5, 6, 3, 50
            , true, new FixedRandomiser(4), 2);
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
    dungeon.shootArrow("-1", Direction.EAST);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShootZeroDis() {
    dungeon.shootArrow("0", Direction.EAST);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testShootDirNull() {
    dungeon.shootArrow("10", null);
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
    assertEquals("Harry", dungeon.getPlayer().getName());
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
    assertEquals(7, count);

  }

  @Test
  public void testPickTreasure() {
    assertEquals(3, dungeon.getListOfCaves().get(1).getTreasureList().size());
    dungeon.playerToPickTreasure();
    assertEquals(3, dungeon.getPlayer().getTreasureList().size());
    assertEquals(0, dungeon.getListOfCaves().get(1).getTreasureList().size());
    assertEquals(Treasure.DIAMOND, dungeon.getPlayer().getTreasureList().get(0));
    assertEquals(Treasure.RUBY, dungeon.getPlayer().getTreasureList().get(1));
    assertEquals(Treasure.SAPPHIRE, dungeon.getPlayer().getTreasureList().get(2));
  }

//  @Test
//  public void testPickTreasureWrapped() {
//    assertEquals(3, dungeonWrapped.getListOfCaves().get(0).getTreasureList().size());
//    dungeonWrapped.playerToPickTreasure();
//    assertEquals(3, dungeonWrapped.getPlayer().getTreasureList().size());
//    assertEquals(0, dungeonWrapped.getListOfCaves().get(1).getTreasureList().size());
//  }

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
    assertEquals("Player is at 1\n"
            + "Arrows with player: 3\n", dungeon.getPlayerDesc());
  }

  @Test
  public void testPlayerDescWithTreasure() {
    assertEquals(3, dungeon.getPlayer().getPlayerCave().getTreasureList().size());
    dungeon.playerToPickTreasure();
    assertEquals("Player is at 1\n"
            + "\n"
            + "Player has the following treasures :\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "SAPPHIRE\n"
            + "Arrows with player: 3\n", dungeon.getPlayerDesc());
  }

  @Test
  public void testLocationDesc() {
    assertEquals(3, dungeon.getPlayer().getPlayerCave().getTreasureList().size());


    assertEquals("Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "SAPPHIRE\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "EAST\n"
            + "WEST\n", dungeon.getLocationDesc());

    dungeon.playerToPickTreasure();
    dungeon.maze("E");
    assertEquals("Player is at 2\n"
            + "\n"
            + "Player has the following treasures :\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "SAPPHIRE\n"
            + "Arrows with player: 3\n", dungeon.getPlayerDesc());
  }

  @Test
  public void testIsTreasure() {
    assertTrue(dungeon.getPlayer().getPlayerCave().getTreasureList().size() > 0);
    assertTrue(dungeon.isTreasure());
  }

  @Test
  public void testMazeMoves() {
    assertEquals(1, dungeon.getPlayer().getPlayerCave().getCaveId());

    assertEquals(2
            , dungeon.getListOfCaves().get(1).getCaveList().get(Direction.EAST).getCaveId());
    assertEquals(7
            , dungeon.getListOfCaves().get(1).getCaveList().get(Direction.SOUTH).getCaveId());
    assertEquals(0
            , dungeon.getListOfCaves().get(1).getCaveList().get(Direction.WEST).getCaveId());

    assertEquals("Player has moved successfully to 0\n", dungeon.maze("W"));


    assertEquals("The player encountered a thief and lost all it's treasures!! :(\n"
            + "Player has moved successfully to 6\n", dungeon.maze("S"));

    assertEquals("Player has moved successfully to 0\n", dungeon.maze("N"));

    assertEquals("Player has moved successfully to 1\n", dungeon.maze("E"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMove() {
    assertEquals(1, dungeon.getPlayer().getPlayerCave().getCaveId());

    assertEquals(2
            , dungeon.getListOfCaves().get(1).getCaveList().get(Direction.EAST).getCaveId());
    assertEquals(7
            , dungeon.getListOfCaves().get(1).getCaveList().get(Direction.SOUTH).getCaveId());
    assertEquals(0
            , dungeon.getListOfCaves().get(1).getCaveList().get(Direction.WEST).getCaveId());
    dungeon.maze("MA");

  }

  @Test
  public void testIncorrectMove() {
    assertEquals(1, dungeon.getPlayer().getPlayerCave().getCaveId());

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
    assertEquals("Treasures in the destinations: \n"
                    + " DIAMOND RUBY SAPPHIRE"
            , dungeon.treasureDesc());
  }

  @Test
  public void testHasReachedEnd() {
    assertEquals(1, dungeon.getStart().getCaveId());
    assertEquals(17, dungeon.getEnd().getCaveId());

    dungeon.maze("S");
    dungeon.maze("W");
    dungeon.maze("S");
    dungeon.maze("E");
    dungeon.maze("E");
    dungeon.maze("E");
    dungeon.maze("E");
    dungeon.maze("E");

    assertEquals(17, dungeon.getPlayer().getPlayerCave().getCaveId());

    assertTrue(dungeon.hasReached());


  }

//  @Test
//  public void testStartEndAtleastFive() {
//
//    //Unwrapped Dungeon
//    dungeon = dungeonImp(5, 6, 2, 50, false
//            , new FixedRandomiser(4), 5);
//
//    Map<Location, Integer> bfsLevel = dungeon.endUsingBfs(dungeon.getStart());
//
//    int distance = 0;
//
//    for (Map.Entry<Location, Integer> levels : bfsLevel.entrySet()) {
//      if (dungeon.getEnd().getCaveId() == levels.getKey().getCaveId()) {
//        distance = levels.getValue();
//      }
//    }
//
//    assertTrue(distance >= 5);
//
//    //Wrapped Dungeon
//    dungeonWrapped = dungeonImp(5, 6, 2, 50
//            , true, new FixedRandomiser(1, 4), 5);
//
//    bfsLevel = dungeonWrapped.endUsingBfs(dungeonWrapped.getStart());
//
//    for (Map.Entry<Location, Integer> levels : bfsLevel.entrySet()) {
//      if (dungeonWrapped.getEnd().getCaveId() == levels.getKey().getCaveId()) {
//        distance = levels.getValue();
//      }
//    }
//    assertTrue(distance >= 5);
//  }

  @Test
  public void testStart() {
    assertEquals(dungeon.getPlayer().getPlayerCave().getCaveId(), dungeon.getStart().getCaveId());
  }

  @Test
  public void testStartWrapped() {
    assertEquals(dungeonWrapped.getPlayer().getPlayerCave().getCaveId()
            , dungeonWrapped.getStart().getCaveId());
  }


  @Test
  public void testDungeon() {
    assertEquals("----------Dungeon----------\n"
                    + "\n"
                    + "Tunnel0\n"
                    + "This tunnel is connected to : \n"
                    + "SOUTH Cave6\n"
                    + "EAST Cave1\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Cave1\n"
                    + "This cave is connected to the :\n"
                    + "SOUTH Cave7\n"
                    + "EAST Cave2\n"
                    + "WEST Tunnel0\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Cave2\n"
                    + "This cave is connected to the :\n"
                    + "SOUTH Cave8\n"
                    + "EAST Tunnel3\n"
                    + "WEST Cave1\n"
                    + "There is a monster in the here\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Tunnel3\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel4\n"
                    + "WEST Cave2\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Tunnel4\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Cave5\n"
                    + "WEST Tunnel3\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Cave5\n"
                    + "This cave is connected to the :\n"
                    + "WEST Tunnel4\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Cave6\n"
                    + "This cave is connected to the :\n"
                    + "NORTH Tunnel0\n"
                    + "SOUTH Cave12\n"
                    + "EAST Cave7\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Cave7\n"
                    + "This cave is connected to the :\n"
                    + "NORTH Cave1\n"
                    + "EAST Cave8\n"
                    + "WEST Cave6\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Cave8\n"
                    + "This cave is connected to the :\n"
                    + "NORTH Cave2\n"
                    + "EAST Tunnel9\n"
                    + "WEST Cave7\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Tunnel9\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel10\n"
                    + "WEST Cave8\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Tunnel10\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Cave11\n"
                    + "WEST Tunnel9\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Cave11\n"
                    + "This cave is connected to the :\n"
                    + "WEST Tunnel10\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Cave12\n"
                    + "This cave is connected to the :\n"
                    + "NORTH Cave6\n"
                    + "SOUTH Cave18\n"
                    + "EAST Tunnel13\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Tunnel13\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel14\n"
                    + "WEST Cave12\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Tunnel14\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel15\n"
                    + "WEST Tunnel13\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Tunnel15\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel16\n"
                    + "WEST Tunnel14\n"
                    + "Arrows = 4\n"
                    + "\n"
                    + "Tunnel16\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Cave17\n"
                    + "WEST Tunnel15\n"
                    + "\n"
                    + "Cave17\n"
                    + "This cave is connected to the :\n"
                    + "WEST Tunnel16\n"
                    + "There is a monster in the here\n"
                    + "\n"
                    + "Cave18\n"
                    + "This cave is connected to the :\n"
                    + "NORTH Cave12\n"
                    + "SOUTH Tunnel24\n"
                    + "EAST Tunnel19\n"
                    + "\n"
                    + "Tunnel19\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel20\n"
                    + "WEST Cave18\n"
                    + "\n"
                    + "Tunnel20\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel21\n"
                    + "WEST Tunnel19\n"
                    + "\n"
                    + "Tunnel21\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel22\n"
                    + "WEST Tunnel20\n"
                    + "\n"
                    + "Tunnel22\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Cave23\n"
                    + "WEST Tunnel21\n"
                    + "\n"
                    + "Cave23\n"
                    + "This cave is connected to the :\n"
                    + "WEST Tunnel22\n"
                    + "\n"
                    + "Tunnel24\n"
                    + "This tunnel is connected to : \n"
                    + "NORTH Cave18\n"
                    + "EAST Tunnel25\n"
                    + "\n"
                    + "Tunnel25\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel26\n"
                    + "WEST Tunnel24\n"
                    + "\n"
                    + "Tunnel26\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel27\n"
                    + "WEST Tunnel25\n"
                    + "\n"
                    + "Tunnel27\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Tunnel28\n"
                    + "WEST Tunnel26\n"
                    + "\n"
                    + "Tunnel28\n"
                    + "This tunnel is connected to : \n"
                    + "EAST Cave29\n"
                    + "WEST Tunnel27\n"
                    + "\n"
                    + "Cave29\n"
                    + "This cave is connected to the :\n"
                    + "WEST Tunnel28"
            , dungeon.getDungeon());
  }


  @Test
  public void testMonster() {
    int monster = 0;
    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
      if (dungeon.getListOfCaves().get(i).hasMonster()) {
        monster++;
      }
    }
    assertEquals(2, monster);
  }

//  @Test
//  public void testFreq() {
//    int treasure = 0;
//    int arrow = 0;
//    int caves = 0;
//    int total = 0;
//    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
//      if (dungeon.getListOfCaves().get(i).getTreasureList().size() > 0) {
//        treasure++;
//      }
//      if (dungeon.getListOfCaves().get(i).getArrow() > 0) {
//        arrow++;
//      }
//    }
//
//    for (int i = 0; i < dungeon.getListOfCaves().size(); i++) {
//      if (dungeon.getListOfCaves().get(i).getLocationType() == LocationType.CAVE) {
//        caves++;
//      }
//      total++;
//    }
//    double arrowFreq = (arrow * 100) / total;
//    double treasureFreq = (treasure * 100) / caves;
//    assertEquals(53, treasureFreq, 0.01);
//    assertEquals(53, arrowFreq, 0.01);
//  }

  @Test
  public void testArrowPickCave() {
    assertEquals(4, dungeon.getPlayer().getPlayerCave().getArrow());
    assertEquals(3, dungeon.getPlayer().getArrowCount());
    assertEquals(dungeon.getPlayer().getPlayerCave().getLocationType(), LocationType.CAVE);
    dungeon.playerToPickArrow();

    assertEquals(7, dungeon.getPlayer().getArrowCount());
    assertEquals(0, dungeon.getPlayer().getPlayerCave().getArrow());
  }

  @Test
  public void testArrowPickTunnel() {
    dungeon.maze("W");
    assertEquals(4, dungeon.getPlayer().getPlayerCave().getArrow());
    assertEquals(3, dungeon.getPlayer().getArrowCount());
    assertEquals(dungeon.getPlayer().getPlayerCave().getLocationType(), LocationType.TUNNEL);
    dungeon.playerToPickArrow();

    assertEquals(7, dungeon.getPlayer().getArrowCount());
    assertEquals(0, dungeon.getPlayer().getPlayerCave().getArrow());
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
    assertEquals(0, dungeon.getPlayer().getPlayerCave().getCaveId());
    dungeon.maze("E");
    assertEquals("Player has injured the monster.\n",
            dungeon.shootArrow("1", Direction.EAST));
    assertEquals("Player has killed the monster.\n",
            dungeon.shootArrow("1", Direction.EAST));

    // After shooting twice at the same monster, the monster is killed.
    dungeon.maze("E");
    //assertFalse(dungeon.checkMonster());

    // The player has shot where the monster is not present.
    assertEquals("Player shot into into the darkness.\n",
            dungeon.shootArrow("4", Direction.WEST));

  }

  @Test
  public void testSmell() {
    assertEquals(1, dungeon.getPlayer().getPlayerCave().getCaveId());

    assertEquals(Smell.HIGH, dungeon.getSmell());

    dungeon.maze("W");

    assertEquals(Smell.LOW, dungeon.getSmell());

    dungeon.maze("S");

    assertEquals(Smell.NONE, dungeon.getSmell());

  }

  @Test
  public void testKill() {
    assertEquals(1, dungeon.getPlayer().getPlayerCave().getCaveId());
    assertEquals("Player has injured the monster.\n",
            dungeon.shootArrow("1", Direction.EAST));

    // Monster has been injured.
    assertEquals(50, dungeon.getListOfCaves().get(2).getMonster().getHealth());
    assertEquals("Player has killed the monster.\n",
            dungeon.shootArrow("1", Direction.EAST));


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
    assertEquals(1, dungeon.getPlayer().getPlayerCave().getCaveId());
    dungeon.maze("E");
    assertEquals(2, dungeon.getPlayer().getPlayerCave().getCaveId());
    assertTrue(dungeon.getPlayer().getPlayerCave().hasMonster());
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

    assertEquals(1, dungeon.getPlayer().getPlayerCave().getCaveId());
    assertEquals("Player has injured the monster.\n",
            dungeon.shootArrow("1", Direction.EAST));
    assertEquals("Player has moved successfully to 2\n"
            + "The player has escaped!\n", dungeon.maze("E"));
  }


  @Test
  public void testTunnelTurn() {
    dungeon = dungeonImp(5, 6, 2, 50
            , false, new FixedRandomiser(4), 5);

    assertEquals(1, dungeon.getPlayer().getPlayerCave().getCaveId());
    assertEquals(0,
            dungeon.getPlayer().getPlayerCave().getCaveList().get(Direction.WEST).getCaveId());
    assertTrue(dungeon.getListOfCaves().get(0).getLocationType() == LocationType.TUNNEL);

    assertTrue(dungeon.getListOfCaves().get(6).hasMonster());

    assertEquals("Player has injured the monster.\n",
            dungeon.shootArrow("1", Direction.WEST));
    assertEquals("Player has killed the monster.\n",
            dungeon.shootArrow("1", Direction.WEST));

    assertFalse(dungeon.getListOfCaves().get(6).hasMonster());

  }

  @Test
  public void testArrowStraight() {
    dungeon = dungeonImp(5, 6, 2, 50
            , false, new FixedRandomiser(4), 5);

    assertEquals(1, dungeon.getPlayer().getPlayerCave().getCaveId());
    assertTrue(dungeon.getListOfCaves().get(2).hasMonster());

    assertTrue(dungeon.getListOfCaves().get(2).getLocationType() == LocationType.CAVE);

    dungeon.shootArrow("1", Direction.EAST);
    dungeon.shootArrow("1", Direction.EAST);

    assertFalse(dungeon.getListOfCaves().get(2).hasMonster());

    dungeon.maze("E");
    assertTrue(dungeon.getListOfCaves().get(5).hasMonster());

    dungeon.shootArrow("1", Direction.EAST);
    dungeon.shootArrow("1", Direction.EAST);

    dungeon.maze("E");

    dungeon.maze("E");

    dungeon.maze("E");

    assertFalse(dungeon.getListOfCaves().get(5).hasMonster());
    assertTrue(dungeon.getListOfCaves().get(5).getLocationType() == LocationType.CAVE);

  }

  @Test
  public void testHasPit() {
    assertTrue(dungeonWrapped.getListOfCaves().get(3).getPit());
    assertTrue(dungeonWrapped.getPit(dungeonWrapped.getListOfCaves().get(2)));
  }

  @Test
  public void testFallenIntoPit() {
    assertTrue(dungeonWrapped.getListOfCaves().get(3).getPit());

    dungeonWrapped.shootArrow("1", Direction.EAST);
    dungeonWrapped.shootArrow("1", Direction.EAST);

    dungeonWrapped.maze("E");

    dungeonWrapped.maze("E");

    assertEquals(3, dungeonWrapped.getPlayer().getPlayerCave().getCaveId());
    assertTrue(dungeonWrapped.hasFallenIntoPit());
  }

  @Test
  public void testHasThief() {

  }

}

