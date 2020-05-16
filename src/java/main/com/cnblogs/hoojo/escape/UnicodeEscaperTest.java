package com.cnblogs.hoojo.escape;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.escape.UnicodeEscaper;
import org.junit.Test;

/**
 * unicode字符转义
 *
 * @author hoojo
 * @version 1.0
 * @date 2022/02/10 16:11:03
 */
public class UnicodeEscaperTest extends BasedTest {

    // Escapes everything except [a-zA-Z0-9]
    private static final UnicodeEscaper SIMPLE_ESCAPER = new UnicodeEscaper() {
        @Override
        protected char[] escape(int cp) {
            return ('a' <= cp && cp <= 'z') || ('A' <= cp && cp <= 'Z') || ('0' <= cp && cp <= '9') ? null : ("[" + String.valueOf(cp) + "]").toCharArray();
        }
    };


    @Test
    public void test() {
        UnicodeEscaper e = SIMPLE_ESCAPER;
        String expected =
                "[0]abyz[128][256][2048][4096]ABYZ[65535]"
                        + "["
                        + Character.MIN_SUPPLEMENTARY_CODE_POINT
                        + "]"
                        + "0189["
                        + Character.MAX_CODE_POINT
                        + "]";
        out(e.escape(expected)); // [91]0[93]abyz[91]128[93][91]256[93][91]2048[93][91]4096[93]ABYZ[91]65535[93][91]65536[93]0189[91]1114111[93]
    }
}
