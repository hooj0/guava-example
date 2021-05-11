package com.cnblogs.hoojo.graph.example;

import com.cnblogs.hoojo.graph.AbstractGraphTests;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.graph.*;
import org.junit.Test;

import java.util.*;

/**
 * AOE网对应研究实际问题是工程的工期问题：
 *  （1）完成一项工程至少需要多少时间？
 *  （2）哪些活动是影响整个工程进度的关键？
 * @author hoojo
 * @createDate 2019年1月22日 下午3:12:02
 * @file AOEValueGraphTest.java
 * @package com.cnblogs.hoojo.graph.example
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class AOEValueGraphTest extends AbstractGraphTests {

	@Test
	public void testAOE() {
		MutableValueGraph<String, Integer> graph = ValueGraphBuilder.directed()
			    .nodeOrder(ElementOrder.insertion())
			    .expectedNodeCount(10)
			    .build();

		String V1 = "v1";
		String V2 = "v2";
		String V3 = "v3";
		String V4 = "v4";
		String V5 = "v5";
		String V6 = "v6";
		String V7 = "v7";
		String V8 = "v8";
		String V9 = "v9";
		
		graph.putEdgeValue(V1, V2, 6);
		graph.putEdgeValue(V1, V3, 4);
		graph.putEdgeValue(V1, V4, 5);
		graph.putEdgeValue(V2, V5, 1);
		graph.putEdgeValue(V3, V5, 1);
		graph.putEdgeValue(V4, V6, 2);
		graph.putEdgeValue(V5, V7, 9);
		graph.putEdgeValue(V5, V8, 7);
		graph.putEdgeValue(V6, V8, 4);
		graph.putEdgeValue(V7, V9, 2);
		graph.putEdgeValue(V8, V9, 4);
		out("graph: " + graph); // graph: isDirected: true, allowsSelfLoops: false, nodes: [v1, v2, v3, v4, v5, v6, v7, v8, v9], edges: {<v1 -> v2>=6, <v1 -> v3>=4, <v1 -> v4>=5, <v2 -> v5>=1, <v3 -> v5>=1, <v4 -> v6>=2, <v5 -> v7>=9, <v5 -> v8>=7, <v6 -> v8>=4, <v7 -> v9>=2, <v8 -> v9>=4}

		/**
		 * 利用Traverser接口将graph进行拓扑排序topologically，此处返回的逆拓扑排序
		 */
		String startNode = "v1";
		Iterable<String> topologicallys = Traverser.forGraph(graph).depthFirstPostOrder(startNode);

		// 倒序
		out("topologically: " + format(topologicallys)); // topologically: v9,v7,v8,v5,v2,v3,v6,v4,v1,
		
		// 递推求得ve(j)值：
		Map<String, Integer> ves = getVeValues(graph, topologicallys);
		out("ves: " + format(ves)); // ves: {v6:7, v7:16, v8:14, v9:18, v1:0, v2:6, v3:4, v4:5, v5:7, }

		// 递推求得vl(j)值
		Map<String, Integer> vls = getVlValues(graph, topologicallys, ves);
		out("vls: " + format(vls)); // vls: {v6:10, v7:16, v8:14, v9:18, v1:0, v2:6, v3:6, v4:8, v5:7, }
		
		// 根据前面求取的ve(j)和vl(j)来找出关键活动（判断条件：ve(j) == vl(k) - dut(<j,k>)）：
		/**
		 * 判断条件：ve(j) == vl(k) - dut(<j,k>)
		 */
		//关键活动路径结果集
		List<EndpointPair<String>> criticalActives = new ArrayList<>(); 
		//返回图中所有活动(边)
		Set<EndpointPair<String>> edgs = graph.edges(); 
		//遍历每一条边（活动），过滤出：ve(j) == vl(k) - dut<j, k>
		for (EndpointPair<String> endpoint : edgs) {
		    final int dut = graph.edgeValueOrDefault(endpoint.nodeU(), endpoint.nodeV(), 0);
		    //ve(j) == vl(k) - dut<j, k>
		    if (vls.get(endpoint.nodeV()) - dut == ves.get(endpoint.nodeU())) { 
		        criticalActives.add(endpoint);
		    }
		}
		out("critical actives: " + format(criticalActives)); // critical actives: <v1 -> v2>,<v2 -> v5>,<v5 -> v7>,<v5 -> v8>,<v7 -> v9>,<v8 -> v9>,


		/*
		 * 从输出可知，图中存在两条关键路径：
		 * {<v1 -> v2>, <v2 -> v5>, <v5 -> v8>, <v8 -> v9>} 和 {<v1 -> v2>, <v2 -> v5>, <v5 -> v7>, <v8 -> v9>}
		 * (在示例图中使用红色线段标注)。
		 * 因此，缩短这两条路径上活动的工期，将能有效的缩短整个工程的工期。
		 */
	}
	
	/**
	 * ve(j) = Max{ve(i) + dut(<i,j>) }; <i,j>属于T，j=1,2...,n-1
	 * @param graph
	 * @param topologicallys
	 * @return
	 */
	private static Map<String, Integer> getVeValues(ValueGraph<String, Integer> graph, 	Iterable<String> topologicallys) {
	    List<String> reverses = Lists.newArrayList(topologicallys.iterator());
	    Collections.reverse(reverses); // 将逆拓扑排序反向
	    
	    Map<String, Integer> ves = new HashMap<>(); // 结果集
	    // 从前往后遍历
	    for (String node : reverses) {
	        ves.put(node, 0); // 每个节点的ve值初始为0

	        // 获取node的前趋列表
	        Set<String> predecessors = graph.predecessors(node); 
	        int maxValue = 0;
	        
	        // 找前趋节点+当前活动耗时最大的值为当前节点的ve值
	        for (String predecessor : predecessors) {
	            maxValue = Math.max(ves.get(predecessor) + graph.edgeValueOrDefault(predecessor, node, 0), maxValue);
	        }
	        ves.put(node, maxValue);
	    }
	    return ves;
	}

	/**
	 * vl(i) = Min{vl(j) - dut(<i,j>}; <i,j>属于S，i=n-2,...,0
	 * @param graph
	 * @param topologicallys
	 * @param vels
	 * @return
	 */
	private static Map<String, Integer> getVlValues(ValueGraph<String, Integer> graph, Iterable<String> topologicallys, Map<String, Integer> vels) {
	    Map<String, Integer> vls = Maps.newHashMap(); // 结果集
	    
	    // 从后往前遍历
	    for (String node : topologicallys) {
	        // 获取node的后继列表
	        Set<String> successors = graph.successors(node);
	        int initValue = Integer.MAX_VALUE; // 初始值为最大值
	        if (successors.size() <= 0) { // 表示是结束点，赋值为ve值
	            initValue = vels.get(node);
	        }
	        vls.put(node, initValue);
	        
	        int minValue = initValue;
	        // 找后继节点-当前活动耗时最少的值为当前节点的vl值
	        for (String successor : successors) {
	            minValue = Math.min(vls.get(successor) - graph.edgeValueOrDefault(node, successor, 0), minValue);
	        }
	        vls.put(node, minValue);
	    }
	    
	    return vls;
	}
}
