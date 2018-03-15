package com.cnblogs.hoojo.eventbus;

import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * 事件总线
 * @author hoojo
 * @createDate 2018年3月15日 上午11:22:57
 * @file EventBusTest.java
 * @package com.cnblogs.hoojo.eventbus
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 * 
 * 用途：Java的进程内事件分发都是通过发布者和订阅者之间的显式注册实现的。
 * 	设计EventBus就是为了取代这种显示注册方式，使组件间有了更好的解耦。
 * 	EventBus不是通用型的发布-订阅实现，不适用于进程间通信。
 * 

<table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="114">事件</td>
<td width="498">可以向事件总线发布的对象</td>
</tr>
<tr>
<td width="114">订阅</td>
<td width="498">向事件总线注册<i>监听者</i>以接受事件的行为</td>
</tr>
<tr>
<td width="114">监听者</td>
<td width="498">提供一个<i>处理方法</i>，希望接受和处理事件的对象</td>
</tr>
<tr>
<td width="114">处理方法</td>
<td width="498">监听者提供的公共方法，事件总线使用该方法向监听者发送事件；该方法应该用Subscribe注解</td>
</tr>
<tr>
<td width="114">发布消息</td>
<td width="498">通过事件总线向所有匹配的监听者提供事件</td>
</tr>
</tbody>
</table>
 * 
 */
public class EventBusTest {

	class EventBusExample {
		
		private EventBus eventBus;
		
		EventBusExample() {
			// 创建
			eventBus = new EventBus("event bus examples");
			
			// 注册
			eventBus.register(this);
		}
		
		public void pushlishMessage(int count) {
			for (int i = 1; i <= count; i++) {
				eventBus.post("publish message number " + i);
			}
		}
		
		@Subscribe
		public void handlerMessage(String message) {
			System.out.println(eventBus.identifier() + ", rev: " + message);
		}
		
		public void destory() {
			eventBus.unregister(this);
		}
	}
	
	@Test
	public void test1() {
		EventBusExample example = new EventBusExample();
		example.pushlishMessage(5);
		
		example.destory();
	}
}
