package encode;

public class Node {
  public short value;
  public Node next;

  public Node(short value) {
    this.value = value;
    this.next = null;
  }

  public Node() {
    this.next = null;
  }
}
