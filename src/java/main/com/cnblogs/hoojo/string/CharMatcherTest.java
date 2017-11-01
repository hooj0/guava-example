package com.cnblogs.hoojo.string;

import org.junit.Test;

import com.google.common.base.CharMatcher;

/**
 * <b>function:</b> 字符匹配器
 * @author hoojo
 * @createDate 2017年10月30日 下午4:42:24
 * @file CharMatcherTest.java
 * @package com.cnblogs.hoojo.string
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class CharMatcherTest {

	@Test
	public void testMatcherAPI() {
		
		System.out.println(CharMatcher.javaIsoControl().removeFrom("abc/efg/234"));
		
		// 删除数字
		System.out.println(CharMatcher.digit().removeFrom("abc234def56"));
		
		// 保留数字
		System.out.println(CharMatcher.digit().retainFrom("abc234def56"));
		
		// 删除两端的空格，并把中间的空格替换成单个指定字符
		System.out.println(CharMatcher.whitespace().trimAndCollapseFrom("   ab c2 34 de  f56   ", '_'));
		
		// 将数字替换成指定字符
		System.out.println(CharMatcher.javaDigit().replaceFrom("abc234def56", "#"));
		
		// 删除匹配到任意字符
		System.out.println(CharMatcher.anyOf("aeiou").removeFrom("abc23QUB4def5XYZ6"));
		
		// 删除匹配到的单个字符
		System.out.println(CharMatcher.is('/').removeFrom("abc/efg/234"));
		
		// 删除匹配 指定区间的字符串
		System.out.println(CharMatcher.inRange('1', '9').removeFrom("abc23QUB4def5XYZ6"));
	}
	
	/**
	 * 多个匹配条件链接进行匹配：
	 * or: 或者，只要满足一个就匹配成功
	 * and: 并且，两个条件都需要满足，才能匹配成功
	 * negate: 取反的意思，满足前置条件后取反向的部分
	 */
	@Test
	public void testJoinAPI() {
		/**----------------------- or -----------------------*/
		
		// 只保留数字和小写字母
		System.out.println(CharMatcher.javaDigit().or(CharMatcher.javaLowerCase()).retainFrom("abc23QUB4def5XYZ6"));
		// 删除 指定数字区间 或者 指定字符串区间
		System.out.println(CharMatcher.inRange('1', '9').or(CharMatcher.inRange('a', 'z')).removeFrom("abc23QUB4def5XYZ6"));
		
		/**----------------------- and -----------------------*/
		System.out.println("and:" + CharMatcher.javaDigit().and(CharMatcher.javaLowerCase()).removeFrom("abc23QUB4def5XYZ6"));
		
		/**----------------------- negate -----------------------*/
		// 只保留数字，negate 取反的意思
		System.out.println(CharMatcher.javaDigit().negate().removeFrom("abc23QUB4def5XYZ6"));
		// 只保留小写字符串
		System.out.println(CharMatcher.javaLowerCase().negate().removeFrom("abc23QUB4def5XYZ6"));
		// 只保留小写字符串和数字
		System.out.println(CharMatcher.javaLowerCase().or(CharMatcher.javaDigit()).negate().removeFrom("abc23QUB4def5XYZ6"));
		// 删除大写字符串和数字
		System.out.println(CharMatcher.javaLowerCase().negate().or(CharMatcher.javaDigit()).removeFrom("abc23QUB4def5XYZ6"));
	}
	
	@Test
	public void testActionAPI() {
		String text = "abc234def56";
		// 删除动作
		System.out.println(CharMatcher.digit().removeFrom(text));
		
		// 替换动作
		System.out.println(CharMatcher.digit().collapseFrom(text, '*'));
		
		// 匹配到的数量次数
		System.out.println(CharMatcher.digit().countIn(text));
		
		// 首次匹配到的索引下标
		System.out.println(CharMatcher.digit().indexIn(text));
		
		// 从指定位置开始匹配，查找匹配的索引下标
		System.out.println(CharMatcher.digit().indexIn(text, 6));
		
		// 最后一次出现的索引下标
		System.out.println(CharMatcher.digit().lastIndexIn(text));
		
		// 是否全部匹配，eg：是否全部为数字
		System.out.println(CharMatcher.digit().matchesAllOf(text));
		System.out.println(CharMatcher.digit().matchesAllOf("1246464"));
		
		System.out.println(CharMatcher.digit().matchesAllOf("1246464"));
	}
}
