package com.lynch.sort.merge;

import java.util.Scanner;

import static com.lynch.sort.primary.BaseSort.isSorted;
import static com.lynch.sort.primary.BaseSort.show;
import static com.lynch.sort.merge.InMerge.merge;

/**
 * 自底向上的归并排序
 * TO(N)=NlogN,SO(N)=N
 * Created by lynch on 2018/9/6. <br>
 **/
public class Down_UPMerge {
    private static Comparable[] aux;

    public static void sort(Comparable[] a) {
        //进行lgN次两两归并
        int N = a.length;
        aux = new Comparable[N];
        for (int sz = 1; sz < N; sz = sz + sz) {//sz数组大小
            for (int lo = 0; lo < N - sz; lo += sz + sz)//lo:子数组索引
                merge(a, aux, lo, lo + sz - 1, Math.min(lo + sz + sz - 1, N - 1));
        }
    }

    public static void main(String[] args) {
        String[] a = new String[10];
        Scanner in = new Scanner(System.in);
        System.out.println("Please input 10 nums:");
        for (int i = 0; i < a.length; i++) {
            String input = in.next();
            a[i] = input;
        }
        sort(a);
        assert isSorted(a);
        show(a);
    }
}
