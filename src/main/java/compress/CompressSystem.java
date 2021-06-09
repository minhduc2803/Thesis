package compress;

import java.io.*;
import java.nio.file.Files;
import java.util.Stack;

public class CompressSystem {
  public Huffman.Node tree;
  public int[] label;
  public String data;
  public byte[] array;
  public byte[] huffmanByte;
  public Huffman.Node huffmanTree;
  public int currentNodePosition;
  public int currentLabelPosition;
  public int currentDecodeDataPosition;
  public int firstDataPosition;
  public int finalDecodeDataPosition;
  public FileOutputStream fileWriter;
  public BufferedOutputStream bufferedWriter;

  public byte oneByte;

  public CompressSystem(Huffman.Node tree, int[] label, String data) {
    this.tree = tree;
    this.data = data;
    this.label = label;
  }

  public CompressSystem() {

  }

  private void saveHuffmanTree() throws Exception {
    Stack<Huffman.Node> stack = new Stack<Huffman.Node>();

    Huffman.Node p = this.tree;
    int oneByte = 0;
    for (int i = 0; p != null || stack.size() > 0; ) {
      while (p != null) {
        stack.push(p);
        if (p.left != null) {
          oneByte |= 1 << (i % 8);
        }
        if ((i + 1) % 8 == 0) {
          this.bufferedWriter.write(oneByte);
          oneByte = 0;
        }
        i++;
        p = p.left;

      }
      p = stack.pop();

      p = p.right;
    }
    if ((2 * label.length - 1) % 8 != 0) {
      this.bufferedWriter.write(oneByte);
    }

  }

  public void save(String filePath) {
    try {
      this.fileWriter = new FileOutputStream(filePath);
      this.bufferedWriter = new BufferedOutputStream(this.fileWriter);

      // first character is a number of labels
      this.bufferedWriter.write(label.length - 1);
      System.out.println("Number of labels " + label.length);
      // next is list of labels
      System.out.println("first label " + label[0]);
      for (int i = 0; i < label.length; i++) {
        this.bufferedWriter.write(label[i]);
      }

      // next is the huffman tree
      saveHuffmanTree();

      // first byte indicates the number of used bits of the last byte
      this.bufferedWriter.write((data.length() % 8));
      System.out.println("Offset " + (data.length() % 8));
      oneByte = 0;
      for (int i = 0; i < data.length(); i++) {
        if (data.charAt(i) == '1') {
          oneByte |= 1 << (i % 8);
        }
        if ((i + 1) % 8 == 0) {
          this.bufferedWriter.write(oneByte);
          oneByte = 0;
        }

      }
      if (data.length() % 8 != 0) {
        this.bufferedWriter.write(oneByte);
      }


      this.bufferedWriter.close();
    } catch (Exception e) {
      System.out.println("Error occurred when save compressed data to file");
      System.out.println(e);
    }

  }

  private int getBitHuffman() {
    int bytePosition = this.currentNodePosition / 8;
    int bitPosition = this.currentNodePosition % 8;
    this.currentNodePosition++;
    byte b = this.huffmanByte[bytePosition];
    return (b >> bitPosition) & 1;
  }

  private int getBitDecodeData() {
    int bytePosition = (this.currentDecodeDataPosition) / 8;
    int bitPosition = this.currentDecodeDataPosition % 8;
    byte b = this.array[bytePosition];
    return (b >> bitPosition) & 1;
  }

  private Huffman.Node loadHuffmanTree() {
    if (getBitHuffman() == 0) {
      int value = this.label[this.currentLabelPosition];
      this.currentLabelPosition++;
      return new Huffman.Node(value);
    } else {
      Huffman.Node left = loadHuffmanTree();
      Huffman.Node right = loadHuffmanTree();
      Huffman.Node root = new Huffman.Node(left, right);
      return root;
    }
  }

  public void load(String filePath) {
    try {
      File file = new File(filePath);
      this.array = Files.readAllBytes(file.toPath());
      // the first character is the length of labels
      System.out.println("Number of bytes " + array.length);
      int numberLabels = (array[0] & 0xff) + 1;
      this.label = new int[numberLabels];
      System.out.println("Number of labels " + numberLabels);
      System.out.println("first label " + array[1]);
      for (int i = 0; i < this.label.length; i++) {
        this.label[i] = array[i + 1] & 0xff;
      }

      // get Huffman tree
      // the Huffman tree's length is 2N - 1, N is length labels -> the byte length (2N - 1)/8 + 1
      int treeLength = (2 * label.length - 1) / 8 + 1;
      int firstBytePosition = this.label.length + 1;
      this.huffmanByte = new byte[treeLength];
      for (int i = 0; i < treeLength; i++) {
        this.huffmanByte[i] = this.array[firstBytePosition + i];
      }
      this.currentNodePosition = 0;
      this.currentLabelPosition = 0;
      this.huffmanTree = loadHuffmanTree();

      // get data
      // the next byte is the final byte offset
      this.firstDataPosition = this.label.length + 1 + treeLength;
      int offset = this.array[firstDataPosition];
      if(offset == 0) offset = 8;
      System.out.println("Offset " + offset);
      this.firstDataPosition++;

      this.currentDecodeDataPosition = this.firstDataPosition * 8;
      this.finalDecodeDataPosition = this.array.length * 8 - 8 + offset;

      StringBuilder stringBuilder = new StringBuilder();
      while (this.currentDecodeDataPosition < this.finalDecodeDataPosition) {
        stringBuilder.append(getBitDecodeData());
        this.currentDecodeDataPosition++;
      }
      this.data = stringBuilder.toString();


    } catch (Exception e) {
      System.out.println("Error occurred when load compressed data from file");
      System.out.println(e.getMessage());
    }

  }

  public static void testWrite(BufferedOutputStream bufferedOutputStream) {
    try {
      bufferedOutputStream.write(1);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

  public static void main(String[] args) {
    try {
      FileOutputStream fileOutputStream = new FileOutputStream("./images/01.hil");
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
      byte test = (byte) 249;
      bufferedOutputStream.write(3);
      CompressSystem.testWrite(bufferedOutputStream);
      bufferedOutputStream.close();

      File file = new File("./images/01.hil");
      byte[] array = Files.readAllBytes(file.toPath());
      for (int i = 0; i < array.length; i++) {
        System.out.println(i + ": " + array[i]);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
