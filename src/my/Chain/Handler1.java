package my.Chain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import my.IndexFileService.FileListService;
import my.IndexServlet.BPTreeIndexFileServlet;

/**
 * 处理当用户选择了模糊查询 而且要重建索引的逻辑操作
 * 
 * @author hmj
 *
 */
public class Handler1 extends Handler{
	/**结果List集合 */
	List<String> resultList = new ArrayList<>(); 
	@Override
	public ArrayList<String> requestFilename(String filename, String vague, String index, String root, String type) {
		if(!filename.isEmpty() && vague.equals("true") && !type.equals("true")  &&index.equals("true")&& !root.isEmpty()){
			if(root.equals("全盘")){
				long startTime = System.nanoTime();
				BPTreeIndexFileServlet.treeAll = new FileListService().constructAllTree(index); // 重新建立全局索引树
				resultList = BPTreeIndexFileServlet.treeAll.getOnVague(filename, BPTreeIndexFileServlet.treeAll);
				long endTime = System.nanoTime();
				System.out.println("查询耗时" + (endTime - startTime) + "ns");
				return (ArrayList<String>) resultList;
			}
			else{
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
					e.printStackTrace();
				}
				resultList = BPTreeIndexFileServlet.treeDisk.getOnVague(filename, BPTreeIndexFileServlet.treeDisk);
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
