package dungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import location.Cave;
import location.LocationMonster;
import location.Direction;
import location.Location;
import location.LocationType;
import location.Smell;
import location.Treasure;
import player.Player;
import player.PlayerImp;
import randomiser.Randomiser;

/**
 * DungeonImpl is an implementation of the Dungeon which forms the maze based on the rows,
 * columns, interconnectivity, wrapping and treasure percentage provided. It forms the dungeon,
 * assigns the treasures to the caves and then player moves in directions based on the location.
 */
public class DungeonImpl implements Dungeon {
  private Location start;
  private Location end;
  private List<Location> listOfCaves;
  private final int interconnectivity;
  private final int rows;
  private final int columns;
  private final Player player;
  private final boolean wrapping;
  private List<Edge> edge;
  private List<Edge> mstList;
  private final Randomiser randomiser;
  private final int finalPercent;
  private final int numberOfMonsters;
  private int movingMonsterCaveId;
  private boolean movingMonsterIsAlive;
  private final float treasurePercent;
  private ArrayList<Location> cavesFinalList;


  /**
   * Constructs the dungeon based on the rows,
   * columns, interconnectivity, wrapping and treasure percentage provided.
   *
   * @param rows              number of rows in the dungeon
   * @param columns           number of columns in the dungeon
   * @param interconnectivity interconnectivity of the dungeon
   * @param wrapping          if the dungeon has to be wrapped or not
   * @param treasurePercent   minimum percentage of caves having treasures
   * @param randomiser        object of randomiser to generate the random number
   * @throws IllegalArgumentException if the rows are less than zero
   * @throws IllegalArgumentException if the columns are less than zero
   * @throws IllegalArgumentException if the interconnectivity is less than or equal to zero
   * @throws IllegalArgumentException if the treasure percentage is less than zero
   *                                  or greater than 100
   * @throws IllegalArgumentException if the randomiser is null
   */
  public DungeonImpl(int rows, int columns, int interconnectivity, boolean wrapping,
                     float treasurePercent, Randomiser randomiser, int numberOfMonsters)
          throws IllegalArgumentException {
    if (rows <= 0) {
      throw new IllegalArgumentException("The number of rows entered is invalid.");
    }
    if (columns <= 0) {
      throw new IllegalArgumentException("The number of columns entered is invalid.");
    }
    if (interconnectivity < 0) {
      throw new IllegalArgumentException("Interconnectivity entered is invalid");
    }
    if (treasurePercent < 0 || treasurePercent > 100) {
      throw new IllegalArgumentException("Treasure percent should be between 0 and 100");
    }
    if (randomiser == null) {
      throw new IllegalArgumentException("Randomiser cannot be null.");
    }
    if (numberOfMonsters < 1) {
      throw new IllegalArgumentException("Number of Otyugh is invalid");
    }

    this.rows = rows;
    this.columns = columns;
    this.interconnectivity = interconnectivity;
    this.wrapping = wrapping;
    this.randomiser = randomiser;
    this.numberOfMonsters = numberOfMonsters;
    this.treasurePercent = treasurePercent;
    this.listOfCaves = new ArrayList<Location>();
    this.edge = new ArrayList<Edge>();
    this.mstList = new ArrayList<Edge>();
    this.finalPercent = (int) Math.ceil(treasurePercent
            + randomiser.getRandom(0, (int) (100 - treasurePercent)));
    this.movingMonsterCaveId = 0;
    this.movingMonsterIsAlive = this.numberOfMonsters > 5;
    this.cavesFinalList = new ArrayList<>();
    createDungeon();
    createListOfCaves();
    addAdjacentCaves();
    findPossibleMoves();
    putTreasure();
    putArrows();
    putMonster();
    if (this.interconnectivity > 2) {
      putPit();
      putThief();
    }

    createCavesFinalList();
    this.player = new PlayerImp("Harry", start);
  }

