package my.BplusTreeModel;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Node {

	/** 是否为叶子节点 */
	protected boolean isLeaf;

	/** 是否为根节点 */
	protected boolean isRoot;

	/** 父节点 */
	protected Node parent;

	/** 叶节点的前节点 */
	protected Node previous;

	/** 叶节点的后节点 */
	protected Node next;

	/** 节点的关键字 */
	protected List<Entry<Comparable, ArrayList<String>>> entries;

	/** 子节点 */
	protected List<Node> children;

	public Node(boolean isLeaf) {
		this.isLeaf = isLeaf;
		entries = new ArrayList<Entry<Comparable, ArrayList<String>>>();

		if (!isLeaf) {
			children = new ArrayList<Node>();
		}
	}

	public Node(boolean isLeaf, boolean isRoot) {
		this(isLeaf);
		this.isRoot = isRoot;
	}

	// 查询操作
	public List<String> get(Comparable key) {

		// 如果是叶子节点
		if (isLeaf) {
			for (Entry<Comparable, ArrayList<String>> entry : entries) {
				if (entry.getKey().compareTo(key) == 0) {
					// 返回找到的对象
					return entry.getValue();
				}
			}
			// 未找到所要查询的对象
			return null;

			// 如果不是叶子节点，找到叶子节点执行上面的操作
		} else {
			// 如果key小于等于节点最左边的key，沿第一个子节点继续搜索
			if (key.compareTo(entries.get(0).getKey()) <= 0) {
				return children.get(0).get(key);
				// 如果key大于节点最右边的key，沿最后一个子节点继续搜索
			} else if (key.compareTo(entries.get(entries.size() - 1).getKey()) >= 0) {
				return children.get(children.size() - 1).get(key);
				// 否则沿比key大的前一个子节点继续搜索
			} else {
				for (int i = 0; i < entries.size(); i++) {
					if (entries.get(i).getKey().compareTo(key) <= 0 && entries.get(i + 1).getKey().compareTo(key) > 0) {
						return children.get(i).get(key);
					}
				}
			}
		}

		return null;
	}

	public List<String> getByVague(Comparable key, BplusTree tree) { // 模糊查询
		Node head = tree.getHead(); // 得到叶子节点的链表头
		// TODO Auto-generated method stub
		List<String> temppath = new ArrayList<>();
		while (head != null) {
			List<Entry<Comparable, ArrayList<String>>> entries2 = head.getEntries(); // 得到每个节点下的键值对集合
			for (Entry<Comparable, ArrayList<String>> entry : entries2) { // 遍历键值对集合
				String entrykey = entry.getKey().toString().toUpperCase(); // 把集合中的key值提取并转换成大写
				String keyup = key.toString().toUpperCase(); // 把输入的key值转换为大写
				if (entrykey.indexOf(keyup) >= 0) { // 使用String的indexOf方法
													// 查询keyup在enrtykey中是否出现
					// 返回找到的对象
					temppath.addAll(entry.getValue()); // 找到了key值
														// 把key值对应的entry的全部value元素添加到temppath集合中
				}
			}
			head = head.getNext(); // head指向下一个叶子节点
		}
		if (temppath != null) { // 如果查询到了文件路径 返回结果结合
			return temppath;
		} else {
			return null; // 否则查询失败
		}

	}

	public List<String> getByType(Comparable key, BplusTree tree) { // 类型查询

		Node head = tree.getHead(); // 得到叶子节点的链表头
		// TODO Auto-generated method stub
		List<String> temppath = new ArrayList<>(); // new一个结果集合
		while (head != null) {
			List<Entry<Comparable, ArrayList<String>>> entries2 = head.getEntries(); // 得到每个叶子节点的关键字集合
																						// 也就是文件名与文件路径键值对的链表集合
			for (Entry<Comparable, ArrayList<String>> entry : entries2) { // 遍历关键字集合
				if (entry.getKey().toString().contains(".")) {
					String[] keytype = entry.getKey().toString().split("\\."); // 把每个节点的关键字集合中的文件类型提取出来
					if (keytype[keytype.length - 1].toUpperCase().indexOf(key.toString().toUpperCase()) >= 0) { // 看输入的文件类型是否存在
						// 返回找到的对象 即返回文件绝对路径
						temppath.addAll(entry.getValue());
					}
				}
			}
			head = head.getNext(); // 指向下一个叶子节点
		}
		if (temppath.size() != 0) {
			return temppath;
		} else {
			return null;
		}
	}

	public List<String> getByButton(Comparable key, BplusTree tree) { // 按searchtype查询
		String searchType = key.toString();
		List<String> res = new ArrayList<String>();
		if (searchType.equals("影视")) {
			Node head = tree.getHead();
			List<String> listContent = new ArrayList<String>();
			listContent.add("mp4");
			listContent.add("rmvb");
			listContent.add("mkv");
			while (head != null) {
				List<Entry<Comparable, ArrayList<String>>> entries2 = head.getEntries();
				for (Entry<Comparable, ArrayList<String>> temp : entries2) {
					if (temp.getKey().toString().contains(".")) {
						String[] fileType = temp.getKey().toString().toLowerCase().split("\\.");
						if (listContent.contains(fileType[fileType.length - 1])) {
							res.addAll(temp.getValue());
						}
					}
				}
				head = head.getNext();
			}
			if (res.size() != 0) {
				return res;
			} else
				return null;
		}
		if (searchType.equals("音乐")) {
			Node head = tree.getHead();
			List<String> listContent = new ArrayList<String>();
			listContent.add("mp3");
			listContent.add("xma");
			while (head != null) {
				List<Entry<Comparable, ArrayList<String>>> entries2 = head.getEntries();
				for (Entry<Comparable, ArrayList<String>> temp : entries2) {
					if (temp.getKey().toString().contains(".")) {
						String[] fileType = temp.getKey().toString().split("\\.");
						if (listContent.contains(fileType[fileType.length - 1])) {
							res.addAll(temp.getValue());
						}
					}
				}
				head = head.getNext();
			}
			if (res.size() != 0) {
				return res;
			} else
				return null;
		}
		if (searchType.equals("图片")) {
			Node head = tree.getHead();
			List<String> listContent = new ArrayList<String>();
			listContent.add("gif");
			listContent.add("bmp");
			listContent.add("jepg");
			while (head != null) {
				List<Entry<Comparable, ArrayList<String>>> entries2 = head.getEntries();
				for (Entry<Comparable, ArrayList<String>> temp : entries2) {
					if (temp.getKey().toString().contains(".")) {
						String[] fileType = temp.getKey().toString().split("\\.");
						if (listContent.contains(fileType[fileType.length - 1])) {
							res.addAll(temp.getValue());
						}
					}
				}
				head = head.getNext();
			}
			if (res.size() != 0) {
				return res;
			} else
				return null;
		}
		if (searchType.equals("文档")) {
			Node head = tree.getHead();
			List<String> listContent = new ArrayList<String>();
			listContent.add("txt");
			listContent.add("pdf");
			listContent.add("docx");
			while (head != null) {
				List<Entry<Comparable, ArrayList<String>>> entries2 = head.getEntries();
				for (Entry<Comparable, ArrayList<String>> temp : entries2) {
					if (temp.getKey().toString().contains(".")) {
						String[] fileType = temp.getKey().toString().split("\\.");
						if (listContent.contains(fileType[fileType.length - 1])) {
							res.addAll(temp.getValue());
						}
					}
				}
				head = head.getNext();
			}
			if (res.size() != 0) {
				return res;
			} else
				return null;
		}
		return null;
	}

	/** 删除节点 */
	protected void remove(Comparable key) {
		int index = -1;
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).getKey().compareTo(key) == 0) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			entries.remove(index);
		}
	}

	// 插入节点
	public void insertOrUpdate(Comparable key, ArrayList<String> arrayList, BplusTree tree) {
		// 如果是叶子节点
		if (this.isLeaf) {
			// 不需要分裂，直接插入或更新
			if (this.contains(key) || entries.size() < tree.getOrder()) {
				this.insertOrUpdate(key, arrayList);
				if (parent != null) {
					// 更新父节点(将最小的值存到父节点的关键字中作为索引)
					parent.updateInsert(tree);
				}

				// 需要分裂
			} else {
				// 分裂成左右两个节点
				Node left = new Node(true); // 这里的true代表这是一个叶子节点
				Node right = new Node(true);
				// 设置链接
				if (previous != null) { // 叶子节点的前节点不为空
					previous.setNext(left);
					left.setPrevious(previous);
				}
				if (next != null) { // 叶子节点的后节点不为空
					next.setPrevious(right);
					right.setNext(next);
				}
				if (previous == null) {
					tree.setHead(left); // 设置为叶子节点链表的头结点
				}

				left.setNext(right); // 链接分裂后的两个节点
				right.setPrevious(left);
				previous = null;
				next = null;

				// 左右两个节点关键字长度
				int leftSize = (tree.getOrder() + 1) / 2 + (tree.getOrder() + 1) % 2;
				int rightSize = (tree.getOrder() + 1) / 2;
				// 复制原节点关键字到分裂出来的新节点
				insertOrUpdate(key, arrayList);
				for (int i = 0; i < leftSize; i++) {
					left.getEntries().add(entries.get(i)); // 添加一个键值对
				}
				for (int i = 0; i < rightSize; i++) {
					right.getEntries().add(entries.get(leftSize + i));
				}

				// 如果不是根节点
				if (parent != null) {
					// 调整父子节点关系
					int index = parent.getChildren().indexOf(this);
					parent.getChildren().remove(this);
					left.setParent(parent);
					right.setParent(parent);
					parent.getChildren().add(index, left);
					parent.getChildren().add(index + 1, right);
					setEntries(null);
					setChildren(null);

					// 父节点插入或更新关键字
					parent.updateInsert(tree);
					setParent(null); // 把被分裂节点释放了
					// 如果是根节点
				} else {
					isRoot = false;
					Node parent = new Node(false, true); // false和true表示这个节点不是叶子节点
															// 是跟节点
					tree.setRoot(parent); // 设置树的根节点
					left.setParent(parent); // 设置分类出来的左节点的父节点
					right.setParent(parent);
					parent.getChildren().add(left);
					parent.getChildren().add(right);
					setEntries(null); // 释放被分裂节点的资源
					setChildren(null);

					// 更新根节点
					parent.updateInsert(tree);
				}

			}

			// 如果不是叶子节点，递归找到叶子节点
		} else {
			// 如果key小于等于节点最左边的key，沿第一个子节点继续搜索
			if (key.compareTo(entries.get(0).getKey()) <= 0) {
				children.get(0).insertOrUpdate(key, arrayList, tree);
				// 如果key大于节点最右边的key，沿最后一个子节点继续搜索
			} else if (key.compareTo(entries.get(entries.size() - 1).getKey()) >= 0) {
				children.get(children.size() - 1).insertOrUpdate(key, arrayList, tree);
				// 否则沿比key大的前一个子节点继续搜索
			} else {
				for (int i = 0; i < entries.size(); i++) {
					if (entries.get(i).getKey().compareTo(key) <= 0 && entries.get(i + 1).getKey().compareTo(key) > 0) {
						children.get(i).insertOrUpdate(key, arrayList, tree);
						break;
					}
				}
			}
		}
	}

	/**
	 * 插入节点后中间节点的更新 让每个节点的子节点个数合法 采用递归的思想
	 */
	protected void updateInsert(BplusTree tree) {

		validate(this, tree); // 校验关键字个数是否合法

		// 如果子节点数超出阶数，则需要分裂该节点，
		if (children.size() > tree.getOrder()) {
			// 分裂成左右两个节点
			Node left = new Node(false); // 这里的false代表不是叶子节点
			Node right = new Node(false); // 同理
			// 左右两个节点关键字长度
			int leftSize = (tree.getOrder() + 1) / 2 + (tree.getOrder() + 1) % 2;
			int rightSize = (tree.getOrder() + 1) / 2;
			// 复制子节点到分裂出来的新节点，并更新关键字
			for (int i = 0; i < leftSize; i++) {
				left.getChildren().add(children.get(i));
				left.getEntries().add(new SimpleEntry(children.get(i).getEntries().get(0).getKey(), null)); // 更新关键字
				children.get(i).setParent(left);// 更新父节点
			}
			for (int i = 0; i < rightSize; i++) {
				right.getChildren().add(children.get(leftSize + i));
				right.getEntries().add(new SimpleEntry(children.get(leftSize + i).getEntries().get(0).getKey(), null));
				children.get(leftSize + i).setParent(right);
			}

			// 如果不是根节点
			if (parent != null) {
				// 调整父子节点关系
				int index = parent.getChildren().indexOf(this); // 取得当前节点的位置
				parent.getChildren().remove(this); // 删除被分裂节点
				left.setParent(parent); // 给分裂后的左右节点设置父节点链接
				right.setParent(parent);
				parent.getChildren().add(index, left); // 给父节点的子节点集合中加上分裂后的左右节点
				parent.getChildren().add(index + 1, right);
				setEntries(null); // 释放当前节点的资源
				setChildren(null);

				// 递归校验父节点子节点个数是否合法
				parent.updateInsert(tree);
				setParent(null);
				// 如果是根节点
			} else {
				isRoot = false; // 设置根节点的标志为false
				Node parent = new Node(false, true);// 表面new一个根节点
				tree.setRoot(parent); // 设置这个树的根节点为parent
				left.setParent(parent); // 设置分裂出来的左右节点的父节点为parent
				right.setParent(parent);
				parent.getChildren().add(left); // 给parent节点的孩子节点添加分裂出来的左右节点
				parent.getChildren().add(right);
				setEntries(null); // 释放被分裂节点的资源
				setChildren(null);

				// 更新根节点
				parent.updateInsert(tree);
			}
		}
	}

	/**
	 * 调整节点关键字 校验、调整每个节点关键字个数合法
	 * 
	 */
	protected static void validate(Node node, BplusTree tree) {

		// 如果关键字个数与子节点个数相同
		if (node.getEntries().size() == node.getChildren().size()) {
			for (int i = 0; i < node.getEntries().size(); i++) {
				Comparable key = node.getChildren().get(i).getEntries().get(0).getKey();// 这里的关键字为每个子节点的最小值
				if (node.getEntries().get(i).getKey().compareTo(key) != 0) {// 如果这个最小值没有在节点的关键字集合中出现
					node.getEntries().remove(i); // 删除这个关键字，把最小值关键字加入到集合中
					node.getEntries().add(i, new SimpleEntry(key, null));
					if (!node.isRoot()) { // 如果当前节点不是根节点
						validate(node.getParent(), tree); // 递归校验节点关键字个数是否合法
					}
				}
			}
			// 如果子节点数不等于关键字个数但仍大于M / 2并且小于M，并且大于2
		} else if (node.isRoot() && node.getChildren().size() >= 2 || node.getChildren().size() >= tree.getOrder() / 2
				&& node.getChildren().size() <= tree.getOrder() && node.getChildren().size() >= 2) {
			node.getEntries().clear();
			for (int i = 0; i < node.getChildren().size(); i++) {
				Comparable key = node.getChildren().get(i).getEntries().get(0).getKey();
				node.getEntries().add(new SimpleEntry(key, null)); // 同上
				if (!node.isRoot()) { // 如果不是根节点
					validate(node.getParent(), tree); // 递归校验每个节点的关键字个数是否合法
				}
			}
		}
	}

	/**
	 * 删除节点后中间节点的更新
	 * 
	 * 调整当前节点的子节点个数是否合法
	 */
	protected void updateRemove(BplusTree tree) {

		validate(this, tree); // 校验每个节点的关键字个数是否合法

		// 如果子节点数小于M / 2或者小于2，则需要合并节点
		if (children.size() < tree.getOrder() / 2 || children.size() < 2) {
			if (isRoot) {
				// 如果是根节点并且子节点数大于等于2，OK
				if (children.size() >= 2) {
					return;
					// 否则与子节点合并，子节点数为1
				} else {
					Node root = children.get(0); // 获取子节点
					tree.setRoot(root); // 设置树的根节点
					root.setParent(null);
					root.setRoot(true);
					setEntries(null);
					setChildren(null);
				}
			} else { // 如果不是根节点
				// 计算前后节点
				int currIdx = parent.getChildren().indexOf(this); // 计算当前节点位置的位置
				int prevIdx = currIdx - 1; // 前兄弟节点
				int nextIdx = currIdx + 1; // 后兄弟节点
				Node previous = null, next = null; // 初始化
				if (prevIdx >= 0) {
					previous = parent.getChildren().get(prevIdx); // 给前兄弟节点赋值
				}
				if (nextIdx < parent.getChildren().size()) { // 父节点的子节点数还没饱和
					next = parent.getChildren().get(nextIdx); // 给后兄弟节点赋值
				}

				// 如果前节点子节点数大于M / 2并且大于2，则从其处借补
				if (previous != null && previous.getChildren().size() > tree.getOrder() / 2
						&& previous.getChildren().size() > 2) {
					int idx = previous.getChildren().size() - 1; // 获取到前兄弟节点的孩子集合的末尾值
					Node borrow = previous.getChildren().get(idx); // 把前兄弟节点的孩子节点的最后一个赋给这个借记节点
					previous.getChildren().remove(idx); // 前兄弟节点删除它的这个孩子节点
					borrow.setParent(this); // 给借记节点设置父节点为当前节点
					children.add(0, borrow); // 把借记节点加入到父节点的孩子节点首位
					validate(previous, tree); // 验证前兄弟节点键值数是否合法
					validate(this, tree); // 验证当前节点关键字个数是否合法
					parent.updateRemove(tree); // 递归调整当前节点的父节点

					// 如果后节点子节点数大于M / 2并且大于2，则从其处借补
				} else if (next != null && next.getChildren().size() > tree.getOrder() / 2
						&& next.getChildren().size() > 2) {
					// 后节点首位添加到末尾
					Node borrow = next.getChildren().get(0); // 把后兄弟节点的首位孩子节点赋给借记节点
					next.getChildren().remove(0); // 把后兄弟节点中的这个首位孩子节点删除
					borrow.setParent(this); // 设置借记节点的父节点为当前节点
					children.add(borrow); // 为当前节点的孩子节点集合中添加借记节点
					validate(next, tree); // 校验节点键值数是否合法
					validate(this, tree);
					parent.updateRemove(tree); // 递归校验父节点的子节点个数是否合法

					// 否则需要合并节点
				} else {
					// 同前面节点合并
					if (previous != null && (previous.getChildren().size() <= tree.getOrder() / 2
							|| previous.getChildren().size() <= 2)) {

						for (int i = previous.getChildren().size() - 1; i >= 0; i--) {
							Node child = previous.getChildren().get(i);
							children.add(0, child);
							child.setParent(this);
						}
						previous.setChildren(null);
						previous.setEntries(null);
						previous.setParent(null);
						parent.getChildren().remove(previous);
						validate(this, tree);
						parent.updateRemove(tree);

						// 同后面节点合并
					} else if (next != null
							&& (next.getChildren().size() <= tree.getOrder() / 2 || next.getChildren().size() <= 2)) {

						for (int i = 0; i < next.getChildren().size(); i++) {
							Node child = next.getChildren().get(i);
							children.add(child);
							child.setParent(this);
						}
						next.setChildren(null);
						next.setEntries(null);
						next.setParent(null);
						parent.getChildren().remove(next);
						validate(this, tree);
						parent.updateRemove(tree);
					}
				}
			}
		}
	}

	public void remove(Comparable key, BplusTree tree) {
		// 如果是叶子节点
		if (isLeaf) {

			// 如果不包含该关键字，则直接返回
			if (!contains(key)) {
				return;
			}

			// 如果既是叶子节点又是跟节点，直接删除
			if (isRoot) {
				remove(key);
			} else {
				// 如果关键字数大于M / 2，直接删除
				if (entries.size() > tree.getOrder() / 2 && entries.size() > 2) {
					remove(key);
				} else {
					// 如果自身关键字数小于M / 2，并且前节点关键字数大于M / 2，则从其处借补
					if (previous != null && previous.getEntries().size() > tree.getOrder() / 2
							&& previous.getEntries().size() > 2 && previous.getParent() == parent) {
						int size = previous.getEntries().size();
						Entry<Comparable, ArrayList<String>> entry = previous.getEntries().get(size - 1);// 把前驱叶节点的最后一个key值取出来
																											// 放到entry当中
						previous.getEntries().remove(entry);// 把前驱叶节点中的借出去的key值删除
						// 添加到首位
						this.entries.add(0, entry);
						remove(key);
						// 如果自身关键字数小于M / 2，并且后节点关键字数大于M / 2，则从其处借补
					} else if (next != null && next.getEntries().size() > tree.getOrder() / 2
							&& next.getEntries().size() > 2 && next.getParent() == parent) {
						Entry<Comparable, ArrayList<String>> entry = next.getEntries().get(0);
						next.getEntries().remove(entry);
						// 添加到末尾
						this.entries.add(entry);
						remove(key);
						// 否则需要合并叶子节点
					} else {
						// 同前面节点合并
						if (previous != null && (previous.getEntries().size() <= tree.getOrder() / 2
								|| previous.getEntries().size() <= 2) && previous.getParent() == parent) {
							for (int i = previous.getEntries().size() - 1; i >= 0; i--) {
								// 从末尾开始添加到首位
								this.entries.add(0, previous.getEntries().get(i));
							}
							remove(key);
							previous.setParent(null);
							previous.setEntries(null);
							parent.getChildren().remove(previous);
							// 更新链表
							if (previous.getPrevious() != null) { // 如果当前驱叶节点还有叶节点
								Node temp = previous; // 把前驱叶节点给临时temp节点
								temp.getPrevious().setNext(this); // 把前驱的前驱叶节点的后继节点设置为合并后的节点
								previous = temp.getPrevious(); // 设置合并后的节点的前驱叶节点为temp的前驱节点
								temp.setPrevious(null); // 把被合并的节点删除
								temp.setNext(null);
							} else {
								tree.setHead(this); // 如果当前前驱叶节点没有前驱叶节点了，设置合并后的叶节点为头结点
								previous.setNext(null); // 删除被合并叶节点
								previous = null; // 释放被合并叶节点资源
							}
							// 同后面节点合并
						} else if (next != null
								&& (next.getEntries().size() <= tree.getOrder() / 2 || next.getEntries().size() <= 2)
								&& next.getParent() == parent) {
							for (int i = 0; i < next.getEntries().size(); i++) {
								// 从首位开始添加到末尾
								this.entries.add(next.getEntries().get(i)); // 把叶节点的后继叶节点合并了
							}
							remove(key); // 删除键值
							next.setParent(null); // 释放后继节点的链接
							next.setEntries(null); // 置空后继节点的集合
							parent.getChildren().remove(next); // 在当前叶节点的父节点的孩子节点集合中删除被合并节点
							// 更新链表 更改链表指向
							if (next.getNext() != null) {
								Node temp = next;
								temp.getNext().setPrevious(this);
								next = temp.getNext();
								temp.setPrevious(null);
								temp.setNext(null);
							} else {
								next.setPrevious(null);
								next = null;
							}
						}
					}
				}
				parent.updateRemove(tree);
			}
			// 如果不是叶子节点
		} else {
			// 如果key小于等于节点最左边的key，沿第一个子节点继续搜索
			if (key.compareTo(entries.get(0).getKey()) <= 0) {
				children.get(0).remove(key, tree); // 递归找到对应叶子节点
				// 如果key大于节点最右边的key，沿最后一个子节点继续搜索
			} else if (key.compareTo(entries.get(entries.size() - 1).getKey()) >= 0) {
				children.get(children.size() - 1).remove(key, tree);
				// 否则沿比key大的前一个子节点继续搜索
			} else {
				for (int i = 0; i < entries.size(); i++) {
					if (entries.get(i).getKey().compareTo(key) <= 0 && entries.get(i + 1).getKey().compareTo(key) > 0) {
						children.get(i).remove(key, tree);
						break;
					}
				}
			}
		}
	}

	/** 判断当前节点是否包含该关键字 */
	protected boolean contains(Comparable key) {
		for (Entry<Comparable, ArrayList<String>> entry : entries) {
			if (entry.getKey().compareTo(key) == 0) {
				return true;
			}
		}
		return false;
	}

	/** 插入到当前节点的关键字中 */
	protected void insertOrUpdate(Comparable key, ArrayList<String> arrayList) {
		Entry<Comparable, ArrayList<String>> entry = new SimpleEntry<Comparable, ArrayList<String>>(key, arrayList);
		// 如果关键字列表长度为0，则直接插入
		if (entries.size() == 0) {
			entries.add(entry);
			return;
		}
		// 否则遍历列表
		for (int i = 0; i < entries.size(); i++) {
			// 如果该关键字键值已存在，则更新
			if (entries.get(i).getKey().compareTo(key) == 0) {
				entries.get(i).setValue(arrayList);
				return;
				// 否则插入
			} else if (entries.get(i).getKey().compareTo(key) > 0) {
				// 插入到链首
				if (i == 0) {
					entries.add(0, entry);
					return;
					// 插入到中间
				} else {
					entries.add(i, entry);
					return;
				}
			}

		}
		// 插入到末尾
		entries.add(entries.size(), entry);
	}

	public Node getPrevious() {
		return previous;
	}

	public void setPrevious(Node previous) {
		this.previous = previous;
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public List<Entry<Comparable, ArrayList<String>>> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry<Comparable, ArrayList<String>>> entries) {
		this.entries = entries;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("isRoot: ");
		sb.append(isRoot);
		sb.append(", ");
		sb.append("isLeaf: ");
		sb.append(isLeaf);
		sb.append(", ");
		sb.append("keys: ");
		for (Entry entry : entries) {
			sb.append(entry.getKey());
			sb.append(", ");
		}
		sb.append(", ");
		return sb.toString();

	}

	public int size() {
		return entries.size();
	}

	public Entry<Comparable, ArrayList<String>> entryAt(int index) {
		return entries.get(index);
	}

	public Node childAt(int index) {
		if (isLeaf())
			throw new UnsupportedOperationException("Leaf node doesn't have children.");
		return children.get(index);
	}

}
