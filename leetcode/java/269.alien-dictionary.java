/*
 * [269] Alien Dictionary
 *
 * https://leetcode.com/problems/alien-dictionary/description/
 *
 * algorithms
 * Hard (29.60%)
 *
 * There is a new alien language which uses the latin alphabet. However, the
 * order among letters are unknown to you. You receive a list of non-empty
 * words from the dictionary, where words are sorted lexicographically by the
 * rules of this new language. Derive the order of letters in this language.
 * 
 * Example 1:
 * 
 * 
 * Input:
 * [
 * ⁠ "wrt",
 * ⁠ "wrf",
 * ⁠ "er",
 * ⁠ "ett",
 * ⁠ "rftt"
 * ]
 * 
 * Output: "wertf"
 * 
 * 
 * Example 2:
 * 
 * 
 * Input:
 * [
 * ⁠ "z",
 * ⁠ "x"
 * ]
 * 
 * Output: "zx"
 * 
 * 
 * Example 3:
 * 
 * 
 * Input:
 * [
 * ⁠ "z",
 * ⁠ "x",
 * ⁠ "z"
 * ] 
 * 
 * Output: "" 
 * 
 * Explanation: The order is invalid, so return "".
 * 
 * 
 * Note:
 * 
 * 
 * You may assume all letters are in lowercase.
 * You may assume that if a is a prefix of b, then a must appear before b in
 * the given dictionary.
 * If the order is invalid, return an empty string.
 * There may be multiple valid order of letters, return any one of them is
 * fine.
 * 
 * 
 */

// Use topological sort to solve this problem. We can use Kahn's algorithm or
// DFS to implement topological sort.

// Method1: Kahn's Algorithm (BFS idea) and build graph using adjacency list
class Solution1 {
  public String alienOrder(String[] words) {
    Map<Character, Set<Character>> adjLists = new HashMap<>();  // adjacency lists of all vertices
    Map<Character, Integer> inDegree = new HashMap<>(); // in-degree of each vertex
    StringBuilder res = new StringBuilder();

    // build the graph
    // initialize the in-degree of each vertex
    for (String w : words) {
      for (char c : w.toCharArray()) {
        inDegree.put(c, 0);
        // or we can create an empty adjacency-list for each vertex so that we
        // do not need to check null when traverse the list at line 130.
        // adjLists.putIfAbsent(c, new HashSet<Character>());
      }
    }
    // add vertices and the corresponding edge
    for (int i = 0; i < words.length - 1; ++i) {
      String cur = words[i];
      String next = words[i + 1];
      int len = Math.min(cur.length(), next.length());
      // find the first different chareacter
      for (int j = 0; j < len; ++j) {
        char curChar = cur.charAt(j);
        char nextChar = next.charAt(j);
        // when we find the first pair of different characters curChar and nextChar,
        // we add the edge curChar->nextChar, which means add nextC into curChar's
        // adjacency list. Then we update nextChar's in-degree.
        // NOTE: if curChar is not in inDegree map, which means this is the
        // first time we met curChar, we should assign 0 tp curChar's in-degree.
        if (curChar != nextChar) {
          Set<Character> list = adjLists.get(curChar);
          if (list == null) {
            list = new HashSet<>();
            adjLists.put(curChar, list);
          }
          // Avoid duplicate: we handle curChar->nextChar only when we first
          // meet it.
          // add(x) returns true if x is added to the set for the first time
          if (list.add(nextChar)) {
            inDegree.put(nextChar, inDegree.getOrDefault(nextChar, 0) + 1);
          }
          break;
        }
      }
    }
    // execute Kahn's algorithm
    Queue<Character> queue = new ArrayDeque<>();
    // put all vertices with 0 in-degree into queue
    for (char c : inDegree.keySet()) {
      if (inDegree.get(c) == 0) {
        queue.offer(c);
      }
    }
    while (!queue.isEmpty()) {
      char c = queue.poll();
      res.append(c);
      // DON'T forget to check null here. Vertex with 0 out-degree has no
      // adjacency-list (i.e., adjLists.get(c) will get null), and traverse null
      // will throw NullPointerException.
      if (adjLists.containsKey(c)) {
        // for each vertex connected from c, i.e., c->cNext, decrease its
        // in-degree by 1, and then if it gets 0, put it into the queue
        for (char cNext : adjLists.get(c)) {
          inDegree.put(cNext, inDegree.get(cNext) - 1);
          if (inDegree.get(cNext) == 0) {
            queue.offer(cNext);
          }
        }
      }
    }
    // check whether the order is invalid, i.e., whether there exists cycle in
    // the graph
    if (res.length() != inDegree.size()) {
      return "";
    }
    return res.toString();
  }
}

