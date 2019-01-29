package com.lynch.sort.primary;

import java.util.Scanner;

import static com.lynch.sort.primary.BaseSort.*;

/**
 * Created by lynch on 2018/8/7. <br>
 * 希尔排序
 * TO(N)=NlogN?|N^6/5,SO(N)=1
 * 又叫缩小增量排序，基本思想是使数组中任意间隔h的元素都是有序的，这样的数组被称为h有序数组。
 * 希尔排序是把记录按下标的一定增量分组，对每组使用直接插入排序算法排序；
 * 随着增量逐渐减少，每组包含的关键词越来越多，当增量减至1时，整个文件恰被分成一组，算法便终止
 **/
public class Shell {
    public static void main(String[] args) {
        String[] a = new String[10];
        Scanner in = new Scanner(System.in);
        System.out.println("Please input 10 numbers:");
        for (int i = 0; i < a.length; i++) {
            String input = in.next();
            a[i] = input;
        }
        sort(a);
        assert isSorted(a);
        show(a);
    }

    public static void sort(Comparable[] a) {
        //将a[]按升序排列
        int N = a.length;
        int h = 1;
        while (h < N / 3)
            h = 3 * h + 1;//1,4,13,40,121,364,1093,...增量分组
        while (h >= 1) {
            //将数组变为h排序
            for (int i = h; i < N; i++) {
                //将a[i]插入到a[i-h],a[i-2*h],a[i-3*h],...
                for (int j = i; j >= h && less(a[j], a[j - h]); j -= h)
                    exch(a, j, j - h);
            }
            h = h / 3;
        }

    }
}
