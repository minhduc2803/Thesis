import app.App;
import utils.Logic;

public class Main {
  public static void getAverageRatio() {
    double totalRatio = 0;
    for (int i = 1; i <= 24; i++) {
      App app = new App();
      String name = String.valueOf(i);
      if (i < 10) {
        name = "0" + name;
      }
      System.out.println("------------------------------------Image Number: " + name+"---------------------------------------");
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
      totalRatio += (double) compressedFileSize / imgFileSize;
    }
    System.out.println("Average compressed ratio: " + totalRatio / 24);
  }

  public static void testOneImage(String name) {
    App app = new App();
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
    getAverageRatio();
  }


}
