package my.Chain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import my.IndexFileService.FileListService;
import my.IndexServlet.BPTreeIndexFileServlet;

public class Handler3 extends Handler{
	List<String> resultList = new ArrayList<>(); 
	@Override
	public ArrayList<String> requestFilename(String filename, String vague, String index, String root, String type) {
		if(!filename.isEmpty() && !vague.equals("true") && type.equals("true") && index.equals("true") && !root.isEmpty()){
			if (root.equals("全盘")) {
				long startTime = System.nanoTime();
				BPTreeIndexFileServlet.treeAll = new FileListService().constructAllTree(index);// 重建全局索引树
				resultList = BPTreeIndexFileServlet.treeAll.getOnTypeByInput(filename, BPTreeIndexFileServlet.treeAll);// 查询得到文件路径
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
					e.printStackTrace();
				}
				resultList = BPTreeIndexFileServlet.treeDisk.getOnTypeByInput(filename, BPTreeIndexFileServlet.treeDisk);// 查询得到文件路径
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
