package com.cnblogs.hoojo.string;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

/**
 * <b>function:</b> Joiner 连接器
 * @author hoojo
 * @createDate 2017年10月30日 下午3:35:30
 * @file JoinerTest.java
 * @package com.cnblogs.hoojo.string
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class JoinerTest {

	@Test
	public void testAPI() throws IOException {
		// 跳过Null值，用“;”链接
		Joiner joiner = Joiner.on(";").skipNulls();
		String result = joiner.join(Arrays.asList("a", "b", 1, null, 3, "c"));
		
		System.out.println(result);
		
		// 空值用None代替，用“;”链接
		System.out.println(Joiner.on(";").useForNull("None").join(Arrays.asList("a", "b", 1, null, 3, "c")));
		
		// 返回StringBuilder
		StringBuilder builder = new StringBuilder();
		Joiner.on(',').skipNulls().appendTo(builder, Arrays.asList("a", "b", 1, null, 3, "c"));
		System.out.println(builder);
		
		// join 支持多参数签名
		System.out.println(Joiner.on(",").join("oh", '!', 1, new Integer(3)));
		
		// 传入 Appendable 接口实现
		System.out.println(Joiner.on(',').skipNulls().appendTo(new StringWriter(), Arrays.asList("a", "b", 1, null, 3, "c")));
		
		// 链接Map数据，并且可以链接Key和Value
		System.out.println(Joiner.on(',').withKeyValueSeparator("_").join(ImmutableMap.of("name", "jack", "age", 24)));
	}
}
