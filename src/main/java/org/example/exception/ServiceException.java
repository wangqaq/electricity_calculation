package org.example.exception;



/**
 * @author xiwei
 * @date 2023/5/2 22:08
 */
public class ServiceException extends RuntimeException {

    private final IResultCode resultCode;

    public ServiceException(String message) {
        super(message);
        this.resultCode = ResultCode.FAILURE;
    }

    public ServiceException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

}
