package com.cnblogs.hoojo.graph.example;

import org.junit.Test;

import com.cnblogs.hoojo.graph.AbstractGraphTests;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.Traverser;

/**
 * 广度优先遍历
 * 也称为广度优先搜索（Breadth First Search），它类似于树的分层遍历算法
 * (按树的深度来遍历，深度为1的节点、深度为2的节点...).
 * 
 * @author hoojo
 * @createDate 2019年1月25日 下午4:09:55
 * @file TraversingValueGraphTest.java
 * @package com.cnblogs.hoojo.graph.example
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TraversingValueGraphTest extends AbstractGraphTests {

	private static final String A = "A";
    private static final String B = "B";
    private static final String C = "C";
    private static final String D = "D";
    private static final String E = "E";
    private static final String F = "F";
    private static final String G = "G";
    private static final String H = "H";

    private static final String V1 = "V1";
    private static final String V2 = "V2";
    private static final String V3 = "V3";
    private static final String V4 = "V4";
    private static final String V5 = "V5";
    private static final String V6 = "V6";
    private static final String V7 = "V7";
    private static final String V8 = "V8";
    
	// 广度遍历优先
	@Test
	public void testGraph() {
		// 构建示例图所示的图结构（无向图）
		MutableGraph<String> graph = GraphBuilder.undirected()
				.nodeOrder(ElementOrder.<String> natural())
				.build();

		graph.putEdge(V1, V2);
		graph.putEdge(V1, V3);
		graph.putEdge(V2, V4);
		graph.putEdge(V2, V5);
		graph.putEdge(V3, V6);
		graph.putEdge(V3, V7);
		graph.putEdge(V4, V8);
		graph.putEdge(V5, V8);
		graph.putEdge(V6, V7);
		
		// 测试breadthFirst()接口，从V1开始遍历
		Iterable<String> bfs = Traverser.forGraph(graph).breadthFirst(V1);
		out("dfs graph: " + format(bfs));

		Iterable<String> dfsPre = Traverser.forGraph(graph).depthFirstPreOrder(V1);
		out("dfsPre graph: " + format(dfsPre));

		Iterable<String> dfsPost = Traverser.forGraph(graph).depthFirstPostOrder(V1);
		out("dfsPost graph: " + format(dfsPost));
	}
	
	// 广度遍历优先
	@Test
	public void testTree() {
		MutableGraph<String> graph = GraphBuilder.directed()
	            .nodeOrder(ElementOrder.<String>natural())
	            .build();
		
        graph.putEdge(A, D);
        graph.putEdge(A, E);
        graph.putEdge(B, E);
        graph.putEdge(B, F);
        graph.putEdge(E, H);
        graph.putEdge(C, G);
	        
		Iterable<String> bfs = Traverser.forTree(graph).breadthFirst(A);
		out("bfs tree: " + format(bfs));

        Iterable<String> dfsPre = Traverser.forTree(graph).depthFirstPreOrder(A);
        out("dfsPre tree: " + format(dfsPre));

        Iterable<String> dfsPost = Traverser.forTree(graph).depthFirstPostOrder(A);
        out("dfsPost tree: " + format(dfsPost));
	}
	
	// 深度度优先遍历算法
	@Test
	public void test() {
		//构建示例图所示的图结构（无向图）
		MutableGraph<String> graph = GraphBuilder.undirected()
		    .nodeOrder(ElementOrder.<String>natural())
		    .build();

		graph.putEdge(V1, V2);
		graph.putEdge(V1, V3);
		graph.putEdge(V2, V4);
		graph.putEdge(V2, V5);
		graph.putEdge(V3, V6);
		graph.putEdge(V3, V7);
		graph.putEdge(V4, V8);
		graph.putEdge(V5, V8);
		graph.putEdge(V6, V7);

		//测试depthFirstPreOrder()接口，从V1开始遍历
		Iterable<String> dfsPre = Traverser.forGraph(graph).depthFirstPreOrder(V1);
		for (String node : dfsPre) {
		    System.out.print(node + ",");
		}
		System.out.println();

		//测试depthFirstPostOrder()接口，从V1开始遍历
		Iterable<String> dfsPost = Traverser.forGraph(graph).depthFirstPostOrder(V1);
		for (String node : dfsPost) {
		    System.out.print(node + ",");
		}
		System.out.println();
	}
}
