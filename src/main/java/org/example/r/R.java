package org.example.r;


import lombok.Data;
import org.example.exception.ResultCode;

/**
 * @author xiwei
 * @date 2023/5/3 8:47
 */

@Data
public class R<T> {
    private int code;
    private String message;
    private T data;
    private long timestamp;


    public R() {
        this.timestamp = System.currentTimeMillis();
    }


    public static <T> R<T> success(T data) {
        R<T> resultData = new R<>();
        resultData.setCode(ResultCode.SUCCESS.getCode());
        resultData.setMessage(ResultCode.SUCCESS.getMessage());
        resultData.setData(data);
        return resultData;
    }

    public static <T> R<T> fail(int code, String message) {
        R<T> resultData = new R<>();
        resultData.setCode(code);
        resultData.setMessage(message);
        return resultData;
    }

    public static <T> R<T> fail(ResultCode resultCode) {
        R<T> resultData = new R<>();
        resultData.setCode(resultCode.getCode());
        resultData.setMessage(resultCode.getMessage());
        return resultData;
    }



    public static <T> R<T> fail(String message) {
        R<T> resultData = new R<>();
        resultData.setCode(ResultCode.FAILURE.getCode());
        resultData.setMessage(message);
        return resultData;
    }


}