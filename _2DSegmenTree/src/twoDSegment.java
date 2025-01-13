public class twoDSegment {
    int[][] tree;
    int[][] matrix;
    int x;
    int y;

    public twoDSegment(int[][] matrix) {
        this.matrix = matrix;
        this.x = matrix.length;
        this.y = matrix[0].length;
        tree = new int[4 * x][4 * y];
        build(matrix, 0, 0, x - 1);
    }

    // 2d segment tree. Each x node of range start -> end contains a segment tree for the  y values

    // this is for when x range is only 1 element, i.e building a 1d segment tree for column startX
    public void buildY1Line(int[][] m, int pX, int startX, int pY, int startY, int endY) {
        if (startY == endY) {
            tree[pX][pY] = m[startX][startY];
        } else {
            int mid = (startY + endY) / 2;
            buildY1Line(m, pX, startX, 2 * pY + 1, startY, mid);
            buildY1Line(m, pX, startX, 2 * pY + 2, mid + 1, endY);
            tree[pX][pY] = tree[pX][2 * pY + 1] + tree[pX][2 * pY + 2];
        }
    }

    // Notice that pY is the same for the left and right segment tree, when merging
    // the range for the x node is constant when merging, only range that changes is the y range
    public void merge(int pX, int pY, int sY, int eY) {
        if (sY == eY) {
            tree[pX][pY] = tree[2 * pX + 1][pY] + tree[2 * pX + 2][pY];
        } else {
            // pY is the same , as the range of y for the left and right segment tree is the same
            // remember, pX,pY implicitly stores the range of X and Y respectively
            tree[pX][pY] = tree[2 * pX + 1][pY] + tree[2 * pX + 2][pY];
            int mid = (sY + eY) / 2;
            merge(pX, 2 * pY + 1, sY, mid);
            merge(pX, 2 * pY + 2, mid + 1, eY);

        }
    }

    public void build(int[][] m, int pX, int sX, int eX) {
        if (sX == eX) {
            buildY1Line(m, pX, sX, 0, 0, y - 1);
        } else {
            int mid = (sX + eX) / 2;
            build(m, 2 * pX + 1, sX, mid);
            build(m, 2 * pX + 2, mid + 1, eX);
            merge(pX, 0, 0, y - 1);
        }
    }

    public int yLeftTrav(int pX, int pY, int sY, int eY, int indexY) {
        if (sY == eY) {
            return tree[pX][pY];
        } else if (indexY == sY) {
            return tree[pX][pY];
        } else {
            int mY = (sY + eY) / 2;
            if (indexY <= mY) {
                return tree[pX][2 * pY + 2] + yLeftTrav(pX, 2 * pY + 1, sY, mY, indexY);
            } else {
                return yLeftTrav(pX, 2 * pY + 2, mY + 1, eY, indexY);
            }
        }
    }

    public int yRightTrav(int pX, int pY, int sY, int eY, int indexY) {
        if (sY == eY) {
            return tree[pX][pY];
        } else if (indexY == eY) {
            return tree[pX][pY];
        } else {
            int mY = (sY + eY) / 2;
            if (indexY <= mY) {
                return yRightTrav(pX, 2 * pY + 1, sY, mY, indexY);
            } else {
                return tree[pX][2 * pY + 1] + yRightTrav(pX, 2 * pY + 2, mY + 1, eY, indexY);
            }
        }
    }

    public int yRangeSum(int pX, int pY, int sY, int eY, int y1, int y2) {
        if (sY == eY) {
            return tree[pX][pY];
        } else if (y1 == sY && y2 == eY) {
            return tree[pX][pY];
        } else {
            int mid = (sY + eY) / 2;
            if (y2 <= mid) {
                return yRangeSum(pX, 2 * pY + 1, sY, mid, y1, y2);
            } else if (mid < y1) {
                return yRangeSum(pX, 2 * pY + 2, mid + 1, eY, y1, y2);
            } else {
                return yLeftTrav(pX, 2 * pY + 1, sY, mid, y1) + yRightTrav(pX, 2 * pY + 2, mid + 1,
                        eY, y2);
            }
        }
    }

    // Must trust yRangeSum to do its job
    // y1, y2 is the y range sum you want
    public int xLeftTrav(int pX, int sX, int eX, int indexX, int y1, int y2) {
        if (sX == eX) {
            return yRangeSum(pX, 0, 0, y - 1, y1, y2);
        } else if (sX == indexX) {
            return yRangeSum(pX, 0, 0, y - 1, y1, y2);
        } else {
            int mid = (sX + eX) / 2;
            if (indexX <= mid) {
                // yRangeSum of everything in the right x segment tree
                return yRangeSum(2 * pX + 2, 0, 0, y - 1, y1, y2) +
                        xLeftTrav(2 * pX + 1, sX, mid, indexX, y1, y2);
            } else {
                return xLeftTrav(2 * pX + 2, mid + 1, eX, indexX, y1, y2);
            }
        }
    }

    public int xRightTrav(int pX, int sX, int eX, int indexX, int y1, int y2) {
        if (sX == eX) {
            return yRangeSum(pX, 0, 0, y - 1, y1, y2);
        } else if (eX == indexX) {
            return yRangeSum(pX, 0, 0, y - 1, y1, y2);
        } else {
            int mid = (sX + eX) / 2;
            if (indexX <= mid) {
                return xRightTrav(2 * pX + 1, sX, mid, indexX, y1, y2);
            } else {
                return yRangeSum(2 * pX + 1, 0, 0, y - 1, y1, y2) +
                        xRightTrav(2 * pX + 2, mid + 1, eX, indexX, y1, y2);
            }
        }
    }

    public int xRangeSum(int pX, int sX, int eX,int x1, int x2, int y1, int y2) {
        if (sX == eX) {
            return yRangeSum(pX, 0, 0, y - 1, y1, y2);
        } else if (x1 == sX && x2 == eX) {
            return yRangeSum(pX, 0, 0, y - 1, y1, y2);
        } else {
            int mid = (sX + eX) / 2;
            if (x2 <= mid) {
                return xRangeSum(2 * pX + 1, sX, mid, x1, x2, y1, y2);
            } else if (mid < x1) {
                return xRangeSum(2 * pX + 2, mid + 1, eX, x1, x2, y1, y2);
            } else {
                return xLeftTrav(2 * pX + 1, sX, mid, x1, y1, y2) +
                        xRightTrav(2 * pX + 2, mid + 1, eX, x2, y1, y2);
            }
        }
    }

    // diff = newValue - oldValue
    public void yUpdate(int pX, int pY, int sY, int eY, int indexY, int diff) {
        if (sY == eY) {
            tree[pX][pY] += diff;
        } else {
            int mid = (sY + eY) / 2;
            tree[pX][pY] += diff;
            if (indexY <= mid) {
                yUpdate(pX, 2 * pY + 1, sY, mid, indexY, diff);
            } else {
                yUpdate(pX, 2 * pY + 2, mid + 1, eY, indexY, diff);
            }
        }
    }

    public void xUpdate2(int pX, int sX, int eX, int indexX, int indexY, int diff) {
        if (sX == eX) {
            yUpdate(pX, 0, 0, y - 1, indexY, diff);
        } else {
            int mid = (sX + eX) / 2;
            // update the x-coordinate segment tree
            yUpdate(pX, 0, 0, y - 1, indexY, diff);
            if (indexX <= mid) {
                xUpdate2(2 * pX + 1, sX, mid, indexX, indexY, diff);
            } else {
                xUpdate2(2 * pX + 2, mid + 1, eX, indexX, indexY, diff);
            }
        }
    }

    public void xUpdate(int indexX, int indexY, int newValue) {
        int diff = newValue - matrix[indexX][indexY];
        xUpdate2(0, 0, x - 1, indexX, indexY, diff);
    }















































}
