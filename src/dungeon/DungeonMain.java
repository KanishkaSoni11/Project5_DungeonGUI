package dungeon;

import java.io.InputStreamReader;

import control.DungeonController;
import randomiser.ActualRandomiser;
import randomiser.FixedRandomiser;


/**
 * Driver class to run the main method for the Dungeon maze implementation.
 */
public class DungeonMain {
  /**
   * Main method to call all the runs.
   *
   * @param args arguments
   */

  public static void main(String[] args) {
    try {
      if (args.length < 5) {
        throw new IllegalArgumentException("Invalid command line arguments given. " +
                "Please provide correct arguments");
      }

      int rows = Integer.parseInt(args[0]);
      int columns = Integer.parseInt(args[1]);
      int interConnectivity = Integer.parseInt(args[2]);
      boolean wrapping = Boolean.parseBoolean(args[3]);
      int treasurePercent = Integer.parseInt(args[4]);
      int numberOfMonster = Integer.parseInt(args[5]);

      Dungeon dungeon = new DungeonImpl(rows, columns, interConnectivity, wrapping
              , treasurePercent, new FixedRandomiser(1,2), numberOfMonster);
      System.out.println(dungeon.getDungeon());
      System.out.println("************Dungeon created**********");
      System.out.println("Start Cave " + dungeon.getStart().getCaveId());
      System.out.println("End Cave " + dungeon.getEnd().getCaveId());
      System.out.println("-------Game Begins---------");


      Readable input = new InputStreamReader(System.in);
      Appendable output = System.out;
      new DungeonController(input, output).play(dungeon);

    } catch (IllegalArgumentException exception) {
      System.out.println(exception.getMessage());
    }


  }
}