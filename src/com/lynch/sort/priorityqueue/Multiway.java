package com.lynch.sort.priorityqueue;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.io.*;
import java.util.Arrays;

/**
 * 使用优先队列的多向归并
 * 将M个输入流归并为一个有序的输出流
 * Created by lynch on 2018/9/7. <br>
 **/
public class Multiway {
    public static void merge(In[] streams) {
        int N = streams.length;
        IndexMinPQ<String> pq = new IndexMinPQ<String>(N);
        for (int i = 0; i < N; i++) {
            if (!streams[i].isEmpty()) {
                pq.insert(i, streams[i].readString());
            }
        }
        while (!pq.isEmpty()) {
            StdOut.print(pq.minKey() + " ");
            int i = pq.delMin();
            if (!streams[i].isEmpty())
                pq.insert(i, streams[i].readString());
        }
    }

    public static void main(String[] args) throws IOException {
        //打印输入流最大的M行
//        Scanner in = new Scanner(System.in);
//        System.out.print("Input N:");
//        int N = in.nextInt();
        String path = "/Users/lynch/Documents/Idea/AlgorithmsTest/src/com/lynch/sort/priorityqueue/file/";
        File file = new File(path);
        File[] files = file.listFiles();
        Arrays.sort(files);
        int N = files.length;
        System.out.println("文件数N=" + N);
        In[] streams = new In[N];
        for (int i = 0; i < N; i++) {
            if (files[i].isFile()) {
//               InputStreamReader reader = new InputStreamReader(new FileInputStream(files[i]));
//                BufferedReader br = new BufferedReader(reader);
//                String line = "";
//                line = br.readLine();
//                while (line != null) {
                streams[i] = new In(files[i]);
            }
        }
        merge(streams);
    }

}
