import java.util.Arrays;

class SpecialSegTree {
    long maxNum = 20000000000l;
    long[] minTree;
    long[] offset;
    int size;

    public SpecialSegTree(int size) {
        this.size = size;
        minTree = new long[4 * size];
        offset = new long[4 * size];
        Arrays.fill(minTree, maxNum);
    }


    // Invariant: minTree[ptr] + offset = actual min val for range [s, e]

    public void updateMin(int ptr, int s, int e, int index, long val, long offsetVal) {
        if (s == e) {
            minTree[ptr] = val - offsetVal;
        } else {
            int m = (s + e) / 2;
            if (index <= m) {
                updateMin(2 * ptr + 1, s, m, index, val, offsetVal + offset[ptr]);
                minTree[ptr] = Math.min(minTree[ptr] + offsetVal, minTree[2 * ptr + 1] + offsetVal + offset[ptr]) - offsetVal;
            } else {
                updateMin(2 * ptr + 2, m + 1, e, index, val, offsetVal + offset[ptr]);
                minTree[ptr] = Math.min(minTree[ptr] + offsetVal, minTree[2 * ptr + 2] + offsetVal + offset[ptr]) - offsetVal;
            }
        }
    }


    
    
    
    public void leftRangeUpdate(int ptr, int s, int e, int left, long val, long offsetVal) {
        if (s == e) {
            offset[ptr] += val;
            minTree[ptr] += val;
        } else if (left == s) {
            minTree[ptr] += val;
            offset[ptr] += val;
        } else {
            int m = (s + e) / 2;
            if (left <= m) {
                minTree[2 * ptr + 2] += val;
                offset[2 * ptr + 2] += val;
                leftRangeUpdate(2 * ptr + 1, s, m, left, val, offsetVal + offset[ptr]);
                minTree[ptr] = Math.min(minTree[2 * ptr + 2] + offsetVal + offset[ptr], minTree[2 * ptr + 1] + offsetVal + offset[ptr]) - offset[ptr];
            } else {
                leftRangeUpdate(2 * ptr + 2, m + 1, e, left, val, offsetVal + offset[ptr]);
                minTree[ptr] = Math.min(minTree[2 * ptr + 1] + offsetVal + offset[ptr], minTree[2 * ptr + 2] + offsetVal + offset[ptr]) - offset[ptr];
            }
        }
    }

    public void rightRangeUpdate(int ptr, int s, int e, int right, long val, long offsetVal) {
        if (s == e) {
            offset[ptr] += val;
            minTree[ptr] += val;
        } else if (right == e) {
            minTree[ptr] += val;
            offset[ptr] += val;
        } else {
            int m = (s + e) / 2;
            if (right <= m) {
                rightRangeUpdate(2 * ptr + 1, s, m, right, val, offsetVal + offset[ptr]);
                minTree[ptr] = Math.min(minTree[2 * ptr + 2] + offsetVal + offset[ptr], minTree[2 * ptr + 1] + offsetVal + offset[ptr]) - offset[ptr];
            } else {
                minTree[2 * ptr + 1] += val;
                offset[2 * ptr + 1] += val;
                rightRangeUpdate(2 * ptr + 2, m + 1, e, right, val, offsetVal + offset[ptr]);
                minTree[ptr] = Math.min(minTree[2 * ptr + 1] + offsetVal + offset[ptr], minTree[2 * ptr + 2] + offsetVal + offset[ptr]) - offset[ptr];
            }
        }
    }

    public void rangeUpdate(int ptr, int s, int e, int l, int r, long val, long offsetVal) {
        if (s == e) {
            offset[ptr] += val;
            minTree[ptr] += val;
        } else if (s == l && e == r) {
            minTree[ptr] += val;
            offset[ptr] += val;
        } else {
            int m = (s + e) / 2;
            if (r <= m) {
                rangeUpdate(2 * ptr + 1, s, m, l, r, val, offsetVal + offset[ptr]);
                minTree[ptr] = Math.min(minTree[2 * ptr + 2] + offsetVal + offset[ptr], minTree[2 * ptr + 1] + offsetVal + offset[ptr]) - offset[ptr];
            } else if (l > m) {
                rangeUpdate(2 * ptr + 2, m + 1, e, l, r, val, offsetVal + offset[ptr]);
                minTree[ptr] = Math.min(minTree[2 * ptr + 1] + offsetVal + offset[ptr], minTree[2 * ptr + 2] + offsetVal + offset[ptr]) - offset[ptr];
            } else {
                leftRangeUpdate(2 * ptr + 1, s, m, l, val, offsetVal);
                rightRangeUpdate(2 * ptr + 2, m + 1, e, r, val, offsetVal);
                minTree[ptr] = Math.min(minTree[2 * ptr + 1] + offsetVal + offset[ptr], minTree[2 * ptr + 2] + offsetVal + offset[ptr]) - offset[ptr];
            }
        }
    }

    
    
    
    
    
    public long leftMin(int ptr, int s, int e, int l, long offsetVal) {
        if (s == e) {
            return minTree[ptr] + offsetVal;
        } else if (s == l) {
            return minTree[ptr] + offsetVal;
        } else {
            int m = (s + e) / 2;
            if (l <= m) {
                long minLeft = leftMin(2 * ptr + 1, s, m, l, offsetVal + offset[ptr]);
                return Math.min(minLeft, minTree[2 * ptr + 2] + offsetVal + offset[ptr]);
            } else {
                return leftMin(2 * ptr + 2, m + 1, e, l, offsetVal + offset[ptr]);
            }
        }
    }

