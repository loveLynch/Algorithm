package com.lynch.sort.priorityqueue;

import edu.princeton.cs.algs4.*;

import java.io.*;
import java.util.Scanner;

/**
 * 优先队列
 * 找出输入流中M个最大元素
 * Created by lynch on 2018/9/6. <br>
 **/
public class TopM {
    private TopM() {
    }

    public static void main(String[] args) throws IOException {
        //打印输入流最大的M行
        Scanner in = new Scanner(System.in);
        System.out.print("Input M:");
        int M = in.nextInt();
        System.out.println("Start Read txt File!");
        //获取读取流
        FileInputStream fis = new FileInputStream("algs4-data/tinyBatch.txt");
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        //FileReader reader = new FileReader("algs4-data/tinyatch.txt");
        //BufferedReader br = new BufferedReader(reader);
        MinPQ<Transaction> pq = new MinPQ<Transaction>(M + 1);
        String line = "";
        while ((line = br.readLine()) != null) {
            //为下一行输入创建一个元素并放入优先队列中
            Transaction transaction = new Transaction(line);
            pq.insert(transaction);
            if (pq.size() > M)
                pq.delMin();//如果优先队列中存在M+1个元素则删除其中最小的元素
        }//最大的M个元素都在优先队列中
        Stack<Transaction> stack = new Stack<Transaction>();
        while (!pq.isEmpty())
            stack.push(pq.delMin());
        for (Transaction t : stack)
            StdOut.println(t);

        //关闭读取流
        br.close();
        isr.close();
        fis.close();
    }

}

