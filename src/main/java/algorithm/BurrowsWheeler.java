package algorithm;

import utils.Logic;
import utils.Print;

import java.util.Arrays;
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
    int[] encodeResult = new int[origin.length];
    Integer[] index = new Integer[origin.length];

    for (int i = 0; i < origin.length; i++) {
      index[i] = i;
    }

    ArrayIndexComparator arrayIndexComparator = new ArrayIndexComparator(origin);
    Arrays.sort(index, arrayIndexComparator);

    for(int i = 0;i<index.length;i++) {
      encodeResult[i] = index[i] - 1 < 0 ? 256 : origin[index[i] - 1];
    }
    Print.print1DArray("Index array", index);
    return encodeResult;
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
