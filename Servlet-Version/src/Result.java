import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

@ManagedBean(name = "result")
@SessionScoped
public class Result {

    public Result() {
    }

    private String fileName = "";
    private long fileSize = -1;
    private String md5CheckSum = "";
    private long messages = -1;
    private boolean present = true;

    public List<DecoderResult> decoderResults = new ArrayList<DecoderResult>();

    public Result(String fileName, long fileSize, String md5CheckSum, long messages) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.md5CheckSum = md5CheckSum;
        this.messages = messages;
    }


    public String getFileName() {
        if (fileName == null) {
            fileName = "";
        }
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
        if (md5CheckSum == null) {
            md5CheckSum = "";
        }
        return this.md5CheckSum;
    }

    public List<DecoderResult> getDecoderResults() {
        return this.decoderResults;

    }

    public void addDecoderResult(String decoder, boolean status, String error) {
        decoderResults.add(new DecoderResult(decoder, status, error));
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        fileName = event.getFile().getFileName();
        fileSize = event.getFile().getSize();

        File file = new File(System.getProperty("java.io.tmpdir"));
        if (!file.exists()) {
            file.mkdir();
        }
        InputStream fis = null;
        OutputStream out = null;
        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        try {
            fis = event.getFile().getInputstream();
            File tempFile = File.createTempFile("prefix-", "-suffix");
            tempFile.deleteOnExit();
            out = new FileOutputStream(tempFile);
            IOUtils.copy(fis, out);
            out.flush();
        }catch (Exception ex){
            System.out.println("ex: 2 " + ex.getMessage());
            System.out.println("ex: 2 " + ex.getClass().getName());
        }
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
