package compress;

public class MoveToFrontList {
  public Node mutableHeadList;
  public Node headList;
  public int[] universeData;

  public MoveToFrontList(int[] universeData) {
    this.universeData = universeData;
  }

  public MoveToFrontList() {
    int[] data = new int[256];
    for (int i = 0; i < 256; i++)
      data[i] = (int) i;
    this.universeData = data;
  }

  public void setUniverseData(int[] data) {
    this.universeData = data;
  }

  private void createList() {
    Node head = new Node(universeData[0]);
    Node tail = head;
    for (int i = 1; i < universeData.length; i++) {
      Node p = new Node(universeData[i]);
      tail.next = p;
      tail = p;
    }
//    Print.printList("List", head);
    this.headList = head;
  }

  private int findPosition(int value) {
    if (headList.value == value) {
      return 0;
    }
    int position = 1;
    Node pre = headList;
    Node p = headList.next;

    while (p.value != value) {
      position++;
      pre = p;
      p = p.next;
    }
    pre.next = p.next;
    p.next = headList;
    headList = p;
    return position;
  }

  private int findValue(int position) {
    if (position == 0) {
      return headList.value;
    }
    Node pre = headList;
    Node p = pre.next;
    for (int i = 1; i < position; i++) {
      pre = p;
      p = p.next;
    }
    pre.next = p.next;
    p.next = headList;
    headList = p;
    return p.value;
  }

  public int[] encode(int[] origin) {
    createList();
    int[] encodeData = new int[origin.length];
    System.out.println("origin length: " + origin.length);
    for (int i = 0; i < origin.length; i++) {
      int d = findPosition(origin[i]);
      encodeData[i] = d;
    }
//    Print.printList("List after encode", this.headList);
    return encodeData;
  }

  public int[] decode(int[] encodeData) {
    createList();
    int[] decodeData = new int[encodeData.length];
    for (int i = 0; i < encodeData.length; i++) {
      decodeData[i] = findValue(encodeData[i]);
    }
//    Print.printList("List after decode", this.headList);
    return decodeData;
  }

  public static class Node {
    public int value;
    public Node next;

    public Node(int value) {
      this.value = value;
      this.next = null;
    }

    public Node() {
      this.next = null;
    }
  }
}
