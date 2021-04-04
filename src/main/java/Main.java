import encode.Huffman;
import encode.MoveToFrontList;
import linearize.HilbertCurve;
import utils.Logic;
import utils.Print;

public class Main {
  public static void main(String[] args) {
    short[][] twoD = {{1, 2, 3, 4}, {1, 2, 6, 7}, {2, 1, 8, 10}, {1, 12, 13, 14}};
    Print.print2DArray("Origin data", twoD);

    HilbertCurve hilbertCurve = new HilbertCurve();
    MoveToFrontList moveToFrontList = new MoveToFrontList();

    short[] oneD = hilbertCurve.encode(twoD);
    Print.print1DArray("1D array: ", oneD);

    short[] encodeData = moveToFrontList.encode(oneD);
    Print.print1DArray("encode data: ", oneD);

    String compressData = Huffman.encode(encodeData);
    System.out.println("compressed data: " + compressData);

    Huffman.Node rootTree = Huffman.buildTree(encodeData);
    short[] depressData = Huffman.decode(compressData, rootTree);
    Print.print1DArray("depressed data: ", depressData);

    short[] decodeData = moveToFrontList.decode(depressData);
    Print.print1DArray("decode data: ", oneD);
    short[][] component = hilbertCurve.decode(decodeData);

    Print.print2DArray("Component data", component);
    System.out.println(Logic.compare2D(twoD, component));
  }
}
