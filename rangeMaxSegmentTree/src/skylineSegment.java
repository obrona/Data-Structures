// data structure that answer this query : given a set of intervals, and each interval has a value x
// for a point p, return the maximum of x for all intervals it intersects

public class skylineSegment {
    int[] tree;
    int size;

    public skylineSegment(int size) {
        this.size = size;
        tree = new int[4 * size];
    }

    // if newValue is lesser than the value currently stored, don't update
    // Invariant of skylineSegment -> in each node stores the max value for values between start to end inclusive
    // we always update fully, i.e if we want to update 3, 6 to new value, if the current range is like 0, 7, we don't
    // update node that represent 0, 7, in fact in this case we this is split node and we must do left and right update

    public void leftUpdate(int index, int pointer, int start, int end, int newValue) {
        if (start == end) {
            tree[pointer] = Math.max(tree[pointer], newValue);
        } else if (index == start) {
            tree[pointer] = Math.max(tree[pointer], newValue);
        } else {
            int mid = (start + end) / 2;
            if (index <= mid) {
                tree[2 * pointer + 2] = Math.max(tree[2 * pointer + 2], newValue);
                leftUpdate(index, 2 * pointer + 1, start, mid, newValue);
            } else {
                leftUpdate(index, 2 * pointer + 2, mid + 1, end, newValue);
            }
        }
    }

    public void rightUpdate(int index, int pointer, int start, int end, int newValue) {
        if (start == end) {
            tree[pointer] = Math.max(tree[pointer], newValue);
        } else if (index == end) {
            tree[pointer] = Math.max(tree[pointer], newValue);
        } else {
            int mid = (start + end) / 2;
            if (index <= mid) {
                rightUpdate(index, 2 * pointer + 1, start, mid, newValue);
            } else {
                tree[2 * pointer + 1] = Math.max(tree[2 * pointer + 1], newValue);
                rightUpdate(index, 2 * pointer + 2, mid + 1, end, newValue);
            }
        }
    }

    public void rangeUpdate(int index1, int index2, int pointer, int start, int end, int newValue) {
        if (start == end) {
            tree[pointer] = Math.max(tree[pointer], newValue);
        } else if (index1 == start && index2 == end) {
            tree[pointer] = Math.max(tree[pointer], newValue);
        } else {
            int mid = (start + end) / 2;
            if (index2 <= mid) {
                rangeUpdate(index1, index2, 2 * pointer + 1, start, mid, newValue);
            } else if (index1 > mid) {
                rangeUpdate(index1, index2, 2 * pointer + 2, mid + 1, end, newValue);
            } else {
                // mid is split node
                leftUpdate(index1, 2 * pointer + 1, start, mid, newValue);
                rightUpdate(index2, 2 * pointer + 2, mid + 1, end, newValue);
            }
        }
    }

    public int getMax(int index, int pointer, int start, int end) {
        if (start == end) {
            return tree[pointer];
        } else {
            int mid = (start + end) / 2;
            if (index <= mid) {
                return Math.max(tree[pointer], getMax(index, 2 * pointer + 1, start, mid));
            } else {
                return Math.max(tree[pointer], getMax(index, 2 * pointer + 2, mid + 1, end));
            }
        }
    }
}
