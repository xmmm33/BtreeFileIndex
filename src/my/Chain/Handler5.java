package my.Chain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import my.BplusTreeModel.BplusTree;
import my.IndexFileService.FileListService;
import my.IndexServlet.BPTreeIndexFileServlet;

/**
 * 处理用户没选择模糊查询 也没选择类型查询 通过输入的文件名查询但是需要重建索引的逻辑操作
 * 
 * 
 * @author hmj
 *
 */
public class Handler5 extends Handler{
	/**结果List集合 */
	List<String> resultList = new ArrayList<>(); 
	@Override
	public ArrayList<String> requestFilename(String filename, String vague, String index, String root, String type) {
		if(!filename.isEmpty() && !vague.equals("true") && !type.equals("true") && index.equals("true") && !root.isEmpty()){
			if (root.equals("全盘")) {
				long startTime = System.nanoTime();
				BPTreeIndexFileServlet.treeAll = new FileListService().constructAllTree(index);// 重建全局树
				resultList = BPTreeIndexFileServlet.treeAll.get(filename);// 查询文件路径
				long endTime = System.nanoTime();
				System.out.println("查询耗时" + (endTime - startTime) + "ns");
				return (ArrayList<String>) resultList;
			} else {
				long startTime = System.nanoTime();
				try {
					try {
						BPTreeIndexFileServlet.treeDisk = new FileListService().constructTree(root, index);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resultList = BPTreeIndexFileServlet.treeDisk.get(filename);// 查询文件路径
				long endTime = System.nanoTime();
				System.out.println("查询耗时" + (endTime - startTime) + "ns");
				return (ArrayList<String>) resultList;
			}
		}
		else{
			return next.requestFilename(filename, vague, index, root, type);
//			return (ArrayList<String>) resultList;
		}
		
	}
}
