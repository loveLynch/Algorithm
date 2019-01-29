package com.lynch.sort.quicksort;

import java.util.Scanner;

import static com.lynch.sort.primary.BaseSort.exch;
import static com.lynch.sort.primary.BaseSort.isSorted;
import static com.lynch.sort.primary.BaseSort.show;

/**
 * 三向切分的快速排序
 * TO(N)=N~NlogN,SO(N)=lgN
 * 从左到右遍历数组依次，维护一个指针lt使得a[lo...lt-1]中的元素都小于v,一个指针gt
 * 使得a[gt+1...hi]中的元素都大于v，一个指针i使得a[lt...i-1]中的元素都等于v，a[i...gt]中
 * 的元素都还未确定，一开始i和lo相等，对a[i]做如下处理
 * 1）a[i]<v时,exch(a[lt],a[i]),lt++,i++
 * 2)a[i]>v时,exch(a[gt],a[i]),gt--
 * 3)a[i]=v时，i++
 * Created by lynch on 2018/9/6. <br>
 **/
public class Quick3way {
    public static void sort(Comparable[] a) {
        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) return;
        int lt = lo, i = lo + 1, gt = hi;
        Comparable v = a[lo];
        while (i <= gt) {
            int cmp = a[i].compareTo(v);
            if (cmp < 0) exch(a, lt++, i++);
            else if (cmp > 0) exch(a, i, gt--);
            else i++;
        }//现在a[lo...lt-1]<v=a[lt...gt]<a[gt...hi]成立
        sort(a, lo, lt - 1);
        sort(a, gt + 1, hi);
    }

    public static void main(String[] args) {
        String[] a = new String[10];
        Scanner in = new Scanner(System.in);
        System.out.println("Please input ten elements:");
        for (int i = 0; i < a.length; i++) {
            String input = in.next();
            a[i] = input;
        }
        sort(a);
        assert isSorted(a);
        show(a);
    }
}
