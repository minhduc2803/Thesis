import algorithm.CompressData;
import algorithm.Huffman;
import algorithm.MoveToFrontList;
import linearize.HilbertCurve;
import utils.Logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {
  public static void main(String[] args) {
    String tifImagePath = "./images/01.tif";
    String pngImagePath = "./images/01.png";
    File inputFile = new File(tifImagePath);
    File pngFile = new File(pngImagePath);
    try {
      BufferedImage image = ImageIO.read(inputFile);
      ImageIO.write(image, "png", pngFile);
      int width = image.getWidth();
      int height = image.getHeight();
      System.out.println("width: " + width + ", height: " + height);

      int[][] dataRed = new int[1024][1024];
      int[][] dataGreen = new int[1024][1024];
      int[][] dataBlue = new int[1024][1024];

      for (int i = 0; i < width; i++)
        for (int j = 0; j < height; j++) {
          int color = image.getRGB(i, j);
          dataRed[i][j] = color & 0xff;
          dataGreen[i][j] = (color & 0xff00) >> 8;
          dataBlue[i][j] = (color & 0xff0000) >> 16;
        }


//    for (String format : ImageIO.getWriterFormatNames()) {
//      System.out.println("format = " + format);
//    }

      int[][] twoD = {{1, 2, 3, 4}, {1, 2, 6, 7}, {2, 1, 8, 10}, {1, 12, 13, 14}};
//    Print.print2DArray("Origin data", twoD);

      HilbertCurve hilbertCurve = new HilbertCurve();
      MoveToFrontList moveToFrontList = new MoveToFrontList();
      Huffman huffman = new Huffman();

      System.out.println("Hilbert encode...");
      int[] oneDRed = hilbertCurve.encode(dataRed);
      int[] oneDGreen = hilbertCurve.encode(dataGreen);
      int[] oneDBlue = hilbertCurve.encode(dataBlue);
      int[] oneD = new int[1024 * 1024 * 3];
      int redLimit = 1024 * 1024;
      int greenLimit = redLimit * 2;
      int blueLimit = redLimit * 3;
      for (int i = 0; i < redLimit; i++) {
        oneD[i] = oneDRed[i];
      }
      for (int i = redLimit; i < greenLimit; i++) {
        oneD[i] = oneDGreen[i - redLimit];
      }
      for (int i = greenLimit; i < blueLimit; i++) {
        oneD[i] = oneDBlue[i - greenLimit];
      }
//    Print.print1DArray("1D array: ", oneD);
      System.out.println("Move to front list encode...");
      int[] encodeData = moveToFrontList.encode(oneD);
//    Print.print1DArray("encode data: ", oneD);
      System.out.println("Huffman encode...");
      String compressData = huffman.encode(encodeData);
//    System.out.println("compressed data: " + compressData);

      String hilFilePath = "./images/01.hil";

      CompressData compressSystem = new CompressData(huffman.tree, huffman.label, compressData);

      compressSystem.save(hilFilePath);

//      huffman.travel(huffman.tree);

      compressSystem.load(hilFilePath);

      String afterCompressData = compressSystem.data;
      Huffman.Node huffmanTree = compressSystem.huffmanTree;

      System.out.println("Huffman decode...");
//      Huffman.Node rootTree = Huffman.buildTree(encodeData);
      int[] depressData = huffman.decode(afterCompressData, huffmanTree);
//    Print.print1DArray("depressed data: ", depressData);
      System.out.println("Move to front list decode...");
      int[] decodeData = moveToFrontList.decode(depressData);
//    Print.print1DArray("decode data: ", oneD);

      int[] decodeRed = new int[1024 * 1024];
      int[] decodeGreen = new int[1024 * 1024];
      int[] decodeBlue = new int[1024 * 1024];
      for (int i = 0; i < redLimit; i++) {
        decodeRed[i] = decodeData[i];
      }
      for (int i = redLimit; i < greenLimit; i++) {
        decodeGreen[i - redLimit] = decodeData[i];
      }
      for (int i = greenLimit; i < blueLimit; i++) {
        decodeBlue[i - greenLimit] = decodeData[i];
      }

      System.out.println("Hilbert decode...");
      int[][] componentRed = hilbertCurve.decode(decodeRed);
      int[][] componentGreen = hilbertCurve.decode(decodeGreen);
      int[][] componentBlue = hilbertCurve.decode(decodeBlue);

//    Print.print2DArray("Component data", component);
      System.out.println(Logic.compare2D(dataRed, componentRed));
      System.out.println(Logic.compare2D(dataGreen, componentGreen));
      System.out.println(Logic.compare2D(dataBlue, componentBlue));

      BufferedImage saveImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          int rgb = componentRed[i][j] | componentGreen[i][j] << 8 | componentBlue[i][j] << 16;
          saveImg.setRGB(i, j, rgb);
        }
      }
      File saveFile = new File("./images/01-output.tif");
      ImageIO.write(saveImg, "tif", saveFile);

      System.out.println("Working Directory = " + System.getProperty("user.dir"));

    } catch (Exception e) {
      System.out.println(e);
      System.out.println(e.getMessage());
    }
  }
}
