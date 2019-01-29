package com.lynch.search.twothreetree;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by lynch on 2018/10/6. <br>
 **/
public class TwoThreeTree {
    private Node root;  //root of the tree

    /**
     * create an empty root
     */
    public TwoThreeTree() {
        root = new Node();
    }

    /**
     * insert method
     *
     * @param x
     * @return
     */
    public boolean insert(int x) {
        Node currNode = root.search(x);
        if (!currNode.keys.contains(x)) {
            currNode.insert(x);
            return true;
        }
        return false;
    }

    /**
     * search method
     *
     * @param x
     * @return
     */
    public String search(int x) {
        String str = "";
        Node currNode = root.search(x);
        for (int i = 0; i < currNode.size(); i++) {
            if (i > 0) str += " ";
            str += currNode.keys.get(i);
        }
        return str;
    }

    //set Node to store information
    private class Node implements Comparable<Node> {
        private final int NODE_MAX = 2;
        private Node parent;
        private ArrayList<Integer> keys;
        private ArrayList<Node> children;

        //root node
        public Node() {
            parent = null;
            keys = new ArrayList<>();
            children = new ArrayList<>();
        }

        //child node
        public Node(int key) {
            parent = null;
            keys = new ArrayList<>();
            keys.add(key);
            children = new ArrayList<>();
        }


        public Node search(int key) {
            if (isLeaf()) return this;

            // Check if key value is in the current node
            for (int i = 0; i < size(); i++) {
                int check = keys.get(i);
                if (check == key)
                    return this;
                else if (key < check)
                    return children.get(i).search(key);
            }
            return children.get(children.size() - 1).search(key);
        }


        public void insert(int value) {
            Node currNode = search(value);
            currNode.add(value);
        }


        public void add(int value) {
            keys.add(value);
            Collections.sort(keys);
            if (size() > NODE_MAX)
                split();
        }


        public void split() {
            Node parentNode;
            Node left = new Node(keys.get(0));
            Node right = new Node(keys.get(2));
            int key = keys.get(1);

            if (!isLeaf()) {
                for (int i = 0; i < 2; i++) {
                    left.children.add(children.get(i));
                    children.get(i).parent = left;
                    right.children.add(children.get(i + 2));
                    children.get(i + 2).parent = right;
                }
            }

            if (parent == null) {
                parentNode = this;
                children.clear();
                keys.clear();
            } else {
                parentNode = parent;
                parentNode.children.remove(this);
            }

            parentNode.children.add(left);
            left.parent = parentNode;
            parentNode.children.add(right);
            right.parent = parentNode;
            Collections.sort(parentNode.children);
            parentNode.add(key);
        }

        /**
         * @return Size of keys ArrayList.
         */
        public int size() {
            return keys.size();
        }

        /**
         * Check if has children
         *
         * @return
         */
        public boolean isLeaf() {
            return children.size() == 0;
        }

        /**
         * compare to other node
         *
         * @param other
         * @return
         */
        public int compareTo(Node other) {
            return this.keys.get(0).compareTo(other.keys.get(0));
        }

    }


}