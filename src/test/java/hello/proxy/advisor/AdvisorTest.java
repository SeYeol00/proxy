package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;

@Slf4j
public class AdvisorTest {

    @Test
    void advisorTest1(){
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        // 기본으로 쓰이는 어드바이저, 이걸 쓰면 포인트 컷 옵션과, 어드바이스를 넣어야 한다.
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface)proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }


    @Test
    @DisplayName("직접 만든 포인트 컷")
    void advisorTest2(){
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        // 기본으로 쓰이는 어드바이저, 이걸 쓰면 포인트 컷 옵션과, 어드바이스를 넣어야 한다. 생성자를 통해서 생성한다.
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointcut(), new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface)proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    @Test
    @DisplayName("스프링이 제공하는 포인트 컷")
    void advisorTest3(){
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        //스프링이 제공하는 포인트 컷
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("save");
        // 기본으로 쓰이는 어드바이저, 이걸 쓰면 포인트 컷 옵션과, 어드바이스를 넣어야 한다.
        // 스프링에서 제공하는 포인트 컷을 넣어줬다.
        // 여기서는 find에 타임 프록시가 적용이 안 됐다.
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface)proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }


    //
    static class MyPointcut implements Pointcut{

        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            // 내가 만든 매서드 매처 사용하기
            return new MyMethodMatcher();
        }
    }

    static class MyMethodMatcher implements MethodMatcher{

        private String matchName = "save";

        // 직접 구현한 매서드 매처, 맞는지 불린 값을 찾는다.
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            boolean result = method.getName().equals(matchName);
            log.info("포인트 컷 호출 method = {}, targetClass = {}", method.getName(),targetClass);
            log.info("포인트 컷 결과 result = {}", result);
            return result;
        }

        // 런타임이 false인 경우 정적정보만 사용한다.
        @Override
        public boolean isRuntime() {
            return false;
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return false;
        }
    }
}
