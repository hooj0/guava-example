package com.cnblogs.hoojo.collections._new;

import com.cnblogs.hoojo.BasedTest;
import org.junit.Test;

import com.google.common.collect.MutableClassToInstanceMap;

/**
 * MutableClassToInstanceMap和 ImmutableClassToInstanceMap。
 * @author hoojo
 * @createDate 2017年10月16日 下午2:41:00
 * @file ClassToInstanceMapTest.java
 * @package com.cnblogs.hoojo.collections
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class ClassToInstanceMapTest extends BasedTest {

	// MutableClassToInstanceMap和 ImmutableClassToInstanceMap。
	@Test
	public void testClassToInstanceMap() {
		
		MutableClassToInstanceMap<Object> map = MutableClassToInstanceMap.create();
		map.put(Object.class, "str");
		map.put(Integer.class, 2);
		
		out(map); // {class java.lang.Object=str, class java.lang.Integer=2}
		
		out(map.getInstance(Integer.class)); // 2
	}
}
