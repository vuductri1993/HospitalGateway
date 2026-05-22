package vn.trivd.hospitalgateway.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.trivd.hospitalgateway.auth.dto.ResponseObject;
import vn.trivd.hospitalgateway.enums.CommonStatus;

@ControllerAdvice
@Slf4j
public class GatewayException {
    @ExceptionHandler(APIException.class)
    @ResponseBody
    public ResponseObject handleAPIException(APIException ex, HttpServletRequest request) {
        ResponseObject response = new ResponseObject();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        StringBuilder sb = new StringBuilder(StringUtils.isEmpty(ex.getMessage()) ? "" : ex.getMessage());
        for (int i = 0; i< stackTrace.length; i++) {
            StackTraceElement s = stackTrace[i];
            sb.append("\n").append(String.format("\tat %s.%s(%s:%d)", s.getClassName(), s.getMethodName(), s.getFileName(), s.getLineNumber()));
            if (i == 3) {
                break;
            }
        }
        String stackTraceString = sb.toString();
        log.error("API Exception: {} - {} - {}", ex.getCode(), ex.getMessage(), stackTraceString);
        response.setStatus(CommonStatus.UNSUCCESS.getCode());
        response.setMessage(ex.getMessage());
        return response;
    }
}
