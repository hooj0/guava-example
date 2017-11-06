package com.cnblogs.hoojo.collections._new;

import org.junit.Test;

import com.google.common.collect.MutableClassToInstanceMap;

/**
 * <b>function:</b> MutableClassToInstanceMap和 ImmutableClassToInstanceMap。
 * @author hoojo
 * @createDate 2017年10月16日 下午2:41:00
 * @file ClassToInstanceMapTest.java
 * @package com.cnblogs.hoojo.collections
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ClassToInstanceMapTest {

	// MutableClassToInstanceMap和 ImmutableClassToInstanceMap。
	@Test
	public void testClassToInstanceMap() {
		
		MutableClassToInstanceMap<Object> map = MutableClassToInstanceMap.create();
		map.put(Object.class, "str");
		map.put(Integer.class, 2);
		
		System.out.println(map); // {class java.lang.Object=str, class java.lang.Integer=2}
		
		System.out.println(map.getInstance(Integer.class)); // 2
	}
}
