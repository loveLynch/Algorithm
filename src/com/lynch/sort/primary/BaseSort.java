package com.lynch.sort.primary;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by lynch on 2018/8/7. <br>
 **/
public class BaseSort {


    public static void show(Comparable[] a) {
        //在单行中打印
        System.out.println("after sort:");
        for (int i = 0; i < a.length; i++)
            StdOut.print(a[i] + " ");
        StdOut.println();
    }

    public static boolean isSorted(Comparable[] a) {
        //测试数组元素是否有序
        for (int i = 0; i < a.length; i++) {
            if (less(a[i], a[i - 1]))
                return false;
        }
        return true;
    }

    public static boolean less(Comparable v, Comparable w) {
        boolean compareflag = false;
        if (v != null && w != null && v.compareTo(w) < 0) {
            compareflag = true;
        } else
            compareflag = false;
        return compareflag;
    }

    public static void exch(Comparable a[], int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}
