package com.cnblogs.hoojo.networking;

import org.junit.Test;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.net.InternetDomainName;

/**
 * 网络域名链接测试
 * InternetDomainName是一个用于解析和操作域名的有用工具。它可以用作验证器，组件提取器，以及用于以类型安全的方式传递域名的值类型。
 * 
 * @author hoojo
 * @createDate 2019年1月2日 下午4:24:54
 * @file NetworkingTest.java
 * @package com.cnblogs.hoojo.networking
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class NetworkingTest extends BasedTest {

	/*
	==========================================================================================================
	InternetDomainName	publicSuffix()	topPrivateDomain()	registrySuffix()	topDomainUnderRegistrySuffix()
	==========================================================================================================
	google.com			com				google.com			com					google.com
	----------------------------------------------------------------------------------------------------------
	co.uk				co.uk			N/A					co.uk				N/A
	----------------------------------------------------------------------------------------------------------
	www.google.co.uk	co.uk			google.co.uk		co.uk				google.co.uk
	----------------------------------------------------------------------------------------------------------
	foo.blogspot.com	blogspot.com	foo.blogspot.com	com					blogspot.com
	----------------------------------------------------------------------------------------------------------
	google.invalid		N/A				N/A					N/A					N/A
	----------------------------------------------------------------------------------------------------------
	*/
	@Test
	public void testInternetDomainName() {
		InternetDomainName owner = InternetDomainName.from("mail.google.com");
		out(owner); // mail.google.com
		out(owner.topPrivateDomain()); // google.com
		
		out(owner.hasParent()); // true
		out(owner.hasPublicSuffix()); // true
		out(owner.isPublicSuffix()); // false
		out(owner.isTopPrivateDomain()); // false
		out(owner.isUnderPublicSuffix()); // true
		
		out(owner.parent()); // google.com
		out(owner.publicSuffix()); // com
		out(owner.registrySuffix()); // com
		
		out(owner.isRegistrySuffix()); // false
		out(owner.isTopDomainUnderRegistrySuffix()); // false
		
		// -----------------------------------------------------------
		out("\n");
		owner = InternetDomainName.from("mail.google.com").topPrivateDomain();
		out(owner); // google.com
		out(owner.topPrivateDomain()); // google.com
		
		out(owner.hasParent()); // true
		out(owner.hasPublicSuffix()); // true
		out(owner.isPublicSuffix()); // false
		out(owner.isTopPrivateDomain()); // true
		out(owner.isUnderPublicSuffix()); // true
		
		out(owner.parent()); // com
		out(owner.publicSuffix()); // com
		out(owner.registrySuffix()); // com
		
		out(owner.isRegistrySuffix()); // false
		out(owner.isTopDomainUnderRegistrySuffix()); // false
		
		// -----------------------------------------------------------
		out("\n");
		owner = InternetDomainName.from("foo.blogspot.com").topDomainUnderRegistrySuffix();
		out(owner); // google.com
		
		out(owner.hasParent()); // true
		out(owner.hasPublicSuffix()); // true
		out(owner.isPublicSuffix()); // false
		out(owner.isTopPrivateDomain()); // true
		out(owner.isUnderPublicSuffix()); // true
		
		out(owner.parent()); // com
		out(owner.publicSuffix()); // com
		out(owner.registrySuffix()); // com
		
		out(owner.isRegistrySuffix()); // false
		out(owner.isTopDomainUnderRegistrySuffix()); // false
	}
}