  public DungeonImpl(ReadOnlyDungeonModel dungeon) throws IllegalArgumentException {
    if (dungeon == null) {
      throw new IllegalArgumentException("Dungeon cannot be null");
    }

    this.rows = dungeon.getRow();
    this.columns = dungeon.getColumns();
    this.interconnectivity = dungeon.getInterconnectivity();
    this.wrapping = dungeon.getWrapping();
    this.randomiser = dungeon.getRandomiser();
    this.numberOfMonsters = dungeon.getNumberOfMonsters();
    this.treasurePercent = dungeon.getTreasurePercent();
    this.finalPercent = dungeon.getFinalPercent();
    this.cavesFinalList = dungeon.getCaveFinalList();
    this.listOfCaves = dungeon.getListOfCaves();
    this.movingMonsterCaveId = 0;
    this.movingMonsterIsAlive = true;

    for (int i = 0; i < cavesFinalList.size(); i++) {
      if (cavesFinalList.get(i).getArrow() != listOfCaves.get(i).getArrow()) {
        this.listOfCaves.get(i).addArrow(dungeon.getCaveFinalList().get(i).getArrow());
      }

      if (!cavesFinalList.get(i).getTreasureList().equals(listOfCaves.get(i).getTreasureList())) {
        for (int j = 0; j < dungeon.getCaveFinalList().get(i).getTreasureList().size(); j++) {
          this.listOfCaves.get(i).addTreasureToCave(dungeon.getCaveFinalList().get(i).getTreasureList().get(j));
        }
      }

      if (this.cavesFinalList.get(i).hasMonster()) {
        this.listOfCaves.get(i).addMonster();
      }

      if (this.cavesFinalList.get(i).getThief()) {
        this.listOfCaves.get(i).addThief();
      }

      if (this.cavesFinalList.get(i).hasMovingMonster()) {
        this.listOfCaves.get(i).addMovingMonster();
      }
      if (this.cavesFinalList.get(i).getPit()) {
        this.listOfCaves.get(i).setPit();
      }
    }

    for (int i = 0; i < cavesFinalList.size(); i++) {
      if (dungeon.getStart().getCaveId() == i) {
        this.start = listOfCaves.get(i);
      } else if (dungeon.getEnd().getCaveId() == i) {
        this.end = listOfCaves.get(i);
      }
    }

    this.player = new PlayerImp("Harry", this.start);

  }

  @Override
  public Player getPlayer() {
    return new PlayerImp(player);
  }

  @Override
  public Location getEnd() {
    return new Cave(this.end);
  }

  @Override
  public Location getStart() {
    return new Cave(this.start);
  }

  @Override
  public List<Location> getListOfCaves() {
    return new ArrayList<>(this.listOfCaves);
  }

  @Override
  public boolean hasReached() {
    return (player.getPlayerCave().getCaveId() == this.end.getCaveId());
  }

  @Override
  public boolean hasFallenIntoPit() {
    return player.getPlayerCave().getPit();
  }

  @Override
  public int getRow() {
    return this.rows;
  }

  @Override
  public int getColumns() {
    return this.columns;
  }

  @Override
  public int getInterconnectivity() {
    return this.interconnectivity;
  }

  @Override
  public boolean getWrapping() {
    return this.wrapping;
  }

  @Override
  public int getNumberOfMonsters() {
    return this.numberOfMonsters;
  }

  @Override
  public float getTreasurePercent() {
    return this.treasurePercent;
  }

  @Override
  public int getFinalPercent() {
    return this.finalPercent;
  }

  @Override
  public ArrayList<Location> getCaveFinalList() {
    return new ArrayList<>(cavesFinalList);
  }

  @Override
  public String playerToPickArrow() {
    StringBuilder sb = new StringBuilder();
    if (player.getPlayerCave().getArrow() > 0 && player.getPlayerLive()) {
      player.pickArrow();
      player.removeArrow(player.getPlayerCave());
      sb.append("Player picked the arrow.").append("\n");
    } else if (!player.getPlayerLive()) {
      sb.append("Player is dead. Can't pick arrows");
    } else {
      sb.append("There are no arrows here").append("\n");
    }
    return sb.toString();
  }

