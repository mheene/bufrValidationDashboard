public class GenericResponse {

  private String error;
  private boolean status;

  public GenericResponse(String error, boolean status) {
    this.error = error;
    this.status = status;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getError() {
    return this.error;
  }

  public void setError(boolean status) {
    this.status = !status;
  }

  public boolean hasError() {
    return !this.status;
  }
}
