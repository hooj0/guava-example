package com.cnblogs.hoojo.base;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.base.Ascii;
import org.junit.Test;

/**
 * ascii测试
 *
 * @author hoojo
 * @version 1.0
 * @date 2022/02/08 11:25:12
 */
public class AsciiTest extends BasedTest {

    @Test
    public void test() {
        out(Ascii.isLowerCase('A')); // false
        out(Ascii.isUpperCase('A')); // true

        out(Ascii.toLowerCase("ABCdefg")); // abcdefg
        out(Ascii.toUpperCase("ABCdefg")); // ABCDEFG

        out(Ascii.truncate("ABCdefg", 15, "...")); // ABCdefg
        out(Ascii.truncate("ABCdefg", 5, "...")); // AB...

        out(Ascii.truncate("foobar", 10, "...")); // foobar
        out(Ascii.truncate("foobar", 5, "...")); // fo...
        out(Ascii.truncate("foobar", 6, "...")); // foobar
        out(Ascii.truncate("foobar", 3, "...")); // ...
        out(Ascii.truncate("foobar", 10, "…")); // foobar
        out(Ascii.truncate("foobar", 4, "…")); // foo…
        out(Ascii.truncate("foobar", 4, "--")); // fo--
        out(Ascii.truncate("foobar", 6, "…")); // foobar
        out(Ascii.truncate("foobar", 5, "…")); // foob…
        out(Ascii.truncate("foobar", 3, "")); // foo
    }
}
