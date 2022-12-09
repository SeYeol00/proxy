package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanPostProcessorTest {

    @Test
    void basicConfig() {

        // @Import와 같은 역할을 하는 인터페이스
        // 스프링 컨테이너를 쓰지 않고 수동 빈 등록을 하고 싶을 때 사용
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);

        // beanA 이름으로 B 객체가 빈으로 등록된다.
        B b = applicationContext.getBean("beanA",B.class);
        b.helloB();

        // A는 빈으로 등록되지 않는다.
        Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                // 콜백 함수, 람다 사용
                // 람다: 인자로 넣을 함수를 매개변수에서 정의하는 방법
                () -> applicationContext.getBean(A.class));
        // 예외가 성공한다.
    }



    @Slf4j
    @Configuration
    static class BeanPostProcessorConfig{

        @Bean(name = "beanA")
        public A a(){
            return new A();
        }

        @Bean
        public AToBPostProcessor helloProcessor(){
            return new AToBPostProcessor();
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

    // 빈 후 처리기 구현체, BeanPostProcessor를 implements 받아야한다.
    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor{

        // 빈 생성 후 처리 매서드
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("beanName = {} bean = {}", beanName, bean);

            // 빈이 A라면 B로 바꿔치기 하겠다.
            if(bean instanceof A){
                return new B();
            }
            return bean;
        }
    }

}
