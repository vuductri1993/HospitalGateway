package vn.trivd.hospitalgateway.exceptions;

import vn.trivd.hospitalgateway.enums.CommonStatus;

public class APIException extends RuntimeException {
    public static final long serialVersionUID = 6312618584181807836L;
    public int code;
    public String message;
    public Object data;

    public APIException(int code, final String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public APIException(int code, final String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public APIException(CommonStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
