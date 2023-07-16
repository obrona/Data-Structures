public class RangeMax {
    int[] array;
    int[] tree;
    int size;

    public RangeMax(int[] array) {
        this.array = array;
        size = array.length - 1;
        tree = new int[array.length * 2];
        buildTree(array, 0, size, 0);

    }

    public int buildTree(int[] array, int start, int end, int index) {
        if (start == end) {
            tree[index] = array[start];
            return array[start];
        } else {
            int mid = (start + end) / 2;
            int left = buildTree(array, start, mid, 2 * index + 1);
            int right = buildTree(array, mid + 1, end, 2 * index + 2);
            tree[index] = Math.max(left, right);
            return tree[index];
        }
    }

    //Recursive Invariant: leftTraversal(l, s, e) return the max of elements from index l to index e

    public int leftTraversal(int leftEnd, int start, int end, int index) {
        if (start == end) {
            return tree[index];
        } else {
            int mid = (start + end) / 2;
            if (leftEnd > mid) {
                return leftTraversal(leftEnd, mid + 1, end, 2 * index + 2);
            } else {
                int right = tree[2 * index + 2];
                return Math.max(right, leftTraversal(leftEnd, start, mid, 2 * index + 1));
            }
        }
    }

    public int rightTraversal(int rightEnd, int start, int end, int index) {
        if (start == end) {
            return tree[index];
        } else {
            int mid = (start + end) / 2;
            if (rightEnd <= mid) {
                return rightTraversal(rightEnd, start, mid, 2 * index + 1);
            } else {
                int left = tree[2 * index  + 1];
                return Math.max(left, rightTraversal(rightEnd, mid + 1, end, 2 * index + 2));
            }
        }
    }

    public int findRangeMax(int leftEnd, int rightEnd, int start, int end, int index) {
        if (start == end) {
            return tree[index];
        } else {
            int mid = (start + end) / 2;
            if (rightEnd <= mid) {
                return findRangeMax(leftEnd, rightEnd, start, mid, 2 * index + 1);
            } else if (leftEnd > mid) {
                return findRangeMax(leftEnd, rightEnd, mid + 1, end, 2 * index + 2);
            } else {
                int l = leftTraversal(leftEnd, start, mid, 2 * index + 1);
                int r = rightTraversal(rightEnd, mid + 1, end, 2 * index + 2);
                return Math.max(l, r);
            }
        }
    }
    //Recursive Invariant: start <= arrayIndex <= end
    public void update(int arrayindex, int newValue, int start, int end,int index) {
        if (start == end) {
            tree[index] = newValue;
        } else {
            tree[index] = Math.max(tree[index], newValue);
            int mid = (start + end) / 2;
            if (arrayindex <= mid) {
                update(arrayindex, newValue, start, mid, 2 * index + 1);
            } else if (arrayindex > mid) {
                update(arrayindex, newValue, mid + 1, end, 2 * index + 2);
            }
        }
    }
}
