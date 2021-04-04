package utils;

import encode.Node;

public class Print {
  public static void print1DArray(String s, short[] str) {
    System.out.println(s);
    for (int i = 0; i < str.length; i++) {
      System.out.print(str[i]);
      System.out.print(' ');
    }
    System.out.println();
  }

  public static void print2DArray(String s, short[][] str) {
    System.out.println(s);
    int m = str.length;
    int n = str[0].length;

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        System.out.print(str[i][j]);
        System.out.print(' ');
      }
      System.out.println();
    }

  }

  public static void printList(String s, Node head) {
    System.out.println(s);
    for (Node p = head; p != null; p = p.next) {
      System.out.print(p.value);
      System.out.print(" ");
    }
    System.out.println();
  }
}
