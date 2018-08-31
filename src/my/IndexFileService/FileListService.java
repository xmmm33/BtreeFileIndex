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

/**
 * 进行文件遍历 、B+树的构建、 保存索引、 得到某一个B+树的数据map 、读取文件索引
 * 
 * @author hmj
 *
 */
public class FileListService {
	/** 结果集map */
	Map<String, ArrayList<String>> fileListMap = new HashMap<String, ArrayList<String>>();
	/** 索引存放集合*/
	HashSet<String> indexSet = new HashSet<String>(); // 只用来存放key值 文件名或目录名
	/** 全局索引存放位置*/
	private static String IndexPath = "G:/全盘索引.txt";
	
	/** 得到磁盘B+树对象*/
	private BplusTree treeDisk = new BplusTree(30); // 阶数为30，磁盘树
	/** 得到全局B+树对象*/
	private static BplusTree treeAll = new BplusTree(30);// 全局树
	
	/**
	 * 非递归扫描某一路径下的所有文件 返回一个map
	 * 
	 * @param path
	 * 			待搜索的路径
	 * @return
	 * 			返回结果map
	 */
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

	/**
	 * 建立某一个磁盘的磁盘B+树
	 * 
	 * @param root
	 * 			需要建树的磁盘
	 * @param index
	 * 			是否需要重建索引的标志位
	 * @return
	 * 			返回B+树对象
	 * @throws FileNotFoundException
	 * 			抛出文件未找到异常
	 * @throws IOException
	 * 			抛出IO异常
	 * @throws ClassNotFoundException
	 * 			抛出Class未找到异常
	 */
	public BplusTree constructTree(String root, String index)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		Map<String, ArrayList<String>> res = new HashMap<String, ArrayList<String>>();
		File diskindex;
		// 得到本地索引文件对象
		if(root.contains("C")){
			diskindex = new File("G:/C盘索引.txt");
		}else{
			diskindex = new File(root + root.substring(0, 1) + "盘索引.txt");
		}
		
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

	/**
	 * 扫描所有磁盘的文件
	 * 
	 * 通过遍历每个磁盘 调用scanFiles方法 返回一个map
	 * 
	 * @return
	 *			返回结果map
	 */
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

	/**
	 * 构建全磁盘B+树
	 * 
	 * @param index
	 * 			是否需要重建索引的标志位
	 * @return
	 * 			返回生成的全局B+树
	 */
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

	/**
	 * 把磁盘B+树的索引持久化到磁盘上
	 * 
	 * @param fileName
	 * 				索引存储文件
	 * @param fileMap
	 * 				待存储的索引
	 * @throws FileNotFoundException
	 * 				抛出文件未找到异常
	 * @throws IOException
	 * 				抛出IO异常
	 */
	public void saveIndex(String fileName, Map<String, ArrayList<String>> fileMap)
			throws FileNotFoundException, IOException {
		ObjectOutputStream objectOutputStream = null;
		objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
		objectOutputStream.writeObject(fileMap);
		objectOutputStream.flush();
		objectOutputStream.close();
	}

	/**
	 * 把全局B+树的索引持久到磁盘上
	 * 
	 * @param fileName
	 * 				索引存储文件
	 * @param fileMap
	 * 				待存储的索引
	 * @throws FileNotFoundException
	 * 				抛出文件未找到异常
	 * @throws IOException
	 * 				抛出IO异常
	 */
	public void saveAllIndex(String fileName, Map<String, ArrayList<String>> fileMap)
			throws FileNotFoundException, IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(fileName, true);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(fileMap);
		objectOutputStream.flush();
		objectOutputStream.close();
	}

	/**
	 * 遍历某一个B+树的关键字集合 把数据放入map中
	 * 
	 * @param tree
	 * 			需要遍历的B+树
	 * @return
	 * 			返回结果map
	 */
	public static Map<String, ArrayList<String>> getMap(BplusTree tree) {
		Map<String, ArrayList<String>> res = new HashMap<String, ArrayList<String>>();
		Node head = tree.getHead();
		while (head != null) {
			List<Entry<Comparable, ArrayList<String>>> list = head.getEntries();
//			Iterator<Entry<Comparable, ArrayList<String>>> iterator = list.iterator();

//			while (iterator.hasNext()) {
//				Map.Entry entry = (Map.Entry) iterator.next();
//				String key = entry.getKey().toString();
//				ArrayList<String> value = (ArrayList<String>) entry.getValue();
//				res.put(key, value);
//			}
			for (int i = 0; i < list.size(); i++) {
				String key = list.get(i).getKey().toString();
				ArrayList<String> Lujing = list.get(i).getValue();
				res.put(key, Lujing);
			}
			head = head.getNext();
		}
		return res;
	}

	/**
	 * 读取索引文件
	 * 
	 * @param fileName
	 * 				索引存储文件
	 * @return
	 * 				返回结果map
	 * @throws FileNotFoundException
	 * 				抛出文件未找到异常
	 * @throws IOException
	 * 				抛出IO异常
	 * @throws ClassNotFoundException
	 * 				抛出Class未找到异常
	 */
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
