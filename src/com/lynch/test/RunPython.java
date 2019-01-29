package com.lynch.test;


import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by lynch on 2018/10/17. <br>
 **/
public class RunPython {

    public static void main(String args[]) {
        try {
            System.out.println("start");
            Process pr = Runtime.getRuntime().exec("/Users/lynch/.virtualenvs2/bin/python /Users/lynch/Documents/Idea/BasePythonTest/com/lynch/redis/RedisFromTo.py");

            BufferedReader in = new BufferedReader(new
                    InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
            System.out.println("end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//        PythonInterpreter interpreter = new PythonInterpreter();
//        interpreter.execfile("/Users/lynch/Documents/Idea/BasePythonTest/com/lynch/BaseNote.py");
}
