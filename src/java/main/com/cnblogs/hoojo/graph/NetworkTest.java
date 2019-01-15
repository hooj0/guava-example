package com.cnblogs.hoojo.graph;

import java.util.Set;

import org.junit.Test;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;

/**
 * network graph test example
 * @author hoojo
 * @createDate 2019年1月15日 下午5:53:20
 * @file NetworkTest.java
 * @package com.cnblogs.hoojo.graph
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class NetworkTest extends BasedTest {

	@Test
	public void testDefined() {
		// 构建有向图
		MutableNetwork<Integer, String> network = NetworkBuilder.directed().build();
		// 添加节点
		network.addNode(1);
		out(network); // isDirected: true, allowsParallelEdges: false, allowsSelfLoops: false, nodes: [1], edges: {}

		network.addEdge(2, 3, "2->3");  // 如果尚未存在，还会添加节点2和3
		out(network); // isDirected: true, allowsParallelEdges: false, allowsSelfLoops: false, nodes: [1, 2, 3], edges: {2->3=<2 -> 3>}

		Set<Integer> successorsOfTwo = network.successors(2);  // returns {3}
		out(successorsOfTwo); // [3]
		
		Set<String> outEdgesOfTwo = network.outEdges(2);   // returns {"2->3"}
		out(outEdgesOfTwo); // [2->3]

		//network.addEdge(2, 3, "2->3 too");  // 异常; 网络不允许平行边缘
		//out(network); 

		network.addEdge(2, 3, "2->3");  // 没有效果;这个边已经存在，并按此顺序连接这些节点
		out(network); // isDirected: true, allowsParallelEdges: false, allowsSelfLoops: false, nodes: [1, 2, 3], edges: {2->3=<2 -> 3>}
	
		// 判断边是否存在
		//Set<String> inEdgesOfFour = network.inEdges(4); // 异常; 节点不在图表中
		//out(inEdgesOfFour);
	}
}
