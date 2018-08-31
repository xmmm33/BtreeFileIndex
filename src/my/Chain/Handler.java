package my.Chain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.attribute.AclEntry.Builder;
import java.util.ArrayList;

public abstract class Handler {
	protected Handler next;
    //设置下一级责任链
    public void setSuccessor(Handler handler){
        this.next=handler;
    }
    
    public Handler getNext() {
        return this.next;
    }
    public abstract ArrayList<String> requestFilename(String filename,String vague, String index,String root,String type);
}
