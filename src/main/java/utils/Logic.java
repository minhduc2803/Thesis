package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Logic {
  public static boolean compare2D(int[][] origin, int[][] component) {
    int m = origin.length;
    int n = origin[0].length;
    if (m != component.length || n != component[0].length)
      return false;

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        if (origin[i][j] != component[i][j])
          return false;
      }
    }
    return true;
  }

  public static boolean compare1D(int[] origin, int[] component) {
    if (origin.length != component.length)
      return false;
    for (int i = 0; i < origin.length; i++) {
      if (origin[i] != component[i])
        return false;
    }
    return true;
  }

  public static boolean compareImg(BufferedImage imgA, BufferedImage imgB) {
    int width = imgA.getWidth();
    int height = imgA.getHeight();
    if (imgB.getWidth() != width || imgB.getHeight() != height)
      return false;

    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (imgA.getRGB(i, j) != imgB.getRGB(i, j))
          return false;
      }
    }

    return true;
  }

  public static boolean compareImgFile(String fileNameA, String fileNameB) {
    File fileA = new File(fileNameA);
    File fileB = new File(fileNameB);
    try {
      BufferedImage imgA = ImageIO.read(fileA);
      BufferedImage imgB = ImageIO.read(fileB);

      return compareImg(imgA, imgB);
    } catch (Exception e) {
      System.out.println("Error occurred in Logic -> compare two image files");
      System.out.println(e.getMessage());
      return false;
    }
  }

  public static long getFileSize(String filePath) {
    File file = new File(filePath);
    return file.length();
  }

  public static int[] copyArray1D(int[] origin) {
    int[] copyArray = new int[origin.length];
    for(int i=0;i<origin.length;i++) {
      copyArray[i] = origin[i];
    }
    return copyArray;
  }
}
