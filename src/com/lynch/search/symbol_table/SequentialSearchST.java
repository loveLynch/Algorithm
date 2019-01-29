package com.lynch.search.symbol_table;


import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.io.*;
import java.util.Scanner;

/**
 * 顺序查找（基于无序链表）
 * Created by lynch on 2018/9/14. <br>
 **/
public class SequentialSearchST<Key, Value> {
    public SequentialSearchST() {
    }

    private int n;
    private Node first; //链表首结点

    private class Node {
        //链表结点的定义
        Key key;
        Value value;
        Node next;

        public Node(Key key, Value value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }


    public Value get(Key key) {
        //查找给定的键，返回相关的值
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key))
                return x.value;//命中
        }
        return null;//未命中
    }

    public void put(Key key, Value value) {
        //查找给定的键，找到则更新其值，否则在表中新建结点
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                x.value = value;
                return;//命中，更新
            }
        }
        first = new Node(key, value, first);//未命中，新建结点
    }


    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        for (Node x = first; x != null; x = x.next)
            queue.enqueue(x.key);
        return queue;
    }


    public static void main(String[] args) throws IOException {
        SequentialSearchST<String, Integer> st = new SequentialSearchST<String, Integer>();
        Scanner in = new Scanner(System.in);
        System.out.print("Input n:");//数组长
        int n = in.nextInt();
        String[] a = new String[n];
        System.out.println("Please input " + n + " letters:");
        for (int i = 0; i < a.length; i++) {
            String input = in.next();
            a[i] = input;

            st.put(a[i], i);//关联型数组，意味着每个键的值取决于最近一次put()方法的调用
            //如 q a z w a x ...会显示a的值是4
        }
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));

    }

}
