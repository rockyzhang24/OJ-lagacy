/*
 * @lc app=leetcode id=200 lang=java
 *
 * [200] Number of Islands
 *
 * https://leetcode.com/problems/number-of-islands/description/
 *
 * algorithms
 * Medium (40.85%)
 * Total Accepted:    329.9K
 * Total Submissions: 806.2K
 * Testcase Example:  '[["1","1","1","1","0"],["1","1","0","1","0"],["1","1","0","0","0"],["0","0","0","0","0"]]'
 *
 * Given a 2d grid map of '1's (land) and '0's (water), count the number of
 * islands. An island is surrounded by water and is formed by connecting
 * adjacent lands horizontally or vertically. You may assume all four edges of
 * the grid are all surrounded by water.
 * 
 * Example 1:
 * 
 * 
 * Input:
 * 11110
 * 11010
 * 11000
 * 00000
 * 
 * Output: 1
 * 
 * 
 * Example 2:
 * 
 * 
 * Input:
 * 11000
 * 11000
 * 00100
 * 00011
 * 
 * Output: 3
 * 
 */

// // Method1: BFS
// class Solution {
//   // directions:                              Left    Right     Up      Down
//   private final static int[][] DIRS = new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

//   public int numIslands(char[][] grid) {
//     // sanity check
//     if (grid == null || grid.length == 0 
//         || grid[0] == null || grid[0].length == 0) {
//       return 0;
//     }
//     int rows = grid.length;
//     int cols = grid[0].length;
//     int count = 0;
//     // do bfs for each '1' cell and update the number of islands so far
//     for (int i = 0; i < rows; ++i) {
//       for (int j = 0; j < cols; ++j) {
//         if (grid[i][j] == '1') {
//           bfs(grid, i, j, rows, cols);
//           count++;
//         }
//       }
//     }
//     return count;
//   }

//   // bfs helper function: we define an identifier for each cell of grid as
//   // r * cols + c where r is the cell's row and c is its column
//   private void bfs(char[][] grid, int i, int j, int rows, int cols) {
//     Queue<Integer> queue = new ArrayDeque<>();
//     queue.offer(i * cols + j);
//     grid[i][j] = '2'; // mark it as visited
//     while (!queue.isEmpty()) {
//       int id = queue.poll();
//       int r = id / cols;
//       int c = id % cols;
//       // for each neighbor on four directions, insert it into the queue if
//       // it is unvisited
//       for (int[] dir : DIRS) {
//         int neiR = r + dir[0];
//         int neiC = c + dir[1];
//         if (neiR >= 0 && neiR < rows && neiC >= 0 && neiC < cols 
//             && grid[neiR][neiC] == '1') {
//           queue.offer(neiR * cols + neiC);
//           grid[neiR][neiC] = '2';
//         }
//       }
//     }
//   }
// }

// // Method2: DFS
// class Solution {
//   // directions
//   private final static int[][] DIRS = new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

//   public int numIslands(char[][] grid) {
//     // sanity check
//     if (grid == null || grid.length == 0 
//         || grid[0] == null || grid[0].length == 0) {
//       return 0;
//     }
//     int rows = grid.length;
//     int cols = grid[0].length;
//     int count = 0;
//     // for each cell which is '1', do dfs
//     for (int i = 0; i < rows; ++i) {
//       for (int j = 0; j < cols; ++j) {
//         if (grid[i][j] == '1') {
//           dfs(grid, i, j, rows, cols);
//           count++;
//         }
//       }
//     }
//     return count;
//   }

//   // dfs helper function where r is the cell's row and c is the 
//   // cell's column 
//   private void dfs(char[][] grid, int r, int c, int rows, int cols) {
//     grid[r][c] = '2'; // mark it as visited
//     // for each neighbor of this cell, calculate its row and col, and do dfs
//     // if it is unvisited
//     for (int[] dir : DIRS) {
//       int neiR = r + dir[0];
//       int neiC = c + dir[1];
//       if (neiR >= 0 && neiR < rows && neiC >= 0 && neiC < cols 
//           && grid[neiR][neiC] == '1') {
//         dfs(grid, neiR, neiC, rows, cols);
//       }
//     }
//   }
// }

// Method3: Union Find
// To present the graph, the vertex is the cell of grid and represented by
// x * cols + y where x and y are the cell's row and column respectively and
// cols are the total number of columns of grid
class Solution {
  // directions for right and down
  private final static int[][] DIRS = new int[][] {{1, 0}, {0, 1}};

  public int numIslands(char[][] grid) {
    // sanity check
    if (grid == null || grid.length == 0 
        || grid[0] == null || grid[0].length == 0) {
      return 0;
    }
    int rows = grid.length;
    int cols = grid[0].length;
    UnionFind uf = new UnionFind(grid, rows, cols);
    // if cell (i, j) is '1', we check each neighbor cell which is right or
    // down to it, and if the neighbor cell is also '1', we connect them.
    // (Checking two directions are enought adn no need to check four
    // directions)
    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < cols; ++j) {
        if (grid[i][j] == '1') {
          for (int[] dir : DIRS) {
            int neiR = i + dir[0];
            int neiC = j + dir[1];
            if (neiR >= 0 && neiR < rows && neiC >= 0 && neiC < cols 
                && grid[neiR][neiC] == '1') {
              uf.union(i * cols + j, neiR * cols + neiC);
            }
          }
        }
      }
    }
    return uf.getCount();
  }
  
  private class UnionFind {
    // the number of disjoint sets so far
    int count;
    // in order to maintain a tree structure, parent[i] is the parent's index
    // of i, for example, parent[5] = 5 means the praent'index of 5 is 5, this
    // means 5 is a root 
    int[] parent;
    int[] rank;

    UnionFind(char[][] grid, int rows, int cols)  {
      int n = rows * cols;
      parent = new int[n];
      rank = new int[n];  // all elements are initialieze to 0 by default
      // initialization
      for (int i = 0; i < rows; ++i) {
        for (int j = 0; j < cols; ++j) {
          if (grid[i][j] == '1') {
            count++;
            int idx = i * cols + j;
            parent[idx] = idx;
          }
        }
      }
    }

    // using path compression: during find operation, make each node on the
    // path from x to root point directly to the root
    int find(int x) {
      if (parent[x] != x) {
        parent[x] = find(parent[x]);
      }
      return parent[x];
    }

    // using union by rank
    void union(int x, int y) {
      // find the roots of x and y respectively
      int xRoot = find(x);
      int yRoot = find(y);
      // if they have the same root which means they belong to the same set, 
      // no need to union them
      if (xRoot == yRoot) {
        return;
      }
      // union by rank: the root with larger rank will be the parent of another
      // root which has smaller rank; if their's ranks are equal, arbitrarily
      // choose a root as the parent and increment its rank
      if (rank[xRoot] > rank[yRoot]) {
        parent[yRoot] = xRoot;
      } else if (rank[xRoot] < rank[yRoot]) {
        parent[xRoot] = yRoot;
      } else {
        parent[xRoot] = yRoot;
        rank[yRoot] = rank[yRoot] + 1;
      }
      count--;
    }

    int getCount() {
      return count;
    }
  }
}

