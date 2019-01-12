package me.nvliu.management.web.exception;

import me.nvliu.management.web.entity.Result;

public class MyRuntimeException extends RuntimeException {
    protected int code;
    protected String msg;
    protected String message;

    public MyRuntimeException() {
        super();
    }

    public MyRuntimeException(Result.ErrorCode enums,String message) {
        super();
        this.code = enums.getCode();
        this.msg = enums.getMsg();
        this.message = message;
    }
    public MyRuntimeException(Result.ErrorCode enums) {
        super();
        this.code = enums.getCode();
        this.msg = enums.getMsg();
    }

    public MyRuntimeException(String message) {
        super(message);
    }

    public MyRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
