package algorithm;

import utils.Logic;
import utils.Print;

import java.util.*;

public class Huffman {

  public static class Node implements Comparable {
    public int value;
    public int frequent;
    public Node left, right;

    public Node(int value, int frequent) {
      this.value = value;
      this.frequent = frequent;
      this.left = null;
      this.right = null;
    }

    public Node(Node left, Node right) {
      this.left = left;
      this.right = right;
      this.frequent = left.frequent + right.frequent;
    }

    public Node(int value) {
      this.value = value;
      this.left = null;
      this.right = null;
    }

    @Override
    public int compareTo(Object p) {
      return this.frequent - ((Node) p).frequent;
    }
  }

  public Node tree;
  public int[] label;
  public int N;

  private Hashtable<Integer, Node> getFrequent(int[] data) {
    Hashtable<Integer, Node> frequentSet = new Hashtable<Integer, Node>();
    Node p;
    ArrayList<Integer> zeroFrequent = new ArrayList<>();
    zeroFrequent.add(0);
    for (int i = 0; i < data.length; i++) {
      if (i != 0 && data[i] == 0) {
        if (data[i] == data[i - 1])
          zeroFrequent.set(zeroFrequent.size() - 1, zeroFrequent.get(zeroFrequent.size() - 1) + 1);
        else {
//          System.out.println("zero size: " + zeroFrequent.get(zeroFrequent.size() - 1));
          zeroFrequent.add(1);
        }

      }
      int key = data[i];
      if (frequentSet.containsKey(key)) {
        p = frequentSet.get(key);
        p.frequent += 1;
      } else {
        p = new Node(data[i], 1);
        frequentSet.put(key, p);
      }
    }
//    for (Integer e : zeroFrequent) {
//      System.out.println("zero size: " + e);
//    }
    this.label = new int[frequentSet.size()];
    return frequentSet;
  }

  private Hashtable<Integer, String> getEncodeSet(Node rootTree, String encodeString, Hashtable<Integer, String> encodeSet) {
    if (rootTree.left == null) {
      this.label[this.N++] = rootTree.value;
      encodeSet.put(rootTree.value, encodeString);
    } else {
      encodeSet = getEncodeSet(rootTree.left, encodeString + "0", encodeSet);
      encodeSet = getEncodeSet(rootTree.right, encodeString + "1", encodeSet);
    }
    return encodeSet;
  }

  public Node buildTree(int[] data) {
    // frequentSet store an set of data: Node { frequent }
    Hashtable<Integer, Node> frequentSet = getFrequent(data);
    PriorityQueue<Node> tree = new PriorityQueue<Node>();

    // add frequentSet to huffman tree
    frequentSet.forEach((key, value) -> {
      tree.offer(value);
    });

    // build huffman tree
    while (tree.size() > 1) {
      // pull out 2 least frequent data
      Node first = tree.poll();
      Node second = tree.poll();
      // create a new internal node as a parent of two least frequent nodes
      Node internalNode = new Node(first, second);
      // add new internal node to the tree
      tree.offer(internalNode);
    }

    // pull out a root of the tree (right now the tree just has only 1 node left
    return tree.poll();
  }

  public String encode(int[] data) {
    this.N = 0;
    this.tree = buildTree(data);

    Hashtable<Integer, String> encodeSet = new Hashtable<Integer, String>();
    encodeSet = getEncodeSet(this.tree, "", encodeSet);

//    encodeSet.forEach((key, value) -> {
//      System.out.println(key + ": " + value);
//    });

    // create encode data by loop through all element in data, transform every element to its encode set
    StringBuilder encodeData = new StringBuilder();

    for (int i = 0; i < data.length; i++) {
      encodeData.append(encodeSet.get(data[i]));
    }
    System.out.println("Encode string length: " + encodeData.length());
    return encodeData.toString();
  }

  public int[] decode(String encodeData, Node rootTree) {
    ArrayList<Integer> decodeData = new ArrayList<Integer>();
    Node p = rootTree;
    for (int i = 0; i < encodeData.length(); i++) {
      p = encodeData.charAt(i) == '0' ? p.left : p.right;
      if (p.left == null) {
        decodeData.add(p.value);
        p = rootTree;
      }
    }
    int[] data = new int[decodeData.size()];
    for (int i = 0; i < decodeData.size(); i++) {
      data[i] = decodeData.get(i);
    }
    return data;
  }

  public void travel(Node p) {
    if (p.left == null) {
      System.out.println("value: " + p.value + ", frequent: " + p.frequent);
    } else {
      travel(p.left);
      travel(p.right);
    }
  }

  public static void main(String[] argv) {
    Huffman huffman = new Huffman();
    int[] data = {1, 2, 1, 3, 5, 7, 1};
//    Print.<int>print1DArray("origin data: ", data);
    String encodeData = huffman.encode(data);
    System.out.println("encode: ");
    System.out.println(encodeData);

//    Print.print1DArray("labels: ", huffman.label);
    CompressSystem compressSystem = new CompressSystem(huffman.tree, huffman.label, encodeData);

    compressSystem.save("./images/01.hil");

    huffman.travel(compressSystem.tree);

    compressSystem.load("./images/01.hil");

    huffman.travel(compressSystem.huffmanTree);

    int[] decodeData = huffman.decode(encodeData, compressSystem.huffmanTree);
//    Print.print1DArray("decode data: ", decodeData);
    System.out.println(Logic.compare1D(data, decodeData));


  }
}
