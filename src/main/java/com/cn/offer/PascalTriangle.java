package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript: 打印杨辉三角
 * @date: 2019-10-15 23:33
 */
public class PascalTriangle {
    public static void main(String[] args){
        int[][] line = printPascalTriangle(5);
        for (int i = 0; i < line.length; i++) {
            for(int j = 0; j<=i; j++){
                System.out.print(line[i][j] + " ");
            }
            System.out.println("");
        }
    }

    /**
     * 打印杨辉三角
     * @param n
     * @return
     */
    private static int[][] printPascalTriangle(int n){
        if(n < 1){
            return null;
        }
        int[][] line = new int[n][];
        //手动定义第一行的数据
        line[0] = new int[]{1};
        for (int i = 1; i < n; i++) {
            line[i] = helpPrintPascalTriangle(i+1, line[i-1]);
        }
        return line;
    }

    /**
     * 根据上一行信息打印下一行
     * @param lineNum
     * @param lastLine
     * @return
     */
    private static int[] helpPrintPascalTriangle(int lineNum, int[] lastLine){
        int[] line = new int[lineNum];
        line[0] = lastLine[0];
        for (int i = 1; i < lineNum-1; i++) {
            line[i] = lastLine[i-1] + lastLine[i];
        }
        line[lineNum-1] = lastLine[lineNum-2];
        return line;
    }
}
