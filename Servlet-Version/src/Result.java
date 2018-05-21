import java.util.ArrayList;
import java.util.List;

public class Result {
    public String fileName = "";
    public long fileSize = -1;
    public String md5CheckSum = "";
    public int messages = -1;
    public List<DecoderResult> decoderResults = new ArrayList<DecoderResult>();

    public Result(String fileName, long fileSize, String md5CheckSum, int messages) {
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

    public int getMessages() {
	return this.messages;
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
    }
}
