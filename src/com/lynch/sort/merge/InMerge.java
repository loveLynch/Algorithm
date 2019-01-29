package com.lynch.sort.merge;

import static com.lynch.sort.primary.BaseSort.less;

/**
 * Created by lynch on 2018/8/7. <br>
 * 原地归并排序
 **/
public class InMerge {
    // private static Comparable[] aux;

    public static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        //将a[lo...mid]和a[mid+1...hi]归并
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            //将a[lo...hi]复制到aux[lo...hi]
            aux[k] = a[k];
        }
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }
}