// Method1 with adjacency matrix as graph representation
class Solution2 {
  // Assume totally we only have 26 letters
  private final int N = 26;
  public String alienOrder(String[] words) {
    boolean[][] graph = new boolean[N][N];  // adjacency matrix
    int[] inDegree = new int[N]; // in-degree
    int count = 0;  // count total vertices in the graph
    StringBuilder ret = new StringBuilder();
    Arrays.fill(inDegree, -1);
    // initialize in-degree and count the total number of vertices
    for (String w : words) {
      for (char c : w.toCharArray()) {
        if (inDegree[c - 'a'] == -1) {
          inDegree[c - 'a'] = 0;
          count++;
        }
      }
    }
    // build the graph
    for (int i = 0; i < words.length - 1; ++i) {
      String curWd = words[i];
      String nextWd = words[i + 1];
      int minLen = Math.min(curWd.length(), nextWd.length());
      for (int j = 0; j < minLen; ++j) {
        char curChar = curWd.charAt(j);
        char nextChar = nextWd.charAt(j);
        if (curChar != nextChar) {
          // avoid duplicate
          if (!graph[curChar - 'a'][nextChar - 'a']) {
            graph[curChar - 'a'][nextChar - 'a'] = true;
            inDegree[nextChar - 'a']++;
          }
          break;
        }
      }
    }
    // topological sort
    Queue<Integer> queue = new ArrayDeque<>();
    for (int i = 0; i < inDegree.length; ++i) {
      if (inDegree[i] == 0) {
        queue.offer(i);
      }
    }
    while (!queue.isEmpty()) {
      int idx = queue.poll();
      ret.append((char) (idx + 'a'));
      for (int i = 0; i < graph[idx].length; ++i) {
        if (graph[idx][i] && --inDegree[i] == 0) {
          queue.offer(i);
        }
      }
    }
    return ret.length() == count ? ret.toString() : "";
  }
}

// Method2: DFS (build graph using adjacency list)
class Solution3 {
  private final int N = 26;
  public String alienOrder(String[] words) {
    Map<Character, Set<Character>> graph = new HashMap<>(); // adjacency-list
    // 3 status for each vertex:
    // -1 for not existed, 0 for not discovered yet, 1 for discovered, 2 for
    // finished
    int[] visited = new int[N];
    StringBuilder sb = new StringBuilder();
    Arrays.fill(visited, -1);
    // initialize visited[] array
    for (String w : words) {
      for (char c : w.toCharArray()) {
        visited[c - 'a'] = 0;
      }
    }
    // build the graph
    for (int i = 0; i < words.length - 1; ++i) {
      String curWord = words[i];
      String nextWord = words[i + 1];
      int minLen = Math.min(curWord.length(), nextWord.length());
      for (int j = 0; j < minLen; ++j) {
        char curChar = curWord.charAt(j);
        char nextChar = nextWord.charAt(j);
        if (curChar != nextChar) {  // we find an edge curChar->nextChar
          Set<Character> set = graph.get(curChar);
          if (set == null) {
            set = new HashSet<Character>();
            graph.put(curChar, set);
          }
          set.add(nextChar);
          break;  // DO NOT FORGET!!!
        }
      }
    }
    // dfs
    for (int i = 0; i < visited.length; ++i) {
      if (visited[i] == 0) {  // for unvisited
        if (!dfs(graph, visited, i, sb)) {
          return "";
        }
      }
    }
    return sb.reverse().toString();
  }

  private boolean dfs (Map<Character, Set<Character>> graph, int[] visited,
      int idx, StringBuilder sb) {
    visited[idx] = 1; // marked as discovered
    char c = (char) (idx + 'a');
    Set<Character> set = graph.get(c);
    if (set != null) {
      for (char next : set) {
        int nextIdx = next - 'a';
        if (visited[nextIdx] == 1) {  // existing cycle in the graph
          return false;
        }
        if (visited[nextIdx] == 0) { // unvisited yet
          if(!dfs(graph, visited, nextIdx, sb)) {
            return false;
          }
        }
      }
    }
    visited[idx] = 2; // marked as finished
    sb.append(c);
    return true;
  }
}

// Method2: DFS (build the graph using adjacency matrix)
class Solution {
  private final int N = 26;
  public String alienOrder(String[] words) {
    boolean [][] graph = new boolean[N][N];
    int[] visited = new int[N];
    StringBuilder sb = new StringBuilder();
    // initialize visited[]
    Arrays.fill(visited, -1);
    for (String w: words) {
      for (char c : w.toCharArray()) {
        visited[c - 'a'] = 0;
      }
    }
    // build the graph
    for (int i = 0; i < words.length - 1; ++i) {
      String curWord = words[i];
      String nextWord = words[i + 1];
      int minLen = Math.min(curWord.length(), nextWord.length());
      for (int j = 0; j < minLen; ++j) {
        char curChar = curWord.charAt(j);
        char nextChar = nextWord.charAt(j);
        if (curChar != nextChar) {
          graph[curChar - 'a'][nextChar - 'a'] = true;
          break;
        }
      }
    }
    // dfs
    for (int i = 0; i < visited.length; ++i) {
      if (visited[i] == 0) {
        if (!dfs(graph, visited, i, sb)) {
          return "";
        }
      }
    }
    return sb.reverse().toString();
  }

  private boolean dfs(boolean[][] graph, int[] visited, int i, 
      StringBuilder sb) {
    visited[i] = 1; // marked as discovered
    for (int j = 0; j < N; ++j) {
      if (graph[i][j]) {
        if (visited[j] == 1) {
          return false;
        }
        if (visited[j] == 0) {
          if (!dfs(graph, visited, j, sb)) {
            return false;
          }
        }
      }
    }
    visited[i] = 2; // marked as finished
    sb.append((char) (i + 'a'));
    return true;
  }
}
