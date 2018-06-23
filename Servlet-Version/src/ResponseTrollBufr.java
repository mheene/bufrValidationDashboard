import java.util.List;
import java.util.Iterator;

public class ResponseTrollBufr {

	public String bufr;
	public String error;
	public String heading;
	public int index;
	public boolean status;

	public ResponseTrollBufr (String bufr, String error, String heading, int index, boolean status) {
	    this.bufr = bufr;
	    this.error = error;
	    this.heading = heading;
	    this.index = index;
	    this.status = status;
	}

	public void setBufr(String bufr) {
	    this.bufr = bufr;
	}
	
	public String getBufr() {
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
	
	
