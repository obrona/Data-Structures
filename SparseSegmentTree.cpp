#include <bits/stdc++.h>
using ll = long long;
using namespace std;

struct Node {
    int l = 0, r = 0;
    ll sum = 0;

    Node() {}
    
    Node(int val): sum(val) {}

    Node(int l, int r, int sum): l(l), r(r), sum(sum) {}
};


// sparse segment tree, so imageing your array is 10^9, but you only have 10^5 updates
// as always, if curr == 0, it is the null node
class SST {
public:
    int sz;
    int CURR = 0;
    vector<Node> tree;

    SST(int sz): sz(sz), tree(1, Node()) {}

    // return the idx of the node updated or created
    int addVal(int curr, int s, int e, int idx, int val) {
        if (s == e) {
            if (curr == 0) {
                Node node(val);
                tree.push_back(node);
                return tree.size() - 1;
            } else {
                tree[curr].sum += val;
                return curr;
            }
        }

        int m = s + ((e - s) >> 1);
        int l, r;
        if (idx <= m) {
            l = addVal(tree[curr].l, s, m, idx, val);
            r = tree[curr].r;
        } else {
            l = tree[curr].l;
            r = addVal(tree[curr].r, m + 1, e, idx, val);
        }

        if (curr == 0) {
            Node node(l, r, tree[l].sum + tree[r].sum);
            tree.push_back(node);
            return tree.size() - 1;
        } else {
            tree[curr].l = l;
            tree[curr].r = r;
            tree[curr].sum = tree[l].sum + tree[r].sum;
            return curr;
        }
    }

    // need to update the 'root'
    void add(int idx, int val) {
        CURR = addVal(CURR, 0, sz - 1, idx, val);
        //cout << CURR << endl;
    }

    ll rangeSum(int curr, int s, int e, int l, int r) {
        if (s == e || (s == l && e == r)) {
            return tree[curr].sum;
        }

        int m = s + ((e - s) >> 1);
        if (r <= m) {
            return rangeSum(tree[curr].l, s, m, l, r);
        } else if (l > m) {
            return rangeSum(tree[curr].r, m + 1, e, l, r);
        } else {
            return rangeSum(tree[curr].l, s, m, l, m) + rangeSum(tree[curr].r, m + 1, e, m + 1, r);
        }
    }
};


void test(int s, int e) {
    unordered_map<int, int> updates;
    SST sst(1e9);

    for (int i = 0; i < 1e5; i ++) {
        updates[i * 5] = 1;
        sst.add(i * 5, 1);
    }

    ll res = sst.rangeSum(sst.CURR, 0, sst.sz - 1, s, e);
    ll ans = 0;
    for (const auto& [key, value] : updates) {
        if (s <= key && key <= e) ans += value;
    }

    if (res != ans) cout << "fail" << endl;

}

int main() {
    test(1e3 + 91732, 1e5 + 378);
}


