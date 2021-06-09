package linearize;

import algorithm.NearestArray;
import utils.Print;

public class SCPO implements Curve {

  NearestArray frontier;

  SCPO() {
    frontier = new NearestArray();
  }

  @Override
  public int[] encode(int[][] origin) {
    int width = origin.length;
    int height = origin[0].length;
    int n = width * height;
    int[] result = new int[n];
    int[][] markedNode = new int[width][height];

    NearestArray.Node node = new NearestArray.Node(0, 0);
    int index = 0;
    int lastValue = origin[node.x][node.y];
    frontier.push(node, lastValue);
    markedNode[node.x][node.y] = 1;
    result[index++] = lastValue;

    while (index < n) {
      node = frontier.pop(lastValue);
      lastValue = origin[node.x][node.y];

      NearestArray.Node[] pickedNodes = {
          new NearestArray.Node(node.x + 1, node.y),
          new NearestArray.Node(node.x, node.y + 1),
          new NearestArray.Node(node.x - 1, node.y),
          new NearestArray.Node(node.x, node.y - 1)
      };

      for (NearestArray.Node pickedNode : pickedNodes) {
        int x = pickedNode.x;
        int y = pickedNode.y;
        if (x >=0 && x < width && y >=0 && y < height && markedNode[x][y] == 0) {
          int value = origin[x][y];
          frontier.push(pickedNode, value);
          markedNode[x][y] = 1;
          result[index++] = value;
        }
      }
    }
    return result;
  }

  @Override
  public int[][] decode(int[] encodeData) {
    return new int[0][];
  }

  public static void main(String[] args) {
    SCPO scpo = new SCPO();
    int[][] origin = {{1, 7, 3}, {4, 6, 8}, {5, 2, 9}};
    int[] result = scpo.encode(origin);

    Print.print2DArray("Origin data", origin);
    Print.print1DArray("Encode data", result);
  }
}
