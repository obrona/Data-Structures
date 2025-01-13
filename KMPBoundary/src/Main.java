import java.util.Arrays;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
       Boundary b = new Boundary();
       int[] s = b.boundary("abaababaa");
       System.out.println(Arrays.toString(s));
       int x = b.patternMatch("cateatdog", "hate");
       System.out.println(x);
    }

}