package com.cnblogs.hoojo.reflection;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;

/**
 * TypeToken是创建，操作，查询泛型类型（以及，隐含的类）对象的方法。
 * 使用了基于反射的技巧甚至让你在运行时都能够巧妙的操作和查询泛型类型。
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

	@SuppressWarnings("serial")
	@Test
	public void testQueryAPI() {
		ArrayList<String> stringList = Lists.newArrayList();
		ArrayList<Integer> intList = Lists.newArrayList();
		
		// Java不能在运行时保留对象的泛型类型信息
		// 由于类型擦除，你不能够在运行时传递泛型类对象
		out(stringList.getClass().isAssignableFrom(intList.getClass())); // true
		//returns true, even though ArrayList<String> is not assignable from ArrayList<Integer>
		
		// 结论：
		// 如果你在运行时有一个ArrayList<String>对象，
		// 你不能够判定这个对象是有泛型类型ArrayList<String>的 —— 并且通过不安全的原始类型，
		// 你可以将这个对象强制转换成ArrayList<Object>。
		
		// 反射允许你去检测方法和类的泛型类型。如果你实现了一个返回List的方法，
		// 并且你用反射获得了这个方法的返回类型，你会获得代表List<String>的ParameterizedType。
		
		// TypeToken类使用这种变通的方法以最小的语法开销去支持泛型类型的操作。
		TypeToken<List<String>> token = new TypeToken<List<String>>() {};
		out(token);	// java.util.List<java.lang.String>
		// 返回组件类型数组。
		out(token.getComponentType());	
		// 返回大家熟知的运行时类
		out(token.getRawType()); 	// interface java.util.List
		// 获得包装的java.lang.reflect.Type.
		out(token.getType());		// java.util.List<java.lang.String>
		// 返回那些有特定原始类的子类型。举个例子，如果这有一个Iterable并且参数是List.class，那么返回将是List。
		out(token.getSubtype(List.class)); // 传入其他类型将出现异常
		// 产生这个类型的超类，这个超类是指定的原始类型。举个例子，如果这是一个Set并且参数是Iterable.class，结果将会是Iterable。
		out(token.getSupertype(List.class));
		// 返回一个Set，包含了这个所有接口，子类和类是这个类型的类。返回的Set同样提供了classes()和 interfaces()方法允许你只浏览超类和接口类。
		out(token.getTypes()); // [java.util.List<java.lang.String>, java.util.Collection<java.lang.String>, java.lang.Iterable<java.lang.String>]
		// 检查某个类型是不是数组，甚至是<? extends A[]>。
		out(token.isArray());	// false
		// 如果此类型是九种基本类型之一，则返回true
		out(token.isPrimitive()); // false
		// 判断是否是该子类型
		out(token.isSubtypeOf(TypeToken.of(List.class)));	// true
		out(token.isSubtypeOf(TypeToken.of(Integer.class))); // false

		out(token.isSupertypeOf((new TypeToken<List<String>>() {}).getClass()));	// false
		out(token.isSupertypeOf(TypeToken.of(Integer.class))); // false
		
	}
	
	@Test
	@SuppressWarnings("serial")
	public void testStatic() {
		// 获取一个基本的、原始类的TypeToken非常简单：
		TypeToken<String> stringTok = TypeToken.of(String.class);
		out(stringTok.getRawType());	// class java.lang.String
		out(stringTok.getType());		// class java.lang.String
		
		TypeToken<Integer> intTok = TypeToken.of(Integer.class);
		out(intTok.getRawType());	// class java.lang.Integer
		out(intTok.getType());		// class java.lang.Integer
		
		// 为获得一个含有泛型的类型的TypeToken —— 当你知道在编译时的泛型参数类型 —— 你使用一个空的匿名内部类：
		TypeToken<List<String>> stringListToken = new TypeToken<List<String>>() {};
		out(stringListToken.getRawType());	// interface java.util.List
		out(stringListToken.getType());		// java.util.List<java.lang.String>
		
		// 指向一个通配符类型：
		TypeToken<Map<?, ?>> mapToken = new TypeToken<Map<?, ?>>() {
		};
		out(mapToken.getRawType());		// interface java.util.Map
		out(mapToken.getType());		// java.util.Map<?, ?>
	}
	
	// 构建动态泛型类型
	@SuppressWarnings("serial")
	static <K, V> TypeToken<Map<K, V>> mapToken(TypeToken<K> keyToken, TypeToken<V> valueToken) {
		
		return new TypeToken<Map<K,V>>() {}
				.where(new TypeParameter<K>() {}, keyToken)
				.where(new TypeParameter<V>() {}, valueToken);
	}
	
	
	@Test
	@SuppressWarnings({ "unused", "serial" })
	public void testDynamic() {
		// 动态的解决泛型类型参数
		TypeToken<Map<String, BigInteger>> mapToken = mapToken(TypeToken.of(String.class), TypeToken.of(BigInteger.class));
		
		TypeToken<Map<Integer, Set<String>>> complexToken = mapToken(TypeToken.of(Integer.class), new TypeToken<Set<String>>() {
		});
	}
	
	@Test
	@SuppressWarnings("serial")
	public void testResolveType() throws NoSuchMethodException, SecurityException {
		// 可以用来“替代” context token 中的类型参数的一个强大而复杂的查询操作。
		TypeToken<Function<Integer, String>> funcToken = new TypeToken<Function<Integer,String>>() {
		};
		
		TypeToken<?> result = funcToken.resolveType(Function.class.getTypeParameters()[1]);
		out(result);	// java.lang.String

		// TypeToken将Java提供的TypeVariables和context token中的类型变量统一起来。
		// 这可以被用来一般性地推断出在一个类型相关方法的返回类型：
		TypeToken<Map<String, Integer>> mapToken = new TypeToken<Map<String,Integer>>() {
		};
		
		TypeToken<?> setToken = mapToken.resolveType(Map.class.getMethod("entrySet").getGenericReturnType());
		out(setToken);	// java.util.Set<java.util.Map.java.util.Map$Entry<java.lang.String, java.lang.Integer>>
		out(setToken.getRawType());
		out(setToken.getType());
	}
	
	private void out(Object o) {
		System.out.println(o);
	}
}
