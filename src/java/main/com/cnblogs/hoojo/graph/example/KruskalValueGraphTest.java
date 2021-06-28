package com.cnblogs.hoojo.graph.example;

import com.cnblogs.hoojo.graph.AbstractGraphTests;
import com.google.common.graph.*;
import org.junit.Test;

import java.util.*;

/**
 * Kruskal（克鲁斯卡尔）算法
 * 该算法也称为“加边法”，初始时最小生成树的边数为0，
 * 每迭代一次选择一条满足条件的最小代价边加入到最小生成树的边集合中。
 * 
 * @author hoojo
 * @createDate 2019年1月24日 下午5:41:47
 * @file KruskalValueGraphTest.java
 * @package com.cnblogs.hoojo.graph.example
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class KruskalValueGraphTest extends AbstractGraphTests {

	private static final String V1 = "v1";
    private static final String V2 = "v2";
    private static final String V3 = "v3";
    private static final String V4 = "v4";
    private static final String V5 = "v5";
    private static final String V6 = "v6";
    
	@Test
	public void test() {
		//最小生成树的结果graph
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

        /**
         * 辅助变量，用于判断两个顶点是否在两个连通分量中
         */
        Map<String, Integer> vest = new HashMap<>();

        /**
         * 初始化辅助数组vest，一开始每个节点单独成一个连通分量
         */
        int index = 0;
        for (String node : graph.nodes()) {
            vest.put(node, index++);
        }

        /**
         * 将图中所有的边，按权值从小到大排序，便于后面一次循环就能从小到大遍历
         * EndpointPair数据结构用于存储边的两个顶点U和V
         */
        List<EndpointPair<String>> edges = sortEdges(graph);

        /**
         * 按增序遍历图中所有边
         */
        for (EndpointPair<String> edge : edges) {
            //out(edge.nodeU() + " ->" + edge.nodeV() + " = " + graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), 0));

            /**
             * 获取两个节点所代表的连通分量的sn值
             */
            final int nodeUSn = vest.get(edge.nodeU());
            final int nodeVSn = vest.get(edge.nodeV());

            /**
             * 判断一条边的两个顶点是否对应不同的连通分量。若不相同，则将该边加入最小生成树的图中
             */
            if (nodeUSn != nodeVSn) {
                final int value = graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), 0);
                graph.putEdgeValue(edge.nodeU(), edge.nodeV(), value);
                out(edge.nodeU() + " ->" + edge.nodeV() + ": " + value);

                /**
                 * 更新加入最小生成树中边对应的整个连通分量的sn值（后一个连通分量并入
                 * 前一个连通分量），以此作为下一次遍历的依据
                 */
                for (String node : vest.keySet()) {
                    if (vest.get(node) == nodeVSn) {
                        vest.put(node, nodeUSn);
                    }
                }
            }
        }
	}
	
	
	// 将图中的边按其权值大小排序
	private static List<EndpointPair<String>> sortEdges(final ValueGraph<String, Integer> graph) {
        List<EndpointPair<String>> edges = new ArrayList<>();
        edges.addAll(graph.edges());
        
        /**
         * 使用Collections的sort函数进行排序，compare()比较的是边权值
         */
        Collections.sort(edges, new Comparator<EndpointPair<String>>() {
            @Override
            public int compare(EndpointPair<String> endPoint1, EndpointPair<String> endPoint2) {
            	
                final int edge1 = graph.edgeValueOrDefault(endPoint1.nodeU(), endPoint1.nodeV(), 0);
                final int edge2 = graph.edgeValueOrDefault(endPoint2.nodeU(), endPoint2.nodeV(), 0);
                
                return edge1 - edge2;
            }
        });
        
        return edges;
    }
}
