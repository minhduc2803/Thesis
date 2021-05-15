package algorithm;

import com.oracle.tools.packager.Log;
import utils.Logic;
import utils.Print;

import java.util.ArrayList;

public class RunLength {

  public static int[] encode(int[] origin) {
//    return Logic.copyArray1D(origin);
    ArrayList<Integer> encodeData = new ArrayList<>();
    boolean isBeforeZero = false;
    for (int i = 0; i < origin.length; i++) {
      if (origin[i] == 0) {
        if (isBeforeZero) {
          int num = encodeData.get(encodeData.size() - 1);
          if(num < 255) {
            num++;
            encodeData.set(encodeData.size() - 1, num);
          } else {
            encodeData.add(0);
            encodeData.add(1);
          }

        } else {
          encodeData.add(0);
          encodeData.add(1);
        }
        isBeforeZero = true;
      } else {
        encodeData.add(origin[i]);
        isBeforeZero = false;
      }

    }
    int[] encodeArray = new int[encodeData.size()];
    for (int i = 0; i < encodeArray.length; i++) {
      encodeArray[i] = encodeData.get(i);
    }
    return encodeArray;
  }

  public static int[] decode(int[] encodeData) {
//    return Logic.copyArray1D(encodeData);
    ArrayList<Integer> decodeData = new ArrayList<>();
    for (int i = 0; i < encodeData.length; i++) {
      if (encodeData[i] == 0) {
        i++;
        int num = encodeData[i];
        for (int j = 0; j < num; j++) {
          decodeData.add(0);
        }
      } else {
        decodeData.add(encodeData[i]);
      }

    }
    int[] decodeArray = new int[decodeData.size()];
    for (int i = 0; i < decodeArray.length; i++) {
      decodeArray[i] = decodeData.get(i);
    }
    return decodeArray;
  }

  public static void main(String[] args) {
    int[] origin = {0, 1, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 1, 0, 1, 0};
    Print.print1DArray("origin data", origin);

    int[] encodeData = RunLength.encode(origin);
    Print.print1DArray("encode data", encodeData);

    int[] decodeData = RunLength.decode(encodeData);


    Print.print1DArray("decode data", decodeData);
    System.out.println(Logic.compare1D(origin, decodeData));
  }
}
