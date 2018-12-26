package com.cnblogs.hoojo.reflection;

import java.util.ArrayList;
import java.util.List;

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
		
		// 结论：
		// 如果你在运行时有一个ArrayList<String>对象，
		// 你不能够判定这个对象是有泛型类型ArrayList<String>的 —— 并且通过不安全的原始类型，
		// 你可以将这个对象强制转换成ArrayList<Object>。
		
		// 反射允许你去检测方法和类的泛型类型。如果你实现了一个返回List的方法，
		// 并且你用反射获得了这个方法的返回类型，你会获得代表List<String>的ParameterizedType。
		
		// TypeToken类使用这种变通的方法以最小的语法开销去支持泛型类型的操作。
		TypeToken<String> token = TypeToken.of(String.class);
	}
	
	@Test
	public void test2() {
		// 获取一个基本的、原始类的TypeToken非常简单：
		TypeToken<String> stringTok = TypeToken.of(String.class);
		
		TypeToken<Integer> intTok = TypeToken.of(Integer.class);
		
		// 为获得一个含有泛型的类型的TypeToken —— 当你知道在编译时的泛型参数类型 —— 你使用一个空的匿名内部类：
		TypeToken<List<String>> stringListToken = new TypeToken<List<String>>() {};
		
	}
	
	private void out(Object o) {
		System.out.println(o);
	}
}
