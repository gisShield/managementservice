package me.nvliu.management.web.aop;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:mvp
 * @ProjectName:
 * @PacketName: me.nvliu.management.web.aop
 * @Description: 日志aop类 主要用于收集用户操作记录
 * @Date:
 * @Modified By:
 */

@Component
//@Aspect
public class WebLogAspect {
    private static  final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
    /*@Autowired
    private LogService logService;
    private Map logMap = new HashMap();

    @Pointcut("execution(public * me.nvliu.management.web.api.*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBeforeTask(JoinPoint joinPoint) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取请求的信息
        String remoteAddr = request.getRemoteAddr();
        String method = request.getMethod(); // 请求方式
        String uri = request.getRequestURI(); //
        String userAgent = request.getHeader("User-Agent");//用户浏览器
        String args = Arrays.toString(joinPoint.getArgs()); // 请求参数
        String userid="";
        if(uri.endsWith("top") || uri.endsWith("hpage")  || uri.endsWith("hinfo") || uri.endsWith("hlist") ||  uri.endsWith("customization") ||  uri.endsWith("music")){
            return;
        }
        User user = (User) request.getSession().getAttribute(Const.SESSION_USER);
        if (user!=null){
            userid = String.valueOf(user.getName());
        }
        if (uri.endsWith("/user/api/login")){
            userid = JSONObject.parseObject(JSON.toJSONString(joinPoint.getArgs()[0])).getString("name");
            args = "[{name="+userid+"}]";
        }else if(uri.endsWith("/user/api/add") ||   uri.endsWith("/user/api/repwd")){
            // 判断 如果有密码选项 进行过滤
            JSONObject jsono = JSONObject.parseObject(JSON.toJSONString(joinPoint.getArgs()[0]));
            if (jsono.containsKey("pwd")){
                jsono.remove("pwd");
            }
            if (jsono.containsKey("oldpwd")){
                jsono.remove("oldpwd");
            }
            if (jsono.containsKey("newpwd")){
                jsono.remove("newpwd");
            }
            args = jsono.toJSONString();
        }else if(uri.endsWith("/doc/api/add") ||   uri.endsWith("/doc/api/edit")) {
            JSONObject jsono = JSONObject.parseObject(JSON.toJSONString(joinPoint.getArgs()[0]));
            if (jsono.containsKey("text")){
                jsono.remove("text");
            }
            args = jsono.toJSONString();
        }else {

        }

        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(); // 请求的类

        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        Map map =new HashMap();
        map.put("id", UUIDTool.getUUID());
        map.put("username", userid);
        map.put("ip", ip);
        map.put("uri", uri);
        map.put("method", method);
        map.put("userAgent", userAgent);
        map.put("classMethod", classMethod);
        map.put("params", args);
        map.put("reqTime", new Date());
        logService.addLog(map);
        logMap.clear();
    }*/
}
