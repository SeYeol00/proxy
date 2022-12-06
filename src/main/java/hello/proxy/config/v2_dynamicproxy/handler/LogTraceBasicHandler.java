package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class LogTraceBasicHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
