import java.util.Arrays;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        int[][] m = new int[][] {{1,2,3,4},{4,5,6,8},{7,8,9,10}};
        twoDSegment sgtree = new twoDSegment(m);
        int x = sgtree.xRangeSum(0, 0, 2, 1, 2, 1,3);
        System.out.println(x);
        sgtree.xUpdate(1, 1, 100);
        //sgtree.xUpdate(1, 2, 100);
        sgtree.xUpdate(2, 3, 100);
        int y = sgtree.xRangeSum(0, 0, 2, 1, 2, 1,3);
        System.out.println(y);
        for (int i = 0; i < sgtree.tree.length; i ++) {
            System.out.println(Arrays.toString(sgtree.tree[i]));
        }
    }

}