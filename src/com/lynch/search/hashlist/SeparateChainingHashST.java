package com.lynch.search.hashlist;

import com.lynch.search.symbol_table.SequentialSearchST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Scanner;

/**
 * Created by lynch on 2018/10/15. <br>
 * 基于拉链的散列表
 **/
public class SeparateChainingHashST<Key, Value> {
    private int N; //键值对总数
    private int M; //散列表大小
    private SequentialSearchST<Key, Value>[] st; //存放链表对象的数组

    public SeparateChainingHashST() {
        this(997);
    }

    public SeparateChainingHashST(int M) {
        this.M = M;
        st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[M];
        for (int i = 0; i < M; i++)
            st[i] = new SequentialSearchST<Key, Value>();
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;//将一个32整数变为一个31为非负整数，求散列值
    }

    public Value get(Key key) {
        return st[hash(key)].get(key);
    }

    public void put(Key key, Value value) {
        st[hash(key)].put(key, value);
    }

    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < M; i++) {
            for (Key key : st[i].keys())
                queue.enqueue(key);
        }
        return queue;
    }

    public static void main(String[] args) {
        SeparateChainingHashST<String, Integer> st = new SeparateChainingHashST<String, Integer>();
        Scanner in = new Scanner(System.in);
        System.out.print("Input n:");//数组长
        int n = in.nextInt();
        String[] a = new String[13];
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
