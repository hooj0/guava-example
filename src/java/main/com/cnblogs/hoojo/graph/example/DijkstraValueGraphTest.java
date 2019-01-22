package com.cnblogs.hoojo.graph.example;

import java.util.Map;

import org.junit.Test;

import com.cnblogs.hoojo.graph.AbstractGraphTests;
import com.google.common.collect.Maps;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

/**
 * 求最短路径算法：Dijkstra（迪杰斯特拉）算法
 * https://www.jianshu.com/p/92e46d990d17
 * 
 * 算法思想是按路径长度递增的次序一步一步并入来求取，是贪心算法的一个应用，
 * 用来解决单源点到其余顶点的最短路径问题。
 * @author hoojo
 * @createDate 2019年1月22日 下午3:35:53
 * @file DijkstraValueGraphTest.java
 * @package com.cnblogs.hoojo.graph.example
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class DijkstraValueGraphTest extends AbstractGraphTests {

	// 引入一个临时结构来记录每个节点运算的中间结果
	private static class NodeExtra {
		public String nodeName; // 当前的节点名称
	    public int distance; // 开始点到当前节点的最短路径
	    public boolean visited; // 当前节点是否已经求的最短路径（S集合）
	    public String preNode; // 前一个节点名称
	    public String path; // 路径的所有途径点
	}
	
	@Test
	public void test() {
		MutableValueGraph<String, Integer> graph = ValueGraphBuilder.directed()
		        .nodeOrder(ElementOrder.insertion())
		        .expectedNodeCount(10)
		        .build();
		
		String V0 = "v0";
		String V1 = "v1";
		String V2 = "v2";
		String V3 = "v3";
		String V4 = "v4";
		String V5 = "v5";
		
		graph.putEdgeValue(V0, V2, 10);
		graph.putEdgeValue(V0, V4, 30);
		graph.putEdgeValue(V0, V5, 100);
		graph.putEdgeValue(V1, V2, 5);
		graph.putEdgeValue(V2, V3, 50);
		graph.putEdgeValue(V3, V5, 10);
		graph.putEdgeValue(V4, V3, 20);
		graph.putEdgeValue(V4, V5, 60);
		
		out(graph); // isDirected: true, allowsSelfLoops: false, nodes: [v0, v2, v4, v5, v1, v3], edges: {<v0 -> v2>=10, <v0 -> v4>=30, <v0 -> v5>=100, <v2 -> v3>=50, <v4 -> v3>=20, <v4 -> v5>=60, <v1 -> v2>=5, <v3 -> v5>=10}

		Map<String, NodeExtra> nodeExtras = Maps.newHashMap();
		
		// 将起始点V0并入集合S中，因为他的最短路径已知为0：
		String startNode = V0;
		NodeExtra current = nodeExtras.get(startNode);
		if (current == null) {
			current = new NodeExtra();
		}
		
		current.distance = 0; // 一开始可设置开始节点的最短路径为0
		current.visited = true; // 并入S集合
		current.path = startNode;
		current.preNode = startNode;
		
		nodeExtras.put(startNode, current);
		
		// 在当前状态下找出起始点V0开始到其他节点路径最短的节点：
		NodeExtra minExtra = null; // 路径最短的节点信息
		int min = Integer.MAX_VALUE;
		for (String notVisitedNode : graph.nodes()) {
			// 获取节点的辅助信息
			NodeExtra extra = nodeExtras.get(notVisitedNode);

			// 不在S集合中，且路径较短
			if (extra != null && !extra.visited && extra.distance < min) {
				min = extra.distance;
				minExtra = extra;
			}
		}
		out("minExtra: " + minExtra);
		
		// 将最短路径的节点并入集合S中：
		if (minExtra != null) { // 找到了路径最短的节点
			minExtra.visited = true; // 并入集合S中
			// 更新其中转节点路径
			minExtra.path = nodeExtras.get(minExtra.preNode).path + " -> " + minExtra.nodeName;
			current = minExtra; // 标识当前并入的最短路径节点
		}

		
	}
	
}
