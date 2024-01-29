package org.example.exception;


import java.io.Serializable;

/**
 * @author xiwei
 * @date 2023/5/2 22:17
 */
public interface IResultCode extends Serializable {
    String getMessage();

    int getCode();
}
