package hello.proxy.config.v2_dynamicproxy;


import hello.proxy.app.v1.*;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceBasicHandler;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceFilterHandler;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyFilterConfig {

    private static final String[] PATTERNS = {"request*","order*","save*"};

    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace logTrace){

        // 이게 프록시의 타겟
        OrderControllerV1 orderController = new OrderControllerV1Impl(orderServiceV1(logTrace));

        // 클래스 로더라는 곳에 객체가 생성되면 올라가기 때문에 지정을 해주어야한다.
        // 여기서 빈 등록할 때 프록시 객체를 생성하는 것이다.
        OrderControllerV1 proxy =
                (OrderControllerV1) Proxy.newProxyInstance(
                        // 어느 클래스 로더에 할지
                        OrderControllerV1.class.getClassLoader(),
                        // 어떤 인터페이스를 기반으로 만들지
                        new Class[]{OrderControllerV1.class},
                        // 프록시가 사용할 로직
                        new LogTraceFilterHandler(orderController,logTrace,PATTERNS));

        return proxy;
    }



    @Bean
    public OrderServiceV1 orderServiceV1(LogTrace logTrace){
        // 이게 타겟
        OrderServiceV1 orderService = new OrderServiceV1Impl(orderRepositoryV1(logTrace));

        // 여기서 빈 등록할 때 프록시 객체를 생성하는 것이다.
        OrderServiceV1 proxy =
                (OrderServiceV1) Proxy.newProxyInstance(
                        // 어느 클래스 로더에 할지
                        OrderServiceV1.class.getClassLoader(),
                        // 어떤 인터페이스를 기반으로 만들지
                        new Class[]{OrderServiceV1.class},
                        // 프록시가 사용할 로직
                        new LogTraceFilterHandler(orderService,logTrace,PATTERNS));

        return proxy;
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace){
        // 이게 프록시의 타겟
        OrderRepositoryV1 orderRepository = new OrderRepositoryV1Impl();

        // 여기서 빈 등록할 때 프록시 객체를 생성하는 것이다.
        OrderRepositoryV1 proxy =
                (OrderRepositoryV1) Proxy.newProxyInstance(
                // 어느 클래스 로더에 할지
                OrderRepositoryV1.class.getClassLoader(),
                // 어떤 인터페이스를 기반으로 만들지
                new Class[]{OrderRepositoryV1.class},
                // 프록시가 사용할 로직
                new LogTraceFilterHandler(orderRepository,logTrace,PATTERNS));

        return proxy;
    }
}
