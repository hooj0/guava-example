package com.cnblogs.hoojo.concurrency;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2017年11月13日 下午4:51:55
 * @file ApplicationTest.java
 * @package com.cnblogs.hoojo.concurrency
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ApplicationTest {

	@SuppressWarnings("unused")
	@Test
	public void test1() {
		
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		ListeningExecutorService service = MoreExecutors.listeningDecorator(executorService);
		
		ListenableFuture<Integer> input = service.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				Thread.sleep(1000 * 5);
				
				Long now = System.currentTimeMillis();
				if (now % 2 == 0) {
					return now.intValue();
				}
				
				throw new RuntimeException("exception");
			}
		});
		
		// 转换类型
		ListenableFuture<String> result = Futures.transform(input, new Function<Number, String>() {
			@Override
			public String apply(Number input) {
				return input + "_" + input.intValue();
			}
		}, executorService);
		
		// 异步
		ListenableFuture<String> result2 = Futures.transformAsync(input, new AsyncFunction<Number, String>() {
			@Override
			public ListenableFuture<String> apply(Number input) throws Exception {
				
				return service.submit(new Callable<String>() {
					@Override
					public String call() throws Exception {
						return "String";
					}
				});
			}
		}, executorService);
		
		ListenableFuture<Integer> futures = service.submit(() -> {
			return 0;
		});
		// 转换list
		// 返回一个ListenableFuture，该ListenableFuture 返回的result是一个List，
		// List中的值是每个ListenableFuture的返回值，假如传入的其中之一fails或者cancel，这个Future fails 或者canceled
		ListenableFuture<List<Integer>> list = Futures.allAsList(futures);
		
		// 返回成功的任务future
		// 返回一个ListenableFuture ，该Future的结果包含所有成功的Future，按照原来的顺序，当其中之一Failed或者cancel，则用null替代
		ListenableFuture<List<Integer>> lists = Futures.successfulAsList(futures);
		
		// 过滤
		/*Futures.makeChecked(futures, new Function<Exception, String>() {
			@Override
			public String apply(Exception input) {
				// TODO Auto-generated method stub
				return null;
			}
		});*/
	}
}
