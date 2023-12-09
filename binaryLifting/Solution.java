import java.util.*;

public class Solution {
    int[][] binary;
    ArrayList<ArrayList<Integer>> adjList;
    int LOG;
    int[] depth;

    public int getLog2Floor(int n) {
        int shift = 0;
        while (n != 1) {
            n = n >>> 1;
            shift += 1;
        }
        return shift;
    }
    
    
    
    public void fill(int node, int parent, int depth) {
        this.depth[node] = depth;
        if (node != parent) {
             binary[node][0] = parent;
        }
        for (int i = 1; i <= LOG; i ++) {
            binary[node][i] = (binary[node][i - 1] != -1) ? binary[binary[node][i - 1]][i - 1] : -1;
        }
        ArrayList<Integer> children = adjList.get(node);
        for (int child : children) {
            if (child != parent) {
                fill(child, node, depth + 1);
            }
        }
    }

    public int getKthAncestor(int node, int k) {
        if (node == -1) {
            return -1;
        } else if (k == 0) {
            return node;
        } else {
            int shift = 0;
            int temp = k;
            while (temp != 1) {
                temp = temp >>> 1;
                shift += 1;
            }
            return getKthAncestor(binary[node][shift], k - (1 << shift));
        }
    }

    public void processFromParent(int[] p) {
        LOG = getLog2Floor(p.length);
        binary = new int[p.length][LOG + 1];
        depth = new int[p.length];
        for (int[] array : binary) {
            Arrays.fill(array, -1);
        }
        adjList = new ArrayList<>();
        for (int i = 0; i < p.length; i ++) {
            adjList.add(new ArrayList<>());
        }
        for (int i = 0; i < p.length; i ++) {
            if (p[i] != -1) {
                adjList.get(i).add(p[i]);
                adjList.get(p[i]).add(i);
            }
        }
    }

    public int getLCA(int node1, int node2) {
        if (depth[node1] < depth[node2]) {
            return getLCA(node2, node1);
        } else {
            int u = getKthAncestor(node1, depth[node1] - depth[node2]);
            int v = node2;
            if (u == v) {
                return u;
            }
            for (int i = LOG; i >= 0; i --) {
                if (binary[u][i] != binary[v][i]) {
                    // going to the last node before the parent of the 2 node are equal
                    u = binary[u][i];
                    v = binary[v][i];
                }
            }
            return binary[u][0];
        }
    }



    public static void main(String[] args) {
        Solution s = new Solution();
        s.processFromParent(new int[] {-1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9});
        s.fill(0, 0, 0);
        int ans = s.getLCA(15, 10);
        
        System.out.println(ans);
    }

}