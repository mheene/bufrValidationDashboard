import java.util.List;
import java.util.Iterator;

@SuppressWarnings("rawtypes")
public class ResponseTrollBufr {

	public List bufr;
	public String error;
	public String heading;
	public int index;
	public boolean status;


	public ResponseTrollBufr (List bufr, String error, String heading, int index, boolean status) {
	    this.bufr = bufr;
	    this.error = error;
	    this.heading = heading;
	    this.index = index;
	    this.status = status;
	}

	public void setBufr(List bufr) {
	    this.bufr = bufr;
	}
	
	public List getBufr() {
	    return this.bufr;
	}

	public void setError(String error) {
	    this.error = error;
	}

	public String getError() {
	    return this.error;
	}

	public void setHeading (String heading) {
	    this.heading = heading;
	}

	public String getHeading() {
	    return this.heading;
	}

	public void setIndex (int index) {
	    this.index = index;
	}
	
	public int getIndex() {
	    return this.index;
	}

	public void setError(boolean status) {
	    this.status = ! status;
	}

	public boolean hasError() {
	    return ! this.status;
	}

}
	
	
