package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    @Test
    void reflection0(){
        Hello target = new Hello();

        // 공통 로직1 시작
        log.info("start");
        String result1 = target.callA(); // 호출하는 메서드가 다름
        log.info("result = {}", result1);
        // 공통 로직1 종료


        // 공통 로직2 시작
        log.info("start");
        String result2 = target.callB(); // 호출하는 메서드가 다름
        log.info("result = {}", result2);
        // 공통 로직2 종료
    }

    @Test
    void reflection1() throws Exception{
        // 클래스 정보
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();
        // callA 매서드 정보
        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target);
        log.info("result1 = {}", result1);

        // callB 매서드 정보
        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2 = {}", result2);
    }




    // 동적 호출을 이용하여 추상화하기
    // 이걸 리플렉션이라고 부른다.

    // 공통 처리로직 핵심
    private void dynamicCall(Method method, Object target) throws Exception{
        log.info("start");
        // 호출하는 메서드가 다른 부분을 추상화 시켜서 함수로 만든 것
        // 중요, 동적 호출이란 방식이다.
        Object result = method.invoke(target);
        log.info("result = {}", result);
    }

    @Test
    void reflection2() throws Exception{
        // 클래스 정보
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();
        // 매서드라는 메타정보를 통해서 호출할 매서드 정보가 동적으로 제공된다.
        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA,target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB,target);
    }


    @Slf4j
    static class Hello{

        public String callA(){
            log.info("callA");
            return "A";
        }

        public String callB(){
            log.info("callB");
            return "B";
        }


    }
}
