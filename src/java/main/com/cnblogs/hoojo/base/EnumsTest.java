package com.cnblogs.hoojo.base;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.base.Converter;
import com.google.common.base.Enums;
import org.junit.Test;

/**
 * 枚举测试
 *
 * @author hoojo
 * @version 1.0
 * @date 2022/02/09 15:42:44
 */
public class EnumsTest extends BasedTest {

    private enum TestEnum {
        SPRING,
        STRUTS,
        MYBATIS
    }

    @Test
    public void testGetIfPresent() {
        out(Enums.getIfPresent(TestEnum.class, "STRUTS")); // Optional.of(STRUTS)
        out(Enums.getIfPresent(TestEnum.class, "STRUTS").isPresent()); // true
        out(Enums.getIfPresent(TestEnum.class, "STRUTS2").isPresent()); // false
        out(Enums.getIfPresent(TestEnum.class, "STRUTS").get()); // STRUTS

        out(Enums.getIfPresent(TestEnum.class, "stRUTS").isPresent()); // false
    }

    @Test
    public void testConvert() {
        Converter<String, TestEnum> converter = Enums.stringConverter(TestEnum.class);
        out(converter.convert("SPRING")); // SPRING
        out(converter.convert("STRUTS")); // STRUTS
        out(converter.convert(null)); // null

        out(converter.reverse().convert(TestEnum.MYBATIS)); // MYBATIS

        converter.convert("xxx"); // Exception
    }

    @Test
    public void testOther() {
        out(Enums.getField(TestEnum.MYBATIS)); // public static final com.cnblogs.hoojo.base.EnumsTest$TestEnum com.cnblogs.hoojo.base.EnumsTest$TestEnum.MYBATIS
    }
}
