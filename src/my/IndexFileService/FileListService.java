package my.IndexFileService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import my.BplusTreeModel.BplusTree;
import my.BplusTreeModel.Node;

public class FileListService {
	Map<String, ArrayList<String>> fileListMap = new HashMap<String, ArrayList<String>>();
	HashSet<String> indexSet = new HashSet<String>(); // 只用来存放key值 文件名或目录名
	// 全局索引存放的位置
	private static String IndexPath = "G:/全盘索引.txt";
	// 需要生成的索引树
	private BplusTree treeDisk = new BplusTree(30); // 阶数为30，磁盘树
	private static BplusTree treeAll = new BplusTree(30);// 全局树

	// 扫描特定路径下的所有文件
	public Map<String, ArrayList<String>> scanFiles(String path) {
		LinkedList<File> list = new LinkedList<File>();
		File dir = new File(path);
		if (dir.exists() && dir.canRead()) {
			File[] file = dir.listFiles();
			if (file == null || file.length == 0) {
				return null;
			} else {
				for (File temp : file) {
					ArrayList<String> filePaths = new ArrayList<String>();
					if (temp.isDirectory()) {
						// 把第一层的目录，全部放入链表
						list.add(temp.getAbsoluteFile());
						if (indexSet.contains(temp.getName())) {
							fileListMap.get(temp.getName()).add(temp.getAbsolutePath());
						} else {
							indexSet.add(temp.getName());
							filePaths.add(temp.getAbsolutePath());
							fileListMap.put(temp.getName(), filePaths);
						}
					} else {
						if (indexSet.contains(temp.getName())) {
							fileListMap.get(temp.getName()).add(temp.getAbsolutePath());
						} else {
							indexSet.add(temp.getName());
							filePaths.add(temp.getAbsolutePath());
							fileListMap.put(temp.getName(), filePaths);
						}
					}
				}
				// 循环遍历链表
				while (!list.isEmpty()) {
					// 把链表的第一个记录删除
					File tmp = list.removeFirst();
					// 如果删除的目录是一个路径的话
					if (tmp.isDirectory()) {
						// 列出这个目录下的文件到数组中
						file = tmp.listFiles();
						if (file == null) {// 空目录
							continue;
						}
						// 遍历文件数组
						for (File temp : file) {
							ArrayList<String> filePaths = new ArrayList<String>();
							if (temp.isDirectory()) {
								// 如果遍历到的是目录，则将继续被加入链表
								list.add(temp.getAbsoluteFile());
								if (indexSet.contains(temp.getName())) {
									fileListMap.get(temp.getName()).add(temp.getAbsolutePath());
								} else {
									indexSet.add(temp.getName());
									filePaths.add(temp.getAbsolutePath());
									fileListMap.put(temp.getName(), filePaths);
								}
							} else {
								if (indexSet.contains(temp.getName())) {
									fileListMap.get(temp.getName()).add(temp.getAbsolutePath());
								} else {
									indexSet.add(temp.getName());
									filePaths.add(temp.getAbsolutePath());
									fileListMap.put(temp.getName(), filePaths);
								}
							}
						}
					}
				}
			}
		}
		return fileListMap;
	}

