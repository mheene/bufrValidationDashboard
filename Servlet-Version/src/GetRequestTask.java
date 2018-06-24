import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

public class GetRequestTask implements IfcRequestTask {

    private GetRequestWork work;
    private FutureTask<String> task;
    private String decoder;
    
    public GetRequestTask( String decoder, String url, DefaultProxyRoutePlanner routePlanner, ExecutorService executor) {
	this.decoder = decoder;
	this.work = new GetRequestWork(url, routePlanner);
	this.task = new FutureTask<String>(work);
	executor.execute(this.task);
    }

    public String getRequest() {
	return this.work.getUrl();
    }

    public boolean isDone() {
	return this.task.isDone();
    }

    public String getDecoder() {
	return this.decoder;
    }
	
    public long getResponseTime() {
	return this.work.getResponseTime();
    }
    
    public String getResponse() {
	try {
	    return this.task.get();
	} catch(Exception e) {
	    throw new RuntimeException(e);
	}
    }
}
