package com.cnblogs.hoojo.string;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Predicate;

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
	public void testMatcherAPI() throws UnsupportedEncodingException {
		
		// 删除iso字符串
		System.out.println(CharMatcher.javaIsoControl().removeFrom("'/u4E2D', '/u56FD'"));
		
		// 删除数字
		System.out.println(CharMatcher.digit().removeFrom("abc234def56")); // abcdef
		
		// 删除任意字符
		System.out.println(CharMatcher.any().removeFrom("abc234def56哈哈")); // ""
		
		// 删除匹配到任意字符
		System.out.println(CharMatcher.anyOf("aeiou").removeFrom("abc23QUB4def5XYZ6")); // bc23QUB4df5XYZ6
				
		// ascii 值 小于等于 128 都匹配
		System.out.println(CharMatcher.ascii().removeFrom("abc234def56哈哈‡›²釦")); //哈哈‡›²釦
		
		// 删除空格
		System.out.println(CharMatcher.breakingWhitespace().removeFrom("  thinking in java,  oh,  my god")); //thinkinginjava,oh,mygod
		
		// 自定义匹配条件
		System.out.println(CharMatcher.forPredicate(new Predicate<Character>() {
			@Override
			public boolean apply(Character input) {
				if (input > 'a' && input < 'e') {
					return false;
				}
				return true;
			}
		}).removeFrom("abcddezxy")); //bcdd
		
		// 在指定区间
		System.out.println(CharMatcher.inRange('a', 'g').removeFrom("abcddezxy")); // zxy
		
		System.out.println(CharMatcher.invisible().removeFrom("abc\r234zx%2f\\ef5\n6;66,zz"));
		
		
		
		
		
		// 将数字替换成指定字符
		System.out.println(CharMatcher.javaDigit().replaceFrom("abc234def56", "#"));
		
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
		System.out.println(CharMatcher.javaDigit().or(CharMatcher.javaLowerCase()).retainFrom("abc23QUB4def5XYZ6")); // abc234def56
		// 删除 指定数字区间 或者 指定字符串区间
		System.out.println(CharMatcher.inRange('1', '9').or(CharMatcher.inRange('a', 'z')).removeFrom("abc23QUB4def5XYZ6")); //QUBXYZ
		
		/**----------------------- and -----------------------*/
		// 英文字母、并且是小写的，删掉
		System.out.println("and:" + CharMatcher.javaLetter().and(CharMatcher.javaLowerCase()).removeFrom("abc23QUB4def5XYZ6")); // 23QUB45XYZ6
		
		/**----------------------- negate -----------------------*/
		// 只保留数字，negate 取反的意思
		System.out.println(CharMatcher.javaDigit().negate().removeFrom("abc23QUB4def5XYZ6")); // 23456
		// 只保留小写字符串
		System.out.println(CharMatcher.javaLowerCase().negate().removeFrom("abc23QUB4def5XYZ6")); // abcdef
		// 只保留小写字符串和数字
		System.out.println(CharMatcher.javaLowerCase().or(CharMatcher.javaDigit()).negate().removeFrom("abc23QUB4def5XYZ6")); //abc234def56
		// 删除大写字符串和数字
		System.out.println(CharMatcher.javaLowerCase().negate().or(CharMatcher.javaDigit()).removeFrom("abc23QUB4def5XYZ6")); // abcdef
	}
	
	@Test
	public void testActionAPI() {
		String text = "abc234def56";
		// 删除动作
		System.out.println(CharMatcher.digit().removeFrom(text)); // abcdef
		
		// 替换动作
		System.out.println(CharMatcher.digit().collapseFrom(text, '*')); // abc*def*
		
		// 匹配到的数量次数
		System.out.println(CharMatcher.digit().countIn(text)); // 5
		
		// 首次匹配到的索引下标
		System.out.println(CharMatcher.digit().indexIn(text)); // 3
		
		// 从指定位置开始匹配，查找匹配的索引下标
		System.out.println(CharMatcher.digit().indexIn(text, 6)); // 9
		
		// 最后一次出现的索引下标
		System.out.println(CharMatcher.digit().lastIndexIn(text)); // 10
		
		// 是否全部匹配，eg：是否全部为数字
		System.out.println(CharMatcher.digit().matchesAllOf(text)); // false
		System.out.println(CharMatcher.digit().matchesAllOf("1246464")); // true
		
		// 是否包含，eg：是否包含数字
		System.out.println(CharMatcher.digit().matchesAnyOf(text)); // true
		System.out.println(CharMatcher.digit().matchesAnyOf("abcABC")); // false
		
		// 是否不包含，是否不包含数字
		System.out.println(CharMatcher.digit().matchesNoneOf(text)); // false
		System.out.println(CharMatcher.digit().matchesNoneOf("abcABC")); // true
		System.out.println(CharMatcher.digit().matchesNoneOf("1234234234")); // false
		
		// 将匹配到的字符替换成字符
		System.out.println(CharMatcher.digit().replaceFrom(text, '_')); // abc___def__
		// 将匹配到的字符替换成字符串
		System.out.println(CharMatcher.digit().replaceFrom(text, "^_^")); // abc^_^^_^^_^def^_^^_^
		
		// 将匹配到的字符全部保留，剔除不匹配的字符；和removeFrom相反
		System.out.println(CharMatcher.digit().retainFrom(text)); // 23456
		
		// 将两端匹配到的替换为空（删掉），剩余中间的部分替换为指定字符
		System.out.println(CharMatcher.digit().trimAndCollapseFrom("1111ab c2 34 de  f566666", '*')); // ab c* * de  f
		System.out.println(CharMatcher.whitespace().trimAndCollapseFrom(" ab c2 34 de  f566  ", '-')); // ab-c2-34-de-f566
		
		// 将两端匹配到的字符删除掉
		System.out.println(CharMatcher.digit().trimFrom("ab13234234ab")); // ab13234234ab  
		System.out.println(CharMatcher.digit().trimFrom("1323AABB4234")); // AABB
		System.out.println(CharMatcher.inRange('a', 'z').trimFrom("ab13234234ab")); // 13234234
		
		// 将左边匹配到的字符串删除掉
		System.out.println(CharMatcher.digit().trimLeadingFrom("1323AABB4234")); // AABB4234
		
		// 将右边匹配到的字符串删除掉
		System.out.println(CharMatcher.digit().trimTrailingFrom("1323AABB4234")); // 1323AABB
		
		System.out.println(CharMatcher.digit().trimTrailingFrom("1323AABB4234")); // 1323AABB
	}
}
