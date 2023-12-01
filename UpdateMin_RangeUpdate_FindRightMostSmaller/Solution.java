// Leetocde 2945
// dp[i], dp[i + 1] .. is always inceasing, why because worst case we can just append i + 1 to dp[i]
// very long explanation
// to get dp[i + 1] we either append to dp[i], or find dp[j] such that num[j+1], num[j+2]... num[i+1] more
// than the last subarray in dp[j]

// now partitioning more will only decease the max element, so in dp[j] we also need to store the sum of the last subarray
//  we assume the sum of the last array is the smallest

// only 2 options for dp[i + 1], append to dp[i] last subarray, so dp[i + 1] = dp[i], or find dp[j] such that
// the last subarray in dp[j] <= sum[j + 1, i + 1], which means dp[i + 1] = dp[j] + 1
// for each dp[j] we keep the additional sum needed to overcome it
// once we processed dp[i + 1], we subtract nums[i + 1] from all dp[j] to update the additional sum needed to overcome last array in dp[j]












public class Solution {
    long[] prefixSum;

    public void processPrefix(int[] nums) {
        long sum = 0;
        prefixSum = new long[nums.length];
        for (int i = 0; i < nums.length; i ++) {
            sum += nums[i];
            prefixSum[i] = sum;
        }
    }

    public long query(int s, int e) {
        return (s == 0) ? prefixSum[e] : prefixSum[e] - prefixSum[s - 1];
    }


    public int findMaximumLength(int[] nums) {
        processPrefix(nums);
        int[] dp = new int[nums.length + 1];
        long[] sum = new long[nums.length + 1];
        SpecialSegTree st = new SpecialSegTree(nums.length + 1);
        dp[0] = 0;
        sum[0] = 0;
        st.updateMin(0, 0, st.size - 1, 0, 0, 0);
        for (int i = 0; i < nums.length; i ++) {
            int append = dp[i];
            int firstIndex = st.findRightFirstMin(0, 0, st.size - 1, i, nums[i], 0);
            int noAppend = dp[firstIndex] + 1;
            if (append > noAppend) {
                long subarraySum = sum[i] + (long) nums[i];
                st.rangeUpdate(0, 0, st.size - 1, 0, i, (long) -nums[i], 0l);
                st.updateMin(0, 0, st.size - 1, i + 1, subarraySum, 0l);
                dp[i + 1] = dp[i];
                sum[i + 1] = subarraySum;
            } else if (noAppend > append) {
                long subarraySum = query(firstIndex, i);
                st.rangeUpdate(0, 0, st.size - 1, 0, i, (long) -nums[i], 0l);
                st.updateMin(0, 0, st.size - 1, i + 1, subarraySum, 0);
                dp[i + 1] = noAppend;
                sum[i + 1] = subarraySum;
            } else  {
                long appendSum = sum[i] + (long) nums[i];
                long noAppendSum = query(firstIndex, i);
                if (appendSum <= noAppendSum) {
                    st.rangeUpdate(0, 0, st.size - 1, 0, i, (long) -nums[i], 0l);
                    st.updateMin(0, 0, st.size - 1, i + 1, appendSum, 0l);
                    dp[i + 1] = dp[i];
                    sum[i + 1] = appendSum;
                } else {
                    st.rangeUpdate(0, 0, st.size - 1, 0, i, (long) -nums[i], 0l);
                    st.updateMin(0, 0, st.size - 1, i + 1, noAppendSum, 0);
                    dp[i + 1] = noAppend;
                    sum[i + 1] = noAppendSum;
                }
            }
        }
        return dp[dp.length - 1];
    }



    public static void main(String[] args) {
        int[] nums = {4,3,2,6,1,2,1,2,8,5,3,6,11,13,4};
        int ans = new Solution().findMaximumLength(nums);
        System.out.println(ans);
    }


}
