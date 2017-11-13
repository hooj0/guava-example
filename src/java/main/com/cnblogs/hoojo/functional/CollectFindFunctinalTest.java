package com.cnblogs.hoojo.functional;

import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;

/**
 * <b>function:</b> 集合查找
 * @author hoojo
 * @createDate 2017年11月10日 下午4:44:17
 * @file CollectFindFunctinalTest.java
 * @package com.cnblogs.hoojo.functional
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class CollectFindFunctinalTest {

	private Iterable<?> records;
	private Map<String, Integer> maps;
	private Predicate<Object> find;
	
	@Before
	public void before() {
		records = Arrays.asList(1, "3", 2, 5, "7", 9, "11", 8, 6, "2", "4", 5, 7);
		
		maps = Maps.newHashMap();
		maps.put("a", 1);
		maps.put("b", 2);
		maps.put("z", 2);
		maps.put("x", 3);
		maps.put("y", 5);
		
		find = (input) -> {
			if (input instanceof Integer && ((Integer) input).intValue() > 6) {
				return true;
			}
			return false;
		};
	}
	
	private void print(Object text) {
		System.out.println(text);
	}
	
	@Test
	public void testFind() {
		
		/** find */
		// 循环并返回一个满足元素满足断言的元素，如果没有则抛出NoSuchElementException 
		print(Iterables.find(records, find)); // 9
		//print(Iterables.find(Ints.asList(), find)); // NoSuchElementException
		print(Iterables.find(Ints.asList(), find, -11)); // -11
		print(Iterators.find(Ints.asList().iterator(), find, -2)); // -2
		
		/** all */
		// 是否所有元素满足断言？懒实现：如果发现有元素不满足，不会继续迭代 	
		print(Iterators.all(records.iterator(), find)); // false
		print(Iterators.all(Iterators.forArray(7, 8, 9), find)); // true

		print(FluentIterable.from(records).allMatch(find)); // false
		print(FluentIterable.from(Ints.asList(7, 8, 9)).allMatch(find)); // true
		
		/** any */
		// 是否有任意元素满足元素满足断言？懒实现：只会迭代到发现满足的元素 
		print(Iterators.any(records.iterator(), find)); // true

		print(FluentIterable.from(records).anyMatch(find)); // true
		
		/** indexOf */
		// 返回第一个满足元素满足断言的元素索引值，若没有返回-1 	
		print(Iterators.indexOf(records.iterator(), find)); // 5
		print(Iterators.indexOf(Ints.asList().iterator(), find)); // -1
		
		/**removeIf */
		// 移除所有满足元素满足断言的元素，实际调用Iterator.remove()方法 	
		print(Iterators.removeIf(records.iterator(), find));
		print(Iterators.removeIf(Ints.asList().iterator(), find));
	}
}
