package com.jackhalfalltrades.foundation.exception.handler;

import com.jackhalfalltrades.foundation.exception.BadRequestException;
import com.jackhalfalltrades.foundation.exception.InternalServerErrorException;
import com.jackhalfalltrades.foundation.exception.ResourceNotFoundException;
import com.jackhalfalltrades.foundation.model.ErrorInfo;
import com.jackhalfalltrades.foundation.utils.FoundationConstants;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler implements MessageSourceAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(HystrixTimeoutException.class)
    @ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
    @ResponseBody
    public ErrorInfo hystrixTimeoutException(HttpServletRequest req, HystrixTimeoutException ex) {
        LOGGER.error(StringEscapeUtils.escapeJava("[Hystrix Exception] " + ex.getMessage()), ex);
        String errorMessage = messageSource.getMessage(FoundationConstants.HYSTRIX_TIMEOUT_EXCEPTION, new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        String errorURL = req.getRequestURL().toString();
        return new ErrorInfo(errorMessage, HttpStatus.REQUEST_TIMEOUT.toString());
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorInfo timeoutException(HttpServletRequest req, Exception ex) {
        LOGGER.error("[Internal ESP Server Error] " + ex.getMessage());
        LOGGER.error(ExceptionUtils.getFullStackTrace(ex));

        String errorMessage = messageSource
                .getMessage(FoundationConstants.RUNTIMEEXCEPTION_MSG_KEY, new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        String errorURL = req.getRequestURL().toString();
        return new ErrorInfo(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorInfo generalException(HttpServletRequest req, Exception ex) {
        LOGGER.error(ExceptionUtils.getFullStackTrace(ex));

        String errorMessage = messageSource.getMessage(FoundationConstants.RUNTIMEEXCEPTION_MSG_KEY, new Object[]{ex.getClass().getName()},
                LocaleContextHolder.getLocale());
        String errorURL = req.getRequestURL().toString();
        return new ErrorInfo(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo processBadRequestError(HttpServletRequest req, BadRequestException ex) {
        LOGGER.error(StringEscapeUtils.escapeJava("[Bad Request Exception] " + ex.getMessage()), ex);

        String errorMessage = messageSource.getMessage(FoundationConstants.BADREQUESTEXCEPTION_MSG_KEY, new Object[]{ex.getMessage(), ex.getParams()}, LocaleContextHolder.getLocale());
        String errorURL = req.getRequestURL().toString();
        return new ErrorInfo(errorMessage, HttpStatus.BAD_REQUEST.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo processRequestNotReadableError(HttpServletRequest req, HttpMessageNotReadableException ex) {
        LOGGER.error(StringEscapeUtils.escapeJava("[Http Request Not Readable Exception] " + ex.getMessage()), ex);

        String errorMessage = messageSource.getMessage("requestnotreadable.exception", new Object[]{ex.getMessage()},
                LocaleContextHolder.getLocale());
        String errorURL = req.getRequestURL().toString();
        return new ErrorInfo(errorMessage, HttpStatus.BAD_REQUEST.toString());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorInfo processResourceNotFoundError(HttpServletRequest req, ResourceNotFoundException ex) {
        LOGGER.error(StringEscapeUtils.escapeJava("[Resource Not Found Exception] " + ex.getMessage()), ex);
        String errorMessage = messageSource.getMessage(FoundationConstants.RESOURCENOTFOUNDEXCEPTION_MSG_KEY, new Object[]{ex.getClass().getName(), ex.getLocalizedMessage(), ex.getParams()}, LocaleContextHolder.getLocale());
        String errorURL = req.getRequestURL().toString();
        return new ErrorInfo(errorMessage, HttpStatus.NOT_FOUND.toString());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo processValidationError(BindException ex) {
        LOGGER.error(StringEscapeUtils.escapeJava("[Validation Bind Exception] " + ex.getMessage()), ex);
        BindingResult result = ex.getBindingResult();
        String errorMessage = result.getFieldErrors().stream().map(e-> e.getField() + " : " + e.getDefaultMessage())
                .collect(Collectors.toList()).toString();
        return new ErrorInfo(errorMessage, HttpStatus.BAD_REQUEST.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo processValidationError(MethodArgumentNotValidException ex) {
        LOGGER.error(StringEscapeUtils.escapeJava("[Validation Error] " + ex.getMessage()), ex);
        BindingResult result = ex.getBindingResult();
        String errorMessage = result.getFieldErrors().stream().map(e-> e.getField() + " : " + e.getDefaultMessage())
                .collect(Collectors.toList()).toString();
        return new ErrorInfo(errorMessage, HttpStatus.BAD_REQUEST.toString());

    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

}