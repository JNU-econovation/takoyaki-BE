package com.bestbenefits.takoyaki.util.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Aspect
public class LoggingAspect {

    @Pointcut("execution(* com.bestbenefits.takoyaki.service..*.*(..))")
    public void loggingPointCutAtService() {}

    @Pointcut("within(com.bestbenefits.takoyaki.controller..*)")
    public void loggingPointCutAtController(){}

    @Around("loggingPointCutAtController()")
    public Object loggingForController(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String controllerName = joinPoint.getSignature().getDeclaringType().getName();
        String methodName = joinPoint.getSignature().getName();
        Map<String, Object> params = new HashMap<>();

        try {
            String decodedURI = URLDecoder.decode(request.getRequestURI(), "UTF-8");

            params.put("controller", controllerName);
            params.put("method", methodName);
            params.put("params", getParams(request));
            params.put("log_time", System.currentTimeMillis());
            params.put("request_uri", decodedURI);
            params.put("http_method", request.getMethod());
        } catch (Exception e) {
            log.error("LoggerAspect error", e);
        }
        log.info("------------------------------------");
        log.info("[Called Controller] [{}] {}", params.get("http_method"), params.get("request_uri"));
        log.info("method: {}.{}", params.get("controller") ,params.get("method"));
        log.info("params: {}", params.get("params"));

        Object proceed = joinPoint.proceed();
        log.info("");

        return proceed;
    }

    @Before(value = "loggingPointCutAtService()")
    public void beforeService(JoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        // 메서드 정보 받아오기

        log.info(">>>> Called {}.{}({})", method.getDeclaringClass().getSimpleName(), method.getName(), getMethodParams(joinPoint));
    }

    // Poincut에 의해 필터링된 경로로 들어오는 경우 메서드 리턴 후에 적용
    @AfterReturning(value = "loggingPointCutAtService()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        Method method = getMethod(joinPoint);
        log.info("<<<< Returned {}", returnObj.getClass().getSimpleName());
    }

    private static JSONObject getParams(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replaceAll("\\.", "-");
            jsonObject.put(replaceParam, request.getParameter(param));
        }
        return jsonObject;
    }

    private static Method getMethod(JoinPoint jp) {
        return ((MethodSignature)jp.getSignature()).getMethod();
    }

    private static String getMethodParams(JoinPoint joinPoint) {
        List<String> typeName = Arrays.stream(getMethod(joinPoint).getParameters())
                .map(Parameter::getName).toList();
        Object[] args = joinPoint.getArgs();

        StringBuilder sb = new StringBuilder(" ");
        for (int i = 0; i < typeName.size(); i++) {
            sb.append(typeName.get(i)).append("(").append(args[i] == null ? "null" : args[i]).append("); ");
        }
        return sb.toString();
    }
}
