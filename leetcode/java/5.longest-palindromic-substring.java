/*
 * @lc app=leetcode id=5 lang=java
 *
 * [5] Longest Palindromic Substring
 *
 * https://leetcode.com/problems/longest-palindromic-substring/description/
 *
 * algorithms
 * Medium (26.93%)
 * Total Accepted:    529.2K
 * Total Submissions: 2M
 * Testcase Example:  '"babad"'
 *
 * Given a string s, find the longest palindromic substring in s. You may
 * assume that the maximum length of s is 1000.
 * 
 * Example 1:
 * 
 * 
 * Input: "babad"
 * Output: "bab"
 * Note: "aba" is also a valid answer.
 * 
 * 
 * Example 2:
 * 
 * 
 * Input: "cbbd"
 * Output: "bb"
 * 
 * 
 */

// Method1: expanding from center
// Time: O(n^2); Space: O(1)
class Solution {
  public String longestPalindrome(String s) {
    if (s == null || s.length() < 2) {
      return s;
    }
    int maxLen = 1;
    int maxLenStart = 0;
    for (int i = 0; i < s.length(); ++i) {
      int len1 = getPalindromeLength(s, i, i);  // odd
      int len2 = getPalindromeLength(s, i, i + 1);  // even
      int len = Math.max(len1, len2);
      if (len > maxLen) {
        maxLen = len;
        maxLenStart = i - (len - 1) / 2;
      }
    }
    return s.substring(maxLenStart, maxLenStart + maxLen);
  }

  private int getPalindromeLength(String s, int l, int r) {
    for (; l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r); --l, ++r);
    return r - l - 1;
  }
}

