// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        AVLNode root = new AVLNode(1);
        for (int i = 2; i < 10000; i ++) {
            root = root.insert(root, i);
        }
        for (int i = 1; i <= 8000; i ++) {
            root = root.delete(root, i);
        }
        //root = root.delete(root,1);
        //root = root.delete(root, 1);
        //root = root.delete(root,73);
        //System.out.println(root.sum);
        System.out.println(root.rangeSumQuery(root, 200, 400));



    }
}