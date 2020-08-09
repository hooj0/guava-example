package com.cnblogs.hoojo.checked;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.base.Verify;
import org.junit.Test;

/**
 * 断言、验证测试
 *
 * @author hoojo
 * @version 1.0
 * @date 2022/01/25 16:00:50
 */
@SuppressWarnings("ALL")
public class VerifyTest extends BasedTest {

    @Test
    public void testVerify() {
        Verify.verify(true); // success

        try {
            Verify.verify(false);
        } catch (Exception e) {
            out(e); // com.google.common.base.VerifyException
        }

        try {
            Verify.verify(false, "valid failure %s", "!!!");
        } catch (Exception e) {
            out(e); // com.google.common.base.VerifyException: valid failure !!!
        }

        try {
            String failure = Verify.verifyNotNull(null, "[%s] param is null", "user");
        } catch (Exception e) {
            out(e); // com.google.common.base.VerifyException: [user] param is null
        }
    }
}
