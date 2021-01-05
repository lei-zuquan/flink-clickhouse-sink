package ru.ivi.opensource.flinkclickhousesink;

/**
 * @Author:
 * @Date: 2021-01-05 8:31
 * @Version: 1.0
 * @Modified By:
 * @Description:
 */
public class CreateDataMain {
    public static void main(String[] args) {
        for (int i = 15; i < 1000; i++) {
            System.out.println(i + "," + i*10 + "," + i+1);
        }
    }
}
