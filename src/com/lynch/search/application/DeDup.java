package com.lynch.search.application;

import edu.princeton.cs.algs4.StdOut;

import java.io.*;
import java.util.HashSet;

/**
 * Created by lynch on 2018/10/15. <br>
 * 过滤器
 **/
public class DeDup {
    public static void main(String[] args) throws IOException {
        //获取读取流
        FileInputStream fis = new FileInputStream("algs4-data/tinyTale.txt");
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        HashSet<String> set = new HashSet<String>();
        String line = "";
        while ((line = br.readLine()) != null) {
            String[] wordArray = line.split(" ");
            for (String lineword : wordArray) {
                //   System.out.println(lineword);
                String key = lineword.trim();
                if (!set.contains(key)) {
                    set.add(key);
                    StdOut.print(key + " ");
                }

            }
        }
        //关闭读取流
        br.close();
        isr.close();
        fis.close();
    }
}
