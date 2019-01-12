package me.nvliu.management.web.exception;

import me.nvliu.management.web.entity.Result;
import org.apache.ibatis.binding.BindingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.net.BindException;
import java.net.ConnectException;
import java.sql.SQLException;

@ControllerAdvice
public class RestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * 请求参数类型错误异常的捕获
     * @param e
     * @return
     */
    @ExceptionHandler(value={BindException.class})
    @ResponseBody
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public Result<String> badRequest(BindException e){
        logger.error("occurs error when execute method ,message {}",e.getMessage());
        return new Result<>(Result.ErrorCode.BAD_REQUEST);
    }

    /**
     * 404错误异常的捕获
     * @param e
     * @return
     */
    @ExceptionHandler(value={NoHandlerFoundException.class})
    @ResponseBody
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public Result<String> badRequestNotFound(BindException e){
        logger.error("occurs error when execute method ,message {}",e.getMessage());
        return new Result<>(Result.ErrorCode.NOT_FOUND);
    }

    /**
     * mybatis未绑定异常
     * @param e
     * @return
     */
    @ExceptionHandler(BindingException.class)
    @ResponseBody
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> mybatis(Exception e){
        logger.error("occurs error when execute method ,message {}",e.getMessage());
        return new Result<>(Result.ErrorCode.BOUND_STATEMENT_NOT_FOUNT);
    }
    /**
     * 自定义异常的捕获
     * 自定义抛出异常。统一的在这里捕获返回JSON格式的友好提示。
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value={MyRuntimeException.class})
    @ResponseBody
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public <T> Result<T> sendError(MyRuntimeException exception,HttpServletRequest request){
        String requestURI = request.getRequestURI();
        logger.error("occurs error when execute url ={} ,message {}",requestURI,exception.getMsg());
        return new Result<>(exception.getMsg(),exception.getCode());
    }
    /**
     * 数据库操作出现异常
     * @param e
     * @return
     */
    @ExceptionHandler(value={SQLException.class,DataAccessException.class})
    @ResponseBody
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> systemError(Exception e){
        logger.error("occurs error when execute method ,message {}",e.getMessage());
        return new Result<>( Result.ErrorCode.DATABASE_ERROR);
    }
    /**
     * 网络连接失败！
     * @param e
     * @return
     */
    @ExceptionHandler(value={ConnectException.class})
    @ResponseBody
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> connect(Exception e){
        logger.error("occurs error when execute method ,message {}",e.getMessage());
        return new Result<>(Result.ErrorCode.CONNECTION_ERROR);
    }

    @ExceptionHandler(value={Exception.class})
    @ResponseBody
    @ResponseStatus(value=HttpStatus.METHOD_NOT_ALLOWED)
    public Result<String> notAllowed(Exception e){
        logger.error("occurs error when execute method ,message {}",e.getMessage());
        return new Result<>(Result.ErrorCode.METHOD_NOT_ALLOWED);
    }
}
