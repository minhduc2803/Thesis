package linearize;

import utils.Print;

public class HilbertCurve implements Curve {
  private class Coordinate {
    public int x;
    public int y;

    public Coordinate(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  int N;

  private int getLastTwoBits(int x) {
    return (x & 3);
  }

  private Coordinate getHilbert(int index) {
    int[][] positions = {{0, 0}, {1, 0}, {1, 1}, {0, 1}};
    int temp[] = positions[getLastTwoBits(index)];
    int currentIndex = index >>> 2;
    int x = temp[0];
    int y = temp[1];
    int bits = (int) (Math.log(this.N) / Math.log(2.0)) + 1;
    for (int n = 2; bits >= 0; n <<= 1, bits --) {
      switch (getLastTwoBits((currentIndex))) {
        case 0:
          // flip around diagonal
          // because the coordinates is at (0,0) so not need to transform to the next generation
          int tmp = x;
          x = y;
          y = tmp;
          if (currentIndex == 0) {
            currentIndex = currentIndex >>> (bits /2) * 2;
            bits = bits % 2;
          }
          break;

        case 1:
          // add coordinates to the next generation -> to the top
          x = x + n;
          break;

        case 2:
          // add coordinates to the next generation -> to the right
          x = x + n;
          y = y + n;
          break;

        case 3:
          // flip 1 -> 0 and 0 -> 1
          x = n - 1 - x;
          y = n - 1 - y;
          // flip around diagonal
          tmp = x;
          x = y;
          y = tmp;
          // add coordinates to the next generation -> to the right
          y = y + n;
          break;
      }
      currentIndex = currentIndex >>> 2;
    }
    return new Coordinate(x, y);
  }

  public int[] encode(int[][] twoD) {
    this.N = twoD.length * twoD[0].length;
    int[] oneD = new int[this.N];
    for (int i = 0; i < this.N; i++) {
      Coordinate p = getHilbert(i);
      oneD[i] = twoD[p.x][p.y];
    }
    return oneD;
  }

  public int[][] decode(int[] oneD) {
    this.N = oneD.length;
    int sqrtN = (int) Math.sqrt(this.N);
    int[][] twoD = new int[sqrtN][sqrtN];
    for (int i = 0; i < this.N; i++) {
      Coordinate p = getHilbert(i);
      twoD[p.x][p.y] = oneD[i];
    }
    return twoD;
  }

  public static void main(String[] args) {
    int[][] twoD = {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}};
    HilbertCurve hilbertCurve = new HilbertCurve();
    int[] oneD = hilbertCurve.encode(twoD);

    Print.print2DArray("2D array: ", twoD);
    Print.print1DArray("Linearize with Hilbert curve: ", oneD);

    int[][] twoDResult = hilbertCurve.decode(oneD);

    Print.print2DArray("2D array: ", twoDResult);
  }
}
