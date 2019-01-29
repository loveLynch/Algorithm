package com.lynch.search.binarytree;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Scanner;


/**
 * Created by lynch on 2018/9/26. <br>
 **/
public class BST<Key extends Comparable<Key>, Value> {
    private Node root; //二叉查找树的根结点

    private class Node {
        private Key key; //键
        private Value value; //值
        private Node left, right; //指向子树的链接
        private int N; //以该结点为根的子树中的结点总数

        public Node(Key key, Value value, int N) {
            this.key = key;
            this.value = value;
            this.N = N;

        }

    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else
            return x.N;
    }

    //查找与排序
    public Value get(Key key) {
        return get(root, key);

    }

    private Value get(Node x, Key key) {
        //如果x结点为根结点的子树中查找并返回key所对应的值；
        //如果查不到则返回null
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.value;
    }

    public void put(Key key, Value value) {
        //查找key,找到则更新它的值，否则为它创建一个新结点
        root = put(root, key, value);

    }

    private Node put(Node x, Key key, Value value) {
        //如果key存在于以x为根结点的子树中则更新它的值
        //否则将以key和value为键值对的新结点插入到该子树中
        if (x == null) return new Node(key, value, 1);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, value);
        else if (cmp > 0) x.right = put(x.right, key, value);
        else x.value = value;
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    //最小键
    public Key min() {
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        return min(x.left);
    }

    //小于或等于key的最大键
    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null) return null;
        return x.key;

    }

    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return floor(x.left, key);
        Node t = floor(x.right, key);
        if (t != null) return t;
        else return x;

    }

    //最大键
    public Key max() {
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        return max(x.right);
    }

    //大于或等于key的最小键
    public Key ceiling(Key key) {
        Node x = ceiling(root, key);
        if (x == null) return null;
        return x.key;

    }

    private Node ceiling(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp > 0) return ceiling(x.right, key);
        Node t = ceiling(x.left, key);
        if (t != null) return t;
        else return x;

    }

    //排名
    public Key select(int k) {
        return select(root, k).key;
    }

    private Node select(Node x, int k) {
        //返回排名为k的结点
        if (x == null) return null;
        int t = size(x.left);
        if (t > k) return select(x.left, k);
        else if (t < k) return select(x.right, k - t - 1);
        else return x;
    }

    public int rank(Key key) {
        return rank(key, root);
    }

    private int rank(Key key, Node x) {
        //返回以x为结点的子树中小于x.key的键的数量
        if (x == null) return 0;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return rank(key, x.left);
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
        else return size(x.left);
    }

    //删除最小键
    public void deleteMin() {
        root = deleteMin(root);
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    //删除最大键
    public void deleteMax() {
        root = deleteMax(root);
    }

    private Node deleteMax(Node x) {
        if (x.right == null) return x.left;
        x.right = deleteMin(x.right);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    //删除
    public void delete(Key key) {
        root = delete(root, key);
    }

    private Node delete(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
            //删除结点有左右两个结点
        else {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            Node t = x; //将要被删除的结点用t代替
            x = min(t.right); //此时x更新为t的最小右结点
            x.right = deleteMin(t.right); //新的x右结点为将要删除的t的右最小结点
            x.left = t.left; //新的x左结点就是t的左结点
        }
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    //范围查找
    public Queue<Key> keys() {
        return keys(min(), max());
    }

    public Queue<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new Queue<Key>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key);
        if (cmphi > 0) keys(x.right, queue, lo, hi);
    }

    //按级别顺序返回BST中的键
    public Iterable<Key> levelOrder() {
        Queue<Key> keys = new Queue<Key>();
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x.key);
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }
        return keys;
    }


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Input n:");//数组长
        int n = in.nextInt();
        String[] a = new String[n];
        System.out.println("Please input " + n + " letters:");
        BST<String, Integer> st = new BST<String, Integer>();

        for (int i = 0; i < a.length; i++) {
            String input = in.next();
            a[i] = input;
            st.put(a[i], i);//关联型数组，意味着每个键的值取决于最近一次put()方法的调用
            //如 q a z w a x ...会显示a的值是4
        }
        //级别排序
        for (String s : st.levelOrder())
            StdOut.println(s + " " + st.get(s));

        StdOut.println();
        //键排序
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
    }

}
