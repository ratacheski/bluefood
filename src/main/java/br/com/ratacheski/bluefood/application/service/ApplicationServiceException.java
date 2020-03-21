package br.com.ratacheski.bluefood.application.service;

public class ApplicationServiceException extends RuntimeException {
    public ApplicationServiceException(String message) {
        super(message);
    }
    public ApplicationServiceException(Throwable cause) {
        super(cause);
    }
}
