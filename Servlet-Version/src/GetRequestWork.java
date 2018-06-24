import java.util.concurrent.Callable;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

public class GetRequestWork implements Callable<String> {
    private final String url;
    private final DefaultProxyRoutePlanner routePlanner;
    private long responseTime = -1;
    
    public GetRequestWork(String url, DefaultProxyRoutePlanner routePlanner) {
	this.url = url;
	this.routePlanner = routePlanner;
    }

    public String getUrl() {
	return this.url;
    }

    public long getResponseTime() {
	return this.responseTime;
    }

    public String call() throws Exception {
	CloseableHttpClient httpclient;
	long startTime = System.currentTimeMillis();
	if (this.routePlanner != null) {
	    httpclient = HttpClients.custom()
                .setRoutePlanner(this.routePlanner)
                .build();
	} else {
	    httpclient = HttpClients.createDefault();
	}

	long endTime = System.currentTimeMillis();
	this.responseTime = (endTime - startTime);
	System.out.println("URL: " + getUrl() + " : " + this.responseTime + " ms"); 

	return httpclient.execute(new HttpGet(getUrl()), new BasicResponseHandler());
    }
}
	
