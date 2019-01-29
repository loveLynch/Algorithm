package com.lynch.sort.priorityqueue;

import java.util.Scanner;

import static com.lynch.sort.primary.BaseSort.isSorted;
import static com.lynch.sort.primary.BaseSort.show;

/**
 * 堆排序
 * TO(N)=NlogN,SO(N)=1
 * 1.堆的构造阶段
 * 2.下沉排序阶段
 * Created by lynch on 2018/9/7. <br>
 **/
public class HeapSort {

    public static void sort(Comparable[] a) {
        int N = a.length;
        for (int k = N / 2; k >= 1; k--)
            sink(a, k, N);
        while (N > 1) {
            exch(a, 1, N--);
            sink(a, 1, N);
        }


    }

    //由上至下的堆有序化（下沉）的实现
    public static void sink(Comparable[] a, int k, int N) {
        while (2 * k <= N) {
            int j = 2 * k;
            if (j < N && less(a, j, j + 1)) j++;
            if (!less(a, k, j)) break;
            exch(a, k, j);
            k = j;
        }
    }

    private static boolean less(Comparable[] a, int i, int j) {
        return a[i - 1].compareTo(a[j - 1]) < 0;
    }

    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i - 1];
        a[i - 1] = a[j - 1];
        a[j - 1] = swap;
    }


    public static void main(String[] args) {
        String[] a = new String[10];
        Scanner in = new Scanner(System.in);
        System.out.println("Please input ten letters:");
        for (int i = 0; i < a.length; i++) {
            String input = in.next();
            a[i] = input;
        }
        sort(a);
        assert isSorted(a);
        show(a);
    }
}
