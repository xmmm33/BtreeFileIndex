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
 * 处理用户选择了类型查询但是不需要重建索引的逻辑操作
 * 
 * @author hmj
 *
 */
public class Handler4 extends Handler{
	/**结果List集合 */
	List<String> resultList = new ArrayList<>(); 
	@Override
	public ArrayList<String> requestFilename(String filename, String vague, String index, String root, String type) {
		if(!filename.isEmpty() && !vague.equals("true") && type.equals("true") && !index.equals("true") && !root.isEmpty()){
			if (root.equals("全盘")) {
				long startTime = System.nanoTime();
				for(Entry<String, BplusTree> temp : BPTreeIndexFileServlet.treemap.entrySet()){
					if(temp.getKey().indexOf("驱动器")> -1)continue;
					BPTreeIndexFileServlet.treeDisk = BPTreeIndexFileServlet.treemap.get(temp.getKey());
					if(BPTreeIndexFileServlet.treeDisk.getOnTypeByExist(filename,BPTreeIndexFileServlet.treeDisk)==null)continue;
					resultList.addAll(BPTreeIndexFileServlet.treeDisk.getOnTypeByInput(filename, BPTreeIndexFileServlet.treeDisk));
				}
//				resultList = treeAll.getOnTypeByInput(filename, treeAll); // 得到全局索引树
				long endTime = System.nanoTime();
				System.out.println("查询耗时" + (endTime - startTime) + "ns");
				return (ArrayList<String>) resultList;
			} else {
				long startTime = System.nanoTime();
				BPTreeIndexFileServlet.treeDisk = BPTreeIndexFileServlet.treemap.get(root); // 得到磁盘索引树
				resultList = BPTreeIndexFileServlet.treeDisk.getOnTypeByInput(filename, BPTreeIndexFileServlet.treeDisk);
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
