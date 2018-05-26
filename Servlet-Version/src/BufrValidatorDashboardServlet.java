import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;


import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;

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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.commons.text.StringEscapeUtils;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    public static final String ECCODES_URL = "http://apps.ecmwf.int/codes/bufr/validator/";
    public static final String DWD_URL = "https://kunden.dwd.de/bufrviewer/uploadFile";
    // DWD JSON Service - not yet released
    //public static final String DWD_URL = "https://kunden.dwd.de/bufrviewer/validatorFile";
    public static final String PYBUFRKIT_URL = "https://z07g0b8s50.execute-api.ap-southeast-2.amazonaws.com/dev/decodeFile";
    public static final Pattern PATTERN_ECMWF = Pattern.compile("https\\://stream\\.ecmwf\\.int.*json");

    public static final String GLOBUS = "BUFR Tools (DWD)";
    public static final String ECCODES = "ecCodes (ECMWF)";
    public static final String PYBUFRKIT = "PyBufrKit";


    public static final HashMap<String, String> DECODER_MAP;
    
    static {

	DECODER_MAP = new HashMap<String, String>();
	DECODER_MAP.put(GLOBUS, "https://kunden.dwd.de/bufrviewer");
	DECODER_MAP.put(ECCODES, ECCODES_URL);
	DECODER_MAP.put(PYBUFRKIT,  "http://aws-bufr-webapp.s3-website-ap-southeast-2.amazonaws.com");
    }

    private Executor executor;	
    private DefaultProxyRoutePlanner routePlanner = null;

    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	System.out.println("Init: call");

	String proxyHost = getServletConfig().getInitParameter("proxyHost");
	String proxyPort = getServletConfig().getInitParameter("proxyPort");

	System.out.println("ProxyHost: " + proxyHost);
	System.out.println("ProxyPort: " + proxyPort);

	if (proxyHost != null) {
	    try {
		HttpHost proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
		routePlanner = new DefaultProxyRoutePlanner(proxy);
	    } catch (NumberFormatException nfe) {
		System.err.println(nfe.toString());
		throw new ServletException(nfe.toString());
	    }
	}


    }

    

    /**
     * Upon receiving file upload submission, parses the request to read
     * upload data and saves the file on disk.
     */
    protected void doPost(HttpServletRequest request,
			  HttpServletResponse response) throws ServletException, IOException {
        // checks if the request actually contains upload file
	// integrated error.jsp 
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
	    String fileName = null;
	    long fileSize = -1;
	    String md5ChkSum = null;
            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        fileName = item.getName();
			fileSize = item.getSize();
			if (Boolean.valueOf(getServletConfig().getInitParameter("storeFiles"))) {
			    String filePath = uploadPath + File.separator + fileName;
			    File storeFile = new File(filePath);
			    // saves the file on disk; old files with same filename are overwritten
			    item.write(storeFile);
			}
			//process bufr
			InputStream is = item.getInputStream();
						
			File tempFile = File.createTempFile("prefix-", "-suffix");
			tempFile.deleteOnExit();
			FileOutputStream out = new FileOutputStream(tempFile);
			IOUtils.copy(is, out);
			out.flush();
				
			md5ChkSum = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
			StringBuffer sb = new StringBuffer();
			HashMap <String, String> responseMap = new HashMap<String, String>();
			executor = Executors.newFixedThreadPool(4);
						

			List<IfcRequestTask> tasks = new ArrayList<IfcRequestTask>();
			//List<IfcRequestTask> tasks = new CopyOnWriteArrayList<IfcRequestTask>();
			tasks.add(new PostRequestTask(ECCODES, ECCODES_URL, fileName, "filebox", tempFile, this.executor));
			tasks.add(new PostRequestTask(GLOBUS, DWD_URL, fileName, "uploadFile",tempFile, this.executor));
			tasks.add(new PostRequestTask(PYBUFRKIT, PYBUFRKIT_URL, fileName, "file",tempFile, this.executor));
			//now wait for all async tasks to complete
			while(!tasks.isEmpty()) {

			    String url = null;
			    boolean foundUrl = false;
			    for(Iterator<IfcRequestTask> it = tasks.iterator(); it.hasNext();) {

				IfcRequestTask task = it.next();
				if(task.isDone()) {
				    String p_request = task.getRequest();
				    String p_response = task.getResponse();
				    Matcher m = PATTERN_ECMWF.matcher(p_response);
				    //System.out.println("Matcher ECMWF: " + m.group());
				    sb.append("Request: " + p_request + " Decoder: " + task.getDecoder() + "\n");
				    //sb.append("Response: " + p_response + "\n");
				    if (m.find()) {
					url = m.group();
					foundUrl = true;
					//sb.append("Matcher ECMWF: " + m.group());
				    } else {
					responseMap.put(task.getDecoder(),p_response);
				    }
				    
				    it.remove();
				}
			    }
			    //avoid tight loop in "main" thread
			    if(!tasks.isEmpty()) Thread.sleep(100);
			    if (foundUrl) {
				tasks.add(new GetRequestTask(ECCODES, url, this.executor));
				Thread.sleep(100);
			    }
			}
			//now you have all responses for all async requests

			Result result = new Result(fileName, fileSize, md5ChkSum, 1);
			result = processResponse(result, responseMap);
			request.setAttribute("bufr", result);

						
						
		    }
		}
	    }
            

        } catch (Exception ex) {
	    
	    System.out.println("ex: 2 " + ex.getMessage());
	    System.out.println("ex: 2 " + ex.getClass().getName());
	    getServletContext().getRequestDispatcher("/error").forward(
									   request, response);
	    return;
	}

	getServletContext().getRequestDispatcher("/upload").forward(
									request, response);

    }


    public Result processResponse (Result p_result, HashMap<String,String> p_mapResponse) {
	Result result = p_result;
	Gson gson = new Gson();
	String errorMesg = null;
	
	String globusResponse = p_mapResponse.get(GLOBUS);
	
	// DWD JSON Service - not yet published
	/* 
	ResponseJSON rdwd = gson.fromJson(globusResponse, ResponseJSON.class);
	if(rdwd !=null) {
	    result.setMessages(rdwd.getMessageCounter());
	}
	
	if (rdwd != null && rdwd.hasErrors()) {
	    List<ErrorJSON> errors = rdwd.getEncounteredErrorsInMessagesArray();
	    StringBuffer sb = new StringBuffer();
	    for (ErrorJSON e : errors) {
		sb.append(e.getMessageID() + ": " + e.getErrorText());
		sb.append("\n");
	    }
	    result.addDecoderResult(GLOBUS, false, sb.toString());
	} else {
	    result.addDecoderResult(GLOBUS, true, null);
	}
	    
	*/
	//System.out.println("GlobusResponse: " + globusResponse);
	
	// Parsing DWD HTML for now
	if (globusResponse.contains("BUFR error")) {
	    result.addDecoderResult(GLOBUS, false, "Error: xxx");
	} else {
	    result.addDecoderResult(GLOBUS, true, null);
	}


	String ecCodesResponse = p_mapResponse.get(ECCODES);

	if (ecCodesResponse == null || ecCodesResponse.contains("Error")) {
	    result.addDecoderResult(ECCODES, false, "Error");
	    System.out.println("ecCodesResponse: " + ecCodesResponse);
	} else {
	    result.addDecoderResult(ECCODES, true, null);
	}

	String pybufrKitResponse = p_mapResponse.get(PYBUFRKIT);

	if (pybufrKitResponse.contains("\"status\": \"error\"")) {

	   ResponsePyBufrKit rpy = gson.fromJson(pybufrKitResponse, ResponsePyBufrKit.class);
	   if(rpy != null) {
	       errorMesg = rpy.getMessage();
	   }
	   
	    result.addDecoderResult(PYBUFRKIT, false, errorMesg);
	    System.out.println("pybufrkitResponse: " + pybufrKitResponse);
	} else {
	    result.addDecoderResult(PYBUFRKIT, true, null);
	}   
	return result;
    }

    class ResponsePyBufrKit {
	public String status = null;
	public String message = null;
	
	public ResponsePyBufrKit () {
	    super();
	}

	public ResponsePyBufrKit (String status, String message) {
	    this.status = status;
	    this.message = message;
	}
	
	public void setStatus(String  status) {
	    this.status = status;
	}
	
	public void setMessage(String message) {
	    this.message = message;
	}
	
	public String getStatus() {
	    return this.status;
	}
	
	public String getMessage() {
	    return this.message;
	}
    }
    
    interface IfcRequestTask {
	public String getRequest();
	public boolean isDone();
	public String getResponse();
	public String getDecoder();
    }
	
    class GetRequestTask implements IfcRequestTask {
        private GetRequestWork work;
        private FutureTask<String> task;
	private String decoder;
        public GetRequestTask( String decoder, String url, Executor executor) {
	    this.decoder = decoder;
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

	public String getDecoder() {
	    return this.decoder;
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
	    CloseableHttpClient httpclient;
	    if (routePlanner != null) {
		httpclient = HttpClients.custom()
                .setRoutePlanner(routePlanner)
                .build();
	    } else {
		httpclient = HttpClients.createDefault();
	    }                                                                                                               
	     return httpclient.execute(new HttpGet(getUrl()), new BasicResponseHandler());
        }
    }
	
    class PostRequestTask implements IfcRequestTask{
        private PostRequestWork work;
        private FutureTask<String> task;
	private String decoder;
	
        public PostRequestTask(String decoder, String url, String p_fileName, String p_fieldName, File p_file, Executor executor) {
            this.work = new PostRequestWork(url, p_fileName, p_fieldName, p_file);
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

	    CloseableHttpClient httpclient;
	    if (routePlanner != null) {
		httpclient = HttpClients.custom()
                .setRoutePlanner(routePlanner)
                .build();
	    } else {
		httpclient = HttpClients.createDefault();
	    }                                                                                                               

	    String returnValue = "";
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
	    return returnValue;
        }
    }
}
