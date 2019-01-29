package com.lynch.sort.primary;

import java.util.Scanner;

import static com.lynch.sort.primary.BaseSort.*;

/**
 * Created by lynch on 2018/8/7. <br>
 * 插入排序
 * TO(N)=N~N^2,SO(N)=1
 * 对于一组数据，选择第二个元素与第一个比较，将小的放其前面（升序）或者后面（降序）,
 * 依次选择上一个元素的后一个依次与前面以排列的数据比较，插入到合适的位置
 **/
public class Insertion {
    public static void main(String[] args) {
        String[] a = new String[10];
        Scanner in = new Scanner(System.in);
        System.out.println("Please input 10 numbers or letters:");
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
            //将a[i]插入到a[i-1],a[i-2],a[a-3]...之中
            for (int j = i; j > 0 && less(a[j], a[j - 1]); j--)
                exch(a, j, j - 1);


        }
    }
}
