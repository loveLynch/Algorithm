package com.lynch.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lynch on 2018/10/8. <br>
 **/
public class Solution {
    String[] dict = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    public List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        if (digits.equals(""))
            return res;

        helper(digits, 0, res, "", dict);
        System.out.println(res);

        return res;
    }

    public void helper(String remainDigits, int start, List<String> list, String temp, String[] dict) {
        if (remainDigits.length() == temp.length()) {
            list.add(new String(temp));
            System.out.println(list);
        } else {
            for (char c : dict[remainDigits.charAt(start) - '0'].toCharArray()) {

                temp += c;
                System.out.println(temp);

                helper(remainDigits, start + 1, list, temp, dict);

                temp = temp.substring(0, temp.length() - 1);
            }


        }
    }

    public int minPathSum(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;
        int m = grid.length;
        int n = grid[0].length;
        int[][] dp = new int[m][n];
        dp[0][0] = grid[0][0];
        System.out.println(m+" "+n);
        for (int i = 1; i < m; i++) {
            dp[i][0] = grid[i][0] + dp[i - 1][0];
        }
        for (int i = 1; i < n; i++) {
            dp[0][i] = grid[0][i] + dp[0][i - 1];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
            }
        }
        System.out.println(dp[m - 1][n - 1]);
        return dp[m - 1][n - 1];
    }

    @Test
    public void testletterCombinations() {
        letterCombinations("27");
    }

    @Test
    public void testminPathSum() {
        /*
        [
            [1,3,1],
            [1,5,1],
            [4,2,1]
        ]
         */
        int[][] gird = {{1,3,1},{1,5,1}};

        minPathSum(gird);
    }

}