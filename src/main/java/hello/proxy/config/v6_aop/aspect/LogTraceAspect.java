package hello.proxy.config.v6_aop.aspect;


import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;

// 어노테이션 기반 프록시 적용
@Slf4j
@Aspect // 어드바이저(포인트컷과 어드바이스)를 편리하게 생성할 수 있게 하는 어노테이션 핵심!!
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    // 어드바이저
    // 포인트컷을 지정하는 어노테이션 -> @Around
    @Around("execution(* hello.proxy.app..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable{
        // 이 함수 안에 어드바이스 로직이 들어간다.
        TraceStatus status = null;
        try{
            // joinPoint도 중요, 알아두자
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            // 로직 호출, joinPoint.proceed()만 하면 된다.
            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        }catch (Exception e){
            logTrace.exception(status,e);
            throw e;
        }
    }
}
