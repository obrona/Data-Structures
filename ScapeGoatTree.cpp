#include <bits/stdc++.h>
using namespace std;

// scapegoat tree. Each child weight is lesser than 2/3 * parent weight
// when inserting or deleting, rebalance the top most unbalanced node only.

class Node {
public:
    int v;
    int w;
    int sum;

    Node *l;
    Node *r;

    Node(int v): v(v), w(1), sum(v), l(nullptr), r(nullptr) {}

    static bool isUnbalanced(int w1, int w2) {
        return w1 > ((w2 << 1) + 3) / 3;
    }

    void clear() {
        l = nullptr; r = nullptr;
        w = 1;
        sum = v;
    }

    static int getWeight(Node* n) {
        return (n == nullptr) ? 0 : n->w;
    }

    static int getSum(Node *n) {
        return (n == nullptr) ? 0 : n->sum;
    }

    void update(Node *left, Node *right) {
        w = 1 + getWeight(left) + getWeight(right);
        sum = v + getSum(left) + getSum(right);
        l = left;
        r = right;
    }

    int getNumChild() {
        return (l != nullptr) + (r != nullptr);
    }

    static void inorder(Node *node, vector<Node*>& store) {
        if (node != nullptr) {
            inorder(node->l, store);
            store.push_back(node);
            inorder(node->r, store);
        }
    }

    static Node* build(int s, int e, vector<Node*>& store) {
        if (s > e) return nullptr;
        
        if (s == e) {
            store[s]->clear();
            return store[s];
        } else {
            int m = (s + e) >> 1;
            Node *l = build(s, m - 1, store);
            Node *r = build(m + 1, e, store);
            store[m]->update(l, r);
            return store[m];

        }
    }

    static Node *rebuild(Node *node) {
        vector<Node*> store;
        inorder(node, store);
        return build(0, store.size() - 1, store);
    }

    // flag means we have found the top most mode that is unbalcned and therefore do the rebalancing
    // we assume that val is not in tree at all
    // otherwise isUnbalanced(1 + getWeight(n->l), n->w + 1) is wrong
    static Node* insert(Node *n, int val, bool found) {
        if (n == nullptr) return new Node(val);
        
        // redundant
        if (n->v == val) return n;
        
        if (val < n->v) {
            if (found || !isUnbalanced(1 + getWeight(n->l), n->w + 1)) {
                Node *left = insert(n->l, val, found);
                n->update(left, n->r);
                return n;
            } else {
                Node *left = insert(n->l, val, true);
                n->update(left, n->r);
                return rebuild(n); 
            } 
        } else {
            if (found || !isUnbalanced(1 + getWeight(n->r), n->w + 1)) {
                Node *right = insert(n->r, val, found);
                n->update(n->l, right);
                return n;
            } else {
                Node *right = insert(n->r, val, true);
                n->update(n->l, right);
                return rebuild(n);
            } 
        }
    }

    // only for used in the delete function
    static Node* findSuccessor(Node *node) {
        return (node->l == nullptr) ? node : findSuccessor(node->l);
    }

    // the value must be in the tree
    // otherwise isUnbalanced(getWeight(n->l) - 1, n->w - 1) is wrong
    static Node* remove(Node *n, int val, bool found) {
        if (n == nullptr) return nullptr;

        if (val < n->v) {
            if (found || !isUnbalanced(getWeight(n->l) - 1, n->w - 1)) {
                Node *left = remove(n->l, val, found);
                n->update(left, n->r);
                return n;
            } else {
                Node *left = remove(n->l, val, true);
                n->update(left, n->r);
                return rebuild(n);
            }
        } else if (val > n->v) {
            if (found || !isUnbalanced(getWeight(n->r) - 1, n->w - 1)) {
                Node *right = remove(n->r, val, found);
                n->update(n->l, right);
                return n;
            } else {
                Node *right = remove(n->r, val, true);
                n->update(n->l, right);
                return rebuild(n);
            } 
        } else {
            if (n->getNumChild() == 0) {
                return nullptr;
            } else if (n->getNumChild() == 1) {
                return (n->l != nullptr) ? n->l : n->r;
            } else {
                Node *successor = findSuccessor(n->r);
                int succVal = successor->v;
                if (found || !isUnbalanced(getWeight(n->r) - 1, n->w - 1)) {
                    Node *right = remove(n->r, succVal, found);
                    n->v = succVal;
                    n->update(n->l, right);
                    return n;
                } else {
                    Node *right = remove(n->r, succVal, true);
                    n->v = succVal;
                    n->update(n->l, right);
                    return rebuild(n);
                }

            }
        }

    }

    static int getHeight(Node *n) {
        return (n == nullptr) ? 0 : 1 + max(getHeight(n->l), getHeight(n->r));
    }


};

int main() {
    Node *root = nullptr;
    for (int i = 1; i <= 100000; i ++) {
        root = Node::insert(root, i, false);
    }

    for (int i = 1; i <= 100000; i += 2) {
        root = Node::remove(root, i, false);
    }

    cout << Node::getHeight(root) << endl;
    vector<Node*> store;
    
    Node::inorder(root, store);
    for (Node *n : store) {
        cout << n->v << endl;
    }
}