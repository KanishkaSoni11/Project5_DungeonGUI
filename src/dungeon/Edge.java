package dungeon;

class Edge {
  private int src;
  private int dest;
  private int weight;

  Edge(int src, int dest, int weight) {
    this.src = src;
    this.dest = dest;
    this.weight = weight;
  }

  @Override
  public String toString() {
    return "(" + src + ", " + dest + ", " + weight + ")";
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
