package dungeon;

import java.util.List;

import randomiser.Randomiser;

 class DungeonWrapped {
  private final List<Edge> edge;
  private final int[][] dungeonArr;
  private final int rows;
  private final int columns;
  private final Randomiser randomiser;

   DungeonWrapped(List<Edge> edge, int[][] dungeonArr, int rows, int columns, Randomiser randomiser) {
    this.dungeonArr = dungeonArr;
    this.edge = edge;
    this.rows = rows;
    this.columns = columns;
    this.randomiser = randomiser;
  }

   List<Edge> makeWrappedDungeon() {
    for (int i = 0; i < rows; i++) {
      edge.add(new Edge(dungeonArr[i][0], dungeonArr[i][columns - 1]
              , randomiser.getRandom(0, 5)));
    }

    for (int i = 0; i < columns; i++) {
      edge.add(new Edge(dungeonArr[0][i], dungeonArr[rows - 1][i]
              , randomiser.getRandom(0, 5)));
    }
    return edge;
  }
}
