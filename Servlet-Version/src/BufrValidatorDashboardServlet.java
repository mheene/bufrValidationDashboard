import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
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



import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.commons.text.StringEscapeUtils;

import org.apache.commons.io.IOUtils;

/**
 * A Java servlet that handles file upload from client.
 *
 * @author Markus Heene
 */
public class BufrValidatorDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
     
    // location to store file uploaded
    private static final String UPLOAD_DIRECTORY = "upload";
 
    // upload settings
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 10;  // 10 MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 10; // 10 MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 20; // 20 MB
 
    private Executor executor;	
 
    /**
     * Upon receiving file upload submission, parses the request to read
     * upload data and saves the file on disk.
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // checks if the request actually contains upload file
	// error.jsp noch einbauen
        if (!ServletFileUpload.isMultipartContent(request)) {
            // if not, we stop here
            PrintWriter writer = response.getWriter();
            writer.println("Error: Form must has enctype=multipart/form-data.");
            writer.flush();
            return;
        }
 
	ServletFileUpload upload = null;
	String uploadPath = null;
	File uploadDir = null;

	// configures upload settings
	DiskFileItemFactory factory = new DiskFileItemFactory();
	// sets memory threshold - beyond which files are stored in disk
	factory.setSizeThreshold(MEMORY_THRESHOLD);
	// sets temporary location to store files
	//System.out.println("tmpdir: " + System.getProperty("java.io.tmpdir"));
	factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
	
	upload = new ServletFileUpload(factory);
        
	// sets maximum size of upload file
	upload.setFileSizeMax(MAX_FILE_SIZE);
        
	// sets maximum size of request (include file + form data)
	upload.setSizeMax(MAX_REQUEST_SIZE);
	
	// constructs the directory path to store upload file
	// this path is relative to application's directory
	uploadPath = getServletContext().getRealPath("")
	    + File.separator + UPLOAD_DIRECTORY;
         
	// creates the directory if it does not exist
	if (Boolean.valueOf(getServletConfig().getInitParameter("storeFiles"))) {
		uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
		    uploadDir.mkdir();
		}
	}

 
	try {
            // parses the request's content to extract file data
            //@SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);
 
            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
						if (Boolean.valueOf(getServletConfig().getInitParameter("storeFiles"))) {
							String filePath = uploadPath + File.separator + fileName;
							File storeFile = new File(filePath);
							// saves the file on disk; old files with same filename are overwritten
							item.write(storeFile);
						}
						//process bufr
						InputStream is = item.getInputStream();
						
						File tempFile = File.createTempFile("prefix-", "-suffix");
						//tempFile.deleteOnExit();
						FileOutputStream out = new FileOutputStream(tempFile);
						IOUtils.copy(is, out);
						out.flush();
						
						StringBuffer sb = new StringBuffer();
						executor = Executors.newFixedThreadPool(4);
						
						//List<GetRequestTask> tasks = new ArrayList<GetRequestTask>();
						List<IfcRequestTask> tasks = new ArrayList<IfcRequestTask>();
						//tasks.add(new GetRequestTask("http://localhost:8080/examples/jsp/jsp2/el/basic-arithmetic.jsp", this.executor));
						//tasks.add(new GetRequestTask("http://localhost:8080/examples/jsp/jsp2/el/basic-comparisons.jsp", this.executor));
						tasks.add(new PostRequestTask("http://apps.ecmwf.int/codes/bufr/validator/", fileName, "filebox", tempFile, this.executor));
						tasks.add(new PostRequestTask("https://kunden.dwd.de/bufrviewer/uploadFile", fileName, "uploadFile",tempFile, this.executor));
						//...
						//do other work here
						//...
						//now wait for all async tasks to complete
						while(!tasks.isEmpty()) {
							//for(Iterator<GetRequestTask> it = tasks.iterator(); it.hasNext();) {
							for(Iterator<IfcRequestTask> it = tasks.iterator(); it.hasNext();) {
								//GetRequestTask task = it.next();
								IfcRequestTask task = it.next();
								if(task.isDone()) {
									String p_request = task.getRequest();
									String p_response = task.getResponse();
									//PUT YOUR CODE HERE
									//possibly aggregate request and response in Map<String,String>
									//or do something else with request and response
									sb.append("Request: " + p_request + "\n");
									sb.append("Response: " + p_response + "\n");
									it.remove();
								}
							}
							//avoid tight loop in "main" thread
							if(!tasks.isEmpty()) Thread.sleep(100);
						}
						//now you have all responses for all async requests

						//the following from your original code
						//note: you should probably pass the responses from above
						//to this next method (to keep your controller stateless)
						//String results = doWorkwithMultipleDataReturned();
						request.setAttribute("bufr", sb.toString()); 
						//model.addAttribute(results, results);
						//return "index";

						
						
					}
				}
			}
            

        } catch (Exception ex) {
	    
			System.out.println("ex: 2 " + ex.getMessage());
			System.out.println("ex: 2 " + ex.getClass().getName());
			getServletContext().getRequestDispatcher("/error.jsp").forward(
                request, response);
				return;
	    }
		
		


	getServletContext().getRequestDispatcher("/upload.jsp").forward(
                request, response);


    }
	
	interface IfcRequestTask {
		public String getRequest();
		public boolean isDone();
		public String getResponse();
	}
	
	class GetRequestTask implements IfcRequestTask {
        private GetRequestWork work;
        private FutureTask<String> task;
        public GetRequestTask(String url, Executor executor) {
            this.work = new GetRequestWork(url);
            this.task = new FutureTask<String>(work);
            executor.execute(this.task);
        }
        public String getRequest() {
            return this.work.getUrl();
        }
        public boolean isDone() {
            return this.task.isDone();
        }
        public String getResponse() {
            try {
                return this.task.get();
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Callable representing actual HTTP GET request
    class GetRequestWork implements Callable<String> {
        private final String url;
        public GetRequestWork(String url) {
            this.url = url;
        }
        public String getUrl() {
            return this.url;
        }
        public String call() throws Exception {
            return new DefaultHttpClient().execute(new HttpGet(getUrl()), new BasicResponseHandler());
        }
    }
	
	class PostRequestTask implements IfcRequestTask{
        private PostRequestWork work;
        private FutureTask<String> task;
		
        public PostRequestTask(String url, String p_fileName, String p_fieldName, File p_file, Executor executor) {
            this.work = new PostRequestWork(url, p_fileName, p_fieldName, p_file);
            this.task = new FutureTask<String>(work);
            executor.execute(this.task);
        }
        public String getRequest() {
            return this.work.getUrl();
        }
        public boolean isDone() {
            return this.task.isDone();
        }
        public String getResponse() {
            try {
                return this.task.get();
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
	
	//Callable representing actual HTTP POST request
    class PostRequestWork implements Callable<String> {
        private final String url;
		private final String fileName;
		private final String fieldName;
		private File file;
		
		/*
        public PostRequestWork(String url) {
            this.url = url;
        }
		*/
		
		public PostRequestWork(String url, String fileName, String fieldName, File file) {
            this.url = url;
			this.fileName = fileName;
			this.fieldName = fieldName;
			this.file = file;
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
		
        public String call() throws Exception {
			
			CloseableHttpClient httpclient = HttpClients.createDefault();
			String returnValue = "";
			try {
				HttpPost httppost = new HttpPost(getUrl());

			/*		
			File tempFile = File.createTempFile("prefix-", "-suffix");
			//tempFile.deleteOnExit();
			FileOutputStream out = new FileOutputStream(tempFile);
			IOUtils.copy(getInputStream(), out);
			out.flush();
			*/
			FileBody bin = new FileBody(getFile());
			//FileBody bin = new FileBody(new File("c:\\Users\\mheene\\Documents\\project\\bufrdashboard\\test\\2bufr.bin"));
			// filebox
			HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart(getFieldName(), bin)          
                    .build();
			//FileBody bin = new FileBody(new File(args[0]));
            //StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
			/*
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addBinaryBody("filebox", getInputStream())          
                    .build();
			*/
            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
					String responseString = EntityUtils.toString(resEntity, "UTF-8");
					System.out.println(responseString);
					returnValue = responseString;
					
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
		return returnValue;
		/*	
		HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(this.getUrl());
			//String textFileName = null;
			//File file = new File(textFileName);
			InputStream ios = getInputStream();
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addBinaryBody("filebox", ios, ContentType.MULTIPART_FORM_DATA, getFileName());
			
			// 
			HttpEntity entity = builder.build();
			post.setEntity(entity);
			HttpResponse response = client.execute(post);	
			return response.toString();
		*/	
        }
    }
}
