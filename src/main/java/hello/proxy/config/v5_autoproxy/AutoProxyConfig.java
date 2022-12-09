package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

    // 스프링 빈 자동 후 처리기는 이 어드바이저만 등록해주면 끝난다.
    // 빈 포스트 프로세서는 이미 스프링 컨테이너에 올라와 있다.
    // 궁극적으로 스프링 빈 후처리기를 사용하면 우리는 어드바이저만 등록해주면 된다는 것이다.

    // 중요, 포인트컷은 2가지에 사용된다.
    // 1. 프록시 적용 여부 판단 - 생성 단계
    // 2. 어드바이스 적용 여부 판단 - 사용단계


    //@Bean
    public Advisor advisor1(LogTrace logTrace) {
        // pointcut -> 자동 처리기가 이 포인트컷을 보고 알아서 클래스 찾는다.
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*","order*","save*");
        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        // 어드바이저는 하나의 포인트컷과 하나의 어드바이스를 가진다.
        return new DefaultPointcutAdvisor(pointcut,advice);
    }

   //@Bean
    public Advisor advisor2(LogTrace logTrace) {
        // pointcut -> 자동 처리기가 이 포인트컷을 보고 알아서 클래스 찾는다.
        // 실무에서 사용하는 포인트컷, 표현식을 알아두자
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        // 이 위치에만 어드바이저를 적용시켜라, 문법은 아래를 참고하자
        // 다만 이 표현은 패키지 위치를 기준으로 하기 때문에 no-log에도 적용이 된다.
        pointcut.setExpression("execution(* hello.proxy.app..*(..))");
        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        // 어드바이저는 하나의 포인트컷과 하나의 어드바이스를 가진다.
        return new DefaultPointcutAdvisor(pointcut,advice);
    }

    @Bean
    public Advisor advisor3(LogTrace logTrace) {
        // pointcut -> 자동 처리기가 이 포인트컷을 보고 알아서 클래스 찾는다.
        // 실무에서 사용하는 포인트컷, 표현식을 알아두자
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        // 이 위치에만 어드바이저를 적용시켜라, 문법은 아래를 참고하자
        // no-log는 제외시킨 표현식 
        pointcut.setExpression("execution(* hello.proxy.app..*(..))" +
                "&& !execution(* hello.proxy.app..noLog(..))");
        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        // 어드바이저는 하나의 포인트컷과 하나의 어드바이스를 가진다.
        return new DefaultPointcutAdvisor(pointcut,advice);
    }
}



