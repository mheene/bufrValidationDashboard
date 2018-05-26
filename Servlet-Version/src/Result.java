import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Result {
    public String fileName = "";
    public long fileSize = -1;
    public String md5CheckSum = "";
    public long messages = -1;
    public boolean present = true;

    public List<DecoderResult> decoderResults = new ArrayList<DecoderResult>();

    public Result(String fileName, long fileSize, String md5CheckSum, long messages) {
	this.fileName = fileName;
	this.fileSize = fileSize;
	this.md5CheckSum = md5CheckSum;
	this.messages = messages;
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

    public boolean isPresent() {
	return this.present;
    }

    public String getMd5CheckSum() {
	return this.md5CheckSum;
    }

    public List<DecoderResult> getDecoderResults() {
	return this.decoderResults;
    }
    
    public void addDecoderResult(String decoder, boolean status, String error) {
	decoderResults.add(new DecoderResult(decoder, status, error));
    }

    public class DecoderResult {

	public String decoder;
	public boolean status;
	public String error = null;

	public HashMap<String, String> DECODER_MAP = BufrValidatorDashboardServlet.DECODER_MAP;
	
	public DecoderResult(String decoder, boolean status, String error) {
	    this.decoder = decoder;
	    this.status = status;
	    this.error = error;
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

	public String getUrl() {
	    return DECODER_MAP.get(this.decoder);
	}

    }
}
