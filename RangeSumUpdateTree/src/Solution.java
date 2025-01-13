import java.util.*;

public class Solution {
    HashMap<Integer, TreeSet<Integer>> map;

    public void collect(int[] nums) {
        map = new HashMap<>();
        for (int i = 0; i < nums.length; i ++) {
            int x = nums[i];
            TreeSet<Integer> lst = map.get(x);
            if (lst == null) {
                lst = new TreeSet<>();
            }
            lst.add(i);
            map.put(x, lst);
        }
    }

    public long naive(int[] nums) {
        long count = 0;
        for (int i = 0; i < nums.length; i ++) {
            for (int j = i; j < nums.length; j ++) {
                HashMap<Integer, Integer> dist = new HashMap<>();
                long score = 0;
                for (int k = i; k <= j; k ++) {
                    Integer inside = dist.get(nums[k]);
                    score = (inside == null) ? score + 1 : score;
                    if (inside == null) {
                        dist.put(nums[k], 1);
                    }
                }
                count += score * score;
            }
        }
        return count;
    }


    public int sumCounts(int[] nums) {
        collect(nums);
        RangeSumUpdateTree tt = new RangeSumUpdateTree(nums.length);
        long count = 0;
        long squareSum = 0;
        for (int i = 0; i < nums.length; i ++) {
            if (i == 0) {
                count += 1;
                squareSum = 1;
                tt.rangeUpdate(0, 0, nums.length - 1, 0, 0, 1);
            } else {
                int curr = nums[i];
                Integer earliestIndex = map.get(curr).floor(i - 1);
                //System.out.println(earliestIndex);
                if (earliestIndex == null) {
                    long thisSum = squareSum + 2 *
                        tt.rangeSum(0, 0, nums.length - 1, 0, i - 1, 0) + (i + 1);
                    count += thisSum;
                    squareSum = thisSum;
                    tt.rangeUpdate(0, 0, nums.length - 1, 0, i, 1);

                    //System.out.println("" + i + " " + thisSum);
                } else {


                    long thisSum = squareSum + 2 *
                        tt.rangeSum(0, 0, nums.length - 1, earliestIndex + 1, i - 1, 0) +
                        ((i - 1) - (earliestIndex + 1) + 1) + 1;
                    count += thisSum;
                    squareSum = thisSum;
                    tt.rangeUpdate(0, 0, nums.length - 1, earliestIndex + 1, i, 1);

                    //System.out.println("" + i + " " + thisSum);
                }
            }
        }
        return (int) Math.floorMod(count, 1000000007);
    }


    public static void main(String[] args) {
        Solution s = new Solution();
        int[] a = new int[100000];
        for (int i = 0; i < a.length; i ++) {
            a[i] = i + 1;
        }
        long ans = s.sumCounts(a);
        //long check = s.naive(a);
        System.out.println(ans);
        //System.out.println(check);
    }
}
