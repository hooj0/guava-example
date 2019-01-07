package com.cnblogs.hoojo.reflection;

import org.junit.Test;

import com.google.common.reflect.Reflection;

/**
 * 类加载
 * 工具方法Reflection.initialize(Class…)能够确保特定的类被初始化——执行任何静态初始化。
 * @author hoojo
 * @createDate 2018年12月26日 下午5:20:51
 * @file ClassLoadingTest.java
 * @package com.cnblogs.hoojo.reflection
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ClassLoadingTest {

	@Test
	public void test1() {
		// 使用这种方法的是一个代码异味，因为静态伤害系统的可维护性和可测试性。
		// 在有些情况下，你别无选择，而与传统的框架，操作间，这一方法有助于保持代码不那么丑。
		
		Reflection.initialize(ClassPathTest.class);
	}
}
