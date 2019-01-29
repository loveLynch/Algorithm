package com.lynch.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by lynch on 2018/9/28. <br>
 **/
public class ConSolePirnt {
    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("output.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(f, true);
        PrintStream printStream = new PrintStream(fileOutputStream);
        System.setOut(printStream);
        System.out.println(" :控制台信息输出到文件 output.txt");
    }
}


