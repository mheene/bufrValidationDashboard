import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Result {
    public String fileName = "";
    public long fileSize = -1;
    public String md5CheckSum = "";
    public long messages = -1;
    public boolean present = true;
    public long overallResponseTime = -1;

    public List<DecoderResult> decoderResults = new ArrayList<DecoderResult>();

    public Result(String fileName, long fileSize, String md5CheckSum, long messages, long overallResponseTime) {
	this.fileName = fileName;
	this.fileSize = fileSize;
	this.md5CheckSum = md5CheckSum;
	this.messages = messages;
	this.overallResponseTime = overallResponseTime;
    }


    public String getFileName() {
	return this.fileName;
    }

    public long getFileSize() {
	return this.fileSize;
    }

    public void setMessages(long num) {
	this.messages = num;
    }

    public long getMessages() {
	return this.messages;
    }

    public long getOverallResponseTime() {
	return this.overallResponseTime;
    }
    
    public void setOverallResponseTime(long ortime) {
	this.overallResponseTime= ortime;
    }

    public boolean isPresent() {
	return this.present;
    }

    public String getMd5CheckSum() {
	return this.md5CheckSum;
    }

    public List<DecoderResult> getDecoderResults() {
	return this.decoderResults;
    }

   
    public void addDecoderResult(String decoder, boolean status, String error, long responseTime) {
	decoderResults.add(new DecoderResult(decoder, status, error, responseTime));
    }

    public String toString() {
	String returnValue = "File: " + this.fileName + " contains " + this.messages + " messages\n\n" ;
	for (int i = 0; i < this.decoderResults.size() ; i++) {
	    returnValue = returnValue + this.decoderResults.get(i) + "\n";
	}
	return returnValue;
	
    }
    
    public class DecoderResult {

	public String decoder;
	public boolean status;
	public String error = null;
	public long responseTime = -1;
	
	public transient HashMap<String, String> DECODER_MAP = BufrValidatorDashboardServlet.DECODER_MAP;
	
	public DecoderResult(String decoder, boolean status, String error, long responseTime) {
	    this.decoder = decoder;
	    this.status = status;
	    this.error = error;
	    this.responseTime = responseTime;
	}

	public String getDecoder() {
	    return this.decoder;
	}
	public boolean isStatus() {
	    return this.status;
	}

	public String getError() {
	    return this.error;
	}

	public long getResponseTime() {
	    return this.responseTime;
	}

	public String getUrl() {
	    return DECODER_MAP.get(this.decoder);
	}

	public String toString() {
	    String returnValue = String.format("%-15s\t%5d ms\t%s\t%s", this.decoder , this.responseTime , this.status, this.error);
					       //return this.decoder + "\t" + this.responseTime + " ms\t" + this.status + "\t" + this.error;
	    return returnValue;
	}

    }
}
