package com.cnblogs.hoojo.reflection;

import org.junit.Test;

import com.google.common.reflect.TypeToken;

/**
 * 类型反射
 * @author hoojo
 * @createDate 2018年3月28日 下午6:37:46
 * @file TypeTokenTest.java
 * @package com.cnblogs.hoojo.reflection
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TypeTokenTest {

	@Test
	public void test1() {
		TypeToken<String> token = TypeToken.of(String.class);
	}
}
