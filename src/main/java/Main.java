import app.App;
import linearize.Curve;
import linearize.HilbertCurve;
import linearize.ZCurve;
import utils.Logic;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {
  public static ArrayList<Double> getAverageRatio(Curve curve) {
    double totalRatio = 0;
    ArrayList<Double> compressRatio = new ArrayList<>();
    long totalTime = System.nanoTime();
    for (int i = 1; i <= 24; i++) {
      App app = new App(curve);
      String name = String.valueOf(i);
      if (i < 10) {
        name = "0" + name;
      }
      System.out.println("------------------------------------Image Number: " + name + "---------------------------------------");
      String imgFile = "./images/" + name + ".tif";
      String compressedFile = "./images/" + name + ".hil";
      String extractedFile = "./images/" + name + ".ex";
      app.compress(imgFile, compressedFile);
      app.extract(compressedFile, extractedFile);

      boolean result = Logic.compareImgFile(imgFile, extractedFile);
      System.out.println("Result: " + (result ? "\u001B[32m" : "\u001B[31m") + result + "\u001B[0m");

      long imgFileSize = Logic.getFileSize(imgFile);
      long compressedFileSize = Logic.getFileSize(compressedFile);
      System.out.println("Original file size: " + imgFileSize);
      System.out.println("Compressed file size: " + compressedFileSize);
      System.out.println("Compressed ratio: " + (double) compressedFileSize / imgFileSize);
      compressRatio.add((double) compressedFileSize / imgFileSize);
    }
    System.out.println("Total time: " + (System.nanoTime() - totalTime) / 1000000);
    System.out.println("Average compressed ratio: " + totalRatio / 24);
    return compressRatio;
  }

  public static void testOneImage(String name) {
    App app = new App(new HilbertCurve());
    System.out.println("Image Number: " + name);
    String imgFile = "./images/" + name + ".tif";
    String compressedFile = "./images/" + name + ".hil";
    String extractedFile = "./images/" + name + ".ex";
    app.compress(imgFile, compressedFile);
    app.extract(compressedFile, extractedFile);

    System.out.println("Result: " + Logic.compareImgFile(imgFile, extractedFile));

    long imgFileSize = Logic.getFileSize(imgFile);
    long compressedFileSize = Logic.getFileSize(compressedFile);
    System.out.println("Original file size: " + imgFileSize);
    System.out.println("Compressed file size: " + compressedFileSize);
    System.out.println("Compressed ratio: " + (double) compressedFileSize / imgFileSize);
  }

  public static void main(String[] args) {
//      testOneImage("15");
    ArrayList<Double> hilbertRatio = getAverageRatio(new HilbertCurve());
    ArrayList<Double> zCurveRatio = getAverageRatio(new ZCurve());

    for (int i = 0; i < hilbertRatio.size(); i++) {
      System.out.println(hilbertRatio.get(i) / zCurveRatio.get(i));
    }
//    System.out.println(hilbertRatio);
//    System.out.println(zCurveRatio);
  }


}
