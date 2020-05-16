package com.cnblogs.hoojo.escape;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.escape.CharEscaperBuilder;
import com.google.common.escape.Escaper;
import org.junit.Test;

/**
 * 炭排放器构建器测试
 *
 * @author hoojo
 * @version 1.0
 * @date 2022/02/10 16:04:24
 */
public class CharEscaperBuilderTest extends BasedTest {

    @Test
    public void test() {
        CharEscaperBuilder builder = new CharEscaperBuilder();
        builder.addEscape('a', "X");
        builder.addEscapes(new char[]{ 'b', 'c', 'd' }, "X");

        Escaper escaper = builder.toEscaper();
        out(escaper.escape("abcdefABCDEF")); // XXXXefABCDEF
    }
}
