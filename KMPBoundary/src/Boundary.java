public class Boundary {
    public int[] boundary(String s) {
        // the returned array is index 1
        // i.e starting index of string is 1
        // it returns , for each prefix from 1 to i, the length of the longest boundary
        // boundary : longest length of substring, such that it is both a prefix and suffix of string s,
        // and is strictly shorter than s
        int[] out = new int[s.length() + 1];
        out[0] = 0;
        // p stores the length of the longest boundary for prefix of string s, from 1 to i, s is 1 indexed
        int p = 0;
        for (int i = 0; i < s.length(); i ++) {
            if (i == 0) {
                out[i + 1] = 0;
            } else {
                while (p >= 0) {
                    if (p == 0) {
                        if (s.charAt(p) == s.charAt(i)) {
                            out[i + 1] = 1;
                            p ++;
                            break;
                        } else {
                            out[i + 1] = 0;
                            break;
                        }
                    } else if (s.charAt(p) == s.charAt(i)) {
                        out[i + 1] = p + 1;
                        p ++;
                        break;
                    } else {
                        // no match go we go to next boundary which is the max boundary of the boundary
                        p = out[p];
                    }
                }
            }
        }
        return out;
    }

    // return the earliest index of which s2 or entire prefix of s2 appears in s1.
    // for example s1 = aaa
    // s2 == aab
    // return 1
    public int patternMatch(String s1, String s2) {
        int[] bound = boundary(s2);
        int p2 = 0; // pointer for bound, pointing at the last character in s2 we have matched
        int p1 = 0;// pointer for s1;
        while (p1 <= s1.length()) {
            if (p1 == s1.length()) {

                return p1 - p2;
            } else if (p2 == 0) {
                if (s1.charAt(p1) == s2.charAt(0)) {
                    p2 ++;
                    p1 ++;
                } else {
                    p1 ++;
                }
            } else if (p2 == s2.length()) {
                // we have matched s2 completely in s1
                return p1 - p2;
            } else {
                if (s1.charAt(p1) == s2.charAt(p2)) {
                    p1 ++;
                    p2 ++;
                } else {
                    p2 = bound[p2];
                }
            }

        }
        return -1;
    }

}
