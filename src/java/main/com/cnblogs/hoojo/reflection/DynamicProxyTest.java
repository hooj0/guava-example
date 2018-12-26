package com.cnblogs.hoojo.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Test;

import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;

/**
 * 动态代理
 * 实用方法Reflection.newProxy(Class, InvocationHandler)是一种更安全，更方便的API，
 * 它只有一个单一的接口类型需要被代理来创建Java动态代理时。
 * @author hoojo
 * @createDate 2018年12月26日 下午4:58:41
 * @file DynamicProxyTest.java
 * @package com.cnblogs.hoojo.reflection
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class DynamicProxyTest {

	interface Foo {
		
	}
	
	@Test
	public void test1() {
		// JDK 代理
		InvocationHandler invocationHandler = new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return null;
			}
		};
		
		Foo foo = (Foo) Proxy.newProxyInstance(
				Foo.class.getClassLoader(),
				new Class<?>[] { Foo.class },
				invocationHandler);
		out(foo);
		
		// Reflection
		foo = Reflection.newProxy(Foo.class, invocationHandler);
		out(foo);
		
		// AbstractInvocationHandler
		// 动态代理能够更直观的支持equals()，hashCode()和toString()，那就是：
		// 一个代理实例equal另外一个代理实例，只要他们有同样的接口类型和equal的invocation handlers。
		// 一个代理实例的toString()会被代理到invocation handler的toString()，这样更容易自定义。
		AbstractInvocationHandler handler = new AbstractInvocationHandler() {
			// 确保传递给handleInvocation(Object, Method, Object[]))的参数数组永远不会空
			protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
				return null;
			}
		};
		
		out(handler);
	}
	
	private void out(Object o) {
		System.out.println(o);
	}
}