    public long rightMin(int ptr, int s, int e, int r, long offsetVal) {
        if (s == e) {
            return minTree[ptr] + offsetVal;
        } else if (s == r) {
            return minTree[ptr] + offsetVal;
        } else {
            int m = (s + e) / 2;
            if (r <= m) {
                return rightMin(2 * ptr + 1, s, m, r, offsetVal + offset[ptr]);
            } else {
                long minRight = rightMin(2 * ptr + 2, m + 1, e, r, offsetVal + offset[ptr]);
                return Math.min(minRight, minTree[2 * ptr + 1] + offsetVal + offset[ptr]);
            }
        }
    }

    public long findMin(int ptr, int s, int e, int l, int r, long offsetVal) {
        if (s == e) {
            return minTree[ptr] + offsetVal;
        } else if (s == l && e == r) {
            return minTree[ptr] + offsetVal;
        } else {
            int m = (s + e) / 2;
            if (r <= m) {
                return findMin(2 * ptr + 1, s, m, l, r, offsetVal + offset[ptr]);
            } else if (l > m) {
                return findMin(2 * ptr + 2, m + 1, e, l, r, offsetVal + offset[ptr]);
            } else {
                long leftmin = leftMin(2 * ptr + 1, s, m, l, offsetVal + offset[ptr]);
                long rightmin = rightMin(2 * ptr + 2, m + 1, e, r, offsetVal + offset[ptr]);
                return Math.min(leftmin, rightmin);
            }
        }
    }
    
    
    
    
    
    
    
    
    public long findIndexVal(int ptr, int s, int e, int index, long offsetVal) {
        if (s == e) {
            return minTree[ptr] + offsetVal;
        } else {
            int m = (s + e) / 2;
            if (index <= m) {
                return findIndexVal(2 * ptr + 1, s, m, index, offsetVal + offset[ptr]);
            } else {
                return findIndexVal(2 * ptr + 2, m + 1, e, index, offsetVal + offset[ptr]);
            }
        }
    }


    public int findRightFirstMin(int ptr, int s, int e, int index, long val, long offsetVal) {
        int m = (s + e) / 2;
        if (s == e) {
            return (val >= minTree[ptr] + offsetVal ) ? s : -1;
        } else if (val < minTree[ptr] + offsetVal) {
            return -1;
        } else if (index >= e) {
            long rightMin = minTree[2 * ptr + 2] + offsetVal + offset[ptr];
            if (rightMin <= val) {
                return findRightFirstMin(2 * ptr + 2, m + 1, e, index, val, offsetVal + offset[ptr]);
            } else {
                return findRightFirstMin(2 * ptr + 1, s, m, index, val, offsetVal + offset[ptr]);
            }
        } else {
            if (index <= m) {
                return findRightFirstMin(2 * ptr + 1, s, m, index, val, offsetVal + offset[ptr]);
            } else {
                int check = findRightFirstMin(2 * ptr + 2, m + 1, e, index, val, offsetVal + offset[ptr]);
                return (check != -1) ? check : findRightFirstMin(2 * ptr + 1, s, m, index, val, offsetVal + offset[ptr]);
            }
        }
    }















    public static void main(String[] args) {
        SpecialSegTree st = new SpecialSegTree(10);
        st.updateMin(0, 0, 9, 4, 0, 0);
        
        st.rangeUpdate(0, 0, 9, 0, 3, 10, 0);
        st.rangeUpdate(0, 0, 9, 3, 9, 20, 0);
        st.updateMin(0, 0, 9, 0, 100, 0);
        //long ans = st.findMin(0, 0, 9, 0, 0, 0);
        int index = st.findRightFirstMin(0, 0, 9, 4, 30, 0);
        //System.out.println(ans);
        System.out.println(index);
    }

    
    
}