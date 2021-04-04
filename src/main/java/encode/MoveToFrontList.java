package encode;

import utils.Print;

public class MoveToFrontList {
  public Node mutableHeadList;
  public Node headList;
  public short[] universeData;

  public MoveToFrontList(short[] universeData) {
    this.universeData = universeData;
  }

  public MoveToFrontList() {
    short[] data = new short[256];
    for (int i = 0; i < 256; i++)
      data[i] = (short) i;
    this.universeData = data;
  }

  public void setUniverseData(short[] data) {
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
    Print.printList("List", head);
    this.headList = head;
  }

  private short findPosition(short value) {
    if (headList.value == value) {
      return 0;
    }
    short position = 1;
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

  private short findValue(short position) {
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

  public short[] encode(short[] origin) {
    createList();
    short[] encodeData = new short[origin.length];

    for (int i = 0; i < origin.length; i++) {
      encodeData[i] = findPosition(origin[i]);
    }
    Print.printList("List after encode", this.headList);
    return encodeData;
  }

  public short[] decode(short[] encodeData) {
    createList();
    short[] decodeData = new short[encodeData.length];
    for (int i = 0; i < encodeData.length; i++) {
      decodeData[i] = findValue(encodeData[i]);
    }
    Print.printList("List after decode", this.headList);
    return decodeData;
  }
}
