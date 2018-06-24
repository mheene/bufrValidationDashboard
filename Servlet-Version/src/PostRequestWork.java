import java.io.File;
import java.io.IOException;

import java.util.concurrent.Callable;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.ContentType;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpHost;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;


public class PostRequestWork implements Callable<String> {

    private final String url;
    private final String fileName;
    private final String fieldName;
    private File file;
    private final DefaultProxyRoutePlanner routePlanner;
    private long responseTime = -1;
    
    public PostRequestWork(String url, String fileName, String fieldName, File file, DefaultProxyRoutePlanner routePlanner) {
	this.url = url;
	this.fileName = fileName;
	this.fieldName = fieldName;
	this.file = file;
	this.routePlanner = routePlanner;
    }
		
    public String getUrl() {
	return this.url;
    }
		
    public String getFileName() {
	return this.fileName;
    }
		
    public String getFieldName() {
	return this.fieldName;
    }
		
    public File getFile() {
	return this.file;
    }

    public long getResponseTime() {
	return this.responseTime;
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

	String returnValue = "";
	long startTime = System.currentTimeMillis();
	try {

	    HttpPost httppost = new HttpPost(getUrl());

	    FileBody bin = new FileBody(getFile());
	    HttpEntity reqEntity = MultipartEntityBuilder.create()
		.addPart(getFieldName(), bin)          
		.build();
	    httppost.setEntity(reqEntity);

	    System.out.println("executing request " + httppost.getRequestLine());
	    CloseableHttpResponse response = httpclient.execute(httppost);
	    try {
		//System.out.println("----------------------------------------");
		//System.out.println(response.getStatusLine());
		HttpEntity resEntity = response.getEntity();
		if (resEntity != null) {
		    //System.out.println("Response content length: " + resEntity.getContentLength());
		    String responseString = EntityUtils.toString(resEntity, "UTF-8");
		    //System.out.println(responseString);
		    returnValue = responseString;
		    
		}
		EntityUtils.consume(resEntity);
	    } finally {
		    response.close();
	    }
	} finally {
	    httpclient.close();
	}
	long endTime = System.currentTimeMillis();
	this.responseTime = (endTime - startTime);
	System.out.println("URL: " + getUrl() + " : " + this.responseTime + " ms"); 
	return returnValue;
    }
}
