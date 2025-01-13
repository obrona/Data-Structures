public class Box<T> {
    T val;

    public Box(T val) {
        this.val = val;
    }

    public void copyFrom(Box<? extends T> box) {
        this.val = box.val;
    }
}
