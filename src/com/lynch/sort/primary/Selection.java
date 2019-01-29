package com.lynch.sort.primary;


import java.util.Scanner;

import static com.lynch.sort.primary.BaseSort.*;

/**
 * Created by lynch on 2018/8/7. <br>
 * 选择排序
 * TO(N)=N^2,SO(N)=1
 * 找出一组数据中的最小值放入最前（升序）或最后（降序），将剩下的数据再选出最小值放入前一个最小值之前（降序）或之后（升序）
 * 依次进行，直到遍历完所有数据
 * 对于长度为N的数组，选择排序的大约N^2/2次比较和N次交换
 **/
public class Selection {
    public static void main(String[] args) {
        String[] a = new String[10];
        Scanner in = new Scanner(System.in);
        System.out.println("Please input 10 letters:");
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
        int N = a.length;//数组长度
        for (int i = 0; i < N; i++) {
            //将a[i]和a[i+1..N]中最小的元素交换
            int min = i;//最小元素索引
            for (int j = i + 1; j < N; j++)
                if (less(a[j], a[min]))
                    min = j;
            exch(a, i, min);
        }

    }
}
