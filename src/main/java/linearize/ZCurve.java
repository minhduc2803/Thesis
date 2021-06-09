package linearize;

import utils.Logic;
import utils.Print;

public class ZCurve implements Curve {
  int N;

  public int getZCurve(int y, int x) {
    int MASKS[] = {0x55555555, 0x33333333, 0x0F0F0F0F, 0x00FF00FF};
    int SHIFTS[] = {1, 2, 4, 8};

    x = (x | (x << SHIFTS[3])) & MASKS[3];
    x = (x | (x << SHIFTS[2])) & MASKS[2];
    x = (x | (x << SHIFTS[1])) & MASKS[1];
    x = (x | (x << SHIFTS[0])) & MASKS[0];

    y = (y | (y << SHIFTS[3])) & MASKS[3];
    y = (y | (y << SHIFTS[2])) & MASKS[2];
    y = (y | (y << SHIFTS[1])) & MASKS[1];
    y = (y | (y << SHIFTS[0])) & MASKS[0];

    int result = x | (y << 1);
    return result;
  }

  public int[] encode(int[][] origin) {
    this.N = origin.length;
    int[] oneD = new int[this.N * this.N];
    for (int i = 0; i < this.N; i++) {
      for (int j = 0; j < this.N; j++) {
        oneD[getZCurve(i, j)] = origin[i][j];
      }
    }

    return oneD;
  }

  public int[][] decode(int[] encodeData) {
    this.N = (int) Math.sqrt(encodeData.length);
    int[][] decodeData = new int[this.N][this.N];
    for (int i = 0; i < this.N; i++) {
      for (int j = 0; j < this.N; j++) {
        decodeData[i][j] = encodeData[getZCurve(i, j)];
      }
    }

    return decodeData;
  }

  public static void main(String[] args) {
    ZCurve zCurve = new ZCurve();
    int[][] origin = {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}};

    int[] encodeData = zCurve.encode(origin);
    int[][] decodeData = zCurve.decode(encodeData);

    Print.print2DArray("origin string: ", origin);
    Print.print1DArray("encode string: ", encodeData);
    Print.print2DArray("decode string: ", decodeData);

    System.out.println(Logic.compare2D(origin, decodeData));

  }

}
