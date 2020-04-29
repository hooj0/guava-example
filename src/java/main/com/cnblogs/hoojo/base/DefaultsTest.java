package com.cnblogs.hoojo.base;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.base.Defaults;
import org.junit.Test;

/**
 * 默认值测试
 *
 * @author hoojo
 * @version 1.0
 * @date 2022/02/09 15:35:34
 */
public class DefaultsTest extends BasedTest {

    @Test
    @SuppressWarnings("UnnecessaryUnboxing")
    public void testDefaults() {
        out(Defaults.defaultValue(boolean.class).booleanValue()); // false
        out(Defaults.defaultValue(char.class).charValue()); // '\0'
        out(Defaults.defaultValue(byte.class).byteValue()); // 0
        out(Defaults.defaultValue(short.class).shortValue()); // 0
        out(Defaults.defaultValue(int.class).intValue()); // 0
        out(Defaults.defaultValue(long.class).longValue()); // 0
        out(Defaults.defaultValue(float.class).floatValue()); // 0.0f
        out(Defaults.defaultValue(double.class).doubleValue()); // 0.0d
        out(Defaults.defaultValue(void.class)); // null
        out(Defaults.defaultValue(String.class)); // null
    }
}
