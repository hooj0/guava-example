package com.cnblogs.hoojo.collections.utility_classes;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.PeekingIterator;

/**
 * 集合Iterators工具类
 * @author hoojo
 * @createDate 2018年1月10日 上午11:28:57
 * @file IteratorsTest.java
 * @package com.cnblogs.hoojo.collections
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class IteratorsTest {

	Iterator<String> iter = Arrays.asList("a", "b", "c").iterator();
	
	Predicate<Object> pred = new Predicate<Object>() {
		@Override
		public boolean apply(Object input) {
			try {
				Integer.parseInt(input.toString());
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		}
	};
	
	@Test
	public void testIter() {
		
		List<String> list = Lists.newArrayList("1", "2");
		// 将iter添加到list集合中
		out(Iterators.addAll(list, iter)); // true
		out(list); // [1, 2, a, b, c]
		
		out(list.size()); // 5
		// 返回指定下标索引，如果不存在就返回最大的索引下标长度
		out(Iterators.advance(list.iterator(), 9)); // 5
		out(Iterators.advance(list.iterator(), 2)); // 2
		
		// 转换枚举类型
		out(Iterators.asEnumeration(list.iterator()));
		
		List<Iterator<String>> iters = Lists.newArrayList(list.iterator(), iter);
		// 连接多个iterator
		Iterators.concat(iters.iterator()).forEachRemaining(new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.print(t + ", ");
			}
		}); // 1, 2, a, b, c, 
		System.out.println();
		
		Iterators.concat(list.iterator(), iter).forEachRemaining(new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.print(t + ", ");
			}
		}); // 1, 2, a, b, c, 
		System.out.println();
		
		// 构建一次性的iterator，在迭代时就移除元素
		Iterator<String> temp = Iterators.consumingIterator(list.iterator());
		// consumingIterator 会在第一次迭代的时候将元素移除
		temp.forEachRemaining(item -> { System.out.print(item + ", "); }); // 1, 2, a, b, c,
		
		if (temp.hasNext()) {
			System.out.print("element");
		} // empty
		System.out.println();
		
		// 构建无限循环的iterator元素，移除指定元素将释放循环
		Iterator<String> iter2 = Iterators.cycle("1", "2");
		while(iter2.hasNext()) {
			System.out.println(iter2.next());
			iter2.remove(); // 不移除将死循环
		}
		
		// 不可变iterator
		Iterators.forArray(1, 2, "a", "b").forEachRemaining(System.out::print); // 12ab
		System.out.println();
		
		// 枚举转换Iterator
		Iterators.forEnumeration(Iterators.asEnumeration(list.iterator())).forEachRemaining(System.out::print); // 12
		System.out.println();
		
		// 合并迭代
		List<Iterator<Integer>> iterList = Lists.newArrayList(Arrays.asList(2, 8, 5, 1, 10, 7).iterator(), Arrays.asList(1, 7, 5).iterator());
		out(Iterators.toString(Iterators.mergeSorted(iterList, Ordering.natural()))); //[1, 2, 7, 5, 8, 5, 1, 10, 7]
		
		// 分组组合
		out(Iterators.toString(Iterators.paddedPartition(Arrays.asList(2, 8, 5, 1, 10, 7).iterator(), 2))); //[[2, 8], [5, 1], [10, 7]]
		out(Iterators.toString(Iterators.paddedPartition(Arrays.asList(2, 8, 5, 1, 10, 7).iterator(), 3))); //[[2, 8, 5], [1, 10, 7]]
		
		out(Iterators.toString(Iterators.partition(Arrays.asList(2, 8, 5, 1, 10, 7).iterator(), 2))); //[[2, 8], [5, 1], [10, 7]]
		
		// 可以输出下一个元素，但不影响next
		PeekingIterator<Integer> pkIter = Iterators.peekingIterator(Arrays.asList(2, 8, 5, 1, 10, 7).iterator());
		out(pkIter.next()); // 2
		out(pkIter.peek()); // 8
		out(pkIter.peek()); // 8
		out(pkIter.next()); // 8

		out(pkIter.peek()); // 5
		out(pkIter.next()); // 5

		out(pkIter.peek()); // 1
		out(pkIter.next()); // 1

		
		// 单个值的iterator
		out(Iterators.toString(Iterators.singletonIterator("xyz"))); // xyz
		
		// 长度，元素个数
		out(Iterators.size(list.iterator())); // 2
		
		// 转换元素
		out(Iterators.toString(Iterators.transform(list.iterator(), new Function<String, Object>() {
			@Override
			public Object apply(String input) {
				
				return input + "->";
			}
		}))); // [1->, 2->]
	}
	
	@Test
	public void testUpdated() {
		List<?> objs = Arrays.asList("x", "z", 2, 3, "y");
		
		objs = Lists.newArrayList(objs);
		out(objs); //[x, z, 2, 3, y]
		// 删除在集合中存在的元素
		out(Iterators.removeAll(objs.iterator(), Lists.newArrayList(2, 3))); // true
		out(Iterators.removeAll(objs.iterator(), Lists.newArrayList(2, 3, 4))); // false
		
		// 删除在集合中不存在的元素
		out("retainAll:" + Iterators.retainAll(objs.iterator(), Lists.newArrayList(2, 3, 4))); // true

		// 删除匹配条件的数据
		Iterator<String> iter3 = Lists.newArrayList("1", "2", "a").iterator();
		out(Iterators.removeIf(iter3, pred)); // true
	}
	
	@Test
	public void testFilter() {
		List<String> list = Lists.newArrayList("1", "2");
		
		out(Iterators.all(list.iterator(), new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				try {
					if (Integer.parseInt(input) > 0) {
						return true;
					}
				} catch (NumberFormatException e) {
					return false;
				}
				return false;
			}
		})); // 是否全部 大于 0
		
		out(Iterators.any(list.iterator(), item -> {
			try {
				if (Integer.parseInt(item) > 0) {
					return true;
				}
			} catch (NumberFormatException e) {
				return false;
			}
			return false;
		})); // 是否有任意个元素大于 0
		
		list = Lists.newArrayList("1", "2");
		// 判断iterator中是否包含指定元素
		out(Iterators.contains(list.iterator(), "1")); // true
		out(Iterators.contains(list.iterator(), "b")); // false
		
		list = Lists.newArrayList("1", "2");
		// 判断iterator中的元素和顺序是否相等
		out(Iterators.elementsEqual(list.iterator(), Lists.newArrayList("2").iterator())); // false
		out(Iterators.elementsEqual(list.iterator(), Lists.newArrayList("2", "1").iterator())); // false
		out(Iterators.elementsEqual(list.iterator(), Lists.newArrayList("1", "2").iterator())); // true
		
		// 按类型过滤元素
		Iterator<?> iterObj = Iterators.filter(Lists.newArrayList(1, 2, "a", "3").iterator(), Integer.class);
		iterObj.forEachRemaining(item -> { System.out.print(item + ", "); }); // 1, 2,
		System.out.println();
		
		// 自定义过滤元素
		iterObj = Iterators.filter(Lists.newArrayList(1, 2, "a", "3").iterator(), pred);
		
		iterObj.forEachRemaining(item -> { System.out.print(item + ", "); }); // 1, 2, 3, 
		System.out.println();
		
		// 查找元素，返回第一个匹配的元素
		out(Iterators.find(Lists.newArrayList(1, 2, "a", "3").iterator(), pred)); // 1
		
		// 支持默认值
		out(Iterators.find(Lists.newArrayList("b", "a").iterator(), pred, "not find")); // not find
		
		//返回指定元素出现的次数
		out(Iterators.frequency(list.iterator(), "x")); // 0
		out(Iterators.frequency(Arrays.asList("1", "2", 2, 3, "2").iterator(), "2")); // 2
		
		List<?> objs = Arrays.asList("x", "z", 2, 3, "y");
		// 返回指定索引位置元素
		out(Iterators.get(objs.iterator(), 1)); // z
		out(Iterators.get(objs.iterator(), 10, "not item")); // not item

		// 返回最后一个元素
		out(Iterators.getLast(objs.iterator())); // y
		out(Iterators.getLast(iter, "notfound")); // notfound

		// 下一个元素
		out(Iterators.getNext(objs.iterator(), "not Found")); // x
		out(Iterators.getNext(iter, "not Found")); // not Found
		
		// 当iterator只有一个元素就返回该元素，否则将抛出异常
		out(Iterators.getOnlyElement(Arrays.asList("2").iterator())); // 2
		out(Iterators.getOnlyElement(iter, "def")); // def
		
		// 匹配第一个符合条件的元素下标索引
		out(Iterators.indexOf(objs.iterator(), pred)); // 2
		
		// 返回前2个元素
		out(Iterators.toString(Iterators.limit(objs.iterator(), 2))); // [x, z]
		
		// 尝试查找，返回Optional
		out(Iterators.tryFind(list.iterator(), pred).get()); // 1
	}
	
	private void out(Object o) {
		System.out.println(o);
	}
}
