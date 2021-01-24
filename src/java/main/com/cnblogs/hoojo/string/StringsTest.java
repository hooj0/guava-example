package com.cnblogs.hoojo.string;

import com.cnblogs.hoojo.BasedTest;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;

/**
 * 字符串工具类
 * @author hoojo
 * @createDate 2017年11月9日 下午5:03:34
 * @file StringsTest.java
 * @package com.cnblogs.hoojo.string
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class StringsTest extends BasedTest {

	@Test
	public void testStrings() {
		
		// 获取相同的前缀
		out(Strings.commonPrefix("abdz", "abcdefg")); // ab
		out(Strings.commonPrefix("abcdg", "abcdefg")); // abcd
		
		// 获取相同的后缀
		out(Strings.commonSuffix("abcefg", "123efg")); // efg
		
		// 空字符 转 null 对象
		out(Strings.emptyToNull(null)); // null
		out(Strings.emptyToNull("")); // null
		
		// 判断为空
		out(Strings.isNullOrEmpty(null)); // true
		out(Strings.isNullOrEmpty("")); // true
		out(Strings.isNullOrEmpty(" ")); // false
		
		// 空对象转 空字符串 “”
		out(Strings.nullToEmpty(null)); // ""
		out(Strings.nullToEmpty("")); // ""
		
		// 在末尾追加字符串，如果长度不足的条件下
		out(Strings.padEnd("hello", 1, '!')); // hello
		out(Strings.padEnd("hello", "hello".length() + 3, '!')); // hello!!!
		
		// 在前面追加字符串
		out(Strings.padStart("hello", 1, '!')); // hello
		out(Strings.padStart("hello", 10, '!')); // !!!!!hello
		
		// 复制本身
		out(Strings.repeat("go", 3)); // gogogo
	}
	
	@Test
	public void testCharset() {
		
		out(Charsets.ISO_8859_1);
		out(new String("".getBytes(Charsets.UTF_8)));
	}
}
