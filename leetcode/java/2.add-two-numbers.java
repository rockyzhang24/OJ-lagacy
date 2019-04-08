/*
 * @lc app=leetcode id=2 lang=java
 *
 * [2] Add Two Numbers
 *
 * https://leetcode.com/problems/add-two-numbers/description/
 *
 * algorithms
 * Medium (30.86%)
 * Total Accepted:    819.7K
 * Total Submissions: 2.7M
 * Testcase Example:  '[2,4,3]\n[5,6,4]'
 *
 * You are given two non-empty linked lists representing two non-negative
 * integers. The digits are stored in reverse order and each of their nodes
 * contain a single digit. Add the two numbers and return it as a linked list.
 * 
 * You may assume the two numbers do not contain any leading zero, except the
 * number 0 itself.
 * 
 * Example:
 * 
 * 
 * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
 * Output: 7 -> 0 -> 8
 * Explanation: 342 + 465 = 807.
 * 
 * 
 */
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
  public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    int carry = 0;
    ListNode dummy = new ListNode(0);
    ListNode cur = dummy;
    while (l1 != null && l2 != null) {
      int sum = l1.val + l2.val + carry;
      cur.next = new ListNode(sum % 10);
      carry = sum / 10;
      cur = cur.next;
      l1 = l1.next;
      l2 = l2.next;
    }
    ListNode l = l1 == null ? l2 : l1;
    while (l != null) {
      int sum = l.val + carry;
      cur.next = new ListNode(sum % 10);
      carry = sum / 10;
      cur = cur.next;
      l = l.next;
    }
    if (carry != 0) {
      cur.next = new ListNode(carry);
    }
    return dummy.next;
  }
}

