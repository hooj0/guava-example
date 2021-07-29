package com.cnblogs.hoojo;

import java.nio.ByteBuffer;

/**
 * based test class
 *
 * @author hoojo
 * @version 1.0
 * @createDate 2019年1月2日 下午4:25:38
 * @file BasedTest.java
 * @package com.cnblogs.hoojo
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 */
public abstract class BasedTest {

    protected static void out() {
        System.out.println();
    }

     protected static void out(Object o) {
        System.out.println(o);
    }

    protected void out(String template, Object... args) {
        System.out.printf((template) + "%n", args);
    }

    protected static void print(Object text) {
        System.out.println(text);
    }

    protected void out(byte[] obj) {
        for (Object o : obj) {
            System.out.print(o + ", ");
        }
        System.out.println();
    }

    protected void out(int[] obj) {
        for (Object o : obj) {
            System.out.print(o + ", ");
        }
        System.out.println();
    }

    protected void out(long[] obj) {
        for (Object o : obj) {
            System.out.print(o + ", ");
        }
        System.out.println();
    }

    protected void out(float[] obj) {
        for (Object o : obj) {
            System.out.print(o + ", ");
        }
        System.out.println();
    }

    protected void out(short[] obj) {
        for (Object o : obj) {
            System.out.print(o + ", ");
        }
        System.out.println();
    }

    protected void out(ByteBuffer buff) {
        for (int i = 0; i < buff.limit(); i++) {
            System.out.print(buff.get(i) + ", ");
        }
        System.out.println();
    }
}
