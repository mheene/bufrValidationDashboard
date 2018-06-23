public  class ResponsePyBufrKit implements IfcResponse {

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
    
