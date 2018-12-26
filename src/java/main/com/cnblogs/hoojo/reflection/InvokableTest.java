package com.cnblogs.hoojo.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

import com.google.common.collect.Maps;
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
	public void testAPI() throws NoSuchMethodException, SecurityException, InvocationTargetException, IllegalAccessException {
		Method method = Map.class.getMethod("size");
		Invokable<Map<String, String>, ?> invokable = new TypeToken<Map<String, String>>(){}.method(method);
		out(invokable);					// java.util.Map<java.lang.String, java.lang.String>.public abstract int java.util.Map.size()
		out(Invokable.from(method));	// public abstract int java.util.Map.size()
		
		out(invokable.isPublic());		// true
		out(invokable.isAbstract());	// true
		out(invokable.isAccessible());	// false
		out(invokable.isNative());		// false
		out(invokable.isFinal());
		out(invokable.isProtected());
		out(invokable.isStatic());
		out(invokable.isSynchronized());
		out(invokable.isSynthetic());
		out(invokable.isVarArgs());
		
		Map<String, String> map = Maps.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		out(invokable.invoke(map, new Object[] {}));	// size 2
		
		out(invokable.getName());
		out(invokable.getTypeParameters());
		out(invokable.getAnnotations());
		out(invokable.getDeclaredAnnotations());
		out(invokable.getDeclaringClass());
		out(invokable.getExceptionTypes());	// interface java.util.Map
		
		out(invokable.getModifiers());	// 1025
		out(invokable.getOwnerType());	// java.util.Map<java.lang.String, java.lang.String>
		out(invokable.getParameters());	// []
		out(invokable.getReturnType()); // int
	}
	
	@SuppressWarnings("serial")
	@Test
	public void test1() throws NoSuchMethodException, SecurityException, InvocationTargetException, IllegalAccessException {
		Method method = Map.class.getMethod("size");
		Invokable<Map<String, String>, ?> invokable = new TypeToken<Map<String, String>>(){}.method(method);

		
		// 方法是否是public的?
		// -------------------------------------------------------
		// JDK
		out(Modifier.isPublic(method.getModifiers())); // true
		
		// Invokable  
		out(invokable.isPublic());		// true
		
		
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
	}
	
	@Test
	@SuppressWarnings("serial")
	public void testNullable() throws NoSuchMethodException, SecurityException {
		Method method = Map.class.getMethod("containsKey", Object.class);
		Invokable<Map<String, String>, ?> invokable = new TypeToken<Map<String, String>>(){}.method(method);
		
		// 方法的第一个参数是否被定义了注解@Nullable？
		// -------------------------------------------------------
		// JDK
		for (Annotation annotation : method.getParameterAnnotations()[0]) {
		    if (annotation instanceof Nullable) {
		        out("yes");
		        break;
		    }
		}
		
		// Invokable
		out(invokable.getParameters().get(0).isAnnotationPresent(Nullable.class));
	}
	
	private void out(Object o) {
		System.out.println(o);
	}
}
