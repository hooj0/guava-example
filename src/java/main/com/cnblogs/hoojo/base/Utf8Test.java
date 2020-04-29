package com.cnblogs.hoojo.base;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.base.Utf8;
import org.junit.Test;

/**
 * use utf8测试
 *
 * @author hoojo
 * @version 1.0
 * @date 2022/02/09 17:32:32
 */
public class Utf8Test extends BasedTest {

    @Test
    public void test() {
        out(Utf8.encodedLength("use utf8测试"));
    }
}
