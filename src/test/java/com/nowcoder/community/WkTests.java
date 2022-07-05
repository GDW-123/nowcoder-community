package com.nowcoder.community;

import java.io.IOException;

/**
 * @Author GuoDingWei
 * @Date 2022/6/27 15:07
 */
public class WkTests {

    public static void main(String[] args) {
        String cmd = "D:/JAVA/tools/wkhtmltopdf/bin/wkhtmltoimage --quality 75  https://www.nowcoder.com  D:/JAVA/tools/wkhtmltopdf/data/wk-images/2.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
