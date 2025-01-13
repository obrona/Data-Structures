#include <bits/stdc++.h>
using namespace std;

// use 2d segment tree and binary search. 1 query is O(logn)^3

struct SegTree2d {
    int X, Y; // the dims of arr
    vector<vector<int>> tree;

    SegTree2d() {}

    SegTree2d(int X, int Y): X(X), Y(Y), tree(4 * X, vector(4 * Y, 0)) {}

    SegTree2d(const vector<vector<int>>& arr): X(arr.size()), Y(arr[0].size()), tree(4 * X, vector(4 * Y, 0)) {
        build(0, 0, X - 1, arr);
    }

    void merge(int p, int p1, int p2, int py, int sy, int ey) {
        tree[p][py] = tree[p1][py] + tree[p2][py];

        if (sy != ey) {
            int m = (sy + ey) >> 1;
            merge(p, p1, p2, (py << 1) + 1, sy, m);
            merge(p, p1, p2, (py << 1) + 2, m + 1, ey);
        }
    }

    // x: what row in arr we are using
    void buildRow(int px, int x, int py, int sy, int ey, const vector<vector<int>>& arr) {
            if (sy == ey) {
                tree[px][py] = arr[x][sy];
                return;
            }

            int m = (sy + ey) >> 1;
            buildRow(px, x, (py << 1) + 1, sy, m, arr);
            buildRow(px, x, (py << 1) + 2, m + 1, ey, arr);
            tree[px][py] = tree[px][(py << 1) + 1] + tree[px][(py << 1) + 2];
    }

    void build(int px, int sx, int ex, const vector<vector<int>>& arr) {
        if (sx == ex) {
            buildRow(px, sx, 0, 0, Y - 1, arr);
            return;
        }

        int m = (sx + ex) >> 1;
        build((px << 1) + 1, sx, m, arr);
        build((px << 1) + 2, m + 1, ex, arr);
        merge(px, (px << 1) + 1, (px << 1) + 2, 0, 0, Y - 1);
    }

    // y: y-coor of insert
    void mergeUpdate(int p, int p1, int p2, int py, int sy, int ey, int y) {
        tree[p][py] = tree[p1][py] + tree[p2][py];
        if (sy == ey) return;
        
        int m = (sy + ey) >> 1;
        if (y <= m) {
            mergeUpdate(p, p1, p2, (py << 1) + 1, sy, m, y);
        } else {
            mergeUpdate(p, p1, p2, (py << 1) + 2, m + 1, ey, y);
        }
    }

    void updateRow(int px, int py, int sy, int ey, int y, int val) {
        if (sy == ey) {
            tree[px][py] = val;
            return;
        }

        int m = (sy + ey) >> 1;
        if (y <= m) {
            updateRow(px, (py << 1) + 1, sy, m, y, val);
        } else {
            updateRow(px, (py << 1) + 2, m + 1, ey, y, val);
        }
        tree[px][py] = tree[px][(py << 1) + 1] + tree[px][(py << 1) + 2];
    }

    void update(int px, int sx, int ex, int x, int y, int val) {
        if (sx == ex) {
            updateRow(px, 0, 0, Y - 1, y, val);
            return;
        }

        int m = (sx + ex) >> 1;
        if (x <= m) {
            update((px << 1) + 1, sx, m, x, y, val);
        } else {
            update((px << 1) + 2, m + 1, ex, x, y, val);
        }
        mergeUpdate(px, (px << 1) + 1, (px << 1) + 2, 0, 0, Y - 1, y);
    }

    int queryY(int px, int py, int sy, int ey, int y1, int y2) {
        if (sy == ey || (sy == y1 && ey == y2)) {
            return tree[px][py];
        }

        int m = (sy + ey) >> 1;
        if (y2 <= m) {
            return queryY(px, (py << 1) + 1, sy, m, y1, y2);
        } else if (y1 > m) {
            return queryY(px, (py << 1) + 2, m + 1, ey, y1, y2);
        } else {
            return queryY(px, (py << 1) + 1, sy, m, y1, m) + queryY(px, (py << 1) + 2, m + 1, ey, m + 1, y2);
        }
    }

    // (x1, y1): top left of query box
    // (x2, y2): bottom right of query box
    int query(int px, int sx, int ex, int x1, int y1, int x2, int y2) {
        if (sx == ex || (sx == x1 && ex == x2)) {
            return queryY(px, 0, 0, Y - 1, y1, y2);
        }

        int m = (sx + ex) >> 1;
        if (x2 <= m) {
            return query((px << 1) + 1, sx, m, x1, y1, x2, y2);
        } else if (x1 > m) {
            return query((px << 1) + 2, m + 1, ex, x1, y1, x2, y2);
        } else {
            return query((px << 1) + 1, sx, m, x1, y1, m, y2) + query((px << 1) + 2, m + 1, ex, m + 1, y1, x2, y2);
        }
    }

};

