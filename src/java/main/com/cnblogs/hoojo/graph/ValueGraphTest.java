package com.cnblogs.hoojo.graph;

import org.junit.Test;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

/**
 * value graph test example
 * @author hoojo
 * @createDate 2019年1月15日 下午5:52:56
 * @file ValueGraphTest.java
 * @package com.cnblogs.hoojo.graph
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ValueGraphTest extends BasedTest {

	@Test
	public void testDefined() {
		// 有向图
		MutableValueGraph<Integer, Double> weightedGraph = ValueGraphBuilder.directed().build();
		// 添加节点
		weightedGraph.addNode(1);
		out(weightedGraph); // isDirected: true, allowsSelfLoops: false, nodes: [1], edges: {}

		// 添加边
		weightedGraph.putEdgeValue(2, 3, 1.5);  // 如果尚未存在，还会添加节点2和3
		out(weightedGraph); // isDirected: true, allowsSelfLoops: false, nodes: [1, 2, 3], edges: {<2 -> 3>=1.5}

		weightedGraph.putEdgeValue(3, 5, 1.5);  // 边值（如Map值）不必是唯一的
		out(weightedGraph); // isDirected: true, allowsSelfLoops: false, nodes: [1, 2, 3, 5], edges: {<2 -> 3>=1.5, <3 -> 5>=1.5}
		
		weightedGraph.putEdgeValue(2, 3, 2.0);  // 将（2,3）的值更新为2.0
		out(weightedGraph); // isDirected: true, allowsSelfLoops: false, nodes: [1, 2, 3, 5], edges: {<2 -> 3>=2.0, <3 -> 5>=1.5}
	}
}
