package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy(){
        // 인터페이스를 사용할 때
        ServiceInterface target = new ServiceImpl();
        // 프록시팩토리 사용법, 타겟을 이 때 넣어준다.
        // 여기서 타겟을 넣어주면 팩토리가
        // 알아서 이게 인터페이스를 쓰는지
        // 혹은 구체 클래스인지
        // 판별을 다 해준다.
        ProxyFactory proxyFactory = new ProxyFactory(target);
        // 어드바이스 등록
        // -> 어드바이스는 인보케이핸들러 매서드 인터셉터의 부모 인터페이스
        // 이러면 엄청 편리하다.
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        proxy.save();

        // 프록시팩토리를 통해 프록시를 만들었을 때 확인이 가능하다.
        // AopUtils를 통해서 검증할 수 있다.
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();

        // jdk 동적 프록시인지 검증도 가능하다.
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();

        // cglib 프록시인지 검증도 가능하다.
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();

    }

    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용")
    void concreteProxy(){
        // 인터페이스를 사용할 때
        ConcreteService target = new ConcreteService();
        // 프록시팩토리 사용법, 타겟을 이 때 넣어준다.
        // 여기서 타겟을 넣어주면 팩토리가
        // 알아서 이게 인터페이스를 쓰는지
        // 혹은 구체 클래스인지
        // 판별을 다 해준다.
        ProxyFactory proxyFactory = new ProxyFactory(target);
        // 어드바이스 등록
        // -> 어드바이스는 인보케이핸들러 매서드 인터셉터의 부모 인터페이스
        // 이러면 엄청 편리하다.
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        proxy.call();

        // 프록시팩토리를 통해 프록시를 만들었을 때 확인이 가능하다.
        // AopUtils를 통해서 검증할 수 있다.
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();

        // jdk 동적 프록시인지 검증도 가능하다.
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();

        // cglib 프록시인지 검증도 가능하다.
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();

    }


    @Test
    @DisplayName("ProxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB를 사용하고, 클래스 기반 프록시 사용")
    void proxyTargetClass(){
        // 인터페이스를 사용할 때
        ServiceInterface target = new ServiceImpl();
        // 프록시팩토리 사용법, 타겟을 이 때 넣어준다.
        // 여기서 타겟을 넣어주면 팩토리가
        // 알아서 이게 인터페이스를 쓰는지
        // 혹은 구체 클래스인지
        // 판별을 다 해준다.
        ProxyFactory proxyFactory = new ProxyFactory(target);
        // 인터페이스를 쓰더라도 CGLIB를 사용하고 싶을 때 주는 옵션
        // 중요하다 실무에서 종종 쓸 때가 있다.
        proxyFactory.setProxyTargetClass(true);

        // 어드바이스 등록
        // -> 어드바이스는 인보케이핸들러 매서드 인터셉터의 부모 인터페이스
        // 이러면 엄청 편리하다.
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        proxy.save();

        // 프록시팩토리를 통해 프록시를 만들었을 때 확인이 가능하다.
        // AopUtils를 통해서 검증할 수 있다.
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();

        // jdk 동적 프록시인지 검증도 가능하다.
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();

        // cglib 프록시인지 검증도 가능하다.
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();

    }
}
