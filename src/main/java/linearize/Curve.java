package linearize;

public interface Curve {
  int[] encode(int[][] origin);

  int[][] decode(int[] encodeData);
}
