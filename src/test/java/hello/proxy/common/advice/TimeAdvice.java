package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

// 프록시 팩토리에 담는 어드바이스
// AOP에서 사용하는 그 어드바이스와 개념이 같다.
@Slf4j
public class TimeAdvice implements MethodInterceptor {

    // 여기서는 타겟을 주입받지 않아도 된다.
    // 프록시 팩토리에서 자동으로 생성해서 넣어줌

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        // 인보케이션 프로시드로 도출한다.
        Object result = invocation.proceed();


        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime = {}", resultTime);

        return result;
    }




}
