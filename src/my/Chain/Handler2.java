package my.Chain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import my.BplusTreeModel.BplusTree;
import my.IndexFileService.FileListService;
import my.IndexServlet.BPTreeIndexFileServlet;

public class Handler2 extends Handler {
	List<String> resultList = new ArrayList<>(); 
	@Override
	public ArrayList<String> requestFilename(String filename, String vague, String index, String root, String type) {
		if(!filename.isEmpty() && vague.equals("true") && !index.equals("true") && !root.isEmpty()){
			if(root.equals("全盘")){
				long startTime = System.nanoTime();
				for(Map.Entry<String, BplusTree> temp : BPTreeIndexFileServlet.treemap.entrySet()){
					if(temp.getKey().indexOf("驱动器")> -1)continue;
					BPTreeIndexFileServlet.treeDisk = BPTreeIndexFileServlet.treemap.get(temp.getKey());
					if(BPTreeIndexFileServlet.treeDisk.getOnVague(filename,BPTreeIndexFileServlet.treeDisk)==null)continue; //防止抛空指针异常
					resultList.addAll(BPTreeIndexFileServlet.treeDisk.getOnVague(filename, BPTreeIndexFileServlet.treeDisk));
				}
//				resultList = treeAll.getOnVague(filename, treeAll); // 通过全局树种模糊查询文件路径
				long endTime = System.nanoTime();
				System.out.println("查询耗时" + (endTime - startTime) + "ns");
				return (ArrayList<String>) resultList;
			}
			else{
				long startTime = System.nanoTime();
				BPTreeIndexFileServlet.treeDisk = BPTreeIndexFileServlet.treemap.get(root); // 得到某一磁盘树
				resultList = BPTreeIndexFileServlet.treeDisk.getOnVague(filename, BPTreeIndexFileServlet.treeDisk); // 通过某一磁盘树模糊查询得到文件路径
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
