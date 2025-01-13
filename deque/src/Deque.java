import java.util.function.Function;

public class Deque<T> {
    Node<T> first;
    Node<T> last;


    public void addFirst(T val) {
        Node<T> n = new Node<>(val);
        if (first == null && last == null) {
            first = n;
            last = n;
        } else {
            n.next = first;
            first.prev = n;
            first = n;
        }
    }

    public void addLast(T val) {
       Node<T> n = new Node<>(val);
        if (first == null && last == null) {
           first = n;
           last = n;
       } else {
            n.prev = last;
            last.next = n;
            last = n;
        }
    }

    public T get(int index) {
       Node<T> curr = first;
       while (index > 0) {
           if (curr == null) {
               break;
           } else {
               curr = curr.next;
               index --;
           }
       }
       return (curr == null) ? null :curr.val;
    }

    public void remove(int index) {
        Node<T> r = first.get(index);
        if (r != null) {
            if (r.prev == null && r.next == null) {
                first = null;
                last = null;
            } else if (r.prev == null) {
                this.first = r.next;
                r.next.prev = null;
            } else if (r.next == null) {
                this.last = r.prev;
                r.prev.next = null;
            } else {
                r.prev.next = r.next;
                r.next.prev = r.prev;
            }
        }
    }

    public Deque<T> appendBack(Deque<? extends T> deque) {
        if (deque.first == null && deque.last == null) {
            return this;
        }
        Node<T> f = (Node<T>) deque.first;
        last.next = f;
        f.prev = last;
        last = (Node<T>) deque.last;
        return this;
    }

    public Deque<T> appendFront(Deque<? extends T> deque) {
        // Use type cast to get around incompatible type errors. You know is safe because ? extends T will do
        // the type checking
        if (deque.first == null && deque.last == null) {
            return this;
        }
        Node<T> f = this.first;
        Node<T> g = (Node<T>) deque.last;
        g.next = f;
        f.prev = g;
        this.first = (Node<T>) deque.first;
        return this;
    }

    public <R> Deque<R> map(Function<? super T, ? extends R> fun) {
        Deque<R> mapped = new Deque<R>();
        Node<R> first = this.first.map(fun);
        Node<R> last = first.getLast();
        mapped.first = first;
        mapped.last = last;
        return mapped;
    }

    public Deque<T> filter(Function<? super T, Boolean> fun) {
        Deque<T> filtered = new Deque<>();
        Node<T> f = this.first.filter(fun);
        if (f != null) {
            filtered.first = f;
            filtered.last = f.getLast();
        }
        return filtered;
    }



    @Override
    public String toString() {
        if (first == null && last == null) {
            return "";
        }
        return first.toString();
    }

}
