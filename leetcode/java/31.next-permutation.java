/*
 * [31] Next Permutation
 *
 * https://leetcode.com/problems/next-permutation/description/
 *
 * algorithms
 * Medium (29.87%)
 *
 * Implement next permutation, which rearranges numbers into the
 * lexicographically next greater permutation of numbers.
 * 
 * If such arrangement is not possible, it must rearrange it as the lowest
 * possible order (ie, sorted in ascending order).
 * 
 * The replacement must be in-place and use only constant extra memory.
 * 
 * Here are some examples. Inputs are in the left-hand column and its
 * corresponding outputs are in the right-hand column.
 * 
 * 1,2,3 → 1,3,2
 * 3,2,1 → 1,2,3
 * 1,1,5 → 1,5,1
 * 
 */
class Solution {
  public void nextPermutation(int[] nums) {
    if (nums == null || nums.length <= 1) {
      return;
    }
    int targetIdx = nums.length - 2;
    // get the target index where the digit should be increased
    for (; targetIdx >= 0 && nums[targetIdx] >= nums[targetIdx + 1]; --targetIdx);
    // if we find the target index, we should find the smallest larger digit
    // from the end and then swap them
    if (targetIdx >= 0) {
      int sl = nums.length - 1;
      for (; nums[sl] <= nums[targetIdx]; --sl);
      swap(nums, targetIdx, sl);
    }
    // rearrange nums[targetIdx + 1, end] ascendingly.
    for (int i = targetIdx + 1, j = nums.length - 1; i < j; ++i, --j) {
      swap(nums, i, j);
    }
  }

  private void swap(int[] array, int i, int j) {
    int temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }
}
