package com.cnblogs.hoojo.functional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;

/**
 * 集合函数式编程
 * @author hoojo
 * @createDate 2017年11月10日 下午3:15:56
 * @file CollectFilterFunctionalTest.java
 * @package com.cnblogs.hoojo.functional
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class CollectFilterFunctionalTest {

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
	
	private void print(Object text) {
		System.out.println(text);
	}
	
	@Test
	public void testFilter() {
		
		/** Iterable */
		// 过滤class
		print(Iterables.filter(records, String.class)); // [3, 7, 11, 2, 4]
		// 过滤
		print(Iterables.filter(records, (input) -> {
			if (input instanceof Integer && ((Integer) input).intValue() > 6) {
				return true;
			}
			return false;
		})); // [9, 8, 7]
		
		/** FluentIterable filter */
		print(FluentIterable.from(records).filter(Integer.class)); // [1, 2, 5, 9, 8, 6, 5, 7]
		print(FluentIterable.from(records).filter((input) -> {
			if (input instanceof Integer && ((Integer) input).intValue() > 6) {
				return true;
			}
			return false;
		})); // [9, 8, 7]
		
		/** Iterators.filter */
		print(Joiner.on(',').join(Iterators.filter(records.iterator(), String.class))); // 3,7,11,2,4
		print(Joiner.on(',').join(Iterators.filter(records.iterator(), (input) -> {
			if (input instanceof Integer && ((Integer) input).intValue() > 6) {
				return true;
			}
			return false;
		}))); // 9,8,7
		
		/** Collection */
		Collection<Integer> collect = Arrays.asList(1, 2, 5, 9, 8, 6, 5, 7);
		print(Collections2.filter(collect, (x) -> {
			if (x instanceof Integer && ((Integer) x).intValue() > 6) {
				return true;
			}
			return false;
		})); // 9,8,7
		
		/** Set */ 	
		print("set:" + Sets.filter(ImmutableSet.copyOf(records), (x) -> {
			if (x instanceof Integer && ((Integer) x).intValue() > 6) {
				return true;
			}
			return false;
		})); // [9, 8, 7]
		
		/** SortedSet */ 	
		print("SortedSet:" + Sets.filter(ImmutableSortedSet.copyOf(Iterables.filter(records, Integer.class)), (x) -> {
			if (x instanceof Integer && ((Integer) x).intValue() > 6) {
				return true;
			}
			return false;
		})); // [7, 8, 9]
		
		/** Map  */	
		print("Map filterKeys:" + Maps.filterKeys(maps, (x) -> {
			if (CharMatcher.inRange('a', 'd').test(x.charAt(0))) {
				return true;
			}
			return false;
		})); // {a=1, b=2}
		
		print("Maps.filterValues:" + Maps.filterValues(maps, (x) -> {
			if (x > 2) {
				return true;
			}
			return false;
		})); // {x=3, y=5}

		print("Maps.filterEntries:" + Maps.filterEntries(maps, (x) -> {
			if (x.getValue() == 2) {
				return true;
			}
			return false;
		})); // {b=2, z=2}
		
		/** SortedMap */ 	
		print("SortedMap filterKeys:" + Maps.filterKeys(new TreeMap<>(maps), (x) -> {
			if (CharMatcher.inRange('a', 'd').test(x.charAt(0))) {
				return true;
			}
			return false;
		})); // {a=1, b=2}
		
		print("SortedMap.filterValues:" + Maps.filterValues(ImmutableSortedMap.copyOf(maps), (x) -> {
			if (x > 2) {
				return true;
			}
			return false;
		}));

		print("SortedMap.filterEntries:" + Maps.filterEntries(ImmutableSortedMap.copyOf(maps), (x) -> {
			if (x.getValue() == 2) {
				return true;
			}
			return false;
		}));
		
		/** Multimap */ 	
		ImmutableMultimap<String, Integer> multimap = ImmutableMultimap.<String, Integer>builder().putAll(maps.entrySet()).build();
		
		print("Multimaps filterKeys:" + Multimaps.filterKeys(multimap, (x) -> {
			if (CharMatcher.inRange('a', 'd').test(x.charAt(0))) {
				return true;
			}
			return false;
		})); // {a=[1], b=[2]}
		
		print("Multimaps.filterValues:" + Multimaps.filterValues(multimap, (x) -> {
			if (x > 2) {
				return true;
			}
			return false;
		})); // {x=[3], y=[5]}

		print("Multimaps.filterEntries:" + Multimaps.filterEntries(multimap, (x) -> {
			if (x.getValue() == 2) {
				return true;
			}
			return false;
		})); // {b=[2], z=[2]}
	}
}
