package com.lynch.search.twothreetree;

class DataStore {
    int data;

    public DataStore(int data) {
        this.data = data;
    }

    public void displayData() {
        System.out.println("/" + data);
    }
}

class Node23 {
    private static final int N = 3;
    private Node23[] childArray = new Node23[N];
    private DataStore[] itemArray = new DataStore[N - 1];
    private Node23 parent;
    private int itemNums;

    public void connectChild(int index, Node23 child) {
        childArray[index] = child;
        if (child != null)
            child.parent = this;
    }

    public Node23 disconnectChild(int index) {
        Node23 temp = childArray[index];
        childArray[index] = null;
        return temp;
    }

    public Node23 getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return childArray[0] == null;
    }

    public boolean isFull() {
        return itemNums == N - 1;
    }

    public DataStore getItem(int index) {
        return itemArray[index];
    }

    public Node23 getChild(int index) {
        return childArray[index];
    }

    public int getItemNums() {
        return itemNums;
    }

    public int findItem(int key) {
        for (int i = 0; i < itemNums; i++) {
            if (itemArray[i] == null)
                break;
            else if (key == itemArray[i].data)
                return i;
        }
        return -1;
    }

    public int insertItem(DataStore item) {
        itemNums++;
        int key = item.data;
        for (int i = N - 2; i >= 0; i--) {
            if (itemArray[i] == null)
                continue;
            else {
                int curKey = itemArray[i].data;
                if (key < curKey) {
                    itemArray[i + 1] = itemArray[i];
                } else {
                    itemArray[i + 1] = item;
                    return i + 1;
                }
            }
        }
        itemArray[0] = item;
        return 0;
    }

    public DataStore removeItem() {
        DataStore temp = itemArray[itemNums - 1];
        itemArray[itemNums - 1] = null;
        itemNums--;
        return temp;
    }

    public void displayNode() {
        for (int i = 0; i < itemNums; i++)
            itemArray[i].displayData();
        System.out.println("/");
    }

}

public class Tree23 {
    private Node23 root = new Node23();
    /*
     * public int find(int key) { Node23 curNode = root; int childNum; while
     * (true) { if ((childNum = curNode.findItem(key)) != -1) return
childNum;
     * else if (curNode.isLeaf()) return -1; else curNode =
     * getNextChild(curNode, key); } }
     */

    private Node23 getNextChild(Node23 node, int key) {
        for (int i = 0; i < node.getItemNums(); i++) {
            if (key < node.getItem(i).data)
                return node.getChild(i);
        }
        return node.getChild(node.getItemNums());
    }

    public void insert(int data) {
        DataStore item = new DataStore(data);
        Node23 curNode = root;
        // 先找到插入的位置
        while (true) {
            if (curNode.isLeaf())
                break;
            else
                curNode = getNextChild(curNode, data);
        }
        // 如果到达叶节点且满了
        if (curNode.isFull()) {
            split(curNode, data);
        } else
            curNode.insertItem(item);

    }

    // 思路：在满的叶节点插入新的数据，得到1个父节点和2个子节点组成的一
    //个分支
    // 然后通过递归使这个分支倒推上去
    public void split(Node23 node, int key) {
        Node23 parent = node.getParent();
        Node23 newNode = new Node23();// 叶节点拆开后新的子节点
        Node23 newNode2 = new Node23();// 保存首次分支父节点
        DataStore mid;
        if (key < node.getItem(0).data) {
            newNode.insertItem(node.removeItem());
            mid = node.removeItem();
            node.insertItem(new DataStore(key));
        } else if (key < node.getItem(1).data) {
            newNode.insertItem(node.removeItem());
            mid = new DataStore(key);
        } else {
            newNode.insertItem(new DataStore(key));
            mid = node.removeItem();
        }
        // 第一次根满了的时候，需要手动设置一次，感觉可以移动别处，因为
        //只需要用一次
        if (node == root)
            root = newNode2;
        newNode2.insertItem(mid);
        newNode2.connectChild(0, node);
        newNode2.connectChild(1, newNode);
        connectNode(parent, newNode2);// 开始递归调整
    }

    private void connectNode(Node23 parent, Node23 node) {
        int key = node.getItem(0).data;
        if (node == root) {// 当前节点为根，这样不需要判断更高的parent了
            return;
        }
        if (parent.isFull()) {
            Node23 gparent = parent.getParent();
            Node23 newNode = new Node23();
            Node23 temp1, temp2;
            DataStore item;
            // 当叶节点满了，并且其父节点也满了时，有3种情况
            // 分别对应此叶节点为父节点的第1,2,3个子节点
            if (key < parent.getItem(0).data) {
                temp1 = parent.disconnectChild(1);
                temp2 = parent.disconnectChild(2);
                newNode.connectChild(0, temp1);
                newNode.connectChild(1, temp2);
                newNode.insertItem(parent.removeItem());
                item = parent.removeItem();
                parent.insertItem(item);
                parent.connectChild(0, node);
                parent.connectChild(1, newNode);
            } else if (key < parent.getItem(1).data) {
                temp1 = parent.disconnectChild(0);
                temp2 = parent.disconnectChild(2);
                Node23 n2 = new Node23();
                newNode.insertItem(parent.removeItem());// N
                newNode.connectChild(0, node.disconnectChild(1));
                newNode.connectChild(1, temp2);
                n2.insertItem(parent.removeItem());// M
                n2.connectChild(0, temp1);
                n2.connectChild(1, node.disconnectChild(0));
                parent.insertItem(node.removeItem());
                parent.connectChild(0, n2);
                parent.connectChild(1, newNode);
            } else {
                item = parent.removeItem();
                newNode.insertItem(parent.removeItem());// M
                newNode.connectChild(0, parent.disconnectChild(0));
                newNode.connectChild(1, parent.disconnectChild(1));
                parent.disconnectChild(2);
                parent.insertItem(item);
                parent.connectChild(0, newNode);
                parent.connectChild(1, node);
            }
            // 直接修改了parent，省了个变量。。
            // 新的分支引领节点是parent，gparent为开始时的父节点
            connectNode(gparent, parent);
        }
        // 如果满叶节点的父节点不满，2种情况，无需递归
        else {
            if (key < parent.getItem(0).data) {
                Node23 temp = parent.disconnectChild(1);
                parent.connectChild(0, node.disconnectChild(0));
                parent.connectChild(1, node.disconnectChild(1));
                parent.connectChild(2, temp);
            } else {
                parent.connectChild(1, node.disconnectChild(0));
                parent.connectChild(2, node.disconnectChild(1));
            }
            parent.insertItem(node.getItem(0));
        }
    }

    public void displayTree() {
        fineDisplay(root, 0, 0);
    }

    // 展示23树的结构
    private void fineDisplay(Node23 node, int level, int childNum) {
        System.out.println("level " + level + " child " + childNum);
        node.displayNode();
        for (int i = 0; i < node.getItemNums() + 1; i++) {
            Node23 child = node.getChild(i);
            if (child != null)
                fineDisplay(child, level + 1, i);
            else
                return;
        }

    }

    // 一个一个数地插入进去，把这些数据修改顺序结果好像不变。
    // 没有更细致的检验程序的正确性，看到错误的朋友请指出。
    public static void main(String[] args) {
        Tree23 tree = new Tree23();
        tree.insert(80);
        tree.insert(50);
        tree.insert(40);
        tree.insert(30);
        tree.insert(20);
        tree.insert(70);
        tree.insert(60);
        tree.insert(10);
        tree.displayTree();
    }
}
