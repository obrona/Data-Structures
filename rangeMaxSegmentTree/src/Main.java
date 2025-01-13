import java.util.Arrays;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        skylineSegment sg = new skylineSegment(8);
        sg.rangeUpdate(3, 7, 0, 0, 7, 20);
        sg.rangeUpdate(4, 6, 0, 0, 0, 1);
        sg.rangeUpdate(1, 5, 0, 0,7, 11);
        sg.rangeUpdate(2, 6, 0,0 ,7, 5);
        sg.rangeUpdate(5, 5, 0, 0, 7, 999);
        int x = sg.getMax(6, 0, 0, 7);
        System.out.println(x);
        System.out.println(Arrays.toString(sg.tree));

    }
}