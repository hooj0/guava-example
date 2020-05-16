package com.cnblogs.hoojo.escape;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.escape.CharEscaper;
import com.google.common.xml.XmlEscapers;
import org.junit.Test;

/**
 * xml 转义
 *
 * @author hoojo
 * @version 1.0
 * @date 2022/02/16 17:38:13
 */
public class XmlEscapersTest extends BasedTest {

    @Test
    public void test() {
        CharEscaper xmlContentEscaper = (CharEscaper) XmlEscapers.xmlContentEscaper();

        out(xmlContentEscaper.escape("\"test\""));  // "test"
        out(xmlContentEscaper.escape("'test'"));    // 'test'

        CharEscaper xmlAttributeEscaper = (CharEscaper) XmlEscapers.xmlAttributeEscaper();
        out(xmlAttributeEscaper.escape("\"test\"")); // &quot;test&quot;
        out(xmlAttributeEscaper.escape("\'test'"));  // &apos;test&apos;
        out(xmlAttributeEscaper.escape("a\"b<c>d&e\"f'")); // a&quot;b&lt;c&gt;d&amp;e&quot;f&apos;

        out(xmlAttributeEscaper.escape("a\tb\nc\rd")); // a&#x9;b&#xA;c&#xD;d
    }
}