  private void createDungeon() {
    int[][] dungeonArr = new int[rows][columns];
    int count = 0;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        dungeonArr[i][j] = count;
        count++;
      }
    }

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns - 1; j++) {
        edge.add(new Edge(dungeonArr[i][j], dungeonArr[i][j + 1], randomiser.getRandom(0, 5)));
      }
    }

    for (int i = 0; i < rows - 1; i++) {
      for (int j = 0; j < columns; j++) {
        edge.add(new Edge(dungeonArr[i][j], dungeonArr[i + 1][j], randomiser.getRandom(0, 5)));
      }
    }

    if (wrapping) {
      DungeonWrapped dungeonWrapped = new DungeonWrapped(edge, dungeonArr, rows, columns, randomiser);
      edge = dungeonWrapped.makeWrappedDungeon();
    }

    DungeonCreation dungeonCreation = new DungeonCreation(edge, rows * columns);
    mstList = dungeonCreation.kruskalAlgo();
    edge.removeAll(mstList);

    randomiser.randomList(edge);

    if (edge.size() < interconnectivity) {
      throw new IllegalArgumentException("The given interconnectivity is wrong");
    }

    for (int i = 0; i < interconnectivity; i++) {
      mstList.add(edge.get(i));
    }

  }

  private void createListOfCaves() {
    int caveId = 0;
    for (int i = 0; i < rows * columns; i++) {
      listOfCaves.add(new Cave(caveId));
      caveId++;
    }
  }

  private void addAdjacentCaves() {
    Direction directions = null;
    Direction oppDirection = null;
    for (int i = 0; i < mstList.size(); i++) {
      for (int j = 0; j < listOfCaves.size(); j++) {
        if (listOfCaves.get(j).getCaveId() == (mstList.get(i).getSrc())) {
          int diff = Math.abs(mstList.get(i).getDest() - listOfCaves.get(j).getCaveId());
          if (diff == 1) {
            directions = Direction.EAST;
            oppDirection = Direction.WEST;

            listOfCaves.get(j).addCaveList(directions, listOfCaves.get(mstList.get(i).getDest()));
            listOfCaves.get(mstList.get(i).getDest()).addCaveList(oppDirection
                    , listOfCaves.get(j));

          } else if (diff == Math.abs(columns - 1)) {
            directions = Direction.WEST;
            oppDirection = Direction.EAST;

            listOfCaves.get(j).addCaveList(directions, listOfCaves.get(mstList.get(i).getDest()));
            listOfCaves.get(mstList.get(i).getDest()).addCaveList(oppDirection
                    , listOfCaves.get(j));

          } else if (diff == columns) {
            directions = Direction.SOUTH;
            oppDirection = Direction.NORTH;

            listOfCaves.get(j).addCaveList(directions, listOfCaves.get(mstList.get(i).getDest()));
            listOfCaves.get(mstList.get(i).getDest()).addCaveList(oppDirection
                    , listOfCaves.get(j));

          } else if (diff > columns) {
            directions = Direction.NORTH;
            oppDirection = Direction.SOUTH;

            listOfCaves.get(j).addCaveList(directions, listOfCaves.get(mstList.get(i).getDest()));
            listOfCaves.get(mstList.get(i).getDest()).addCaveList(oppDirection
                    , listOfCaves.get(j));

          }
        }
      }
    }
  }

  private List<Location> getRandomCaveList() {
    List<Location> listOfCavesCopy = new ArrayList<Location>();

    for (int i = 0; i < listOfCaves.size(); i++) {
      if (listOfCaves.get(i).getLocationType().equals(LocationType.CAVE)) {
        listOfCavesCopy.add(listOfCaves.get(i));
      }
    }
    randomiser.randomList(listOfCavesCopy);
    return new ArrayList<>(listOfCavesCopy);
  }


  private void putTreasure() {
    Treasure[] treasureList = Treasure.values();
    List<Location> treasureCave = new ArrayList<Location>();
    List<Location> listOfCavesCopy = getRandomCaveList();

    int loopNum = (int) (finalPercent * (listOfCavesCopy.size() / 100.0));
    for (int i = 0; i < loopNum; i++) {
      treasureCave.add(listOfCavesCopy.get(i));
      for (int j = 0; j < treasureList.length; j++) {
        treasureCave.get(i).addTreasureToCave(treasureList[j]);
      }
    }
  }


  private void putArrows() {
    int loopNum = (int) (finalPercent * listOfCaves.size() / 100.0);

    List<Location> listOfCavesCopy = new ArrayList<Location>(listOfCaves);

    randomiser.randomList(listOfCavesCopy);

    for (int i = 0; i < loopNum; i++) {
      listOfCavesCopy.get(i).addArrow(randomiser.getRandom(1, 3));
    }
  }

  private void putMonster() {
    end.addMonster();
    List<Location> listOfCavesCopy = getRandomCaveList();

    if (numberOfMonsters - 1 > listOfCavesCopy.size()) {
      throw new IllegalArgumentException("The number of monsters should be less "
              + "than number of caves");
    }

    listOfCavesCopy.remove(start);
    listOfCavesCopy.remove(end);

    for (int i = 0; i < this.numberOfMonsters - 1; i++) {
      listOfCavesCopy.get(i).addMonster();
    }
  }

  @Override
  public String treasureDesc() {
    StringBuilder sb = new StringBuilder();
    if (isTreasure()) {
      sb.append("Treasures in the destinations: ").append("\n");
      for (int i = 0; i < player.getPlayerCave().getTreasureList().size(); i++) {
        Collections.sort(player.getPlayerCave().getTreasureList());
        sb.append(" " + player.getPlayerCave().getTreasureList().get(i).toString());
      }
    }
    return sb.toString();
  }

  @Override
  public Smell getSmell() {
    int numberOfMonsters = 0;
    Location cave;

    for (Map.Entry<Direction, Location> set : player.getPlayerCave().getCaveList().entrySet()) {
      if (set.getValue().hasMonster()) {
        return Smell.HIGH;
      } else {
        cave = set.getValue();
        for (Map.Entry<Direction, Location> setNeighbour : cave.getCaveList().entrySet()) {
          if (setNeighbour.getValue().hasMonster()) {
            numberOfMonsters++;
          }
        }
      }
    }

    if (numberOfMonsters >= 2) {
      return Smell.HIGH;
    } else if (numberOfMonsters == 1) {
      return Smell.LOW;
    }
    return Smell.NONE;
  }

  public boolean getSoil() {
    for (Map.Entry<Direction, Location> set : player.getPlayerCave().getCaveList().entrySet()) {
      if (set.getValue().getPit()) {
        return true;
      }
    }
    return false;
  }


  private void putPit() {
    List<Location> listOfCavesCopy = getRandomCaveList();

    listOfCavesCopy.remove(start);

    for (int i = 0; i < listOfCavesCopy.size(); i++) {
      if (!listOfCavesCopy.get(i).hasMonster()) {
        System.out.println(listOfCavesCopy.get(i).getCaveId());
        listOfCavesCopy.get(i).setPit();
        break;
      }
    }
  }

  @Override
  public Randomiser getRandomiser() {
    return this.randomiser;
  }

  private void putThief() {
    List<Location> listOfCavesCopy = new ArrayList<Location>();

    for (Location listOfCave : listOfCaves) {
      if (listOfCave.getLocationType().equals(LocationType.CAVE)) {
        listOfCavesCopy.add(listOfCave);
      }
    }

    randomiser.randomList(listOfCavesCopy);
    listOfCavesCopy.remove(start);

    for (int i = 0; i < listOfCavesCopy.size(); i++) {
      if (!listOfCavesCopy.get(i).hasMonster() && !listOfCavesCopy.get(i).getPit()) {
        listOfCavesCopy.get(i).addThief();
        break;
      }
    }
  }

  @Override
  public boolean getPit(Location cave) {
    for (Map.Entry<Direction, Location> set : cave.getCaveList().entrySet()) {
      if (set.getValue().getPit()) {
        return true;
      }
    }
    return false;
  }


  /**
   * code referred from:
   * https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/
   */

  @Override
  public ArrayList<Location> endUsingBfs(Location src) {
    if (src == null) {
      throw new IllegalArgumentException("Source Location cannot be null");
    }
    boolean[] isVisited = new boolean[listOfCaves.size()];
    LinkedList<Location> queue = new LinkedList<Location>();
    List<Location> arr = new ArrayList<>();
    isVisited[src.getCaveId()] = true;

    int level = 0;
    queue.add(src);

    while (queue.size() != 0) {
      src = queue.poll();

      for (Location n : new ArrayList<Location>(src.getCaveList().values())) {
        if (!isVisited[n.getCaveId()]) {
          isVisited[src.getCaveId()] = true;
          queue.add(n);
          level++;
          if (level > 6) {
            arr.add(n);
          }
        }
      }
    }

    for (Location i : listOfCaves) {
      isVisited[i.getCaveId()] = false;
    }

    return new ArrayList<>(arr);
  }

  private void findPossibleMoves() {

    List<Location> listForStart = getRandomCaveList();

    for (Location i : listForStart) {
      this.start = i;
      ArrayList<Location> findEnd = endUsingBfs(this.start);
      this.end = null;

      if (findEnd.size() > 0) {
        randomiser.randomList(findEnd);
        for (int j = 0; j < findEnd.size(); j++) {
          if (findEnd.get(j).getLocationType() == LocationType.CAVE) {
            this.end = findEnd.get(j);
            break;
          }
        }
      }
    }

    if (this.end == null) {
      throw new IllegalArgumentException("The matrix size is not valid, no start end possible!");
    }
  }

  @Override
  public String shootArrow(String distance, Direction direction) throws IllegalArgumentException {
    StringBuilder sb = new StringBuilder();
    try {
      int dis = Integer.parseInt(distance);
      if (dis <= 0) {
        throw new IllegalArgumentException("Distance should be greater than one.");
      }
      if (dis > 5) {
        sb.append("The distance cannot be greater than 5");
      }
      if (direction == null) {
        throw new IllegalArgumentException("Direction cannot be negative");
      }

      Location caveForArrow = player.getPlayerCave();
      Direction oppDir = null;

      player.shootArrow();

      if (player.getPlayerCave().getCaveList().containsKey(direction)) {
        caveForArrow = player.getPlayerCave().getCaveList().get(direction);
        while (dis > 0) {
          if (caveForArrow.getLocationType() == LocationType.CAVE) {
            dis--;
            if (dis == 0) {
              break;
            }
            caveForArrow = caveForArrow.getCaveList().get(direction);
          } else if (caveForArrow.getLocationType() == LocationType.TUNNEL) {
            for (Map.Entry<Direction, Location> entry : caveForArrow.getCaveList().entrySet()) {
              if (direction == Direction.NORTH) {
                oppDir = Direction.SOUTH;
              } else if (direction == Direction.SOUTH) {
                oppDir = Direction.NORTH;
              } else if (direction == Direction.EAST) {
                oppDir = Direction.WEST;
              } else if (direction == Direction.WEST) {
                oppDir = Direction.EAST;
              }
              if (entry.getKey() != oppDir) {
                caveForArrow = entry.getValue();
                direction = entry.getKey();

                if (caveForArrow.getLocationType() == LocationType.CAVE) {
                  dis--;
                }
                break;
              }
            }
          } else {
            break;
          }
        }
      }

      if (caveForArrow.hasMonster() && dis == 0) {
        caveForArrow.hitMonster();
        if (caveForArrow.hasMonster()) {
          sb.append("Player has injured the monster.").append("\n");
          return sb.toString();
        } else {
          sb.append("Player has killed the monster.").append("\n");
          return sb.toString();
        }
      } else if (dis != 0) {
        sb.append("Player shot into into the darkness.").append("\n");
        return sb.toString();
      }
      sb.append("Player shot into into the darkness.").append("\n");
      return sb.toString();
    } catch (NumberFormatException ex) {
      throw new NumberFormatException("Distance has to be number");
    }

  }

  @Override
  public String maze(String move) throws IllegalArgumentException {
    if (move == null) {
      throw new IllegalArgumentException("Move cannot be null");
    }
    boolean correctMove;
    StringBuilder sb = new StringBuilder();
    if (player.getPlayerLive() && !hasReached()) {
      if (move.equalsIgnoreCase("n")) {
        correctMove = player.move(Direction.NORTH);
      } else if (move.equalsIgnoreCase("s")) {
        correctMove = player.move(Direction.SOUTH);

      } else if (move.equalsIgnoreCase("e")) {
        correctMove = player.move(Direction.EAST);

      } else if (move.equalsIgnoreCase("w")) {
        correctMove = player.move(Direction.WEST);

      } else {

        throw new IllegalArgumentException("The entered direction was invalid");
      }

      if (player.getPlayerCave().getThief()) {
        player.removeTreausuresWithPlayer();
        sb.append("The player encountered a thief and lost all it's treasures!! :(").append("\n");
      }

      if (hasFallenIntoPit()) {
        player.killPlayer();
      }

      if (!correctMove) {
        sb.append("The entered direction was invalid").append("\n");
        return sb.toString();
      } else if (correctMove) {
        sb.append("Player has moved successfully to ").append(player.getPlayerCave().getCaveId()).append("\n");
        if (this.getListOfCaves().get(movingMonsterCaveId).hasMovingMonster()) {
          this.getListOfCaves().get(movingMonsterCaveId).removeMovingMonster();
        }
        if (movingMonsterIsAlive) {
          movingMonsterLoc();
        }

        checkMonster(sb);
        if (movingMonsterIsAlive) {
          monsterCombat(sb);
        }
      } else {
        sb.append("Player is dead! Can't Move");
      }
    }
    return sb.toString();
  }

  private void movingMonsterLoc() {
    List<Location> listOfCavesMonster = new ArrayList<Location>();

    for (Location listOfCave : listOfCaves) {
      if (listOfCave.getLocationType().equals(LocationType.CAVE) && !listOfCave.hasMonster()) {
        listOfCavesMonster.add(listOfCave);
      }
    }
    randomiser.randomList(listOfCavesMonster);
    movingMonsterCaveId = randomiser.getRandom(0, listOfCavesMonster.size() - 1);
    listOfCavesMonster.get(movingMonsterCaveId).addMovingMonster();
    movingMonsterCaveId = listOfCavesMonster.get(movingMonsterCaveId).getCaveId();

  }

  private void monsterCombat(StringBuilder sb) {
    if (player.getPlayerCave().hasMovingMonster()) {
      sb.append("Player has to fight the moving monster!");
      LocationMonster movingMonster = player.getPlayerCave().getMovingMonster();
      while (player.getHealth() > 0 && movingMonster.getHealth() > 0) {
        int random = this.randomiser.getRandom(0, 1) % 2;
        if (random == 0) {
          movingMonster.injure();
          sb.append("Player has injured the monster. Monster left with health: ").append(movingMonster.getHealth()).append("\n");

        } else {
          player.injure();
          sb.append("Player has been injured. Remaining health:").append(player.getHealth()).append("\n");
        }
      }
      if (player.getHealth() <= 0) {
        player.killPlayer();
        sb.append("The moving monster has attacked the player and won the combat!!").append("\n");
      } else if (movingMonster.getHealth() <= 0) {
        movingMonsterIsAlive = false;
        sb.append("Moving Monster has been shot to death in the combat!!").append("\n");
      }
    }
  }

  private String checkMonster(StringBuilder sb) {
    if (player.getPlayerCave().hasMonster()) {
      if (player.getPlayerCave().getMonster().getHealth() == 100) {
        player.killPlayer();
        sb.append("Oops there was a monster here. Eating the player chomp chomp chomp!!")
                .append("\n");

      } else if (player.getPlayerCave().getMonster().getHealth() == 50) {
        if (randomiser.getRandom(0, 1) % 2 == 1) {
          player.killPlayer();
          sb.append("Oops there was a monster here. Eating the player chomp chomp chomp!!")
                  .append("\n");
        }
        sb.append("The player has escaped!").append("\n");
      }
    }
    return sb.toString();
  }

  @Override
  public String playerToPickTreasure() {
    StringBuilder sb = new StringBuilder();
    if (player.getPlayerCave().getTreasureList().size() > 0 && player.getPlayerLive()) {
      player.pickTreasure(player.getPlayerCave().getTreasureList());
      player.removeTreasure(player.getPlayerCave());
      sb.append("Player picked the treasures.").append("\n");
    } else if (!player.getPlayerLive()) {
      sb.append("Player is dead. Can't pick treasures.");
    } else {
      sb.append("There are no treasures here").append("\n");
    }
    return sb.toString();
  }

  public void createCavesFinalList() {
    for (int i = 0; i < listOfCaves.size(); i++) {
      cavesFinalList.add(new Cave(listOfCaves.get(i)));
    }
  }

  @Override
  public String getPlayerDesc() {
    StringBuilder sb = new StringBuilder();
    sb.append("Player is at ").append(player.getPlayerCave().getCaveId()).append("\n");

    if (player.getTreasureList().size() > 0) {
      sb.append("\nPlayer has the following treasures :").append("\n");
      for (int i = 0; i < player.getTreasureList().size(); i++) {
        sb.append(player.getTreasureList().get(i).toString()).append("\n");
      }
    }

    sb.append("Arrows with player: ").append(player.getArrowCount()).append("\n");
    return sb.toString();
  }

  public String getLocationDesc() {
    StringBuilder sb = new StringBuilder();
    if (this.getSmell() != Smell.NONE) {
      sb.append("Smell at the current cave is ").append(this.getSmell().toString()).append("\n");
    }

    if (this.getSoil()) {
      sb.append("There seems to be some soil nearby! Be careful!").append("\n");
    }

    if (player.getPlayerCave().getArrow() > 0) {
      sb.append("Arrows are present in this cave.").append("\n");
    }

    if (player.getPlayerCave().getTreasureList().size() > 0) {
      sb.append("Wow this cave has treasures.").append("\n");
      for (int i = 0; i < player.getPlayerCave().getTreasureList().size(); i++) {
        sb.append(player.getPlayerCave().getTreasureList().get(i).toString()).append("\n");
      }
    }

    if (player.getPlayerCave().getThief()) {
      sb.append("There is a thief in the cave, run!").append("\n");
    }

    sb.append("\nThe player can go to the following destinations :").append("\n");
    for (Map.Entry<Direction, Location> set : player.getPlayerCave().getCaveList().entrySet()) {
      sb.append(set.getKey().toString()).append("\n");
    }

    return sb.toString();
  }

  @Override
  public boolean isTreasure() {
    return (player.getPlayerCave().getTreasureList().size() > 0);
  }

  @Override
  public String getDungeon() {
    StringBuilder sb = new StringBuilder();
    sb.append("----------Dungeon----------");

    for (int i = 0; i < listOfCaves.size(); i++) {
      sb.append("\n");
      if (listOfCaves.get(i).getLocationType() == LocationType.CAVE) {
        sb.append("\nCave").append(listOfCaves.get(i).getCaveId());
        sb.append("\nThis cave is connected to the :");
        for (Map.Entry<Direction, Location> entry : listOfCaves.get(i).getCaveList().entrySet()) {
          Direction k = entry.getKey();
          Location v = entry.getValue();
          sb.append("\n").append(k);
          if (v.getLocationType() == LocationType.CAVE) {
            sb.append(" Cave").append(v.getCaveId());
          } else {
            sb.append(" Tunnel").append(v.getCaveId());
          }
        }

      } else {
        sb.append("\nTunnel").append(listOfCaves.get(i).getCaveId());
        sb.append("\nThis tunnel is connected to : ");
        for (Map.Entry<Direction, Location> entry : listOfCaves.get(i).getCaveList().entrySet()) {
          Direction k = entry.getKey();
          Location v = entry.getValue();
          sb.append("\n").append(k);
          if (v.getLocationType() == LocationType.CAVE) {
            sb.append(" Cave").append(v.getCaveId());
          } else {
            sb.append(" Tunnel").append(v.getCaveId());
          }

        }

      }

      if (listOfCaves.get(i).hasMonster()) {
        sb.append("\nThere is a monster in the here");
      }
      if (listOfCaves.get(i).getArrow() > 0) {
        sb.append("\nArrows = ").append(listOfCaves.get(i).getArrow());
      }
    }
    return sb.toString();
  }

}





