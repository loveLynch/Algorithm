package com.lynch.search.hashlist;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Scanner;

/**
 * Created by lynch on 2018/10/15. <br>
 * 基于线性探测的散列表
 **/
public class LinearProbingHashST<Key, Value> {
    private int N;//符号表中键值对的总数
    private int M = 16;//线性探测表的大小
    private Key[] keys;//键
    private Value[] values;//值

    public LinearProbingHashST() {
        keys = (Key[]) new Object[M];
        values = (Value[]) new Object[M];
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    private void resize(int capacity) {
        LinearProbingHashST<Key, Value> temp = new LinearProbingHashST<Key, Value>();
        for (int i = 0; i < M; i++) {
            if (keys[i] != null) {
                temp.put(keys[i], values[i]);
            }
        }
        keys = temp.keys;
        values = temp.values;
        M = temp.M;
    }

    public void put(Key key, Value value) {
        if (N >= M / 2) resize(2 * M); //将M加倍
        int i;
        for (i = hash(key); keys[i] != null; i = (i + 1) % M) {
            if (keys[i].equals(key)) {
                values[i] = value;
                return;
            }
        }
        keys[i] = key;
        values[i] = value;
        N++;
    }

    public Value get(Key key) {
        for (int i = hash(key); keys[i] != null; i = (i + 1) % M) {
            if (keys[i].equals(key))
                return values[i];
        }

        return null;
    }

    //删除
    public void delete(Key key) {
        if (!contains(key))
            return;
        int i = hash(key);
        while (!key.equals(keys[i])) {
            i = (i + 1) % M;
        }
        keys[i] = null;
        values[i] = null;
        i = (i + 1) % M;
        while (keys[i] != null) {
            Key keyToRedo = keys[i];
            Value valueToRedo = values[i];
            keys[i] = null;
            values[i] = null;
            N--;
            put(keyToRedo, valueToRedo);
            i = (i + 1) % M;
        }
        N--;
        if (N > 0 && N == M / 8)
            resize(M / 2);
    }

    private boolean contains(Key key) {
        return get(key) != null;
    }

    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < M; i++)
            if (keys[i] != null) queue.enqueue(keys[i]);
        return queue;
    }

    public static void main(String[] args) {
        LinearProbingHashST<String, Integer> st = new LinearProbingHashST<String, Integer>();
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
