package encode;

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

    @Override
    public int compareTo(Object p) {
      return this.frequent - ((Node) p).frequent;
    }
  }

  private static Hashtable<Integer, Node> getFrequent(int[] data) {
    Hashtable<Integer, Node> frequentSet = new Hashtable<Integer, Node>();
    Node p;
    for (int i = 0; i < data.length; i++) {
      int key = data[i];
      if (frequentSet.containsKey(key)) {
        p = frequentSet.get(key);
        p.frequent += 1;
      } else {
        p = new Node(data[i], 1);
        frequentSet.put(key, p);
      }
    }
    return frequentSet;
  }

  private static Hashtable<Integer, String> getEncodeSet(Node rootTree, String encodeString, Hashtable<Integer, String> encodeSet) {
    if (rootTree.left == null) {
      encodeSet.put(rootTree.value, encodeString);
    } else {
      encodeSet = getEncodeSet(rootTree.left, encodeString + "0", encodeSet);
      encodeSet = getEncodeSet(rootTree.right, encodeString + "1", encodeSet);
    }
    return encodeSet;
  }

  public static Node buildTree(int[] data) {
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

  public static String encode(int[] data) {

    Node rootTree = buildTree(data);

    Hashtable<Integer, String> encodeSet = new Hashtable<Integer, String>();
    encodeSet = getEncodeSet(rootTree, "", encodeSet);

    encodeSet.forEach((key, value) -> {
//      System.out.println(key + ": " + value);
    });

    // create encode data by loop through all element in data, transform every element to its encode set
    StringBuilder encodeData = new StringBuilder();
    long start = System.nanoTime();
    for (int i = 0; i < data.length; i++) {
      encodeData.append(encodeSet.get(data[i]));
    }
    System.out.println("Encode string length: " + encodeData.length());
    System.out.println("Time execute encode data into string: " + (System.nanoTime() - start));
    return encodeData.toString();
  }

  public static int[] decode(String encodeData, Node rootTree) {
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

  public static void main(String[] argv) {
    int[] data = {1, 2, 1, 3, 5, 1, 2, 7, 5, 2, 1};
    Print.print1DArray("origin data: ", data);
    String encodeData = Huffman.encode(data);
    System.out.println("encode: ");
    System.out.println(encodeData);

    Node rootTree = Huffman.buildTree(data);
    int[] decodeData = Huffman.decode(encodeData, rootTree);
    Print.print1DArray("decode data: ", decodeData);
    System.out.println(Logic.compare1D(data, decodeData));


  }
}
