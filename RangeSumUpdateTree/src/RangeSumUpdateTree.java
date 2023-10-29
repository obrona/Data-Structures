import java.util.Arrays;

public class RangeSumUpdateTree {
	long[] tree;
	long[] entireRange;
	int n;

	public RangeSumUpdateTree(int size) {
		this.tree = new long[size * 4];
		this.entireRange = new long[size * 4];
		this.n = size;
	}

	// entireRange[pI] : we need to add this value to all elements[start, end]


	// How do create a segment tree that can do BOTH rangeSum and rangeUpdate?
	// invariant: during the updates, we only update entire range if all elements[start, end]
	// need to be added
	// We also must stop early, eg if we got the correct range already eg start == left, end == right, in leftUpdate
	// and rightUpdate, we stop recursing.
	//
	// when we sum we keep track of accumulate or accu, the sum of the entireRange[parentIndex] of the parent nodes
	// suppose our sum ends at this node
	// clearly we need to add accu * (end - start + 1) as the accu indicates that rangeUpdates stopped at the parents
	// above, so sum curr node was not updated
	// What for the rangeUpdate that ended at children nodes
	// they were updated in the curr node too eg sum(currNode) += (right - left + 1) * toAdd



	public long leftSum(int pI, long start, long end, long left, long accu) {
		if (start == end) {
			// we got the range
			return tree[pI] + accu;
		} else if (left == start) {
			return tree[pI] + accu * (end - start + 1);
		} else {
			long mid = (start + end) / 2;
			if (left <= mid) {
				return tree[2 * pI + 2] + (accu + entireRange[pI]) * (end - (mid + 1) + 1) +
					leftSum(2 * pI + 1, start, mid, left,accu + entireRange[pI]);
			} else {
				return leftSum(2 * pI + 2, mid + 1, end, left,accu + entireRange[pI]);
			}
		}
	}

	public long rightSum(int pI, long start, long end, long right, long accu) {
		if (start == end) {
			// we got the range
			return tree[pI] + accu;
		} else if (right == end) {
			return tree[pI] + accu * (end - start + 1);
		} else {
			long mid = (start + end) / 2;
			if (right <= mid) {
				return rightSum(2 * pI + 1, start, mid, right,accu + entireRange[pI]);
			} else {
				return tree[2 * pI + 1] + (accu + entireRange[pI]) * (mid - start + 1) +
					rightSum(2 * pI + 2, mid + 1, end, right, accu + entireRange[pI]);
			}
		}
	}

	public long rangeSum(int pI, long start, long end, long left, long right, long accu) {
		//System.out.println("" + pI + " " + left + " " + right + " " + start + " " + end);
		if (start == end) {
			//System.out.println("" + pI + " " + left + " " + right + " " + start + " " + end);
			return tree[pI] + accu;
		} else if (start == left && end == right) {
			return tree[pI] + accu * (end - start + 1);
		} else {
			long mid = (end + start) / 2;
			if (left > mid) {
				return rangeSum(2 * pI + 2, mid + 1, end, left, right, accu + entireRange[pI]);
			} else if (right <= mid) {
				return rangeSum(2 * pI + 1, start, mid, left, right, accu + entireRange[pI]);
			} else {
				return leftSum(2 * pI + 1, start, mid, left, accu + entireRange[pI]) +
					rightSum(2 * pI + 2, mid + 1, end, right, accu + entireRange[pI]);
			}
		}
	}

	public void leftUpdate(int pI, long start, long end, long left, long toAdd) {
		if (start == end) {
			tree[pI] += toAdd;
			entireRange[pI] += toAdd;
		} else if (start == left) {
			tree[pI] += toAdd * (end - start + 1);
			entireRange[pI] += toAdd;
		} else {
			long mid = (start + end) / 2;
			tree[pI] += (end - left + 1) * toAdd;
			if (left <= mid) {
				leftUpdate(2 * pI + 1, start, mid, left, toAdd);
				leftUpdate(2 * pI + 2, mid + 1, end, mid + 1, toAdd);
			} else {
				leftUpdate(2 * pI + 2, mid + 1, end , left, toAdd);
			}
		}
	}

	public void rightUpdate(int pI, long start, long end, long right, long toAdd) {
		if (start == end) {
			tree[pI] += toAdd;
			entireRange[pI] += toAdd;
		} else if (end == right) {
			tree[pI] += toAdd * (end - start + 1);
			entireRange[pI] += toAdd;
		} else {
			long mid = (start + end) / 2;
			tree[pI] += (right - start + 1) * toAdd;
			if (right <= mid) {
				rightUpdate(2 * pI + 1, start, mid, right, toAdd);
			} else {
				rightUpdate(2 * pI + 1, start, mid, mid, toAdd);
				rightUpdate(2 * pI + 2, mid + 1, end, right, toAdd);
			}
		}
	}

	public void rangeUpdate(int pI, long start, long end, long left, long right, long toAdd) {
		if (start == end) {
			tree[pI] += toAdd;
			entireRange[pI] += toAdd;
		} else if (start == left && end == right) {
			tree[pI] += toAdd * (end - start + 1);
			entireRange[pI] += toAdd;
		} else {
			long mid = (start + end) / 2;
			tree[pI] += (right - left + 1) * toAdd;
			if (left > mid) {
				rangeUpdate(2 * pI + 2, mid + 1, end, left, right, toAdd);
			} else if (right <= mid) {
				rangeUpdate(2 * pI + 1, start, mid, left, right, toAdd);
			} else {
				leftUpdate(2 * pI + 1, start, mid, left, toAdd);
				rightUpdate(2 * pI + 2, mid + 1, end, right, toAdd);
			}
		}
	}

	public static void main(String[] args) {
		RangeSumUpdateTree tt = new RangeSumUpdateTree(9);
		tt.rangeUpdate(0, 0, 8, 0, 0, 1);
		tt.rangeUpdate(0, 0, 8, 0, 1, 1);
		tt.rangeUpdate(0, 0, 8, 1, 2, 1);
		tt.rangeUpdate(0, 0, 8, 0, 3, 1);
		tt.rangeUpdate(0, 0, 8, 3, 4, 1);
		tt.rangeUpdate(0, 0, 8, 0, 5, 1);
		tt.rangeUpdate(0, 0, 8, 2, 6, 1);
		long sum = tt.rangeSum(0, 0, 8,  2,7, 0);
		System.out.println(sum);
		System.out.println(Arrays.toString(tt.tree));
		System.out.println(Arrays.toString(tt.entireRange));
		//System.out.println(tt.tree[11]);
		System.out.println(tt.rightSum(2, 5, 8, 5, 0));
		System.out.println(tt.leftSum(1, 0, 4, 2, 0));
		//System.out.println(tt.leftSum(3, 0, 2, 2, 1));
	}
}
