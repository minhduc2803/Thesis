package app;

import algorithm.CompressSystem;
import algorithm.Huffman;
import algorithm.MoveToFrontList;
import algorithm.RunLength;
import linearize.HilbertCurve;
import utils.Logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class App {
  public HilbertCurve hilbertCurve;
  public MoveToFrontList moveToFrontList;
  public RunLength runLength;
  public Huffman huffman;

  int redLimit = 1024 * 1024;
  int greenLimit = redLimit * 2;
  int blueLimit = redLimit * 3;

  int width, height;

  public App() {
    hilbertCurve = new HilbertCurve();
    moveToFrontList = new MoveToFrontList();
    runLength = new RunLength();
    huffman = new Huffman();
  }
  public void compress(String originFileName, String compressedFileName) {
    File inputFile = new File(originFileName);
    try {
      BufferedImage image = ImageIO.read(inputFile);
      int width = image.getWidth();
      int height = image.getHeight();
      this.width = width;
      this.height = height;
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
      int[] moveToFrontEncodeData = moveToFrontList.encode(oneD);
      System.out.println("Move to front list encode length: "+ moveToFrontEncodeData.length);
//    Print.print1DArray("encode data: ", moveToFrontEncodeData);

      System.out.println("Run length encode...");
      int[] runLengthEncodeData = runLength.encode(moveToFrontEncodeData);
      System.out.println("Run length encode length: "+ runLengthEncodeData.length);

      System.out.println("Huffman encode...");
      String compressData = huffman.encode(runLengthEncodeData);

//    System.out.println("compressed data: " + compressData);

      CompressSystem compressSystem = new CompressSystem(huffman.tree, huffman.label, compressData);

      compressSystem.save(compressedFileName);

    } catch (Exception e) {
      System.out.println("Error occurred in app.App -> compress method");
      System.out.println(e.getMessage());
    }
  }

  public void extract(String compressedFileName, String extractFileName) {
    CompressSystem compressSystem = new CompressSystem();
    compressSystem.load(compressedFileName);

    String afterCompressData = compressSystem.data;
    System.out.println("Huffman string length: "+ afterCompressData.length());
    Huffman.Node huffmanTree = compressSystem.huffmanTree;

    System.out.println("Huffman decode...");
//      Huffman.Node rootTree = Huffman.buildTree(encodeData);
    int[] huffmanDecodeData = huffman.decode(afterCompressData, huffmanTree);
    System.out.println("Huffman decode length: "+ huffmanDecodeData.length);
//    Print.print1DArray("depressed data: ", depressData);

    System.out.println("Run length decode...");
    int[] runLengthDecodeData = runLength.decode(huffmanDecodeData);
    System.out.println("Run length decode length: "+ runLengthDecodeData.length);
    System.out.println("Move to front list decode...");
    int[] decodeData = moveToFrontList.decode(runLengthDecodeData);
    System.out.println("Move to front list decode length: "+ decodeData.length);
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

    BufferedImage saveImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int rgb = componentRed[i][j] | componentGreen[i][j] << 8 | componentBlue[i][j] << 16;
        saveImg.setRGB(i, j, rgb);
      }
    }
    File saveFile = new File(extractFileName);
    try {
      ImageIO.write(saveImg, "tif", saveFile);
    } catch(Exception e) {
      System.out.println("Error occurred in app.App -> extract save file method");
      System.out.println(e.getMessage());
    }

  }

  public static void main(String[] args) {
    App app = new App();
    app.compress("./images/02.tif", "./images/02.hil");
    app.extract("./images/02.hil", "./images/02.ex");

    System.out.println("Result: " + Logic.compareImgFile("./images/02.tif", "./images/02.ex"));
  }
}
