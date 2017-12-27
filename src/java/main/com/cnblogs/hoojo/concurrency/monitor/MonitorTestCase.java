package com.cnblogs.hoojo.concurrency.monitor;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.util.concurrent.Monitor;

/**
Monitor有几个常用的方法

    enter()：进入到当前Monitor，无限期阻塞。
	enter(long time, TimeUnit unit)：进入到当前Monitor，最多阻塞给定的时间，返回是否进入Monitor。

	enterInterruptibly()：进入到当前Monitor，无限期阻塞，但可能会被打断。
	enterInterruptibly(long time, TimeUnit unit)：进入到当前Monitor，最多阻塞给定的时间，但可能会被打断，返回是否进入Monitor。

	tryEnter()：如果可以的话立即进入Monitor，不阻塞，返回是否进入Monitor。
	tryEnterIf(Guard guard)：如果Guard的isSatisfied()为true并且可以的话立即进入Monitor，不等待获取锁，也不等待Guard satisfied。

	enterWhen(Guard guard)：当Guard的isSatisfied()为true时，进入当前Monitor，无限期阻塞，但可能会被打断。
	enterWhen(Guard guard, long time, TimeUnit unit)：当Guard的isSatisfied()为true时，进入当前Monitor，最多阻塞给定的时间，这个时间包括获取锁的时间和等待Guard satisfied的时间，但可能会被打断。
	enterWhenUninterruptibly(Guard guard)：当Guard的isSatisfied()为true时，进入当前Monitor，无限期阻塞。
	enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit)：当Guard的isSatisfied()为true时，进入当前Monitor，最多阻塞给定的时间，这个时间包括获取锁的时间和等待Guard satisfied的时间。

	enterIf(Guard guard)：如果Guard的isSatisfied()为true，进入当前Monitor，无限期的获得锁，不需要等待Guard satisfied。
	enterIf(Guard guard, long time, TimeUnit unit)：如果Guard的isSatisfied()为true，进入当前Monitor，在给定的时间内持有锁，不需要等待Guard satisfied。
	
	enterIfInterruptibly(Guard guard)：如果Guard的isSatisfied()为true，进入当前Monitor，无限期的获得锁，不需要等待Guard satisfied，但可能会被打断。
	enterIfInterruptibly(Guard guard, long time, TimeUnit unit)：如果Guard的isSatisfied()为true，进入当前Monitor，在给定的时间内持有锁，不需要等待Guard satisfied，但可能会被打断。

	waitFor(Guard guard)：等待Guard satisfied，无限期等待，但可能会被打断，当一个线程当前占有Monitor时，该方法才可能被调用。
	waitFor(Guard guard, long time, TimeUnit unit)：等待Guard satisfied，在给定的时间内等待，但可能会被打断，当一个线程当前占有Monitor时，该方法才可能被调用。
	waitForUninterruptibly(Guard guard)：等待Guard satisfied，无限期等待，当一个线程当前占有Monitor时，该方法才可能被调用。
	waitForUninterruptibly(Guard guard, long time, TimeUnit unit)：等待Guard satisfied，在给定的时间内等待，当一个线程当前占有Monitor时，该方法才可能被调用。

	leave()：离开当前Monitor，当一个线程当前占有Monitor时，该方法才可能被调用。
	
	isFair()：判断当前Monitor是否使用一个公平的排序策略。
	isOccupied()：返回当前Monitor是否被任何线程占有，此方法适用于检测系统状态，不适用于同步控制。
	isOccupiedByCurrentThread()：返回当前线程是否占有当前Monitor。
	getOccupiedDepth()：返回当前线程进入Monitor的次数，如果房前线程不占有Monitor，返回0。
	getQueueLength()：返回一个估计的等待进入Monitor的线程数量，只是一个估算值，因为线程的数量在这个方法访问那不数据结构的时候可能会动态改变。此方法适用于检测系统状态，不适用于同步控制。
	getWaitQueueLength(Guard guard)：返回一个等待给定Guard satisfied的线程估计数量， 注意，因为超时和中断可能发生在任何时候，所以估计只作为一个等待线程的实际数目的上限。此方法适用于检测系统状态，不适用于同步控制。
	hasQueuedThreads()：返回是否有任何线程正在等待进入这个Monitor，注意，因为取消随时可能发生，所以返回true并不保证任何其他线程会进入这个Monitor。此方法设计用来检测系统状态。
	hasQueuedThread(Thread thread)：返回给定线程是否正在等待进入这个Monitor，注意，因为取消随时可能发生，所以返回true并不保证给定线程会进入这个Monitor。此方法设计用来检测系统状态。
	hasWaiters(Guard guard)：返回是否有任何线程正在等待给定Guard satisfied，注意，因为取消随时可能发生，所以返回true并不保证未来Guard变成satisfied时唤醒任意线程。此方法设计用来检测系统状态。
 * 
 * 多线程并发测试
 * @author hoojo
 * @createDate 2018年9月24日 下午5:14:52
 * @file MonitorTestCase.java
 * @package com.cnblogs.hoojo.concurrency.monitor
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MonitorTestCase {

	private class MonitorTest<V> {
		private V value;
		
		// fair true参数表示资源公平竞争；false 表示非公平，速度效率快
		private final Monitor monitor = new Monitor();

		private final Monitor.Guard valuePresent = new Monitor.Guard(monitor) {
			public boolean isSatisfied() {
				System.out.println("valuePresent.isSatisfied");
				return value != null;
				// return false; // 返回false 就会一直阻塞，直到返回true
			}
		};
		
		private final Monitor.Guard valueAbsent = new Monitor.Guard(monitor) {
			public boolean isSatisfied() {
				System.out.println("valueAbsent.isSatisfied");
				return value == null;
			}
		};

		public V get() throws InterruptedException {
			monitor.enterWhen(valuePresent); // 一直阻塞，直到返回 true
			try {
				V result = value;
				value = null;
				return result;
			} finally {
				monitor.leave();
			}
		}

		public void set(V newValue) throws InterruptedException {
			monitor.enterWhen(valueAbsent);
			try {
				value = newValue;
			} finally {
				monitor.leave();
			}
		}
		
		public void set2(V newValue) throws InterruptedException {
			if (monitor.enterIf(valueAbsent)) { // 不阻塞！等候获取锁！ 如果为true立即进入，否则就执行false
				System.out.println("set2.....");
				
				try {
					value = newValue;
				} finally {
					monitor.leave();
				}
			} else {
				throw new InterruptedException("无法进入set");
			}
		}
		
		public V get2() throws InterruptedException {
			if (monitor.enterIf(valuePresent)) {
				System.out.println("get2.....");
				
				try {
					V result = value;
					value = null;
					return result;
				} finally {
					monitor.leave();
				}
			} else {
				throw new InterruptedException("无法进入get");
			}
			
		}
		
		public void set3(V newValue) throws InterruptedException {
			if (monitor.tryEnterIf(valueAbsent)) { // 不阻塞！ 如果为true立即进入，否则就执行false
				System.out.println("set2.....");
				
				try {
					value = newValue;
				} finally {
					monitor.leave();
				}
			} else {
				throw new InterruptedException("无法进入set");
			}
		}
		
		public V get3() throws InterruptedException {
			if (monitor.tryEnterIf(valuePresent)) {
				System.out.println("get2.....");
				
				try {
					V result = value;
					value = null;
					return result;
				} finally {
					monitor.leave();
				}
			} else {
				throw new InterruptedException("无法进入get");
			}
		}
	}
	
	@Test
	public void test1() {
		MonitorTest<String> test = new MonitorTest<>();
		try {
			
			Thread t = new Thread(() -> {
				try {
					System.out.println("get....");
					// 被阻塞，直到set方法执行完成
					System.out.println("get: " + test.get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			t.start();
			
			// 等待 上面线程被执行
			Thread.sleep(1000);
			System.out.println("sleep...");
			Thread.sleep(1000);
			
			// 执行set，上面的get不再阻塞
			System.out.println("set...");
			test.set("test");
			
			// 当设置 Monitor(false)， 非公平竞争，下面的代码被优秀抢占资源执行任务
			// 当设置 Monitor(true)， 公平竞争，下面的代码没有被执行任务，get1 被执行
			System.out.println("get2: " + test.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		
		MonitorTest<String> test = new MonitorTest<>();
		try {
			
			Thread t = new Thread(() -> {
				try {
					System.out.println("get....");
					// 无法进入被阻塞，抛出异常 InterruptedException: 无法进入get
					System.out.println("get: " + test.get2());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			t.start();
			
			// 等待 上面线程被执行
			Thread.sleep(1000);
			System.out.println("sleep...");
			Thread.sleep(1000);
			
			// 执行set
			System.out.println("set...");
			test.set2("test");
			
			// 再次get 获取数据
			System.out.println("get2: " + test.get2());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test3() {
		
		MonitorTest<String> test = new MonitorTest<>();
		try {
			
			Thread t = new Thread(() -> {
				try {
					System.out.println("get....");
					// 无法进入被阻塞，抛出异常 InterruptedException: 无法进入get
					System.out.println("get: " + test.get3());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			t.start();
			
			// 等待 上面线程被执行
			Thread.sleep(1000);
			System.out.println("sleep...");
			Thread.sleep(1000);
			
			// 执行set
			System.out.println("set...");
			test.set3("test");
			
			// 再次get 获取数据
			System.out.println("get2: " + test.get3());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 非安全的并发操作，变量被重写并发
	 */
	@Test
	public void test_1() throws InterruptedException {
		
		class Value {
			private int value;

			public int add() {
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
						Thread.sleep(200);
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
		
		Thread.sleep(100 * 1000);
	}
	
	/**
	 * 线程安全：无限期阻塞，等待锁
	 * 模拟两个线程访问同一个资源，进行monitor.enter() 锁操作，保证共享资源安全并发
	 */
	@Test
	public void test_2() throws InterruptedException {
		
		Monitor monitor = new Monitor();
		
		class Value {
			private int value;

			public int add() {
				monitor.enter(); // 一直锁定，等待阻塞
				try {
					this.value++;
					return this.value;
				} finally {
					monitor.leave();
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
						Thread.sleep(200);
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
		
		Thread.sleep(100 * 1000);
	}
	
	/**
	 * 线程安全：阻塞给定的时间，等待锁
	 * add() 方法进行锁操作，monitor.enter(600, TimeUnit.MILLISECONDS);
	 * 在指定时间内没有得到操作权，将抛出异常释放资源；
	 * 其他线程将获取锁继续执行
	 */
	@Test
	public void test_3() throws InterruptedException {
		
		Monitor monitor = new Monitor();
		
		class Value {
			private int value;

			public int add() {
				monitor.enter(600, TimeUnit.MILLISECONDS); // 一直锁定，等待阻塞，时间到达后会抛出异常释放资源
				try {
					this.value++;
					if (this.value == 10) {
						Thread.sleep(1000L * 3);
					}
					return this.value;
				} catch (InterruptedException e) {
					e.printStackTrace();
					return 0;
				} finally {
					monitor.leave();
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
						Thread.sleep(200);
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
		
		Thread.sleep(100 * 1000);
	}
	
	/**
	 * enterIf(Guard guard)
	 * 线程安全：等待获得锁，不阻塞
	 * 如果Guard的isSatisfied()为true，进入当前Monitor。等待获得锁，不需要等待Guard satisfied。不阻塞
	 */
	@Test
	public void test_4() throws InterruptedException {
		
		Monitor monitor = new Monitor();
		Monitor.Guard present = new Monitor.Guard(monitor) {
			public boolean isSatisfied() {
				return System.currentTimeMillis() % 2 == 0;
			}
		};
		
		class Value {
			private int value;

			public int add() {
				// 如果Guard的isSatisfied()为true，进入当前Monitor。等待获得锁，不需要等待Guard satisfied。不阻塞
				if (monitor.enterIf(present)) {
					try {
						this.value++;
						return this.value;
					} finally {
						monitor.leave();
					}
				} else {
					return 0;
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
						System.out.println(this.getName() + ": " + i + "-> " + value.add());
						Thread.sleep(200);
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
		
		Thread.sleep(100 * 1000);
	}
	
	/**
	 * 线程安全：不阻塞，在指定时间内未完成就释放锁（在给定的时间内持有锁）
	 * 如果Guard的isSatisfied()为true，进入当前Monitor，在给定的时间内持有锁，不需要等待Guard satisfied。
	 */
	@Test
	public void test_5() throws InterruptedException {
		
		Monitor monitor = new Monitor();
		Monitor.Guard present = new Monitor.Guard(monitor) {
			public boolean isSatisfied() {
				return true;
			}
		};
		
		class Value {
			private int value;

			public int add() {
				
				if (monitor.enterIf(present, 10, TimeUnit.MILLISECONDS)) {
					try {
						this.value++;
						if (this.value >= 5 && this.value <= 10) {
							Thread.sleep(1000L * 3);
						}
						return this.value;
					} catch (InterruptedException e) {
						e.printStackTrace();
						return 0;
					} finally {
						monitor.leave();
					}
				} else {
					return 0;
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
						Thread.sleep(200);
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
		
		Thread.sleep(100 * 1000);
	}
	
	/**
	 * 线程安全：无限阻塞，但可能中断
	 * 当前线程长时间占用资源，线程将无限等待；
	 * 当前线程若发生异常将中断当前线程；
	 * 当Guard的isSatisfied()为true时，进入当前Monitor，无限期阻塞，但可能会被打断。
	 */
	@Test
	public void test_6() throws InterruptedException {
		
		Monitor monitor = new Monitor();
		Monitor.Guard present = new Monitor.Guard(monitor) {
			public boolean isSatisfied() {
				return true;
			}
		};
		
		class Value {
			private int value;

			public int add() throws InterruptedException {
				monitor.enterWhen(present);
				try {
					this.value++;
					if (this.value == 10) {
						Thread.sleep(5000L); // 阻塞
						throw new RuntimeException(); // 异常中断
					}
					return this.value;
				} finally {
					monitor.leave();
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
						Thread.sleep(200);
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
		
		Thread.sleep(100 * 1000);
	}
	
	/**
	 * 线程安全，不阻塞
	 * 如果Guard的isSatisfied()为true并且可以的话立即进入Monitor，不等待获取锁，也不等待Guard satisfied。
	 * 
	 * 如果发现资源被锁定，立即执行else操作业务
	 * 如果发现获取资源锁，立即执行
	 */
	@Test
	public void test_7() throws InterruptedException {
		
		Monitor monitor = new Monitor();
		Monitor.Guard present = new Monitor.Guard(monitor) {
			public boolean isSatisfied() {
				return true;
			}
		};
		
		class Value {
			private int value;
			
			public int add() throws InterruptedException {
				if (monitor.tryEnterIf(present)) { // 如果可以的话立即进入Monitor，不阻塞，返回是否进入Monitor。
					try {
						this.value++;
						return this.value;
					} finally {
						monitor.leave();
					}
				} else {
					return 0;
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
						System.out.println(this.getName() + ": " + i + "->" + value.add());
						Thread.sleep(200);
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
		
		Thread.sleep(100 * 1000);
	}
	
	/**
	 * 线程安全：无限阻塞，异常中断
	 * 
	 * 进入到当前Monitor，无限期阻塞，但可能会被打断。
	 */
	@Test
	public void test_8() throws InterruptedException {
		
		Monitor monitor = new Monitor();
		class Value {
			private int value;

			public int add() throws InterruptedException {
				monitor.enterInterruptibly(); // 未获得锁直接异常
				try {
					this.value++;
					if (this.value == 10) {
						throw new RuntimeException();
					}
					return this.value;
				} finally {
					monitor.leave();
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
						Thread.sleep(200);
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
		
		Thread.sleep(100 * 1000);
	}
	
	/**
	 * 等待Guard satisfied，在给定的时间内等待，但可能会被打断，当一个线程当前占有Monitor时，该方法才可能被调用。
	 */
	@Test
	public void test_9() throws InterruptedException {
		
		Monitor monitor = new Monitor();
		Monitor.Guard present = new Monitor.Guard(monitor) {
			public boolean isSatisfied() {
				return false;
			}
		};
		
		class Value {
			private int value;

			public int add() throws InterruptedException {
				monitor.enter();
				try {
					// 等待Guard satisfied，在给定的时间内等待，但可能会被打断，当一个线程当前占有Monitor时，该方法才可能被调用。
					monitor.waitFor(present, 1000, TimeUnit.MILLISECONDS);
					this.value++;
					return this.value;
				} finally {
					monitor.leave();
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
						Thread.sleep(200);
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
		
		Thread.sleep(100 * 1000);
	}
}
