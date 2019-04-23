/*
 * @lc app=leetcode id=146 lang=java
 *
 * [146] LRU Cache
 *
 * https://leetcode.com/problems/lru-cache/description/
 *
 * algorithms
 * Hard (24.68%)
 * Total Accepted:    277.9K
 * Total Submissions: 1.1M
 * Testcase Example:  '["LRUCache","put","put","get","put","get","put","get","get","get"]\n[[2],[1,1],[2,2],[1],[3,3],[2],[4,4],[1],[3],[4]]'
 *
 * 
 * Design and implement a data structure for Least Recently Used (LRU) cache.
 * It should support the following operations: get and put.
 * 
 * 
 * 
 * get(key) - Get the value (will always be positive) of the key if the key
 * exists in the cache, otherwise return -1.
 * put(key, value) - Set or insert the value if the key is not already present.
 * When the cache reached its capacity, it should invalidate the least recently
 * used item before inserting a new item.
 * 
 * 
 * Follow up:
 * Could you do both operations in O(1) time complexity?
 * 
 * Example:
 * 
 * LRUCache cache = new LRUCache(2); // 2 is capacity
 * 
 * cache.put(1, 1);
 * cache.put(2, 2);
 * cache.get(1);       // returns 1
 * cache.put(3, 3);    // evicts key 2
 * cache.get(2);       // returns -1 (not found)
 * cache.put(4, 4);    // evicts key 1
 * cache.get(1);       // returns -1 (not found)
 * cache.get(3);       // returns 3
 * cache.get(4);       // returns 4
 * 
 * 
 */
class LRUCache {

  private int capacity;
  // Use a map <key=item's key, value=the address of the ListNode storing the 
  // item's value> and a doubly linked list. The head is the most recently
  // used item's value and the tail is the least recently used item's value
  private Map<Integer, ListNode> map;
  private ListNode head;
  private ListNode tail;

  // doubly linked list
  // NOTE: here this nested class can also be static because this nested class
  // won't use any members of its enclosing class
  private class ListNode {
    int key;
    int val;
    ListNode next;
    ListNode prev;

    ListNode(int key, int val) {
      this.key = key;
      this.val = val;
    }

    void setValue(int val) {
      this.val = val;
    }

    int getKey() {
      return key;
    }

    int getValue() {
      return val;
    }
  }

  public LRUCache(int capacity) {
    if (capacity < 1) {
      throw new IllegalArgumentException("Capacity must be larger than 1.");
    }
    this.capacity = capacity;
    map = new HashMap<Integer, ListNode>();
    //  dummy head and dummy tail
    head = new ListNode(-1, -1);
    tail = new ListNode(-1, -1);
    head.next = tail;
    tail.prev = head;
  }
  
  public int get(int key) {
    ListNode node = map.get(key);
    if (node == null) {
      return -1;
    }
    moveToHead(node);
    return node.getValue();
  }
  
  public void put(int key, int value) {
    ListNode node = map.get(key);
    // if the key already exists, update its value and move its corresponding
    // node to the head of the linked list
    if (node != null) {
      node.setValue(value);
      moveToHead(node);
      return;
    }
    ListNode newNode = new ListNode(key, value);
    // if reach the capacity, invalidate the least recently used item (by
    // remove the last node of the linked list and its corresponding entry
    // in the hashmap)
    if (map.size() == capacity) {
      map.remove(tail.prev.getKey());
      removeNode(tail.prev);
    }
    map.put(key, newNode);
    insertToHead(newNode);
  }

  // insert a node to the beginning of the linked list
  private void insertToHead(ListNode node) {
    node.next = head.next;
    head.next.prev = node;
    head.next = node;
    node.prev = head;
  }

  // remove the node from linked list
  private void removeNode(ListNode node) {
    node.prev.next = node.next;
    node.next.prev = node.prev;
  }

  // move the node to the head of the linked list
  private void moveToHead(ListNode node) {
    if (head.next != node) {
      removeNode(node);
      insertToHead(node);
    }
  }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */

