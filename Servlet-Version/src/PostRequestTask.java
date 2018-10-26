import java.io.File;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

public class PostRequestTask implements IfcRequestTask{

    private PostRequestWork work;
    private FutureTask<String> task;
    private String decoder;
	
    public PostRequestTask(String decoder, String url, String p_fileName, String p_fieldName, File p_file, ExecutorService executor, DefaultProxyRoutePlanner routePlanner) {
	this.work = new PostRequestWork(url, p_fileName, p_fieldName, p_file, routePlanner);
	this.task = new FutureTask<String>(work);
	this.decoder = decoder;
	executor.execute(this.task);
    }

    public String getDecoder() {
	return this.decoder;
    }
	
    public String getRequest() {
	return this.work.getUrl();
    }
    
    public boolean isDone() {
	return this.task.isDone();
    }

    public long getResponseTime() {
	return this.work.getResponseTime();
    }

    public String getResponse() {
	try {
	    return this.task.get();
	} catch(Exception e) {
	    System.out.println("Decoder: " + this.getDecoder() + " throws an error.");
	    System.out.println("Ex: " + e.toString());
	    return "";
	    //throw new RuntimeException(e);
	}
    }
}
