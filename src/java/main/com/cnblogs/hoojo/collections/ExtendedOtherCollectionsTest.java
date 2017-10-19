package com.cnblogs.hoojo.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;

/**
 * 其他扩展
 * @author hoojo
 * @createDate 2017年10月19日 下午6:04:37
 * @file ExtendedOtherCollectionsTest.java
 * @package com.cnblogs.hoojo.collections
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ExtendedOtherCollectionsTest {

	// PeekingIterator 能让你事先窥视[peek()]到下一次调用next()返回的元素
	@Test
	public void testPeekingIterator() {
		
		List<Integer> src = Lists.newArrayList(1, 2, 3, 1, 2, 3, 5);
		// 打印下一个元素
		PeekingIterator<Integer> iter = Iterators.peekingIterator(src.iterator());
		while (iter.hasNext()) {
			Integer current = iter.next();
			
		    if (iter.hasNext()) {
		    	Integer next = iter.peek();
		    	System.out.println("current->" + current + ", next->" + next);
		    }
		}
		/*output：
			current->1, next->2
			current->2, next->3
			current->3, next->1
			current->1, next->2
			current->2, next->3
			current->3, next->5
		*/
		
		// 去掉紧跟下一个元素的重复元素
		src = Lists.newArrayList(1, 2, 3, 3, 3, 2, 2, 5);
		List<Integer> result = Lists.newArrayList();
		iter = Iterators.peekingIterator(src.iterator());
		while (iter.hasNext()) {
			Integer current = iter.next();
		    while (iter.hasNext() && iter.peek().equals(current)) {
		        //跳过重复的元素
		        iter.next();
		    }
		    result.add(current);
		}
		
		System.out.println(result); // [1, 2, 3, 2, 5]
	}
	
	// 自定义Iterator
	// 注意：AbstractIterator继承了UnmodifiableIterator，所以禁止实现remove()方法。
	// 如果你需要支持remove()的迭代器，就不应该继承AbstractIterator。
	class MyGuavaIter<T> extends AbstractIterator<T> {

		private Iterator<T> iter;
		
		public MyGuavaIter(Iterator<T> iter) {
			this.iter = iter;
		}
		
		@Override
		protected T computeNext() {
			
			while (iter.hasNext()) {
                T s = iter.next();
                if (s != null) {
                    return s;
                }
            }
			
            return endOfData();
		}
	}
	
	// 自定义List集合
	class MyGuavaList<E> extends ForwardingList<E> {

		private final List<E> delegate = Lists.newArrayList();
		
		@Override
		protected List<E> delegate() {
			return delegate;
		}
		
		@Override
		public Iterator<E> iterator() {
			return new MyGuavaIter<E>(delegate.iterator());
		}
		
		public MyGuavaIter<E> myGuavaIter() {
			return new MyGuavaIter<E>(standardIterator());
		}
	}
	
	@Test
	public void testAbstractIterator() {
		
		MyGuavaList<Integer> list = new MyGuavaList<Integer>();
		list.addAll(Arrays.asList(1, 5, null, 3, 3, 2, null));
		
		System.out.println(list); // [1, 5, null, 3, 3, 2, null]
		System.out.println(list.iterator().hasNext()); // true
		System.out.println(list.myGuavaIter().peek()); // 1
		
		Iterator<Integer> iter = list.iterator(); // filter Null element
		while (iter.hasNext()) {
			System.out.println("elements: " + iter.next());
		}
		/*
			elements: 1
			elements: 5
			elements: 3
			elements: 3
			elements: 2
		 */
	}
	
	/**
	 * 接受前一个值作为参数的迭代器
	 * 注意，你必须额外传入一个初始值，或者传入null让迭代立即结束。
	 * 因为computeNext(T)假定null值意味着迭代的末尾——AbstractSequentialIterator不能用来实现可能返回null的迭代器
	 * 
	 * note：默认接受一个构造参数，作为起始值。每次迭代输出的结果作为下次迭代的参数。
	 */
	@Test
	public void testAbstractSequentialIterator() {
		
		List<Integer> list = Arrays.asList(1, 5, null, 3, 3, 2, null);
		Iterator<Integer> iter = list.iterator();
		
		/**
		 * 默认接受一个构造参数，作为起始值。每次迭代输出的结果作为下次迭代的参数。
		 */
		Iterator<Integer> iter2 = new AbstractSequentialIterator<Integer>(1) {
			@Override
			protected Integer computeNext(Integer previous) {
				
				if (iter.hasNext()) {
					Integer s = iter.next();
					
					int result = previous;
	                if (s != null) {
	                	result = s * previous;
	                } 
	                
	                System.out.println("前一个元素: " + previous + ", 当前元素: " + s + ", 结果：" + result);
	                return result;
	            }
				
				return null;
			}
		};
		
		System.out.println(list); // [1, 5, null, 3, 3, 2, null]
		while (iter2.hasNext()) {
			iter2.next();
		}
		
		/*
			前一个元素: 1, 当前元素: 1, 结果：1
			前一个元素: 1, 当前元素: 5, 结果：5
			前一个元素: 5, 当前元素: null, 结果：5
			前一个元素: 5, 当前元素: 3, 结果：15
			前一个元素: 15, 当前元素: 3, 结果：45
			前一个元素: 45, 当前元素: 2, 结果：90
			前一个元素: 90, 当前元素: null, 结果：90
		 */
		
		System.out.println("---------------------------------");
		Iterator<Integer> powersOfTwo = new AbstractSequentialIterator<Integer>(1) { // note the initial value!
			protected Integer computeNext(Integer previous) {
				return (previous == 1 << 10) ? null : previous * 2;
			}
		};
		
		List<Integer> result = Lists.newArrayList();
		while (powersOfTwo.hasNext()) {
			result.add(powersOfTwo.next());
		}
		// elements: 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024
		System.out.println("elements: " + Joiner.on(", ").join(result));
		
		System.out.println("---------------------------------");
		Iterator<Integer> iter3 = new AbstractSequentialIterator<Integer>(1) { // note the initial value!
			protected Integer computeNext(Integer previous) {
				//return (previous > 10000) ? null : (previous * 2 + 1);
				return (previous > 10000) ? null : (previous * previous + 1);
			}
		};
		
		result.clear();
		while (iter3.hasNext()) {
			result.add(iter3.next());
		}
		// elements: 1, 2, 5, 26, 677, 458330
		System.out.println("elements: " + Joiner.on(", ").join(result));
	}
}
