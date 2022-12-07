package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;


@Slf4j
public class CglibTest {

    @Test
    void cglib(){
        // 실제 객체 프록시 말고
        ConcreteService target = new ConcreteService();

        // cglib를 사용할 땐 인핸서를 사용해야한다.\
        // cglib를 만드는 클래스
        Enhancer enhancer = new Enhancer();
        // target을 지정해주자, 부모클래스를 지정해주는 것이다.
        enhancer.setSuperclass(ConcreteService.class);
        // 동적 프록시 클래스를 지정해야한다.
        enhancer.setCallback(new TimeMethodInterceptor(target));
        // object로 나오니까 형변환
        ConcreteService proxy = (ConcreteService) enhancer.create();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        proxy.call();
    }
}
