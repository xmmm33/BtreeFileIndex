package my.IndexFileService;

import java.util.ArrayList;
import my.IndexServlet.BPTreeIndexFileServlet;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyAdapter;
import net.contentobjects.jnotify.JNotifyException;

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
			 System.out.println(jnotifyPath + "fileCreated, the created file path is " +
			 rootPath + name);
			 String root = rootPath.substring(0,3).toUpperCase().toString();
			 String fileName = name.substring(name.lastIndexOf("\\")+1);
//			 FileListService fileListService = new FileListService();
/*			 try {
				 if(rootPath.contains("C")){
					 if(cn%15==0){
						 BPTreeIndexFileServlet.treemap.put(root, fileListService.constructTree(root, "true")); 
					 }
					 cn++;
				 }else{
					 BPTreeIndexFileServlet.treemap.put(root, fileListService.constructTree(root, "true"));
				 }
//				 BPTreeIndexFileServlet.treeAll = fileListService.constructAllTree("true");
				System.out.println("动态更新索引成功" + cn);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			ArrayList<String> treediskList = (ArrayList<String>) BPTreeIndexFileServlet.treemap.get(root).get(fileName);
			if(treediskList != null){ 
				BPTreeIndexFileServlet.treemap.get(root).get(fileName).clear();//不清除的话 后面查询同名文件 会出现以前同名文件留下的路径
				BPTreeIndexFileServlet.treemap.get(root).get(fileName).add(rootPath  + name);
				System.out.println("索引更新成功");
			}else{
				treediskList = new ArrayList<String>();
				treediskList.add((rootPath  + name).toString());
				BPTreeIndexFileServlet.treemap.get(root).insertOrUpdate(fileName, treediskList);
				System.out.println("索引更新成功");
			}
//			ArrayList<String> treeallList = (ArrayList<String>) BPTreeIndexFileServlet.treeAll.get(fileName);
//			if(treeallList != null){
//				BPTreeIndexFileServlet.treeAll.get(fileName).add(rootPath + "\\" + name);
//			}else{
//				BPTreeIndexFileServlet.treeAll.insertOrUpdate(fileName, (ArrayList<String>) Arrays.asList(rootPath + "\\" + name));
//			}
		}
	}
	
	public void fileRenamed(int wd,String rootPath,String oldName,String newName){
		if(rootPath.substring(0,1).equalsIgnoreCase(jnotifyPath.substring(0,1))){
			 System.out.println(jnotifyPath + "旧文件为" +
			 rootPath  + oldName
			 + "新文件为" + rootPath + "\\" + newName);
			 String root = rootPath.substring(0,3).toUpperCase().toString();
//			 FileListService fileListService = new FileListService();
			 
			 String oldfileName = oldName.substring(oldName.lastIndexOf("\\")+1);
			 String newfileName = newName.substring(newName.lastIndexOf("\\")+1);
			 String temp = oldName.substring(0,oldName.lastIndexOf("\\")+1);
			 temp = root +"\\" + temp + newfileName;
			 
			 ArrayList<String> treediskList = (ArrayList<String>) BPTreeIndexFileServlet.treemap.get(root).get(oldfileName);
			 if(treediskList != null){
				 treediskList.clear();
			     treediskList.add(temp);
			 }
			 BPTreeIndexFileServlet.treemap.get(root).insertOrUpdate(newfileName, treediskList);
			 //测试文件索引更新成功没有
//			 ArrayList<String> treediskListnew = (ArrayList<String>) BPTreeIndexFileServlet.treemap.get(root).get(newfileName);
//			 BPTreeIndexFileServlet.treemap.get(root).get(oldfileName).clear();
			 BPTreeIndexFileServlet.treemap.get(root).remove(oldfileName);
			 System.out.println("索引更新成功");
/*			 try {
				 if(rootPath.contains("C")){
					 if(cn%15==0){
						 BPTreeIndexFileServlet.treemap.put(root, fileListService.constructTree(root, "true")); 
					 }
					 cn++;
				 }else{
					 BPTreeIndexFileServlet.treemap.put(root, fileListService.constructTree(root, "true"));
				 }
//				 BPTreeIndexFileServlet.treeAll = fileListService.constructAllTree("true");
				System.out.println("动态更新索引成功"+cn);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}*/	 
		}
	}
	public void fileDeleted(int wd, String rootPath, String name) {
		if(rootPath.substring(0,1).equalsIgnoreCase(jnotifyPath.substring(0,1))){
			// 如果删除的是树
			 System.out.println(jnotifyPath + "fileDeleted , the deleted file path is " +
			 rootPath + name);
			 String root = rootPath.substring(0,3).toUpperCase().toString();
//			 FileListService fileListService = new FileListService();
			 String fileName = name.substring(name.lastIndexOf("\\")+1);
			 String fileLujing =root +  name.substring(0,name.lastIndexOf("\\")+1);
			 //测试删除文件路径是否得到
			 ArrayList<String> treeList = (ArrayList<String>) BPTreeIndexFileServlet.treemap.get(root).get(fileName);
			 //排除同名文件 但路径不同 删除的时候正确删除
			 if(treeList.size() != 1 && !treeList.isEmpty()){
				 for(int i=0; i<treeList.size(); i++){
					 if(treeList.get(i).toString().substring(0, treeList.get(i).toString().lastIndexOf("\\")+1).equals(fileLujing)){
						 String temp = treeList.get(i).toString();
						 BPTreeIndexFileServlet.treemap.get(root).get(fileName).remove(temp);
						 System.out.println("索引更新成功");
						 return;
					 }
				 }
			 }
			 BPTreeIndexFileServlet.treemap.get(root).get(fileName).clear();
			 //测试删除文件是否成功
			 treeList = (ArrayList<String>) BPTreeIndexFileServlet.treemap.get(root).get(fileName);
			 BPTreeIndexFileServlet.treemap.get(root).remove(fileName);
			 System.out.println("索引更新成功");
/*			 try {
				 if(rootPath.contains("C")){
					 if(cn%15==0){
						 BPTreeIndexFileServlet.treemap.put(root, fileListService.constructTree(root, "true")); 
					 }
					 cn++;
				 }else{
					 BPTreeIndexFileServlet.treemap.put(root, fileListService.constructTree(root, "true"));
				 }
//				 BPTreeIndexFileServlet.treeAll = fileListService.constructAllTree("true");
				System.out.println("动态更新索引成功" + cn );
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			 
		}
	}
	//文件修改事件不用进行索引更新 因为只是文件内容发生了变化
	public void fileModified(int wd, String rootPath, String name) {
		  System.err.println(jnotifyPath + "the modified file path is " + rootPath  + name);		  
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
