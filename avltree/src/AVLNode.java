public class AVLNode {
    long val;
    int count;
    int height;
    int weight;
    long sum;
    AVLNode left;
    AVLNode right;

    public AVLNode(int val) {
        this.val = val;
        count = 1;
        weight = 1;
        sum = val;
        height = 0;
    }

    public int getHeight(AVLNode node) {
        if (node == null) {
            return -1;
        } else {
            return node.height;
        }
    }

    public int getWeight(AVLNode node) {
        if (node == null) {
            return 0;
        } else {
            return node.weight;
        }
    }

    public long getSum(AVLNode node) {
        if (node == null) {
            return 0;
        } else {
            return node.sum;
        }
    }
    // right heavy trees
    public AVLNode leftRotate(AVLNode node, boolean forced) {
        if (forced) {
            AVLNode newRoot = node.right;
            node.right = node.right.left;
            node.weight = node.count + getWeight(node.left) + getWeight(node.right);
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
            node.sum = node.val * node.count + getSum(node.left) + getSum(node.right);
            newRoot.left = node;
            newRoot.weight = newRoot.count + getWeight(newRoot.left) + getWeight(newRoot.right);
            newRoot.sum = newRoot.val * newRoot.count + getSum(newRoot.left) + getSum(newRoot.right);
            //System.out.println(newRoot);
            return newRoot;
        } else if (getHeight(node.right.left) > Math.max(getHeight(node.right.right), getHeight(node.left))) {
            node.right = rightRotate(node.right, true);
            return leftRotate(node, true);
        } else {
            return leftRotate(node, true);
        }
    }
    // left heavy trees
    public AVLNode rightRotate(AVLNode node, boolean forced) {
        if (forced)  {
            AVLNode newRoot = node.left;
            node.left = node.left.right;
            node.weight = node.count + getWeight(node.left) + getWeight(node.right);
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
            node.sum = node.val * node.count + getSum(node.right) + getSum(node.left);
            newRoot.right = node;
            newRoot.weight = newRoot.count + getWeight(newRoot.left) + getWeight(newRoot.right);
            newRoot.height = 1 + Math.max(getHeight(newRoot.left), getHeight(newRoot.right));
            newRoot.sum = newRoot.val * newRoot.count + getSum(newRoot.left) + getSum(newRoot.right);
            //System.out.println(newRoot);
            return newRoot;
        } else if (getHeight(node.left.right) > Math.max(getHeight(node.left.left), getHeight(node.right))) {
            node.left = leftRotate(node.left, true);
            return rightRotate(node, true);
        } else {
            return rightRotate(node, true);
        }
    }

    public AVLNode insert(AVLNode node, int val) {
        //System.out.println("hello");
        if (node == null) {
            return new AVLNode(val);
        } else if (node.val == val) {
            node.count += 1;
            node.weight += 1;
            node.sum += val;
            return node;
        } else if (node.val > val) {
            node.left = insert(node.left, val);
            if (getHeight(node.left) > 1 + getHeight(node.right)) {
                return rightRotate(node, false);
            } else {
                node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
                node.weight = node.count + getWeight(node.left) + getWeight(node.right);
                node.sum = node.val * node.count + getSum(node.left) + getSum(node.right);
                return node;
            }
        } else {
            node.right = insert(node.right, val);
            if (getHeight(node.right) > getHeight(node.left) + 1) {
                return leftRotate(node, false);
            } else {
                node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
                node.weight = node.count + getWeight(node.left) + getWeight(node.right);
                node.sum = node.val * node.count + getSum(node.left) + getSum(node.right);
                return node;
            }
        }
    }

    // a small class to store 2 AVLNodes
    // Damn I could have just used an array, this both fields are of the same type
    class Pair {
        AVLNode succ;
        AVLNode result;

        public Pair(AVLNode succ, AVLNode result) {
            this.succ = succ;
            this.result = result;
        }

    }

    // Only for BST where all nodes are larger than target node
    // Pair stores the successor node, and the balanced BST of the right tree with the successor node deleted
    // Simply traverse left as much as possible
    public Pair getDeleteSuccessor(AVLNode node) {
        if (node.left == null) {
            return new Pair(node, node.right);
        } else {
            Pair p = getDeleteSuccessor(node.left);
            node.left = p.result;
            if (getHeight(node.right) > getHeight(node.left) + 1) {
                // the new balanced node
                p.result = leftRotate(node, false);
            } else {
                node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
                node.weight = node.count + getWeight(node.left) + getWeight(node.right);
                node.sum = node.val * node.count + getSum(node.left) + getSum(node.right);
                p.result = node;
            }
            return p;
        }
    }
    public AVLNode delete(AVLNode node, int val) {
        if (node == null) {
            return null;
        } else if (node.val < val) {
            node.right = delete(node.right, val);
            if (getHeight(node.left) > getHeight(node.right) + 1) {
                return rightRotate(node, false);
            } else {
                node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
                node.weight = node.count + getWeight(node.left) + getWeight(node.right);
                node.sum = node.val * node.count + getSum(node.left) + getSum(node.right);
                return node;
            }
        } else if (node.val > val) {
            node.left = delete(node.left, val);
            if (getHeight(node.right) > getHeight(node.left) + 1) {
                return leftRotate(node, false);
            } else {
                node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
                node.weight = node.count + getWeight(node.left) + getWeight(node.right);
                node.sum = node.val * node.count + getSum(node.left) + getSum(node.right);
                return node;
            }
        } else {
            if (node.count > 1) {
                node.count -= 1;
                node.weight -= 1;
                node.sum -= node.val;
                return node;
            } else if (node.left == null && node.right == null) {
                return null;
            } else if (node.left != null && node.right == null) {
                return node.left;
            } else if (node.left == null && node.right != null) {
                return node.right;
            } else {
                Pair p = getDeleteSuccessor(node.right);
                AVLNode newRoot = p.succ;
                newRoot.left = node.left;
                newRoot.right = p.result;
                newRoot.height = 1 + Math.max(getHeight(newRoot.left), getHeight(newRoot.right));
                newRoot.weight = newRoot.weight + getWeight(newRoot.left) + getWeight(newRoot.right);
                newRoot.sum = newRoot.val * newRoot.count + getSum(newRoot.left) + getSum(newRoot.right);
                // Only need to check if left heavy, as original tree is already balanced, and we delete from the right.
                if (getHeight(newRoot.left) > getHeight(newRoot.right) + 1) {
                    return rightRotate(newRoot, false);
                } else {
                    return newRoot;
                }
            }
        }
    }
    // gets the ith position, in the sorted order of the elements in increasing order;
    // the indexes are 1-index, counting starts from 1
    // this is so that we don't call on null nodes
    // Invariant: always recurse on valid node, no null node
    public long getPosition(AVLNode node, int index) {
        int leftCount = getWeight(node.left);
        if (index <= leftCount) {
            return getPosition(node.left, index);
        } else if (leftCount < index && index <= leftCount + node.count) {
            return node.val;
        } else {
            return getPosition(node.right, index - leftCount - node.count);
        }
    }

    // gets the range sum query
    // from index i to index j inclusive at both ends
    // remember, elements are 1 - index
    // need to get split node for left and right traversal

    // takes sum of index to split node, not including the split node, so is left index
    // to the rightmost element of the left subtree of the split node
    public long leftTraversal(AVLNode node, int index) {
        int leftCount = getWeight(node.left);
        if (index <= leftCount) {
            return leftTraversal(node.left, index) + node.val * node.count +
                getSum(node.right);
        } else if (leftCount < index && index <= leftCount + node.count) {
            return node.val * (leftCount + node.count - index + 1) + getSum(node.right);
        } else {
            return leftTraversal(node.right, index - leftCount - node.count);
        }
    }
    // takes sum of right index to the leftmost element of the split node right subtree
    public long rightTraversal(AVLNode node, int index) {
        int leftCount = getWeight(node.left);
        if (index <= leftCount) {
            return rightTraversal(node.left, index);
        } else if (index < leftCount) {
            return rightTraversal(node.left, index);
        } else if (leftCount < index && index <= leftCount + node.count) {
            return node.val * (index - leftCount) + getSum(node.left);
        } else {
            return getSum(node.left) + node.val * node.count + rightTraversal(node.right, index - leftCount -
                node.count);
        }
    }
    // Remember, the elements are 1-index
    public long rangeSumQuery(AVLNode node, int start, int end) {
        int leftCount = getWeight(node.left);
        if (end <= leftCount) {
            return rangeSumQuery(node.left, start, end);
        } else if (start > leftCount + node.count) {
            return rangeSumQuery(node.right, start - leftCount - node.count, end - leftCount - node.count);
        } else if (leftCount < start && start <= leftCount + node.count && end <= leftCount + node.count) {
            return node.val * (end - start + 1);
        } else if (leftCount < start) {
            return node.val * (leftCount + node.count - start + 1) + rightTraversal(node.right, end - leftCount - node.count);
        } else if (end <= leftCount + node.count) {
            return node.val * (end - leftCount) + leftTraversal(node.left, start);
        } else {
            return leftTraversal(node.left, start) + node.val * node.count + rightTraversal(node.right, end - leftCount - node.count);
        }
    }








}
