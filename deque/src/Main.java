// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Deque<Integer> dq = new Deque<>();
        for (int i = 0; i < 11; i ++) {
            dq.addLast(i);
            //System.out.println(dq.last.val.toString());
        }
        Deque<Integer> dq2 = new Deque<>();
        for (int i = 110; i < 114; i ++) {
            dq2.addLast(i);
        }
        dq.appendFront(dq2);
        //dq.remove(0);
       // dq.remove(1);
        //dq.remove(0);
        //dq.remove(10);
        //dq.appendFront(dq2);
        //dq.addLast(999);
        //dq.addFirst(1000);
        Deque<String> dq3 = dq.map(x -> "hello");
        //Deque<Integer> dq4 = dq.filter(x -> x%2 == 1);
        //System.out.println(dq3.toString());
        //System.out.println(dq4.toString());
        System.out.println(dq.toString());
        //System.out.println(dq2.toString());
        //System.out.println(dq.get(5));
        


    }
}