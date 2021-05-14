package com.cnblogs.hoojo.graph.example;

import com.cnblogs.hoojo.graph.AbstractGraphTests;
import com.google.common.base.Strings;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * prim 普里姆 算法
 * 
 * 该算法也称为“加点法”，每次迭代选择代价最小的边对应的节点加入到最小生成树中。
 * 算法从某个节点S出发，逐步覆盖整个连通网的所有顶点。
 * 
 * @author hoojo
 * @createDate 2019年1月24日 下午4:58:36
 * @file PrimValueGraphTest.java
 * @package com.cnblogs.hoojo.graph.example
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class PrimValueGraphTest extends AbstractGraphTests {

	private static final String V1 = "v1";
    private static final String V2 = "v2";
    private static final String V3 = "v3";
    private static final String V4 = "v4";
    private static final String V5 = "v5";
    private static final String V6 = "v6";
    
    private static class CloseEdge {
        public String preNode; //顶点信息
        public int lowsestCost; //最小代价
    }
    
	@Test
	public void test() {
		MutableValueGraph<String, Integer> graph = ValueGraphBuilder.undirected()
                .nodeOrder(ElementOrder.<String>natural())
                .expectedNodeCount(10)
                .build();

        graph.putEdgeValue(V1, V2, 6);
        graph.putEdgeValue(V1, V3, 1);
        graph.putEdgeValue(V1, V4, 5);
        graph.putEdgeValue(V2, V3, 5);
        graph.putEdgeValue(V2, V5, 3);
        graph.putEdgeValue(V3, V4, 5);
        graph.putEdgeValue(V3, V5, 6);
        graph.putEdgeValue(V3, V6, 4);
        graph.putEdgeValue(V4, V6, 2);
        graph.putEdgeValue(V5, V6, 6);
        
        
		// 最小生成树的结果graph
		MutableValueGraph<String, Integer> result = ValueGraphBuilder.from(graph).build();

		// 节点的附加信息，用于保存计算的中间结果
		Map<String, CloseEdge> closeEdges = new HashMap<>();

        String startNode = V1;
        
        /**
         * 初始化开始节点与节点的权重信息
         */
		for (String node : graph.nodes()) {
			CloseEdge extra = new CloseEdge();
			extra.preNode = startNode; // 前一个节点为startNode节点
			// 有边连接时，lowsestCost为其边上的权值；没有边连接时，则为无穷大
			extra.lowsestCost = graph.edgeValueOrDefault(startNode, node, Integer.MAX_VALUE);
			closeEdges.put(node, extra);
		}

		closeEdges.get(startNode).lowsestCost = 0; // 初始化时，先将startNode并入U集合（lowsestCost = 0）
		for (int i = 0; i < closeEdges.size() - 1; i++) { // 循环n -1次
			/**
			 * 找到一条{U, V - U}中权值最小的边，minCostNode是V - U集合中的顶点
			 */
			String minCostNode = "";
			int min = Integer.MAX_VALUE;
			for (String node : closeEdges.keySet()) {
				CloseEdge value = closeEdges.get(node);

				/**
				 * lowsestCost == 0时，表示该边已经并入了U集合，不在查找范围
				 */
				if (value.lowsestCost > 0 && value.lowsestCost < min) {
					min = value.lowsestCost;
					minCostNode = node;
				}
			}

			if (!Strings.isNullOrEmpty(minCostNode)) {
				CloseEdge minEdge = closeEdges.get(minCostNode);
				/**
				 * 将最小的权重边放入结果Graph中
				 */
				result.putEdgeValue(minEdge.preNode, minCostNode, minEdge.lowsestCost);
				out(minEdge.preNode + " -> " + minCostNode);
				minEdge.lowsestCost = 0; // 将找到的最小的边并入U集合中

				/**
				 * 并入minCostNode后，更新minCostNode到其他节点的lowsestCost信息，作为下次循环查找的条件
				 */
				for (String node : graph.adjacentNodes(minCostNode)) { // 优化：只遍历邻接点即可
					final int cost = graph.edgeValueOrDefault(minCostNode, node, Integer.MAX_VALUE);
					CloseEdge current = closeEdges.get(node);
					if (current.lowsestCost > 0 && cost < current.lowsestCost) {
						current.lowsestCost = cost;
						current.preNode = minCostNode;
					}
				}
			}
		}
		
		out(result); // isDirected: false, allowsSelfLoops: false, nodes: [v1, v2, v3, v4, v5, v6], edges: {[v3, v1]=1, [v3, v2]=5, [v5, v2]=3, [v6, v3]=4, [v6, v4]=2}
	}
}
