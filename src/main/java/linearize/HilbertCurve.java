package linearize;

import utils.Print;

public class HilbertCurve {
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
    index = index >>> 2;
    int x = temp[0];
    int y = temp[1];

    for (int n = 4; n <= this.N; n *= 2) {
      int n2 = n / 2;

      switch (getLastTwoBits((index))) {
        case 0:
          // flip around diagonal
          // because the coordinates is at (0,0) so not need to transform to the next generation
          int tmp = x;
          x = y;
          y = tmp;
          break;

        case 1:
          // add coordinates to the next generation -> to the top
          x = x + n2;
          break;

        case 2:
          // add coordinates to the next generation -> to the right
          x = x + n2;
          y = y + n2;
          break;

        case 3:
          // flip 1 -> 0 and 0 -> 1
          x = n2 - 1 - x;
          y = n2 - 1 - y;
          // flip around diagonal
          tmp = x;
          x = y;
          y = tmp;
          // add coordinates to the next generation -> to the right
          y = y + n2;
          break;
      }
      index = index >>> 2;
    }
    return new Coordinate(x, y);
  }

  public short[] encode(short[][] twoD) {
    this.N = twoD.length * twoD[0].length;
    short[] oneD = new short[this.N];
    for (int i = 0; i < this.N; i++) {
      Coordinate p = getHilbert(i);
      oneD[i] = twoD[p.x][p.y];
    }
    return oneD;
  }

  public short[][] decode(short[] oneD) {
    this.N = oneD.length;
    int sqrtN = (int) Math.sqrt(this.N);
    short[][] twoD = new short[sqrtN][sqrtN];
    for (int i = 0; i < this.N; i++) {
      Coordinate p = getHilbert(i);
      twoD[p.x][p.y] = oneD[i];
    }
    return twoD;
  }

  public static void main(String[] args) {
    short[][] twoD = {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}};
    HilbertCurve hilbertCurve = new HilbertCurve();
    short[] oneD = hilbertCurve.encode(twoD);

    Print.print2DArray("2D array: ", twoD);
    Print.print1DArray("Linearize with Hilbert curve: ", oneD);

    short[][] twoDResult = hilbertCurve.decode(oneD);

    Print.print2DArray("2D array: ", twoDResult);
  }
}
