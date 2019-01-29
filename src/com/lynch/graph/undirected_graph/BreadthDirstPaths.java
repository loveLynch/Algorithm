package com.lynch.graph.undirected_graph;


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Scanner;
import java.util.Stack;

/**
 * BFS
 * 广度优先搜索
 * Created by lynch on 2018/10/30. <br>
 **/
public class BreadthDirstPaths {
    private boolean[] marked;//到达该顶点的最短路径已知吗？
    private int[] edgeTo;//到达该顶点的已知路径上的最后一个顶点
    private final int s;//起点

    public BreadthDirstPaths(Graph G, int s) {
        this.marked = new boolean[G.V()];
        this.edgeTo = new int[G.V()];
        this.s = s;
        bfs(G, s);
    }

    private void bfs(Graph G, int s) {
        Queue<Integer> queue = new Queue<>();
        marked[s] = true;//标记起点
        queue.enqueue(s);//将它加入队列
        while (!queue.isEmpty()) {
            int v = queue.dequeue();//从队列中删除去下一个顶点
            for (int w : G.adj(v))
                if (!marked[w])//对于每个未被标记的相邻顶点
                {
                    edgeTo[w] = v;//保存最短路径的最后一条边
                    marked[w] = true;//标记它，因为最短路径已知
                    queue.enqueue(w);//并将它添加到队列中
                }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != s; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(s);
        return path;
    }

    public static void main(String[] args) {
        String file = "algs4-data/tinyCG.txt";
        In in = new In(file);
        Scanner input = new Scanner(System.in);
        Graph G = new Graph(in);
        System.out.print("input s:");
        int s = input.nextInt();
        BreadthDirstPaths bfs = new BreadthDirstPaths(G, s);
        for (int v = 0; v < G.V(); v++) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%d to %d:  ", s, v);
                for (int x : bfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else StdOut.print(x + "-");
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d:  not connected\n", s, v);
            }

        }
    }
}
