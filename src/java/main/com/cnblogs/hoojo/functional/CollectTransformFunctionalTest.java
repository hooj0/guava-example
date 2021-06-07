package com.cnblogs.hoojo.functional;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.base.Joiner;
import com.google.common.collect.*;
import com.google.common.collect.Maps.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * 集合函数式编程
 * @author hoojo
 * @createDate 2017年11月10日 下午3:15:56
 * @file CollectTransformFunctionalTest.java
 * @package com.cnblogs.hoojo.functional
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class CollectTransformFunctionalTest extends BasedTest {

	private Iterable<Object> records;
	private Map<String, Integer> maps;
	
	@Before
	public void before() {
		records = Arrays.asList(1, "3", 2, 5, "7", 9, "11", 8, 6, "2", "4", 5, 7);
		
		maps = Maps.newHashMap();
		maps.put("a", 1);
		maps.put("b", 2);
		maps.put("z", 2);
		maps.put("x", 3);
		maps.put("y", 5);
	}
	
	@Test
	public void testTransform() {
		
		/** Iterable */
		print(Iterables.transform(records, (input) -> {
			if (input instanceof Integer && ((Integer) input).intValue() > 6) {
				return ((Integer) input).intValue() + 1;
			}
			return input;
		})); // [1, 3, 2, 5, 7, 10, 11, 9, 6, 2, 4, 5, 8]
		
		/** FluentIterable filter */
		print(FluentIterable.from(records).transform((input) -> {
			if (input instanceof Integer && ((Integer) input).intValue() > 6) {
				return ((Integer) input).intValue() + 2;
			}
			return null;
		})); // [null, null, null, null, null, 11, null, 10, null, null, null, null, 9]
		
		/** Iterators.filter */
		print(Joiner.on(',').join(Iterators.transform(records.iterator(), (input) -> {
			if (input instanceof Integer && ((Integer) input).intValue() > 6) {
				return 1;
			}
			return 2;
		}))); 
		
		/** Collection */
		Collection<Integer> collect = Arrays.asList(1, 2, 5, 9, 8, 6, 5, 7);
		print(Collections2.transform(collect, (x) -> {
			if (x instanceof Integer && ((Integer) x).intValue() > 6) {
				return true;
			}
			return false;
		})); // 9,8,7
		
		/** Lists */ 	
		print("Lists:" + Lists.transform(Arrays.asList(1, 2, 5, 9, 8, 6, 5, 7), (x) -> {
			if (x instanceof Integer && ((Integer) x).intValue() > 6) {
				return true;
			}
			return false;
		})); 
		
		
		/** SortedMap */ 	
		print("SortedMap.filterValues:" + Maps.transformValues(ImmutableSortedMap.copyOf(maps), (x) -> {
			if (x > 2) {
				return x + 2;
			}
			return x - 2;
		}));

		print("SortedMap.filterEntries:" + Maps.transformEntries(maps, new EntryTransformer<String, Integer, Integer>() {
			@Override
			public Integer transformEntry(String key, Integer value) {
				return value + 5;
			}
		}));
	}
}
