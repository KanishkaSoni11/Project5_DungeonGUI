package dungeon;

import java.util.ArrayList;
import java.util.List;

import location.Location;
import location.Smell;
import player.Player;
import randomiser.Randomiser;

public interface ReadOnlyDungeonModel {

  int getRow();

  int getColumns();

  int getInterconnectivity();

  boolean getWrapping();

  int getNumberOfMonsters();

  float getTreasurePercent();

  /**
   * Method to get the details of the player.
   *
   * @return Player
   */
  Player getPlayer();

  /**
   * Method to get a list of all the caves that are present in the dungeon.
   *
   * @return list of caves
   */
  List<Location> getListOfCaves();

  /**
   * Method to get the degree of smell in a particular cave.
   *
   * @return Smell Type in the cave
   */
  Smell getSmell();

  boolean getPit(Location cave);


  /**
   * Method to get the start cave of the maze.
   *
   * @return start cave
   */
  Location getStart();

  /**
   * Method to test if the player has reached the final end cave or not.
   *
   * @return true or false based on the location of the player
   */
  boolean hasReached();

  boolean hasFallenIntoPit();

  boolean getSoil();

  /**
   * Method  to get the description of the player which includes name, current location,
   * all the possible locations from the current one and the treasures with the player.
   *
   * @return Description of the player in string format
   */
  String getPlayerDesc();

  String getLocationDesc();

  Randomiser getRandomiser();

  List<Edge> getEdge();

  List<Edge> getMstList();

  int getFinalPercent();

  Dungeon getDungeonCopy();

  ArrayList<Location> getCaveFinalList() ;

  /**
   * Method to get the end cave of the maze.
   *
   * @return end cave
   */
  Location getEnd();

}
