import java.util.*;

public class RangeMaxRangeFillSegTree {
    int[] tree;
    int n;
    boolean[] filled;

    
    
    

    // invariant, everytime we recurse on a range, it is correct, i.e if filled, filled[ptr] is true
    // also tree[p] the max of the range is correct

    // to preserve variant, edit children before recursing on children


    public RangeMaxRangeFillSegTree(int[] array) {
        n = array.length;
        tree = new int[4 * n];
        build(0, 0, n - 1, array);
        filled = new boolean[4 * n];
    }
    
    
    
    
    public void build(int ptr, int l, int r, int[] array) {
        if (l == r) {
            tree[ptr] = array[l];
        } else {
            int m = (l + r) / 2;
            build(2 * ptr + 1, l, m, array);
            build(2 * ptr + 2, m + 1, r, array);
            tree[ptr] = Math.max(tree[2 * ptr + 1], tree[2 * ptr + 2]);
        }
    }

    public int leftMax(int p, int s, int e, int l) {
        if (s == e) {
            return tree[p];
        } else if (filled[p]) {
            return tree[p];
        } else if (l == s) {
            return tree[p];
        } else {
            int m = (s + e) / 2;
            if (l > m) {
                return leftMax(2 * p + 2, m + 1, e, l);
            } else {
                return Math.max(tree[2 * p + 2], leftMax(2 * p + 1, s, m, l));
            }
        }
    }

    public int rightMax(int p, int s, int e, int r) {
        if (s == e) {
            return tree[p];
        } else if (filled[p]) {
            return tree[p];
        } else if (r == e) {
            return tree[p];
        } else {
            int m = (s + e) / 2;
            if (r <= m) {
                return rightMax(2 * p + 1, s, m, r);
            } else {
                return Math.max(tree[2 * p + 1], rightMax(2 * p + 2, m + 1, e, r));
            }
        }
    }

    public int rangeMax(int p, int s, int e, int l, int r) {
        if (s == e) {
            return tree[p];
        } else if (filled[p]) {
            return tree[p];
        } else if (s == l && e == r) {
            return tree[p];
        } else {
            int m = (s + e) / 2;
            if (r <= m) {
                return rangeMax(2 * p + 1, s, m, l, r);
            } else if (l > m) {
                return rangeMax(2 * p + 2, m + 1, e, l, r);
            } else {
                return Math.max(leftMax(2 * p + 1, s, m, l), rightMax(2 * p + 2, m + 1, e, r));
            }
        }
    }

    public void leftFill(int p, int s, int e, int l, int fillValue) {
        if (s == e) {
            tree[p] = fillValue;
            filled[p] = true;
        } else if (s == l) {
            tree[p] = fillValue;
            filled[p] = true;
        } else if (filled[p]) {
            int m = (s + e) / 2;
            if (l > m) {
                tree[2 * p + 1] = tree[p];
                filled[2 * p + 1] = true;
                tree[2 * p + 2] = tree[p];
                filled[2 * p + 2] = true;
                // can update max first, because [s, e] is filled
                tree[p] = Math.max(tree[p], fillValue);
                filled[p] = false;
                leftFill(2 * p + 2, m + 1, e, l, fillValue);
            } else {
                tree[2 * p + 2] = fillValue;
                filled[2 * p + 2] = true;
                tree[2 * p + 1] = tree[p];
                filled[2 * p + 1] = true;
                tree[p] = Math.max(tree[p], fillValue);
                filled[p] = false;
                leftFill(2 * p + 1, s, m, l, fillValue);
            }
        } else {
            int m = (s + e) / 2;
            if (l > m) {
                //tree[p] = Math.max(tree[p], fillValue);
                leftFill(2 * p + 2, m + 1, e, l, fillValue);
                tree[p] = Math.max(tree[2 * p + 1], tree[2 * p + 2]);
            } else {
                //tree[p] = Math.max(tree[p], fillValue);
                tree[2 * p + 2] = fillValue;
                filled[2 * p + 2] = true;
                leftFill(2 * p + 1, s, m, l, fillValue);
                tree[p] = Math.max(tree[2 * p + 1], tree[2 * p + 2]);
            }
        }
    }


