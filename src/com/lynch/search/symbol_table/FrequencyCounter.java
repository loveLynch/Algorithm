package com.lynch.search.symbol_table;

import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.io.*;
import java.util.Scanner;

/**
 * 有序符号表
 * 性能测试
 * 输入最小键长（单词的至少长度）,执行后将会统计某个出现频率最高且满足最小键长的单词。
 * 可能某几个单词同时满足最小键长且出现频率最高且相同，但具体打印出哪个取决于符号表的具体实现
 * Created by lynch on 2018/9/14. <br>
 **/
public class FrequencyCounter {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.print("Input minlen:");//最小键长
        int minlen = in.nextInt();
        System.out.println("Start Read txt File!");
        //获取读取流
        //tinyTale.txt
        FileInputStream fis = new FileInputStream("algs4-data/tinyTale.txt");
        //tale.txt
//        FileInputStream fis = new FileInputStream("algs4-data/tale.txt");
        //leipzig1M.txt
//        FileInputStream fis = new FileInputStream("algs4-data/leipzig1M.txt");
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        ST<String, Integer> st = new ST<String, Integer>();
        String line = "";
        while ((line = br.readLine()) != null) {
            //构造符号表并统计频率
            String[] wordArray = line.split(" ");
            for (String lineword : wordArray) {
                //   System.out.println(lineword);
                String word = lineword.trim();
                if (word.length() < minlen) continue;//忽略较短的单词
                if (!st.contains(word))//键word是否存在表中
                    st.put(word, 1);
                else st.put(word, st.get(word) + 1);
            }
        }

        //找出出现频率最高的单词
        String max = "";
        st.put(max, 0);
        for (String word : st.keys())
            if (st.get(word) > st.get(max))
                max = word;
        StdOut.println(max + " " + st.get(max));
        //关闭读取流
        br.close();
        isr.close();
        fis.close();
    }
}
