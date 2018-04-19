package com.jackhalfalltrades.foundation.exception;

import com.jackhalfalltrades.foundation.utils.FoundationConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;
import java.util.Arrays;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FoundationClientException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -985346194351074231L;

    private Object[] params;

    public FoundationClientException(String messageKey, Object[] params) {
        super(StringUtils.isBlank(messageKey) ? FoundationConstants.RUNTIMEEXCEPTION_MSG_KEY : messageKey);
        this.params = params == null ? null : params.clone();
    }

    public FoundationClientException(String message, Throwable cause) {
        super(message, cause);
        this.params = null;
    }

    public FoundationClientException(String message) {
        super(message);
        this.params = null;
    }

    public Object[] getParams() {
        if (params != null) {
            return Arrays.copyOf(params, params.length);
        } else {
            return new Object[0];
        }
    }
}