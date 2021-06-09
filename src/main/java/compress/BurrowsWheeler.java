package compress;

import utils.Print;

import java.util.Comparator;

public class BurrowsWheeler {
  public class ArrayIndexComparator implements Comparator<Integer> {
    private final int[] array;

    public ArrayIndexComparator(int[] array) {
      this.array = array;
    }

    @Override
    public int compare(Integer index1, Integer index2) {
      return array[index1] - array[index2];
    }
  }

  public int[] encode(int[] origin) {

    // call recursion

    // name triples

    int n = origin.length;
    int padding = 3 - (n - 1) % 3;
    int[] T = new int[n + padding];
    for (int i = 0; i < n; i++) {
      T[i] = origin[i];
    }
    for (int i = n; i < n + padding; i++) {
      T[i] = -1;
    }

    // sort triples

    int[] A12 = new int[2 * (n + padding) / 3 - 1];

    // radix sort

    int endA1 = 1;
    for (int i = 0; i * 3 + 1 < n + padding; i++) {
      A12[i] = i * 3 + 1;
      endA1 = i;
    }
    endA1++;
    for (int i = 0; i * 3 + 2 < n + padding; i++) {
      A12[i + endA1] = i * 3 + 2;
    }
    Print.print1DArray("suffix triple A12", A12);
    // suffix on S0

    // merge S0 and S12

    return origin;
  }

  public int[] decode(int[] encodeArray) {
    int[] decodeResult = new int[encodeArray.length];
    return decodeResult;
  }

  public static void main(String[] args) {
    BurrowsWheeler burrowsWheeler = new BurrowsWheeler();

    int[] origin = {3, 2, 1, 2, 1, 2, -1};
    int[] encodeArray = burrowsWheeler.encode(origin);
//    int[] decodeArray = burrowsWheeler.decode(encodeArray);

    Print.print1DArray("Original array", origin);
    Print.print1DArray("Encode array: ", encodeArray);
//    Print.print1DArray("Decode array", decodeArray);
//    System.out.println("Result: " + Logic.compare1D(origin, decodeArray));
  }
}
