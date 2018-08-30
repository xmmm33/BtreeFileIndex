package my.IndexServlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;

import net.sf.json.JSONArray;

public class ShowDiskNameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ShowDiskNameServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		File[] diskRoots = File.listRoots();
		List<String> diskRootsList = new ArrayList<String>();
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		for (File temp : diskRoots) {
			String diskType = fileSystemView.getSystemTypeDescription(temp);  
			if (diskType.indexOf("驱动器") > -1)
				continue;
			else
				diskRootsList.add(temp.getPath());
		}
		String json = JSONArray.fromObject(diskRootsList).toString();
		response.getWriter().write(json);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
