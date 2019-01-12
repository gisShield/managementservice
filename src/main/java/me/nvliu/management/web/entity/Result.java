package me.nvliu.management.web.entity;

import java.io.Serializable;

/**
 * 统一返回结果
 * @author mvp
 */
public class Result<T> implements Serializable {
    private T data;
    private String msg;
    private int code;
    private String jwtToken;

    public Result() {
        super();
    }

    public Result(ErrorCode code) {
        this.msg = code.getMsg();
        this.code = code.getCode();
    }

    public Result(T data, ErrorCode code) {
        this.data = data;
        this.msg = code.getMsg();
        this.code = code.getCode();
    }

    public Result(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public String toString() {
        return "Result{" +
                "data=" + data +
                ", msg='" + msg + '\'' +
                ", code=" + code +
                ", jwtToken='" + jwtToken + '\'' +
                '}';
    }

    public enum ErrorCode{
        SYSTEM_ERROR(6601,"系统异常"),
        BAD_REQUEST(6602,"错误的请求参数"),
        NOT_FOUND(6603,"找不到请求路径！"),
        CONNECTION_ERROR(6604,"网络连接请求失败！"),
        METHOD_NOT_ALLOWED(6605,"不合法的请求方式"),
        DATABASE_ERROR(6606,"数据库异常"),
        BOUND_STATEMENT_NOT_FOUNT(6607,"找不到方法！"),
        NO_USER_EXIST(6607,"用户不存在"),
        INVALID_PASSWORD(6608,"密码错误"),
        NO_PERMISSION(6609,"非法请求！"),
        SUCCESS_OPTION(6610,"操作成功！"),

        NOT_MATCH(6611,"用户名和密码不匹配"),
        FAIL_GETDATA(6612,"获取信息失败"),
        BAD_REQUEST_TYPE(6613,"错误的请求类型"),
        NO_RECORD(6614,"没有查到相关记录"),
        LOGIN_SUCCESS(6615,"登陆成功"),
        LOGOUT_SUCCESS(6616,"已退出登录"),
        SENDEMAIL_SUCCESS(6617,"邮件已发送，请注意查收"),
        EDITPWD_SUCCESS(6618,"修改密码成功"),
        No_FileSELECT(6619,"未选择文件"),
        FILEUPLOAD_SUCCESS(6620,"上传成功"),
        NOLOGIN(6621,"未登陆"),
        ILLEGAL_ARGUMENT(6622,"参数不合法"),
        ERROR_IDCODE(6623,"验证码不正确"),
        FAIL_OPTION(6624,"操作失败！");
        private int code;
        private String msg;

        ErrorCode(int code, String msg) {
            this.code = code;
            this.msg = msg;
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
    }
}

