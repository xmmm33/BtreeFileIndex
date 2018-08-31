package my.BplusTreeModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import lombok.Data;

/**
 * B+树
 * 
 * @author hmj
 *
 */
//使用lombok注释自动生成getset方法 简化代码
@Data
public class BplusTree {
	/**根节点 */
	protected Node root; // 根节点
	/**B+树的阶数 */
	protected int order; // 阶数
	/**叶子节点的链表的头结点 */
	protected Node head; // 叶子节点的链表头
	
	// 生成get set方法
//	public Node getRoot() {
//		return root;
//	}
//
//	public void setRoot(Node root) {
//		this.root = root;
//	}
//
//	public int getOrder() {
//		return order;
//	}
//
//	public void setOrder(int order) {
//		this.order = order;
//	}
//
//	public Node getHead() {
//		return head;
//	}
//
//	public void setHead(Node head) {
//		this.head = head;
//	}
	/**
	 * 根据用户输入的文件索引值进行文件路径查询
	 * 
	 * @param key
	 * 			按文件名查询文件路径
	 * @return
	 * 			返回list集合
	 */
	public List<String> get(Comparable key) {
		return root.get(key);
	}
	/**
	 * 进行简单的模糊匹配查询 主要用的String的indexOf方法
	 * 
	 * @param key
	 * 			输入的索引值
	 * @param tree
	 * 			在哪一个B+树进行查询
	 * @return
	 * 			返回list集合
	 */
	public List<String> getOnVague(Comparable key, BplusTree tree) {
		return root.getByVague(key, tree);
	}
	/**
	 * 进行文件类型匹配查询 这里的文件类型索引值由用户输入
	 * 
	 * @param key
	 * 			输入的索引值
	 * @param tree
	 *			在哪一棵B+进行查询
	 * @return
	 * 			返回list集合
	 */
	public List<String> getOnTypeByInput(Comparable key, BplusTree tree) {
		return root.getOnTypeByInput(key, tree);
	}
	/**
	 * 进行文件类型匹配查询 这里的文件类型由系统默认提供
	 * 
	 * @param key
	 * 			输入的文件类型索引值
	 * @param tree
	 * 			在哪一棵B+树进行查询
	 * @return
	 * 			返回list集合
	 */
	public List<String> getOnTypeByExist(Comparable key, BplusTree tree) {
		return root.getOnTypeByExist(key, tree);
	}
	/**
	 * 删除操作
	 * 
	 * @param key
	 * 			被删除元素的索引值
	 */
	public void remove(Comparable key) {
		root.remove(key);
	}
	/**
	 * 进行元素的插入操作
	 * 
	 * @param key
	 * 			待插入元素的索引值
	 * @param arrayList
	 * 			待插入元素的路径值
	 */
	public void insertOrUpdate(Comparable key, ArrayList<String> arrayList) {
		root.insertOrUpdate(key, arrayList, this);
	}
	/**
	 * B+树构造方法
	 * 
	 * @param order
	 *			B+树的阶数 必须大于等于3阶
	 */
	public BplusTree(int order) {
		if (order < 3) {
			System.out.println("阶数必须大于2");
			System.exit(0);
		}
		this.order = order;
		root = new Node(true, true);
	}
	
//	public void output() {
//		Queue<Node> queue = new LinkedList<Node>();
//		queue.offer(head);
//		while (!queue.isEmpty()) {
//			Node node = queue.poll();
//			for (int i = 0; i < node.size(); ++i)
//				System.out.print(node.entryAt(i) + " ");
//			System.out.println();
//			if (!node.isLeaf()) {
//				for (int i = 0; i <= node.size(); ++i)
//					queue.offer(node.childAt(i));
//			}
//		}
//	}
}
