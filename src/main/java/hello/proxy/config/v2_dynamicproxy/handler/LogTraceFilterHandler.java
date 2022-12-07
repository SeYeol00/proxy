package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;

    private final String[] patterns;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 매서드 이름 필터
        String methodName = method.getName();

        // save, request, reque*, *est
        // PatternMatchUtils를 사용하면 매칭 로직을 쉽게 적용할 수 있다.
        // xxx, *xxx, xxx*, *xxx*
        if(!PatternMatchUtils.simpleMatch(patterns,methodName)){
            return method.invoke(target,args);
        }


        TraceStatus status = null;
        try{
            // 이렇게 공통로직으로 만들면 프록시를 템플릿처럼 하나 만들어두고 쓰는 거다.
            // 하나의 프록시를 여러 객체에 적용이 가능하다.
            String message = method.getDeclaringClass()
                    .getSimpleName()
                    + "." + method.getName()
                    + "()";
            status = logTrace.begin(message);

            // 로직 호출
            Object result = method.invoke(target,args);
            logTrace.end(status);
            return result;
        }catch (Exception e){
            logTrace.exception(status,e);
            throw e;
        }
    }
}
