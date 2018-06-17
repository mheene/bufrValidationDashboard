import java.util.concurrent.Callable;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

public class GetRequestWork implements Callable<String> {
    private final String url;
    private final DefaultProxyRoutePlanner routePlanner;
    
    public GetRequestWork(String url, DefaultProxyRoutePlanner routePlanner) {
	this.url = url;
	this.routePlanner = routePlanner;
    }

    public String getUrl() {
	return this.url;
    }

    public String call() throws Exception {
	CloseableHttpClient httpclient;
	if (this.routePlanner != null) {
	    httpclient = HttpClients.custom()
                .setRoutePlanner(this.routePlanner)
                .build();
	} else {
	    httpclient = HttpClients.createDefault();
	}                                                                                                               
	return httpclient.execute(new HttpGet(getUrl()), new BasicResponseHandler());
    }
}
	
