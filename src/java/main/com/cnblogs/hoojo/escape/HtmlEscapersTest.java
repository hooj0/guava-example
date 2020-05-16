package com.cnblogs.hoojo.escape;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.html.HtmlEscapers;
import org.junit.Test;

/**
 * html字符转义
 *
 * @author hoojo
 * @version 1.0
 * @date 2022/02/10 17:11:00
 */
public class HtmlEscapersTest extends BasedTest {

    @Test
    public void test() {
        out(HtmlEscapers.htmlEscaper().escape("<html>ha</html>")); // &lt;html&gt;ha&lt;/html&gt;

        out(HtmlEscapers.htmlEscaper().escape("xxx")); // xxx
        out(HtmlEscapers.htmlEscaper().escape("\"test\"")); // &quot;test&quot;
        out(HtmlEscapers.htmlEscaper().escape("\'test'"));  // &#39;test&#39;
        out(HtmlEscapers.htmlEscaper().escape("test & test & test")); // test &amp; test &amp; test
        out(HtmlEscapers.htmlEscaper().escape("test << 1")); // test &lt;&lt; 1
        out(HtmlEscapers.htmlEscaper().escape("test >> 1")); // test &gt;&gt; 1
        out(HtmlEscapers.htmlEscaper().escape("<tab>"));    // &lt;tab&gt;

        // Test simple escape of '&'.
        out(HtmlEscapers.htmlEscaper().escape("foo&bar")); // foo&amp;bar

        String s = "blah blah farhvergnugen";
        out(HtmlEscapers.htmlEscaper().escape(s)); // blah blah farhvergnugen

        // Tests escapes at begin and end of string.
        out(HtmlEscapers.htmlEscaper().escape("<p>")); // &lt;p&gt;

        // Test all escapes.
        out(HtmlEscapers.htmlEscaper().escape("a\"b<c>d&")); // a&quot;b&lt;c&gt;d&amp;

        // Test two escapes in a row.
        out(HtmlEscapers.htmlEscaper().escape("foo&&bar")); // "foo&amp;&amp;bar"

        // Test many non-escaped characters.
        s = "!@#$%^*()_+=-/?\\|]}[{,.;:" + "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" + "1234567890";
        out(HtmlEscapers.htmlEscaper().escape(s)); // !@#$%^*()_+=-/?\|]}[{,.;:abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890
    }
}
