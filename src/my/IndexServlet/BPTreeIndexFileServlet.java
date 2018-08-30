package my.IndexServlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import my.BplusTreeModel.BplusTree;
import my.IndexFileService.FileListService;
import my.IndexFileService.FileMonitorAndFileDynamicUpdateService;
import net.sf.json.JSONArray;

public class BPTreeIndexFileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static BplusTree treeDisk; // 磁盘索引树
	public static BplusTree treeAll;// 默认不重建索引，建立全局索引b+树
	public static Map<String, BplusTree> treemap = new HashMap<>(); // 一个文件名或磁盘名对应一个b+树
	public static Map<String, BplusTree> treetempmap = new HashMap<>();

	static { // 静态初始化块
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool(); // 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
		File[] files = File.listRoots(); // 获取机器盘符 如C:\ 这个file集合里面的值为你电脑的里的所有磁盘符
		FileMonitorAndFileDynamicUpdateService[] threadGroup = new FileMonitorAndFileDynamicUpdateService[files.length];// 创建文件监控对象数组
		int i= 0;
		for (File temp : files) { // 依次建议每个磁盘的索引树
			try {
				treemap.put(temp.getPath(), new FileListService().constructTree(temp.getPath(), "false"));// 把建立的磁盘索引b+树放入treemap集合中
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			threadGroup[i] = new FileMonitorAndFileDynamicUpdateService(temp.getPath() + File.separator);
			cachedThreadPool.execute(threadGroup[i++]); // 放入线程池的execute方法执行
//			System.err.println(System.getProperty("java.library.path"));
		}
	}
	public BPTreeIndexFileServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8"); // 设置编码格式 只对post请求有效
		response.setContentType("text/html;charset=utf-8"); // 设置页面中为中文编码
		String filename = ""; // 初始化filename 防止空指针异常 下同理
		String filetype = "";
		String index = "";
		String vague = "";
		String type = "";
		if (request.getParameter("filename") != null) {
			filename = new String(request.getParameter("filename").getBytes("iso-8859-1"), "utf-8");
		}
		if (request.getParameter("filetype") != null) {
			filetype = new String(request.getParameter("filetype").getBytes("iso-8859-1"), "utf-8");
		}
		vague = request.getParameter("vague");
		index = request.getParameter("index");
		type = request.getParameter("type");

		String root = new String(request.getParameter("root").getBytes("iso-8859-1"), "utf-8"); // 是否选择了磁盘符
		String save = request.getParameter("save"); // 是否需要保存索引
		for (String r : treemap.keySet()) { // treemap里面的key值为所有磁盘符名
			if (r.indexOf(root) > -1) { // 遍历匹配用户选择的盘符
				root = r;
			}
		}
/*		if(treeDisk == null || treetempmap.get(root) ==null){
			treeDisk = treemap.get(root);
			treetempmap.put(root, treeDisk);
		}
		if(treeDisk != null && treetempmap.get(root) != null){
			treeDisk = treemap.get(root);
			treetempmap.remove(root);
		}*/
		List<String> resultList = new ArrayList<>(); // 保存向前台传输的绝对路径，封装在json传给前台

		// 搜索名称不为空
		if (!filename.isEmpty()) {
			// 模糊查询
			if (vague.equals("true")) { // 如果是模糊查询
				// 重建索引
				if (index.equals("true")) { // 如果需要重建索引
					// 磁盘目录
					if (!root.isEmpty()) {
						if (root.equals("全盘")) {
							long startTime = System.nanoTime();
							treeAll = new FileListService().constructAllTree(index); // 重新建立全局索引树
							resultList = treeAll.getOnVague(filename, treeAll);
							long endTime = System.nanoTime();
							System.out.println("查询耗时" + (endTime - startTime) + "ns");
						} else {
							long startTime = System.nanoTime();
							try {
								treeDisk = new FileListService().constructTree(root, index);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							resultList = treeDisk.getOnVague(filename, treeDisk);
							long endTime = System.nanoTime();
							System.out.println("查询耗时" + (endTime - startTime) + "ns");
						}
					}
				} else { // 不需要重建索引的时候
					if (!root.isEmpty()) {
						if (root.equals("全盘")) {
							long startTime = System.nanoTime();
							for(Map.Entry<String, BplusTree> temp : treemap.entrySet()){
								if(temp.getKey().indexOf("驱动器")> -1)continue;
								treeDisk = treemap.get(temp.getKey());
								if(treeDisk.getOnVague(filename,treeDisk)==null)continue; //防止抛空指针异常
								resultList.addAll(treeDisk.getOnVague(filename, treeDisk));
							}
//							resultList = treeAll.getOnVague(filename, treeAll); // 通过全局树种模糊查询文件路径
							long endTime = System.nanoTime();
							System.out.println("查询耗时" + (endTime - startTime) + "ns");
						} else {
							long startTime = System.nanoTime();
							treeDisk = treemap.get(root); // 得到某一磁盘树
							resultList = treeDisk.getOnVague(filename, treeDisk); // 通过某一磁盘树模糊查询得到文件路径
							long endTime = System.nanoTime();
							System.out.println("查询耗时" + (endTime - startTime) + "ns");
						}
					}
				}
			} else { // 如果没有选择模糊查询按钮时
				if (type.equals("true")) { // 如果选择了类型查询
					if (index.equals("true")) { // 如果选择了重建索引
						if (!root.isEmpty()) {
							if (root.equals("全盘")) {
								long startTime = System.nanoTime();
								treeAll = new FileListService().constructAllTree(index);// 重建全局索引树
								resultList = treeAll.getOnTypeByInput(filename, treeAll);// 查询得到文件路径
								long endTime = System.nanoTime();
								System.out.println("查询耗时" + (endTime - startTime) + "ns");
							} else {
								long startTime = System.nanoTime();
								try {
									treeDisk = new FileListService().constructTree(root, index);
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								}
								resultList = treeDisk.getOnTypeByInput(filename, treeDisk);// 查询得到文件路径
								long endTime = System.nanoTime();
								System.out.println("查询耗时" + (endTime - startTime) + "ns");
							}
						}
					} else { // 如果没有选择重建索引
						if (!root.isEmpty()) {
							if (root.equals("全盘")) {
								long startTime = System.nanoTime();
								for(Entry<String, BplusTree> temp : treemap.entrySet()){
									if(temp.getKey().indexOf("驱动器")> -1)continue;
									treeDisk = treemap.get(temp.getKey());
									if(treeDisk.getOnTypeByExist(filename,treeDisk)==null)continue;
									resultList.addAll(treeDisk.getOnTypeByInput(filename, treeDisk));
								}
//								resultList = treeAll.getOnTypeByInput(filename, treeAll); // 得到全局索引树
								long endTime = System.nanoTime();
								System.out.println("查询耗时" + (endTime - startTime) + "ns");
							} else {
								long startTime = System.nanoTime();
								treeDisk = treemap.get(root); // 得到磁盘索引树
								resultList = treeDisk.getOnTypeByInput(filename, treeDisk);
								long endTime = System.nanoTime();
								System.out.println("查询耗时" + (endTime - startTime) + "ns");
							}
						}
					}
				} else { // 如果没有选择类型查询
					if (index.equals("true")) { // 如果选择了重建索引
						if (!root.isEmpty()) { // 选择了某一确定磁盘
							if (root.equals("全盘")) {
								long startTime = System.nanoTime();
								treeAll = new FileListService().constructAllTree(index);// 重建全局树
								resultList = treeAll.get(filename);// 查询文件路径
								long endTime = System.nanoTime();
								System.out.println("查询耗时" + (endTime - startTime) + "ns");
							} else {
								long startTime = System.nanoTime();
								try {
									treeDisk = new FileListService().constructTree(root, index);
								} catch (ClassNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								resultList = treeDisk.get(filename);// 查询文件路径
								long endTime = System.nanoTime();
								System.out.println("查询耗时" + (endTime - startTime) + "ns");
							}
						}
					} else { // 没选择重建索引时
						if (!root.isEmpty()) { // 选择了某一确定磁盘
							if (root.equals("全盘")) {
								long startTime = System.nanoTime();
								for(Entry<String, BplusTree> temp : treemap.entrySet()){
									if(temp.getKey().indexOf("驱动器")> -1)continue;
									treeDisk = treemap.get(temp.getKey());
									if(treeDisk.get(filename)==null)continue;
									resultList.addAll(treeDisk.get(filename)); 
								}
//								resultList = treeAll.get(filename); // 在全局树中查询文件路径
								long endTime = System.nanoTime();
								System.out.println("查询耗时" + (endTime - startTime) + "ns");
							} else {
								long startTime = System.nanoTime();
								treeDisk = treemap.get(root); 
								resultList = treeDisk.get(filename); // 查询文件路径
								long endTime = System.nanoTime();
								System.out.println("查询耗时" + (endTime - startTime) + "ns");
							}
						}
					}
				}
			}

		}
		if (!filetype.equals("undefined") && !filetype.equals("")) { // 是否点击了四个按钮查询

			if (!root.isEmpty()) { // 选择了某一确定磁盘，默认没选重建索引
				if (root.equals("全盘")) {
					resultList = treeAll.getOnTypeByExist(filetype, treeAll);
				} else {
					treeDisk = treemap.get(root);
					resultList = treeDisk.getOnTypeByExist(filetype, treeDisk);
				}
			}
		}
		if (resultList != null) {
			Collections.sort(resultList);
		}
		if (save != null) { // 如果选择了保存索引
			if (!root.equals("全盘")) {
				for (Entry<String, BplusTree> entry : treemap.entrySet()) { // 遍历map
					FileListService fileList = new FileListService();
					char temp = entry.getKey().toString().charAt(0); // 拿到盘符名称 
					Map<String, ArrayList<String>> fileMap = FileListService.getMap(entry.getValue()); // 把全部的索引树map得到
					if (root.equals(entry.getKey())) {
						fileList.saveIndex(entry.getKey() + temp + "盘索引.txt", fileMap);//匹配到当前所选盘符就进行索引持久化
						break;
					}
				}
				resultList.add("OK");   //返回给前端的判断标志
			} else {
				for (Entry<String, BplusTree> entry : treemap.entrySet()) {
					FileListService fileListService = new FileListService();
					Map<String, ArrayList<String>> fileMap = FileListService.getMap(entry.getValue());
					fileListService.saveAllIndex("G:/全盘索引.txt", fileMap); //对这个文件追加写方式写入索引 依次存入每个盘的索引
				}
				resultList.add("OK");
			}
		}
		response.getWriter().write(JSONArray.fromObject(resultList).toString()); // 向前台传输数据

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
