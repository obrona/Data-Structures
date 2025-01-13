import java.util.function.Function;

public class Node<S> {
    Node<S> prev;
    Node<S> next;
    S val;

    public Node(S val) {
        this.val = val;
    }

    public Node<S> getLast() {
        if (this.next == null) {
            return this;
        } else {
            return this.next.getLast();
        }
    }

    public Node<S> get(int index) {
        if (this.next == null && index != 0) {
            return null;
        } else {
            if (index == 0) {
                return this;
            } else {
                return this.next.get(index - 1);
            }
        }
    }
    // Map is destructive, the original deque is modified and not a new one is create
    public <R> Node<R> map(Function<? super S, ? extends R> fun) {
        R val = fun.apply(this.val);
        if (this.next == null) {
            // Hack it by typecasting. Don't be afraid to typecast
            // Node is just a wrapper anyway
            Node<R> n = (Node<R>) this;
            n.val = val;
            return n;
        } else {
            Node<R> n = (Node<R>) this;
            n.val = val;
            Node<R> m = this.next.map(fun);
            n.next = m;
            m.prev = n;
            return n;
        }
    }
    // Filter is also destructive
    public Node<S> filter(Function<? super S, Boolean> predicate) {
        if (this.next == null) {
            return (predicate.apply(this.val)) ? this : null;
        } else if (predicate.apply(this.val)) {
            this.next = this.next.filter(predicate);
            return this;
        } else {
            return this.next.filter(predicate);
        }
    }

    @Override
    public String toString() {
        return this.toSB().toString();
    }

    public StringBuilder toSB() {
        Node<S> curr = this;
        StringBuilder sb = new StringBuilder();
        while (curr != null) {
            sb.append(curr.val.toString());
            if (curr.next != null) {
                sb.append(" ");
            }
            curr = curr.next;
        }
        return sb;
    }
}