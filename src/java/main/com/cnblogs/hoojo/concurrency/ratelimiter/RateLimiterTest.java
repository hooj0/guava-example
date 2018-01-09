package com.cnblogs.hoojo.concurrency.ratelimiter;

import org.junit.Test;

import com.google.common.util.concurrent.RateLimiter;

/**
 * http://ifeve.com/guava-ratelimiter/
 * 
 * RateLimiter 类与Java api中的semaphore信号量比较类似，
 * 主要用于限制对资源并发访问的线程数，RateLimiter类限制线程访问的时间，
 * 这就意味着可以限制每 秒中线程访问资源的数量。
	
	create(double permitsPerSecond)：创建具有指定稳定吞吐量的RateLimiter类，传入允许每秒提交的任务数量。
	create(double permitsPerSecond, long warmupPeriod, TimeUnit unit)：创建具有指定稳定吞吐量的RateLimiter类，传入允许每秒提交的任务数量和准备阶段的时间，在这段时间RateLimiter会有个缓冲，直到达到它的最大速率（只要有饱和的足够的请求）。
	
	setRate(double permitsPerSecond)：稳定的更新RateLimiter的速率，RateLimiter的构造方法中中设置permitsPerSecond参数，调用这个方法后，当前阻塞的线程不会被唤醒，因此它们不会观察到新的速率被设置。
	getRate()：返回RateLimiter被设置的稳定的速率值。
	
	acquire()：从这个ratelimiter获得一个许可，阻塞线程直到请求可以再授予许可。
	acquire(int permits)：获取传入数量的许可，阻塞线程直到请求可以再授予许可。
	
	tryAcquire(long timeout, TimeUnit unit)：判断是否可以在指定的时间内从ratelimiter获得一个许可，或者在超时期间内未获得许可的话，立即返回false。
	tryAcquire(int permits)：判断是否可以立即获取相应数量的许可。
	tryAcquire()：判断是否可以立即获取许可。
	tryAcquire(int permits, long timeout, TimeUnit unit)：判断是否可以在超时时间内获取相应数量的许可。
 * 
 * 多线程并发漏斗测试
 * @author hoojo
 * @createDate 2018年9月24日 下午5:16:15
 * @file RateLimiterTest.java
 * @package com.cnblogs.hoojo.concurrency.ratelimiter
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class RateLimiterTest {

	/**
	 * 创建具有指定稳定吞吐量的RateLimiter类，传入允许每秒提交的任务数量。
	 * acquire 会控制该代码后面的代码库每次进入线程并发的次数
	 */
	@Test
	public void test1() {
		// 每秒钟只能进入一个线程操作
		RateLimiter limiter = RateLimiter.create(2);
		
		class Value {
			private int value;

			public int add() {
				
				System.out.println("进入数量：" + limiter.acquire());
				this.value++;
				return this.value;
			}
		}
		
		class MonitorTest4 extends Thread {
			private Value value;
			
			public MonitorTest4(String name, Value value) {
				super(name);
				this.value = value;
			}
			
			@Override
			public void run() {
				try {
					for (int i = 0; i < 50; i++) {
						System.out.println(this.getName() + ": " + value.add());
						Thread.sleep(300);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		Value val = new Value();
		
		MonitorTest4 test = new MonitorTest4("线程1", val);
		test.start();
		
		MonitorTest4 test2 = new MonitorTest4("线程2", val);
		test2.start();
		
		MonitorTest4 test3 = new MonitorTest4("线程3", val);
		test3.start();
		
		MonitorTest4 test4 = new MonitorTest4("线程4", val);
		test4.start();
		
		try {
			Thread.sleep(100 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断是否可以进入，不能可以做其他业务操作
	 */
	@Test
	public void test2() {
		// 每秒钟只能进入一个线程操作
		RateLimiter limiter = RateLimiter.create(3);
		
		class Value {
			private int value;

			public int add() {
				
				if (limiter.tryAcquire()) { // 如果可以进入
					this.value++;
					return this.value;
				} else { // 不能进入后的业务
					return -1;
				}
			}
		}
		
		class MonitorTest4 extends Thread {
			private Value value;
			
			public MonitorTest4(String name, Value value) {
				super(name);
				this.value = value;
			}
			
			@Override
			public void run() {
				try {
					for (int i = 0; i < 50; i++) {
						System.out.println(this.getName() + ": " + value.add());
						Thread.sleep(800);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		Value val = new Value();
		
		MonitorTest4 test = new MonitorTest4("线程1", val);
		test.start();
		
		MonitorTest4 test2 = new MonitorTest4("线程2", val);
		test2.start();
		
		MonitorTest4 test3 = new MonitorTest4("线程3", val);
		test3.start();
		
		MonitorTest4 test4 = new MonitorTest4("线程4", val);
		test4.start();
		
		try {
			Thread.sleep(100 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void test3() {
		// 每秒钟只能进入一个线程操作
		RateLimiter limiter = RateLimiter.create(5);
		
		class Value {
			private int value;

			public int add() {
				
				System.out.println("进入数量：" + limiter.acquire(2));
				this.value++;
				return this.value;
			}
		}
		
		class MonitorTest4 extends Thread {
			private Value value;
			
			public MonitorTest4(String name, Value value) {
				super(name);
				this.value = value;
			}
			
			@Override
			public void run() {
				try {
					for (int i = 0; i < 50; i++) {
						System.out.println(this.getName() + ": " + value.add());
						Thread.sleep(300);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		Value val = new Value();
		
		MonitorTest4 test = new MonitorTest4("线程1", val);
		test.start();
		
		MonitorTest4 test2 = new MonitorTest4("线程2", val);
		test2.start();
		
		MonitorTest4 test3 = new MonitorTest4("线程3", val);
		test3.start();
		
		MonitorTest4 test4 = new MonitorTest4("线程4", val);
		test4.start();
		
		try {
			Thread.sleep(100 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
