package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BasicTest {

    @Test
    void basicConfig() {

        // @Import와 같은 역할을 하는 인터페이스
        // 스프링 컨테이너를 쓰지 않고 수동 빈 등록을 하고 싶을 때 사용
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicConfig.class);

        // A는 빈으로 등록된다.
        A a = applicationContext.getBean("beanA",A.class);
        a.helloA();

        // B는 빈으로 등록되지 않는다.
        Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                // 콜백 함수, 람다 사용
                // 람다: 인자로 넣을 함수를 매개변수에서 정의하는 방법
                () -> applicationContext.getBean(B.class));
        // 예외가 성공한다.
    }



    @Slf4j
    @Configuration
    static class BasicConfig{

        @Bean(name = "beanA")
        public A a(){
            return new A();
        }
    }

    @Slf4j
    static class A{
        public void helloA(){
            log.info("hello A");
        }
    }

    @Slf4j
    static class B{
        public void helloB(){
            log.info("hello B");
        }
    }
}
