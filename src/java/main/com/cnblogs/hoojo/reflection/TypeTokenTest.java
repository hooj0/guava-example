package com.cnblogs.hoojo.reflection;

import java.util.ArrayList;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;

/**
 * reflection type token
 * 
 * 使用了基于反射的技巧甚至让你在运行时都能够巧妙的操作和查询泛型类型。
 * TypeToken是创建，操作，查询泛型类型（以及，隐含的类）对象的方法。
 * 
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
		ArrayList<String> stringList = Lists.newArrayList();
		ArrayList<Integer> intList = Lists.newArrayList();
		
		// Java不能在运行时保留对象的泛型类型信息
		// 由于类型擦除，你不能够在运行时传递泛型类对象
		System.out.println(stringList.getClass().isAssignableFrom(intList.getClass())); // true
		//returns true, even though ArrayList<String> is not assignable from ArrayList<Integer>
		
		TypeToken<String> token = TypeToken.of(String.class);
	}
}
