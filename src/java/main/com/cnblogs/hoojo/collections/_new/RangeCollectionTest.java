package com.cnblogs.hoojo.collections._new;

import org.junit.Test;

import com.google.common.collect.BoundType;
import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.google.common.collect.TreeRangeSet;

/**
 * <b>function:</b> RangeSet/RangeMap
 * @author hoojo
 * @createDate 2017年10月16日 下午2:48:26
 * @file RangeCollectionTest.java
 * @package com.cnblogs.hoojo.collections
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class RangeCollectionTest {

	@Test
	public void testRangeSet() {
		
		TreeRangeSet<Integer> tree = TreeRangeSet.<Integer>create();
		
		tree.add(Range.open(2, 9)); // 合并相邻重合区间的映射
		tree.add(Range.closed(5, 10));
		tree.add(Range.openClosed(10, 13));
		tree.add(Range.closedOpen(15, 20));
		
		System.out.println(tree);
		System.out.println(tree.encloses(Range.open(5, 10))); // 存在某一区间
		System.out.println(tree.encloses(Range.open(5, 14)));
		
		System.out.println(tree.intersects(Range.open(50, 188))); // 只要和边界有交集就为true
		System.out.println(tree.intersects(Range.open(14, 188)));
		
		System.out.println(tree.asDescendingSetOfRanges()); // 倒序集合区间
		System.out.println(tree.asRanges());
		System.out.println(tree.complement()); // 为重合区间边界
		
		System.out.println(tree.rangeContaining(22)); // 返回该数值所在区间
		System.out.println(tree.rangeContaining(10));
		
		System.out.println(tree.span()); // 最小-最大区间边界
		System.out.println(tree.subRangeSet(Range.open(5, 55))); // 返回区间的交集区间
		
		ImmutableRangeSet<Integer> range = ImmutableRangeSet.<Integer>of(Range.open(2, 10));
		System.out.println(range);
	}
	
	@Test 
	public void testRangeMap() {
		RangeMap<Integer, String> range = TreeRangeMap.<Integer, String>create();
		
		range.put(Range.closed(3, 9), "foo");
		range.put(Range.closed(7, 9), "t00");
		range.put(Range.closed(17, 22), "bar");
		
		System.out.println(range);
		System.out.println(range.asMapOfRanges());
		System.out.println(range.getEntry(8));
	}
	
	@Test
	public void testRange() {
		System.out.println(Range.all()); // (-∞..+∞)
		System.out.println(Range.atLeast(5)); // 上限
		System.out.println(Range.atMost(5)); // 下限
		
		System.out.println(Range.downTo(4, BoundType.CLOSED));
		System.out.println(Range.greaterThan(9));
		System.out.println(Range.singleton(3)); // 单区间
	}
}
