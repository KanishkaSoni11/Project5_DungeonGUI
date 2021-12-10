package dungeon;

/**
 * code referred from
 * https://www.techiedelight.com/kruskals-algorithm-for-finding-minimum-spanning-tree/
 */

class Edge {
  private int src;
  private int dest;
  private int weight;

  Edge(int src, int dest, int weight) {
    this.src = src;
    this.dest = dest;
    this.weight = weight;
  }

  int getSrc() {
    return src;
  }

  int getDest() {
    return dest;
  }

  int getWeight() {
    return weight;
  }
}
