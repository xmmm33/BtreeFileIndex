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
 * 处理用户选择了直接通过输入文件名查询没选择模糊和类型查询也没选择重建索引的逻辑操作
 * 
 * @author hmj
 *
 */
public class Handler6 extends Handler{
	/**结果List集合 */
	List<String> resultList = new ArrayList<>(); 
	@Override
	public ArrayList<String> requestFilename(String filename, String vague, String index, String root, String type) {
		if(!filename.isEmpty() && !vague.equals("true") && !type.equals("true") && !index.equals("true") && !root.isEmpty()){
			if (root.equals("全盘")) {
				long startTime = System.nanoTime();
				for(Entry<String, BplusTree> temp : BPTreeIndexFileServlet.treemap.entrySet()){
					if(temp.getKey().indexOf("驱动器")> -1)continue;
					BPTreeIndexFileServlet.treeDisk = BPTreeIndexFileServlet.treemap.get(temp.getKey());
					if(BPTreeIndexFileServlet.treeDisk.get(filename)==null)continue;
					resultList.addAll(BPTreeIndexFileServlet.treeDisk.get(filename)); 
				}
//				resultList = treeAll.get(filename); // 在全局树中查询文件路径
				long endTime = System.nanoTime();
				System.out.println("查询耗时" + (endTime - startTime) + "ns");
				return (ArrayList<String>) resultList;
			} else {
				long startTime = System.nanoTime();
				BPTreeIndexFileServlet.treeDisk = BPTreeIndexFileServlet.treemap.get(root); 
				resultList = BPTreeIndexFileServlet.treeDisk.get(filename); // 查询文件路径
				long endTime = System.nanoTime();
				System.out.println("查询耗时" + (endTime - startTime) + "ns");
				return (ArrayList<String>) resultList;
			}
		}
		else{
			return (ArrayList<String>) resultList;
		}
		
	}
}
