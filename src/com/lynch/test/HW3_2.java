package com.lynch.test;

import java.util.*;


public class HW3_2 {
    public static void main(String[] args) {
        /*
         * Given two arrays a[] and b[], containing M distinct integers and N distinct
         * keys, respectively, (with N ≥ M), write a Java method to determine how many
         * keys are in common between the two arrays. The running time of your algorithm
         * should be proportional to N log M in the worst case and use at most a
         * constant amount of extra memory.
         */

        final int N = 25, M = 10;
        Integer[] a = new Integer[N];
        Integer[] b = new Integer[M];

        for (int i = 0; i < N; i++)
            a[i] = 5 - i;

        for (int i = 0; i < M; i++)
            b[i] = i + 1;

        System.out.println(Arrays.toString(a));
        System.out.println(Arrays.toString(b));

        System.out.println("Values in Common: " + valuesInCommon(a, b));
    }

    //Time complexity: O(n)
    private static int valuesInCommon(Integer[] a, Integer[] b) {
        Set<Integer> N = new HashSet<>();
        Set<Integer> M = new HashSet<>();
        for (int i = 0; i < a.length; i++) {
            N.add(a[i]);
        }
        for (int i = 0; i < b.length; i++) {
            if (N.contains(b[i])) {
                M.add(b[i]);
            }
        }
        return M.size();
    }

    /*
     * 将两个数组排好序，之后设置两个数组的下标i=0,j = 0,从头到尾扫描两个数组中数是否相同，
     * 如果不同，较小的数靠后移位，如果相同，则两个下标同时后移，直到其中一个下标到了末尾，
     * 则结束。时间复杂度为O(nlogn),空间复杂度O(1)
     * */
    private static int valuesInCommon1(Integer[] a, Integer[] b) {
        Set<Integer> set = new HashSet<>();
        Arrays.sort(a);
        Arrays.sort(b);
        int i = 0;
        int j = 0;
        while (i < a.length && j < b.length) {
            if (a[i] < b[j]) {
                i++;
            } else if (a[i] > b[j]) {
                j++;
            } else {
                set.add(a[i]);
                i++;
                j++;
            }
        }
        return set.size();
    }

}