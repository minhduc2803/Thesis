package algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class NearestArray {

  public static class Node {
    public int x, y;

    public Node(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  int n;
  Queue<Node>[] array;

  public NearestArray() {
    this.n = 0;
    this.array = new LinkedList[256];
    for (int i = 0; i < 256; i++) {
      array[i] = new LinkedList<>();
    }
  }

  public void push(Node node, int value) {
    array[value].add(node);
  }

  public Node pop(int value) {
    if (array[value].isEmpty()) {
      int above = value + 1;
      while (above < 256 && array[above].isEmpty()) {
        above++;
      }

      int below = value - 1;
      while (below >= 0 && array[below].isEmpty()) {
        below--;
      }

      return above < 256 && above - value <= value - below ? array[above].remove() : array[below].remove();
    } else {
      return array[value].remove();
    }
  }

  public static void main(String args[]) {
    NearestArray nearestArray = new NearestArray();
    nearestArray.push(new NearestArray.Node(0, 0), 100);
    nearestArray.push(new NearestArray.Node(1, 0), 100);
    nearestArray.push(new NearestArray.Node(2, 0), 102);
    nearestArray.push(new NearestArray.Node(0, 1), 103);
    nearestArray.push(new NearestArray.Node(0, 2), 102);
    nearestArray.push(new NearestArray.Node(0, 3), 110);

    NearestArray.Node node = nearestArray.pop(100);
    System.out.println(node.x + " " + node.y);
    node = nearestArray.pop(100);
    System.out.println(node.x + " " + node.y);
    node = nearestArray.pop(100);
    System.out.println(node.x + " " + node.y);
    node = nearestArray.pop(105);
    System.out.println(node.x + " " + node.y);
  }
}
