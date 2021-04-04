package utils;

public class Logic {
  public static boolean compare2D(int[][] origin, int[][] component) {
    int m = origin.length;
    int n = origin[0].length;
    if (m != component.length || n != component[0].length)
      return false;

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        if (origin[i][j] != component[i][j])
          return false;
      }
    }
    return true;
  }

  public static boolean compare1D(int[] origin, int[] component) {
    if (origin.length != component.length)
      return false;
    for (int i = 0; i < origin.length; i++) {
      if (origin[i] != component[i])
        return false;
    }
    return true;
  }

}
