package com.cnblogs.hoojo.collections._new;

import com.cnblogs.hoojo.BasedTest;
import org.junit.Test;

import com.google.common.collect.BoundType;
import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.google.common.collect.TreeRangeSet;

/**
 * RangeSet/RangeMap
 * @author hoojo
 * @createDate 2017年10月16日 下午2:48:26
 * @file RangeCollectionTest.java
 * @package com.cnblogs.hoojo.collections
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class RangeCollectionTest extends BasedTest {

	@Test
	public void testRangeSet() {
		
		TreeRangeSet<Integer> tree = TreeRangeSet.<Integer>create();
		
		tree.add(Range.open(2, 9)); // 合并相邻重合区间的映射
		tree.add(Range.closed(5, 10));
		tree.add(Range.openClosed(10, 13));
		tree.add(Range.closedOpen(15, 20));
		
		out(tree); // [(2..13], [15..20)]
		out(tree.encloses(Range.open(5, 10))); // true 存在某一区间
		out(tree.encloses(Range.open(5, 14))); // false
		
		out(tree.intersects(Range.open(50, 188))); // false 只要和边界有交集就为true
		out(tree.intersects(Range.open(14, 188))); // true
		
		out(tree.asDescendingSetOfRanges()); // [[15..20), (2..13]] 倒序集合区间
		out(tree.asRanges()); // [(2..13], [15..20)]
		out(tree.complement()); // [(-∞..2], (13..15), [20..+∞)] 为重合区间边界
		
		out(tree.rangeContaining(22)); // null 返回该数值所在区间
		out(tree.rangeContaining(10)); // (2..13]
		
		out(tree.span()); // (2..20) 最小-最大区间边界
		out(tree.subRangeSet(Range.open(5, 55))); // [(5..13], [15..20)] 返回区间的交集区间
		
		ImmutableRangeSet<Integer> range = ImmutableRangeSet.<Integer>of(Range.open(2, 10));
		out(range); // [(2..10)]
	}
	
	@Test 
	public void testRangeMap() {
		RangeMap<Integer, String> range = TreeRangeMap.<Integer, String>create();
		
		range.put(Range.closed(3, 9), "foo");
		range.put(Range.closed(7, 9), "t00");
		range.put(Range.closed(17, 22), "bar");
		
		out(range); // [[3..7)=foo, [7..9]=t00, [17..22]=bar]
		out(range.asMapOfRanges()); // {[3..7)=foo, [7..9]=t00, [17..22]=bar}
		// 匹配到key的对应的区间
		out(range.getEntry(8)); // [7..9]=t00
	}
	
	@Test
	public void testRange() {
		out(Range.all()); // (-∞..+∞)
		out(Range.atLeast(5)); // 上限 [5..+∞)
		out(Range.atMost(5)); // 下限  (-∞..5]
		
		out(Range.downTo(4, BoundType.CLOSED)); // [4..+∞)
		out(Range.greaterThan(9)); // (9..+∞)
		out(Range.singleton(3)); // [3..3] 单区间
	}
}
