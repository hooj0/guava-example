# graph 图论
`Guava`库的目录`common.graph`包含的模块是一个描述实体(`entity`)以及实体之间的关系的图数据结构模型库。例如：网页与超链接、科学家与他们写的论文、机场与其航线、人与其家族等。`Guava-Graph`模块的目的是提供一种通用以及可扩展的语言来描述类似上述的举例。

## 关系图

![uml](draw.jpg)

## 定义
- **`graph` 的组成**，图中每一条边都是**有向**边的，则被称为**有向图**；每一条边都是**无向**的，则被称为**无向图**。**不支持图中既有有向边又有无向边的情形**。
	- **`node` 节点**：一组节点(`node`)（也称为**顶点**）
	- **`edge` 边**：一组连接节点的边(`edge`)（也称为**链接**或者**弧**）
	- **`endpoint` 端点**：边缘的节点称为端点(`endpoint`)
	- **`directed` 有向边**: 定义了开始(`source`)和结束(`target`)，有向边适用于非对称的关系模型（起源、指向、作者）
	- **`undirected` 无向边**: 没有定义了开始(`source`)或结束(`target`)，无向边适用于对称关系模型（折叠、距离、同级关系）
	- **`source` 起点**: 边的起始点，用来连接边
	- **`target` 终点**: 边的结束点，用来连接边

**示例**：	
```java
graph.addEdge(nodeU, nodeV, edgeUV);
```
`nodeU`和`nodeV`是两个邻接点(`adjacent`)，`edgeUV`是顶点`nodeU`到顶点`nodeV`的事件(`incident`)
	
在**有向图**中，有如下定义：
+ `nodeU`是`nodeV`的一个**前趋**(`predecessor`)
+ `nodeV`是`nodeU`的一个**后继**(`successor`)
+ `edgeUV`是`nodeU`的一条**出度**(`outgoing`)边
+ `edgeUV`是`nodeV`的一条**入度**(`incoming`)边
+ `nodeU`是边`edgeUV`的**起点**(`source`)
+ `nodeV`是边`edgeUV`的**终点**(`target`)

在**无向图**中，有如下定义：
+ `nodeU`既是`nodeV`的**前趋**也是`nodeV`的**后继**
+ `nodeV`既是`nodeU`的**前趋**也是`nodeU`的**后继**
+ `edgeUV`既是`nodeU`的**入度**也是`nodeU`的**出度**
+ `edgeUV`既是`nodeV`的**入度**也是`nodeV`的**出度**

一条连接节点本身的边被称为**自环**(`self-loop`)，也就是说，**一条边连接了两个相同的节点**。如果这个自环是**有向**的，那么这条边既是节点的**入度边**也是节点的**出度边**，这个节点既是边的**起点**(`source`)也是边的**终点**(`target`)。

如果两条边以**相同的顺序**连接**相同**的节点，则称这两条边为**平行边**(`parallel`)。如果以**相反的顺序**连接相同的节点则称这两条边为**逆平行边**(`antiparallel`)，**无向边**不能被称为**逆平行边**。

示例：
```java
//有向图
directedGraph.addEdge(nodeU, nodeV, edgeUV_a);
directedGraph.addEdge(nodeU, nodeV, edgeUV_b);
directedGraph.addEdge(nodeV, nodeU, edgeVU);

//无向图
undirectedGraph.addEdge(nodeU, nodeV, edgeUV_a);
undirectedGraph.addEdge(nodeU, nodeV, edgeUV_b);
undirectedGraph.addEdge(nodeV, nodeU, edgeVU);
```
在**有向图**`directedGraph`中，边`edgeUV_a`和边`edgeUV_b`是**相互平行边**，与边`edgeVU`是**逆平行边**；在**无向图**`undirectedGraph`中，边`edgeUV_a`、`edgeUV_b`和`edgeVU`是**两两相互逆平行边**。

## 功能
`common.graph`模块的核心是提供**图相关操作的接口和类**。另外，它**没有提供类似`I/O`或者可视化**的功能。如果选用这个模块将会有非常多的限制，具体详细信息可以查看下面`FAQ`的相关主题。

总体来讲，它提供了如下几种类型的图：
+ 有向图
+ 无向图
+ 节点和（或）边带权图
+ 允许（不允许）自环图
+ 允许（不允许）平行边图，允许平行边图有时也称为**多重图**(`multigraphs`)
+ 节点或边被有序插入、顺序、无序图(`graphs whose nodes/edges are insertion-ordered, sorted, or unordered`)

