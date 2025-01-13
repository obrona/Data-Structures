public class rightTree {
    // update the range index -> end with + 1;
    // i.e value[index] + 1, value[index + 1] + 1 ... value[end] + 1
    // like cs2040s flipping  range of cards, ice cream, builder
    int[] tree;

    public rightTree(int size) {
        tree = new int[2 * size];
        //Arrays.fill(tree, -1);
    }

    public void update(int updateindex, int index, int start, int end) {
        if (index >= tree.length) {
            return;
        } else if (start == end) {
            tree[index] += 1;
        } else {
            int mid = (start + end) / 2 + 1;
            if (updateindex < mid) {
                tree[index] += 1;
                update(updateindex, 2 * index + 1, start, mid - 1);
            } else if (updateindex == mid) {
                tree[index] += 1;
            } else {
                update(updateindex, 2 * index + 2, mid, end);
            }
        }
    }

    public int find(int findindex, int index, int start, int end) {
        if (index >= tree.length) {
            return 0;
        } else if (start == end) {
            return tree[index];
        } else {
            int mid = (start + end) / 2 + 1;
            if (findindex < mid) {
                return find(findindex, 2 * index + 1, start, mid - 1);
            } else {
                return tree[index] + find(findindex, 2 * index + 2, mid, end);
            }
        }
    }

}


