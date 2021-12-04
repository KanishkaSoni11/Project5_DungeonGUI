package controllertest;

import org.junit.Test;

import java.io.StringReader;

import control.DungeonCommandController;
import control.DungeonController;
import control.IDungeonController;
import control.command.Move;
import control.command.PickArrow;
import control.command.PickTreasure;
import control.command.ShootArrow;
import dungeon.Dungeon;
import dungeon.DungeonImpl;
import location.Direction;
import randomiser.FixedRandomiser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class to test the controller of our Dungeon model.
 */
public class DungeonControllerTest {

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidModel() {
    StringReader input = new StringReader("M E S 1 E");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = null;
    controller.play(dungeon);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    StringReader input = new StringReader("M E S 1 E");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    controller.play(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidModelMove() {
    DungeonCommandController move = new Move("N");
    move.goPlay(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidModelPickArrow() {
    DungeonCommandController arrow = new PickArrow();
    arrow.goPlay(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidModelPickTreasure() {
    DungeonCommandController treasure = new PickTreasure();
    treasure.goPlay(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidModelShootArrow() {
    DungeonCommandController shoot = new ShootArrow(1, Direction.EAST);
    shoot.goPlay(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInNull() {
    StringReader input = null;
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOutNull() {
    StringReader input = new StringReader("M E S 1 E");

    Appendable gameLog = null;
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
  }

  @Test(expected = IllegalStateException.class)
  public void testFailingAppendable() {
    StringReader input = new StringReader("M E S 1 E");
    FailingAppendable failingAppendable = new FailingAppendable();
    IDungeonController controller = new DungeonController(input, failingAppendable);

    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
  }

  @Test
  public void testMove() {

    StringReader input = new StringReader("M E");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player is at 1\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testPickUpTreasure() {
    StringReader input = new StringReader("P Treasure");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "What?\n" +
            "Treasures in the destinations: \n" +
            " SAPPHIRE DIAMOND RUBY\n" +
            "Player picked the treasures.\n" +
            "\n" +
            "Player is at 0\n" +
            "\n" +
            "Player has the following treasures :\n" +
            "SAPPHIRE\n" +
            "DIAMOND\n" +
            "RUBY\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testPickUpArrow() {
    StringReader input = new StringReader("P Arrow");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "What?\n" +
            "Player picked the arrow.\n" +
            "\n" +
            "Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 5\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testShootArrow() {
    StringReader input = new StringReader("S 1 E");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "Player has injured the monster.\n" +
            "\n" +
            "Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 2\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testMoveInvalid() {
    StringReader input = new StringReader("M A");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "The entered direction was invalid\n" +
            "Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testInvalidPick() {
    StringReader input = new StringReader("P R");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "What?\n" +
            "Invalid entry!!\n" +
            "Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testArrowInvalid() {
    StringReader input = new StringReader("M E S 1 S S 1 S M S M S M S P Arrow");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertTrue("Player is at 19\n" +
            "Arrows with player: 1\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "NORTH\n" +
            "SOUTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "What?\n" +
            "There are no arrows here\n" +
            "Player is at 19\n" +
            "Arrows with player: 1", gameLog.toString().contains("There are no arrows here"));
  }

  @Test
  public void testTreasureInvalid() {
    StringReader input = new StringReader("M E S 1 S S 1 S M S M S M S P Treasure");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertTrue("The player can go to the following destinations :\n" +
            "NORTH\n" +
            "SOUTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "What?\n" +
            "There are no treasures here", gameLog.toString()
            .contains("There are no treasures here"));
  }

  @Test
  public void testPlayerDead() {
    StringReader input = new StringReader("M E M S P Arrow");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player is at 1\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "Oops there was a monster here. Eating the player chomp chomp chomp!!\n" +
            "\n" +
            "Player is dead!!", gameLog.toString());
  }

  @Test
  public void testShotDarkness() {
    StringReader input = new StringReader("M E S 1 E");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player is at 1\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "Player shot into into the darkness.\n" +
            "\n" +
            "Player is at 1\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 2\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testPlayerEscaped() {
    StringReader input = new StringReader("M E S 1 S M S M S");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player is at 1\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "Player has injured the monster.\n" +
            "\n" +
            "Player is at 1\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 2\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "The player has escaped!\n" +
            "\n" +
            "Player is at 7\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 2\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "NORTH\n" +
            "SOUTH\n" +
            "EAST\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player is at 13\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 2\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "NORTH\n" +
            "SOUTH\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n", gameLog.toString());

  }

  @Test
  public void testCommandInvalid() {
    StringReader input = new StringReader("A");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Unknown command A\n" +
            "Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testShootNoArrow() {
    StringReader input = new StringReader("S 1 E S 1 E S 1 E S 1 E");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "Player has injured the monster.\n" +
            "\n" +
            "Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 2\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "Player has killed the monster.\n" +
            "\n" +
            "Player is at 0\n" +
            "Arrows with player: 1\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "Player shot into into the darkness.\n" +
            "\n" +
            "Player is at 0\n" +
            "Arrows with player: 0\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player is at 0\n" +
            "Arrows with player: 0\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testReachedEnd() {
    StringReader input = new StringReader("P Arrow M W S 1 S S 1 S M S M S M " +
            "E S 1 W S 1 W M W M S S 1 W S 1 W M W");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(4, 5, 4, true,
            20, new FixedRandomiser(2, 3, 4), 5);
    controller.play(dungeon);

    assertEquals("Player is at 3\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "NORTH\n" +
            "EAST\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "What?\n" +
            "Player picked the arrow.\n" +
            "\n" +
            "Player is at 3\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 7\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "NORTH\n" +
            "EAST\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player is at 2\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 7\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "Player has injured the monster.\n" +
            "\n" +
            "Player is at 2\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 6\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "Player has killed the monster.\n" +
            "\n" +
            "Player is at 2\n" +
            "Arrows with player: 5\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player is at 7\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 5\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "NORTH\n" +
            "SOUTH\n" +
            "EAST\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player is at 12\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 5\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "NORTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "The entered direction was invalid\n" +
            "\n" +
            "Player is at 12\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 5\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "NORTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "Player has injured the monster.\n" +
            "\n" +
            "Player is at 12\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 4\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "NORTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "Player has killed the monster.\n" +
            "\n" +
            "Player is at 12\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "NORTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player is at 11\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "EAST\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player is at 16\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "NORTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "Player has injured the monster.\n" +
            "\n" +
            "Player is at 16\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 2\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "NORTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "Player has killed the monster.\n" +
            "\n" +
            "Player is at 16\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 1\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "NORTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player has reached the final destination!!!!", gameLog.toString());
  }

  @Test
  public void testInvalidDis() {
    StringReader input = new StringReader("S 0 E S -1 E");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "Distance should be greater than one.\n" +
            "Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter distance:\n" +
            "Enter the direction:\n" +
            "Distance should be greater than one.\n" +
            "Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n", gameLog.toString());

  }

  @Test
  public void testQuit() {
    StringReader input = new StringReader("M E Q");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player is at 1\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Game quit!", gameLog.toString());
  }

  @Test
  public void extraAfterGame() {
    StringReader input = new StringReader("M E Q M S");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player is at 1\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Game quit!", gameLog.toString());
  }

  @Test
  public void testExtraAfterEnd() {
    StringReader input = new StringReader("M E M S P Arrow M S");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n" +
            "Smell at the current cave is LOW\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "EAST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "\n" +
            "Player is at 1\n" +
            "Smell at the current cave is HIGH\n" +
            "Arrows with player: 3\n" +
            "\n" +
            "The player can go to the following destinations :\n" +
            "SOUTH\n" +
            "WEST\n" +
            "What do you want to do next ? M/P/S\n" +
            "Enter the direction:\n" +
            "Oops there was a monster here. Eating the player chomp chomp chomp!!\n" +
            "\n" +
            "Player is dead!!", gameLog.toString());
  }

  @Test
  public void testAllDir() {
    StringReader input = new StringReader("M E M W M E S 1 S S 1 S M S M N P Arrow M S");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows with player: 3\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "\n"
            + "Player is at 1\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows with player: 3\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "\n"
            + "Player is at 0\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows with player: 3\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "\n"
            + "Player is at 1\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows with player: 3\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Player has injured the monster.\n"
            + "\n"
            + "Player is at 1\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows with player: 2\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Player has killed the monster.\n"
            + "\n"
            + "Player is at 1\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows with player: 1\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "\n"
            + "Player is at 7\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows with player: 1\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "NORTH\n"
            + "SOUTH\n"
            + "EAST\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "\n"
            + "Player is at 1\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows with player: 1\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "What?\n"
            + "Player picked the arrow.\n"
            + "\n"
            + "Player is at 1\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows with player: 4\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "\n"
            + "Player is at 7\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows with player: 4\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "NORTH\n"
            + "SOUTH\n"
            + "EAST\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n", gameLog.toString());
  }


}