    public void rightFill(int p, int s, int e, int r, int fillValue) {
        if (s == e) {
            tree[p] = fillValue;
            filled[p] = true;
        } else if (e == r) {
            tree[p] = fillValue;
            filled[p] = true;
        } else if (filled[p]) {
            int m = (s + e) / 2;
            if (r <= m) {
                tree[2 * p + 2] = tree[p];
                filled[2 * p + 2] = true;
                tree[2 * p + 1] = tree[p];
                filled[2 * p + 1] = true;
                tree[p] = Math.max(tree[p], fillValue);
                filled[p] = false;
                rightFill(2 * p + 1, s, m, r, fillValue);
            } else {
                tree[2 * p + 1] = fillValue;
                filled[2 * p + 1] = true;
                tree[2 * p + 2] = tree[p];
                filled[2 * p + 2] = true;
                tree[p] = Math.max(tree[p], fillValue);
                filled[p] = false;
                rightFill(2 * p + 2, m + 1, e, r, fillValue);
            }
        } else {
             int m = (s + e) / 2;
             if (r <= m) {
                //tree[p] = Math.max(tree[p], fillValue);
                rightFill(2 * p + 1, s, m, r, fillValue);
                tree[p] = Math.max(tree[2 * p + 1], tree[2 * p + 2]);
             } else {
                tree[2 * p + 1] = fillValue;
                filled[2 * p + 1] = true;
                //tree[p] = Math.max(tree[p], fillValue);
                rightFill(2 * p + 2, m + 1, e, r, fillValue);
                tree[p] = Math.max(tree[2 * p + 1], tree[2 * p + 2]);
             }
        }
    }

    public void rangeFill(int p, int s, int e, int l, int r, int fillValue) {
        if (s == e) {
            tree[p] = fillValue;
            filled[p] = true;
        } else if (s == l && e == r) {
            tree[p] = fillValue;
            filled[p] = true;
        } else if (filled[p]) {
            int m = (s + e) / 2;
            if (r <= m) {
                tree[2 * p + 2] = tree[p];
                filled[2 * p + 2] = true;
                tree[2 * p + 1] = tree[p];
                filled[2 * p + 1] = true;
                tree[p] = Math.max(tree[p], fillValue);
                filled[p] = false;
                rangeFill(2 * p + 1, s, m, l, r, fillValue);
            } else if (l > m) {
                tree[2 * p + 1] = tree[p];
                filled[2 * p + 1] = true;
                tree[2 * p + 2] = tree[p];
                filled[2 * p + 2] = true;
                tree[p] = Math.max(tree[p], fillValue);
                filled[p] = false;
                rangeFill(2 * p + 2, m + 1, e, l, r, fillValue);
            } else {
                tree[2 * p + 1] = tree[p];
                filled[2 * p + 1] = true;
                tree[2 * p + 2] = tree[p];
                filled[2 * p + 2] = true;
                tree[p] = Math.max(tree[p], fillValue);
                filled[p] = false;
                leftFill(2 * p + 1, s, m, l, fillValue);
                rightFill(2 * p + 2, m + 1, e, r, fillValue);
            }
        } else {
            int m = (s + e) / 2;
            if (r <= m) {
                //tree[p] = Math.max(tree[p], fillValue);
                rangeFill(2 * p + 1, s, m, l, r, fillValue);
                tree[p] = Math.max(tree[2 * p + 1], tree[2 * p + 2]);
            } else if (l > m) {
                //tree[p] = Math.max(tree[p], fillValue);
                rangeFill(2 * p + 2, m + 1, e, l, r, fillValue);
                tree[p] = Math.max(tree[2 * p + 1], tree[2 * p + 2]);
            } else {
                leftFill(2 * p + 1, s, m, l, fillValue);
                rightFill(2 * p + 2, m + 1, e, r, fillValue);
                tree[p] = Math.max(tree[2 * p + 1], tree[2 * p + 2]);
            }
        }
    }



    public static void main(String[] args) {
        int[] a = new int[] {0,0,0,0,0};
        RangeMaxRangeFillSegTree s = new RangeMaxRangeFillSegTree(a);
        //System.out.println(s.n);
        s.rangeFill(0, 0, 4, 1, 2, 0);
        System.out.println(s.rangeMax(0, 0, 4, 0, 4));
        
        s.rangeFill(0, 0, 4, 0, 4, 2);
        System.out.println(s.rangeMax(0, 0, 4, 0,  4));
        
        s.rangeFill(0, 0, 4, 1,3, 6);
        //System.out.println(s.rightMax(2, 3, 4, 3));
        System.out.println(s.rangeMax(0, 0, 4, 0,3));

        System.out.println(Arrays.toString(s.tree));
        System.out.println(Arrays.toString(s.filled));
        //System.out.println(s.rangeMax(0, 0, 4, 0, 4));
        //s.rangeFill(0, 0, 0, 1, 2, 38);
        //s.rangeFill(0, 0, 0, 0, s.n - 1, 0);
        //System.out.println(s.rangeMax(0, 0, 5, 0, 5));
    }
}