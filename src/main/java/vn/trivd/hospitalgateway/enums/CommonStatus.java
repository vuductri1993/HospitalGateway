package vn.trivd.hospitalgateway.enums;

import org.apache.commons.lang3.StringUtils;

public enum CommonStatus {
    SUCCESS(200,"Thành công"),
    UNSUCCESS(400,"Thất bại"),;
    private String message;
    private int code;

    CommonStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    public String getMessageByCode(int code) {
        for (CommonStatus status : CommonStatus.values()) {
            if (status.getCode() == code) {
                return status.getMessage();
            }
        }
        return StringUtils.EMPTY;
    }
}
