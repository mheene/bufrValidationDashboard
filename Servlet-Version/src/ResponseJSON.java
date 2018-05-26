import java.util.ArrayList;
import java.util.List;

public class ResponseJSON {
    
    private String bufrviewerVersion = "1.0";
    
    private String fileName = null;
    private long fileSizeInBytes = 0;
    private String md5CheckSum = null;
    private long messageCounter = 0;
    private boolean errors = true;
    private long decodingTimeInMilliSeconds = 0;
    private ArrayList<ErrorJSON> encounteredErrorsInMessagesArray = new ArrayList<ErrorJSON>();
    
    public ResponseJSON() {
	super();
    }

    public void setFileName (String p_fileName) {
	this.fileName = p_fileName;
    }

    public void setMd5CheckSum (String p_md5) {
	this.md5CheckSum = p_md5;
    }
    
    public String getFileName() {
	return this.fileName ;
    }
    
    public String getMd5CheckSum() {
	return this.md5CheckSum;
    }

    public String getBufrviewerVersion() {
	return bufrviewerVersion;
    }

    public void setFileSizeInBytes (long p_fileSizeInBytes) {
	this.fileSizeInBytes = p_fileSizeInBytes;
    }
    
    public long getFileSizeInBytes() {
	return this.fileSizeInBytes;
    }

    public void setMessageCounter (long p_messageCounter) {
	this.messageCounter = p_messageCounter;
    }

    public long getMessageCounter() {
	return this.messageCounter;
    }
    
    public boolean hasErrors() {
	return this.errors;
    }

    public void setErrors(boolean p_errors) {
	this.errors = p_errors;
    }

    public void setDecodingTimeInMilliSeconds( long p_decodingTimeInMilliSeconds) {
	this.decodingTimeInMilliSeconds = p_decodingTimeInMilliSeconds;
    }

    public long getDecodingTimeInMilliSeconds() {
	return this.decodingTimeInMilliSeconds;
    }

    public List<ErrorJSON> getEncounteredErrorsInMessagesArray() {
	return this.encounteredErrorsInMessagesArray;
    }

    public void addError(ErrorJSON p_errorJson) {
	this.encounteredErrorsInMessagesArray.add(p_errorJson);
    }


    
}
