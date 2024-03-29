package location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Cave is the implementation of Location to specify each cave of the dungeon which can be
 * classified as cave or tunnel based on the neighbours. It assigns the treasures to caves,
 * checks the neighbours of the cave.
 */
public class Cave implements Location {

  private final Map<Direction, Location> caveList;
  private List<Treasure> treasureList;
  private final int caveId;
  private int arrows;
  private LocationMonster monster;
  private boolean hasPit;
  private boolean hasThief;
  private LocationMonster movingMonster;

  /**
   * Constructs the cave location based on the cave id given.
   *
   * @param caveId unique id given to each cave
   * @throws IllegalArgumentException if the cave id is negative
   */
  public Cave(int caveId) throws IllegalArgumentException {
    if (caveId < 0) {
      throw new IllegalArgumentException("Cave Id should be positive");
    }
    this.caveId = caveId;
    this.treasureList = new ArrayList<Treasure>();
    this.caveList = new TreeMap<Direction, Location>();
    this.monster = null;
    this.arrows = 0;
    this.hasPit = false;
    this.hasThief = false;
    this.movingMonster = null;
  }

  /**
   * Copy constructor of the cave to avoid mutating of the cave object.
   *
   * @param cave the cave object
   * @throws IllegalArgumentException if the cave object is null
   */
  public Cave(Location cave) throws IllegalArgumentException {
    if (cave == null) {
      throw new IllegalArgumentException("Cave cannot be null.");
    }
    this.caveList = cave.getCaveList();
    this.treasureList = cave.getTreasureList();
    this.caveId = cave.getCaveId();
    this.monster = cave.getMonster();
    this.arrows = cave.getArrow();
    this.hasPit = cave.getPit();
    this.hasThief = cave.getThief();
    this.movingMonster = cave.getMovingMonster();
  }

  @Override
  public void addCaveList(Direction directions, Location cave) throws IllegalArgumentException {
    if (directions == null) {
      throw new IllegalArgumentException("Direction cannot be null");
    }

    if (cave == null) {
      throw new IllegalArgumentException("Cave cannot be null.");
    }
    this.caveList.put(directions, cave);
  }

  @Override
  public Map<Direction, Location> getCaveList() {
    return new TreeMap<>(caveList);
  }

  @Override
  public void addTreasureToCave(Treasure treasure) throws IllegalArgumentException {
    if (treasure == null) {
      throw new IllegalArgumentException("Treasure cannot be null.");
    }
    treasureList.add(treasure);
  }

  @Override
  public LocationType getLocationType() {
    if (this.caveList.size() == 2) {
      return LocationType.TUNNEL;
    } else {
      return LocationType.CAVE;
    }
  }

  @Override
  public List<Treasure> getTreasureList() {
    return new ArrayList<Treasure>(this.treasureList);
  }

  @Override
  public int getCaveId() {
    return caveId;
  }

  @Override
  public void removeTreasure() {
    this.treasureList.clear();
  }

  @Override
  public void addArrow(int number) {
    arrows = this.arrows + number;
  }

  @Override
  public int getArrow() {
    return arrows;
  }

  @Override
  public void removeArrow() {
    this.arrows = 0;
  }

  @Override
  public void addMonster() {
    this.monster = new Monster();
  }

  @Override
  public boolean hasMonster() {
    return (monster != null && monster.getHealth() > 0);
  }

  @Override
  public LocationMonster getMonster() {
    if (this.hasMonster()) {
      return new Monster(monster);
    }
    return null;
  }

  @Override
  public void hitMonster() {
    if (this.monster.getHealth() > 0) {
      this.monster.injure();
    }
  }

  @Override
  public void setPit() {
    this.hasPit = true;
  }

  @Override
  public boolean getPit() {
    return this.hasPit;
  }

  @Override
  public void addThief() {
    this.hasThief = true;
  }

  @Override
  public boolean getThief() {
    return this.hasThief;
  }

  @Override
  public void addMovingMonster() {
    this.movingMonster = new MovingMonster();
  }

  @Override
  public LocationMonster getMovingMonster() {
    if (this.hasMovingMonster()) {
      return new MovingMonster(movingMonster);
    }
    return null;
  }

  @Override
  public boolean hasMovingMonster() {
    return this.movingMonster != null && this.movingMonster.getHealth() > 0;
  }

  @Override
  public void removeMovingMonster() {
    this.movingMonster = null;
  }




}
