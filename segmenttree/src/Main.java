import java.util.Arrays;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        int [] test = new int[] {3,1,5,6,2,2,6,9,8,1,10,11,0,0,20};
        /*SegmentTree sgt = new SegmentTree(test);
        System.out.println(Arrays.toString(sgt.tree));
        int x = sgt.findSumRange(1, 3, 0, 7, 0);
        sgt.update(1, 100, 0, 7, 0);
        int y = sgt.findSumRange(1, 3, 0, 7, 0);
        System.out.println(x);
        System.out.println(y);*/

        RangeMax rm = new RangeMax(test);
        System.out.println(Arrays.toString(rm.tree));
        int x = rm.findRangeMax(8, 14, 0, 14, 0);
        rm.update(3, 9999, 0, 7, 0);
        int y = rm.findRangeMax(3, 14, 0, 14, 0);
        System.out.println(x);
        System.out.println(y);

    }
}