package com.cnblogs.hoojo.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.junit.Test;

import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;

/**
 * Invokable是对java.lang.reflect.Method和java.lang.reflect.Constructor的流式包装。
 * 它简化了常见的反射代码的使用。
 * @author hoojo
 * @createDate 2018年12月26日 下午3:27:24
 * @file InvokableTest.java
 * @package com.cnblogs.hoojo.reflection
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class InvokableTest {

	@SuppressWarnings("serial")
	@Test
	public void test1() throws NoSuchMethodException, SecurityException {
		Method method = Map.class.getMethod("size");
		Invokable<Map<String, String>, ?> invokable = new TypeToken<Map<String, String>>(){}.method(method);

		
		// 方法是否是public的?
		// -------------------------------------------------------
		// JDK
		out(Modifier.isPublic(method.getModifiers())); // true
		
		// Invokable  
		out(invokable.isPublic());		// true
		out(invokable.getReturnType()); // int
		
		
		// 方法是否是package private?
		// -------------------------------------------------------
		// JDK
		boolean privatePackage = !(Modifier.isPrivate(method.getModifiers()) || Modifier.isPublic(method.getModifiers()));
		out(privatePackage); // false
		// Invokable  
		out(invokable.isPackagePrivate()); // false
		
		// 方法是否能够被子类重写？
		// -------------------------------------------------------
		// JDK
		boolean isOverride = !(Modifier.isFinal(method.getModifiers())
				|| Modifier.isPrivate(method.getModifiers())
				|| Modifier.isStatic(method.getModifiers())
				|| Modifier.isFinal(method.getDeclaringClass().getModifiers()));
		out(isOverride);	// true
		// Invokable  
		out(invokable.isOverridable());	// true
		
		
		// 方法的第一个参数是否被定义了注解@Nullable？

	}
	
	private void out(Object o) {
		System.out.println(o);
	}
}
