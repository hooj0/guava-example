package com.cnblogs.hoojo.eventbus;

import org.junit.Test;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

/**
 * <b>function:</b> 事件总线，消息分发
 * @author hoojo
 * @createDate 2018年3月27日 下午6:28:20
 * @file EventBusMessageTest.java
 * @package com.cnblogs.hoojo.eventbus
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class EventBusMessageTest {

	/** 事件监听者 */
	class EventBusMessageListeners {
		
		// 事件总线，管理和追踪监听者
		private EventBus eventBus;
		
		EventBusMessageListeners() {
			eventBus = new EventBus(new SubscriberExceptionHandler() {
				@Override
				public void handleException(Throwable exception, SubscriberExceptionContext context) {
					System.out.println("事件：" + context.getEvent() + ", Subscriber：" + context.getSubscriber() + ", SubscriberMethod：" + context.getSubscriberMethod());
					System.out.println("发生异常：" + exception.getMessage());
				}
			});
			
			/*** 订阅 ***/
			// 把事件监听者注册到事件生产者
			// 事件生产者和监听者共享相同的EventBus实例
			// 注册实例到事件总线
			eventBus.register(this);
		}
		
		/*********************处理方法********************/
		// 事件监听者，处理触发事件后的业务
		// 监听特定事件
		@Subscribe
		public void handlerMessage(MessageEvent event) {
			System.out.println("handlerMessage: " + event.getEvent() + ": " + event.getMessage());
		}
		
		// 监听Error 类型事件
		@Subscribe
		public void handlerErrorMessage(ErrorMessageEvent event) {
			System.out.println("handlerErrorMessage: " + event.getEvent() + ": " + event.getMessage());
		}
		
		// 监听 Info类型事件
		@Subscribe
		public void handlerInfoMessage(InfoMessageEvent event) {
			System.out.println("handlerErrorMessage: " + event.getEvent() + ": " + event.getMessage());
		}
		
		@Subscribe
		public void handerDeadEvent(DeadEvent deadEvent) {
			System.out.println("unknow event: " + deadEvent.getEvent() + ", source: " + deadEvent.getSource());
		}

		public EventBus getEventBus() {
			return eventBus;
		}
	}
	
	/** 定义事件 */
	public class MessageEvent {
		
		private String message;
		private String event;

		public MessageEvent(String message, String event) {
			this.message = message;
			this.event = event;
		}
		
		public String getMessage() {
			return message;
		}

		public String getEvent() {
			return event;
		}
	}
	
	/** 对于继承的事件对象，如果存在对父类的监听，同样会触发父类接口的事件业务 */
	class ErrorMessageEvent extends MessageEvent {

		public ErrorMessageEvent(String message) {
			super(message, "Error消息事件机制");
		}
	}
	
	class InfoMessageEvent extends MessageEvent {

		public InfoMessageEvent(String message) {
			super(message, "info消息事件机制");
		}
	}
	
	/**
	 * 事件生产者的“事件生产方法”，处理发布事件或触发事件的动作
	 */
	class EventBusMessageHandler {
		private EventBus eventBus;
		
		public EventBusMessageHandler(EventBus eventBus) {
			this.eventBus = eventBus;
		}
		
		/***********************发布消息，分发事件************************/
		// 向监听者分发事件；处理发布事件或触发事件的动作
		public void sendMessage(String message) {
			eventBus.post(new MessageEvent(message, "消息事件总线"));
		}

		public void sendInfoMessage(String info) {
			eventBus.post(new InfoMessageEvent(info));
		}
		
		public void sendErrorMessage(String error) {
			if (error == null) {
				eventBus.post(error);
			} else {
				eventBus.post(new ErrorMessageEvent(error));
			}
		}
		
		// 向监听者分发事件
		public void sendMessage() {
			eventBus.post("不带特定监听的消息事件总线");
		}
	}
	
	@Test
	public void test1() {
		
		EventBusMessageListeners listeners = new EventBusMessageListeners();
		EventBusMessageHandler handler = new EventBusMessageHandler(listeners.getEventBus());
		
		//handler.sendMessage();
		handler.sendErrorMessage("发送error消息");
		//handler.sendInfoMessage("发送info消息");
		//handler.sendMessage("发送普通消息");
	}
	 
}