enum Coor {
    ROW, COL
};

int X, Y, G;
vector<vector<int>> grid;
vector<set<int>> rowSets;
vector<set<int>> colSets;

SegTree2d st;

int dist(int x1, int y1, int x2, int y2) {
    int dx = abs(x1 - x2);
    int dy = abs(y1 - y2);
    return dx * dx + dy * dy;
}


int getClosestElem(set<int>& s, int x) {
    if (s.size() == 0) return -1;
    
    auto ub = s.lower_bound(x);
    auto lb = (ub == s.begin()) ? s.end() : prev(ub, 1);
    
    if (ub == s.end()) {
        return *lb;
    } else if (lb == s.end()) {
        return *ub;
    } else if (abs(x - *lb) < abs(*ub - x)) {
        return *lb;
    } else {
        return *ub;
    }
}

int getClosestDist(int x, int y) {
    int s = 0;
    int e = max(X - 1, Y - 1);

    while (s < e) {
        int m = (s + e) >> 1;
        int x1 = max(0, x - m), y1 = max(0, y - m);
        int x2 = min(X - 1, x + m), y2 = min(Y - 1, y + m);
        int numPoints = st.query(0, 0, st.X - 1, x1, y1, x2, y2);

        if (numPoints > 0) {
            e = m;
        } else {
            s = m + 1;
        }
    }
    if (x == 12 && y == 4) cout << "hello" << s << endl;

    int topRow = max(0, x - s), bottomRow = min(X - 1, x + s);
    int leftCol = max(0, y - s), rightCol = min(Y - 1, y + s);

    int x1, y1, d;
    int best = 1e7;

    x1 = topRow, y1 = getClosestElem(rowSets[topRow], y);
    if (y1 != -1) {
        d = dist(x1, y1, x, y);
        best = min(d, best);
        if (x == 12 && y == 4) {
            cout << x1 << " " << y1 << " " << d << endl;
        }
        
    }

    x1 = bottomRow, y1 = getClosestElem(rowSets[bottomRow], y);
    if (y1 != -1) {
        d = dist(x1, y1, x, y);
        best = min(best, d);
        if (x == 12 && y == 4) {
            cout << x1 << " " << y1 << " " << d << endl;
        }
        
    }

    x1 = getClosestElem(colSets[leftCol], x), y1 = leftCol;
    if (x1 != -1) {
        d = dist(x1, y1, x, y);
        best = min(best, d);
        if (x == 12 && y == 4) {
            cout << x1 << " " << y1 << " " << d << endl;
        }
        
    }

    x1 = getClosestElem(colSets[rightCol], x), y1 = rightCol;
    if (x1 != -1) {
        d = dist(x1, y1, x, y);
        best = min(best, d);
        if (x == 12 && y == 4) {
            cout << x1 << " " << y1 << " " << d << endl;
        }
        
    }

    st.update(0, 0, st.X - 1, x, y, 1);
    rowSets[x].insert(y);
    colSets[y].insert(x);

    return best;
    


}



/*int main() {
    vector<vector<int>> arr = {{1,2,3,4},
                               {2,3,4,5},
                               {3,4,5,6}};
    SegTree2d st(arr);
    
    cout << st.query(0, 0, st.X - 1, 1, 1, 1, 3) << endl;
    st.update(0, 0, st.X - 1, 1, 1, 100);
    cout << st.query(0, 0, st.X - 1, 1, 1, 1, 3) << endl;
    st.update(0, 0, st.X - 1, 1, 1, 10);
    cout << st.query(0, 0, st.X - 1, 0, 0, 1, 1);
   
    


}*/

/*int main() {
    set<int> s = {3, 6, 100};
    cout << getClosestElem(s, -100) << endl;

}*/

int main() {
    ios_base::sync_with_stdio(0);
    cin.tie(0);

    cin >> X >> Y;
    grid = vector(X, vector(Y, 0));
    rowSets = vector(X, set<int>());
    colSets = vector(Y, set<int>());
    

    for (int r = 0; r < X; r ++) {
        for (int c = 0; c < Y; c ++) {
            char a;
            cin >> a;
            if (a == 'x') {
                grid[r][c] = 1;
                rowSets[r].insert(c);
                colSets[c].insert(r);
            }

        }
    }

    //cout << rowSets[0].size() << endl;

    st = SegTree2d(grid);

    cin >> G;
    for (int i = 0; i < G; i ++) {
        int x, y;
        cin >> x >> y;
        x --; y --;
        
        int d = getClosestDist(x, y);
        cout << d << '\n';
    }
}