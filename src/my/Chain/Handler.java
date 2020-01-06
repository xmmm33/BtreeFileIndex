package my.Chain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.attribute.AclEntry.Builder;
import java.util.ArrayList;

/**
 * 责任链设计模式
 * <p>
 * 处理逻辑操作
 * <p>
 * 提供逻辑操作处理的抽象方法 设置下一级操作者的方法
 *
 * @author hmj
 */
public abstract class Handler {
    /**
     * 责任下一级处理人
     */
    protected Handler next;
    private String a = "ssss";

    //设置下一级责任链
    public void setSuccessor(Handler handler) {
        this.next = handler;
    }

    public Handler getNext() {
        return this.next;
    }

    public abstract ArrayList<String> requestFilename(String filename, String vague, String index, String root, String type);
}
