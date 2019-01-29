package com.lynch.sort.quicksort;

import java.util.Scanner;

import static com.lynch.sort.primary.BaseSort.*;

/**
 * 快速排序
 * TO(N)=NlogN,SO(N)=lgN
 * 分治的排序算法，将一个数组分成两个子数组，将两部分独立地排序
 * 切分（partition)
 * Created by lynch on 2018/9/6. <br>
 **/
public class Quick {
    public static void sort(Comparable[] a) {
        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) return;
        int j = partition(a, lo, hi);//切分
        sort(a, lo, j - 1);//将左半部分a[lo...j-1]排序
        sort(a, j+1, hi);//将右半部分a[j...hi]排序
    }

    private static int partition(Comparable[] a, int lo, int hi) {
        //将数组切分为a[lo...i-1],a[i],a[i+1...hi]
        int i = lo, j = hi + 1;//左右扫面指针
        Comparable v = a[lo];//切分元素
        while (true) {
            //扫描左右，检查扫面是否结束并交换元素
            while (less(a[++i], v)) if (i == hi) break;
            while (less(v, a[--j])) if (j == lo) break;
            if (i >= j) break;
            exch(a, i, j);

        }
        exch(a, lo, j); //将v=a[j]放入正确的位置
        return j;//a[lo...j-1]<=a[j]<=a[j+1...hi]达成

    }

    public static void main(String[] args) {
        String[] a = new String[10];
        Scanner in = new Scanner(System.in);
        System.out.println("Please input 10 elements:");
        for (int i = 0; i < a.length; i++) {
            String input = in.next();
            a[i] = input;
        }
        sort(a);
        assert isSorted(a);
        show(a);
    }
}
