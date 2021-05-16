package com.cnblogs.hoojo.object;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import org.junit.Test;

import java.util.Arrays;

/**
 * Objects 工具类
 * @author hoojo
 * @createDate 2017年10月10日 下午5:42:23
 * @file ObjectsTest.java
 * @package com.cnblogs.hoojo.object
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class ObjectsTest extends BasedTest {

	@Test
	public void testEq() {
		
		try {
			out(Objects.equal(1, 2)); //false
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			out(Objects.equal(null, 2)); //false
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			out(Objects.equal(1, 1)); // true
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testArrayEq() {
		String[] a = { "a", "b", "c" };
		String[] b = { "a", "b", "c" };
		
		out(a.equals(b)); // false
		out(Objects.equal(a, b)); // false
		out(java.util.Objects.deepEquals(a, b)); // true
		
		a = new String[] { "a", "b", "c",  };
		b = new String[] { "a", "b", "c", "" };
		
		out(a.equals(b)); // false
		out(Objects.equal(a, b)); // false
		out(java.util.Objects.deepEquals(a, b)); // false
	}
	
	@Test
	public void testNonNull() {
		
		try {
			java.util.Objects.requireNonNull(null); // NullPointerException
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			java.util.Objects.requireNonNull(null, "对象为空"); // NullPointerException: 对象为空
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			out(java.util.Objects.nonNull(null)); // false
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		out(java.util.Objects.isNull(null)); // true
		
		out(ComparisonChain.start().compare(0, 1).compare(2.2, 3.0).result());
	}
	
	@Test
	public void testHash() {
		
		out(Objects.hashCode("A", "b", 1, 2)); //2954147
		
		out(java.util.Objects.hash("A", "b", 1, 2)); //2954147
		out(java.util.Objects.hashCode(Arrays.asList("A", "b", 1, 2))); //2954147
	}
	
	@Test
	public void testMoreObjects() {
		// 如果参数1 不为空 就直接返回参数1
		// 如果参数1 为空就返回不为空的参数2
		// 如果参数1/2 都为空就抛出异常
		try {
			Object o = MoreObjects.firstNonNull("a", "2"); // return a
			// Object o = MoreObjects.firstNonNull(null, "2"); // return 2
			// Object o = MoreObjects.firstNonNull(null, null); // NullPointException
			out(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 构造字符串，通常在toString方法中使用
		try {
			Object o = MoreObjects.toStringHelper(this).add("name", "hoo").add("age", 11).addValue(100).omitNullValues(); // ObjectsTest{name=hoo, age=11, 100}
			out(o);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
