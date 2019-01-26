/*
 * [904] Fruit Into Baskets
 *
 * https://leetcode.com/problems/fruit-into-baskets/description/
 *
 * algorithms
 * Medium (40.50%)
 *
 * In a row of trees, the i-th tree produces fruit with type tree[i].
 * 
 * You start at any tree of your choice, then repeatedly perform the following
 * steps:
 * 
 * 
 * Add one piece of fruit from this tree to your baskets.  If you cannot,
 * stop.
 * Move to the next tree to the right of the current tree.  If there is no tree
 * to the right, stop.
 * 
 * 
 * Note that you do not have any choice after the initial choice of starting
 * tree: you must perform step 1, then step 2, then back to step 1, then step
 * 2, and so on until you stop.
 * 
 * You have two baskets, and each basket can carry any quantity of fruit, but
 * you want each basket to only carry one type of fruit each.
 * 
 * What is the total amount of fruit you can collect with this procedure?
 * 
 * 
 * 
 * Example 1:
 * 
 * 
 * Input: [1,2,1]
 * Output: 3
 * Explanation: We can collect [1,2,1].
 * 
 * 
 * 
 * Example 2:
 * 
 * 
 * Input: [0,1,2,2]
 * Output: 3
 * Explanation: We can collect [1,2,2].
 * If we started at the first tree, we would only collect [0, 1].
 * 
 * 
 * 
 * Example 3:
 * 
 * 
 * Input: [1,2,3,2,2]
 * Output: 4
 * Explanation: We can collect [2,3,2,2].
 * If we started at the first tree, we would only collect [1, 2].
 * 
 * 
 * 
 * Example 4:
 * 
 * 
 * Input: [3,3,3,1,2,1,1,2,3,3,4]
 * Output: 5
 * Explanation: We can collect [1,2,1,1,2].
 * If we started at the first tree or the eighth tree, we would only collect 4
 * fruits.
 * 
 * 
 * 
* 
* 
* 
* 
* Note:
* 
* 
* 1 <= tree.length <= 40000
* 0 <= tree[i] < tree.length
* 
* 
*/

// This question is equivelent to find the longest subarray containing at most
// two types of elements.

// Method1: using sliding window and a HashMap
class Solution1 {
  public int totalFruit(int[] tree) {
    int len = tree.length;
    // hashmap <key = fruit type, value = the amount of fruit with this type>
    HashMap<Integer, Integer> map = new HashMap<>();
    int totalAmount = 0;
    // sliding window [l, r]
    for (int l = 0, r = 0; r < len; ++r) {
      map.put(tree[r], map.getOrDefault(tree[r], 0) + 1);
      while (map.size() > 2) {
        map.put(tree[l], map.get(tree[l]) - 1);
        if (map.get(tree[l]) == 0) {
          map.remove(tree[l]);
        }
        l++;
      }
      totalAmount = Math.max(totalAmount, r - l + 1);
    }
    return totalAmount;
  }
}

// Method2: without using a HashMap
class Solution {
  public int totalFruit(int[] tree) { 
    int len = tree.length;
    int last = -1;  // last fruit type
    int secondLast = -1;  // second last fruit type
    int maxAmount = 0;
    int nextL = 0;  // index for the next left-boundary of the sliding window
    // sliding window [l, r]
    for (int l = 0, r = 0; r < len; ++r) {
      int t = tree[r];
      if (t != last && t != secondLast) {
        l = nextL;
      }
      if (t != last) {
        nextL = r;
        secondLast = last;
        last = t;
      }
      maxAmount = Math.max(maxAmount, r - l + 1);
    }
    return maxAmount;
  }
}
