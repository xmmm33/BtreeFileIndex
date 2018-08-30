package my.IndexFileService;

import java.io.FileNotFoundException;
import java.io.IOException;
import my.BplusTreeModel.BplusTree;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyAdapter;
import net.contentobjects.jnotify.JNotifyException;
import my.IndexServlet.*;

public class FileMonitorAndFileDynamicUpdateService extends JNotifyAdapter implements Runnable{
	
	/** 关注目录的事件 */
	int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;
	/** 是否监视子目录，即级联监视 */
	boolean watchSubtree = true;
	/** 监听程序Id */
	public int watchID;
	/** 将内存中的内存树写到硬盘的工具类 */
	static FileListService fileList = new FileListService();
	/** 监控的目录 */
	private String jnotifyPath;
	private static int cn=0;
	public String getJnotifyPath() {
		return jnotifyPath;
	}
	
	public FileMonitorAndFileDynamicUpdateService(){
		
	}
	public FileMonitorAndFileDynamicUpdateService(String monitorPath){
		this.jnotifyPath = monitorPath;
	}
	public void startWatch(){
		try {
			this.watchID = JNotify.addWatch(jnotifyPath, mask, watchSubtree, this);
			System.out.println(jnotifyPath + "文件监控服务启动成功");
		} catch (JNotifyException e) {
			e.printStackTrace();
		}
		//死循环 一直进行文件监控
		while(true){
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
	/**
	 * 当监听目录下一旦有新的文件被创建，则即触发该事件
	 * 
	 * @param wd
	 *            监听线程id
	 * @param rootPath
	 *            监听目录
	 * @param name
	 *            文件名称
	 */
	public void fileCreated(int wd,String rootPath,String name){
		if(rootPath.substring(0,1).equalsIgnoreCase(jnotifyPath.substring(0,1))){
			 System.err.println(jnotifyPath + "fileCreated, the created file path is " +
			 rootPath + "\\" + name);
			 String root = rootPath.substring(0,3).toUpperCase().toString();
			 FileListService fileListService = new FileListService();
			 try {
				 if(rootPath.contains("C")){
					 if(cn%10==0){
						 BPTreeIndexFileServlet.treemap.put(root, fileListService.constructTree(root, "true")); 
					 }
					 cn++;
				 }else{
					 BPTreeIndexFileServlet.treemap.put(root, fileListService.constructTree(root, "true"));
				 }
//				 BPTreeIndexFileServlet.treeAll = fileListService.constructAllTree("true");
				System.out.println("动态更新索引成功");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void fileRenamed(int wd,String rootPath,String oldName,String newName){
		if(rootPath.substring(0,1).equalsIgnoreCase(jnotifyPath.substring(0,1))){
			 System.err.println(jnotifyPath + "旧文件为" +
			 rootPath + "\\" + oldName
			 + "新文件为" + rootPath + "\\" + newName);
			 String root = rootPath.substring(0,3).toUpperCase().toString();
			 FileListService fileListService = new FileListService();
			 try {
				 if(rootPath.contains("C")){
					 if(cn%10==0){
						 BPTreeIndexFileServlet.treemap.put(root, fileListService.constructTree(root, "true")); 
					 }
					 cn++;
				 }else{
					 BPTreeIndexFileServlet.treemap.put(root, fileListService.constructTree(root, "true"));
				 }
//				 BPTreeIndexFileServlet.treeAll = fileListService.constructAllTree("true");
				System.out.println("动态更新索引成功");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	 
		}
	}
	public void fileDeleted(int wd, String rootPath, String name) {
		if(rootPath.substring(0,1).equalsIgnoreCase(jnotifyPath.substring(0,1))){
			// 如果删除的是树
			 System.err.println(jnotifyPath + "fileDeleted , the deleted file path is " +
			 rootPath + "\\" + name);
			 String root = rootPath.substring(0,3).toUpperCase().toString();
			 FileListService fileListService = new FileListService();
			 try {
				 if(rootPath.contains("C")){
					 if(cn%10==0){
						 BPTreeIndexFileServlet.treemap.put(root, fileListService.constructTree(root, "true")); 
					 }
					 cn++;
				 }else{
					 BPTreeIndexFileServlet.treemap.put(root, fileListService.constructTree(root, "true"));
				 }
//				 BPTreeIndexFileServlet.treeAll = fileListService.constructAllTree("true");
				System.out.println("动态更新索引成功");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		try {
			startWatch();
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
	}
