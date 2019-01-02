package com.cnblogs.hoojo.string;

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
public class StringsTest {

	@Test
	public void testStrings() {
		
		// 获取相同的前缀
		System.out.println(Strings.commonPrefix("abdz", "abcdefg")); // ab
		System.out.println(Strings.commonPrefix("abcdg", "abcdefg")); // abcd
		
		// 获取相同的后缀
		System.out.println(Strings.commonSuffix("abcefg", "123efg")); // efg
		
		// 空字符 转 null 对象
		System.out.println(Strings.emptyToNull(null)); // null
		System.out.println(Strings.emptyToNull("")); // null
		
		// 判断为空
		System.out.println(Strings.isNullOrEmpty(null)); // true
		System.out.println(Strings.isNullOrEmpty("")); // true
		System.out.println(Strings.isNullOrEmpty(" ")); // false
		
		// 空对象转 空字符串 “”
		System.out.println(Strings.nullToEmpty(null)); // ""
		System.out.println(Strings.nullToEmpty("")); // ""
		
		// 在末尾追加字符串，如果长度不足的条件下
		System.out.println(Strings.padEnd("hello", 1, '!')); // hello
		System.out.println(Strings.padEnd("hello", "hello".length() + 3, '!')); // hello!!!
		
		// 在前面追加字符串
		System.out.println(Strings.padStart("hello", 1, '!')); // hello
		System.out.println(Strings.padStart("hello", 10, '!')); // !!!!!hello
		
		// 复制本身
		System.out.println(Strings.repeat("go", 3)); // gogogo
	}
	
	@Test
	public void testCharset() {
		
		System.out.println(Charsets.ISO_8859_1);
		System.out.println(new String("".getBytes(Charsets.UTF_8)));
	}
}
