package hello.proxy.jdkdynamic.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


// 인보케이션 핸들러 -> jdk 동적 프록시 사용 관련해서 제공하는 인터페이스
// 구현체를 만들어서 사용한다.
@RequiredArgsConstructor
@Slf4j // 동적 프록시는 인터페이스가 필수라 인터페이스를 사용하는 패턴에만 사용이 가능하다.
public class TimeInvocationHandler implements InvocationHandler {

    private final Object target;


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        // method 로직
        Object result = method.invoke(target,args);

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime = {}", resultTime);

        return result;
    }
}
