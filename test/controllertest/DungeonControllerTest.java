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
import randomiser.ActualRandomiser;
import randomiser.FixedRandomiser;
import view.DungeonGridView;
import view.DungeonView;

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
    DungeonCommandController shoot = new ShootArrow("1", Direction.EAST);
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
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 1\n"
            + "\n"
            + "Player is at 1\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testPickUpTreasure() {
    StringReader input = new StringReader("P Treasure");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "What?\n"
            + "Treasures in the destinations: \n"
            + " SAPPHIRE DIAMOND RUBY\n"
            + "Player picked the treasures.\n"
            + "\n"
            + "Player is at 0\n"
            + "\n"
            + "Player has the following treasures :\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testPickUpArrow() {
    StringReader input = new StringReader("P Arrow");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "SAPPHIRE\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Player has injured the monster.\n"
            + "\n"
            + "Player is at 0\n"
            + "Arrows with player: 2\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "SAPPHIRE\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testShootArrow() {
    StringReader input = new StringReader("S 1 E");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "SAPPHIRE\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Player has injured the monster.\n"
            + "\n"
            + "Player is at 0\n"
            + "Arrows with player: 2\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "SAPPHIRE\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testMoveInvalid() {
    StringReader input = new StringReader("M A");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "The entered direction was invalid\n"
            + "Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testInvalidPick() {
    StringReader input = new StringReader("P R");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "SAPPHIRE\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "What?\n"
            + "Invalid entry!!\n"
            + "Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "SAPPHIRE\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n", gameLog.toString());
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
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 1\n"
            + "\n"
            + "Player is at 1\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 7\n"
            + "Oops there was a monster here. Eating the player chomp chomp chomp!!\n"
            + "\n"
            + "Player is dead!!", gameLog.toString());
  }

  @Test
  public void testShotDarkness() {
    StringReader input = new StringReader("M E S 1 E");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "SAPPHIRE\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 1\n"
            + "\n"
            + "Player is at 1\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Player shot into into the darkness.\n"
            + "\n"
            + "Player is at 1\n"
            + "Arrows with player: 2\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testPlayerEscaped() {
    StringReader input = new StringReader("M E S 1 S M S M S");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 1\n"
            + "\n"
            + "Player is at 1\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
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
            + "Arrows with player: 2\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 7\n"
            + "Oops there was a monster here. Eating the player chomp chomp chomp!!\n"
            + "The player has escaped!\n"
            + "\n"
            + "Player is dead!!", gameLog.toString());

  }

  @Test
  public void testCommandInvalid() {
    StringReader input = new StringReader("A");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Unknown command A\n"
            + "Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n", gameLog.toString());
  }

  @Test
  public void testShootNoArrow() {
    StringReader input = new StringReader("S 1 E S 1 E S 1 E S 1 E");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Player has injured the monster.\n"
            + "\n"
            + "Player is at 0\n"
            + "Arrows with player: 2\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Player has killed the monster.\n"
            + "\n"
            + "Player is at 0\n"
            + "Arrows with player: 1\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Player shot into into the darkness.\n"
            + "\n"
            + "Player is at 0\n"
            + "Arrows with player: 0\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "\n"
            + "Player is at 0\n"
            + "Arrows with player: 0\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n", gameLog.toString());
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

    assertEquals("Player is at 3\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "RUBY\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "NORTH\n"
            + "EAST\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "What?\n"
            + "Player picked the arrow.\n"
            + "\n"
            + "Player is at 3\n"
            + "Arrows with player: 7\n"
            + "Smell at the current cave is HIGH\n"
            + "Wow this cave has treasures.\n"
            + "RUBY\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "NORTH\n"
            + "EAST\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 2\n"
            + "\n"
            + "Player is at 2\n"
            + "Arrows with player: 7\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Player has injured the monster.\n"
            + "\n"
            + "Player is at 2\n"
            + "Arrows with player: 6\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Player has killed the monster.\n"
            + "\n"
            + "Player is at 2\n"
            + "Arrows with player: 5\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 7\n"
            + "\n"
            + "Player is at 7\n"
            + "Arrows with player: 5\n"
            + "Smell at the current cave is HIGH\n"
            + "Wow this cave has treasures.\n"
            + "RUBY\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "NORTH\n"
            + "SOUTH\n"
            + "EAST\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 12\n"
            + "\n"
            + "Player is at 12\n"
            + "Arrows with player: 5\n"
            + "Smell at the current cave is HIGH\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "NORTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "The entered direction was invalid\n"
            + "\n"
            + "Player is at 12\n"
            + "Arrows with player: 5\n"
            + "Smell at the current cave is HIGH\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "NORTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Player has injured the monster.\n"
            + "\n"
            + "Player is at 12\n"
            + "Arrows with player: 4\n"
            + "Smell at the current cave is HIGH\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "NORTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Player has killed the monster.\n"
            + "\n"
            + "Player is at 12\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "NORTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 11\n"
            + "Player has to fight the moving monster!Player has been injured. Remaining health:50\n"
            + "Player has injured the monster. Monster left with health: 50\n"
            + "Player has injured the monster. Monster left with health: 0\n"
            + "Moving Monster has been shot to death in the combat!!\n"
            + "\n"
            + "Player is at 11\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is HIGH\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "EAST\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 16\n"
            + "\n"
            + "Player is at 16\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is HIGH\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "NORTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Player has injured the monster.\n"
            + "\n"
            + "Player is at 16\n"
            + "Arrows with player: 2\n"
            + "Smell at the current cave is HIGH\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "NORTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Player has killed the monster.\n"
            + "\n"
            + "Player is at 16\n"
            + "Arrows with player: 1\n"
            + "Smell at the current cave is LOW\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "NORTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 15\n"
            + "\n"
            + "Player has reached the final destination!!!!", gameLog.toString());
  }

  @Test
  public void testInvalidDis() {
    StringReader input = new StringReader("S 0 E S -1 E");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Distance should be greater than one.\n"
            + "Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter distance:\n"
            + "Enter the direction:\n"
            + "Distance should be greater than one.\n"
            + "Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n", gameLog.toString());

  }

  @Test
  public void testQuit() {
    StringReader input = new StringReader("M E Q");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 1\n"
            + "\n"
            + "Player is at 1\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Game quit!", gameLog.toString());
  }

  @Test
  public void extraAfterGame() {
    StringReader input = new StringReader("M E Q M S");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 1\n"
            + "\n"
            + "Player is at 1\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Game quit!", gameLog.toString());
  }

  @Test
  public void testExtraAfterEnd() {
    StringReader input = new StringReader("M E M S P Arrow M S");
    Appendable gameLog = new StringBuilder();
    IDungeonController controller = new DungeonController(input, gameLog);
    Dungeon dungeon = new DungeonImpl(5, 6, 2, true
            , 50, new FixedRandomiser(1, 2, 3), 5);
    controller.play(dungeon);
    assertEquals("Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 1\n"
            + "\n"
            + "Player is at 1\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 7\n"
            + "Oops there was a monster here. Eating the player chomp chomp chomp!!\n"
            + "\n"
            + "Player is dead!!", gameLog.toString());
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
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 1\n"
            + "\n"
            + "Player is at 1\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 0\n"
            + "\n"
            + "Player is at 0\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "EAST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 1\n"
            + "\n"
            + "Player is at 1\n"
            + "Arrows with player: 3\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
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
            + "Arrows with player: 2\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
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
            + "Arrows with player: 1\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 7\n"
            + "\n"
            + "Player is at 7\n"
            + "Arrows with player: 1\n"
            + "Smell at the current cave is HIGH\n"
            + "Arrows are present in this cave.\n"
            + "Wow this cave has treasures.\n"
            + "SAPPHIRE\n"
            + "DIAMOND\n"
            + "RUBY\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "NORTH\n"
            + "SOUTH\n"
            + "EAST\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 1\n"
            + "\n"
            + "Player is at 1\n"
            + "Arrows with player: 1\n"
            + "Smell at the current cave is LOW\n"
            + "Arrows are present in this cave.\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "What?\n"
            + "Player picked the arrow.\n"
            + "\n"
            + "Player is at 1\n"
            + "Arrows with player: 4\n"
            + "Smell at the current cave is LOW\n"
            + "\n"
            + "The player can go to the following destinations :\n"
            + "SOUTH\n"
            + "WEST\n"
            + "What do you want to do next ? M/P/S\n"
            + "Enter the direction:\n"
            + "Player has moved successfully to 7\n"
            + "Player has to fight the moving monster!Player has injured the monster. Monster left with health: 50\n"
            + "Player has been injured. Remaining health:50\n"
            + "Player has been injured. Remaining health:0\n"
            + "The moving monster has attacked the player and won the combat!!\n"
            + "\n"
            + "Player is dead!!", gameLog.toString());
  }

  //Project 5

  @Test
  public void testMockMove() {
    StringBuilder mockView = new StringBuilder();
    Dungeon model = new DungeonImpl(5, 6, 2, false, 50, new FixedRandomiser(1), 1);
    DungeonView view = new MockView(mockView);
    DungeonController controller = new DungeonController(model, view);
    controller.playView();
    controller.moveDir("E");
    assertEquals("Calling reset focus\n"
            + "Calling action listener\n"
            + "Calling reset focus\n"
            + "Calling action listener\n"
            + "Calling add click listener method\n"
            + "Calling make visible method\n"
            + "Calling set string method\n"
            + "Player has moved successfully to 2\n"
            + "Player has to fight the moving monster!Player has been injured. Remaining health:50\n"
            + "Player has been injured. Remaining health:0\n"
            + "The moving monster has attacked the player and won the combat!!\n"
            + "Showing cave Details\n"
            + "Calling reset focus\n", mockView.toString());
  }

  @Test
  public void testMockPickTreasure() {
    StringBuilder mockView = new StringBuilder();
    Dungeon model = new DungeonImpl(5, 6, 2, false, 50, new FixedRandomiser(1), 1);
    DungeonView view = new MockView(mockView);
    DungeonController controller = new DungeonController(model, view);
    controller.playView();
    controller.pickTreasure();
    assertEquals("Calling reset focus\n"
            + "Calling action listener\n"
            + "Calling reset focus\n"
            + "Calling action listener\n"
            + "Calling add click listener method\n"
            + "Calling make visible method\n"
            + "Calling set string method\n"
            + "Treasures in the destinations: \n"
            + " DIAMOND RUBY SAPPHIRE\n"
            + "Player picked the treasures.\n"
            + "Showing cave Details\n"
            + "Calling reset focus\n", mockView.toString());
  }

  @Test
  public void testMockPickArrow() {
    StringBuilder mockView = new StringBuilder();
    Dungeon model = new DungeonImpl(5, 6, 2, false, 50, new FixedRandomiser(1), 1);
    DungeonView view = new MockView(mockView);
    DungeonController controller = new DungeonController(model, view);
    controller.playView();
    controller.pickArrow();
    assertEquals("Calling reset focus\n"
            + "Calling action listener\n"
            + "Calling reset focus\n"
            + "Calling action listener\n"
            + "Calling add click listener method\n"
            + "Calling make visible method\n"
            + "Calling set string method\n"
            + "Player picked the arrow.\n"
            + "Showing cave Details\n"
            + "Calling reset focus\n", mockView.toString());
  }

  @Test
  public void testMockShoot() {
    StringBuilder mockView = new StringBuilder();
    Dungeon model = new DungeonImpl(5, 6, 2, false, 50, new FixedRandomiser(1), 1);
    DungeonView view = new MockView(mockView);
    DungeonController controller = new DungeonController(model, view);
    controller.playView();

    controller.moveDir("E");

    controller.shoot("1", Direction.EAST);

    assertEquals("Calling reset focus\n"
            + "Calling action listener\n"
            + "Calling reset focus\n"
            + "Calling action listener\n"
            + "Calling add click listener method\n"
            + "Calling make visible method\n"
            + "Calling set string method\n"
            + "Player has moved successfully to 7\n"
            + "Showing cave Details\n"
            + "Calling reset focus\n"
            + "Calling set string method\n"
            + "Player has moved successfully to 8\n"
            + "Showing cave Details\n"
            + "Calling reset focus\n"
            + "Calling set string method\n"
            + "Player has injured the monster.\n"
            + "Calling reset focus\n", mockView.toString());

    controller.shoot("1", Direction.EAST);

    assertEquals("Calling reset focus\n"
            + "Calling action listener\n"
            + "Calling reset focus\n"
            + "Calling action listener\n"
            + "Calling add click listener method\n"
            + "Calling make visible method\n"
            + "Calling set string method\n"
            + "Player has moved successfully to 7\n"
            + "Showing cave Details\n"
            + "Calling reset focus\n"
            + "Calling set string method\n"
            + "Player has moved successfully to 8\n"
            + "Showing cave Details\n"
            + "Calling reset focus\n"
            + "Calling set string method\n"
            + "Player has injured the monster.\n"
            + "Calling reset focus\n"
            + "Calling set string method\n"
            + "Player has killed the monster.\n"
            + "Calling reset focus\n", mockView.toString());
  }

  // Mock Model

  @Test
  public void testMockModelMove() {
    StringBuilder sb = new StringBuilder();
    StringBuilder gameLog = new StringBuilder();
    Dungeon model = new MockModel(sb);
    DungeonView view = new MockView(gameLog);
    IDungeonController controller = new DungeonController(model, view);
    controller.move("N");
    assertEquals("Moving the player", sb.toString());
    assertEquals("Calling reset focus\n"
            + "Calling action listener\n"
            + "Calling reset focus\n"
            + "Calling set string method\n"
            + "nullShowing cave Details\n"
            + "Calling reset focus\n", gameLog.toString());

  }

  @Test
  public void testMockModelPickArrow() {
    StringBuilder sb = new StringBuilder();
    StringBuilder gameLog = new StringBuilder();
    Dungeon model = new MockModel(sb);
    DungeonView view = new MockView(gameLog);
    IDungeonController controller = new DungeonController(model, view);
    controller.pickArrow();
    assertEquals("Player picking arrows", sb.toString());
    assertEquals("Calling reset focus\n"
            + "Calling action listener\n"
            + "Calling reset focus\n"
            + "Calling set string method\n"
            + "nullShowing cave Details\n"
            + "Calling reset focus\n", gameLog.toString());
  }

  @Test
  public void testMockModelPickTreasure() {
    StringBuilder sb = new StringBuilder();
    StringBuilder gameLog = new StringBuilder();
    Dungeon model = new MockModel(sb);
    DungeonView view = new MockView(gameLog);
    IDungeonController controller = new DungeonController(model, view);
    controller.pickTreasure();
    assertEquals("Getting treasure description\n"
            + "Player picking treasures\n", sb.toString());
    assertEquals("Calling reset focus\n"
            + "Calling action listener\n"
            + "Calling reset focus\n"
            + "Calling set string method\n"
            + "null\n"
            + "nullShowing cave Details\n"
            + "Calling reset focus\n", gameLog.toString());
  }

}