	// 生成单个磁盘树
	public BplusTree constructTree(String root, String index)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		Map<String, ArrayList<String>> res = new HashMap<String, ArrayList<String>>();
		// 得到本地索引文件对象
		File diskindex = new File(root + root.substring(0, 1) + "盘索引.txt");
		if (index.equals("true")) {
			res = scanFiles(root);
			String temp = root.substring(0, 1);
			if(root.contains("C")){
				saveIndex("G:" + File.separator + temp + "盘索引.txt", res);
			}else{
				saveIndex(temp + ":" + File.separator + temp + "盘索引.txt", res);
			}
		} else {
			if (diskindex.exists() && diskindex.length() != 0) {
				res = readFileIndex(diskindex.getAbsolutePath().toString());
			} else {
				res = scanFiles(root);
			}
		}
		for (Entry<String, ArrayList<String>> temp : res.entrySet()) {
			treeDisk.insertOrUpdate(temp.getKey(), temp.getValue());
		}
		return treeDisk;
	}

	// 得到全盘文件
	public Map<String, ArrayList<String>> scanAllFiles() {
		File[] fileRoots = File.listRoots();
		if (fileRoots.length != 0) {
			for (int i = 0; i < fileRoots.length; i++) {
				String path = fileRoots[i].getPath();
				fileListMap = scanFiles(path);
			}
		}
		if (fileRoots.length != 0)
			return fileListMap;
		else
			return null;
	}

	// 生成全局树
	public BplusTree constructAllTree(String index) {
		File fileAllIndex = new File(IndexPath); // 创建保存全局索引的文件
		Map<String, ArrayList<String>> res = new HashMap<String, ArrayList<String>>();
		if (index.equals("true")) {
			res = scanAllFiles();
		} else {
			if (fileAllIndex.exists() && fileAllIndex.length() != 0) {
				try {
					res = readFileIndex(IndexPath);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			} else {
				res = scanAllFiles();
			}
		}
		for (Entry<String, ArrayList<String>> temp : res.entrySet()) {
			treeAll.insertOrUpdate(temp.getKey(), temp.getValue());
		}

		return treeAll;
	}

	// 保存索引
	public void saveIndex(String fileName, Map<String, ArrayList<String>> fileMap)
			throws FileNotFoundException, IOException {
		ObjectOutputStream objectOutputStream = null;
		objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
		objectOutputStream.writeObject(fileMap);
		objectOutputStream.flush();
		objectOutputStream.close();
	}

	// 保存索引
	public void saveAllIndex(String fileName, Map<String, ArrayList<String>> fileMap)
			throws FileNotFoundException, IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(fileName, true);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(fileMap);
		objectOutputStream.flush();
		objectOutputStream.close();
	}

	/*
	 * 所有的数据项都是存在叶子节点，而叶子节点之间有指向。所以遍历b+树的话 也就只需要遍历叶子节点即可
	 * 
	 */
	public static Map<String, ArrayList<String>> getMap(BplusTree tree) {
		Map<String, ArrayList<String>> res = new HashMap<String, ArrayList<String>>();
		Node head = tree.getHead();
		while (head != null) {
			List<Entry<Comparable, ArrayList<String>>> list = head.getEntries();
			Iterator<Entry<Comparable, ArrayList<String>>> iterator = list.iterator();

			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String key = entry.getKey().toString();
				ArrayList<String> value = (ArrayList<String>) entry.getValue();
				res.put(key, value);
			}
			for (int i = 0; i < list.size(); i++) {
				String key = list.get(i).getKey().toString();
				ArrayList<String> Lujing = list.get(i).getValue();
				res.put(key, Lujing);
			}
			head = head.getNext();
		}
		return res;
	}

	// 读取索引文件内容
	public Map<String, ArrayList<String>> readFileIndex(String fileName)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream objectInputStream = null;
		Object obj = null;
		Map<String, ArrayList<String>> fileListMap = new HashMap<String, ArrayList<String>>();
		objectInputStream = new ObjectInputStream(new FileInputStream(new File(fileName)));
		obj = objectInputStream.readObject();
		if (obj == null) {
			System.out.println("索引文件内容为空");
			objectInputStream.close();
			return null;
		}
		if (obj != null) {
			Map<String, ArrayList<String>> resMap = (Map<String, ArrayList<String>>) obj;
			for (Entry<String, ArrayList<String>> temp : resMap.entrySet()) { // entrySet()方法返回反应map键值的映射关系
				fileListMap.put(temp.getKey(), temp.getValue());
			}
			objectInputStream.close();
		}
		return fileListMap;
	}
}
