public class ErrorJSON {
    private long messageID = 0;
    private String errorText = "";
    
    public ErrorJSON() {
	super();
    }
    
    public ErrorJSON(long p_messageID, String p_errorText) {
	this.messageID = p_messageID;
	this.errorText = p_errorText;
    }

    public void setMessageID( long p_messageID) {
	this.messageID = p_messageID;
    }

    public long getMessageID() {
	return this.messageID;
    }

    public void setErrorText (String p_errorText) {
	this.errorText = p_errorText;
    }

    public String getErrorText() {
	return this.errorText;
    }
}
