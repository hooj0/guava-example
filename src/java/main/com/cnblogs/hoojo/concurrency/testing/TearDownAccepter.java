package com.cnblogs.hoojo.concurrency.testing;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2017年11月24日 下午4:22:41
 * @file TearDownAccepter.java
 * @package com.cnblogs.hoojo.concurrency.service.execution
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Beta
@GwtCompatible
public interface TearDownAccepter {
  /**
   * Registers a TearDown implementor which will be run after the test proper.
   *
   * <p>In JUnit4 language, that means as an {@code @After}.
   *
   * <p>In JUnit3 language, that means during the
   * {@link junit.framework.TestCase#tearDown()} step.
   */
  void addTearDown(TearDown tearDown);
}
