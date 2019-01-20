package com.cnblogs.hoojo.graph;

import java.util.Collection;
import java.util.Iterator;

import com.cnblogs.hoojo.BasedTest;

/**
 * abstract based tests
 * @author hoojo
 * @createDate 2019年1月20日 下午6:04:38
 * @file AbstractGraphTests.java
 * @package com.cnblogs.hoojo.graph
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class AbstractGraphTests extends BasedTest {

	// 格式化节点函数：
	protected String format(Collection<?> collections) {
	    StringBuilder builder = new StringBuilder();
	    for (Object value : collections) {
	        builder.append(value);
	        builder.append(",");
	    }
	    return builder.toString();
	}
	
	protected String format(Iterable<?> iterable) {
	    StringBuilder builder = new StringBuilder();
	    Iterator<?> iter = iterable.iterator();
	    while (iter.hasNext()) {
	    	 builder.append(iter.next());
		        builder.append(",");
	    }
	    return builder.toString();
	}
}
