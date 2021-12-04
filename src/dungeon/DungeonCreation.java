package dungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DungeonCreation {
  private Map<Integer, Integer> parent;
  private List<Edge> edges;
  private int n;

  DungeonCreation(List<Edge> edges, int n) {
    this.edges = edges;
    this.n = n;
    this.parent = new HashMap<Integer, Integer>();
  }

  private void makeSet() {
    for (int i = 0; i < n; i++) {
      parent.put(i, i);
    }
  }

  private int find(int k) {
    if (parent.get(k) == k) {
      return k;
    }
    return find(parent.get(k));
  }

  private void union(int a, int b) {
    int x = find(a);
    int y = find(b);
    parent.put(x, y);
  }

  List<Edge> kruskalAlgo() {
    List<Edge> mst = new ArrayList();
    makeSet();

    int index = 0;
    Collections.sort(edges, Comparator.comparingInt(e -> e.getWeight()));

    while (mst.size() != n - 1) {
      Edge next_edge = edges.get(index++);
      int x = find(next_edge.getSrc());
      int y = find(next_edge.getDest());

      if (x != y) {
        mst.add(next_edge);
        union(x, y);
      }
    }
    return new ArrayList<>(mst);
  }

}
