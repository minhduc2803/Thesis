package linearize;

import algorithm.NearestArray;

public class SCPO implements Curve {

  NearestArray frontier;
  SCPO() {
    frontier = new NearestArray();
  }
  @Override
  public int[] encode(int[][] origin) {
    int n = origin.length * origin[0].length;
    int[] result = new int[n];
    NearestArray.Node node = new NearestArray.Node(0 , 0);
    int index = 0;
    int value = origin[node.x][node.y];
    frontier.push(node, value);
    result[index] = value;

    while (index < n) {
      node = frontier.pop(value);
      // TODO: mark a node when move a frontier
      // TODO: find nearest nodes around node
    }
    return new int[0];
  }

  @Override
  public int[][] decode(int[] encodeData) {
    return new int[0][];
  }
}
