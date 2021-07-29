package com.cnblogs.hoojo.reflection;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.reflect.ClassPath;
import org.junit.Test;

import java.io.IOException;

/**
 * 尽最大努力的类路径扫描
 * ClassPath是一个尽力而为的工具。它只扫描jar文件中或者某个文件目录下的class文件。
 * 也不能扫描非URLClassLoader的自定义class loader管理的class，所以不要将它用于关键任务生产任务。
 * @author hoojo
 * @createDate 2018年12月26日 下午5:12:19
 * @file ClassPathTest.java
 * @package com.cnblogs.hoojo.reflection
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class ClassPathTest extends BasedTest {

	@Test
	public void test1() throws IOException {
		ClassLoader classloader = ClassLoader.getSystemClassLoader();
		ClassPath classPath = ClassPath.from(classloader); // 扫描类加载器使用的类路径
		// 扫描指定包下面的class
		for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClasses("com.cnblogs.hoojo.functional")) {
			out(classInfo.getName());
		}
		
		// 会把所以class加载出来，包括jar文件中
		for (ClassPath.ClassInfo classInfo : classPath.getAllClasses()) {
			out(classInfo.getName());
		}
		
		// 递归
		for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClassesRecursive("com.cnblogs.hoojo.functional")) {
			out(classInfo.getName());
		}
	}
}
