package com.lynch.search.symbol_table;

import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.Scanner;

/**
 * 有序符号表
 * 行为测试用例
 * 详细查看ST符号表的API
 * Created by lynch on 2018/9/14. <br>
 **/
public class BehaviorTest {
    public static void main(String[] args) {
        ST<String, Integer> st;
        st = new ST<String, Integer>();
        String[] a = new String[10];
        Scanner in = new Scanner(System.in);
        System.out.println("Please input 10 letters:");
        for (int i = 0; i < a.length; i++) {
            String input = in.next();
            a[i] = input;

            st.put(a[i], i);//关联型数组，意味着每个键的值取决于最近一次put()方法的调用
                            //如 q a z w a x ...会显示a的值是4
        }

        for (String s : st.keys()) {
            StdOut.println(s + " " + st.get(s));
        }


    }
}
