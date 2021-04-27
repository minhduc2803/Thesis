import app.App;
import utils.Logic;

public class Main {
  public static void main(String[] args) {
    App app = new App();
    String imgFile = "./images/02.tif";
    String compressedFile = "./images/02.hil";
    String extractedFile = "./images/02.ex";
    app.compress(imgFile, compressedFile);
    app.extract(compressedFile, extractedFile);

    System.out.println("Result: " + Logic.compareImgFile(imgFile, extractedFile));

    long imgFileSize = Logic.getFileSize(imgFile);
    long compressedFileSize = Logic.getFileSize(compressedFile);
    System.out.println("Original file size: " + imgFileSize);
    System.out.println("Compressed file size: " + compressedFileSize);
    System.out.println("Compressed ratio: " + (double)compressedFileSize/imgFileSize);
  }
}
