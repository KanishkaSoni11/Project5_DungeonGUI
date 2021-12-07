package dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import location.Cave;
import location.CaveCreature;
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
  private Dungeon dungeonCopy;


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
      throw new IllegalArgumentException("Inter connectivity entered is invalid");
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
    this.movingMonsterIsAlive = true;
    this.cavesFinalList = new ArrayList<>();
    createDungeon();
    createListOfCaves();
    addAdjacentCaves();
    findPossibleMoves();
    putTreasure();
    putArrows();
    putMonster();
    putPit();
    putThief();
    createCavesFinalList();
    player = new PlayerImp("John", start);
    makeDungeonCopy();
  }

  public DungeonImpl(ReadOnlyDungeonModel dungeon) {

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
      this.listOfCaves.get(i).addArrow(dungeon.getCaveFinalList().get(i).getArrow());
      for (int j = 0; j < dungeon.getCaveFinalList().get(i).getTreasureList().size(); j++) {
        this.listOfCaves.get(i).addTreasureToCave(dungeon.getCaveFinalList().get(i).getTreasureList().get(j));
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
    }

    for (int i = 0; i < cavesFinalList.size(); i++) {
      if (dungeon.getStart().getCaveId() == i) {
        this.start = listOfCaves.get(i);
      } else if (dungeon.getEnd().getCaveId() == i) {
        this.end = listOfCaves.get(i);
      }
    }
    this.player = new PlayerImp("John", this.start);

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
    return (player.getCurrentCave().getCaveId() == this.end.getCaveId());
  }

  public boolean hasFallenIntoPit() {
    return player.getCurrentCave().getPit();
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
  public List<Edge> getEdge() {
    return new ArrayList<>(this.edge);
  }

  @Override
  public List<Edge> getMstList() {
    return new ArrayList<>(this.mstList);
  }

  @Override
  public int getFinalPercent() {
    return this.finalPercent;
  }

  public void makeDungeonCopy() {
    dungeonCopy = new DungeonImpl(this);
  }

  @Override
  public Dungeon getDungeonCopy() {
    return new DungeonImpl(dungeonCopy);
  }

  @Override
  public ArrayList<Location> getCaveFinalList() {
//    for (int i = 0; i < cavesFinalList.size(); i++) {
//      System.out.println(cavesFinalList.get(i));
//
//    }
    return new ArrayList<>(cavesFinalList);
  }


  @Override
  public String playerToPickArrow() {
    StringBuilder sb = new StringBuilder();
    if (player.getCurrentCave().getArrow() > 0 && player.getPlayerLive()) {
      player.pickArrow();
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
        edge.add(new Edge(dungeonArr[i][j], dungeonArr[i][j + 1], randomiser.getRandom(0, 10)));
      }
    }

    for (int i = 0; i < rows - 1; i++) {
      for (int j = 0; j < columns; j++) {
        edge.add(new Edge(dungeonArr[i][j], dungeonArr[i + 1][j], randomiser.getRandom(0, 10)));
      }
    }

    if (wrapping) {
      for (int i = 0; i < rows; i++) {
        edge.add(new Edge(dungeonArr[i][0], dungeonArr[i][columns - 1]
                , randomiser.getRandom(0, 10)));
      }

      for (int i = 0; i < columns; i++) {
        edge.add(new Edge(dungeonArr[0][i], dungeonArr[rows - 1][i]
                , randomiser.getRandom(0, 10)));
      }
    }

    DungeonCreation dungeonCreation = new DungeonCreation(edge, rows * columns);
    mstList = dungeonCreation.kruskalAlgo();
    edge.removeAll(mstList);

    randomiser.shuffle(edge);

    if (edge.size() < interconnectivity) {
      throw new IllegalArgumentException("The given interconnectivity is wrong");
    }

    for (int i = 0; i < interconnectivity; i++) {
      mstList.add(edge.get(i));
    }
    System.out.println(mstList);
  }

  private void createListOfCaves() {
    int caveId = 0;
    for (int i = 0; i < rows * columns; i++) {
      listOfCaves.add(new Cave(caveId));
      caveId++;
    }
  }

  private void addAdjacentCaves() {
    Direction directions;
    for (Edge value : mstList) {
      for (Location listOfCave : listOfCaves) {
        if (listOfCave.getCaveId() == (value.getSrc())) {
          if (value.getDest() - listOfCave.getCaveId() == 1) {
            directions = Direction.EAST;

            listOfCave.addCaveList(directions, listOfCaves.get(value.getDest()));
            listOfCaves.get(value.getDest()).addCaveList(Direction.WEST
                    , listOfCave);

          } else if (value.getDest()
                  - listOfCave.getCaveId() == Math.abs(columns - 1)) {
            directions = Direction.WEST;

            listOfCave.addCaveList(directions, listOfCaves.get(value.getDest()));
            listOfCaves.get(value.getDest()).addCaveList(Direction.EAST
                    , listOfCave);

          } else if (value.getDest() - listOfCave.getCaveId() == columns) {
            directions = Direction.SOUTH;

            listOfCave.addCaveList(directions, listOfCaves.get(value.getDest()));
            listOfCaves.get(value.getDest()).addCaveList(Direction.NORTH
                    , listOfCave);

          } else if (value.getDest() - listOfCave.getCaveId() > columns) {
            directions = Direction.NORTH;

            listOfCave.addCaveList(directions, listOfCaves.get(value.getDest()));
            listOfCaves.get(value.getDest()).addCaveList(Direction.SOUTH
                    , listOfCave);

          }
        }
      }
    }
  }

  private void putTreasure() {
    Treasure[] treasureList = Treasure.values();

    List<Location> treasureCave = new ArrayList<Location>();
    List<Location> listOfCavesCopy = new ArrayList<Location>();

    for (int i = 0; i < listOfCaves.size(); i++) {
      if (listOfCaves.get(i).getLocationType().equals(LocationType.CAVE)) {
        listOfCavesCopy.add(listOfCaves.get(i));
      }
    }
    randomiser.shuffle(listOfCavesCopy);
    int loopNum = (int) (finalPercent * (listOfCavesCopy.size() / 100.0));
    for (int i = 0; i < loopNum; i++) {
      treasureCave.add(listOfCavesCopy.get(i));
      for (int j = 0; j < treasureList.length; j++) {
        treasureCave.get(i).addTreasureToCave(treasureList[this.randomiser.getRandom(0, 2) % 3]);
      }
    }

  }

  private void putArrows() {
    int loopNum = (int) (finalPercent * listOfCaves.size() / 100.0);

    List<Location> listOfCavesCopy = new ArrayList<Location>(listOfCaves);

    randomiser.shuffle(listOfCavesCopy);

    for (int i = 0; i < loopNum; i++) {
      listOfCavesCopy.get(i).addArrow(randomiser.getRandom(1, 3));
    }
  }

  private void putMonster() {
    List<Location> listOfCavesCopy = new ArrayList<Location>();

    for (int i = 0; i < listOfCaves.size(); i++) {
      if (listOfCaves.get(i).getLocationType().equals(LocationType.CAVE)) {
        listOfCavesCopy.add(listOfCaves.get(i));
      }
    }
    end.addMonster();
    randomiser.shuffle(listOfCavesCopy);

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
      for (int i = 0; i < player.getCurrentCave().getTreasureList().size(); i++) {
        sb.append(" " + player.getCurrentCave().getTreasureList().get(i).toString());
      }
    }
    return sb.toString();
  }

  @Override
  public Smell getSmell() {
    int count = 0;
    Map<Location, Integer> smellList;
    smellList = endUsingBfs(player.getCurrentCave());
    for (Map.Entry<Location, Integer> set : smellList.entrySet()) {
      if (set.getValue() == 1 && set.getKey().hasMonster()) {
        return Smell.HIGH;
      } else if (set.getValue() == 2 && set.getKey().hasMonster()) {
        count++;
      }
    }

    if (count >= 2) {
      return Smell.HIGH;
    } else if (count == 1) {
      return Smell.LOW;
    }
    return Smell.NONE;
  }

  public boolean getSoil() {
    for (Map.Entry<Direction, Location> set : player.getCurrentCave().getCaveList().entrySet()) {
      if (set.getValue().getPit()) {
        return true;
      }
    }
    return false;
  }


  private void putPit() {
    List<Location> listOfCavesCopy = new ArrayList<Location>();

    for (Location listOfCave : listOfCaves) {
      if (listOfCave.getLocationType().equals(LocationType.CAVE)) {
        listOfCavesCopy.add(listOfCave);
      }
    }

    randomiser.shuffle(listOfCavesCopy);
    listOfCavesCopy.remove(start);

    for (int i = 0; i < listOfCavesCopy.size(); i++) {
      if (!listOfCavesCopy.get(i).hasMonster()) {
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

    randomiser.shuffle(listOfCavesCopy);
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


  @Override
  public Map<Location, Integer> endUsingBfs(Location src) {
    if (src == null) {
      throw new IllegalArgumentException("Source Location cannot be null");
    }
    LinkedList<Location> queue = new LinkedList<Location>();
    Map<Location, Integer> levelMap = new HashMap<Location, Integer>();

    src.updateVisit(true);
    queue.add(src);
    levelMap.put(src, 0);

    while (queue.size() != 0) {
      src = queue.poll();

      for (Location n : new ArrayList<Location>(src.getCaveList().values())) {
        if (!n.isVisited()) {
          n.updateVisit(true);
          queue.add(n);
          levelMap.put(n, levelMap.get(src) + 1);
        }
      }
    }

    for (Location i : listOfCaves) {
      i.updateVisit(false);
    }

    return new HashMap<Location, Integer>(levelMap);
  }

  private void findPossibleMoves() {

    List<Location> listForStart = new ArrayList<Location>();

    for (Location location : listOfCaves) {
      if (location.getLocationType() != LocationType.TUNNEL) {
        listForStart.add(location);
      }
    }

    randomiser.shuffle(listForStart);

    for (Location i : listForStart) {
      this.start = i;
      Map<Location, Integer> level = endUsingBfs(this.start);
      this.end = null;

      for (Location j : level.keySet()) {
        int value = level.get(j);

        if (value >= 5 && j.getLocationType() != LocationType.TUNNEL) {
          this.end = j;
          break;
        }
      }

      if (this.end != null) {
        break;
      }
    }

    if (this.end == null) {
      throw new IllegalArgumentException("The matrix size is not valid, no start end possible!");
    }
  }

  @Override
  public String shootArrow(int distance, Direction direction) throws IllegalArgumentException {
    StringBuilder sb = new StringBuilder();
    if (distance <= 0) {
      throw new IllegalArgumentException("Distance should be greater than one.");
    }
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be negative");
    }
    if (player.getArrowCount() <= 0) {
      sb.append("The player is not left with any arrows to shoot.");
    }
    Location caveForArrow;
    Direction oppDir = null;

    player.shootArrow();

    if (player.getCurrentCave().getCaveList().containsKey(direction)) {
      caveForArrow = player.getCurrentCave().getCaveList().get(direction);
      if (caveForArrow.getLocationType() == LocationType.CAVE) {
        distance--;
      }
      if (distance == 0) {
        if (caveForArrow.hasMonster()) {
          caveForArrow.hitMonster();
          if (caveForArrow.hasMonster()) {
            sb.append("Player has injured the monster.").append("\n");
            return sb.toString();
          } else {
            sb.append("Player has killed the monster.").append("\n");
            return sb.toString();
          }
        }
        sb.append("Player shot into into the darkness.").append("\n");
        return sb.toString();
      }
    } else {
      sb.append("Player shot into into the darkness.").append("\n");
      return sb.toString();
    }

    while (distance > 0) {
      if (caveForArrow.getLocationType() == LocationType.CAVE) {
        if (caveForArrow.getCaveList().containsKey(direction)) {
          caveForArrow = caveForArrow.getCaveList().get(direction);
          if (caveForArrow.getLocationType() == LocationType.CAVE) {
            distance--;
          }
          if (distance == 0) {
            break;
          }
        } else {
          break;
        }
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
              distance--;
            }
            break;
          }
        }
      }
    }

    if (caveForArrow.hasMonster() && distance == 0) {
      if (caveForArrow.hasMonster()) {
        caveForArrow.hitMonster();
        if (caveForArrow.hasMonster()) {
          sb.append("Player has injured the monster.").append("\n");
          return sb.toString();
        } else {
          sb.append("Player has killed the monster.").append("\n");
          return sb.toString();
        }
      }
      sb.append("Player shot into into the darkness.").append("\n");
      return sb.toString();
    }
    sb.append("Player shot into into the darkness.").append("\n");
    return sb.toString();
  }

  @Override
  public String maze(String move) throws IllegalArgumentException {
    if (move == null) {
      throw new IllegalArgumentException("Move cannot be null");
    }
    boolean correctMove;
    StringBuilder sb = new StringBuilder();
    if (player.getPlayerLive() && !hasReached()) {
      if (move.equalsIgnoreCase("N")) {
        correctMove = player.move(Direction.NORTH);
      } else if (move.equalsIgnoreCase("S")) {
        correctMove = player.move(Direction.SOUTH);

      } else if (move.equalsIgnoreCase("E")) {
        correctMove = player.move(Direction.EAST);

      } else if (move.equalsIgnoreCase("W")) {
        correctMove = player.move(Direction.WEST);

      } else {

        throw new IllegalArgumentException("The entered direction was invalid");
      }

      if (player.getCurrentCave().getThief()) {
        player.removeTreausuresWithPlayer();
        sb.append("The player encountered a thief and lost all it's treasures!! :(").append("\n");
        //.out.println("size" + player.getTreasureList().size());
      }

      if (hasFallenIntoPit()) {
        player.killPlayer();
      }

      if (!correctMove) {
        sb.append("The entered direction was invalid").append("\n");
        return sb.toString();
      } else if (correctMove) {
        sb.append("Player has moved successfully " + player.getCurrentCave().getCaveId()).append("\n");
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
    randomiser.shuffle(listOfCavesMonster);
    movingMonsterCaveId = randomiser.getRandom(0, listOfCavesMonster.size() - 1);
    listOfCavesMonster.get(movingMonsterCaveId).addMovingMonster();
    movingMonsterCaveId = listOfCavesMonster.get(movingMonsterCaveId).getCaveId();

  }

  private String monsterCombat(StringBuilder sb) {
    if (player.getCurrentCave().hasMovingMonster()) {
      CaveCreature movingMonster = player.getCurrentCave().getMovingMonster();
      while (player.getHealth() > 0 && movingMonster.getHealth() > 0) {
        int random = this.randomiser.getRandom(0, 1) % 2;
        if (random == 0) {
          movingMonster.injure();
          sb.append("Player has injured the monster. Monster left with health: "
                  + movingMonster.getHealth()).append("\n");

        } else {
          player.injure();
          sb.append("Player has been injured. Remaining health:" + player.getHealth()).append("\n");
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
    return sb.toString();
  }

  private String checkMonster(StringBuilder sb) {
    if (player.getCurrentCave().hasMonster()) {
      if (player.getCurrentCave().getMonster().getHealth() == 100) {
        player.killPlayer();
        sb.append("Oops there was a monster here. Eating the player chomp chomp chomp!!")
                .append("\n");

      } else if (player.getCurrentCave().getMonster().getHealth() == 50) {
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
    if (player.getCurrentCave().getTreasureList().size() > 0 && player.getPlayerLive()) {
      player.pickTreasure(player.getCurrentCave().getTreasureList());
      player.removeTreasure(player.getCurrentCave());
      sb.append("Player picked the treasures.").append("\n");
    } else if (!player.getPlayerLive()) {
      sb.append("Player is dead. Can't pick treasures.");
    } else {
      sb.append("There are no treasures here").append("\n");
    }
    return sb.toString();
  }

  public void createCavesFinalList() {
    for (Location listOfCave : listOfCaves) {
      cavesFinalList.add(new Cave(listOfCave));
    }
//    for (int i = 0; i < cavesFinalList.size(); i++) {
//      if (start.getCaveId() == cavesFinalList.get(i).getCaveId()) {
//
//      }
//    }
  }

  @Override
  public String getPlayerDesc() {
    StringBuilder sb = new StringBuilder();
    sb.append("Player is at " + player.getCurrentCave().getCaveId()).append("\n");

    if (player.getTreasureList().size() > 0) {
      sb.append("\nPlayer has the following treasures :").append("\n");
      for (int i = 0; i < player.getTreasureList().size(); i++) {
        sb.append(player.getTreasureList().get(i).toString()).append("\n");
      }
    }

//    if (this.getSmell() != Smell.NONE) {
//      sb.append("Smell at the current cave is " + this.getSmell()).append("\n");
//    }
//
//    if (this.getSoil()) {
//      sb.append("There seems to be some soil nearby! Be careful!").append("\n");
//    }

    sb.append("Arrows with player: " + player.getArrowCount()).append("\n");
//    sb.append("\nThe player can go to the following destinations :").append("\n");
//    for (Map.Entry<Direction, Location> set : player.getCurrentCave().getCaveList().entrySet()) {
//      sb.append(set.getKey().toString()).append("\n");
//    }
    return sb.toString();
  }

  public String getLocationDesc() {
    StringBuilder sb = new StringBuilder();
    if (this.getSmell() != Smell.NONE) {
      sb.append("Smell at the current cave is " + this.getSmell().toString()).append("\n");
    }

    if (this.getSoil()) {
      sb.append("There seems to be some soil nearby! Be careful!").append("\n");
    }

    if (player.getCurrentCave().getArrow() > 0) {
      sb.append("Arrows are present in this cave.").append("\n");
    }

    if (player.getCurrentCave().getTreasureList().size() > 0) {
      sb.append("Wow this cave has treasures.").append("\n");
      for (int i = 0; i < player.getCurrentCave().getTreasureList().size(); i++) {
        sb.append(player.getCurrentCave().getTreasureList().get(i).toString()).append("\n");
      }
    }

    if (player.getCurrentCave().getThief()) {
      sb.append("There is a thief in the cave, run!").append("\n");
    }

    sb.append("\nThe player can go to the following destinations :").append("\n");
    for (Map.Entry<Direction, Location> set : player.getCurrentCave().getCaveList().entrySet()) {
      sb.append(set.getKey().toString()).append("\n");
    }

    return sb.toString();
  }

  @Override
  public boolean isTreasure() {
    return (player.getCurrentCave().getTreasureList().size() > 0);
  }

  @Override
  public String getDungeon() {
    StringBuilder sb = new StringBuilder();
    sb.append("----------Dungeon----------");

    for (int i = 0; i < listOfCaves.size(); i++) {
      sb.append("\n");
      if (listOfCaves.get(i).getLocationType() == LocationType.CAVE) {
        sb.append("\nCave" + listOfCaves.get(i).getCaveId());
        sb.append("\nThis cave is connected to the :");
        for (Map.Entry<Direction, Location> entry : listOfCaves.get(i).getCaveList().entrySet()) {
          Direction k = entry.getKey();
          Location v = entry.getValue();
          sb.append("\n" + k);
          if (v.getLocationType() == LocationType.CAVE) {
            sb.append(" Cave" + v.getCaveId());
          } else {
            sb.append(" Tunnel" + v.getCaveId());
          }
        }

      } else {
        sb.append("\nTunnel" + listOfCaves.get(i).getCaveId());
        sb.append("\nThis tunnel is connected to : ");
        for (Map.Entry<Direction, Location> entry : listOfCaves.get(i).getCaveList().entrySet()) {
          Direction k = entry.getKey();
          Location v = entry.getValue();
          sb.append("\n" + k);
          if (v.getLocationType() == LocationType.CAVE) {
            sb.append(" Cave" + v.getCaveId());
          } else {
            sb.append(" Tunnel" + v.getCaveId());
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





