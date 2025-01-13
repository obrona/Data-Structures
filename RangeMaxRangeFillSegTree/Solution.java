import java.util.*;

public class Solution {
    HashMap<Integer, Integer> compression(int[][] positions) {
        HashMap<Integer, Integer> out = new HashMap<>();
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        HashMap<Integer, Integer> used = new HashMap<>();
        for (int[] a : positions) {
            int s = a[0];
            int e = a[0] + a[1];
            if (used.get(s) == null) {
                used.put(s, 1);
                pq.add(s);
            }
            if (used.get(e) == null) {
                used.put(e, 1);
                pq.add(e);
            }
        }
        int index = 0;
        while (!pq.isEmpty()) {
            int n = pq.poll();
            out.put(n, index);
            index += 1;
        }
        return out;
    }

    public List<Integer> fallingSquares(int[][] positions) {
        List<Integer> out = new ArrayList<>();
        HashMap<Integer, Integer> compressMap = compression(positions);
        RangeMaxRangeFillSegTree st = new RangeMaxRangeFillSegTree(new int[compressMap.size()]);
       
        for (int i = 0; i < positions.length; i ++) {
          
            int[] pos = positions[i];
            int s = compressMap.get(pos[0]);
            int e = compressMap.get(pos[0] + pos[1]) - 1;
            //System.out.println(""  + i + " " + s + " " + e);
            int currMax = st.rangeMax(0, 0, st.n - 1, s, e);
            int newHeight = currMax + pos[1];
            
            //System.out.println(newHeight);
            st.rangeFill(0, 0, st.n - 1, s, e, newHeight);
            //System.out.println(st.rangeMax(0, 0, st.n - 1, 0, st.n - 1));
            st.rangeFill(0, 0, 4, s, e, newHeight);
            int maxHeight = st.rangeMax(0, 0, st.n - 1, 0, st.n - 1);
            //System.out.println(maxHeight);
            out.add(maxHeight);
        }
        return out;
    }

    public static void main(String[] args) {
        int[][] positions = new int[][] {{2, 5}, {2,7}, {1,10}, {1,10}, {2,6}};
        Solution s = new Solution();
        List<Integer> ans = s.fallingSquares(positions);
        System.out.println(Arrays.toString(ans.toArray()));
    }
}
