import encode.Huffman;
import encode.MoveToFrontList;
import linearize.HilbertCurve;
import utils.Logic;
import utils.Print;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {
  public static void main(String[] args) {
    String tifImagePath = "./images/01.tif";
    String pngImagePath = "./images/01.png";
    File inputFile = new File(tifImagePath);
    File outputFile = new File(pngImagePath);
    try {
      BufferedImage image = ImageIO.read(inputFile);
      ImageIO.write(image, "png", outputFile);
      int width = image.getWidth();
      int height = image.getHeight();
      System.out.println("width: " + width + ", height: " + height);
      int[][] intData = new int[1024][1024];
      int[][] data = new int[1024][1024];

      for (int i = 0; i < width; i++)
        for (int j = 0; j < height; j++)
          data[i][j] = image.getRGB(i, j) & 255;

//    for (String format : ImageIO.getWriterFormatNames()) {
//      System.out.println("format = " + format);
//    }

      int[][] twoD = {{1, 2, 3, 4}, {1, 2, 6, 7}, {2, 1, 8, 10}, {1, 12, 13, 14}};
//    Print.print2DArray("Origin data", twoD);

      HilbertCurve hilbertCurve = new HilbertCurve();
      MoveToFrontList moveToFrontList = new MoveToFrontList();

      System.out.println("Hilbert encode...");
      int[] oneD = hilbertCurve.encode(data);
//    Print.print1DArray("1D array: ", oneD);
      System.out.println("Move to front list encode...");
      int[] encodeData = moveToFrontList.encode(oneD);
//    Print.print1DArray("encode data: ", oneD);
      System.out.println("Huffman encode...");
      String compressData = Huffman.encode(encodeData);
//    System.out.println("compressed data: " + compressData);
      System.out.println("Huffman decode...");
      Huffman.Node rootTree = Huffman.buildTree(encodeData);
      int[] depressData = Huffman.decode(compressData, rootTree);
//    Print.print1DArray("depressed data: ", depressData);
      System.out.println("Move to front list decode...");
      int[] decodeData = moveToFrontList.decode(depressData);
//    Print.print1DArray("decode data: ", oneD);
      System.out.println("Hilbert decode...");
      int[][] component = hilbertCurve.decode(decodeData);

//    Print.print2DArray("Component data", component);
      System.out.println(Logic.compare2D(data, component));

      System.out.println("Working Directory = " + System.getProperty("user.dir"));

    } catch (Exception e) {
      System.out.println(e);
      System.out.println(e.getMessage());
    }
  }
}
