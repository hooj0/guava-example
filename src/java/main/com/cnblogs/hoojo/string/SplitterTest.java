package com.cnblogs.hoojo.string;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

/**
 * <b>function:</b> 字符串分割器
 * @author hoojo
 * @createDate 2017年10月30日 下午3:59:51
 * @file SplitterTest.java
 * @package com.cnblogs.hoojo.string
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class SplitterTest {

	@SuppressWarnings("static-access")
	@Test
	public void testAPI() {
		String str = "1,2,3,, 4   ,5,";
		
		// 以 "," 进行分割
		System.out.println(Splitter.on(",").split(str));
		
		// 对空字符串进行过滤
		System.out.println(Splitter.on(",").omitEmptyStrings().split(str));
		
		// 只切割3块，后面的将不被切割
		System.out.println(Splitter.on(",").limit(3).split("1,2,3,4,5"));
		
		// 过滤掉空格字符串
		System.out.println(Splitter.on(",").omitEmptyStrings().trimResults().split(str));
		
		// 过滤指定字符串
		System.out.println(Splitter.on(",").trimResults(CharMatcher.is(' ')).split(str));
		// 过滤分割字符串前后的字符
		System.out.println(Splitter.on(",").trimResults(CharMatcher.is(';')).split(";abc,;e;,f;,g,1,2"));
		// 过滤小写字符串
		System.out.println(Splitter.on(",").trimResults(CharMatcher.javaLowerCase()).split(";abc,;e;,f;,g,1,2"));
		
		// 将字符串切割成map对象
		System.out.println(Splitter.on(",").withKeyValueSeparator("-").split("name-jack,age-22,email-abc@g.cn"));
		
		// 按固定长度分割字符串
		System.out.println(Splitter.on(",").fixedLength(2).split("1234566779abc"));
		// 使用正则切割
		System.out.println(Splitter.on(",").onPattern("[a-z]").split("1a2c34d56e67f79abc"));
		
		// 多种方式分割
		System.out.println(Splitter.on(",").on(";").split(";abc,;e;,f;,g,1,2"));
	}
}

