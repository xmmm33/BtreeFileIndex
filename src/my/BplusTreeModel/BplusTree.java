package my.BplusTreeModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BplusTree {

	protected Node root; // 根节点
	protected int order; // 阶数
	protected Node head; // 叶子节点的链表头
	
	// 生成get set方法
	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Node getHead() {
		return head;
	}

	public void setHead(Node head) {
		this.head = head;
	}

	public List<String> get(Comparable key) {
		return root.get(key);
	}

	public List<String> getOnVague(Comparable key, BplusTree tree) {
		return root.getByVague(key, tree);
	}

	public List<String> getOnTypeByInput(Comparable key, BplusTree tree) {
		return root.getByType(key, tree);
	}

	public List<String> getOnTypeByExist(Comparable key, BplusTree tree) {
		return root.getByButton(key, tree);
	}

	public void remove(Comparable key) {
		root.remove(key);
	}

	public void insertOrUpdate(Comparable key, ArrayList<String> arrayList) {
		root.insertOrUpdate(key, arrayList, this);
	}

	public BplusTree(int order) {
		if (order < 3) {
			System.out.println("阶数必须大于2");
			System.exit(0);
		}
		this.order = order;
		root = new Node(true, true);
	}

	public void output() {
		Queue<Node> queue = new LinkedList<Node>();
		queue.offer(head);
		while (!queue.isEmpty()) {
			Node node = queue.poll();
			for (int i = 0; i < node.size(); ++i)
				System.out.print(node.entryAt(i) + " ");
			System.out.println();
			if (!node.isLeaf()) {
				for (int i = 0; i <= node.size(); ++i)
					queue.offer(node.childAt(i));
			}
		}
	}
}
