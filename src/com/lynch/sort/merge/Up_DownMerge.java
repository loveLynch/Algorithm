package com.lynch.sort.merge;

import java.util.Scanner;

import static com.lynch.sort.primary.BaseSort.isSorted;
import static com.lynch.sort.primary.BaseSort.show;
import static com.lynch.sort.merge.InMerge.merge;

/**
 * 自定向下的归并排序
 * Created by lynch on 2018/8/7. <br>
 **/
public class Up_DownMerge {
    private static Comparable[] aux;

    private static void sort(Comparable[] a) {
        aux = new Comparable[a.length];
        sort(a, 0, a.length - 1);

    }

    private static void sort(Comparable[] a, int lo, int hi) {
        //将数组a[lo...hi]排序
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, lo, mid);//将左半边排序
        sort(a, mid + 1, hi);//将右半边排序
        merge(a, aux, lo, mid, hi);

    }

    public static void main(String[] args) {
        String[] a = new String[10];
        Scanner in = new Scanner(System.in);
        System.out.println("Please input 10 characters:");
        for (int i = 0; i < a.length; i++) {
            String input = in.next();
            a[i] = input;
        }
        sort(a);
        assert isSorted(a);
        show(a);
    }
}
