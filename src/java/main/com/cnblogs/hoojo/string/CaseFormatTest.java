package com.cnblogs.hoojo.string;

import org.junit.Test;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.base.CaseFormat;

/**
 * string case format
 * 字符串大小写转换和格式化 
 * @author hoojo
 * @createDate 2019年1月2日 下午5:11:57
 * @file CaseFormatTest.java
 * @package com.cnblogs.hoojo.string
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class CaseFormatTest extends BasedTest {

	@Test
	public void testCaseFormat() {
		
		// 字符串格式转换
		System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "CONSTANT_NAME")); // constantName
		System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, "CONSTANT_NAME")); // constant-name
		System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_UNDERSCORE, "CONSTANT_NAME")); // constant_name
		System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "CONSTANT_NAME")); // ConstantName
		System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, "CONSTANT_NAME")); // CONSTANT_NAME
	}
}
