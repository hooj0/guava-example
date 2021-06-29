package com.cnblogs.hoojo.range;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.collect.BoundType;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import org.junit.Test;

import java.util.Arrays;

/**
 * Range 区间测试
 * 
 * 
    (a..b) = {x | a < x < b}
    [a..b] = {x | a <= x <= b}
    [a..b) = {x | a <= x < b}
    (a..b] = {x | a < x <= b}
    (a..+∞) = {x | x > a}
    [a..+∞) = {x | x >= a}
    (-∞..b) = {x | x < b}
    (-∞..b] = {x | x <= b}
    (-∞..+∞) = 所有值

 * 
 * @author hoojo
 * @createDate 2018年2月28日 下午3:54:20
 * @file RangeTest.java
 * @package com.cnblogs.hoojo.range
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class RangeTest extends BasedTest {

	@Test
	public void testCreate() {
		// 全开区间 {x | a < x < b}
		out(Range.<String>open("a", "z")); // (a..z)
		// 左开右闭区间 {x | a < x <= b}
		out(Range.<String>openClosed("d", "v")); // (d..v]
		
		// 全闭区间 {x | a <= x <= b}
		out(Range.<String>closed("a", "z")); // [a..z]
		// 左闭右开区间 {x | a <= x < b}
		out(Range.<String>closedOpen("x", "y")); // [x..y)

		// 所有区间
		out(Range.<String>all()); // (-∞..+∞)
		// 单区间
		out(Range.<String>singleton("j")); // [j..j]
		
		// 无限大且包含区间 {x | x >= a}
		out(Range.<String>atLeast("a")); // [a..+∞)
		// 无限大不包含 区间 {x | x > a}
		out(Range.<String>greaterThan("a")); // (a..+∞)

		// 无限小区间 {x | x <= b}
		out(Range.<String>atMost("b")); // (-∞..b]
		// 无限小且不包含区间 {x | x < b}
		out(Range.<String>lessThan("b")); // (-∞..b)
		
		// 上限
		out(Range.<String>downTo("xyz", BoundType.OPEN)); // (xyz..+∞)
		out(Range.<String>downTo("xyz", BoundType.CLOSED)); // [xyz..+∞)
		
		// 下限
		out(Range.<String>upTo("xyz", BoundType.OPEN)); // (-∞..xyz)
		out(Range.<String>upTo("xyz", BoundType.CLOSED)); // (-∞..xyz]
		
		// 通过集合计算一个全闭的区间
		out(Range.<String>encloseAll(Arrays.asList("b", "a", "x"))); // [a..x]
		
		out(Range.<String>range("a", BoundType.CLOSED, "z", BoundType.OPEN)); // [a..z)
	}
	
	@Test
	public void testCompare() {
		// 判断数值是否在某个区间
		out(Range.<String>closed("a", "z").and(input -> { return true; }).test("9")); // false
		out(Range.<String>closed("a", "z").and(input -> { return true; }).test("h")); // true

		// 区间中是否包含某个值
		out(Range.<String>closed("a", "e").contains("x")); // false
		out(Range.<String>closed("a", "e").contains("c")); // true
		
		// 区间中是否包含集合
		out(Range.<String>closed("a", "e").containsAll(Arrays.asList("e", "f"))); // false
		out(Range.<String>closed("a", "e").containsAll(Arrays.asList("e", "d"))); // true
		
		// 区间中是否包含区间值
		out(Range.<String>closed("b", "f").encloses(Range.<String>closed("a", "z"))); // false
		out(Range.<String>closed("b", "f").encloses(Range.<String>closed("d", "e"))); // true
		
		out(Range.<String>closed("b", "f").encloses(Range.<String>closed("d", "z"))); // false
		out(Range.<String>closed("b", "f").encloses(Range.<String>closed("a", "e"))); // false

		// 是否存在无限小 区间
		out(Range.<String>closed("b", "f").hasLowerBound()); // true
		out(Range.<String>all().hasLowerBound()); // false
		out(Range.<String>atMost("z").hasLowerBound()); // false
		out(Range.<String>lessThan("z").hasLowerBound()); // false
		
		// 是否存在无限大 区间
		out(Range.<String>closed("b", "f").hasUpperBound()); // true
		out(Range.<String>all().hasUpperBound()); // false
		out(Range.<String>atLeast("z").hasUpperBound()); // false
		
		// 判断区间是否连续
		out(Range.<Integer>closed(1, 5).isConnected(Range.<Integer>closed(6, 9))); // false
		out(Range.<Integer>closed(1, 5).isConnected(Range.<Integer>closed(3, 9))); // true
		out(Range.<Integer>closed(1, 5).isConnected(Range.<Integer>open(5, 9))); // true
		out(Range.<Integer>closed(1, 5).isConnected(Range.<Integer>closed(5, 9))); // true
		
		out("--empty---");
		out(Range.<Integer>closedOpen(3, 4).isEmpty()); // false
		out(Range.<Integer>closed(4, 4).isEmpty()); // false
		out("--empty---");
		
		// negate 取反
		out(Range.<Integer>closed(3, 6).negate().test(5)); // false
		out(Range.<Integer>closed(3, 6).negate().test(7)); // true

		// 满足区间值或者满足后面的表达式
		out(Range.<Integer>closed(3, 6).or(item -> { return true; }).test(7)); // true
		out(Range.<Integer>closed(3, 6).or(item -> { return false; }).test(7)); // false
		
		//Predicates.compose(predicate, function)
	}
	
	@Test
	public void test1() {
		// 获取左边区间类型
		out(Range.<String>open("a", "b").lowerBoundType()); // OPEN
		// 获取左边区间值
		out(Range.<String>open("a", "b").lowerEndpoint()); // a
		// 获取右边区间类型
		out(Range.<String>open("a", "b").upperBoundType()); // OPEN
		// 获取左边区间值
		out(Range.<String>open("a", "b").upperEndpoint()); // b
	}
	
	@Test
	public void test_combination() {

		// DiscreteDomain
		out(Range.<Integer>closed(1, 5).canonical(DiscreteDomain.integers()).test(8)); // false
		out(Range.<Integer>closed(1, 5).canonical(DiscreteDomain.integers()).test(3)); // true
				
		// 交集区间
		out(Range.<Integer>closed(1, 5).intersection(Range.<Integer>closed(3, 8))); // [3..5]
		
		// 组合区间
		out(Range.<Integer>closed(3, 5).span(Range.<Integer>open(4, 8))); // [3..8)
		out(Range.<Integer>closed(3, 5).span(Range.<Integer>open(7, 8))); // [3..8)
	}
}
