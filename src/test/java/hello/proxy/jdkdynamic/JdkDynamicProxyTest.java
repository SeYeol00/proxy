package hello.proxy.jdkdynamic;


import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA(){
        AInterface target = new AImpl();

        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // 자바에서 만들어둔 프록시 클래스, 아래 매서드로 동적 프록시를 생성할 수 있다.
        // 타입 강제변환도 사용 가능하다.
         AInterface proxy = (AInterface) Proxy.newProxyInstance(
                // 어느 클래스 로더에 할지
                AInterface.class.getClassLoader(),
                // 어떤 인터페이스를 기반으로 만들지
                new Class[]{AInterface.class},
                // 프록시가 사용할 로직
                handler);

         proxy.call();
         log.info("targetClass = {}", target.getClass());
         log.info("proxyClass = {}", proxy.getClass());

    }


    @Test
    void dynamicB(){
        BInterface target = new BImpl();

        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // 자바에서 만들어둔 프록시 클래스, 아래 매서드로 동적 프록시를 생성할 수 있다.
        // 타입 강제변환도 사용 가능하다.
        BInterface proxy = (BInterface) Proxy.newProxyInstance(
                // 어느 클래스 로더에 할지
                BInterface.class.getClassLoader(),
                // 어떤 인터페이스를 기반으로 만들지
                new Class[]{BInterface.class},
                // 프록시가 사용할 로직
                handler);

        proxy.call();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());

    }
}
