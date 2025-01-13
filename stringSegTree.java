public class stringSegTree {
    int[][] tree;
    int size;
    StringBuilder sb;
    

    public stringSegTree(String str) {
        tree = new int[4 * str.length()][26];
        size = str.length();
        sb = new StringBuilder(str);
        build(0, 0, size - 1, str);
    }
    
    void build(int p, int s, int e, String str) {
        if (s == e) {
            tree[p][str.charAt(s) - 'a'] = 1;
        } else {
            int m = (s + e) / 2;
            build(2 * p + 1, s, m, str);
            build(2 * p + 2, m + 1, e, str);
            for (int i = 0; i < 26; i ++) {
                tree[p][i] = tree[2 * p + 1][i] + tree[2 * p + 2][i];
            }
        }
    }

    int findLongestPrefix(int p, int s, int e, int index, int[] count, int distinctLimit) {
        // invariant, when called, on range [s, e], means range [x, s-1] is successful
        // if fail, int[] count return to state when start of call
        // if success, keep modified count
        int m = (s + e) / 2;
        if (s == e) {
            int distinct = 0;
            for (int i = 0; i < 26; i ++) {
                count[i] += tree[p][i];
                distinct += (count[i] > 0) ? 1 : 0;
            }
            if (distinct <= distinctLimit) {
                return s;
            } else {
                for (int i = 0; i < 26; i ++) {
                    count[i] -= tree[p][i];
                }
                return -1;
            }
        } else if (index == s) {
            int distinct = 0;
            for (int i = 0; i < 26; i ++) {
                count[i] += tree[p][i];
                distinct += (count[i] > 0) ? 1 : 0;
            }
            if (distinct <= distinctLimit) {
                return e;
            } else {
                for (int i = 0; i < 26; i ++) {
                    count[i] -= tree[p][i];
                }
                int left = findLongestPrefix(2 * p + 1, s, m, index, count, distinctLimit);
                if (left == m) {
                    int right = findLongestPrefix(2 * p + 2, m + 1, e, index, count, distinctLimit);
                    return (right != -1) ? right : left;
                }
                return left;
            }
        } else if (index > m) {
            return findLongestPrefix(2 * p + 2, m + 1, e, index, count, distinctLimit);
        } else {
            int left = findLongestPrefix(2 * p + 1, s, m, index, count, distinctLimit);
            if (left == m) {
                int right = findLongestPrefix(2 * p + 2, m + 1, e, index, count, distinctLimit);
                return (right != -1) ? right : left;
            }
            return left;
        }
    }

    void update(int p, int s, int e, int index, int c) {
        int m = (s + e) / 2;
        if (s == e) {
            int cOriginal = sb.charAt(index) - 'a';
            tree[p][cOriginal] -= 1;
            tree[p][c] += 1;
            sb.setCharAt(index, (char) (c + 'a'));
        } else if (index <= m) {
            update(2 * p + 1, s, m, index, c);
            for (int i = 0; i < 26; i ++) {
                tree[p][i] = tree[2 * p + 1][i] + tree[2 * p + 2][i];
            }
        } else {
            update(2 * p + 2, m + 1, e, index, c);
            for (int i = 0; i < 26; i ++) {
                tree[p][i] = tree[2 * p + 1][i] + tree[2 * p + 2][i];
            }
        }
    }

    

    public static void main(String[] args) {
        String s = "ccbaabbba";
        stringSegTree st = new stringSegTree(s);
        System.out.println(st.findLongestPrefix(0, 0, st.size - 1, 3, new int[26], 2));
        st.update(0, 0, st.size - 1, 0, 25);
        st.update(0, 0, st.size - 1, 6, 25);
        System.out.println(st.findLongestPrefix(0, 0, st.size - 1, 3, new int[26], 2));
    }
}
