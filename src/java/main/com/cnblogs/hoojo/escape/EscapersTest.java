package com.cnblogs.hoojo.escape;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import org.junit.Test;

/**
 * 字符转义转换
 *
 * @author hoojo
 * @version 1.0
 * @date 2022/02/09 17:37:48
 */
public class EscapersTest extends BasedTest {

    @Test
    public void test() {
        out(Escapers.nullEscaper().escape("\0\n\t\\az09~\uD800\uDC00\uFFFF")); //  \az09~𐀀￿

        Escaper escaper = Escapers.builder().setSafeRange('a', 'z').build();
        out(escaper.escape("The Quick Brown Fox \\0\\uFFFF")); // The Quick Brown Fox \0\uFFFF

        escaper = Escapers.builder().setUnsafeReplacement("X").build();
        out(escaper.escape("The Quick \0\uFFFF")); // The Quick  ￿

        Escapers.Builder builder = Escapers.builder();
        builder.setSafeRange('a', 'z'); // 设置安全字符边界
        builder.setUnsafeReplacement("X"); // 不安全字符进行替换
        out(builder.build().escape("The Quick Brown Fox!")); // XheXXuickXXrownXXoxX

        builder.addEscape(' ', "_"); // 添加替换字符
        builder.addEscape('!', "_"); // 添加替换字符
        out(builder.build().escape("The Quick Brown Fox!")); // "Xhe_Xuick_Xrown_Xox_",

        // 显式替换优先于安全字符
        builder.setSafeRange(' ', '~'); // 设置安全字符边界
        out(builder.build().escape("The Quick Brown Fox!")); // The_Quick_Brown_Fox_
    }

    @Test
    public void build() {
        Escapers.Builder builder = Escapers.builder();

        builder.setSafeRange('a', 'z');
        builder.setUnsafeReplacement("X");
        builder.addEscape(' ', "_");
        Escaper first = builder.build();

        builder.addEscape(' ', "-");
        builder.addEscape('!', "$");
        Escaper second = builder.build();

        // 不会改变之前的 builder
        builder.addEscape(' ', "*");

        out(first.escape("The Quick Brown Fox!")); // Xhe_Xuick_Xrown_XoxX
        out(second.escape("The Quick Brown Fox!")); // Xhe-Xuick-Xrown-Xox$
    }
}
