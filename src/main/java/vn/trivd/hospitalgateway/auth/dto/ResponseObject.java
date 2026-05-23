package vn.trivd.hospitalgateway.auth.dto;

public class ResponseObject<T> {
    private String message;
    private int status;
    private T data;

    public ResponseObject() {
    }

    public ResponseObject(String message, int status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
